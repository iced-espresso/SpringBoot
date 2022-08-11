package com.espresso.springboot.service;

import com.espresso.springboot.domain.posts.Posts;
import com.espresso.springboot.domain.posts.PostsRepository;
import com.espresso.springboot.service.posts.PostsService;
import com.espresso.springboot.web.dto.PostsResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith( SpringRunner.class )
public class PostServiceUnitTest {

    @MockBean
    private PostsRepository postsRepository;

    @Autowired
    private PostsService postsService;

    @Test
    public void PostServiceMaskingTest(){
        Posts posts = Posts.builder().title("t").content("c").author("aaa").build();

        given(postsRepository.findById(any())).willReturn(Optional.ofNullable(posts));

        PostsResponseDto postsResponseDto = postsService.findById_MaskedAuthor(0L);

        assertThat(postsResponseDto.getAuthor()).isEqualTo("a*a");
        assertThat(postsResponseDto.getTitle()).isEqualTo(posts.getTitle());
        assertThat(postsResponseDto.getContent()).isEqualTo(posts.getContent());
    }
}
