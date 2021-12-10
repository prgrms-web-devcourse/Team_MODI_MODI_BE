package com.prgrms.modi.user.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserPartyBriefResponse {

    private Long partyId;

    private Long ottId;

    private String ottName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isLeader;

    private Integer monthlyReimbursement;

    @QueryProjection
    public UserPartyBriefResponse(Long partyId, Long ottId, String ottName, LocalDate startDate, LocalDate endDate,
        boolean isLeader, Integer monthlyReimbursement) {
        this.partyId = partyId;
        this.ottId = ottId;
        this.ottName = ottName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isLeader = isLeader;
        this.monthlyReimbursement = monthlyReimbursement;
    }
    
}
