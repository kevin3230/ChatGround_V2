package com.chatground.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 聊天訊息
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Message {

    /**
     *訊息編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     *發訊人
     */
    @Column(nullable = false)
    private long sender;

    /**
     *發訊時間
     */
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Timestamp time;

    /**
     *訊息內容
     */
    @Column(nullable = false)
    @Size(max = 500)
    private String content;

    /**
     *聊天室編號
     */
//    @Column(nullable = false)
    @ManyToOne
    private ChatRoom chatroom;

    /**
     *訊息狀態(失敗,已讀,未讀,收回,刪除)
     */
    @Column(nullable = false, columnDefinition = "enum('fail', 'read', 'unread', 'cancel', 'delete')")
    private String status;

}
