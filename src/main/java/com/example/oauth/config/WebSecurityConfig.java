package com.example.oauth.config;

import com.example.oauth.filter.JwtAuthenticationFilter;
import com.example.oauth.handler.OAuth2SuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final DefaultOAuth2UserService defaultOAuth2UserService;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception{

        http
                .cors(cors-> cors
                        .configurationSource(corsConfigurationSource())
                )

                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(request -> request
                                .requestMatchers("/","/api/v1/auth/**","/oauth2/**").permitAll()
                                .requestMatchers("/api/v1/user/**").hasRole("USER")
                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/v1/logispw/**").hasAnyRole("USER","ADMIN")
                                .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 ->oauth2
                        //http://localhost:4040/oauth2/authorization/{kakao,naver} == 이경로는 스프링시큐리티 자체 경롱이다 그러므로 이 요청명을 api/v1/auth/oauth2 이걸로 커스텀
                        //http://localhost:4040/api/v1/auth/oauth2/{kakao,naver} 이렇게 커스텀 된다
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                        .redirectionEndpoint(endpoint-> endpoint.baseUri("/oauth2/callback/*")) //redirect url => 이게 네이버 로그인페이지로 이동시키는 역할을 한다
                        .userInfoEndpoint(endpoint -> endpoint.userService(defaultOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )

                //authorizeHttpRequests 에서 발생한 exception 이발생 한걸 처리
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint()) //인스턴스를 넘겨준다
                )

                //필터 등록
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); //모든 출처에 대해서 허용하겠다
        configuration.addAllowedMethod("*"); //모든 메서드에 대해서 허용하겠다
        configuration.addAllowedHeader("*"); //모든 헤더에 대해서 허용하겠다

        //예제
//        CorsConfiguration configurationv1 = new CorsConfiguration();
//        configuration.addAllowedOrigin("*"); //모든 출처에 대해서 허용하겠다
//        configuration.addAllowedMethod("*"); //모든 메서드에 대해서 허용하겠다
//        configuration.addAllowedHeader("*"); //모든 헤더에 대해서 허용하겠다

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 이렇게 2개를 적용 시킬수도 있다
        //        source.registerCorsConfiguration("/api/v1/**",configurationv1);
        //        source.registerCorsConfiguration("/api/v2/**",configuration);
        source.registerCorsConfiguration("/**",configuration); //모든 패턴에 대해 적용 시킨다

        return source;
    }


}

//위에 권한으로 페이지 접속 실패시
class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 권한이 없다고 표시해준다
        // "{code:"NP", "message":"No permisssion."}"
        response.getWriter().write("{\"code\" : \"NP\" ,\"message\" : \"No Permission.\"}"); //인증 실패시 이값을 반환 한다
    }
}
