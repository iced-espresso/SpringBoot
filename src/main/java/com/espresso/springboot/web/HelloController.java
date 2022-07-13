package com.espresso.springboot.web;
import com.espresso.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
        private int myword=0;
        @GetMapping("/getword")
        public String getWord(){
            return Integer.toString(myword);
        }

        @GetMapping("/hello")
        public String hello(){
            myword++;
            return "hello";
        }

        @GetMapping("/hello/dto")
        public HelloResponseDto helloDto(@RequestParam("name") String name,
                                         @RequestParam("amount") int amount){
            return new HelloResponseDto(name, amount);
        }
}
