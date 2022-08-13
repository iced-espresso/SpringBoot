package com.espresso.springboot.web.dto;

import com.espresso.springboot.domain.posts.Posts;
import com.espresso.springboot.utils.StringMasker;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@NoArgsConstructor
@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;
    private String formatedModifiedDate;

    public PostsListResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
        this.formatedModifiedDate = modifiedDate.truncatedTo(ChronoUnit.SECONDS).toString();
        this.formatedModifiedDate = this.formatedModifiedDate.replace('T',' ');
    }

    public PostsListResponseDto MaskingAuthor(){
        if (author.isEmpty())
            return this;

        author = new StringMasker(author).toString();
        return this;
    }

    public PostsListResponseDto DropMilliSeconds(){
        modifiedDate = modifiedDate.truncatedTo(ChronoUnit.MILLIS);
        return this;
    }
}
