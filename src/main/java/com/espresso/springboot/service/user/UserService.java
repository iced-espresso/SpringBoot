package com.espresso.springboot.service.user;

import com.espresso.springboot.config.auth.dto.SessionUser;
import com.espresso.springboot.domain.user.User;
import com.espresso.springboot.domain.user.UserRepository;
import com.espresso.springboot.web.dto.LocalLoginRequestDto;
import com.espresso.springboot.web.dto.UserNamePwdUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    private User findByEmailAndName(LocalLoginRequestDto localLoginRequestDto){
        Optional<User> optionalUser = userRepository.findByEmail(localLoginRequestDto.getEmail());
        optionalUser.orElseThrow(() -> new IllegalArgumentException("email이 없습니다."));
        return optionalUser.get();
    }

    @Transactional(readOnly = true)
    public Long login(LocalLoginRequestDto localLoginRequestDto){
        User user = findByEmailAndName(localLoginRequestDto);
        if(!passwordEncoder.matches(localLoginRequestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다.");
        }
        SessionUser sessionUser = new SessionUser(user);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(user.getRoleKey()));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(sessionUser, null, list));
        httpSession.setAttribute("user", sessionUser);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return user.getId();
    }

    @Transactional
    public void changePassword(String newPassword) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if(sessionUser == null){
            throw new IllegalArgumentException("로그인 필요");
        }

        userRepository.findByEmail(sessionUser.getEmail())
                .map(entity -> entity.updatePwd(passwordEncoder.encode(newPassword)))
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    @Transactional
    public void update(UserNamePwdUpdateRequestDto userNamePwdUpdateRequestDto){
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if(sessionUser == null){
            throw new IllegalArgumentException("로그인 필요");
        }

        Optional<User> user = userRepository.findByEmail(sessionUser.getEmail());
        user.map(entity -> {
                    entity.update(userNamePwdUpdateRequestDto.getName(), entity.getPicture(), passwordEncoder.encode(userNamePwdUpdateRequestDto.getPassword()));
                    return entity;
                }
                ).orElseThrow(() -> new IllegalArgumentException("user를 찾을 수 없습니다."));
        httpSession.setAttribute("user", new SessionUser(user.get()));
    }
}

