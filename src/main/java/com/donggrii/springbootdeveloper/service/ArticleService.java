package com.donggrii.springbootdeveloper.service;

import com.donggrii.springbootdeveloper.domain.Article;
import com.donggrii.springbootdeveloper.dto.ArticleAddRequestDto;
import com.donggrii.springbootdeveloper.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 서블릿 컨테이너에 빈으로 등록
public class ArticleService {

    private final ArticleRepository articleRepository;

    // 블로그 글 추가 메서드
    public Article save(ArticleAddRequestDto dto) {
        // JpaRepository에서 지원하는 저장 메서드 save()
        // ArticleAddRequestDto 클래스에 저장된 값들을 article 데이터베이스에 저장
        return articleRepository.save(dto.toEntity());
    }
}