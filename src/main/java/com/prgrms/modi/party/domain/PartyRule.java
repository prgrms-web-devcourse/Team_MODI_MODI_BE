package com.prgrms.modi.party.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "party_rule")
public class PartyRule {

    @EmbeddedId
    private PartyRulePK id = new PartyRulePK();

    @MapsId(value = "partyId")
    @ManyToOne
    private Party party;

    @MapsId(value = "ruleId")
    @ManyToOne
    private Rule rule;

    protected PartyRule() {
    }

    public PartyRule(Party party, Rule rule) {
        this.party = party;
        this.rule = rule;
    }

}
