package com.spinner.www.report.entity;

import com.spinner.www.common.entity.BaseEntity;
import com.spinner.www.member.entity.Member;
import com.spinner.www.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportIdx")
    @Comment("신고 PK")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportTypeIdx")
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postIdx")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberIdx")
    private Member member;

    protected Report() {}

    /**
     * 생성 메서드
     */
    public static Report create(ReportType reportType, Post post, Member member) {
        return  Report.builder()
                .reportType(reportType)
                .post(post)
                .member(member)
                .build();
    }
}
