package com.espresso.springboot.web;

import com.espresso.springboot.domain.posts.Posts;
import com.espresso.springboot.domain.posts.PostsRepository;
import com.espresso.springboot.web.dto.PostsResponseDto;
import com.espresso.springboot.web.dto.PostsSaveRequestDto;
import com.espresso.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록() throws Exception{
        // given
        String title = "title";
        String content = "content";
        String author = "csu2018@gmail.com";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                            .title(title)
                                                            .content(content)
                                                            .author(author)
                                                            .build();
        String url = "http://localhost:" + port + "api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
                .isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle())
                .isEqualTo(title);
        assertThat(all.get(0).getContent())
                .isEqualTo(content);
    }

    @Test
    public void Posts_수정() throws Exception{
        Posts savedPosts = postsRepository.save(Posts.builder().title("title").content("content").author("csu2018@gmail.com").build());
        Long id = postsRepository.findAll().get(0).getId();

        String update_title = "update_title";
        String update_content = "update_content";
        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto.builder()
                .title(update_title)
                .content(update_content)
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts/" + id;
        ResponseEntity<Long> updateResponseEntity = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequestDto), Long.class);

        assertThat(updateResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(updateResponseEntity.getBody())
                .isGreaterThan(0L);


        HttpEntity<Posts> longHttpEntity = new HttpEntity<>(savedPosts);
//        ResponseEntity<PostsResponseDto> responseEntity = restTemplate.getForEntity(url,PostsResponseDto.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                longHttpEntity, String.class);
        String responseDto = responseEntity.getBody();
//        assertThat(responseDto.getTitle()).isEqualTo(update_title);
//        assertThat(responseDto.getContent()).isEqualTo(update_content);
    }

    @Test
    public void Posts_조회_PathVariable() throws Exception{
        String title = "title";
        String content ="testcontent2";
        String author = "csu2018@gmail.com";

        postsRepository.save(Posts.builder().title(title).content(content).author(author).build());
        Long id = postsRepository.findAll().get(0).getId();
        String url = "http://localhost:" + port + "/api/v1/posts/get/" + id;

        ResponseEntity<PostsResponseDto> responseEntity = restTemplate.getForEntity(url, PostsResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PostsResponseDto postsResponseDto = responseEntity.getBody();

        assertThat(postsResponseDto.getTitle()).isEqualTo(title);
        assertThat(postsResponseDto.getContent()).isEqualTo(content);
        assertThat(postsResponseDto.getAuthor()).isEqualTo(author);
    }

    @Test
    public void Posts_조회_RequestParam() throws Exception{
        String title = "title";
        String content ="testcontent";
        String author = "csu2018@gmail.com";

        postsRepository.save(Posts.builder().title(title).content(content).author(author).build());
        Long id = postsRepository.findAll().get(0).getId();
        String url = "http://localhost:" + port + "/api/v1/posts/get?id=" + id;

        ResponseEntity<PostsResponseDto> responseEntity = restTemplate.getForEntity(url, PostsResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PostsResponseDto postsResponseDto = responseEntity.getBody();

        assertThat(postsResponseDto.getTitle()).isEqualTo(title);
        assertThat(postsResponseDto.getContent()).isEqualTo(content);
        assertThat(postsResponseDto.getAuthor()).isEqualTo(author);
    }

    @Test
    public void Posts_모자이크조회_PathVariable() throws Exception{
        String title = "title";
        String content ="testcontent2";
        String author = "cseu";

        postsRepository.save(Posts.builder().title(title).content(content).author(author).build());
        Long id = postsRepository.findAll().get(0).getId();
        String url = "http://localhost:" + port + "/api/v1/posts/get_mosaic/" + id;

        ResponseEntity<PostsResponseDto> responseEntity = restTemplate.getForEntity(url, PostsResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PostsResponseDto postsResponseDto = responseEntity.getBody();

        assertThat(postsResponseDto.getTitle()).isEqualTo(title);
        assertThat(postsResponseDto.getContent()).isEqualTo(content);
        assertThat(postsResponseDto.getAuthor()).isEqualTo("c**u");

    }

    @Test
    public void Posts_모든리스트조회_PathVariable() throws Exception{
        String title = "title";
        String content ="testcontent";
        String author = "cseu";

        ArrayList<Posts> postsArrayList = new ArrayList<Posts>();
        for(int i=0;i<5;i++)
        {
            Posts posts = Posts.builder().title(title + Integer.toString(i)).content(content + Integer.toString(i)).author(author + Integer.toString(i))
                    .build();
            postsArrayList.add(posts);
            postsRepository.save(posts);
        }

        String url = "http://localhost:" + port + "/api/v1/posts/get_all";

        ResponseEntity<PostsResponseDto[]> responseEntity = restTemplate.getForEntity(url, PostsResponseDto[].class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PostsResponseDto[] postsResponseDtoArray = responseEntity.getBody();
        assertThat(postsResponseDtoArray.length).isEqualTo(postsArrayList.size());

        for(int i=0;i<5;i++)
        {
            PostsResponseDto postsResponseDto = postsResponseDtoArray[i];
            assertThat(postsResponseDto.getTitle()).isEqualTo(postsArrayList.get(i).getTitle());
            assertThat(postsResponseDto.getContent()).isEqualTo(postsArrayList.get(i).getContent());
            assertThat(postsResponseDto.getAuthor()).isEqualTo(postsArrayList.get(i).getAuthor());
        }

    }

}
