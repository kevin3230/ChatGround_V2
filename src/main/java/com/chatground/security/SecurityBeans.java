package com.chatground.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.chatground.utility.JwtUtil;

@Configuration
public class SecurityBeans {
	
	//使用BCrypt 加密
	@Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	//產生預設配置的JwtUtil Bean
	@Bean
	public JwtUtil jwtUtil(
			@Value("${jwt.secret-key}")String secretKey,
			@Value("${jwt.expiration-second}")int validSeconds) {
		return new JwtUtil(secretKey, validSeconds);
	}
	
}
