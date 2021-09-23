package com.chatground.controller;

import com.chatground.dto.AccountAndEmail;
import com.chatground.entity.Member;
import com.chatground.entity.SysRole;
import com.chatground.repository.MemberRepository;
import com.chatground.repository.SysRoleRepository;
import com.chatground.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;

    @GetMapping("/member/signup")
    public String signUpForm(Model model, Member member){
        model.addAttribute("member", member);
        return "member/MembersSignUp";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/signup")
    public String signUp(@Valid Member member, BindingResult bindingResult, RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            return "member/MembersSignUp";
        }

        System.out.println(member);

        PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        member.setPassword(pwEncoder.encode(member.getPassword()));
        member.setStatus("active");
        member.setSysRoleList(List.of(sysRoleRepository.findByRole("ROLE_USER")));
        memberRepository.save(member);

        attr.addFlashAttribute("member", member);
        return "redirect:/member/result";
    }

    @GetMapping("/member/result")
    public String result(){
        return "member/result";
    }

    @ResponseBody
    @PostMapping("/member/checkAccountAndEmail")
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
        accountAndEmail.setMessage("query format incorrect");
        return mapper.writeValueAsString(accountAndEmail);
    }

}
