package com.example.demo.post.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MockMVC를 사용하기 위한 어노테이션
@AutoConfigureTestDatabase // MockMVC를 사용하기 위한 어노테이션
public class PostCreateControllerTest {

    // MockMVC : controller 테스트를 위해 사용하며, api 테스트할 때 많이 사용됨
    @Autowired
    private MockMvc mockMvc;

    // TODO : USER와 같이 채워보기
    @Test
    void 헬스_체크_응답이_200으로_내려온다() throws Exception {
        // 우리 서버에 mockMvc를 통해 api를 get방식으로 호출한다.
        mockMvc.perform(get("/health_check.html"))
                .andExpect(status().isOk());
    }
}
