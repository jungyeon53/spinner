package com.spinner.www.vote.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteDto {

    private Long voteId;
    private String voteName;
}
