package com.prgrms.modi.party.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.error.exception.InvalidAuthorizationException;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.RuleListResponse;
import com.prgrms.modi.party.dto.response.SharedAccountResponse;
import com.prgrms.modi.party.service.PartyService;

import com.prgrms.modi.party.service.RuleService;
import io.swagger.models.Response;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class PartyController {

    private final PartyService partyService;

    private final RuleService ruleService;

    public PartyController(PartyService partyService, RuleService ruleService) {
        this.partyService = partyService;
        this.ruleService = ruleService;
    }

    @GetMapping("/otts/{ottId}/parties")
    public ResponseEntity<PartyListResponse> getPartyList(
        @PathVariable Long ottId,
        @RequestParam @Positive Integer size,
        @RequestParam(required = false) Long lastPartyId
    ) {
        if (lastPartyId == null) {
            return ResponseEntity.ok(partyService.getPartyList(ottId, size));
        }
        return ResponseEntity.ok(partyService.getPartyList(ottId, size, lastPartyId));
    }

    @GetMapping("/parties/{partyId}")
    public ResponseEntity<PartyDetailResponse> getParty(@PathVariable @Positive Long partyId) {
        return ResponseEntity.ok(partyService.getParty(partyId));
    }

    @GetMapping("/parties/{partyId}/sharedAccount")
    public ResponseEntity<SharedAccountResponse> getPartySharedAccount(
        @PathVariable Long partyId,
        @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        if (partyService.notPartyMember(partyId, authentication.userId)) {
            throw new InvalidAuthorizationException("인가되지 않은 사용자입니다");
        }
        return ResponseEntity.ok(partyService.getSharedAccount(partyId));
    }

    @PostMapping("/parties")
    public ResponseEntity<PartyIdResponse> createParty(
        @RequestBody final CreatePartyRequest request,
        @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        return ResponseEntity.ok(partyService.createParty(request, authentication.userId));
    }

    @PostMapping("/parties/{partyId}/join")
    @Operation(summary = "파티 가입 신청", description = "파티 가입을 신청합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "파티 가입 후, 가입 된 파티 ID를 응답으로 보내줍니다."),
        @ApiResponse(responseCode = "400", description = "파티 정원이 가득 찼거나, 포인트가 부족할 경우"),
        @ApiResponse(responseCode = "401", description = "토큰이 없어 인증 할 수 경우")
    })
    public ResponseEntity<PartyIdResponse> joinParty(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @Parameter(description = "파티의 ID") @PathVariable Long partyId
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }

        return ResponseEntity.ok(partyService.joinParty(authentication.userId, partyId));
    }

    @GetMapping("/rules")
    public ResponseEntity<RuleListResponse> getRuleList() {
        return ResponseEntity.ok(ruleService.getAllRule());
    }

}
