package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.party.domain.Party;

public class PartyIdResponse {

    private final Long partyId;

    public PartyIdResponse(Party party) {
        this.partyId = party.getId();
    }

    public static PartyIdResponse from(Party party) {
        return new PartyIdResponse(party);
    }

    public Long getPartyId() {
        return partyId;
    }

}
