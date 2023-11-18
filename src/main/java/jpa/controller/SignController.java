package jpa.controller;

import io.swagger.annotations.ApiParam;
import jpa.data.dto.result.SignInResultDto;
import jpa.data.dto.result.SignUpResultDto;
import jpa.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sign-api")
@RequiredArgsConstructor
@Slf4j
public class SignController {
    private final SignService signService;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResultDto> signIn(
            @ApiParam(value = "ID", required = true) @RequestParam String id,
            @ApiParam(value = "Password", required = true) @RequestParam String password) throws RuntimeException {
        log.info("[signIn] 로그인을 시도하고 있습니다. id {}, pw : ****", id);
        SignInResultDto signInResultDto = signService.signIn(id, password);
        return ResponseEntity.status(HttpStatus.OK).body(signInResultDto);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResultDto> signUp(
            @ApiParam(value = "ID", required = true) @RequestParam String id,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "이름", required = true) @RequestParam String name,
            @ApiParam(value = "권한", required = true) @RequestParam String role) {
        log.info("[signUp] 회원가입을 수행합니다. id : {}, password: ****, name : {}, role : {}", id, name, role );
        SignUpResultDto signUpResultDto = signService.signUp(id, name, password, role);
        log.info("[signUp] 회원가입을 완료했습니다. id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(signUpResultDto);
    }

    @GetMapping("/refresh")
    public ResponseEntity<SignInResultDto> getNewRefreshToken(String refreshToken) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(signService.crateNewRefreshToken(refreshToken));
    }

    @GetMapping(value = "/exception")
    public void exceptionTest() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었습니다.");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, httpHeaders, httpStatus);
    }


}
