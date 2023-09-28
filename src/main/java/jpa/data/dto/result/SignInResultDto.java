package jpa.data.dto.result;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignInResultDto {
    private boolean success;
    private int code;
    private String msg;
    private String token;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.token = token;
    }
}
