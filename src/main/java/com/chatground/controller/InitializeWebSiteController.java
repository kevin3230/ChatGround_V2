package com.chatground.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chatground.entity.CustomProperties;
import com.chatground.entity.Member;
import com.chatground.entity.SysPermission;
import com.chatground.entity.SysRole;
import com.chatground.repository.CustomPropertiesRepository;
import com.chatground.repository.MemberRepository;
import com.chatground.repository.SysPermissionRepository;
import com.chatground.repository.SysRoleRepository;
import com.chatground.service.MemberService;
import com.chatground.utility.LanguageUtils;
import com.chatground.utility.MemberStatus;
import com.chatground.utility.Role;
import com.chatground.utility.SystemConstants;

import jakarta.validation.Valid;

@Controller
public class InitializeWebSiteController {
	private static final Logger log = LoggerFactory.getLogger(InitializeWebSiteController.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;
    @Autowired
    SysPermissionRepository sysPermissionRepository;
    @Autowired
	CustomPropertiesRepository customPropertiesRepository;
    @Autowired
	private MessageSource messageSource;

    /**
     * 初始化頁面
     * @param model
     * @param member
     * @return
     */
    @GetMapping("/initializeWebSite")
    public String initializeForm(Model model, Member member){
        model.addAttribute("member", member);
        return "index/initializeWebSite";
    }
    
    /**
     * 初始化，新增管理員
     * @param member
     * @param bindingResult
     * @param attr
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/initializeWebSite")
    public String initialize(@Valid Member member, BindingResult bindingResult, RedirectAttributes attr, Model model){

        log.debug("BindingResult.hasErrors: " + String.valueOf(bindingResult.hasErrors()));

        if(bindingResult.hasErrors()){
            return "index/initializeWebSite";
        }
        
        //先建立角色跟權限物件
        SysPermission groundPermission = new SysPermission();
        groundPermission.setName("/chatground/ground");
        groundPermission.setAvailable(true);
        groundPermission.setParentId(0);
        groundPermission.setPermission("ground");
        groundPermission.setResourceType("menu");
        groundPermission.setUrl("/chatground/ground");
        sysPermissionRepository.saveAndFlush(groundPermission);
        
        Set<SysPermission> sysPermissionSet = new HashSet<>();
        sysPermissionSet.add(groundPermission);
        
        SysRole userRole = new SysRole();
        userRole.setAvailable(true);
        userRole.setCnName("user");
        userRole.setRole(Role.USER);
        userRole.setSysPermissionList(sysPermissionSet);
        
        SysRole adminRole = new SysRole();
        adminRole.setAvailable(true);
        adminRole.setCnName("admin");
        adminRole.setRole(Role.ADMIN);
        adminRole.setSysPermissionList(sysPermissionSet);
        
        //由User新增
        sysRoleRepository.saveAndFlush(userRole);
        sysRoleRepository.saveAndFlush(adminRole);
        
        //新增管理員
        List<String> errorMessages = new ArrayList<>();

        log.debug("Member content: " + member.toString());

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setStatus(MemberStatus.ACTIVE);
        member.setSysRoleList(List.of(sysRoleRepository.findByRole(Role.ADMIN)));  //設定角色為ADMIN
        
        Member memberResult;
        
        try {
        	memberResult = memberRepository.saveAndFlush(member);
        }catch(Exception e) {
        	log.info("MemberRepository save member failed. ", e);
        	
        	errorMessages.add(messageSource.getMessage("save.member.failed", null, LanguageUtils.getLocale()));
            model.addAttribute("errorMessages", errorMessages);
        	return "index/initializeWebSite";
        }
        
        //設定系統初始化參數
        CustomProperties initialization = new CustomProperties();
        initialization.setDomain(SystemConstants.DOMAIN_SYSTEM);
        initialization.setType(SystemConstants.TYPE_INITIALIZATION);
        initialization.setCommonName("initialization");
        initialization.setHardCode(SystemConstants.HARD_CODE_YES);
        if(memberResult != null) {
        	initialization.setCreatedBy(memberResult.getId());
        	initialization.setModifiedBy(memberResult.getId());
        }
        
        customPropertiesRepository.save(initialization);
        

        attr.addFlashAttribute("member", member);

        return "redirect:/initializeWebSite_result";
    }
    
    /**
     * 初始化成功頁面
     * @return
     */
    @GetMapping("/initializeWebSite_result")
    public String initializationResult(){
        return "index/initializeWebSite_result";
    }
    
}
