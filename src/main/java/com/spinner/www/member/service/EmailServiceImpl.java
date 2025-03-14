package com.spinner.www.member.service;


import com.spinner.www.common.io.CommonResponse;
import com.spinner.www.constants.CommonResultCode;
import com.spinner.www.member.dto.SessionInfo;
import com.spinner.www.member.entity.EmailLog;
import com.spinner.www.member.entity.EmailTemplate;
import com.spinner.www.member.entity.Member;
import com.spinner.www.member.io.EmailAuthRequest;
import com.spinner.www.util.EmailTemplateCacheStorage;
import com.spinner.www.util.ResponseVOUtils;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService{

    private static final int DEFAULT_AUTHCODE_MINUTES = 5;
    private final JavaMailSender javaMailSender;
    private final EmailTemplateCacheStorage emailTemplateCacheStorage;
    private final RedisService redisService;
    private final SessionInfo sessionInfo;
    private final EmailLogService emailLogService;
    private final MemberService memberService;

    /**
     * 6자리 랜덤 숫자 (인증코드)
     * @return authCode String
     */
    @Override
    public String createAuthCode() {
        StringBuilder authCode = new StringBuilder();
        Random random = new Random();
        for (int i =0; i< 6; i++){
            authCode.append(random.nextInt(10));
        }
        return authCode.toString();
    }

    /**
     * 이메일 전송 메서드
     *
     * @param toEmail 수신자 이메일 주소
     * @param authCode 인증 코드
     * @param emailTemplate 이메일 템플릿
     * @return ResponseEntity<CommonResponse> 이메일 전송 결과
     */
    private ResponseEntity<CommonResponse> sendEmailMessage(String toEmail, String authCode, EmailTemplate emailTemplate) {
        try {
            String body = emailTemplate.getEmailTemplateBody().replace("{authCode}", authCode);

            // 이메일 메시지 생성
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress("spinner.mgr@gmail.com", "스피너팀"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(emailTemplate.getEmailTemplateTitle());
            message.setText(body, "UTF-8");

            // 이메일 발송
            javaMailSender.send(message);

            // 이메일 로그
            EmailLog emailSend = EmailLog.insertEmailLog(toEmail, emailTemplate);
            emailLogService.saveEmailSend(emailSend);

            return new ResponseEntity<>(ResponseVOUtils.getSuccessResponse(authCode), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseVOUtils.getFailResponse(CommonResultCode.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 인증 메일 전송
     *
     * @param toEmail 수신자 이메일 주소
     * @param type 이메일 템플릿 유형
     * @return ResponseEntity<CommonResponse> 이메일 전송 결과
     */
    @Override
    public ResponseEntity<CommonResponse> sendEmail(String toEmail, String type) {

        // 이메일 중복 검사
        boolean member = memberService.isEmailInvalid(toEmail);
        if(member){
            return new ResponseEntity<>(ResponseVOUtils.getFailResponse(CommonResultCode.DUPLICATE), HttpStatus.BAD_REQUEST);
        }

        // 인증 코드 생성
        String authCode = createAuthCode();

        // 이메일 템플릿 조회
        EmailTemplate emailTemplate = emailTemplateCacheStorage.getEmailTemplate(type);
        if (emailTemplate == null) {
            throw new IllegalArgumentException("유효하지 않은 이메일 템플릿 유형입니다: " + type);
        }

        // Redis에 인증 코드 저장
        redisService.saveRedis(toEmail, authCode, DEFAULT_AUTHCODE_MINUTES, TimeUnit.MINUTES);

        // 이메일 발송
        return sendEmailMessage(toEmail, authCode, emailTemplate);
    }

    /**
     * 인증 메일 재전송
     *
     * @param toEmail 수신자 이메일 주소
     * @param type 이메일 템플릿 유형
     * @return ResponseEntity<CommonResponse> 이메일 전송 결과
     */
    @Override
    public ResponseEntity<CommonResponse> reSendEmail(String toEmail, String type) {

        // Redis에서 인증 코드 조회
        String authCode = redisService.getValue(toEmail);

        // 이메일 템플릿 조회
        EmailTemplate emailTemplate = emailTemplateCacheStorage.getEmailTemplate(type);
        if (emailTemplate == null) {
            throw new IllegalArgumentException("유효하지 않은 이메일 템플릿 유형입니다: " + type);
        }

        if (authCode != null) {
            // Redis에서 인증 코드가 존재하면 기존 코드로 이메일 재발송
            return sendEmailMessage(toEmail, authCode, emailTemplate);
        } else {
            // 인증 코드가 없다면 새로 생성하여 이메일 발송
            return sendEmail(toEmail, type);
        }
    }

    /**
     * 이메일 인증코드 검증
     * @param emailAuthRequest EmailAuthRequest
     * @return ResponseEntity<CommonResponse> 인증코드 비교 결과
     */
    @Override
    public ResponseEntity<CommonResponse> invalidateAuthCode(EmailAuthRequest emailAuthRequest) {
        String authCode = redisService.getValue(emailAuthRequest.getEmail());
        if (emailAuthRequest.getAuthCode() == null || emailAuthRequest.getAuthCode().isEmpty())  {
            return new ResponseEntity<>(ResponseVOUtils.getFailResponse(1002, "인증 코드를 적어주세요"),HttpStatus.BAD_REQUEST);
        }
        if (authCode == null || !authCode.equals(emailAuthRequest.getAuthCode()))  {
            return new ResponseEntity<>(ResponseVOUtils.getFailResponse(1001, "인증 코드가 유효하지 않습니다."),HttpStatus.UNAUTHORIZED);
        }
        // 이메일 인증이 완료된 후 연장시간 10분으로 늘려야함
        redisService.saveRedis(emailAuthRequest.getEmail(), "success", 10, TimeUnit.MINUTES);
        sessionInfo.setMemberEmail(emailAuthRequest.getEmail());
        return new ResponseEntity<>(ResponseVOUtils.getSuccessResponse("인증이 완료되었습니다."), HttpStatus.OK);
    }


}
