package com.espresso.springboot.service.posts;

import com.espresso.springboot.domain.posts.Posts;
import com.espresso.springboot.domain.posts.PostsRepository;
import com.espresso.springboot.web.dto.PostsResponseDto;
import com.espresso.springboot.web.dto.PostsSaveRequestDto;
import com.espresso.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found posts. id =" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id = " + id));
        return new PostsResponseDto(entity);
    }

    public PostsResponseDto[] findAll(){
        List<Posts> postsList = postsRepository.findAll();
        List<PostsResponseDto> postsResponseDtos = new ArrayList<PostsResponseDto>();

        for(Posts posts:postsList){
            postsResponseDtos.add(new PostsResponseDto(posts));
        }

        return  postsResponseDtos.toArray(new PostsResponseDto[0]);

    }
}
