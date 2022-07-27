package com.espresso.springboot.web.dto;

import com.espresso.springboot.domain.posts.Posts;
import com.espresso.springboot.utils.StringMasker;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }

    private Boolean _DoMasking(int i, int len){
        if(len < 3)
            return i == len-1; // 2글자 이하일 경우 마지막 글자 마스킹
        else
            return (0<i && i < len-1); // 3글자 이상일 경우, 중간 글자 마스킹
    }
    public PostsResponseDto MaskingAuthor(){
        if (author.isEmpty())
            return this;

        author = new StringMasker(author).toString();
        return this;
    }
}
