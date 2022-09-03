package com.espresso.springboot.utils;

import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Getter
@Component

public class ServerCounter {
    private int dummyCount;

    @Async
    @Scheduled(fixedRate=1000)
    public void counter(){
        dummyCount++;
    }
}
