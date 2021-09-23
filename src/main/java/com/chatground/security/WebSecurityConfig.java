package com.chatground.security;

import com.chatground.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//指定為Spring Security設定類別
@EnableWebSecurity
//如果要開啟方法安全設定，則開啟此項
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        //使用BCrypt 加密
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.formLogin().usernameParameter("uname").passwordParameter("pwd")
                .loginPage("/member/login").permitAll().defaultSuccessUrl("/index")
                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/utility/**").permitAll()
                .antMatchers("/member/signup").permitAll()
                .antMatchers("/member/result").permitAll()
                .antMatchers("/member/checkAccountAndEmail").permitAll()
//                .antMatchers("/member/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/member/**").access("@rbacService.hasPermission(request, authentication)")
                .antMatchers("/index", "/", "/favicon.ico").permitAll()
                .antMatchers("/redis/**").permitAll()
                //除上面外的所有請求全部需要驗證認證
                .anyRequest().authenticated();
        http.logout().permitAll().deleteCookies("JSESSIONID").logoutUrl("/member/logout").logoutSuccessUrl("/index");//成功登出後跳轉至哪個頁面


        //處理例外，拒絕造訪就重新導向到403頁面
//        http.exceptionHandling().accessDeniedPage("/403");//  403頁面要自己建
        http.rememberMe().rememberMeParameter("rememberme").key("uniqueAndSecret").tokenValiditySeconds(86400);//記住我功能

        //設定開啟CSRF保護預防攻擊
//        http.csrf().ignoringAntMatchers("/member/upload");
        http.csrf().disable();
    }

    @Bean
    UserDetailsService service(){return new UserSecurityService();}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(service()).passwordEncoder(getPasswordEncoder());
    }

    //測試角色授權url
//    @Autowired
//    public void configuredGlobal(AuthenticationManagerBuilder auth) throws Exception{
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("user").password(new BCryptPasswordEncoder().encode("123"))
//                .roles("USER");
//    }
}
