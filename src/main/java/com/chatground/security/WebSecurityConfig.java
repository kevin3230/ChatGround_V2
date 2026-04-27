package com.chatground.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.chatground.rbac.RbacService;
import com.chatground.service.UserSecurityService;
import com.chatground.utility.GetApplicationProperties;

@Configuration
//指定為Spring Security設定類別
@EnableWebSecurity
//如果要開啟方法安全設定，則開啟此項
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	@Autowired
	private GetApplicationProperties getApplicationProperties;
    
	@Autowired
    private RbacService rbacService;
	
    private PasswordEncoder getPasswordEncoder(){
        //使用BCrypt 加密
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	//自訂WebSecurity的debug開關
        return (web) -> web.debug(getApplicationProperties.getSecurityDebug());
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf((csrf) -> csrf.disable())	//停用CSRF
        	//授權
	    http.authorizeHttpRequests((authorize) -> authorize
        		.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
        		.requestMatchers("/admin/**").hasRole("ADMIN")
        		.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
        		.requestMatchers("/login/**").anonymous()
        		.requestMatchers("/css/**", "/js/**", "/utility/**").permitAll()
        		.requestMatchers("/static/**").permitAll()
		        .requestMatchers("/member/login").permitAll()
		        .requestMatchers("/member/logout_confirm").permitAll()
		        .requestMatchers("/member/signup").permitAll()
		        .requestMatchers("/member/signup_result").permitAll()
		        .requestMatchers("/member/checkAccountAndEmail").permitAll()
		        //棄用預設角色
//		        .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
		        //檢查權限
		        .requestMatchers("/member/**").access((authentication, context) ->
		        	new AuthorizationDecision(rbacService.hasPermission(context.getRequest(), authentication.get())))
	        	.requestMatchers("/chatground/**").access((authentication, context) ->
		        	new AuthorizationDecision(rbacService.hasPermission(context.getRequest(), authentication.get())))
		        .requestMatchers("/index", "/", "/favicon.ico").permitAll()
		        .requestMatchers("/redis/**").permitAll()
		        //除上面外的所有請求全部需要驗證認證
        		.anyRequest().authenticated());
        
        //表單
        http.formLogin((formLogin) -> formLogin
	    		.usernameParameter("uname").passwordParameter("pwd")
	    		.loginPage("/member/login").permitAll()
	    		.defaultSuccessUrl("/index").failureUrl("/member/login?error"));
        
        //登出
        http.logout((logout) -> logout
        		.deleteCookies("JSESSIONID")
        		.logoutUrl("/member/logout").permitAll()
        		.logoutSuccessUrl("/index"));//成功登出後跳轉至哪個頁面
        
        
        //處理例外，拒絕造訪就重新導向到403頁面
//      http.exceptionHandling((config) -> config
//      		.accessDeniedPage("/403"));//  403頁面要自己建
      
        //記住我功能
        http.rememberMe((config) -> config
	  		.rememberMeParameter("rememberme").key("uniqueAndSecret")
	  		.tokenValiditySeconds(86400));

      	//設定開啟CSRF保護預防攻擊
//      http.csrf().ignoringAntMatchers("/member/upload");
//      http.csrf().disable();

        return http.build();
    }


    private UserDetailsService service(){return new UserSecurityService();}
    
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(service()).passwordEncoder(getPasswordEncoder());
    }

    //測試角色授權url
//    @Autowired
//    public void configuredGlobal(AuthenticationManagerBuilder auth) throws Exception{
//        auth.inMemoryAuthentication()
//        	.passwordEncoder(new BCryptPasswordEncoder())
//        	.withUser("user")
//        	.password(new BCryptPasswordEncoder().encode("123"))
//            .roles("USER");
//    }
    
}
