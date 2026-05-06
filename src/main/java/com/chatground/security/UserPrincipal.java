package com.chatground.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chatground.entity.Member;
import com.chatground.entity.SysRole;

public class UserPrincipal implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2662660952875464846L;
	
	private final Member user;
	private String nickName = "";
	
	public UserPrincipal(Member user) {
        this.user = user;
        this.nickName = user.getNickName();
    }
	
	public String getId() {
		return String.valueOf(user.getId());
	}
	
	public String getNickName(){
		return user.getNickName();
	}
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(user != null) {
        	List<SysRole> sysRoleList = user.getSysRoleList();
        	if(sysRoleList != null && !sysRoleList.isEmpty()) {
        		for(SysRole role: sysRoleList){
        			authorities.add(new SimpleGrantedAuthority(role.getRole().toString()));
        		}
        	}
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    @Override
    public String getUsername(){
        return user.getAccount();
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }
}
