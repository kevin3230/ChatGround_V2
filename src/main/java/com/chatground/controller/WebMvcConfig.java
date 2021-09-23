package com.chatground.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index/index");
        registry.addViewController("/index").setViewName("index/index");
        registry.addViewController("/member/login").setViewName("member/MembersSignIn");
//        registry.addViewController("/member/signup").setViewName("member/MembersSignUp");
        registry.addViewController("/member/test").setViewName("chatground/test");

    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        if (!registry.hasMappingForPattern("/css/**")) {
//            registry.addResourceHandler("/css/**")
//                    .addResourceLocations("/css/");
//        }
//        if (!registry.hasMappingForPattern("/js/**")) {
//            registry.addResourceHandler("/js/**")
//                    .addResourceLocations("/js/");
//        }
//        if (!registry.hasMappingForPattern("/utility/**")) {
//            registry.addResourceHandler("/utility/**")
//                    .addResourceLocations("/utility/");
//        }
//    }
}
