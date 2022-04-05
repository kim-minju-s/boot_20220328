package com.example.controller;

import com.example.dto.MemberDTO;
import com.example.dto.MyUserDTO;
import com.example.mapper.MemberMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {
    
    @Autowired MemberMapper mMapper;

    @GetMapping(value={"/security_403"})
    public String security403GET() {
        return "/security/403page";
    }

    // 홈화면(로그인 전)
    @GetMapping(value={"/security_home"})
    public String securityHomeGET(Model model,
            @AuthenticationPrincipal MyUserDTO user) {

        if (user != null) {
            System.out.println(user.getUsername());
            System.out.println(user.getName());
            System.out.println(user.getUserphone());
            System.out.println(user.getAuthorities().toArray()[0]);
        }
        model.addAttribute("user", user);
        // model.addAttribute("userid", user.getUsername());
        // model.addAttribute("userrole", user.getAuthorities().toArray()[0]);
        return "/security/home";
    }
    // 관리자 홈화면(로그인 후에)
    @GetMapping(value={"/security_admin/home"})
    public String securityAdminHomeGET() {
        return "/security/admin_home";
    }
    // 판매자 홈화면(로그인 후에)
    @GetMapping(value={"/security_seller/home"})
    public String securitySellerHomeGET() {
        return "/security/seller_home";
    }
    // 고객 홈화면(로그인 후에)
    @GetMapping(value={"/security_customer/home"})
    public String securityCustomerHomeGET() {
        return "/security/customer_home";
    }

    @GetMapping(value = "/member/security_login")
    public String securityLoginGET(){
        return "/security/login";
    }

    @PostMapping(value = "/member/security_join")
    public String securityJoinPOST(@ModelAttribute MemberDTO member){

        // 1. 회원가입 할 때 암호를 가져와서 해시한 후 다시 추가하기
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        member.setUpw( bcpe.encode( member.getUpw() ) );
        member.setUrole("CUSTOMER");

        int ret = mMapper.memberJoin(member);
        if (ret == 1) { // 성공 시
            return "redirect:/security_home";
        }
        // 실패 시
        return "redirect:/member/security_join";
    }

    // 127.0.0.1:9090/ROOT/member/security_join
    @GetMapping(value = "/member/security_join")
    public String securityJoinGET(){
        return "/security/join";
    }
}
