package com.donggrii.springbootdeveloper.dto;

import com.donggrii.springbootdeveloper.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class ArticleAddRequestDto {

    private String title;
    private String content;

    public Article toEntity() { // 생성자를 사용해 객체 생성 (빌더 패턴을 사용해 DTO를 Entity로 만드는 메서드)
        return Article.builder()
                      .title(title)
                      .content(content)
                      .build();
    }
}
