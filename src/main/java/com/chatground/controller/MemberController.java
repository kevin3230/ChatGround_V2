package com.chatground.controller;

import com.chatground.dto.AccountAndEmail;
import com.chatground.entity.Member;
import com.chatground.repository.MemberRepository;
import com.chatground.repository.SysRoleRepository;
import com.chatground.service.MemberService;
import com.chatground.utility.LanguageUtils;
import com.chatground.utility.MemberStatus;
import com.chatground.utility.Role;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/member")
public class MemberController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;
    @Autowired
	private MessageSource messageSource;
    
    /**
     * 註冊頁面
     * @param model
     * @param member
     * @return
     */
    @GetMapping("/signup")
    public String signUpForm(Model model, Member member){
        model.addAttribute("member", member);
        return "member/MembersSignUp";
    }

    /**
     * 新增會員
     * @param member
     * @param bindingResult
     * @param picFile
     * @param attr
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public String signUp(@Valid Member member, BindingResult bindingResult, @RequestParam(value="mem_pic")MultipartFile picFile, RedirectAttributes attr, Model model){

        log.debug("BindingResult.hasErrors: " + String.valueOf(bindingResult.hasErrors()));

        if(bindingResult.hasErrors()){
            return "member/MembersSignUp";
        }

        List<String> errorMessages = new ArrayList<>();

        log.debug("Member content: " + member.toString());
        

        //驗證圖片格式
        if( !picFile.isEmpty()){    //判斷圖片檔案是否存在
            String picType = picFile.getContentType();
            if(!"image/png".equals(picType) && !"image/jpeg".equals(picType)){
                errorMessages.add(messageSource.getMessage("profile.picture.extension.mismatched", null, LanguageUtils.getLocale()));
                model.addAttribute("errorMessages", errorMessages);
                return "member/MembersSignUp";
            }else if(picFile.getSize() > 10240000L){	//圖片大小限制10mb
                errorMessages.add(messageSource.getMessage("profile.picture.oversize", new Object[]{"10"}, LanguageUtils.getLocale()));
                model.addAttribute("errorMessages", errorMessages);
                return "member/MembersSignUp";
            }else{
                try{
                    member.setPicture(picFile.getBytes());
                }catch (IOException e){
                    log.info("picFile getBytes() failed: {}", e);
                }
            }
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setStatus(MemberStatus.ACTIVE);
        member.setSysRoleList(List.of(sysRoleRepository.findByRole(Role.USER)));  //設定角色為USER
        try {
        	memberRepository.save(member);
        }catch(Exception e) {
        	log.info("MemberRepository save member failed. ", e);
        	
        	errorMessages.add(messageSource.getMessage("save.member.failed", null, LanguageUtils.getLocale()));
            model.addAttribute("errorMessages", errorMessages);
        	return "member/MembersSignUp";
        }

        //在註冊成功頁面顯示大頭照
        if(member.getPicture() != null){
            String mem_pic = Base64.getEncoder().encodeToString(member.getPicture());
            attr.addFlashAttribute("mem_pic", mem_pic);
        }

        attr.addFlashAttribute("member", member);

        return "redirect:/member/signup_result";
    }

    /**
     * 註冊成功頁面
     * @return
     */
    @GetMapping("/signup_result")
    public String signupResult(){
        return "member/signup_result";
    }


    /**
     * AJAX 判斷帳號或信箱是否重複
     * @param map
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/checkAccountAndEmail")
    public String checkAccountAndEmail(@RequestBody MultiValueMap<String, String> map) throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        AccountAndEmail accountAndEmail;

        if("checkAccount".equals(map.getFirst("action"))){
            accountAndEmail = memberService.checkAccountUnique(map);

            return mapper.writeValueAsString(accountAndEmail);
        }

        if("checkEmail".equals(map.getFirst("action"))){
            accountAndEmail = memberService.checkEmailUnique(map);

            return mapper.writeValueAsString(accountAndEmail);
        }

        accountAndEmail = new AccountAndEmail();
        accountAndEmail.setUnique(false);
        accountAndEmail.setError(true);
        accountAndEmail.setMessage("Query format incorrect.");
        return mapper.writeValueAsString(accountAndEmail);
    }

}
