package com.chatground.redis;

import com.chatground.dto.ChatgroundMessage;
import com.chatground.entity.GroundMessage;
import com.chatground.repository.GroundMessageRepository;
import com.chatground.utility.JwtUtil;

import io.jsonwebtoken.Claims;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class RedisService {
	
	private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GroundMessageRepository groundMessageRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    private static final int DEFAULT_TRIGGER_AMOUNT = 100;//觸發儲存GroundMessage到資料庫的訊息數量
    private static final int DEFAULT_RESERVED_AMOUNT = 30;//保留GroundMessage歷史訊息在記憶體，不儲存進資料庫的數量

    public void saveMessage(ChatgroundMessage req){
        String key = "message:chatground";
        
        String jwt = req.getJwt();
        String sender = "";
        
        try {
        	Claims claims = jwtUtil.parseToken(jwt);
        	sender = claims.get("sender", String.class);
        	
        }catch (Exception e) {
        	log.info("parsing jwt token error :{}", e);
		}
        
        
        
        GroundMessage messageDto = new GroundMessage();
        messageDto.setSender(Long.valueOf(sender));
        messageDto.setContent(req.getMessage());
        messageDto.setTime(Timestamp.valueOf(LocalDateTime.now()));

        try {
            stringRedisTemplate.opsForList().leftPush(key, objectMapper.writeValueAsString(messageDto));
            
        	
        }catch (JacksonException e) {
            log.info("parsing GroundMessage to JsonString error :{}", e);
        }
        
        try {
        	//檢查如果List長度超過triggerAmount，將最新reservedAmount則以前的舊訊息存到RDB，歷史訊息只保留reservedAmount則訊息
        	saveGroundMessageToRelationDBDefault();
        	
        }catch (Exception e) {
        	log.info("saveGroundMessageToRelativeDBDefault error :{}", e);
        }
    }
    
	//如果List長度超過triggerAmount，將最新reservedAmount 則以前的舊訊息存到RDB，歷史訊息只保留reservedAmount則訊息
    public boolean saveGroundMessageToRelationDB(int triggerAmount, int reservedAmount) {
    	boolean isSuccess = false;
    	String key = "message:chatground";
    	long groundMessageListSize = stringRedisTemplate.opsForList().size(key).longValue();
    	
    	try{
            //如果List長度超過triggerAmount，將最新reservedAmount 則以前的舊訊息存到RDB，歷史訊息只保留reservedAmount則訊息
            if(groundMessageListSize > triggerAmount) {
            	long saveAmount = 0;//要儲存進Relative DB的訊息數量
            	
            	List<GroundMessage> list = new LinkedList<>();  //batch insert GroundMessage使用
            	
            	//超過保留數量的訊息才儲存
            	if(groundMessageListSize > reservedAmount) {
            		saveAmount = groundMessageListSize - reservedAmount;
            	}
            	
            	if(saveAmount > 0) {
            		//get要儲存的元素放入tempList，暫不移除元素
            		List<String> tempList = stringRedisTemplate.opsForList().range(key, 0 - saveAmount, -1);
            		
            		//Parse GroundMessage from String, 要儲存的元素放入list
            		if(tempList != null) {
            			GroundMessage message;
            			for(String tempMessage : tempList) {
            				message = objectMapper.readValue(tempMessage, GroundMessage.class);
            				list.add(message);
            			}
            		}
            	
            		//Batch insert進資料庫
            		groundMessageRepository.saveAll(list);
            		
            		//儲存成功，從Redis的key="message:chatground" List移除已儲存的元素
            		//從Redis的List right tail移除固定數量元素，等於trim保留start到end index的元素
            		stringRedisTemplate.opsForList().trim(key, 0, groundMessageListSize - 1 - saveAmount);
            	}
            }
            
            isSuccess = true;
            
        }catch (JacksonException e) {
            log.info("parsing GroundMessage to JsonString error :{}", e);
        }catch (Exception e) {
    		log.info("saveGroundMessageToRelativeDB failed :{}", e);
    	}
    	
    	return isSuccess;
    }
    
    //預設如果List長度超過100，將最新30則以前的舊訊息存到RDB，歷史訊息只保留30則
    public boolean saveGroundMessageToRelationDBDefault() {
    	return saveGroundMessageToRelationDB(DEFAULT_TRIGGER_AMOUNT, DEFAULT_RESERVED_AMOUNT);
    }
}
