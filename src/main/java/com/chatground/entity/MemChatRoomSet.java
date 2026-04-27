package com.chatground.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


/**
 * 此表格尚未用到
 * 會員聊天室對應
 */
@Entity
@Data
@Table(name = "mem_chat_room_set")
public class MemChatRoomSet {

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
