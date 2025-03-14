package com.example.oauth.common;

public class CertificationNumber {

    // 인증 번호 생성
    public static String getCertificationNumber(){

        String certificationNumber = " ";
        //Math.random == 0.0 이상 1.0 미만의 소수점을 반환
        for (int count = 0; count<4; count++) certificationNumber += (int) (Math.random() * 10); //0~9 사이의 숫자를 임의로 받아온다

        return certificationNumber;
    }
}
