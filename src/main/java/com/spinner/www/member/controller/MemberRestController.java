package com.spinner.www.member.controller;

import com.spinner.www.common.io.CommonResponse;
import com.spinner.www.member.io.EmailAuthRequest;
import com.spinner.www.member.io.EmailSend;
import com.spinner.www.member.io.MemberLogin;
import com.spinner.www.member.io.MemberJoin;
import com.spinner.www.member.service.EmailService;
import com.spinner.www.member.service.RedisService;
import com.spinner.www.member.service.TokenService;
import com.spinner.www.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberRestController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final TokenService tokenService;

    /**
     * 회원가입
     * @param memberJoin MemberJoin 회원가입 요청
     * @return ResponseEntity<CommonResponse> 회원가입 결과
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> joinMember(@ModelAttribute MemberJoin memberJoin) {
        return memberService.insertUser(memberJoin);
    }

    /**
     * 로그인
     * @param userLoginRequest UserLoginDto 로그인 요청 데이터
     * @return ResponseEntity<CommonResponse> 로그인 결과
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> loginUser(@RequestBody MemberLogin userLoginRequest) {
        return memberService.loginMember(userLoginRequest);
    }

    /**
     * 이메일 인증
     * @param emailSend EmailSend
     * @return ResponseEntity<CommonResponse> 인증 코드 발송 결과
     */
    @PostMapping("/sendEmail")
    public ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailSend emailSend) {
        return emailService.sendEmail(emailSend.getEmail(), emailSend.getType());
    }

    /**
     * 이메일 재발송
     * @param emailSend EmailSend
     * @return ResponseEntity<CommonResponse> 인증 코드 재발송 결과
     */
    @PostMapping("/reSendEmail")
    public ResponseEntity<CommonResponse> reSendEmail(@RequestBody EmailSend emailSend) {
        return emailService.reSendEmail(emailSend.getEmail(), emailSend.getType());
    }

    /**
     * 이메일 인증코드 검증
     * @param emailAuthRequest EmailAuthRequest
     * @return ResponseEntity<CommonResponse> 인증코드 비교 결과
     */
    @PostMapping("/authCode")
    public ResponseEntity<CommonResponse> getAuthCode(@RequestBody EmailAuthRequest emailAuthRequest){
        return emailService.invalidateAuthCode(emailAuthRequest);
    }

    @GetMapping("/main")
    public String test(){
        return "success";
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            @RequestParam("memberIdx") Long memberIdx) {
        return tokenService.refreshToken(refreshToken, memberIdx);
    }
}
