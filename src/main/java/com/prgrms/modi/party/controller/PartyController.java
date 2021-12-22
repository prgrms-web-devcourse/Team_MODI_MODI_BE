package com.prgrms.modi.party.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.AlreadyJoinedException;
import com.prgrms.modi.error.exception.ForbiddenException;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.error.exception.RequestAgainException;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.UpdateSharedAccountRequest;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.SharedAccountResponse;
import com.prgrms.modi.party.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api")
@Validated
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/otts/{ottId}/parties")
    @Operation(summary = "모집중인 파티 목록 조회", description = "특정 OTT 파티를 모집 중인 파티 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<PartyListResponse> getPartyList(
        @PathVariable @Positive Long ottId,
        @RequestParam @Positive Integer size,
        @RequestParam(required = false) @Positive Long lastPartyId
    ) {
        if (lastPartyId == null) {
            PartyListResponse resp = partyService.getPartyList(ottId, size);
            return ResponseEntity.ok(resp);
        }
        PartyListResponse resp = partyService.getPartyList(ottId, size, lastPartyId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/parties/{partyId}")
    @Operation(summary = "파티 상세 정보 조회", description = "파티 목록에서 파티 상세 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "NOTFOUND")
    })
    public ResponseEntity<PartyDetailResponse> getParty(@PathVariable @Positive Long partyId) {
        PartyDetailResponse resp = partyService.getParty(partyId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/parties/{partyId}/sharedAccount")
    @Operation(summary = "파티 공유 계정 조회", description = "파티의 OTT 공유계정 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "JWT가 없는 경우 UNAUTHORIZED"),
        @ApiResponse(responseCode = "403", description = "파티 멤버가 아닌 유저인 경우 FORBIDDEN")
    })
    public ResponseEntity<SharedAccountResponse> getPartySharedAccount(
        @PathVariable @Positive Long partyId,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        if (!partyService.isPartyMember(partyId, authentication.userId)) {
            throw new ForbiddenException("인가되지 않은 사용자입니다");
        }
        SharedAccountResponse resp = partyService.getSharedAccount(partyId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/parties")
    @Operation(summary = "파티 생성", description = "파티 생성. JWT토큰 필요")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "JWT가 없는 경우 UNAUTHORIZED")
    })
    public ResponseEntity<PartyIdResponse> createParty(
        @RequestBody @Valid final CreatePartyRequest request,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        PartyIdResponse resp = partyService.createParty(request, authentication.userId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/parties/{partyId}/join")
    @Operation(summary = "파티 가입 신청", description = "파티 가입을 신청합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "파티 가입 후, 가입 된 파티 ID를 응답으로 보내줍니다."),
        @ApiResponse(responseCode = "400", description = "파티 정원이 가득 찼거나, 포인트가 부족할 경우"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증 할 수 경우")
    })
    public ResponseEntity<PartyIdResponse> joinParty(
        @AuthenticationPrincipal @ApiIgnore JwtAuthentication authentication,
        @Parameter(description = "파티의 ID") @PathVariable Long partyId
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        if (partyService.isPartyMember(partyId, authentication.userId)) {
            throw new AlreadyJoinedException("이미 가입한 파티입니다");
        }
        int attemptCount = 0;
        int maxAttemptCount = 2;
        while(attemptCount < maxAttemptCount) {
            try {
                PartyIdResponse resp = partyService.joinParty(authentication.userId, partyId);
                return ResponseEntity.ok(resp);
            } catch (ObjectOptimisticLockingFailureException e) {
                attemptCount++;
            } catch (DataIntegrityViolationException e) {
                throw new AlreadyJoinedException("이미 가입한 파티입니다");
            }
        }
        throw new RequestAgainException("잠시 후에 다시 시도해주세요");
    }

    @DeleteMapping("/parties/{partyId}")
    @Operation(summary = "파티 삭제", description = "파티장은 파티원이 아무도 없고 파티 시작 전일 때 파티를 삭제할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "파티 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "파티원이 파티장을 제외하고 한 명이라도 있거나 파티 시작일이 지난 경우"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증 할 수 없는 경우"),
        @ApiResponse(responseCode = "403", description = "파티장이 아닌 유저인 경우")
    })
    public ResponseEntity<Void> deleteParty(
        @AuthenticationPrincipal @ApiIgnore JwtAuthentication authentication,
        @Parameter(description = "파티의 ID") @PathVariable @Positive Long partyId
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        if (!partyService.isPartyLeader(partyId, authentication.userId)) {
            throw new ForbiddenException("인가되지 않은 사용자입니다");
        }
        partyService.deleteParty(partyId);
        return ResponseEntity.noContent().build();
  }
  
    @PatchMapping("parties/{partyId}/sharedAccount/update")
    @Operation(summary = "파티 공유 계정 수정", description = "파티장이 파티 공유 계정을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "파티 공유 계정 수정 후, 수정된 파티 ID를 응답으로 보내줍니다."),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증 할 수 경우"),
        @ApiResponse(responseCode = "403", description = "파티장이 아닌 유저인 경우 FORBIDDEN")
    })
    public ResponseEntity<PartyIdResponse> updateSharedAccount(
        @AuthenticationPrincipal @ApiIgnore JwtAuthentication authentication,
        @Parameter(description = "파티의 ID") @PathVariable Long partyId,
        @RequestBody @Valid final UpdateSharedAccountRequest request
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        if (!partyService.isPartyLeader(partyId, authentication.userId)) {
            throw new ForbiddenException("인가되지 않은 사용자입니다");
        }
        PartyIdResponse resp = partyService.updateSharedAccount(partyId, request);
        return ResponseEntity.ok(resp);
    }

}
