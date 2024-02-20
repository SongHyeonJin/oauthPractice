package com.example.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoDto {
    private long id;
    private String nickname;
}
