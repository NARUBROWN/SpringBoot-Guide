package jpa.service;

import jpa.config.security.JwtTokenProvider;
import jpa.data.dto.common.CommonResponse;
import jpa.data.dto.result.SignInResultDto;
import jpa.data.dto.result.SignUpResultDto;
import jpa.data.entity.User;
import jpa.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignService {
    public final UserRepository userRepository;
    public final JwtTokenProvider jwtTokenProvider;
    public final PasswordEncoder passwordEncoder;

    public SignUpResultDto signUp(String id, String name, String password, String role) {
        log.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;
        if(role.equalsIgnoreCase("admin")) {
            user = User.builder()
                    .uid(id)
                    .name(name)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        } else {
            user = User.builder()
                    .uid(id)
                    .name(name)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }

        log.info("테스트" + passwordEncoder.matches(password, user.getPassword()));

        User savedUser = userRepository.save(user);

        log.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if (!savedUser.getName().isEmpty()) {
            log.info("[getSignUpResult] 정상 처리 완료");
            return new SignUpResultDto(true, CommonResponse.SUCCESS.getCode(), CommonResponse.SUCCESS.getMsg());
        } else {
            log.info("[getSignUpResult] 실패 처리 완료");
            return new SignUpResultDto(false, CommonResponse.FAIL.getCode(), CommonResponse.FAIL.getMsg());
        }
    }

    public SignInResultDto signIn(String id, String password) throws RuntimeException {
        log.info("[getSignInResult] signDataHandler로 회원 정보 요청");
        User user = userRepository.getByUid(id);
        log.info("[getSignInResult] id : {}", id);
        log.info("[getSignInResult] 패스워드 비교 수행 " + password);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }
        log.info("[getSignInResult] 패스워드 일치");
        log.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRoles()))
                .build();
        log.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        new SignInResultDto(true, CommonResponse.SUCCESS.getCode(), CommonResponse.SUCCESS.getMsg());
        return signInResultDto;
    }

}
