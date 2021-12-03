package com.prgrms.modi.party.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PartyRulePK implements Serializable {

    private Long partyId;

    private Long ruleId;

}
