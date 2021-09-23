package com.chatground.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * 會員聊天室對應
 */
@Entity
@Data
@Table(name = "mem_cr_set")
public class MemCRSet {

    /**
     *會員聊天室編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    /**
     *會員編號
     */
    @Column(name = "mem_id", nullable = false)
    @NotEmpty(message = "mem_id不可為空")
    private long memId;

    /**
     *聊天室編號
     */
    @Column(name = "cr_id", nullable = false)
    @NotEmpty(message = "cr_id不可為空")
    private long crId;

}
