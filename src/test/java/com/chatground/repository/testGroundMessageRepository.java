package com.chatground.repository;

import com.chatground.dto.ChatgroundMessage;
import com.chatground.entity.ChatRoom;
import com.chatground.entity.GroundMessage;
import com.chatground.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class testGroundMessageRepository{

    @Autowired
    private GroundMessageRepository groundMessageRepository;

    private List<String> list;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void before() throws Exception {
        //假資料
        list = new ArrayList<>();
        for(int i = 1; i < 11; i++){
            GroundMessage message = new GroundMessage();
            message.setSender(i);
            message.setContent("" + i);
            message.setTime(Timestamp.valueOf(LocalDateTime.now()));
            list.add(objectMapper.writeValueAsString(message));
        }
    }

    @Test
    public void testFindByTimeBetween(){

    }

    /**
     * 測試List<GroundMessage>存入redis,再從redis取出list後batch insert存入MySQL
     * @throws Exception
     */
    @Test
    public void testBatchSave()throws Exception{
        stringRedisTemplate.opsForList().rightPushAll("message:chatground", list);
        List<GroundMessage> linkedList = new LinkedList<>();
        System.out.println(stringRedisTemplate.opsForList().size("message:chatground"));
        System.out.println(stringRedisTemplate.opsForList().size("message:chatground"));
        long size = stringRedisTemplate.opsForList().size("message:chatground");
        for(long i = 0; i < size; i++){
            System.out.println("i = " + i);
            linkedList.add(objectMapper.readValue(stringRedisTemplate.opsForList().leftPop("message:chatground"), GroundMessage.class));
        }
        groundMessageRepository.saveAll(linkedList);
    }
}
