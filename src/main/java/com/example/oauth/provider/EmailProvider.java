package com.example.oauth.provider;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailProvider {

    private final JavaMailSender javaMailSender;
    private final String SUBJECT = "[임대주택 가격 서비스] 인증메일 입니다.";

    public boolean sendCertificationMail(String email, String certificationNumber){ //certificationNumber == 전송할 인증번호
        try {

            MimeMessage message = javaMailSender.createMimeMessage(); //이메일의 발신자, 수신자, 제목, 본문 등을 설정할 수 있습니다.
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true); //더 쉽게 이메일의 수신자, 제목, 본문 등을 설정

            String htmlContent = getCertificationMessage(certificationNumber); //이메일 본문을 구성하는 데 사용됩니다. 인증 번호를 포함한 HTML 메시지를 생성

            messageHelper.setTo(email); // 어디로 메일을 보낼 것인지 설정
            messageHelper.setSubject(SUBJECT); // 이메일 제목 설정
            messageHelper.setText(htmlContent, true); // 이메일 본문 설정 (true: HTML 형식 적용)

            javaMailSender.send(message);//전송을 해준다

        } catch (Exception e) {
            e.printStackTrace();
            log.error("메일 전송 실패: {}", e.getMessage());
            return false; // 오류 발생 시 false 반환
        }

        return true; // 메일 전송 성공 시 true 반환
    }

    private String getCertificationMessage(String certificationNumber){

        String certificationMessage = "" ;
        certificationMessage += "<h1 style='text-align: center;'>[임대주택 가격 서비스]  인증메일</h1>";
        certificationMessage += "<h3 style='text-align: center;'> 인증코드 : <strong style= 'font-size: 32px; letter-spacing: 8px;'>" +
                certificationNumber +"</strong></h3>";
        return certificationMessage;
    }

}
