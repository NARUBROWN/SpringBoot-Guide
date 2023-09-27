package jpa.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Getter
@RequiredArgsConstructor
/*
    OncePerRequestFilter를 상속받아 사용하는 것이 편하다.
    대표적으로 OncePerRequestFilter와 GenericFilterBean을 사용하는데
    GernericFilterBean은 작동 구조 때문에 필터가 두 번 실행되는 현상이 발생한다.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ServletRequest에서 토큰을 추출한다.
        String token = jwtTokenProvider.resolveToken(request);
        log.info("[doFilterInternal] token 값 유효성 체크 시작");

        // 유효성 검사를 진행한다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하다면 Authentication 객체를 생성해서 SecurityContextHolder에 대한 유효성을 검사한다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[doFilterInternal] token 값 유효성 체크 완료");
        }

        /*
            서블릿을 실행하는 메소드이다. doFilter를 기준으로 앞에 작성한 코드는 서블릿이 실행되기 전에 실행되고,
            뒤에 작성한 코드는 서블릿이 실행된 이후에 실행된다.
        */
        filterChain.doFilter(request, response);
    }
}
