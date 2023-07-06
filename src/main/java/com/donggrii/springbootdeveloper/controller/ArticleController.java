package com.donggrii.springbootdeveloper.controller;

import com.donggrii.springbootdeveloper.domain.Article;
import com.donggrii.springbootdeveloper.dto.ArticleAddRequestDto;
import com.donggrii.springbootdeveloper.dto.ArticleResponseDto;
import com.donggrii.springbootdeveloper.service.ArticleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class ArticleController {

    private final ArticleService articleService;

    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // 요청 본문 값 매핑
    // @RequestBody : HTTP를 요청할 때 응답에 해당하는 값을 애너테이션이 붙은 대상 객체인 dto에 매핑
    public ResponseEntity<Article> addArticle(@RequestBody ArticleAddRequestDto dto) {
        Article savedArticle = articleService.save(dto);

        // 요청한 자원이 성공적으로 생성되었으며, 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponseDto>> findAllArticles() {
        List<ArticleResponseDto> articles = articleService.findAll()
                                                          .stream()
                                                          .map(ArticleResponseDto::new)
                                                          .toList();

        return ResponseEntity.ok()
                             .body(articles);
    }
}
