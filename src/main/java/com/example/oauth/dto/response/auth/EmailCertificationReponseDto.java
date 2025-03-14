package com.example.oauth.dto.response.auth;

import com.example.oauth.common.ResponseCode;
import com.example.oauth.common.ResponseMessage;
import com.example.oauth.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EmailCertificationReponseDto extends ResponseDto {

    private EmailCertificationReponseDto(){
        super();
    }


    public static ResponseEntity<EmailCertificationReponseDto> success(){
        EmailCertificationReponseDto reponseDto = new EmailCertificationReponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(reponseDto);
    }


    public static ResponseEntity<ResponseDto> duplicateId(){
        ResponseDto responsebody = new ResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsebody);
    }


    public static ResponseEntity<ResponseDto> mailsendFail(){
        ResponseDto responsebody = new ResponseDto(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responsebody);
    }
}
