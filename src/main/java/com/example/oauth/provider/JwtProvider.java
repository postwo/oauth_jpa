package com.example.oauth.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${spring.jwt.secret-key}") //yml파일에 있는 키값을 가지고 온다
    private String secretKey;

    //jwt 생성 메소드
    public String create(String userId) {

        //현재 시간으로부터 1시간 후를 만료 시간으로 설정
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));//추가한 날짜 , //1시간    //만료기간

        //주입받은 비밀 키를 사용하여 HMAC SHA 알고리즘용 키 객체를 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));// 시크릿키를 만든다

        //payload에는 sub==subject,iat==setIssuedAt,exp==setExpiration 등 이 들어간다 -> 결론적으로는 내가 넣고 싶은대로 값을 변경할수 있다 
        String jwt = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256) //위에서 생성한 키와 HS256 알고리즘을 사용하여 JWT를 서명
                .setSubject(userId) //JWT의 subject(주제) 필드에 사용자 ID를 설정
                .setIssuedAt(new Date()).setExpiration(expiredDate)  //JWT가 발행된 시간을 현재 시간으로 설정
                .compact(); //JWT 문자열을 생성
        return jwt;

    }


    //jwt 검증
    public String Validate(String jwt){//주어진 JWT를 검증하고, 유효한 경우 해당 JWT의 subject를 반환하는 메소드

        //payload == claims 같은 뜻이다
        String subject = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try{
            subject = Jwts.parserBuilder()
                    .setSigningKey(key) // JWT의 서명을 검증하기 위한 키를 설정
                    .build()
                    .parseClaimsJws(jwt) //주어진 JWT를 파싱하고 서명을 검증
                    .getBody()
                    .getSubject(); //JWT의 페이로드에서 subject를 추출하여 변수에 저장

        }catch (Exception exception){
            exception.printStackTrace();
            return null; //실패하면 null
        }

        return subject;
    }

}
