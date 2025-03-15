package com.example.oauth.service.implement;

import com.example.oauth.common.CertificationNumber;
import com.example.oauth.dto.request.auth.CheckCertificationRequestDto;
import com.example.oauth.dto.request.auth.EmailCertificationRequestDto;
import com.example.oauth.dto.request.auth.IdCheckRequestDto;
import com.example.oauth.dto.response.ResponseDto;
import com.example.oauth.dto.response.auth.CheckCertificationResponseDto;
import com.example.oauth.dto.response.auth.EmailCertificationReponseDto;
import com.example.oauth.dto.response.auth.IdCheckResponseDto;
import com.example.oauth.entity.CertificationEntity;
import com.example.oauth.provider.EmailProvider;
import com.example.oauth.repository.CertificationRepository;
import com.example.oauth.repository.UserRepository;
import com.example.oauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;

    private final CertificationRepository certificationRepository;

    private final EmailProvider emailProvider;

    //아이디 중복 확인
    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try{
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if (isExistId) return IdCheckResponseDto.duplicateId(); // true일경우

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IdCheckResponseDto.success();
    }

    // 이메일 전송
    @Override
    public ResponseEntity<? super EmailCertificationReponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try{
            String userId = dto.getId();
            String email = dto.getEmail();

            //존재하지 않는 이메일인지 확인
            boolean isExistId = userRepository.existsById(userId);
            if (isExistId) return EmailCertificationReponseDto.duplicateId();

            // 번호 생성
            String certificationNumber = CertificationNumber.getCertificationNumber(); //0~9 사이에 수 를 받아온다

            //이메일 전송
            boolean isSuccessed = emailProvider.sendCertificationMail(email,certificationNumber);
            if (!isSuccessed) return EmailCertificationReponseDto.mailsendFail(); //false 일 경우

            CertificationEntity certificationEntity = new CertificationEntity(userId,email,certificationNumber);

            // 그전송 결과를 저장
            certificationRepository.save(certificationEntity);

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationReponseDto.success();
    }

    //메일 인증 확인
    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try{
            String userId = dto.getId();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            if (certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

            // trim == 문자열 공백 제거
            String trimmedCertificationNumber = certificationNumber.trim();
            String trimmedEntityCertificationNumber = certificationEntity.getCertificationNumber().trim();

            boolean isMatched = certificationEntity.getEmail().equals(email) && trimmedEntityCertificationNumber.equals(trimmedCertificationNumber);
            if (!isMatched) return  CheckCertificationResponseDto.certificationFail();//false일경우

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }
}