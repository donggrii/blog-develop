package com.donggrii.springbootdeveloper.controller;

import com.donggrii.springbootdeveloper.domain.Article;
import com.donggrii.springbootdeveloper.dto.ArticleListViewResponseDto;
import com.donggrii.springbootdeveloper.dto.ArticleViewResponseDto;
import com.donggrii.springbootdeveloper.service.ArticleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller // 컨트롤러라는 것을 명시적으로 표시 (return 값과 동일한 이름의 html 파일을 찾도록 함)
public class ArticleViewController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(Model model) { // 뷰로 데이터를 넘겨주는 Model 객체
        // 블로그 글 전체 리스트를 담은 뷰를 반환
        List<ArticleListViewResponseDto> articles = articleService.findAll()
                                                                  .stream()
                                                                  .map(
                                                                      ArticleListViewResponseDto::new)
                                                                  .toList();
        model.addAttribute("articles", articles); // 블로그 글 리스트를 Model 객체에 저장

        return "articleList"; // articleList.html 라는 뷰 조회
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", new ArticleViewResponseDto(article));

        return "article"; // article.html 라는 뷰 조회
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        // @RequestParam(required = false)으로 id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑 (id는 Optional)
        if (id == null) { // id가 없으면 생성
            model.addAttribute("article", new ArticleViewResponseDto());
        } else { // id가 있으면 수정
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponseDto(article));
        }

        return "newArticle";
    }
}
