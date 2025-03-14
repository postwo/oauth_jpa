package com.example.oauth.handler;

import com.example.oauth.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// controlleradvice를 달아야 exception들을 낚아채온다
@ControllerAdvice
public class ValidationExceptionHandler {
    //에러 메시지 전송


    //execptionhandler ==특정 예외가 발생했을 때 해당 메서드를 호출하도록 지정
    //MethodArgumentNotValidException == 요청이 유효성 검증을 통과하지 못했을 때 발생
    //HttpMessageNotReadableException == 요청 본문이 읽을 수 없는 형식일 때 발생
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class}) //중괄호 쳐서 배열로 전달
    public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception){
        exception.printStackTrace(); // 디버깅을 위해 예외 스택 트레이스 출력
        return ResponseDto.validationFail();
    }

}