package com.prgrms.modi.history.service;

import com.prgrms.modi.history.domain.PointDetail;
import com.prgrms.modi.history.domain.PointHistory;
import com.prgrms.modi.history.repository.PointHistoryRepository;
import com.prgrms.modi.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public void save(PointDetail pointDetail, Integer price, User user) {
        pointHistoryRepository.save(
            new PointHistory(pointDetail, price, user));
    }

}
