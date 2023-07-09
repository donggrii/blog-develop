package com.donggrii.springbootdeveloper.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 컨트롤러라는 것을 명시적으로 표시 (return 값과 동일한 이름의 html 파일을 찾도록 함)
public class ThymeleafExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) { // 뷰로 데이터를 넘겨주는 Model 객체
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", examplePerson); // Person 객체 지정
        model.addAttribute("today", LocalDate.now());

        return "example"; // example.html라는 뷰 조회
    }

    @Setter
    @Getter
    class Person {

        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}
