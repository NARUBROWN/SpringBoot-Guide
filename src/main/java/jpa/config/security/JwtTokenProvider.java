package jpa.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKey";

    /*
        @PostConstruct는 해당 객체가 빈 객체로 주입된 이후 수행되는 메소드를 가리킨다.
        JwtTokenProvider 클래스에는 @Component 어노테이션이 지정되어 있어 애플리케이션이 가동되면서 빈으로 자동주입된다.
        그때 @PostConstruct가 지정되어 있는 init() 메소드가 자동으로 실행된다.

        init() 메소드에서는 secretKey를 Base64 형식으로 인코딩한다.
    */
    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider 내 secretkey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] JwtTokenProvider 내 secreKey 초기화 완료");
    }

    public String createToken(String userUid, List<String> roles) {
        log.info("[createToken] 토큰 생성 시작");
        /*
            JWT 토큰의 내용에 값을 넣기 위해 Clamis 객체를 생성한다.
            setSubject() 메서드를 통해 sub 속성에 값을 추가하려면 User의 uid 값을 사용합니다.
        */
        Claims claims = Jwts.claims().setSubject(userUid);
        /*
            해당 토큰을 사용하는 사용자의 권한을 확인할 수 있는 role 값을 별개로 추가
        */
        claims.put("roles", roles);
        Date now = new Date();

        long tokenValidMillisecond = 1000L * 60 * 60;

        // Jwts.builder를 사용해 토큰을 생성
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        log.info("[createToken] 토큰 생성 완료");
        return token;
    }

    /*
        이 메소드는 필터에서 인증이 성공했을 때 SecurityContextHolder에 저장할 Authentication을 생성하는 역할을 한다.
        Authentication을 구현하는 편한 방법은 UsernamePasswordAuthenticationToken을 사용하는 것이다.

        UsernamePasswordAuthenticationToken 클래스를 사용하기 위해서는 초기화를 위한 UserDetails가 필요하다.
    */
    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        /*
            초기화를 위한 UserDetails는 UserDetailsService를 통해 가져오게 된다.
        */
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        // Jwts.parser()를 통해 secretKey를 설정하고 클레임을 추출해서 토큰을 생성할 때 넣었던 sub 값을 추출한다.
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    /*
        이 메소드는 HttpServletRequest를 파라미터로 받아 헤더 값으로 전달된 'X-AUTH-TOKEN' 값을 가져와 리턴한다.
     */
    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    /*
        이 메소드는 토큰을 전달 받아 클레임의 유효기간을 체크하고 boolean 타입 값을 리턴하는 역할을 한다.
    */
    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
