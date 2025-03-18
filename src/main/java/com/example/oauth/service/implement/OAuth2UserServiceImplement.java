package com.example.oauth.service.implement;

import com.example.oauth.entity.CustomOAuth2User;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        //OAuth 2.0을 통해 로그인을 신청한 사용자의 정보를 가져온다는 뜻
        OAuth2User oAuth2User = super.loadUser(request);

        String oauthClientName = request.getClientRegistration().getClientName();

        //이거는 oauth 내용이 서버에 잘 전달되는지 테스트해보기 위해 작성
//        try {
//            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        UserEntity userEntity = null;
        String userId = null;
        String email = "email@email.com";

        if (oauthClientName.equals("kakao")){
            userId = "kakao_"+oAuth2User.getAttributes().get("id");
            userEntity = new UserEntity(userId,email,"kakao");
        }

        if (oauthClientName.equals("naver")){
            //"response":{"id":"H8_4CMebvViPliJbjBVhmxm6E-aonjSjm8kKv2wj0NA","email":"cos@naver.com","name":"chichieo"}}
            Map<String,String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            userId = "naver_"+ responseMap.get("id").substring(0,14);
            email = responseMap.get("email");
            userEntity = new UserEntity(userId,email,"naver");
        }

        userRepository.save(userEntity);

        return new CustomOAuth2User(userId);

    }
}
