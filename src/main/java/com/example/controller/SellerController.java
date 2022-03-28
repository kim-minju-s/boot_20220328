package com.example.controller;

import java.util.List;

import com.example.dto.MemberDTO;
import com.example.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/seller")
public class SellerController {
    @Autowired
    MemberService mService;

    // 9090/ROOT/seller/insert
    @GetMapping(value = "/insert")
    public String insertGET(){
        // template 폴더/seller폴더/insert.html 표시 렌터링
        return "/seller/insert";
    }

    @PostMapping(value = "/insert")
    public String insertPOST(@ModelAttribute MemberDTO member ){
        System.out.println(member.toString());
        mService.insertMember(member);
        return "redirect:/home";    // 주소를 바꾼다음 엔터키
    }

    @GetMapping(value="/selectlist")
    public String selectlistGET(Model model){
        List<MemberDTO> list = mService.selectMemberList();
        model.addAttribute("list", list);
        return "/seller/selectlist";
    }

    // 127.0.0.1:9090/ROOT/seller/delete?email=b
    // RequestMapping -> get과 post 방식 둘 다 가능하게
    @RequestMapping(value="/delete", method={RequestMethod.GET, RequestMethod.POST})
    public String deleteGETPOST(@RequestParam(name="email") String em){
        int ret = mService.deleteMemberOne(em);
        if(ret == 1){
            //성공시
        }
        else {
            // 실패시
        }
        return "redirect:/seller/selectlist";
    }

    @GetMapping(value="/update")
    public String updateGET(
                Model model,
                @RequestParam(name="email") String em){
        MemberDTO member = mService.selectMemberOne(em);
        model.addAttribute("obj", member);
        return "/seller/update";
    }

    @PostMapping(value="/update")
    public String updatePOST(@ModelAttribute MemberDTO member){
        System.out.println(member.toString());
        int ret = mService.updateMemberOne(member);
        if(ret == 1){
            return "redirect:/seller/selectlist";
        }
        return "redirect:/seller/update?email="+ member.getUemail();
    }

}
