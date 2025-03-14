package com.spinner.www.member.service;


import com.spinner.www.common.io.CommonResponse;
import com.spinner.www.member.dto.MemberSessionDto;
import com.spinner.www.member.entity.Member;
import com.spinner.www.member.io.MemberLogin;
import com.spinner.www.member.io.MemberJoin;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface MemberService {

    /**
     * 회원가입
     * @param memberRequest UserRequestDto 회원가입 요청 데이터
     * @return ResponseEntity<CommonResponse> 회원가입 결과
     */
    ResponseEntity<CommonResponse> insertUser(MemberJoin memberRequest);

    /**
     * 로그인
     * @param memberLogin MemberLogin 로그인 요청 데이터
     * @return ResponseEntity<CommonResponse> 로그인 결과
     */
    ResponseEntity<CommonResponse> loginMember(MemberLogin memberLogin);

    /**
     * 쿠키에 refreshToken 저장
     * @param response HttpServletResponse
     * @param refreshToken String
     * @param expiryDate int
     */
    void setRefreshTokenCookie(HttpServletResponse response, String refreshToken , int expiryDate);

    /**
     * idx로 회원 조회
     * @param memberIdx Long
     * @return Member
     */
    Member getMember(Long memberIdx);

    /**
     * 이메일로 회원조회
     * @param memberEmail String
     * @return Member
     */
    Member getMember(String memberEmail);

    /**
     * 토큰 생성
     * @param member Member
     * @param memberSessionDto MemberLoginDto
     */
    void makeLoginToken(Member member, MemberSessionDto memberSessionDto);

    /**
     * 이메일 중복검사
     * @param memberEmail String
     * @return boolean
     */
    boolean isEmailInvalid (String memberEmail);
}
