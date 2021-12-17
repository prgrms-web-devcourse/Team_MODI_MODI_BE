package com.prgrms.modi.party.dto.response;

import com.prgrms.modi.ott.domain.OTT;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

public class PartyListResponse {

    @ApiModelProperty(value = "OTT 서비스 ID")
    private Long ottId;

    @ApiModelProperty(value = "OTT 서비스 이름")
    private String ottName;

    @ApiModelProperty(value = "OTT 파티목록 전체 size")
    private Integer totalSize;

    @ApiModelProperty(value = "간략한 파티 정보")
    private List<PartyBriefResponse> partyList;

    public PartyListResponse(Long ottId, String ottName, Integer totalSize, List<PartyBriefResponse> partyList) {
        this.ottId = ottId;
        this.ottName = ottName;
        this.totalSize = totalSize;
        this.partyList = partyList;
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public List<PartyBriefResponse> getPartyList() {
        return partyList;
    }

    public static PartyListResponse from(OTT ott, Integer totalSize, List<PartyBriefResponse> parties) {
        return new PartyListResponse(
            ott.getId(),
            ott.getName(),
            totalSize,
            parties
        );
    }

}
