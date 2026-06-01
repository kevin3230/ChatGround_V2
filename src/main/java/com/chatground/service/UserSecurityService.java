package com.chatground.service;

import com.chatground.entity.Member;
import com.chatground.repository.MemberRepository;
import com.chatground.security.UserPrincipal;
import com.chatground.utility.LanguageUtils;
import com.chatground.utility.MemberStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
	private MessageSource messageSource;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        Member member = memberRepository.findByAccount(name);
        if(member == null){
            Member emailMember = memberRepository.findByEmail(name);
            if(emailMember == null){
                throw new UsernameNotFoundException(messageSource.getMessage("account.notfound", null, LanguageUtils.getLocale()));
            }else{
                member =  emailMember;
            }
        }else if(MemberStatus.LOCKED.equals(member.getStatus())){  //被鎖定，無法登入
            throw new LockedException(messageSource.getMessage("account.locked", null, LanguageUtils.getLocale()));
        }
        UserPrincipal user = new UserPrincipal(member);
        
        return user;
    }
}
