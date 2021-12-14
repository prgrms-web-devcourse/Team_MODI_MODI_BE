package com.prgrms.modi.party.controller;

import com.prgrms.modi.party.dto.response.RuleListResponse;
import com.prgrms.modi.party.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    private final RuleService ruleService;

    @GetMapping
    @Operation(summary = "전체 규칙 조회", description = "모든 규칙 태그 조회")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<RuleListResponse> getRuleList() {
        RuleListResponse resp = ruleService.getAllRule();
        return ResponseEntity.ok(resp);
    }


}
