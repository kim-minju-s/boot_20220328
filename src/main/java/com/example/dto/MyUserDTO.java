package com.example.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MyUserDTO extends User {

    private static final long serialVersionUID = 1L;
    private String username = null;
    private String password = null;
    private String userphone = null;
    private String name = null;

    public MyUserDTO(
            String username, 
            String password, 
            Collection<? extends GrantedAuthority> authorities,
            String userphone,
            String name) {
        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.userphone = userphone;
        this.name = name;
    }
    
}
