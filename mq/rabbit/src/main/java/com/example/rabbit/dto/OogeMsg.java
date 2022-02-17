package com.example.rabbit.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OogeMsg {
    private String operateHint;
    private String deepLink;
    @Singular
    private List<OogeOperation> oogeOperations;

}
