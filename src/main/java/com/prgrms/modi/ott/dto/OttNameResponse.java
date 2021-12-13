package com.prgrms.modi.ott.dto;

import com.prgrms.modi.ott.domain.OTT;

public class OttNameResponse {

    private final Long ottId;

    private final String ottName;

    private final String ottNameEn;

    private OttNameResponse(OTT ott) {
        this.ottId = ott.getId();
        this.ottName = ott.getName();
        this.ottNameEn = ott.getEnglishName();
    }

    public static OttNameResponse from(OTT ott) {
        return new OttNameResponse(ott);
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
    }

    public String getOttNameEn() {
        return ottNameEn;
    }

}
