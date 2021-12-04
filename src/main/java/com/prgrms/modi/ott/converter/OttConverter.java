package com.prgrms.modi.ott.converter;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.dto.OttResponse;

public class OttConverter {

    public static OttResponse toOttResponse(OTT ott) {
        return new OttResponse(ott.getId(), ott.getName());
    }

}
