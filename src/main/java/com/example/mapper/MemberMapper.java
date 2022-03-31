package com.example.mapper;

import com.example.dto.MemberDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    
    // INSERT INTO 테이블명(칼럼들..) VALUES (추가할 값들..)
    @Insert({" INSERT INTO MEMBER",
            " (UEMAIL, UPW, UNAME, UPHONE, UROLE, UREGDATE)",
            " VALUES(#{obj.uemail}, #{obj.upw}, #{obj.uname}, #{obj.uphone}, #{obj.urole}, CURRENT_DATE)"})
    public int memberJoin( @Param(value="obj") MemberDTO member);

    // SELECT 컬럼명들 FROM 테이블명 WHERE 조건 AND 조건
    @Select({"SELECT UEMAIL, UNAME, UROLE FROM MEMBER",
            " WHERE UEMAIL=#{email} AND UPW=#{pw}"})
    public MemberDTO memberLogin(
            // Param 의 value가 변수, String email은 받는값 
            @Param(value = "email") String email,
            @Param(value = "pw") String pw);
  
}
