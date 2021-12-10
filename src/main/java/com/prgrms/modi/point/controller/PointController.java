package com.prgrms.modi.point.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.point.dto.PointAddRequest;
import com.prgrms.modi.point.service.PointService;
import com.prgrms.modi.user.dto.PointAmountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping(path = "/add")
    @Operation(summary = "유저 포인트 충전", description = "유저의 포인트를 충전합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "포인트 중천 성공(OK)"),
        @ApiResponse(responseCode = "400", description = "충전할 포인트가 음수일 경우(BADREQUEST)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<PointAmountDto> addPoints(
        @AuthenticationPrincipal @ApiIgnore JwtAuthentication authentication,
        @Parameter(description = "충전할 포인트의 양") @RequestBody @Valid PointAddRequest pointAddRequest
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(pointService.addPoints(authentication.userId, pointAddRequest.getPoints()));
    }

}
