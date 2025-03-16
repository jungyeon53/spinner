package com.spinner.www.member.service;

import com.spinner.www.common.io.CommonResponse;
import com.spinner.www.member.entity.Member;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface TokenService {

    /**
     * 토큰 생성
     * @param expiration Date
     * @param member Member
     * @return token
     */
    String makeToken(Date expiration, Member member);

    boolean validateAcessToken(String acessToken);
    boolean validateRefreshToken(Long memberIdx, String refreshToken);

    String getUserEmailFromToken(String token);

    ResponseEntity<CommonResponse> refreshToken(String refreshToken, Long memberIdx);
}

