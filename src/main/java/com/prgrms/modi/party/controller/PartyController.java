package com.prgrms.modi.party.controller;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.error.exception.InvalidAuthenticationException;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.service.PartyService;
import com.prgrms.modi.user.service.UserService;
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

@RestController
@RequestMapping("/api")
@Validated
public class PartyController {

    private final PartyService partyService;

    private final UserService userService;

    public PartyController(PartyService partyService, UserService userService) {
        this.partyService = partyService;
        this.userService = userService;
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

}