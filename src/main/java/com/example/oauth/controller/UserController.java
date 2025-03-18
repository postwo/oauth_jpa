package com.example.oauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    //test
    //권한이 user면 조회 가능하게 admin이면 nopermision이 뜨게 된다
    @GetMapping("/aa")
    public String aa(){
        return "aa";
    }
}
