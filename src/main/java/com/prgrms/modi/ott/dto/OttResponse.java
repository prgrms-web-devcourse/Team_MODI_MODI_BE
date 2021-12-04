package com.prgrms.modi.ott.dto;

public class OttResponse {

    private final Long ottId;

    private final String ottName;

    public OttResponse(Long ottId, String ottName) {
        this.ottId = ottId;
        this.ottName = ottName;
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
    }

}
