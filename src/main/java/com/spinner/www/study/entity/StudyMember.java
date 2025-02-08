package com.spinner.www.study.entity;

import com.spinner.www.common.entity.BaseEntity;
import com.spinner.www.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyMember extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyMemberIdx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyIdx")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberIdx")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Comment("스터디 상태 타입 | waiting (대기), join (가입)")
    private StudyMemberStatusType studyMemberStatus;

    @Enumerated(EnumType.STRING)
    @Comment("스터디 롤 타입 | leader (스터디장), member (스터디원)")
    private StudyMemberRoleType studyMemberRole;

    @Comment("삭제 여부")
    private String studyMemberRemoved;

    @Comment("가입 신청 소개")
    private String studyMemberJoinIntro;

    // 연관관계 메서드
    public void addToStudy(Study study) {
        if (this.study == null) {
            this.study = study;
        }
    }
}
