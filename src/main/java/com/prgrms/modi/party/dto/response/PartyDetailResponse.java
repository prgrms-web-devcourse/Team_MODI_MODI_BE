package com.prgrms.modi.party.dto.response;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PartyDetailResponse {

    private Long partyId;

    private Long ottId;

    private String ottName;

    private String grade;

    private Integer monthlyFee;

    private Integer totalFee;

    private Integer partyMemberCapacity;

    private Integer currentMember;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer startsIn;

    private Integer period;

    private boolean mustFilled;

    private PartyStatus status;

    private List<MemberResponse> members;

    private List<RuleResponse> rules;

    protected PartyDetailResponse() {
    }

    private PartyDetailResponse(Party party, List<MemberResponse> members, List<RuleResponse> rules) {
        this.partyId = party.getId();
        this.ottId = party.getOtt().getId();
        this.ottName = party.getOtt().getName();
        this.grade = party.getOtt().getGrade();
        this.partyMemberCapacity = party.getPartyMemberCapacity();
        this.currentMember = party.getCurrentMember();
        this.startDate = party.getStartDate();
        this.endDate = party.getEndDate();
        this.startsIn = (int) DAYS.between(LocalDate.now(), this.startDate);
        this.period = (int) MONTHS.between(this.startDate, this.endDate);
        this.mustFilled = party.isMustFilled();
        this.totalFee = party.getTotalFee();
        this.monthlyFee = this.totalFee / this.period;
        this.status = party.getStatus();
        this.members = members;
        this.rules = rules;
    }

    public static PartyDetailResponse from(Party party) {
        return new PartyDetailResponse(
            party,
            party.getMembers()
                .stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList()),
            party.getPartyRules()
                .stream()
                .map(partyRule -> RuleResponse.from(partyRule.getRule()))
                .collect(Collectors.toList())
        );
    }

    public Long getPartyId() {
        return partyId;
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
    }

    public String getGrade() {
        return grade;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public Integer getPartyMemberCapacity() {
        return partyMemberCapacity;
    }

    public Integer getCurrentMember() {
        return currentMember;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getStartsIn() {
        return startsIn;
    }

    public Integer getPeriod() {
        return period;
    }

    public boolean isMustFilled() {
        return mustFilled;
    }

    public PartyStatus getStatus() {
        return status;
    }

    public List<MemberResponse> getMembers() {
        return members;
    }

    public List<RuleResponse> getRules() {
        return rules;
    }

}
