package com.chatground.schedule;

import com.chatground.entity.GroundMessage;
import com.chatground.repository.GroundMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 排程將redis的ground聊天訊息存入MySQL
 */
@Slf4j
@Component
public class GroundMessageScheduledTasks {

    @Autowired
    private GroundMessageRepository groundMessageRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 0 2 ? * *") //每天2:00執行
    public void syncGroundMessage(){

        log.info("start GroundMessageScheduledTasks");

        ObjectMapper objectMapper = new ObjectMapper();
        Long startTime = System.nanoTime();
        GroundMessage message;
        List<GroundMessage> list = new LinkedList();  //batch insert GroundMessage使用

        long messageCount = stringRedisTemplate.opsForList().size("message:chatground");
        try{
            for(long i = 0; i < messageCount; i++){
                String value = stringRedisTemplate.opsForList().leftPop("message:chatground");
                message = objectMapper.readValue(value, GroundMessage.class);
//                groundMessageRepository.save(message);
                list.add(message);
            }
            groundMessageRepository.saveAll(list);
        }catch (JsonProcessingException e){
            log.info("parsing JsonString to ChatGround error: {}", e);
        }

        log.info("finished GroundMessageScheduledTasks");
    }

}
