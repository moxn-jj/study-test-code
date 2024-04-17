package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest // JPA 테스트를 위한 어노테이션
@Sql("/sql/user-repository-test-data.sql") // 기본 데이터 생성
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void UserfindByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given : sql로 대체
//        UserEntity userEntity = new UserEntity();
//        userEntity.setEmail("moxn1948@gmail.com");
//        userEntity.setAddress("Seoul");
//        userEntity.setNickname("moxn");
//        userEntity.setStatus(UserStatus.ACTIVE);
//        userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
//        userRepository.save(userEntity);

        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void UserfindByIdAndStatus_는_데이터가_없으면_Optional_empty_르_내려준다() {
        // given : sql로 대체
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given : sql로 대체
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("moxn1948@gmail.com", UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }
    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_르_내려준다() {
        // given : sql로 대체
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("moxn1948@gmail.com", UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

}
