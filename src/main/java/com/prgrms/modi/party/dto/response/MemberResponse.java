package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.user.domain.Member;

public class MemberResponse {

    private long userId;

    private String username;

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
