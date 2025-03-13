package com.example.oauth.filter;

import com.example.oauth.entity.UserEntity;
import com.example.oauth.provider.JwtProvider;
import com.example.oauth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {// 한번만 동작

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = ParseBearerToken(request);
            if (token == null) { //Authorization,Bearer 가 아닐경우 진행하지 않게 한다
                filterChain.doFilter(request,response); //다음 필터로 이동
                return;
            }

            String userId = jwtProvider.Validate(token);
            if (userId == null){
                filterChain.doFilter(request,response); //다음 필터로 이동.

                return;
            }

            // 유저정보를 가져온다
            UserEntity userEntity =  userRepository.findByUserId(userId);
            String role = userEntity.getRole(); //role = ROLE_USER,ROLE_ADMIN

            System.out.println(role);

            //권한이 여러개일 수도 있기 때문에 list로 받는다
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            //빈 Context 생성
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            //이 토큰에는 유저 정보와 권한을 넣어준다 userId= 접근 주체에 대한 정보, 비밀번호는 설정안할거기 때문에 null로 처리
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId,null,authorities);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);


        }catch (Exception exception){
            exception.printStackTrace();
        }

        filterChain.doFilter(request,response); //다음 필터로 넘어가게 한다

    }

    //로그인한 사용자가 요청 헤더에 포함시킨 Bearer 토큰 값을 추출하는 역할
    private String ParseBearerToken(HttpServletRequest request){

        String authorization = request.getHeader("Authorization"); //이이름 그대로 사용하기 틀리면 에러 발생

        //hasText= 값이 있는지 확인
        boolean hasAuthorization = StringUtils.hasText(authorization); //hasnext는 만약에 null이 아니거나 길이가 0이 아닌상태를 검사한다 = 값이 있는지 검사

        if (!hasAuthorization) return null; //true가 아닐경우 null 반환

        //authorization bearer인증 방식인지 확인
        boolean isBearer = authorization.startsWith("Bearer "); //Bearer 한칸 띄우고 로 시작하는지 검사
        if (!isBearer) return null; //false 면 null반환

        String token = authorization.substring(7); //위에 Bearer 에 7번(띄워쓰기 포함)째부터 요소를 가지고 온다

        return token;
    }
}
