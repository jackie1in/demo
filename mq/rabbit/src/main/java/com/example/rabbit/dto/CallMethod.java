package com.example.rabbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallMethod {
    private String url;
    private HttpMethod method;
    private Map<String, Object> param;

    public static enum HttpMethod {
        POST,
    }
}