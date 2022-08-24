package com.espresso.springboot.config.auth;

import com.espresso.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/local-login").permitAll()
                .antMatchers("/api/v1/local-login").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())

                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .formLogin()
                .loginPage("/local-login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }
}
