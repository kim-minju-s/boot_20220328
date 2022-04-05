package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 127.0.0.1:9090/ROOT/home 서버주소 : 포트번호/컨텍스트 PATH/home
    @GetMapping(value={"/","/home"})
    public String homeGET(HttpSession httpSession) {
        System.out.println(httpSession.getAttribute("BACKURL"));
        return "home";
    }
    
}
