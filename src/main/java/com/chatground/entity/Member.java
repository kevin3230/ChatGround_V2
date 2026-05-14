package com.chatground.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.chatground.utility.Gender;
import com.chatground.utility.MemberStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 會員
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member{

	/**
     *會員編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     *帳號
     */
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "帳號不可為空")
    @Size(max = 20)
    private String account;

    /**
     *密碼
     */
    @Column(nullable = false)
    @NotEmpty(message = "密碼不可為空")
    private String password;

    /**
     *暱稱
     */
    @Column(nullable = false)
    @NotEmpty(message = "暱稱不可為空")
    @Size(max = 10)
    private String nickName;

    /**
     *信箱
     */
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Email不可為空")
    @Size(max = 30)
    @Email
    private String email;

    /**
     *生日
     */
    @Column
    @Past
    private Date birth;

    /**
     *性別
     */
    @Column
    @Enumerated(EnumType.STRING)	//enum(X("X"),MALE("男"),FEMALE("女"))
    private Gender gender;

    /**
     *註冊日期
     */
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Timestamp regDate;

    /**
     *帳號狀態(封鎖,正常)
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)	//enum("locked", "active")
    private MemberStatus status;

    /**
     *角色(管理員, 使用者)
     */
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)	//enum("ADMIN", "USER")
//    private Role role;

    /**
     *頭像
     */
    @Column(length=10485760) //檔案大小10MB，DDL自動判斷column type 
    private byte[] picture;

    /**
     *聊天室
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="member_chatroom", joinColumns = {@JoinColumn(name="mem_id")},
    inverseJoinColumns = {@JoinColumn(name="cr_id")})
    private Set<ChatRoom> chatRoomSet;

    /**
     * 角色
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="member_sysrole", joinColumns = {@JoinColumn(name="mem_id")},
    inverseJoinColumns = {@JoinColumn(name="role_id")})
    private List<SysRole> sysRoleList;

}
