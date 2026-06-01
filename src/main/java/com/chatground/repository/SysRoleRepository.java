package com.chatground.repository;

import com.chatground.entity.SysRole;
import com.chatground.utility.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    SysRole findByRole(Role role);
}
