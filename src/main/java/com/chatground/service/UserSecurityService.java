package com.chatground.service;

import com.chatground.entity.Member;
import com.chatground.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

public class UserSecurityService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        Member member = memberRepository.findByAccount(name);
        if(member == null){
            Member emailMember = memberRepository.findByEmail(name);
            if(emailMember == null){
                throw new UsernameNotFoundException("帳號或電子郵件不存在");
            }else{
                member =  emailMember;
            }
        }else if("locked".equals(member.getStatus())){  //被鎖定，無法登入
            throw new LockedException("帳號被鎖定");
        }
        return member;
    }
}
