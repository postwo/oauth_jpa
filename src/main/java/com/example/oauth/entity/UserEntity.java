package com.example.oauth.entity;

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
}