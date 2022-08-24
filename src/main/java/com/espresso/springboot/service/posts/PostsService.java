package com.espresso.springboot.service.posts;

import com.espresso.springboot.config.auth.dto.SessionUser;
import com.espresso.springboot.domain.posts.Posts;
import com.espresso.springboot.domain.posts.PostsRepository;
import com.espresso.springboot.web.dto.PostsListResponseDto;
import com.espresso.springboot.web.dto.PostsResponseDto;
import com.espresso.springboot.web.dto.PostsSaveRequestDto;
import com.espresso.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final HttpSession httpSession;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if(sessionUser==null){
            throw new IllegalArgumentException("로그인 해주십시오.");
        }
        Posts newPosts = requestDto.toEntity(sessionUser.getId());
        return postsRepository.save(newPosts).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found posts. id =" + id));
        validateUser(posts);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id = " + id));
        return new PostsResponseDto(entity);
    }

    public PostsResponseDto findById_MaskedAuthor(Long id){
        return findById(id).MaskingAuthor();
    }

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAll(){
        return  postsRepository.findAll().stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAll_Masked(){
        return  postsRepository.findAll().stream()
                .map(posts -> new PostsListResponseDto(posts).MaskingAuthor())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc(PageRequest.of(0,10)).stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("게시글이 없습니다. id=" + id.toString()));

        validateUser(posts);
        postsRepository.deleteById(id);
    }

    private void validateUser(Posts posts) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if(!sessionUser.getEmail().equals("admin") &&
                !sessionUser.getId().equals(posts.getUid())){
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
