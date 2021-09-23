package com.chatground.repository;

import com.chatground.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByAccount(String account);
    Member findByEmail(String email);
}
