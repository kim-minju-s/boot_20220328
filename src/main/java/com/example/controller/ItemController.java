package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.example.dto.ItemDTO;
import com.example.dto.ItemImageDTO;
import com.example.service.ItemImageService;
import com.example.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    ItemImageService iiService;

    @Autowired
    ResourceLoader resLoader;   // 디폴트 이미지

    @GetMapping(value="/insert")
    public String insertGET(){
        return "/item/insert";
    }

    @PostMapping(value="/insert")
    public String insertPOST(
                HttpSession httpSession,
                @RequestParam(name = "timage") MultipartFile file,
                @ModelAttribute ItemDTO item) throws IOException{

            // System.out.println(file.getSize());
            // System.out.println(file.getContentType());
            // System.out.println(file.getOriginalFilename());
            // System.out.println(file.getBytes());

            item.setIimage(file.getBytes());;
            item.setIimagesize(file.getSize());
            item.setIimagetype(file.getContentType());
            item.setIimagename(file.getOriginalFilename());
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
            HttpSession httpSession,
            @RequestParam(name="txt", defaultValue="") String txt,
            @RequestParam(name="page", defaultValue="1") int page){

        // 세션에서 이메일 정보를 받음
        String em = (String)httpSession.getAttribute("SESSION_EMAIL");
        if (em != null) {
            String role = (String)httpSession.getAttribute("SESSION_ROLE");
            if (role.equals("SELLER")) {
                Map<String, Object> map = new HashMap<>();
                map.put("txt", txt);
                map.put("start", ((page-1)*10)+1);
                map.put("end", page*10);
                map.put("email", em);
        
                // page 1, start 1 end 10
                // page 2, start 11 end 20
                // page 3, start 21 end 30
        
                List<ItemDTO> list = iService.selectItemList(map);
                model.addAttribute("list", list);
        
                long cnt = iService.selectItemCount(map);
                // 9 => 1
                // 11 => 2
                // 24 => 3
                model.addAttribute("pages", (cnt-1)/10+1);
        
                return "/item/selectlist";
            }
        }
        return "redirect:/seller/select";
    }

    //127.0.0.1:9090/ROOT/item/selectone?code=
    @GetMapping(value = "/selectone")
    public String selectoneGET(
            Model model,    
            @RequestParam(name = "code") long code){
        
        ItemDTO item = iService.selectItemOne(code);
        model.addAttribute("item", item);
        System.out.println(item.toString());

        List<Long> imgcode = iiService.selectItemImageList(code);
        model.addAttribute("imgcode", imgcode);

        return "/item/selectone";
    }

    //127.0.0.1:9090/ROOT/item/image?code=3
    // html에서 th:src="@{/item/image(no=15)}"
    @GetMapping(value="/image")
    public ResponseEntity<byte[]> imageGET(
                @RequestParam(name="code") long code) throws IOException{
        
        // 이미지명, 이미지크기, 이미지종류, 이미지데이터
        ItemDTO item = iService.selectItemImageOne(code);
        // System.out.println(item.getIimagetype());
        // System.out.println(item.getIimage().length);
        if(item != null){   // 물품정보가 존재하면
            if(item.getIimagesize() > 0){   // 첨부한 파일 존재
                HttpHeaders headers = new HttpHeaders();

                if(item.getIimagetype().equals("image/jpeg")){
                    headers.setContentType(MediaType.IMAGE_JPEG);
                }
                else if(item.getIimagetype().equals("image/png")){
                    headers.setContentType(MediaType.IMAGE_PNG);
                }
                else if(item.getIimagetype().equals("image/gif")){
                    headers.setContentType(MediaType.IMAGE_GIF);
                }

                // 이미지 byte[], headers, HttpStatus.Ok
                ResponseEntity<byte[]> response
                    = new ResponseEntity<>(item.getIimage(), headers, HttpStatus.OK);
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

    // 서브이미지
    @PostMapping(value = "/insertimages")
    public String insertimagesPOST(
                @RequestParam(name="icode") long icode,
                @RequestParam(name = "timage") MultipartFile[] files)
                    throws IOException{

        // ItemImageDTO를 n개 보관할 수 있는 list
        List<ItemImageDTO> list = new ArrayList<>();
        
        for( MultipartFile file : files){
            ItemImageDTO obj = new ItemImageDTO();
            obj.setIcode(icode);    // 물품코드
            obj.setIimage(file.getBytes()); //이미지
            obj.setIimagetype(file.getContentType());   //타입
            obj.setIimagesize(file.getSize());  //사이즈
            obj.setIimagename(file.getOriginalFilename());  //이름

            list.add(obj);
        }

        iiService.insertItemImageBatch(list);

        return "redirect:/item/selectone?code=" + icode;

    }

    @GetMapping(value="/subimage")
    public ResponseEntity<byte[]> subimageGET(
                @RequestParam(name = "imgcode") long imgcode)
                    throws IOException{

        ItemImageDTO itemimage = iiService.selectItemImageOne(imgcode);

        System.out.println(itemimage.getIimagetype());
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

}
