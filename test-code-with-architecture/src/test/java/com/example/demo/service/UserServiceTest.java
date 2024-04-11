package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest // 스프링 부트의 의존성 주입을 위함
@TestPropertySource("classpath:test-application.properties") // TODO : 없어도 되는 건지 확인

@SqlGroup({
    // 테스트 시작 시 기본 데이터 생성
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    // 테스트 완료 후 데이터 삭제
    , @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "moxn1948@gmail.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("moxn1948");
    }

    @Test
    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "moxn1949@gmail.com";

        // when
        // then
        assertThatThrownBy(() -> {
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        long id = 1;

        // when
        UserEntity result = userService.getById(id);

        // then
        assertThat(result.getNickname()).isEqualTo("moxn1948");
    }

    @Test
    void getById_는_PENDING_상태인_유저를_찾아올_수_있다() {
        // given
        long id = 2;

        // when
        // then
        assertThatThrownBy(() -> {
            userService.getById(id);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

}