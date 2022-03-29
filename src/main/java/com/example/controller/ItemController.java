package com.example.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.example.dto.ItemDTO;
import com.example.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/item")
public class ItemController {
    
    @Autowired
    ItemService iService;

    @GetMapping(value="/insert")
    public String insertGET(){
        return "/item/insert";
    }

    @PostMapping(value="/insert")
    public String insertPOST(
                HttpSession httpSession,
                @RequestParam(name = "timage") MultipartFile file,
                @ModelAttribute ItemDTO item) throws IOException{

            System.out.println(file.getSize());
            System.out.println(file.getContentType());
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getBytes());

            // item.setIimage(file.getBytes());;
            // item.setIimagesize(file.getSize());
            // item.setIimagetype(file.getContentType());
            // item.setIimagename(file.getOriginalFilename());
            // System.out.println(item.toString());

            String em = (String)httpSession.getAttribute("SESSION_EMAIL");
            item.setUemail(em);

            int ret = iService.insertItemOne(item);
            if(ret == 1){
                return "redirect:/item/selectlist";
            }

            return "redirect:/item/insert";
    }

    // 127.0.0.1:9090/ROOT/item/selectlist?txt=검색어&page=1
    @GetMapping(value="/selectlist")
    public String selectlistGET(Model model,
            @RequestParam(name="txt", defaultValue="") String txt,
            @RequestParam(name="page", defaultValue="1") int page){

        Map<String, Object> map = new HashMap<>();
        map.put("txt", txt);
        map.put("start", ((page-1)*10)+1);
        map.put("end", page*10);

        // page 1, start 1 end 10
        // page 2, start 11 end 20
        // page 3, start 21 end 30

        List<ItemDTO> list = iService.selectItemList(map);
        model.addAttribute("list", list);

        long cnt = iService.selectItemCount(txt);
        // 9 => 1
        // 11 => 2
        // 24 => 3
        model.addAttribute("pages", (cnt-1)/10+1);

        return "/item/selectlist";
    }

}
