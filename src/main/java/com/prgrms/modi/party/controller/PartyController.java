package com.prgrms.modi.party.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.ForbiddenException;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.RuleListResponse;
import com.prgrms.modi.party.dto.response.SharedAccountResponse;
import com.prgrms.modi.party.service.PartyService;
import com.prgrms.modi.party.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.constraints.Positive;
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
import springfox.documentation.annotations.ApiIgnore;

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
    @Operation(summary = "모집중인 파티 목록 조회", description = "특정 OTT 파티를 모집 중인 파티 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "OK")
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
    @Operation(summary = "파티 상세 정보 조회", description = "파티 목록에서 파티 상세 정보 조회")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<PartyDetailResponse> getParty(@PathVariable @Positive Long partyId) {
        return ResponseEntity.ok(partyService.getParty(partyId));
    }

    @GetMapping("/parties/{partyId}/sharedAccount")
    @Operation(summary = "파티 상세 정보 조회", description = "파티 목록에서 파티 상세 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "JWT가 없는 경우 UNAUTHORIZED"),
        @ApiResponse(responseCode = "403", description = "파티 멤버가 아닌 유저인 경우 FORBIDDEN")
    })
    public ResponseEntity<SharedAccountResponse> getPartySharedAccount(
        @PathVariable Long partyId,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }
        if (partyService.notPartyMember(partyId, authentication.userId)) {
            throw new ForbiddenException("인가되지 않은 사용자입니다");
        }
        return ResponseEntity.ok(partyService.getSharedAccount(partyId));
    }

    @PostMapping("/parties")
    @Operation(summary = "파티 생성", description = "파티 생성. JWT토큰 필요")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "JWT가 없는 경우 UNAUTHORIZED")
    })
    public ResponseEntity<PartyIdResponse> createParty(
        @RequestBody final CreatePartyRequest request,
        @ApiIgnore @AuthenticationPrincipal JwtAuthentication authentication
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
        @AuthenticationPrincipal @ApiIgnore JwtAuthentication authentication,
        @Parameter(description = "파티의 ID") @PathVariable Long partyId
    ) {
        if (authentication == null) {
            throw new InvalidAuthenticationException("인증되지 않는 사용자입니다");
        }

        return ResponseEntity.ok(partyService.joinParty(authentication.userId, partyId));
    }

    @GetMapping("/rules")
    @Operation(summary = "전체 규칙 조회", description = "모든 규칙 태그 조회")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<RuleListResponse> getRuleList() {
        return ResponseEntity.ok(ruleService.getAllRule());
    }

}
