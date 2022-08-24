package com.espresso.springboot.config.auth.dto;

import com.espresso.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String picture;

    private LocalDateTime localDateTime;
    public SessionUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.localDateTime = LocalDateTime.now();
    }
}
