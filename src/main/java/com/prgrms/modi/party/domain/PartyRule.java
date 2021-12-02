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
    private PartyRulePK id;

    @MapsId(value = "partyId")
    @ManyToOne
    private Party party;

    @MapsId(value = "ruleId")
    @ManyToOne
    private Rule rule;

}
