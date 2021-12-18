package com.prgrms.modi.party.service;

import com.prgrms.modi.history.repository.PointHistoryRepository;
import com.prgrms.modi.ott.repository.OttRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class PartyIntegrationTest {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private OttRepository ottRepository;

    @Autowired
    private PartyService partyService;

    @Test
    @DisplayName("매달 파티장에게 환급금을 주어야한다.")
    @Transactional
    public void reimburseAll() {
        int originalRemainingReimbursementAmount = 25000;
        List<Party> reimbursableParties = partyRepository.findAllReimbursableParty();
        LocalDate reimbursementDay = LocalDate.of(2021, 12, 2);
        partyService.reimburseAll(reimbursementDay);

        for (Party after : reimbursableParties) {
            User user = after.getMembers().stream().filter(Member::isLeader).map(Member::getUser).findFirst().get();
            int reimburseAmount = after.getMonthlyReimbursement() * (after.getMembers().size() - 1);
            assertThat(
                after.getRemainingReimbursement(),
                equalTo(originalRemainingReimbursementAmount - reimburseAmount)
            );
            assertThat(user.getPoints(), equalTo(reimburseAmount * 3));
            assertThat(pointHistoryRepository.findAllByUserId(user.getId()).size(), equalTo(3));
        }

    }

    @Test
    @DisplayName("파티 시작 날짜가 되면 파티는 Ongoing 상태가 되야한다.")
    @Transactional
    public void checkRecruitingStatus() {
        Party partyWillBeOngoing = new Party.Builder()
            .partyMemberCapacity(4)
            .currentMember(4)
            .monthlyReimbursement(5000)
            .remainingReimbursement(15000)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(1))
            .mustFilled(true)
            .sharedId("testSharedId")
            .sharedPasswordEncrypted("testSharedPw")
            .status(PartyStatus.RECRUITING)
            .ott(ottRepository.findById(1L).get())
            .build();

        partyRepository.save(partyWillBeOngoing);
        partyService.changeRecruitingStatus(LocalDate.now());
        assertThat(partyWillBeOngoing.getStatus(), equalTo(PartyStatus.ONGOING));
    }

    @Test
    @DisplayName("파티의 isMustFilled 가 true 이며, 파티가 다 차지 않았을 상황에서 시작날짜가 됬을 경우 삭제되야한다.")
    @Transactional
    public void deletePartyWhenIsMustFilledTrue() {
        Party partyWillBeDeleted = new Party.Builder()
            .partyMemberCapacity(4)
            .currentMember(3)
            .monthlyReimbursement(5000)
            .remainingReimbursement(15000)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(1))
            .mustFilled(true)
            .sharedId("testSharedId")
            .sharedPasswordEncrypted("testSharedPw")
            .status(PartyStatus.RECRUITING)
            .ott(ottRepository.findById(1L).get())
            .build();

        partyRepository.save(partyWillBeDeleted);
        partyService.changeRecruitingStatus(LocalDate.now());
        assertThat(partyRepository.findAll().size(), equalTo(7));
    }

    @Test
    @DisplayName("파티 기간이 끝나면 FINISH 상태가 되야한다.")
    @Transactional
    public void changeFinishStatus() {
        Party partyWillBeFinished = new Party.Builder()
            .partyMemberCapacity(4)
            .currentMember(4)
            .monthlyReimbursement(5000)
            .remainingReimbursement(15000)
            .startDate(LocalDate.now().minusMonths(1))
            .endDate(LocalDate.now())
            .mustFilled(true)
            .sharedId("testSharedId")
            .sharedPasswordEncrypted("testSharedPw")
            .status(PartyStatus.ONGOING)
            .ott(ottRepository.findById(1L).get())
            .build();

        partyRepository.save(partyWillBeFinished);
        partyService.changeFinishStatus(LocalDate.now());
        assertThat(partyWillBeFinished.getStatus(), equalTo(PartyStatus.FINISHED));
    }

}
