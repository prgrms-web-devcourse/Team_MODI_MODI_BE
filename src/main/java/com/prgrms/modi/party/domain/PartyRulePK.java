package com.prgrms.modi.party.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PartyRulePK implements Serializable {

    private Long partyId;

    private Long ruleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PartyRulePK that = (PartyRulePK) o;
        return Objects.equals(partyId, that.partyId) &&
            Objects.equals(ruleId, that.ruleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, ruleId);
    }

}
