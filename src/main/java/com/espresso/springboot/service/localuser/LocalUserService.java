package com.espresso.springboot.service.localuser;

import com.espresso.springboot.config.auth.dto.SessionUser;
import com.espresso.springboot.domain.user.User;
import com.espresso.springboot.domain.user.UserRepository;
import com.espresso.springboot.web.dto.LocalLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocalUserService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional(readOnly = true)
    private User findByEmailAndName(LocalLoginRequestDto localLoginRequestDto){
        Optional<User> optionalUser = userRepository.findByEmailAndName(localLoginRequestDto.getEmail(), localLoginRequestDto.getName());
        optionalUser.orElseThrow(() -> new IllegalArgumentException("email 또는 name이 잘못되었습니다."));
        return optionalUser.get();
    }

    @Transactional(readOnly = true)
    public Long login(LocalLoginRequestDto localLoginRequestDto){
        User User = findByEmailAndName(localLoginRequestDto);
        SessionUser sessionUser = new SessionUser(User);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(User.getRoleKey()));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(sessionUser, null, list));
        httpSession.setAttribute("user", sessionUser);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return User.getId();
    }

}

