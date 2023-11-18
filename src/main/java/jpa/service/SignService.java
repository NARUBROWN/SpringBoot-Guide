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
import java.util.List;
import java.util.Objects;

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

        log.info("password : {} {}", password, user.getPassword());


        User savedUser = userRepository.save(user);

        log.info(String.valueOf(passwordEncoder.matches(password, savedUser.getPassword())));

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
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }
        log.info("[getSignInResult] 패스워드 일치");
        log.info("[getSignInResult] SignInResultDto 객체 생성");
        // Refresh Token 정보를 DB에 저장
        user.updateRefreshToken(jwtTokenProvider.createRefreshToken(user.getUid()));
        userRepository.save(user);

        return SignInResultDto.builder()
                .success(true)
                .code(CommonResponse.SUCCESS.getCode())
                .msg(CommonResponse.SUCCESS.getMsg())
                .token(new String[]{
                        jwtTokenProvider.createAccessToken(String.valueOf(user.getUid()), user.getRoles()),
                        userRepository.getByUid(id).getRefreshToken()
                })
                .build();
    }

    public SignInResultDto crateNewRefreshToken(String token) throws Exception {
        // 토큰에서 유저 아이디 추출
        String uid = jwtTokenProvider.getUsername(token);
        if (Objects.equals(token, userRepository.getByUid(uid).getRefreshToken())) {
            log.info(token, userRepository.getByUid(uid).getRefreshToken());
            // DB 토큰과 요청 받은 토큰이 일치한다면
            // 새로운 리프레쉬 토큰을 발급
            String newRefreshToken = jwtTokenProvider.createRefreshToken(uid);
            // 새로운 리프레쉬 토큰을 DB에 저장
            User user = userRepository.getByUid(uid);
            user.updateRefreshToken(newRefreshToken);
            userRepository.save(user);
            return SignInResultDto.builder()
                    .success(true)
                    .code(CommonResponse.SUCCESS.getCode())
                    .msg(CommonResponse.SUCCESS.getMsg())
                    .token(new String[]{
                            jwtTokenProvider.createAccessToken(uid, getUserRoles(uid)),
                            newRefreshToken
                    })
                    .build();
        } else {
            throw new Exception("오류");
        }
        }



    public List<String> getUserRoles(String uid) {
        return userRepository.getByUid(uid).getRoles();
    }
}
