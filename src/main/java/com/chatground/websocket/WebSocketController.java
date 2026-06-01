package com.chatground.websocket;

import com.chatground.dto.ChatgroundMessage;
import com.chatground.redis.RedisService;
import com.chatground.utility.JwtUtil;
import com.chatground.utility.SystemConstants;

import io.jsonwebtoken.Claims;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {
	
	private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private final static Set<String> onlineMembersSet =  ConcurrentHashMap.newKeySet();  //紀錄在線上的會員，thread safe

    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtUtil jwtUtil;

    @MessageMapping("/open")
    @SendTo("/topic/getResponse")
    public ChatgroundMessage open(ChatgroundMessage req){
        ChatgroundMessage res;
        
        boolean isJwtValid = false;
        String jwt = req.getJwt();
        String sender = "";
        
        try {
        	Claims claims = jwtUtil.parseToken(jwt);
        	sender = claims.get("sender", String.class);
        	
        	isJwtValid = true;
        }catch (Exception e) {
        	log.info("parsing jwt token error :{}", e);
		}

        //驗證發送者身分
        log.debug("boolean isJwtValid = " + isJwtValid);
        if(isJwtValid && sender != null && !sender.isBlank()) {
        	//改由jwt取得account username
//        onlineMembersSet.add(req.getSender());  //線上人數+1
        	onlineMembersSet.add(sender != null ? sender : "");  //線上人數+1
        	
        }
        
        res = ChatgroundMessage.builder()
        		.type("onOpen")
        		.onlineCounter(onlineMembersSet.size())
        		.build();

        return res;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/getResponse")
    public ChatgroundMessage chat(ChatgroundMessage req){
        ChatgroundMessage res;
        
        boolean isJwtValid = false;
        String jwt = req.getJwt();
        String sender = "";
        String senderNickname = "";
        
        try {
        	Claims claims = jwtUtil.parseToken(jwt);
        	sender = claims.get("sender", String.class);
        	senderNickname = claims.get("senderNickname", String.class);
        	
        	isJwtValid = true;
        }catch (Exception e) {
        	log.info("parsing jwt token error :{}", e);
		}

        //檢查訊息字數不超過400字
        String message = req.getMessage();
        if(message.length() > SystemConstants.CHATGROUND_TEXTAREA_CHAR_NUMBER_LIMIT){
            message = message.substring(0, SystemConstants.CHATGROUND_TEXTAREA_CHAR_NUMBER_LIMIT);
        }

        //驗證發送者身分
        log.debug("boolean isJwtValid = " + isJwtValid);
        if(isJwtValid && sender != null && !sender.isBlank()) {
        	//存入Redis
        	redisService.saveMessage(req);
        	
        	res = ChatgroundMessage.builder()
        			.type("chat")
        			.senderNickname(senderNickname)
        			.message(message)
        			.onlineCounter(onlineMembersSet.size())
        			.build();
        }else {
        	res = ChatgroundMessage.builder()
        			.build();
        }
        
        
        return res;
    }

    @MessageMapping("/close")
    @SendTo("/topic/getResponse")
    public ChatgroundMessage close(ChatgroundMessage req){
        ChatgroundMessage res;
        
        boolean isJwtValid = false;
        String jwt = req.getJwt();
        String sender = "";
        
        try {
        	Claims claims = jwtUtil.parseToken(jwt);
        	sender = claims.get("sender", String.class);
        	
        	isJwtValid = true;
        }catch (Exception e) {
        	log.info("parsing jwt token error :{}", e);
		}

        //驗證發送者身分
        log.debug("boolean isJwtValid = " + isJwtValid);
        if(isJwtValid && sender != null && !sender.isBlank()) {
        	onlineMembersSet.remove(req.getSender());   //線上人數-1
        }
        
        log.debug("disconnect");
        onlineMembersSet.forEach(e -> log.debug(e)); //測試顯示剩餘線上成員

        //推送線上人數給client
        res = ChatgroundMessage.builder()
                .type("onSomeoneClose")
                .onlineCounter(onlineMembersSet.size())
                .build();
        return res;
    }
}
