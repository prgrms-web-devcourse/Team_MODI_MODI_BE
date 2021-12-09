package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;

public class MemberResponse {

    private long userId;

    private String username;

    private boolean isLeader;

    protected MemberResponse() {
    }

    public MemberResponse(Member member) {
        this.userId = member.getUser().getId();
        this.username = member.getUser().getUsername();
        this.isLeader = member.isLeader();
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

    public static MemberResponse from(Member member) {
        return new MemberResponse(member);
    }

}
