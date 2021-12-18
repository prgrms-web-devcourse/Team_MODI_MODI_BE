package com.prgrms.modi.user.dto;

import javax.validation.constraints.NotBlank;

public class UsernameRequest {

    @NotBlank
    String username;

    protected UsernameRequest() {

    }

    public UsernameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
