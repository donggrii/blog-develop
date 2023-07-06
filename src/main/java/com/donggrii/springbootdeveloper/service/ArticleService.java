package com.donggrii.springbootdeveloper.service;

import com.donggrii.springbootdeveloper.domain.Article;
import com.donggrii.springbootdeveloper.dto.ArticleAddRequestDto;
import com.donggrii.springbootdeveloper.repository.ArticleRepository;
import java.util.List;
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

    public List<Article> findAll() {
        return articleRepository.findAll(); // JPA 지원 메서드 findAll() : 모든 데이터 조회
    }

    public Article findById(long id) {
        // JPA 지원 메서드 findById() : ID를 받아 Entity 조회하고, 없으면 IllegalArgumentException 발생
        return articleRepository.findById(id)
                                .orElseThrow(
                                    () -> new IllegalArgumentException("not found: " + id));
    }
}
