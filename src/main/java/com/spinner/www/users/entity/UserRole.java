package com.spinner.www.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;


@Entity
@Table(name = "userRole")
@Comment("유저 권한 테이블")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("권한PK")
    private int roIdx;
    @Comment("권한이름")
    private String roName;
}
