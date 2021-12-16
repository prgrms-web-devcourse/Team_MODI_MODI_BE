package com.prgrms.modi.party.dto.response;

import io.swagger.annotations.ApiModelProperty;

public class SharedAccountResponse {

    @ApiModelProperty(value = "공유 OTT 계정 아이디")
    private String sharedId;

    @ApiModelProperty(value = "공유 OTT 계정 비밀번호")
    private String sharedPassword;

    protected SharedAccountResponse() {
    }

    public SharedAccountResponse(String sharedId, String sharedPassword) {
        this.sharedId = sharedId;
        this.sharedPassword = sharedPassword;
    }

    public String getSharedId() {
        return sharedId;
    }

    public String getSharedPassword() {
        return sharedPassword;
    }

}
