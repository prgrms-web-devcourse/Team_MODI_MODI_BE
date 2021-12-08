package com.prgrms.modi.party.repository;

import com.prgrms.modi.party.domain.PartyRule;
import com.prgrms.modi.party.domain.PartyRulePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRuleRepository extends JpaRepository<PartyRule, PartyRulePK> {

}
