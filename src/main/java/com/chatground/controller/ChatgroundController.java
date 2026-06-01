package com.chatground.controller;

import com.chatground.security.UserPrincipal;
import com.chatground.utility.JwtUtil;
import com.chatground.utility.SystemConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatground")
public class ChatgroundController {
	@Autowired
	private JwtUtil jwtUtil;

    @GetMapping("/ground")
    public String chatground(Model model, Authentication authentication){
        String member_nickname = "nickname not found";
        String jwt = "";
        Object principal = authentication.getPrincipal();
        

        //取得使用者身分
        if(principal instanceof UserDetails){
            UserDetails user = (UserDetails) principal;
            if(user instanceof UserPrincipal){
            	UserPrincipal userPrincipal = (UserPrincipal)user;
                member_nickname = String.valueOf(userPrincipal.getNickName());
                jwt = jwtUtil.createToken(userPrincipal);
            }
        }
        model.addAttribute("textareaCharNumberLimit", SystemConstants.CHATGROUND_TEXTAREA_CHAR_NUMBER_LIMIT);
        model.addAttribute("member_nickname", member_nickname);
        model.addAttribute("jwt", jwt);
        return "chatground/chatground";
    }

}
