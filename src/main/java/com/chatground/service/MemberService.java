package com.chatground.service;

import com.chatground.dto.AccountAndEmail;
import org.springframework.util.MultiValueMap;

public interface MemberService {

    AccountAndEmail checkAccountUnique(MultiValueMap<String, String> map);

    AccountAndEmail checkEmailUnique(MultiValueMap<String, String> map);
}
