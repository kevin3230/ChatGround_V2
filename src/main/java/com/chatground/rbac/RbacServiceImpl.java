package com.chatground.rbac;

import com.chatground.entity.Member;
import com.chatground.entity.SysPermission;
import com.chatground.entity.SysRole;
import com.chatground.repository.MemberRepository;
import com.chatground.repository.SysPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component("rbacService")
public class RbacServiceImpl implements RbacService{
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication){
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
//        System.out.println("request.uri : " + request.getRequestURI());
        if(principal != null && principal instanceof UserDetails){
            String userName = ((UserDetails)principal).getUsername();
            Set<String> urls = new HashSet<>();
            Member member = memberRepository.findByAccount(userName);
            try{
                for(SysRole role : member.getSysRoleList()){
                    for(SysPermission permission : role.getSysPermissionList()){
//                        System.out.println(permission.getUrl());
                        urls.add(permission.getUrl());
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
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
