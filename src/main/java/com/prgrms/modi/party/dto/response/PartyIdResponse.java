package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.party.domain.Party;
import io.swagger.annotations.ApiModelProperty;

public class PartyIdResponse {

    @ApiModelProperty(value = "파티 ID")
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
