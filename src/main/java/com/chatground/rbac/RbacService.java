package com.chatground.rbac;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;


public interface RbacService {
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
