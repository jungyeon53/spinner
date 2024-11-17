package com.spinner.www.users.entity;

import com.spinner.www.constants.ServiceTermsType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

import org.hibernate.annotations.Comment;

@Entity
@Table(name = "serviceTerms")
@Comment("약관 테이블")
public class ServiceTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("약관PK")
    private Long serviceTermsIdx;

    @Comment("이메일 수신 동의 or 앱 푸쉬 동의 ")
    private ServiceTermsType serviceTermsType;
    @Comment("약관내용")
    private String serviceTermsContent;

}
