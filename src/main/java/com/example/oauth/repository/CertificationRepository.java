package com.example.oauth.repository;


import com.example.oauth.entity.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<CertificationEntity,String> {

    //아이디 조회
    CertificationEntity findByUserId(String userId);
}
