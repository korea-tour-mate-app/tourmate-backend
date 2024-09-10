package com.snowflake_guide.tourmate.domain.member.service;

import com.snowflake_guide.tourmate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 이메일이 이미 사용 중인지 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 이메일이 이미 사용 중이면 true, 아니면 false
     */
    public boolean isEmailAlreadyInUse(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
