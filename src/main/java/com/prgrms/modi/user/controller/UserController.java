package com.prgrms.modi.user.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.error.exception.InvalidAuthorizationException;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.service.PartyService;
import com.prgrms.modi.user.dto.PointAmountResponse;
import com.prgrms.modi.user.dto.UserPartyListResponse;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.dto.UsernameListResponse;
import com.prgrms.modi.user.dto.UsernameRequest;
import com.prgrms.modi.user.dto.UsernameResponse;
import com.prgrms.modi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    private final PartyService partyService;

    private Logger log = LoggerFactory.getLogger(getClass());

    public UserController(UserService userService, PartyService partyService) {
        this.userService = userService;
        this.partyService = partyService;
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
        UserResponse resp = userService.getUserDetail(authentication.userId);
        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/me/username")
    @Operation(summary = "유저 닉네임 수정", description = "자동 생성된 닉네임으로만 수정 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "올바르지 않은 닉네임"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<Void> changeUsername(
        @RequestBody @Valid final UsernameRequest request,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication auth
    ) {
        userService.changeUsername(auth.userId, request.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/me/points")
    @Operation(summary = "유저 현재 포인트 조회", description = "자신만 접근 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 개인 정보 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<PointAmountResponse> getUserPoints(
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        PointAmountResponse resp = userService.getUserPoints(authentication.userId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me/parties")
    @Operation(summary = "유저가 참여한 파티 목록 조회", description = "자신만 접근 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 참여 파티 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증할 수 없는 경우 (UNAUTHORIZED)")
    })
    public ResponseEntity<UserPartyListResponse> getUserPartyList(
        @Parameter(name = "status", description = "ONGOING: 진행중인 파티 / RECRUITING: 모집중인 파티 / FINISHED: 끝난 파티 / DELETED: 삭제된 파티")
        @RequestParam(value = "status") PartyStatus partyStatus,

        @Parameter(name = "size", description = "불러 올 개수")
        @RequestParam @Positive Integer size,

        @Parameter(name = "lastSortingId", description = "마지막 정렬용 id, 최초 조회 시 생략 가능")
        @RequestParam(required = false) Long lastSortingId,

        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }

        UserPartyListResponse resp = userService
            .getUserPartyList(authentication.userId, partyStatus, size, lastSortingId);

        return ResponseEntity.ok(resp);
    }


    @GetMapping("/me/parties/{partyId}")
    @Operation(summary = "유저 참여한 파티 상세 조회", description = "자신만 접근 가능")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 참여 파티 정보 조회 성공 (OK)"),
        @ApiResponse(responseCode = "401", description = "토큰이 없거나 다른 파티의 상세 정보에 접근할 때 (UNAUTHORIZED)")
    })
    public ResponseEntity<PartyDetailResponse> getUserPartyDetail(
        @Parameter(name = "partyId", description = "조회할 파티 id") @PathVariable @Positive Long partyId,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        if (!partyService.isPartyMember(partyId, authentication.userId)) {
            throw new InvalidAuthorizationException("인가되지 않은 사용자입니다");
        }
        PartyDetailResponse resp = userService.getUserPartyDetail(partyId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/generate-username")
    @Operation(summary = "랜덤 닉네임 생성", description = "랜덤 닉네임 생성 API")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<UsernameListResponse> generateUsername(
        @Parameter(name = "size", description = "생성할 닉네임 개수") @RequestParam @Positive Integer size
    ) {
        List<UsernameResponse> generateUsernames = userService.generateUsernames(size);
        return ResponseEntity.ok(
            new UsernameListResponse(generateUsernames));
    }

}
