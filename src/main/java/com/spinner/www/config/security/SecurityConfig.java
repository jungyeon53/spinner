package com.spinner.www.config.security;

import com.spinner.www.config.handler.CustomerLogoutHandler;
import com.spinner.www.member.dto.SessionInfo;
import com.spinner.www.member.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security
 * 스프링 시큐리티 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /** 추 후 url 추가 예정 */
    private static  final String[] PERMIT_URL_ARRAY = {};

    private final SessionInfo sessionInfo;
    private final CustomerLogoutHandler customerLogoutHandler;
    private final JwtTokenFilter jwtTokenFilter;

    /**
     * 비밀번호 단방향 암호화
     * [note] 복호화가 불가능하며 입력된 비밀번호를 암호화하여 비교 -> 암호화 알고리즘 재검토 필요
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 스프링 시큐리티 암호화 설정
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OauthService oauthService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // 토큰 검증
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 소셜 로그인
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauthService)
                        )
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/member/main");
                        })
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            sessionInfo.logout();
                        })
                        .logoutSuccessHandler(customerLogoutHandler)
                        .deleteCookies("JSESSIONID", "refreshToken")
                );
        return http.build();
    }


}
