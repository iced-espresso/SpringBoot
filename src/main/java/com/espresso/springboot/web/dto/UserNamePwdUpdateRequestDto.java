package com.espresso.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserNamePwdUpdateRequestDto {
    private String name;
    private String password;

    @Builder
    public UserNamePwdUpdateRequestDto(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
