package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;
import io.swagger.annotations.ApiModelProperty;

public class MemberResponse {

    @ApiModelProperty(value = "유저 ID")
    private long userId;

    @ApiModelProperty(value = "유저 닉네임")
    private String username;

    @ApiModelProperty(value = "유저 파티장 여부")
    private boolean isLeader;

    protected MemberResponse() {
    }

    private MemberResponse(Member member) {
        this.userId = member.getUser().getId();
        this.username = member.getUser().getUsername();
        this.isLeader = member.isLeader();
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member);
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLeader() {
        return isLeader;
    }

}
