package com.prgrms.modi.user.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserPartyListResponse {

    @ApiModelProperty(value = "유저 참여 파티의 갯수")
    private int totalSize;

    @ApiModelProperty(value = "유저 참여 파티 목록 - RECRUITING / ONGOING / FINISHED")

    private List<UserPartyBriefResponse> parties;

    public UserPartyListResponse(List<UserPartyBriefResponse> parties, int totalSize) {
        this.parties = parties;
        this.totalSize = totalSize;
    }

}
