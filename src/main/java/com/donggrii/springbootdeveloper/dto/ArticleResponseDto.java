package com.donggrii.springbootdeveloper.dto;

import com.donggrii.springbootdeveloper.domain.Article;
import lombok.Getter;

@Getter
public class ArticleResponseDto {

    private final String title;
    private final String content;

    public ArticleResponseDto(Article article) { // Entity를 인수로 받는 생성자
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
