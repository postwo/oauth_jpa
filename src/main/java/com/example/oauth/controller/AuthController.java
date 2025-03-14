package com.example.oauth.controller;

import com.example.oauth.dto.request.auth.EmailCertificationRequestDto;
import com.example.oauth.dto.request.auth.IdCheckRequestDto;
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


}
