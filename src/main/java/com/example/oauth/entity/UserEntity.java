package com.example.oauth.entity;

import com.example.oauth.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class UserEntity {
    @Id
    private String userId;

    private String password;

    private String email;

    private String type;

    private String role;

    public UserEntity (SignUpRequestDto dto){
        this.userId = dto.getId();
        this.password = dto.getPassword();
        this.email  =  dto.getEmail();
        this.type = "app";
        this.role = "ROLE_USER";
    }
}