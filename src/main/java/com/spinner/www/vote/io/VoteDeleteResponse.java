package com.spinner.www.vote.io;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class VoteDeleteResponse {
    private Long voteIdx;
    private List<Long> voteItemIdxList;
}
