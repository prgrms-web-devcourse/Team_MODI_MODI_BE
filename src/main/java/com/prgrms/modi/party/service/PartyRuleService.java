package com.prgrms.modi.party.service;

import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyRule;
import com.prgrms.modi.party.repository.PartyRuleRepository;
import org.springframework.stereotype.Service;

@Service
public class PartyRuleService {

    private final PartyRuleRepository partyRuleRepository;

    private final RuleService ruleService;

    public PartyRuleService(PartyRuleRepository partyRuleRepository, RuleService ruleService) {
        this.partyRuleRepository = partyRuleRepository;
        this.ruleService = ruleService;
    }

    public void savePartyRule(Party party, Long ruleId) {
        partyRuleRepository.save(new PartyRule(party, ruleService.findRule(ruleId)));
    }

}
