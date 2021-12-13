package com.prgrms.modi.utils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.domain.Rule;
import com.prgrms.modi.user.domain.User;
import java.time.LocalDate;
import org.mockito.Mockito;

public class MockCreator {

    public static User getUserFixture(Long id, Integer points) {
        User user = Mockito.mock(User.class);
        given(user.getId()).willReturn(id);
        given(user.getPoints()).willReturn(points);
        return user;
    }

    public static Party getPartyFixture(Long id) {
        long period = 6;
        Party party = Mockito.mock(Party.class);
        OTT ott = getOttFixture(1L);
        given(party.getId()).willReturn(id);
        given(party.getPartyMemberCapacity()).willReturn(4);
        given(party.getCurrentMember()).willReturn(2);
        given(party.getTotalPrice()).willReturn(16000);
        given(party.getMonthlyReimbursement()).willReturn(12000);
        given(party.getRemainingReimbursement()).willReturn(48000);
        given(party.getStartDate()).willReturn(LocalDate.now());
        given(party.getEndDate()).willReturn(LocalDate.now().plusMonths(period));
        given(party.isMustFilled()).willReturn(true);
        given(party.getStatus()).willReturn(PartyStatus.RECRUITING);
        given(party.getOtt()).willReturn(ott);
        return party;
    }

    public static OTT getOttFixture(Long id) {
        OTT ott = Mockito.mock(OTT.class);
        given(ott.getId()).willReturn(id);
        given(ott.getName()).willReturn("testOttName");
        given(ott.getEnglishName()).willReturn("testEnglishName");
        given(ott.getSubscriptionFee()).willReturn(16000);
        given(ott.getMonthlyPrice()).willReturn(4000);
        given(ott.getMaxMemberCapacity()).willReturn(4);
        given(ott.getGrade()).willReturn("프리미엄");
        return ott;
    }

    public static Rule getRuleFixture(Long id) {
        Rule rule = Mockito.mock(Rule.class);
        doReturn(id).when(rule).getId();
        doReturn("테스트 규칙 " + id).when(rule).getName();

        return rule;
    }

}
