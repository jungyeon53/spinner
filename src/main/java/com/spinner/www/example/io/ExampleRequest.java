package com.spinner.www.example.io;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ExampleRequest {

    private Long Id;

    private String text;

}
