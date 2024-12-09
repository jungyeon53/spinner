package com.spinner.www.vote.entity;

import com.spinner.www.common.entity.BaseEntity;
import com.spinner.www.vote.dto.VoteItemCreateDto;
import com.spinner.www.vote.dto.VoteItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voteItemIdx")
    @Comment("투표_항목 idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voteIdx")
    @Comment("투표 idx")
    private Vote vote;

    @Comment("투표_항목 이름")
    private String voteItemName;

    @Comment("투표_항목 삭제 여부")
    private String voteItemIsRemoved;

    /**
     * 생성 메서드
     * @param vote Vote
     * @param voteItemCreateDto voteItemCreateDto
     * @return VoteItem
     */
    public static VoteItem create(Vote vote, VoteItemCreateDto voteItemCreateDto) {
        return VoteItem.builder()
                .vote(vote)
                .voteItemName(voteItemCreateDto.getVoteItemName())
                .voteItemIsRemoved("N")
                .build();
    }

    /**
     * 업데이트 메서드
     * @param voteItemDto voteItemDto
     */
    public void update(VoteItemDto voteItemDto) {
        this.id = voteItemDto.getVoteItemIdx();
        this.voteItemName = voteItemDto.getVoteItemName();
    }
}
