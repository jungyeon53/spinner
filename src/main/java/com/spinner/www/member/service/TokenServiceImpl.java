package com.spinner.www.member.service;

import com.spinner.www.common.io.CommonResponse;
import com.spinner.www.constants.CommonResultCode;
import com.spinner.www.member.dto.JwtProperties;
import com.spinner.www.member.entity.Member;
import com.spinner.www.util.ResponseVOUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService{

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;

    /**
     * key 객체 생성
     * @return Keys
     */
    private Key getSignKey(){
        byte [] key = Base64.getDecoder().decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(key);
    }

    /**
     * 토큰 생성
     * @param expiration Date
     * @param member Member
     * @return 만들어진 토큰 반환
     */
    @Override
    public String makeToken(Date expiration, Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(member.getMemberEmail())
                .claim("nickname", member.getMemberNickname())
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    /**
     * acessToken 유효성 검증
     * @param acessToken String
     * @return boolean
     */
    @Override
    public boolean validateAcessToken(String acessToken){
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build().parseClaimsJws(acessToken);
            return true;
        } catch (ExpiredJwtException e){
            System.out.println("토큰 만료");
        } catch (JwtException e){
            System.out.println("이상한 토큰");
        }
        return false;
    }

    /**
     * refreshToken 유효성 검증
     * @param memberIdx Long
     * @param refreshToken String
     * @return boolean
     */
    @Override
    public boolean validateRefreshToken(Long memberIdx, String refreshToken){
        String token = redisTemplate.opsForValue().get(memberIdx);
        return refreshToken.equals(token);
    }

    /**
     * 토큰에서 이메일 추철
     * @param token String
     * @return claims
     */
    @Override
    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
        return claims.getSubject();
    }

    @Override
    public ResponseEntity<CommonResponse> refreshToken(String refreshToken, Long memberIdx) {

        // refreshToken이 유효하지 않으면
        if(refreshToken == null || !validateRefreshToken(memberIdx, refreshToken)){
            return new ResponseEntity<>(ResponseVOUtils.getFailResponse(CommonResultCode.DATA_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        Member member = memberService.getMember(memberIdx);
        String newToken = makeToken(new Date(System.currentTimeMillis() + Duration.ofDays(7).toMillis()), member);
        return null;
    }
}
