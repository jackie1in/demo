package com.example.rabbit.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Msg {
    @Singular
    private Set<Channel> channels;
    @Singular
    private List<String> tos;
    private String title;
    private String content;
    private String data;
    private OogeMsg oogeMsg;

    public static enum Channel{
        EMAIL,
        APP,
    }
}
