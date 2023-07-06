package com.donggrii.springbootdeveloper.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.donggrii.springbootdeveloper.domain.Article;
import com.donggrii.springbootdeveloper.dto.ArticleAddRequestDto;
import com.donggrii.springbootdeveloper.dto.ArticleUpdateRequestDto;
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

    @DisplayName("addArticle: 한 개의 블로그 글 추가 (CREATE)")
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
        // perform((HTTP 메서드(URL)).(요청 타입).(요청 본문))
        ResultActions result = mockMvc.perform(post(url)
            .contentType(
                MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody));

        // [then]
        // 응답 코드가 201 Created인지 확인, Blog를 전체 조회해 크기가 1인지 확인, 실제 저장된 데이터와 요청값 비교
        result.andExpect(status().isCreated());

        List<Article> articles = articleRepository.findAll();

        assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: 블로그 글 전체 목록 조회 (READ-ALL)")
    @Test
    public void findAllArticles() throws Exception {
        // [given] : 블로그 글 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        articleRepository.save(Article.builder()
                                      .title(title)
                                      .content(content)
                                      .build());

        // [when] : 목록 조회 API 호출
        final ResultActions result = mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON));

        // [then] : 응답 코드 200 OK 확인, 반환받은 값 중 0번째 요소의 content와 title이 저장된 값과 같은지 확인
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value(content))
            .andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("findArticle: 한 개의 블로그 글 조회 (READ-ONE)")
    @Test
    public void findArticle() throws Exception {
        // [given] : 블로그 글 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(Article.builder()
                                                             .title(title)
                                                             .content(content)
                                                             .build());

        // [when] : 저장한 블로그 글의 id 값으로 API 호출
        final ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));

        // [then] : 응답 코드 200 OK 확인, 반환받은 content와 title이 저장된 값과 같은지 확인
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(content))
            .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("deleteArticle: 한 개의 블로그 글 삭제 (DELETE)")
    @Test
    public void deleteArticle() throws Exception {
        // [given] : 블로그 글 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(Article.builder()
                                                             .title(title)
                                                             .content(content)
                                                             .build());

        // [when] : 저장한 블로그 글의 id 값으로 삭제 API 호출
        mockMvc.perform(delete(url, savedArticle.getId()))
               .andExpect(status().isOk());

        // [then] : 응답 코드 200 OK 확인, 블로그 글 리스트를 전체 조회해 조회한 배열 크기가 0인지 확인
        List<Article> articles = articleRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 한 개의 블로그 글 수정 (UPDATE)")
    @Test
    public void updateArticle() throws Exception {
        // [given] : 블로그 글 저장, 블로그 글 수정에 필요한 요청 객체 생성
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(Article.builder()
                                                             .title(title)
                                                             .content(content)
                                                             .build());

        final String newTitle = "new-title";
        final String newContent = "new-content";

        ArticleUpdateRequestDto dto = new ArticleUpdateRequestDto(newTitle, newContent);

        // [when] : given절에서 만든 객체를 요청 본문으로 UPDATE API로 수정 요청을 보냄 (요청 타입 : JSON)
        // perform((HTTP 메서드(URL)).(요청 타입).(요청 본문))
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dto)));

        // [then] : 응답 코드 200 OK 확인, 블로그 글 id로 조회한 후 값 수정되었는지 확인
        result.andExpect(status().isOk());

        Article article = articleRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}