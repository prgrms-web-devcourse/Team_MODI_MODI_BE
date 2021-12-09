package com.prgrms.modi.history.service;

import com.prgrms.modi.history.domain.CommissionDetail;
import com.prgrms.modi.history.domain.CommissionHistory;
import com.prgrms.modi.history.repository.CommissionHistoryRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class CommissionHistoryService {

    private final Double COMMISSION_PERCENTAGE = 0.05;

    private final CommissionHistoryRepository commissionHistoryRepository;

    public CommissionHistoryService(CommissionHistoryRepository commissionHistoryRepository) {
        this.commissionHistoryRepository = commissionHistoryRepository;
    }

    public void save(CommissionDetail commissionDetail, Integer fee, User user) {
        commissionHistoryRepository.save(
            new CommissionHistory(commissionDetail, getCommission(fee), user));
    }

    private int getCommission(Integer totalFee) {
        return (int) (totalFee * COMMISSION_PERCENTAGE);
    }

}
