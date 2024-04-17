package com.example.demo.user.controller;


import com.example.demo.user.domain.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserCreateControllerTest {

    // MockMVC : controller 테스트를 위해 사용하며, api 테스트할 때 많이 사용됨
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender javaMailSender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원_가입된_사용자는_PENDING_상태이다() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("moxn.jj@gmail.com")
                .nickname("moxn.jj")
                .address("Pangyo")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // when
        // then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("moxn.jj@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("moxn.jj"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
