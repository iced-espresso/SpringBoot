package com.espresso.springboot.web;

import com.espresso.springboot.service.localuser.LocalUserService;
import com.espresso.springboot.web.dto.LocalLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LocalUserApiController {
    private final LocalUserService localUserService;

    @PostMapping("/api/v1/local-login")
    public Long login(@RequestBody LocalLoginRequestDto localLoginRequestDto){
        return localUserService.login(localLoginRequestDto);
    }
}
