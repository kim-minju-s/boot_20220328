package com.example.mapper;

import java.util.List;

import com.example.dto.ItemDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemMapper {

    // 물품 1개 상세조회
    // SELECT I.* FROM ITEM WHERE ICODE=#{code}
    @Select({
        "SELECT I.ICODE, I.INAME, I.ICONTENT, I.IPRICE, I.IQUANTITY FROM ITEM I WHERE ICODE=#{code}"
    })
    public ItemDTO selectItemOne(@Param(value="code") long code);
    
    // 등록일 기준을 내림차순 1
    // 물품명 기준 오름차순 2
    // 가격 기준 오름차순 3
    // SELECT I.*, ROW_NUMBER() OVER (ORDER BY INAME ASC) ROWN FROM ITEM I
    @Select({
        "<script>",
            "SELECT * FROM (",
            "SELECT I.ICODE, I.INAME , ",
                " I.IPRICE, ROW_NUMBER() OVER (",
                "<choose>",
                    " <when test='type == 1'> ORDER BY IREGDATE DESC </when>",
                    " <when test='type == 2'> ORDER BY INAME ASC </when>",
                    " <when test='type == 3'> ORDER BY IPRICE ASC </when>",
                "</choose>",
                " )ROWN",
            " FROM ITEM I )",
            " WHERE ROWN BETWEEN 1 AND 8 ORDER BY ROWN ASC",
        "</script>"
    })
    public List<ItemDTO> selectItemList(@Param(value = "type") int type);
}
