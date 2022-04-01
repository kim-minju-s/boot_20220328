package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.dto.ItemDTO;
import com.example.dto.ItemImageDTO;
import com.example.mapper.CartMapper;
import com.example.mapper.ItemImageMapper;
import com.example.mapper.ItemMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/shop")
public class ShopController {
    
    @Autowired ItemMapper iMapper;
    @Autowired ItemImageMapper iiMapper;
    @Autowired ResourceLoader resLoader;
    @Autowired HttpSession httpSession;
    @Autowired CartMapper cMapper;

    // 장바구니에 물품등록
    @PostMapping(value="/cart")
    public String cartPOST(
            @RequestParam(name="code") long code,
            @RequestParam(name="cnt") long cnt){

        String em = (String)httpSession.getAttribute("M_EMAIL");
        String em1 = (String)httpSession.getId();
        if(em != null){
            // System.out.println(code);
            // System.out.println(cnt);
            // System.out.println(em);
            cMapper.addCartOne(code, em, cnt);

        }
        else {
            // 로그인 되지 않았을 때는 고유한 값을 ex) 세션의키
            // System.out.println("고유값-->"+httpSession.getId());
            cMapper.addCartOne(code, em1, cnt);
        }

        return "redirect:/shop/detail?code="+code;
    }

    // 서브이미지 출력
    @GetMapping(value="/subimage")
    public ResponseEntity<byte[]> subimageGET(
                @RequestParam(name = "imgcode") long imgcode)
                    throws IOException{

        ItemImageDTO itemimage = iiMapper.selectItemImageCodeOne(imgcode);

        // System.out.println(itemimage.getIimagetype());
        if (itemimage != null) {
            if(itemimage.getIimagesize() > 0){
                HttpHeaders headers = new HttpHeaders();

                if(itemimage.getIimagetype().equals("image/jpeg")){
                    headers.setContentType(MediaType.IMAGE_JPEG);
                }
                else if(itemimage.getIimagetype().equals("image/png")){
                    headers.setContentType(MediaType.IMAGE_PNG);
                }
                else if(itemimage.getIimagetype().equals("image/gif")){
                    headers.setContentType(MediaType.IMAGE_GIF);
                }

                ResponseEntity<byte[]> response
                    = new ResponseEntity<>(itemimage.getIimage(), headers, HttpStatus.OK);
                return response;
            }
            else {  // 이미지가 없을 경우 default 이미지로 대체
                InputStream is 
					= resLoader
					.getResource("classpath:/static/img/def1.png")
					.getInputStream();
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_PNG);
				
				ResponseEntity<byte[]> response
					= new ResponseEntity<>(is.readAllBytes(),
						headers, HttpStatus.OK );
				
				return response;
            }
        }
        return null;
    }

    // 물품 상세조회
    @GetMapping(value="/detail")
    public String detailGET( Model model,
            @RequestParam(name="code") long code){

        // System.out.println("물품상세 코드"+ code);
        model.addAttribute("idetail", iMapper.selectItemOne(code));
        
        List<Long> list = iiMapper.selectItemImageCodeList(code);
        model.addAttribute("list", list);

        return "/shop/detail";
    }

    // http://127.0.0.1:9090/ROOT/shop/home
    @GetMapping(value = {"/", "/home"})
    public String shopGET(Model model){
        // 등록일
        List<ItemDTO> list1 = iMapper.selectItemList(1);
        model.addAttribute("list1", list1);

        // 물품명
        List<ItemDTO> list2 = iMapper.selectItemList(2);
        model.addAttribute("list2", list2);

        // 가격
        List<ItemDTO> list3 = iMapper.selectItemList(3);
        model.addAttribute("list3", list3);

        return "/shop/home";
    }

}
