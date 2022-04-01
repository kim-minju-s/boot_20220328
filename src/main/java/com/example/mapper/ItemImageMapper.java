package com.example.mapper;

import java.util.List;

import com.example.dto.ItemImageDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemImageMapper {
    
    // 물품코드가 오면 관련되어 있는 서브이미지 번호들 반환
    @Select({
        "SELECT IMGCODE FROM ITEMIMAGE WHERE ICODE=#{code}"
    })
    public List<Long> selectItemImageCodeList(@Param("code") long code);

    // 서브이미지코드에 해당하는 1개의 이미지의 정보를 반환
    @Select({
        "SELECT * FROM ITEMIMAGE WHERE IMGCODE=#{imgcode}"
    })
    public ItemImageDTO selectItemImageCodeOne(@Param("imgcode") long imgcode);
}
