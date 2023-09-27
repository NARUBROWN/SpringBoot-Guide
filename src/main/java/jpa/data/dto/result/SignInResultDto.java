package jpa.data.dto.result;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignInResultDto {
    private boolean success;
    private int code;
    private String msg;

    private String token;

    public SignInResultDto(boolean success, int code, String msg) {
        new SignUpResultDto(success, code, msg);
    }

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token) {
        new SignUpResultDto(success, code, msg);
        this.token = token;
    }
}
