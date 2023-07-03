package com.donggrii.springbootdeveloper.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.donggrii.springbootdeveloper.domain.Article;
import com.donggrii.springbootdeveloper.dto.ArticleAddRequestDto;
import com.donggrii.springbootdeveloper.repository.ArticleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc // MockMvc 생성
class ArticleControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                      .build();
        articleRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가 (CREATE)")
    @Test
    public void addArticle() throws Exception {
        // [given] : 블로그 글 추가에 필요한 요청 객체 만들기
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final ArticleAddRequestDto userRequest = new ArticleAddRequestDto(title, content);

        // 자바 객체를 JSON으로 직렬화 (Serialization)
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // [when] : given절에서 만든 객체를 요청 본문으로 블로그 글 추가 API에 보내기 (요청 타입 : JSON)
        ResultActions result = mockMvc.perform(post(url) // HTTP 메서드, URL
                                                         .contentType(
                                                             MediaType.APPLICATION_JSON_VALUE) // 요청 타입
                                                         .content(requestBody)); // 요청 본문

        // [then]
        // 응답 코드가 201 Created인지 확인, Blog를 전체 조회해 크기가 1인지 확인, 실제 저장된 데이터와 요청값 비교
        result.andExpect(status().isCreated());

        List<Article> articles = articleRepository.findAll();

        assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }
}