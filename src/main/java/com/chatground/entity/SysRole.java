package com.chatground.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cnName;

    @Column(nullable = false, columnDefinition = "enum('ROLE_ADMIN', 'ROLE_USER')")
    private String role;

    private String description;

    private Boolean available = false;//角色是否可用

    /**
     * 會員與角色對應
     */
    @ManyToMany
    @JoinTable(name="member_sysrole", joinColumns = {@JoinColumn(name="role_id")},
    inverseJoinColumns = {@JoinColumn(name="mem_id")})
    private List<Member> memberList;

    /**
     * 角色與授權對應
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="SysRolePermission", joinColumns={@JoinColumn(name="role_id")},
    inverseJoinColumns={@JoinColumn(name="permission_id")})
    private List<SysPermission> sysPermissionList;


}
