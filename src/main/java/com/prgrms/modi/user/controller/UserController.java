package com.prgrms.modi.user.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.user.dto.PointAmountDto;
import com.prgrms.modi.user.dto.UserPartyListResponse;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/me")
    @Operation(summary = "유저 개인 정보 조회", description = "파라미터 X, 토큰 Authorize만 필요, 다른 사용자 접근 불가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 개인 정보 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<UserResponse> getUserDetail(
        @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(
            userService.getUserDetail(authentication.userId));
    }

    @GetMapping(path = "/me/points")
    @Operation(summary = "유저 현재 포인트 조회", description = "파라미터 X, 토큰 Authorize만 필요, 다른 사용자 접근 불가")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 개인 정보 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<PointAmountDto> getUserPoints(
        @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(
            userService.getUserPoints(authentication.userId));
    }

    @GetMapping("/me/parties")
    public ResponseEntity<UserPartyListResponse> getUserPartyList(
        @RequestParam(value = "partyStatus") PartyStatus partyStatus,
        @RequestParam @Positive Integer size,
        @RequestParam(required = false) Long lastPartyId,
        @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(userService.getUserPartyList(1L, partyStatus, size, lastPartyId));
    }

}
