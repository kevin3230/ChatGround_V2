package com.chatground.controller;

import com.chatground.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatground")
public class ChatgroundController {

    @GetMapping("/ground")
    public String chatground(Model model, Authentication authentication){
        String member_id = "not an mem_id";
        String member_nickname = "not found nickname";
        Object principal = authentication.getPrincipal();

        //取得使用者身分
        if(principal instanceof UserDetails){
            UserDetails user = (UserDetails) principal;
            if(user instanceof Member){
                Member member = (Member)user;
                member_id = String.valueOf(member.getId());
                member_nickname = String.valueOf(member.getNickName());
            }
        }
        model.addAttribute("member_id", member_id);
        model.addAttribute("member_nickname", member_nickname);
        return "chatground/chatground";
    }

}
