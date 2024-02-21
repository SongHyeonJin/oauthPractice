package com.example.oauth.controller;

import com.example.oauth.dto.KakaoDto;
import com.example.oauth.entity.MsgEntity;
import com.example.oauth.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;

    @GetMapping("/user/oauth")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception{
        KakaoDto kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", kakaoInfo));
    }
}
