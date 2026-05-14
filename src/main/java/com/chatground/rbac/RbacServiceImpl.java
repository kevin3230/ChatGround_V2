package com.chatground.rbac;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.chatground.entity.Member;
import com.chatground.entity.SysPermission;
import com.chatground.entity.SysRole;
import com.chatground.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component("rbacService")
public class RbacServiceImpl implements RbacService{
	
	private static final Logger log = LoggerFactory.getLogger(RbacServiceImpl.class);
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication){
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
        log.debug("request.uri : " + request.getRequestURI());
        if(principal != null && principal instanceof UserDetails){
            String userName = ((UserDetails)principal).getUsername();
            Set<String> urls = new HashSet<>();
            Member member = memberRepository.findByAccount(userName);
            try{
                for(SysRole role : member.getSysRoleList()){
                    for(SysPermission permission : role.getSysPermissionList()){
                    	log.debug(permission.getUrl());
                        urls.add(permission.getUrl());
                    }
                }
            } catch(Exception e){
                log.info(e.toString());
            }
            for(String url : urls){
                if(antPathMatcher.match(url, request.getRequestURI())){
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }

}
