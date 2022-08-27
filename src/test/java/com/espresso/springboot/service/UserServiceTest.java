package com.espresso.springboot.service;

import com.espresso.springboot.domain.user.User;
import com.espresso.springboot.domain.user.UserRepository;
import com.espresso.springboot.service.user.UserService;
import com.espresso.springboot.web.dto.LocalLoginRequestDto;
import com.espresso.springboot.web.dto.LocalRegisterRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.cert.TrustAnchor;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@RunWith( SpringRunner.class )
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    public void 로컬_가입_테스트(){
        // given
        String email = "admin";
        String password = "abc";
        String name = "choi";
        LocalRegisterRequestDto localLoginRequestDto
                = LocalRegisterRequestDto.builder()
                                        .email(email)
                                        .password(password)
                                        .name(name)
                                        .build();

        // when
        userService.register(localLoginRequestDto);

        // then
        Optional<User> optionalUser = userRepository.findByEmail("admin");
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        User user = optionalUser.get();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getRoleKey()).isEqualTo("ROLE_USER");
        assertThat(user.getPassword()).isNotEqualTo(password);
        System.out.println(">>>>>>>>>>>>>> password: " + user.getPassword());
        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    public void 로컬_가입_중복_방지_테스트(){
        String email = "admin";
        String password = "abc";
        String name = "choi";
        LocalRegisterRequestDto localLoginRequestDto
                = LocalRegisterRequestDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
        userService.register(localLoginRequestDto);
        assertThatIllegalArgumentException().isThrownBy(() ->{
                    userService.register(localLoginRequestDto);
                }
        );


    }

}
