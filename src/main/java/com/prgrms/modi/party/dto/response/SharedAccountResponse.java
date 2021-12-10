package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.party.domain.Party;

public class SharedAccountResponse {

    private String sharedId;

    private String sharedPassword;

    protected SharedAccountResponse() {
    }

    private SharedAccountResponse(Party party) {
        this.sharedId = party.getSharedId();
        this.sharedPassword = party.getSharedPasswordEncrypted();
    }

    public static SharedAccountResponse from(Party party) {
        return new SharedAccountResponse(party);
    }

    public String getSharedId() {
        return sharedId;
    }

    public String getSharedPassword() {
        return sharedPassword;
    }

}
