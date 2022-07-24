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

    public PostsResponseDto MosaicAuthor(){
        if (author.length() < 3){
            return this;
        }

        StringBuilder mosaic_author = new StringBuilder();
        char firstChar = author.charAt(0);
        char lastChar = author.charAt(author.length()-1);
        mosaic_author.append(firstChar);
        for(int i=0;i<author.length()-2;i++)
           mosaic_author.append("*");
        mosaic_author.append(lastChar);
        author = mosaic_author.toString();
        return this;
    }
}
