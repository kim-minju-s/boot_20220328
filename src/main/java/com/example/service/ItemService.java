package com.example.service;

import java.util.List;
import java.util.Map;

import com.example.dto.ItemDTO;

import org.springframework.stereotype.Service;

@Service
public interface ItemService {

    // 아이템 등록
    public int insertItemOne(ItemDTO item);

    // 물품 조회(검색+페이지네이션)
    public List<ItemDTO> selectItemList( Map<String, Object> map);

    // 물품개수 (페이지네이션)
    public long selectItemCount(Map<String, Object> map);

    // 물품 상세
    public ItemDTO selectItemOne(long code);

    // 이미지 가져오기
    public ItemDTO selectItemImageOne(long code);

}
