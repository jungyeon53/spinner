package com.spinner.www.example.service;

import com.spinner.www.common.CommonResponse;
import com.spinner.www.constants.CommonResultCode;
import com.spinner.www.example.entity.Example;
import com.spinner.www.example.io.ExampleRequest;
import com.spinner.www.example.repository.ExampleRepository;
import com.spinner.www.util.ResponseVOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.spinner.www.example.entity.Example.exampleRequestBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    /**
     * 데이터 삽입 예시
     * @param exampleRequest ExampleRequest
     * @return ResponseEntity<CommonResponse>
     */
    @Override
    public ResponseEntity<CommonResponse> insertExample(ExampleRequest exampleRequest) {
        exampleRepository.save(exampleRequestBuilder(exampleRequest));
        return new ResponseEntity<>(ResponseVOUtils.getSuccessResponse(), HttpStatus.CREATED);
    }

    /**
     * 데이터 조회 예시
     * @param id Long
     * @return ResponseEntity<CommonResponse>
     */
    @Override
    public ResponseEntity<CommonResponse> selectExample(Long id) {

        Optional<Example> example = exampleRepository.findById(id);

        if (example.isEmpty()) {
            // 공통 에러 코드에 정의된 실패
            return new ResponseEntity<>(ResponseVOUtils.getFailResponse(CommonResultCode.DATA_NOT_FOUND), HttpStatus.NOT_FOUND);
        } else if (example.get().getId() == -1) {
            // 공통 에러 코드 커스텀 실패 메시지
            return new ResponseEntity<>(ResponseVOUtils.getFailResponse(50001, "알 수 없는 오류입니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 성공 메시지와 함께 결과값 조회
        return new ResponseEntity<>(ResponseVOUtils.getSuccessResponse(example), HttpStatus.OK);
    }

}
