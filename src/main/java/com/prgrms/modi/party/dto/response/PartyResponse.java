package com.prgrms.modi.party.dto.response;

import static java.time.temporal.ChronoUnit.DAYS;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.prgrms.modi.party.domain.Party;
import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PartyResponse {

    private Long partyId;

    private String grade;

    private Integer price;

    private Integer maxMemberCapacity;

    private Integer currentMemberCapacity;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer startsIn;

    private boolean mustFilled;

    public Long getPartyId() {
        return partyId;
    }

    public String getGrade() {
        return grade;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getMaxMemberCapacity() {
        return maxMemberCapacity;
    }

    public Integer getCurrentMemberCapacity() {
        return currentMemberCapacity;
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

    public boolean isMustFilled() {
        return mustFilled;
    }

    private PartyResponse(
        Long partyId, String grade, Integer price,
        Integer maxMemberCapacity, Integer currentMemberCapacity, LocalDate startDate,
        LocalDate endDate, Integer startsIn, boolean mustFilled
    ) {
        this.partyId = partyId;
        this.grade = grade;
        this.price = price;
        this.maxMemberCapacity = maxMemberCapacity;
        this.currentMemberCapacity = currentMemberCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startsIn = startsIn;
        this.mustFilled = mustFilled;
    }

    public static PartyResponse from(Party party) {
        return new PartyResponse(
            party.getId(),
            party.getOtt().getGrade(),
            party.getTotalFee(),
            party.getMaxMemberCapacity(),
            party.getCurrentMemberCapacity(),
            party.getStartDate(),
            party.getEndDate(),
            (int) DAYS.between(LocalDate.now(), party.getStartDate()),
            party.isMustFilled()
        );
    }

}
