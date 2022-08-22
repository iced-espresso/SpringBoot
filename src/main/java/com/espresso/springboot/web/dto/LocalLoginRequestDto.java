package com.espresso.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocalLoginRequestDto {
    private String email;
    private String password;

    @Builder
    public LocalLoginRequestDto(String email, String password){
        this.email = email;
        this.password = password;
    }

}
