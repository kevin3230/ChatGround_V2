package com.chatground.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 會員
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {

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
    @Column(columnDefinition = "enum('X','男','女')")
    @Size(max = 1)
    private String gender;

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
    @Column(nullable = false, columnDefinition = "enum('locked', 'active')")
    private String status;

    /**
     *角色(管理員, 使用者)
     */
//    @Column(nullable = false, columnDefinition = "enum('ADMIN', 'USER')")
//    private String role;

    /**
     *頭像
     */
    @Column
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
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name="member_sysrole", joinColumns = {@JoinColumn(name="mem_id")},
    inverseJoinColumns = {@JoinColumn(name="role_id")})
    private List<SysRole> sysRoleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<SysRole> roleList = this.getSysRoleList();
        for(SysRole role: roleList){
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    @Override
    public String getUsername(){
        return account;
    }

    @Override
    public String getPassword(){
        return password;
    }

}
