package com.chatground.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 此表格尚未用到
 * 聊天室
 */
@Entity
@Data
public class ChatRoom {

    /**
     *聊天室編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     *聊天室暱稱
     */
    @Column(nullable = false)
    @Size(max = 20, min = 1)
    @NotEmpty(message = "聊天室名稱不可為空")
    private String nickName;

    /**
     *會員
     */
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="member_chatroom", joinColumns = {@JoinColumn(name="cr_id")},
    inverseJoinColumns = {@JoinColumn(name="mem_id")})
    private Set<Member> memberSet;

    /**
     * 聊天訊息
     */
    @OneToMany
    @JoinColumn(name = "chatroom_id")
    private List<Message> messageList;

    /**
     * member ID與nickname對照
     */
    @Transient
    private Map<Long, String> member_ID_NicknameMap;
}
