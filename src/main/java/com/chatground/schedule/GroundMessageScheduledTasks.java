package com.chatground.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chatground.redis.RedisService;

/**
 * 排程將Redis的ground聊天訊息存入資料庫
 */
@Component
public class GroundMessageScheduledTasks {
	
	private static final Logger log = LoggerFactory.getLogger(GroundMessageScheduledTasks.class);

    @Autowired
    private RedisService redisService;

    @Scheduled(cron = "0 0 2 ? * *") //每天2:00執行
    public void syncGroundMessage(){

        log.info("Start GroundMessageScheduledTasks.");

        //每日定期檢查如果List長度超過triggerAmount，將最新reservedAmount則以前的舊訊息存到RDB，歷史訊息只保留reservedAmount 則訊息
        redisService.saveGroundMessageToRelationDBDefault();

        log.info("Finished GroundMessageScheduledTasks.");
    }

}
