package com.donggrii.springbootdeveloper.controller;

import com.donggrii.springbootdeveloper.domain.Article;
import com.donggrii.springbootdeveloper.dto.ArticleAddRequestDto;
import com.donggrii.springbootdeveloper.dto.ArticleResponseDto;
import com.donggrii.springbootdeveloper.dto.ArticleUpdateRequestDto;
import com.donggrii.springbootdeveloper.service.ArticleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponseDto> findArticle(@PathVariable long id) {
        // @PathVariable : URL에서 값을 가져오는 애너테이션
        //                 URL에서 {id}에 해당하는 값이 argument로 들어옴
        Article article = articleService.findById(id);

        return ResponseEntity.ok()
                             .body(new ArticleResponseDto(article)); // body에 담아 웹 브라우저로 전송
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        // @PathVariable : URL에서 값을 가져오는 애너테이션
        //                 URL에서 {id}에 해당하는 값이 argument로 들어옴
        articleService.delete(id);

        return ResponseEntity.ok()
                             .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
        @RequestBody ArticleUpdateRequestDto dto) {
        // PUT 요청이 오면 Request Body 정보가 dto로 넘어옴
        // 이 dto를 다시 service 클래스의 update() 메서드에 id와 함께 넘겨주는 것
        Article updatedArticle = articleService.update(id, dto);

        return ResponseEntity.ok()
                             .body(updatedArticle); // 응답 값은 body에 담아 전송
    }
}
