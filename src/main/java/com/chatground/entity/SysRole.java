package com.chatground.entity;

import lombok.Data;

import java.util.List;
import java.util.Set;

import com.chatground.utility.Role;

import jakarta.persistence.*;

@Data
@Entity
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cnName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)	//enum("ADMIN", "USER")
    private Role role;

    private String description;

    private Boolean available = false;//角色是否可用

    /**
     * 會員與角色對應
     */
    @ManyToMany(mappedBy = "sysRoleList")
    private List<Member> memberList;

    /**
     * 角色與授權對應
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="SysRolePermission", joinColumns={@JoinColumn(name="role_id")},
    inverseJoinColumns={@JoinColumn(name="permission_id")})
    private Set<SysPermission> sysPermissionList;


}
