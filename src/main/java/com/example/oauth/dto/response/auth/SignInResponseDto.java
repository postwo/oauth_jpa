package com.example.oauth.dto.response.auth;

import com.example.oauth.common.ResponseCode;
import com.example.oauth.common.ResponseMessage;
import com.example.oauth.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDto extends ResponseDto {

    private String token; //jwt 토큰
    private int expirationTime; // 종료시간

    private SignInResponseDto(String token){
        super();
        this.token = token;
        this.expirationTime = 3600; //고정값
    }

    public static ResponseEntity<SignInResponseDto> success (String token){
        SignInResponseDto responseBody = new SignInResponseDto(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signInFail(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

}