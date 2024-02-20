package com.example.oauth.controller;

import com.example.oauth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final KakaoService kakaoService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login(Model model){
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());

        return "login";
    }
}
