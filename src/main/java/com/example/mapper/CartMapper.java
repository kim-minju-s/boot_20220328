package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {
    
    @Insert({
        "INSERT INTO CART(CNO, CCNT, CREGDATE, ICODE, UEMAIL)", 
        " VALUES",
        " (SEQ_CART_NO.NEXTVAL, #{cnt}, CURRENT_DATE, #{code}, #{email})"
    })
    public int addCartOne(@Param("code") long code,
            @Param("email") String email, @Param("cnt") long cnt);
}
