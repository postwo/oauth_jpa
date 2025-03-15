package com.example.oauth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "certification")
public class CertificationEntity {

    @Id
    private String userId;
    private String email;
    private String certificationNumber; // 메일로 온 인증번호
}
