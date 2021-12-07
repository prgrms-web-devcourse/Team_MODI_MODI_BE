package com.prgrms.modi.party.dto;

import com.prgrms.modi.ott.domain.OTT;
import java.util.List;

public class PartyListResponse {

    private Long ottId;

    private String ottName;

    private List<PartyResponse> partyList;

    public PartyListResponse(Long ottId, String ottName, List<PartyResponse> partyList) {
        this.ottId = ottId;
        this.ottName = ottName;
        this.partyList = partyList;
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
    }

    public List<PartyResponse> getPartyList() {
        return partyList;
    }

    public static PartyListResponse from(OTT ott, List<PartyResponse> parties) {
        return new PartyListResponse(
            ott.getId(),
            ott.getName(),
            parties
        );
    }

}
