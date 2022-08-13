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

    public PostsResponseDto MaskingAuthor(){
        if (author.isEmpty())
            return this;

        author = new StringMasker(author).toString();
        return this;
    }
}
