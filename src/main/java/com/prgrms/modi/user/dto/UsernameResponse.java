package com.prgrms.modi.user.dto;

import io.swagger.annotations.ApiModelProperty;

public class UsernameResponse {

    @ApiModelProperty(value = "생성된 닉네임")
    String generatedUsername;

    public UsernameResponse(String generatedUsername) {
        this.generatedUsername = generatedUsername;
    }

    public String getGeneratedUsername() {
        return generatedUsername;
    }

}
