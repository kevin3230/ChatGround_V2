package com.chatground.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.chatground.entity.Member;
import com.chatground.security.UserPrincipal;
import com.chatground.utility.Gender;
import com.chatground.utility.MemberStatus;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@Transactional
public class testMemberRepository {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @Before
    public void before(){
        member = Member.builder()
                .account("account1")
                .password(passwordEncoder.encode("123456"))
                .nickName("first 1")
                .email("123@abc.com")
                .gender(Gender.X)
                .status(MemberStatus.ACTIVE)
                .build();


    }

    @Test
    public void testSave(){
        memberRepository.save(member);
    }

    @Test
    public void testFindByAccount(){
        Member member = memberRepository.findByAccount("account1");

        UserPrincipal user = new UserPrincipal(member);
        //顯示會員角色
        for(GrantedAuthority authority : user.getAuthorities()){
            System.out.println(authority.getAuthority());
        }
    }

}
