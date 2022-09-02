package com.espresso.springboot.web;

import com.espresso.springboot.service.posts.PostsService;
import com.espresso.springboot.web.dto.PostsListResponseDto;
import com.espresso.springboot.web.dto.PostsResponseDto;
import com.espresso.springboot.web.dto.PostsSaveRequestDto;
import com.espresso.springboot.web.dto.PostsUpdateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        if(requestDto.getAuthor().length() < 2){
            throw new RuntimeException("author가 2글자 미만");
        }
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public void delete(@PathVariable Long id){
        postsService.delete(id);
    }

    @GetMapping("/api/v1/posts")
    public PostsResponseDto findByIdRequestParam (@RequestParam Long id) throws Exception{
        return postsService.findById(id);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable String id) throws Exception{
        return postsService.findById(Long.parseLong(id));
    }

    @GetMapping("/api/v1/posts-masked/{id}")
    public PostsResponseDto findById_MaskedAuthor (@PathVariable String id) throws Exception{
        return postsService.findById_MaskedAuthor(Long.parseLong(id));
    }

    @GetMapping("/api/v1/posts-title/{id}")
    public String findTitleById (@PathVariable String id) throws Exception{
        return postsService.findById(Long.parseLong(id)).getTitle();
    }

    @GetMapping("/api/v1/posts-all")
    public List<PostsResponseDto> getAll() throws Exception{
        return postsService.findAll();
    }

    @GetMapping("/api/v1/posts-all-masking")
    public List<PostsListResponseDto> getAll_Masking() throws Exception{
        return postsService.findAll_Masked();
    }

    @GetMapping("/api/v1/validate-edit")
    public Boolean validateEdit(@RequestParam Long id){
        return postsService.validateEdit(id);
    }

}


