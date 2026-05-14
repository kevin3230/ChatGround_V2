package com.chatground.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chatground.entity.CustomProperties.CustomPropertiesId;
import com.chatground.repository.CustomPropertiesRepository;
import com.chatground.utility.SystemConstants;

@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	CustomPropertiesRepository customPropertiesRepository;
	
	/**
     * 首頁
     * @param model
     * @return
     */
	@RequestMapping(value={"/", "/index"})
    public String index(Model model){
    	//hard code:網站是否初始化
    	CustomPropertiesId initializedPropertiesId = new CustomPropertiesId(SystemConstants.DOMAIN_SYSTEM, SystemConstants.TYPE_INITIALIZATION);
    	boolean isInitialized = customPropertiesRepository.existsById(initializedPropertiesId);
    	
    	log.debug("CustomProperties isInitialized:" + String.valueOf(isInitialized));
    	if( !isInitialized) {	//若網站未初始化，則導至初始化頁面
        	
        	return "redirect:/initializeWebSite";
        }else {
        	return "index/index";
        }
    }
	
}
