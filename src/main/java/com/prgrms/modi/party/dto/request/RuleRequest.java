package com.prgrms.modi.party.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class RuleRequest {

    @NotNull
    @Positive
    private Long ruleId;

    private String ruleName;

    public Long getRuleId() {
        return ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public RuleRequest() {
    }

    public RuleRequest(Long ruleId, String ruleName) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
    }

}
