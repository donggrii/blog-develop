package com.donggrii.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Article의 created_at, updated_at 컬럼값 자동 업데이트를 위한 애너테이션
@SpringBootApplication
public class SpringBootBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBlogApplication.class, args);
    }
}
