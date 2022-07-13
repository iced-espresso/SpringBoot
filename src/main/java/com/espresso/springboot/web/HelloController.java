package com.espresso.springboot.web;
import com.espresso.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private int myword=0;
    private String mylog = "";
    @GetMapping("/getword")
    public String getWord(){
        return Integer.toString(myword);
    }

    @GetMapping("/showlog")
    public String showLog(){
        return mylog;
    }

    @GetMapping("/clrlog")
    public void clearLog(){
        mylog = "";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") String amount) {
        mylog += name + ',' + amount + "\n";
        return new HelloResponseDto(name, Integer.parseInt(amount));
    }
}
