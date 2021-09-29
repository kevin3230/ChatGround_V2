package com.chatground.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * for大廳聊天室chatground專用,紀錄聊天訊息
 */
@Entity
@Data
public class GroundMessage{

    /**
     *訊息編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //GenerationType.IDENTITY不能使用batch insert
    private long id;

    /**
     *發訊人
     */
    @Column(nullable = false)
    private long sender;

    /**
     *發訊時間
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Timestamp time;

    /**
     *訊息內容
     */
    @Column(nullable = false)
    @Size(max = 500)
    private String content;
}
