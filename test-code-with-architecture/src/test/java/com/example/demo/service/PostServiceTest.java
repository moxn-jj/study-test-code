package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    , @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void getById는_존재하는_게시글을_가져온다() {
        // given
        // when
        PostEntity result = postService.getById(1);

        // then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("moxn.jj@gmail.com");
    }
    
    @Test
    void getById는_존재하지_않는_게시글을_선택하면_에러를_던진다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            postService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .writerId(1)
                .content("foobar")
                .build();

        // when
        PostEntity result = postService.create(postCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isGreaterThan(0L); // 우선 기본 값인 0보다 크다는 것으로 테스트
        // assertThat(result.getCreatedAt()).isEqualTo("FIXME : 현재는 테스트할 방법이 없다.");
    }

    @Test
    void postUpdateDto_를_이용하여_게시글을_수정할_수_있다() {
        // given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("foobar 수정")
                .build();

        // when
        PostEntity result = postService.update(1, postUpdateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar 수정");
        assertThat(result.getModifiedAt()).isGreaterThan(0L); // 우선 기본 값인 0보다 크다는 것으로 테스트
        // assertThat(result.getModifiedAt()).isEqualTo("FIXME : 현재는 테스트할 방법이 없다.");
    }

}