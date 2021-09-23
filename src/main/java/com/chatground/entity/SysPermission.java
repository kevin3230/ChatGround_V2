package com.chatground.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class SysPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    //資源類型
    private String resourceType;
    private String url;//資源路徑

    //權限字串以|分隔,例如menu: role:*,button: role:create, role:update, role:delete, role:view
    private String permission;

    private long parentId;//父編號

    private String parentIds;//父編號列表

    private Boolean available = false;

    @Transient
    private List<String> permissions;

    @ManyToMany
    @JoinTable(name="SysRolePermission", joinColumns = {@JoinColumn(name="permission_id")},
    inverseJoinColumns = {@JoinColumn(name="role_id")})
    private List<SysRole> sysRoleList;

    public List<String> getPermissions(){
        return Arrays.asList(permission.trim().split("|"));
    }

    public void setPermissions(List<String> permissions){
        this.permissions = permissions;
    }

}
