package com.example.service;

import java.util.List;

import com.example.dto.MemberDTO;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    
    // 판매자 등록
    public int insertMember(MemberDTO member);

    // 판매자 목록(parameter은 없고 return 만)
    public List<MemberDTO> selectMemberList();

    // 판매자 삭제(이메일 전송 후 int 값 리턴)
    public int deleteMemberOne(String uemail);

    // 판매자 1명 조회
    public MemberDTO selectMemberOne(String uemail);

    // 판매자 수정
    public int updateMemberOne(MemberDTO member);
}
