package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

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

    public UserEntity getByEmail(String email) {
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
    public UserEntity getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    /**
     * [리팩토링] createUser > create
     * UserService이기 때문에 User를 붙이지 않아도 user를 생성한다는 의미를 가지기 때문
     *
     * @param userCreateDto
     * @return
     */
    @Transactional
    public UserEntity create(UserCreateDto userCreateDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userCreateDto.getEmail());
        userEntity.setNickname(userCreateDto.getNickname());
        userEntity.setAddress(userCreateDto.getAddress());
        userEntity.setStatus(UserStatus.PENDING);
        userEntity.setCertificationCode(UUID.randomUUID().toString());
        userEntity = userRepository.save(userEntity);
        String certificationUrl = generateCertificationUrl(userEntity);
        sendCertificationEmail(userCreateDto.getEmail(), certificationUrl);
        return userEntity;
    }

    /**
     * [리팩토링] updateUser > update
     * UserService이기 때문에 User를 붙이지 않아도 user를 수정한다는 의미를 가지기 때문
     *
     * @param id
     * @param userUpdateDto
     * @return
     */
    @Transactional
    public UserEntity update(long id, UserUpdateDto userUpdateDto) {
        UserEntity userEntity = getById(id);
        userEntity.setNickname(userUpdateDto.getNickname());
        userEntity.setAddress(userUpdateDto.getAddress());
        userEntity = userRepository.save(userEntity);
        return userEntity;
    }

    @Transactional
    public void login(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        userEntity.setLastLoginAt(Clock.systemUTC().millis());
    }

    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        if (!certificationCode.equals(userEntity.getCertificationCode())) {
            throw new CertificationCodeNotMatchedException();
        }
        userEntity.setStatus(UserStatus.ACTIVE);
    }

    private void sendCertificationEmail(String email, String certificationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Please certify your email address");
        message.setText("Please click the following link to certify your email address: " + certificationUrl);
        mailSender.send(message);
    }

    private String generateCertificationUrl(UserEntity userEntity) {
        return "http://localhost:8080/api/users/" + userEntity.getId() + "/verify?certificationCode=" + userEntity.getCertificationCode();
    }
}