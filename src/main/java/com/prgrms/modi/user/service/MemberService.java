package com.prgrms.modi.user.service;

import com.prgrms.modi.history.domain.CommissionDetail;
import com.prgrms.modi.history.domain.PointDetail;
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

    public User findUser(Long userId) {
        return userService.findUser(userId);
    }

    public void save(Party party, User user) {
        memberRepository.save(new Member(party, user, false));
        userService.saveCommissionHistory(CommissionDetail.PARTICIPATE, party.getTotalFee(), user);
        userService.savePointHistory(PointDetail.PARTICIPATE, party.getTotalFee(), user);
    }

}
