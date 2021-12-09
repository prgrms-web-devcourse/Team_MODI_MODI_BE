package com.prgrms.modi.point.service;

import com.prgrms.modi.history.domain.PointDetail;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountDto;
import com.prgrms.modi.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final UserService userService;

    public PointService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    public PointAmountDto chargePoints(Long userId, Integer points) {
        User user = userService.findUser(userId);
        user.chargePoints(points);
        userService.savePointHistory(PointDetail.ADD, points, user);
        return new PointAmountDto(user.getPoints());
    }

}
