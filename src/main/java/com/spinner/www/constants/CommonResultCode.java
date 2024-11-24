package com.spinner.www.constants;

public enum CommonResultCode {

    /**
     * 성공 처리
     */
    SUCCESS(20000, "요청 성공"),

    /**
     * 로그인이 안되어있어 권한이 없는경우
     */
    UNAUTHORIZED(40101, "권한이 없습니다."),

    /**
     * 로그인은 되어 있으나 접근 권한이 없는 경우(ex: 게시글의 작성자와 수정자가 불일치)
     */
    FORBIDDEN(40301, "접근이 금지되었습니다."),

    /**
     * 데이터 중복
     */
    DUPLICATE(40900, "데이터 중복"),

    /**
     * 신고 중복
     */
    DUPLICATE_REPORT(40901, "이미 신고한 게시물입니다."),

    /**
     * 서버 에러
     */
    ERROR(50000, "서버 에러"),

    /**
     * DB 조회 후 데이터 미존재
     */
    DATA_NOT_FOUND(50001, "데이터를 찾을 수 없음"),

    /**
     * 파일 업로드 실패
     */
    FILE_UPLOAD_FAIL(50002, "파일 업로드에 실패했습니다");

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
