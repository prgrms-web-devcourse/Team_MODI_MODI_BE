package com.prgrms.modi.history.service;

import com.prgrms.modi.history.domain.CommissionDetail;
import com.prgrms.modi.history.domain.CommissionHistory;
import com.prgrms.modi.history.repository.CommissionHistoryRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class CommissionHistoryService {

    public static final Double COMMISSION_PERCENTAGE = 0.05;

    private final CommissionHistoryRepository commissionHistoryRepository;

    public CommissionHistoryService(CommissionHistoryRepository commissionHistoryRepository) {
        this.commissionHistoryRepository = commissionHistoryRepository;
    }

    public void save(CommissionDetail commissionDetail, Integer price, User user) {
        commissionHistoryRepository.save(
            new CommissionHistory(commissionDetail, getCommission(price), user));
    }

    private int getCommission(Integer totalPrice) {
        return (int) (totalPrice * COMMISSION_PERCENTAGE);
    }

}
