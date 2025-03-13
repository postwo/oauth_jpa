package com.example.oauth.service;

import com.example.oauth.dto.request.auth.IdCheckRequestDto;
import com.example.oauth.dto.response.auth.IdCheckResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    //부모 형태도 같이 반환 할 수 있게 이렇게 ?을 표현함
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);

}