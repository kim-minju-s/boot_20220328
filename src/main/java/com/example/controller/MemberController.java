package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.dto.MemberDTO;
import com.example.dto.MemberaddrDTO;
import com.example.mapper.MemberMapper;
import com.example.mapper.MemberaddrMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired MemberMapper mMapper;
    @Autowired MemberaddrMapper MaddrMapper;

    @Autowired HttpSession httpSession;

    @PostMapping(value = "/deleteaddr")
    public String deleteaddrPOST(@RequestParam(name = "code") long code){
        String em = (String)httpSession.getAttribute("M_EMAIL");

        MaddrMapper.deleteMemberAddrOne(code, em);

        return "redirect:/member/address";
    }

    // 주소 변경하기
    @PostMapping(value = "/updateaddraction")
    public String updateaddrPOST(@ModelAttribute MemberaddrDTO maddress){

        System.out.println("주소"+ maddress.toString());
        String em = (String)httpSession.getAttribute("M_EMAIL");
        if (em != null) {
            maddress.setUemail(em);
            
            MaddrMapper.updateMemberAddrOne(maddress);

            return "redirect:/member/address";
        }
        return "redirect:/member/login";
    }

    // 주소 변경 화면
    @GetMapping(value = "/updateaddr")
    public String updateaddrGET( Model model,
                @RequestParam(name="code") long code){

        String em = (String)httpSession.getAttribute("M_EMAIL");
        if (em != null) {
            model.addAttribute("addr", MaddrMapper.selectMemberAddrOne(code, em));

            return "/member/updateaddr";
        }

        return "redirect:/member/login";
    }

    // 대표주소 설정
    @PostMapping(value="/setaddr")
    public String addrPOST(@RequestParam(name="code") long code){

        // System.out.println(code);
        String em = (String)httpSession.getAttribute("M_EMAIL");
        String ro = (String)httpSession.getAttribute("M_ROLE");
        if(em != null){ //로그인 되었는지
            if(ro.equals("CUSTOMER")){  // 권한이 정확한지
                MaddrMapper.updateMemberAddrSet(code, em);
                
                return "redirect:/member/address";
            }
        }
        return "redirect:/member/login";
    }

    // 주소 추가
    @PostMapping(value = "/addraction")
    public String addractionPOST(@ModelAttribute MemberaddrDTO Maddress){
        // System.out.println(Maddress.toString());

        String em = (String)httpSession.getAttribute("M_EMAIL");
        String ro = (String)httpSession.getAttribute("M_ROLE");
        if(em != null){ //로그인 되었는지
            if(ro.equals("CUSTOMER")){  // 권한이 정확한지
                Maddress.setUemail(em);
                System.out.println(Maddress.toString());

                MaddrMapper.memberAddr(Maddress);

                return "redirect:/member/address";
            }
        }
        return "redirect:/member/login";
    }

    // 주소관리
    @GetMapping(value = "/address")
    public String memberaddrGET(Model model){
        String em = (String)httpSession.getAttribute("M_EMAIL");
        String ro = (String)httpSession.getAttribute("M_ROLE");
        if(em != null){ //로그인 되었는지
            if(ro.equals("CUSTOMER")){  // 권한이 정확한지
                // 화면표시

                // view에 주소목록 전송해야함
                List<MemberaddrDTO> list = MaddrMapper.selectMemberAddrList(em);
                // System.out.println(list);
                model.addAttribute("addr", list);

                return "/member/address";
            }
            return "redirect:/403page";
        }
        return "redirect:/member/login";
    }

    // 로그인
    @PostMapping(value = "/loginaction")
    public String loginactionPOST(
                @RequestParam(name = "uemail") String email,
                @RequestParam(name = "upw") String pw ){

        System.out.println(email);
        System.out.println(pw);

        MemberDTO member = mMapper.memberLogin(email, pw);
        System.out.println(member.toString());

        if(member != null){
            httpSession.setAttribute("M_EMAIL", member.getUemail());
            httpSession.setAttribute("M_NAME", member.getUname());
            httpSession.setAttribute("M_ROLE", member.getUrole());
            // return "redirect:/";
        }
        return "redirect:/member/login";
    }

    @PostMapping(value="/logout")
    public String logoutPOST(){
        httpSession.invalidate();
        return "redirect:/";
    }

    @PostMapping(value = "/joinaction")
    public String joinactionPOST(
            @ModelAttribute MemberDTO member){

        // 필요한 항목을 수동으로 채움.
        member.setUrole("CUSTOMER");

        // 객체에 값이 잘 들어갔는지 확인
        System.out.println(member.toString());

        int ret = mMapper.memberJoin(member);
        if (ret == 1) {
            // redirect는 주소를 변경한 후에 엔터키를 치는것
            return "redirect:/";
        }
        return "redirect:/member/join";

    }
    
    @GetMapping(value = "/join")
    public String joinGET(){
        // template안 member폴더의 join을 표시(render)
        // render은 GET에서만 사용해야함.
        return "/member/join";
    }

    @GetMapping(value = "/login")
    public String loginGET(){

        return "/member/login";
    }

}
