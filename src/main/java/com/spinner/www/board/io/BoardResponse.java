package com.spinner.www.board.io;

import com.spinner.www.common.io.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class BoardResponse extends BaseResponse {
    private Long idx;
    private String nickName;
    private String title;
    private String content;
}
