package com.prgrms.modi.user.service;

import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final UserService userService;

    public MemberService(MemberRepository memberRepository, UserService userService) {
        this.memberRepository = memberRepository;
        this.userService = userService;
    }

    public void saveLeaderMember(Party party, Long userId) {
        memberRepository.save(new Member(party, userService.findUser(userId), true));
    }

    @Transactional
    public User findUserWithHistory(Long userId) {
        return userService.findUserWithPointHistory(userId);
    }

    @Transactional
    public void save(Party party, User user) {
        memberRepository.save(new Member(party, user, false));
    }

}
