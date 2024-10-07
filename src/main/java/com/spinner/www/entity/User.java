package com.spinner.www.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo; // 회원번호
    private String email; // 이메일
    private String nickname; // 닉네임
    private String username; // 이름
    private String password; // 비밀번호

}
