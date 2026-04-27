package com.chatground.redis;

import com.chatground.dto.ChatgroundMessage;
import com.chatground.entity.GroundMessage;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void saveMessage(ChatgroundMessage req){
        String key = "message:chatground";
        GroundMessage messageDto = new GroundMessage();
        messageDto.setSender(Long.valueOf(req.getSender()));
        messageDto.setContent(req.getMessage());
        messageDto.setTime(Timestamp.valueOf(LocalDateTime.now()));

        try{
            stringRedisTemplate.opsForList().rightPush(key, objectMapper.writeValueAsString(messageDto));
        }catch (JacksonException e) {
            log.info("parsing GroundMessage to JsonString error :{}", e);
        }
    }
}
