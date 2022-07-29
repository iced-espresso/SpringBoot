package com.espresso.springboot.domain.posts;

import com.espresso.springboot.service.posts.PostsService;
import com.espresso.springboot.web.dto.PostsResponseDto;
import com.espresso.springboot.web.dto.PostsSaveRequestDto;
import com.espresso.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest

public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        String title = "테스트 게시글 제목";
        String content = "테스트 게시글 본문";
        String author = "csu2018@gmail.com";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
        assertThat(posts.getAuthor()).isEqualTo(author);
    }

    @Test
    public void BaseTimeEntity_등록() throws InterruptedException {
        //given
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Posts.builder().content("c").title("t").author("a").build());


        Posts posts = postsRepository.findAll().get(0);
        System.out.println(">>>>> createdDate:" + posts.getCreatedDate() + " >>>>>> modifiedDate:" + posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfterOrEqualTo(now);
        assertThat(posts.getModifiedDate()).isAfterOrEqualTo(now);
    }

}
