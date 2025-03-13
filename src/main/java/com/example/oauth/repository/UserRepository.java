package com.example.oauth.repository;

import com.example.oauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,String> {

    //반환 타입이 UserEntity인 이유는 데이터베이스에서 가져온 데이터를 UserEntity 객체의 필드에 매핑하여 사용하기 위함
    UserEntity findByUserId(String userId);

    boolean existsByUserId(String userId);
}
