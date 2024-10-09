package com.spinner.www.example.service;

import com.spinner.www.common.CommonResponse;
import com.spinner.www.example.io.ExampleRequest;
import org.springframework.http.ResponseEntity;

public interface ExampleService {

    /**
     * 데이터 삽입 예제
     * @param exampleRequest ExampleRequest
     * @return ResponseEntity<CommonResponse>
     */
    ResponseEntity<CommonResponse> insertExample(ExampleRequest exampleRequest);

    /**
     * 데이터 조회 예제
     * @param id Long;
     * @return ResponseEntity<CommonResponse>
     */
    ResponseEntity<CommonResponse> selectExample(Long id);
}
