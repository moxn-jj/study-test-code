package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

import java.time.Clock;
import java.util.UUID;

import com.example.demo.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final CertificationService certificationService;

    private final UuidHolder uuidHolder;

    private final ClockHolder clockHolder;

    /**
     * [리팩토링] getById > findById
     * Optional을 반환한다는 의미로 find로 변경
     *
     * [리팩토링] 사용하지 않는 메소드는 삭제
     * 주석을 위해 남겨두었지만 원래는 삭제 처리
     *
     * @param id
     * @return
     */
    /*public Optional<UserEntity> findById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE);
    }*/

    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }

    /**
     * [리팩토링] getByIdOrElseThrow > getById
     * find/get의 차이 (일반적인 컨벤션 기준)
     * get은 데이터가 없으면 에러를 던진다는 의미가 내포되어 있음
     *
     * @param id
     * @return
     */
    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    /**
     * [리팩토링] createUser > create
     * UserService이기 때문에 User를 붙이지 않아도 user를 생성한다는 의미를 가지기 때문
     *
     * @param userCreate
     * @return
     */
    @Transactional
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate, uuidHolder);
        user = userRepository.save(user);
        certificationService.send(userCreate.getEmail(), user.getId(), user.getCertificationCode());
        return user;
    }

    /**
     * [리팩토링] updateUser > update
     * UserService이기 때문에 User를 붙이지 않아도 user를 수정한다는 의미를 가지기 때문
     *
     * @param id
     * @param userUpdate
     * @return
     */
    @Transactional
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        user = user.update(userUpdate);
//        user = userRepository.save(user);
        return userRepository.save(user);
    }

    @Transactional
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.login(clockHolder);
        userRepository.save(user); // 영속성 객체가 아닌 도메인 객체를 변경했기 때문에 저장까지 해줌
    }

    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.certificate(certificationCode);
        userRepository.save(user);
    }

}