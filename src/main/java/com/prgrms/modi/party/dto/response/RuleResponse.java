package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.party.domain.Rule;
import io.swagger.annotations.ApiModelProperty;

public class RuleResponse {

    @ApiModelProperty(value = "규칙 ID")
    private Long ruleId;

    @ApiModelProperty(value = "파티 이름")
    private String ruleName;

    protected RuleResponse() {
    }

    private RuleResponse(Rule rule) {
        this.ruleId = rule.getId();
        this.ruleName = rule.getName();
    }

    public Long getRuleId() {
        return ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public static RuleResponse from(Rule rule) {
        return new RuleResponse(rule);
    }

}
