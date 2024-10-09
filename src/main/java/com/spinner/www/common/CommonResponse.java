package com.spinner.www.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 공통 응답 정리
 */
@Getter
@Builder
public class CommonResponse {

    private int code;
    private String message;

    @Nullable
    private Object results;
}
