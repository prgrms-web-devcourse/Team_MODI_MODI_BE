package com.prgrms.modi.user.dto;

import java.util.ArrayList;
import java.util.List;

public class UsernameListResponse {

    private List<UsernameResponse> generatedUsernames = new ArrayList<>();

    public UsernameListResponse(List<UsernameResponse> generatedUsernames) {
        this.generatedUsernames = generatedUsernames;
    }

    public List<UsernameResponse> getGeneratedUsernames() {
        return generatedUsernames;
    }

}
