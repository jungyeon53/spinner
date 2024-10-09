package com.spinner.www.example.io;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ExampleRequest {

    private int Id;

    @NotBlank(message = "샘플 텍스트는 필수로 입력되어야 합니다.")
    private String text;

}
