package com.spinner.www.example.entity;

import com.spinner.www.example.io.ExampleRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = true)
    private String text;

    /**
     * ExampleRequest -> Example 변환
     * @param exampleRequest Example
     * @return Example
     */
    public static Example exampleRequestBuilder(ExampleRequest exampleRequest) {
        return Example.builder()
                .Id(exampleRequest.getId())
                .text(exampleRequest.getText())
                .build();
    }
}
