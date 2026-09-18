package com.global.login.dto;

import lombok.Getter;
import lombok.Setter;

//로그인 전달 파라미터


@Getter @Setter
public class LoginRequest {
    private String loginId;
    private String loginPw;
}
