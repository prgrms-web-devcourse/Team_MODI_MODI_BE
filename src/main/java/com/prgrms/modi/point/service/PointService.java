package com.prgrms.modi.point.service;

import com.prgrms.modi.history.domain.PointDetail;
import com.prgrms.modi.history.domain.PointHistory;
import com.prgrms.modi.history.repository.PointHistoryRepository;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountResponse;
import com.prgrms.modi.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final UserService userService;

    private final PointHistoryRepository pointHistoryRepository;

    public PointService(UserService userService, PointHistoryRepository pointHistoryRepository) {
        this.userService = userService;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Transactional
    public PointAmountResponse addPoints(Long userId, Integer points) {
        User user = userService.findUser(userId);
        user.addPoints(points);

        PointHistory pointHistory = new PointHistory(PointDetail.ADD, points, user);
        pointHistoryRepository.save(pointHistory);

        return new PointAmountResponse(user.getPoints());
    }

}
