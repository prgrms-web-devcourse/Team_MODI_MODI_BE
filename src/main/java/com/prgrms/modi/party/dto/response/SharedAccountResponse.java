package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.party.domain.Party;
import io.swagger.annotations.ApiModelProperty;

public class SharedAccountResponse {

    @ApiModelProperty(value = "공유 OTT 계정 아이디")
    private String sharedId;

    @ApiModelProperty(value = "공유 OTT 계정 비밀번호")
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
