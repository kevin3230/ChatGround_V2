package com.chatground.repository;

import com.chatground.entity.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
//@Transactional
public class testMemberRepository {

    @Autowired
    private MemberRepository memberRepository;

    Member member;

    @Before
    public void before(){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        member = Member.builder()
                .account("account1")
                .password(bcrypt.encode("123456"))
                .nickName("first 1")
                .email("123@abc.com")
                .gender("X")
                .status("active")
                .build();


    }


    @Test
    public void testSave(){

//        member = Member.builder()
//                .account("account1")
//                .password("123456")
//                .nickName("first 1")
//                .email("123@abc.com")
//                .gender("X")
//                .status("active")
//                .role("user")
//                .build();
//        System.out.println(member);
        memberRepository.save(member);
    }

    @Test
    public void testFindAll(){
        List<Member> memberList = memberRepository.findAll();
        for(Member member : memberList){
            System.out.println(member);
        }
    }

    @Test
    public void testFindByAccount(){
        Member user = memberRepository.findByAccount("account1");
        System.out.println(user);
        for(GrantedAuthority authority : user.getAuthorities()){
            System.out.println(authority.getAuthority());
        }
    }

}
