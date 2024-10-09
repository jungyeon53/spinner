package com.spinner.www.example.controller;

import com.spinner.www.common.CommonResponse;
import com.spinner.www.example.entity.Example;
import com.spinner.www.example.io.ExampleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/example")
public class ExampleController {

    @PostMapping("/insert")
    public ResponseEntity<CommonResponse> insertExample(@RequestBody ExampleRequest exampleRequest) {
        return null;
    }

    public ResponseEntity<CommonResponse> selectExample(@RequestBody ExampleRequest exampleRequest) {
        return null;
    }
}
