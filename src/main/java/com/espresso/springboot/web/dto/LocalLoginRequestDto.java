package com.espresso.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocalLoginRequestDto {
    private String email;
    private String name;

    @Builder
    public LocalLoginRequestDto(String email, String userName){
        this.email = email;
        this.name = userName;
    }

}
