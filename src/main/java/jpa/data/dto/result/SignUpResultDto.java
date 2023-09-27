package jpa.data.dto.result;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SignUpResultDto {
    private boolean success;
    private int code;
    private String msg;
}
