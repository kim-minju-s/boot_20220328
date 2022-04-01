package com.example.mapper;

import java.util.List;

import com.example.dto.MemberaddrDTO;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberaddrMapper {

    // 주소 삭제
    // DELETE FROM 테이블명 WHERE 조건
    @Delete({
        "DELETE FROM MEMBERADDR WHERE UCODE=#{code} AND UEMAIL=#{email}"
    })
    public int deleteMemberAddrOne(@Param("code") long ucode,  @Param("email") String uemail);

    // 1개의 주소를 수정
    // UPDATE 테이블명 SET 변경컬럼명 = 변경값 WHERE 조건
    @Update({
        "UPDATE MEMBERADDR SET UADDRESSS=#{obj.uaddresss}, UPOSTCODE=#{obj.upostcode} ",
        " WHERE UCODE=#{obj.ucode} AND UEMAIL=#{obj.uemail}"
    })
    public int updateMemberAddrOne(@Param("obj") MemberaddrDTO maddress);

    // (수정)1개주소 정보정보 가져오기
    // SELECT * FROM MEMBERADDR WHERE 조건
    @Select({
        "SELECT UCODE, UADDRESSS, UPOSTCODE FROM MEMBERADDR",
        " WHERE UCODE=#{code} AND UEMAIL=#{email}"
    })
    public MemberaddrDTO selectMemberAddrOne(@Param("code") long ucode, @Param("email") String uemail);

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
