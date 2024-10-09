package com.spinner.www.constants;

public enum CommonResultCode {

    /**
     * 성공 처리
     */
    SUCCESS(20000, "요청 성공"),

    /**
     * 서버 에러
     */
    ERROR(50000, "서버 에러"),

    /**
     * DB 조회 후 데이터 미존재
     */
    DATA_NOT_FOUND(50001, "데이터를 찾을 수 없음");

    /**
     * 변수 설정
     */
    private final int code;
    private final String message;

    /**
     * 생성자 생성
     * @param code    int
     * @param message String
     */
    CommonResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 오류 코드를 반환한다
     * @return String
     */
    public int code() {
        return code;
    }

    /**
     * 오류 메시지를 반환한다
     * 치환 문자("{}") 및 공백 문자 제거
     * @return String
     */
    public String message() {
        return message.replace("{}", "").trim();
    }
}
