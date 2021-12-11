package com.prgrms.modi.user.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.prgrms.modi.party.domain.PartyStatus;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserPartyBriefResponse {

    @ApiModelProperty(value = "파티 ID")
    private Long partyId;

    @ApiModelProperty(value = "파티 상태")
    private PartyStatus status;

    @ApiModelProperty(value = "OTT ID")
    private Long ottId;

    @ApiModelProperty(value = "OTT 서비스명")
    private String ottName;

    @ApiModelProperty(value = "파티 시작일")
    private LocalDate startDate;

    @ApiModelProperty(value = "파티 종료일")
    private LocalDate endDate;

    @ApiModelProperty(value = "파티 종료일")
    private boolean isLeader;

    @ApiModelProperty(value = "파티장 월 환급금")
    private Integer monthlyReimbursement;

    @ApiModelProperty(value = "파티장 잔여 환급금")
    private Integer remainingReimbursement;

    @ApiModelProperty(value = "월 파티 참여 가격")
    private Integer monthlyPrice;

    @ApiModelProperty(value = "총 파티 참여 가격")
    private Integer totalPrice;

    @QueryProjection
    public UserPartyBriefResponse(Long partyId, PartyStatus status, Long ottId, String ottName,
        LocalDate startDate, LocalDate endDate, boolean isLeader, Integer monthlyReimbursement,
        Integer remainingReimbursement, Integer monthlyPrice, Integer totalPrice) {
        this.partyId = partyId;
        this.status = status;
        this.ottId = ottId;
        this.ottName = ottName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isLeader = isLeader;
        this.monthlyReimbursement = monthlyReimbursement;
        this.remainingReimbursement = remainingReimbursement;
        this.monthlyPrice = monthlyPrice;
        this.totalPrice = totalPrice;
    }

}
