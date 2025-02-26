package com.spinner.www.study.entity;

import com.spinner.www.common.entity.BaseEntity;
import com.spinner.www.file.entity.Files;
import com.spinner.www.study.dto.StudyCreateDto;
import com.spinner.www.study.dto.StudyUpdateDto;
import com.spinner.www.study.io.StudyUpdateRequest;
import com.spinner.www.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Study extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyIdx")
    private Long id;

    @Comment("스터디 이름")
    private String studyName;

    @Comment("스터디 소개")
    private String studyIntro;

    @OneToOne
    @JoinColumn(name = "fileIdx")
    @Comment("스터디 소개 사진")
    private Files files;

    @Comment("스터디 삭제 여부")
    private String studyIsRemoved;

    @OneToOne
    @JoinColumn(name = "tagIdx")
    @Comment("스터디 분야")
    private Tag tag;

    @Comment("스터디 최대 인원")
    private int studyMaxPeople;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StudyMember> studyMembers = new ArrayList<>();

    /**
     * 연관관계 메서드 설정
     * @param studyMember StudyMember
     */
    public void addStudyMember(StudyMember studyMember) {
        if (!this.studyMembers.contains(studyMember)) {
            this.studyMembers.add(studyMember);
            studyMember.addToStudy(this);
        }
    }

    public static Study create(StudyCreateDto studyCreateDto) {
        return Study.builder()
            .studyName(studyCreateDto.getStudyName())
            .studyIntro(studyCreateDto.getStudyIntro())
            .studyMaxPeople(studyCreateDto.getStudyMaxPeople())
            .studyIsRemoved("N")
            .build();
    }

    public void updateFile(Files files) {
        this.files = files;
    }

    public void update(StudyUpdateDto studyUpdateDto) {
        this.studyName = studyUpdateDto.getStudyName();
        this.studyIntro = studyUpdateDto.getStudyIntro();
        this.studyMaxPeople = studyUpdateDto.getStudyMaxPeople();
        this.studyIsRemoved = "N";
    }

    public void softDelete() {
        this.studyIsRemoved = "Y";
    }
}
