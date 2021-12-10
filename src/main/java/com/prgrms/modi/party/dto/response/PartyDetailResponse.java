package com.prgrms.modi.party.dto.response;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PartyDetailResponse {

    @ApiModelProperty(value = "파티 ID")
    private Long partyId;

    @ApiModelProperty(value = "OTT ID")
    private Long ottId;

    @ApiModelProperty(value = "OTT 서비스명")
    private String ottName;

    @ApiModelProperty(value = "OTT 서비스 등급")
    private String grade;

    @ApiModelProperty(value = "파티장 월 환급금")
    private Integer monthlyReimbersement;

    @ApiModelProperty(value = "파티장 잔여 환급금")
    private Integer remainingReimbersement;

    @ApiModelProperty(value = "월 파티 참여 가격")
    private Integer monthlyPrice;

    @ApiModelProperty(value = "총 파티 참여 가격")
    private Integer totalPrice;

    @ApiModelProperty(value = "파티 정원")
    private Integer partyMemberCapacity;

    @ApiModelProperty(value = "현재 참여 중 파티 인원")
    private Integer currentMember;

    @ApiModelProperty(value = "파티 시작일")
    private LocalDate startDate;

    @ApiModelProperty(value = "파티 종료일")
    private LocalDate endDate;

    @ApiModelProperty(value = "파티 시작까지 남은 일수")
    private Integer startsIn;

    @ApiModelProperty(value = "총 파티 진행 기간")
    private Integer period;

    @ApiModelProperty(value = "파티원 충족되지 않을 시 시작 여부")
    private boolean mustFilled;

    @ApiModelProperty(value = "파티 상태")
    private PartyStatus status;

    @ApiModelProperty(value = "파티 멤버 배열")
    private List<MemberResponse> members;

    @ApiModelProperty(value = "파티 규칙 배열")
    private List<RuleResponse> rules;

    protected PartyDetailResponse() {
    }

    private PartyDetailResponse(Party party, List<MemberResponse> members, List<RuleResponse> rules) {
        this.partyId = party.getId();
        this.ottId = party.getOtt().getId();
        this.ottName = party.getOtt().getName();
        this.grade = party.getOtt().getGrade();
        this.monthlyReimbersement = party.getMonthlyReimbursement();
        this.remainingReimbersement = party.getRemainingReimbursement();
        this.partyMemberCapacity = party.getPartyMemberCapacity();
        this.currentMember = party.getCurrentMember();
        this.startDate = party.getStartDate();
        this.endDate = party.getEndDate();
        this.startsIn = (int) DAYS.between(LocalDate.now(), this.startDate);
        this.period = (int) MONTHS.between(this.startDate, this.endDate);
        this.mustFilled = party.isMustFilled();
        this.totalPrice = party.gettotalPrice();
        this.monthlyPrice = this.totalPrice / this.period;
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

    public Integer getMonthlyReimbersement() {
        return monthlyReimbersement;
    }

    public Integer getRemainingReimbersement() {
        return remainingReimbersement;
    }

    public Integer getMonthlyPrice() {
        return monthlyPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
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
