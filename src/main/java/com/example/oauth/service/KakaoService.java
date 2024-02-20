package com.example.oauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoService {
    @Value("${kakao.client.id}")
    private String KAKAO_ID;
    @Value("${kakao.client.secret}")
    private String KAKAO_SECRET;
    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URI;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin(){
        return KAKAO_AUTH_URI + "/oauth/authorize"
                +"?client_id="+KAKAO_ID
                +"&redirect_uri="+KAKAO_REDIRECT_URI
                +"&response_type=code";
    }

}
