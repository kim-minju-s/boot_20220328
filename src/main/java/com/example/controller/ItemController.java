package com.example.controller;

import com.example.dto.ItemDTO;
import com.example.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/item")
public class ItemController {
    
    @Autowired
    MemberService mService;

    @GetMapping(value="/insert")
    public String insertGET(){
        return "/item/insert";
    }

    @PostMapping(value="/insert")
    public String insertPOST(@ModelAttribute ItemDTO item){
        System.out.println(item.toString());
        return "redirect:/home";
    }

}
