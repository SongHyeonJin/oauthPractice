package com.example.oauth.service;

import com.example.oauth.dto.KakaoDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    public KakaoDto getKakaoInfo(String code) throws Exception{
        if(code == null) throw new Exception("Failed get authorization code");

        String accessToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_ID);
            params.add("client_secret", KAKAO_SECRET);
            params.add("code", code);
            params.add("redirect_uri" , KAKAO_REDIRECT_URI);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String) jsonObj.get("access_token");
        } catch (Exception e){
            throw new Exception("API call failed");
        }
        return getUserWithToken(accessToken);
    }

    private KakaoDto getUserWithToken(String accessToken) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id");
        String nickname = String.valueOf(profile.get("nickname"));

        return KakaoDto.builder()
                .id(id)
                .nickname(nickname)
                .build();
    }

}
