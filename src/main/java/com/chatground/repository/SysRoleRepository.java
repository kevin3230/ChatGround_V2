package com.chatground.repository;

import com.chatground.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    SysRole findByRole(String role);
}
