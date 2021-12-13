package com.prgrms.modi.party.service;

import com.prgrms.modi.error.exception.NotFoundException;
import com.prgrms.modi.party.domain.Rule;
import com.prgrms.modi.party.dto.response.RuleListResponse;
import com.prgrms.modi.party.dto.response.RuleResponse;
import com.prgrms.modi.party.repository.RuleRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public RuleListResponse getAllRule() {
        List<RuleResponse> rules = ruleRepository.findAll()
            .stream()
            .map(RuleResponse::from)
            .collect(Collectors.toList());

        return RuleListResponse.from(rules);
    }

    public Rule findRule(Long id) {
        return ruleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 규칙입니다"));
    }

}
