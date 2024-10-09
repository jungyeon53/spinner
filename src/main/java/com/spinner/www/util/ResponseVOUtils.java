package com.spinner.www.util;

import com.spinner.www.common.CommonResponse;
import com.spinner.www.constants.CommonResultCode;
import lombok.Builder;
import lombok.experimental.UtilityClass;

/**
 * 응답 Response 생성
 */
@UtilityClass
@Builder
public class ResponseVOUtils {

    /**
     * 응답 성공 시 반환 메서드
     * 반환 결과가 존재할 때
     * @param results Object
     * @return CommonResponse
     */
    public static CommonResponse getSuccessResponse(Object results) {
        CommonResultCode resultCode = CommonResultCode.SUCCESS;
        return CommonResponse.builder()
                .code(resultCode.code())
                .message(resultCode.message())
                .results(results)
                .build();
    }

    /**
     * 응답 성공 시 반환 메서드
     * 반환 결과가 존재하지 않을 때
     * @return CommonResponse
     */
    public static CommonResponse getSuccessResponse() {
        CommonResultCode resultCode = CommonResultCode.SUCCESS;
        return CommonResponse.builder()
                .code(resultCode.code())
                .message(resultCode.message())
                .build();
    }

    /**
     * 응답 실패 시 메서드 코드 존재
     * @param resultCode CommonResultCode
     * @return CommonResponse
     */
    public static CommonResponse getFailResponse(CommonResultCode resultCode) {
        return CommonResponse.builder()
                .code(resultCode.code())
                .message(resultCode.message())
                .build();
    }

    /**
     * 응답 실패 시 커스텀 메시지 반환
     * @param code String
     * @param message String
     * @return CommonResponse
     */
    public static CommonResponse getFailResponse(int code, String message) {
        return  CommonResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
