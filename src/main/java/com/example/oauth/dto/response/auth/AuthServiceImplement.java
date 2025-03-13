package com.example.oauth.dto.response.auth;

import com.example.oauth.dto.request.auth.IdCheckRequestDto;
import com.example.oauth.dto.response.ResponseDto;
import com.example.oauth.repository.UserRepository;
import com.example.oauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try{
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if (isExistId) return IdCheckResponseDto.duplicateId(); // true일경우

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IdCheckResponseDto.success();
    }
}