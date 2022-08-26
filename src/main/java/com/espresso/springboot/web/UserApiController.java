package com.espresso.springboot.web;

import com.espresso.springboot.service.user.UserService;
import com.espresso.springboot.web.dto.LocalLoginRequestDto;
import com.espresso.springboot.web.dto.LocalRegisterRequestDto;
import com.espresso.springboot.web.dto.UserNamePwdUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserService userService;

    @PostMapping("/api/v1/local-register")
    public Long register(@RequestBody LocalRegisterRequestDto localRegisterRequestDto){
        return userService.register(localRegisterRequestDto);
    }
    @PostMapping("/api/v1/local-login")
    public Long login(@RequestBody LocalLoginRequestDto localLoginRequestDto){
        return userService.login(localLoginRequestDto);
    }

    @PutMapping("/api/v1/change-pw")
    public Long changePw(@RequestBody String pw){
        userService.changePassword(pw);
        return 0L;
    }

    @PutMapping("/api/v1/change-user-info")
    public Long changeUserInfo(@RequestBody UserNamePwdUpdateRequestDto userNamePwdUpdateRequestDto){
        userService.update(userNamePwdUpdateRequestDto);
        return 0L;
    }

}
