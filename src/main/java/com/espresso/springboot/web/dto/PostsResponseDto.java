package com.espresso.springboot.web.dto;

import com.espresso.springboot.domain.posts.Posts;
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
        if (author.length() == 0){
            return this;
        }

        StringBuilder mosaic_author = new StringBuilder(author);
        for(int i=0;i<mosaic_author.length();i++)
        {
            if(_DoMasking(i,mosaic_author.length())){
                mosaic_author.setCharAt(i, '*');
            }
        }

        author = mosaic_author.toString();
        return this;
    }
}
