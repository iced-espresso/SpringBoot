package com.espresso.springboot.web;

import com.espresso.springboot.config.auth.dto.SessionUser;
import com.espresso.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postsService.findAllDesc());
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if (sessionUser != null){
            model.addAttribute("name", sessionUser.getName());
            model.addAttribute("loginTime", sessionUser.getLocalDateTime());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        model.addAttribute("post", postsService.findById(id));
        return "posts-update";
    }

    @GetMapping("/posts/view/{id}")
    public String postsView(@PathVariable Long id, Model model){
        model.addAttribute("post", postsService.findById(id));
        return "posts-view";
    }

    @GetMapping("/local-login")
    public String localLogin(){
        return "local-login";
    }

    @GetMapping("/local-register")
    public String localRegister(){
        return "local-register";
    }

    @GetMapping("/change-user-info")
    public String changePwd(Model model){
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if (sessionUser == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        model.addAttribute("email", sessionUser.getEmail());
        model.addAttribute("name", sessionUser.getName());

        return "change-user-info";
    }
}
