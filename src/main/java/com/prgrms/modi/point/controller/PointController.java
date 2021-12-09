package com.prgrms.modi.point.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.point.dto.PointChargeRequest;
import com.prgrms.modi.point.service.PointService;
import com.prgrms.modi.user.dto.PointAmountDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<PointAmountDto> chargePoints(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @RequestBody @Valid PointChargeRequest pointChargeRequest
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(pointService.chargePoints(authentication.userId, pointChargeRequest.getPoints()));
    }

}
