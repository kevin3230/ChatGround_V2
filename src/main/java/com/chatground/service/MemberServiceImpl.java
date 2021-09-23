package com.chatground.service;

import com.chatground.dto.AccountAndEmail;
import com.chatground.entity.Member;
import com.chatground.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberRepository memberRepository;
    AccountAndEmail accountAndEmail = new AccountAndEmail();

    @Override
    public AccountAndEmail checkAccountUnique(MultiValueMap<String, String> map) {

        Member member = memberRepository.findByAccount(map.getFirst("account"));

        if(member == null){
            accountAndEmail.setUnique(true);
            accountAndEmail.setMessage("account OK");
        }else{
            accountAndEmail.setUnique(false);
            accountAndEmail.setMessage("account Duplicate");
        }
        return accountAndEmail;

    }

    @Override
    public AccountAndEmail checkEmailUnique(MultiValueMap<String, String> map) {

        Member member = memberRepository.findByEmail(map.getFirst("email"));

        if(member == null){
            accountAndEmail.setUnique(true);
            accountAndEmail.setMessage("email OK");
        }else{
            accountAndEmail.setUnique(false);
            accountAndEmail.setMessage("email Duplicate");
        }
        return accountAndEmail;
    }
}
