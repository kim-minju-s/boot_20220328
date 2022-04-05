package com.example.service;

import java.util.Collection;

import com.example.dto.MemberDTO;
import com.example.dto.MyUserDTO;
import com.example.mapper.MemberMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailServiceImpl implements UserDetailsService{

    @Autowired MemberMapper mMapper;

    // 로그인에서 입력하는 정보 중에서 아이디를 받음
    // MemberMapper를 이용해서 정보를 가져와서 UserDetails로 리턴
    // 아이디, 암호, 권한
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("MemberDetailService: " + username);
        MemberDTO member = mMapper.memberEmail(username);

        // 권한 정보를 문자열 배열로 만듦.
        String[] strRole = { member.getUrole() };

        // String 배열 권한을 Collection<> 변환함.
        Collection<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(strRole);

        // 자동으로 세션에 데이터가 들어감
        // 목표: 아이디와 비밀번호, 권한을 리턴시킴
        // User user = new User(member.getUemail(), member.getUpw(), roles);
        MyUserDTO user = new MyUserDTO(
            member.getUemail(),
            member.getUpw(),
            roles,
            member.getUphone(),
            member.getUname());
        return user;
    }

}
