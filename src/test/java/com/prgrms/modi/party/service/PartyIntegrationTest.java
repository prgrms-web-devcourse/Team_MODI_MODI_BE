package com.prgrms.modi.party.service;

import com.prgrms.modi.history.repository.PointHistoryRepository;
import com.prgrms.modi.party.domain.Party;
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
    private PartyService partyService;

    @Test
    @DisplayName("매달 파티장에게 환급금을 주어야한다.")
    public void reimburseAll() {
        List<Party> reimbursableParties = partyRepository.findOngoingParty();
        LocalDate reimbursementDay = LocalDate.of(2021, 12, 2);
        partyService.reimburseAll(reimbursementDay);
        List<Party> partiesAfterReimbursement = partyRepository.findOngoingParty();

        for (int i = 0; i < partiesAfterReimbursement.size(); i++) {
            Party before = reimbursableParties.get(i);
            Party after = partiesAfterReimbursement.get(i);
            User user = after.getMembers().stream().filter(Member::isLeader).map(Member::getUser).findFirst().get();
            int reimburseAmount = before.getMonthlyReimbursement() * (before.getMembers().size() - 1);
            assertThat(
                after.getRemainingReimbursement(),
                equalTo(before.getRemainingReimbursement() - reimburseAmount)
            );
            assertThat(user.getPoints(), equalTo(reimburseAmount * 3));
            assertThat(pointHistoryRepository.findAllByUserId(user.getId()).size(), equalTo(3));
        }

    }

}
