package com.prgrms.modi.party.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

public class UpdateSharedAccountRequest {

    @ApiModelProperty(value = "공유 OTT 계정 비밀번호")
    @NotBlank
    private String sharedPassword;

    protected UpdateSharedAccountRequest() {
    }

    public UpdateSharedAccountRequest(String sharedPassword) {
        this.sharedPassword = sharedPassword;
    }

    public String getSharedPassword() {
        return sharedPassword;
    }

}
