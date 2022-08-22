package com.espresso.springboot.domain.user;


import com.espresso.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String password;

    @Builder
    public User(String name, String email, String picture, Role role, String password) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.password = password;
    }

    public User update(String name, String picture, String password){
        this.name = name;
        this.picture = picture;
        this.password = password;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public User updatePwd(String newPassword) {
        this.password = newPassword;
        return this;
    }
}
