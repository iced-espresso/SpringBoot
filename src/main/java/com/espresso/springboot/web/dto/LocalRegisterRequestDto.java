package com.espresso.springboot.web.dto;

import com.espresso.springboot.domain.user.Role;
import com.espresso.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
public class LocalRegisterRequestDto {
    private String name;

    private String email;

    private String password;

    @Builder
    public LocalRegisterRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
