package com.chatground.configuration;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.chatground.security.UserPrincipal;

@Component("auditorAware")
public class MyAuditorAware implements AuditorAware<Long>{
	private static final Logger log = LoggerFactory.getLogger(MyAuditorAware.class);
	
	@Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        log.debug("Authentication is AnonymousAuthenticationToken: " + String.valueOf(authentication instanceof UserPrincipal));
        //未登入使用者與匿名使用者沒有Member_id
        if(authentication == null || !authentication.isAuthenticated() 
        		|| authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return Optional.of(((UserPrincipal)authentication.getPrincipal()).getId());
    }
}
