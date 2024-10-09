package com.spinner.www.example.controller;

import com.spinner.www.common.CommonResponse;
import com.spinner.www.example.entity.Example;
import com.spinner.www.example.io.ExampleRequest;
import com.spinner.www.example.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/example")
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping("/insert")
    public ResponseEntity<CommonResponse> insertExample(@RequestBody ExampleRequest exampleRequest) {
        return exampleService.insertExample(exampleRequest);
    }

    @GetMapping("/select/{id}")
    public ResponseEntity<CommonResponse> selectExample(@PathVariable Long id) {
        return exampleService.selectExample(id);
    }
}
