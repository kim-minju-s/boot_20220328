package com.example.mapper;

import java.util.List;

import com.example.dto.MemberaddrDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberaddrMapper {

    @Update({
        "UPDATE MEMBERADDR SET UCHK = ",
        " (SELECT MAX(UCHK) FROM MEMBERADDR WHERE UEMAIL=#{email})+1",
        " WHERE UCODE = #{code}"
    })
    public int updateMemberAddrSet(
            @Param("code") long code,
            @Param("email") String email);

    @Select({"SELECT * FROM MEMBERADDR WHERE UEMAIL=#{em} ORDER BY UCHK DESC"})
    public List<MemberaddrDTO> selectMemberAddrList(@Param(value="em") String email);
    
    @Insert({"INSERT INTO MEMBERADDR",
    "(UCODE, UADDRESSS, UPOSTCODE, UREGDATE, UEMAIL)",
    " VALUES(SEQ_MEMBERADDR_UCODE.NEXTVAL, #{obj.uaddresss}, #{obj.upostcode}, CURRENT_DATE, #{obj.uemail})"})
    public int memberAddr( @Param(value = "obj") MemberaddrDTO Maddress);  
}
