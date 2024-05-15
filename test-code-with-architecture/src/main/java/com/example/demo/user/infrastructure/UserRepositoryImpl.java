package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements com.example.demo.user.service.port.UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(long id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return userJpaRepository.findByIdAndStatus(id, userStatus).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return userJpaRepository.findByEmailAndStatus(email, userStatus).map(UserEntity::toModel);
    }

    @Override
    public User save(User user) {
        // 저장하는 부분은 반대로 도메인 객체를 영속성 객체로 바꿔주는 메서드가 필요함
        // User.toModel 처럼 만들지 않는 이유는 도메인은 인프라 레이어의 정보를 모르는 것이 좋음
        return userJpaRepository.save(UserEntity.fromModel(user)).toModel();
    }

}
