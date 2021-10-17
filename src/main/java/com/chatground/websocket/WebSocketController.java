package com.chatground.websocket;

import com.chatground.dto.ChatgroundMessage;
import com.chatground.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class WebSocketController {

    private final static Set<String> onlineMembersSet = new HashSet<>();  //紀錄在線上的會員

    @Autowired
    private RedisService redisService;

    @MessageMapping("/open")
    @SendTo("/topic/getResponse")
    public ChatgroundMessage open(ChatgroundMessage req){
        ChatgroundMessage res;

            onlineMembersSet.add(req.getSender());  //線上人數+1

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

        //檢查訊息字數不超過400字
        String message = req.getMessage();
        if(message.length() > 400){
            message = message.substring(0, 400);
        }

        //存入Redis
        redisService.saveMessage(req);

        res = ChatgroundMessage.builder()
                .type("chat")
                .senderNickname(req.getSenderNickname())
                .message(message)
                .onlineCounter(onlineMembersSet.size())
                .build();
        return res;
    }

    @MessageMapping("/close")
    @SendTo("/topic/getResponse")
    public void close(ChatgroundMessage req){

        onlineMembersSet.remove(req.getSender());   //線上人數-1
//            System.out.println("disconnect");
//            onlineMembersSet.forEach(e -> System.out.println(e)); //測試剩餘線上人數

    }
}
