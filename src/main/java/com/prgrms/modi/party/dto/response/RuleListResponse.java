package com.prgrms.modi.party.dto.response;

import java.util.List;

public class RuleListResponse {

    private List<RuleResponse> rules;

    public List<RuleResponse> getRules() {
        return rules;
    }

    protected RuleListResponse() {
    }

    private RuleListResponse(List<RuleResponse> rules) {
        this.rules = rules;
    }

    public static RuleListResponse from(List<RuleResponse> rules) {
        return new RuleListResponse(rules);
    }

}
