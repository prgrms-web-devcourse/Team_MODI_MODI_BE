package com.prgrms.modi.user.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.user.dto.PointAmountDto;
import com.prgrms.modi.user.dto.UserPartyListResponse;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/me")
    @Operation(summary = "유저 개인 정보 조회", description = "자신만 접근 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 개인 정보 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<UserResponse> getUserDetail(
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(
            userService.getUserDetail(authentication.userId));
    }

    @GetMapping(path = "/me/points")
    @Operation(summary = "유저 현재 포인트 조회", description = "자신만 접근 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 개인 정보 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<PointAmountDto> getUserPoints(
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(
            userService.getUserPoints(authentication.userId));
    }

    @GetMapping("/me/parties")
    @Operation(summary = "유저가 참여한 파티 조회", description = "자신만 접근 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 참여 파티 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<UserPartyListResponse> getUserPartyList(
        @Parameter(name = "status", description = "ONGOING: 진행중인 파티 / RECRUITING: 모집중인 파티 / FINISHED: 끝난 파티 / DELETED: 삭제된 파티")
        @RequestParam(value = "status") PartyStatus partyStatus,
        @Parameter(name = "size", description = "불러 올 개수")
        @RequestParam @Positive Integer size,
        @Parameter(name = "lastPartyId", description = "마지막 파티 id, 최초 조회 시 생략 가능")
        @RequestParam(required = false) Long lastPartyId,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(userService.getUserPartyList(authentication.userId, partyStatus, size, lastPartyId));
    }

}
