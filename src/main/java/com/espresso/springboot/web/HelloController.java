package com.espresso.springboot.web;
import org.springframework.web.bind.annotation.GetMapping;
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
}
