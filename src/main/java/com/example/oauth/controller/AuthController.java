package com.example.oauth.controller;

import com.example.oauth.dto.request.auth.CheckCertificationRequestDto;
import com.example.oauth.dto.request.auth.EmailCertificationRequestDto;
import com.example.oauth.dto.request.auth.IdCheckRequestDto;
import com.example.oauth.dto.response.auth.CheckCertificationResponseDto;
import com.example.oauth.dto.response.auth.EmailCertificationReponseDto;
import com.example.oauth.dto.response.auth.IdCheckResponseDto;
import com.example.oauth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck (@RequestBody @Valid IdCheckRequestDto requsetbody){
        ResponseEntity<? super IdCheckResponseDto> response = authService.idCheck(requsetbody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationReponseDto> emailCertification (
            @RequestBody @Valid EmailCertificationRequestDto requestbody
    ){
        System.out.println("Request Body: " + requestbody); // 요청 본문 디버깅
        ResponseEntity<? super EmailCertificationReponseDto> response = authService.emailCertification(requestbody);
        return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
            @RequestBody @Valid CheckCertificationRequestDto requestbody
    ){
        //서버는 문제가 없다 react 단에서 문제다 = 인증번호 틀릴시 메시지 띄우는게 문제
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestbody);
        return response;
    }



}
