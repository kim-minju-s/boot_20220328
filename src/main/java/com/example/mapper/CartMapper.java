package com.example.mapper;

import com.example.dto.CartDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {
    
    @Insert({
        "INSERT INTO CART(CNO, CCNT, CREGDATE, ICODE, UEMAIL)", 
        " VALUES",
        " (SEQ_CART_NO.NEXTVAL, #{obj.ccnt}, CURRENT_DATE, #{obj.icode}, #{obj.uemail})"
    })
    public int addCartOne(@Param("obj") CartDTO cart);
}
