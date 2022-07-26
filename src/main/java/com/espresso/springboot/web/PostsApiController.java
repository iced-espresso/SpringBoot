package com.espresso.springboot.web;

import com.espresso.springboot.service.posts.PostsService;
import com.espresso.springboot.web.dto.PostsResponseDto;
import com.espresso.springboot.web.dto.PostsSaveRequestDto;
import com.espresso.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/get")
    public PostsResponseDto findByIdRequestParam (@RequestParam Long id) throws Exception{
        return postsService.findById(id);
    }

    @GetMapping("/api/v1/posts/get/{id}")
    public PostsResponseDto findById (@PathVariable String id) throws Exception{
        return postsService.findById(Long.parseLong(id));
    }

    @GetMapping("/api/v1/posts/get-masked/{id}")
    public PostsResponseDto findById_MaskedAuthor (@PathVariable String id) throws Exception{
        return postsService.findById(Long.parseLong(id)).MaskingAuthor();
    }

    @GetMapping("/api/v1/posts/get_title/{id}")
    public String findTitleById (@PathVariable String id) throws Exception{
        return postsService.findById(Long.parseLong(id)).getTitle();
    }

    @GetMapping("/api/v1/posts/get-all")
    public PostsResponseDto[] getAll () throws Exception{
        return postsService.findAll();
    }

    @GetMapping("/api/v1/posts/get-all-masking")
    public PostsResponseDto[] getAll_Masking () throws Exception{
        PostsResponseDto[] postsResponseDtos = postsService.findAll();
        for(PostsResponseDto postsResponseDto:postsResponseDtos){
            postsResponseDto.MaskingAuthor();
        }
        return postsResponseDtos;
    }

}
