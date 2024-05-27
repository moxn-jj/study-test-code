package com.example.demo.mock;

import com.example.demo.user.service.port.MailSender;

/**
 * 테스트에서 메일 발송을 대체할 페이크 객체
 */
public class FakeMailSender implements MailSender {

    // 실제로 어떤 값이 들어왔는 지 확인할 수 있도록 멤버변수 만들어 줌
    public String email;
    public String title;
    public String content;


    @Override
    public void send(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
