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
 * 系統推播
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "system_notice")
public class SystemNotice {

    /**
     *推播編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     *推播內容
     */
    @Column(nullable = false)
    @Size(max = 500)
    private String content;

    /**
     *推播時間
     */
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(nullable = false)
    private Timestamp time;


}
