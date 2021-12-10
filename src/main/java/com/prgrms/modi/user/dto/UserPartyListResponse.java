package com.prgrms.modi.user.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserPartyListResponse {

    private List<UserPartyBriefResponse> parties;

    public UserPartyListResponse(List<UserPartyBriefResponse> parties) {
        this.parties = parties;
    }

}
