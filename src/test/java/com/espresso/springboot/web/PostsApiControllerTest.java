package com.espresso.springboot.web;

import com.espresso.springboot.config.auth.dto.SessionUser;
import com.espresso.springboot.domain.posts.Posts;
import com.espresso.springboot.domain.posts.PostsRepository;
import com.espresso.springboot.domain.user.Role;
import com.espresso.springboot.domain.user.User;
import com.espresso.springboot.web.dto.PostsListResponseDto;
import com.espresso.springboot.web.dto.PostsResponseDto;
import com.espresso.springboot.web.dto.PostsSaveRequestDto;
import com.espresso.springboot.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@WithMockUser(roles="USER")
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;


    private MockMvc mvc;
    private MockHttpSession session;
    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        session = new MockHttpSession();
        User user = User.builder().role(Role.USER).email("admin").name("name").password("1").build();
        SessionUser sessionUser = new SessionUser(user);
        session.setAttribute("user", sessionUser);
    }
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
        mvc.perform(post(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
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
        //when
        mvc.perform(put(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(update_title);
        assertThat(all.get(0).getContent()).isEqualTo(update_content);
    }
    @Test
    public void Posts_수정_Auditing() throws Exception{
        Posts savedPosts = postsRepository.save(Posts.builder().title("title").content("content").author("csu2018@gmail.com").build());


        Long id = postsRepository.findAll().get(0).getId();

        String update_title = "update_title";
        String update_content = "update_content";
        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto.builder()
                .title(update_title)
                .content(update_content)
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts/" + id;
        mvc.perform(put(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());


        LocalDateTime createdTime = postsRepository.findAll().get(0).getCreatedDate();
        LocalDateTime modifiedTime = postsRepository.findAll().get(0).getModifiedDate();
        System.out.println(">>>>> createdDate:" + createdTime + " >>>>>> modifiedDate:" + modifiedTime);
        assertThat(modifiedTime).isAfter(createdTime);
    }
    @Test
    public void Posts_조회_PathVariable() throws Exception{
        String title = "title";
        String content ="testcontent2";
        String author = "csu2018@gmail.com";

        postsRepository.save(Posts.builder().title(title).content(content).author(author).build());
        Long id = postsRepository.findAll().get(0).getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + id;


        MvcResult mvcResult = mvc.perform(get(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        PostsResponseDto postsResponseDto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PostsResponseDto.class);
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
        String url = "http://localhost:" + port + "/api/v1/posts?id=" + id;

        MvcResult mvcResult = mvc.perform(get(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        PostsResponseDto postsResponseDto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PostsResponseDto.class);

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
        String url = "http://localhost:" + port + "/api/v1/posts-masked/" + id;

        MvcResult mvcResult = mvc.perform(get(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        PostsResponseDto postsResponseDto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PostsResponseDto.class);

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

        String url = "http://localhost:" + port + "/api/v1/posts-all";

        MvcResult mvcResult = mvc.perform(get(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        PostsResponseDto[] postsResponseDtoArray = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PostsResponseDto[].class);

        assertThat(postsResponseDtoArray.length).isEqualTo(postsArrayList.size());

        for(int i=0;i<5;i++)
        {
            PostsResponseDto postsResponseDto = postsResponseDtoArray[i];
            assertThat(postsResponseDto.getTitle()).isEqualTo(postsArrayList.get(i).getTitle());
            assertThat(postsResponseDto.getContent()).isEqualTo(postsArrayList.get(i).getContent());
            assertThat(postsResponseDto.getAuthor()).isEqualTo(postsArrayList.get(i).getAuthor());
        }
    }

    @Test
    public void Posts_모든리스트마스킹조회_PathVariable() throws Exception{
        String title = "title";
        String content ="testcontent";
        String[] authors = {"a","ab","abc","abcd",""};
        String[] maskedAuthors = {"*", "a*", "a*c", "a**d", ""};

        ArrayList<Posts> postsArrayList = new ArrayList<Posts>();
        for(int i=0;i<5;i++)
        {
            Posts posts = Posts.builder()
                    .title(title + Integer.toString(i))
                    .content(content + Integer.toString(i))
                    .author(authors[i])
                    .build();
            postsArrayList.add(posts);
            postsRepository.save(posts);
        }

        String url = "http://localhost:" + port + "/api/v1/posts-all-masking";

        MvcResult mvcResult = mvc.perform(get(url).session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        PostsListResponseDto[] postsResponseDtoArray = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PostsListResponseDto[].class);

        assertThat(postsResponseDtoArray.length).isEqualTo(postsArrayList.size());

        for(int i=0;i<5;i++)
        {
            PostsListResponseDto postsResponseDto = postsResponseDtoArray[i];
            assertThat(postsResponseDto.getTitle()).isEqualTo(postsArrayList.get(i).getTitle());
            assertThat(postsResponseDto.getAuthor()).isEqualTo(maskedAuthors[i]);
        }
    }
}
