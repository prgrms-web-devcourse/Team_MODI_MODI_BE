package com.prgrms.modi.party.dto.response;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.prgrms.modi.party.domain.Party;
import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PartyBriefResponse {

    private Long partyId;

    private String grade;

    private Integer totalPrice;

    private Integer partyMemberCapacity;

    private Integer currentMember;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer startsIn;

    private Integer period;

    private boolean mustFilled;

    private PartyBriefResponse(Builder builder) {
        partyId = builder.partyId;
        grade = builder.grade;
        totalPrice = builder.totalPrice;
        partyMemberCapacity = builder.partyMemberCapacity;
        currentMember = builder.currentMember;
        startDate = builder.startDate;
        endDate = builder.endDate;
        startsIn = builder.startsIn;
        period = builder.period;
        mustFilled = builder.mustFilled;
    }

    public Long getPartyId() {
        return partyId;
    }

    public String getGrade() {
        return grade;
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

    public static PartyBriefResponse from(Party party) {
        return new Builder()
            .partyId(party.getId())
            .grade(party.getOtt().getGrade())
            .totalPrice(party.getTotalFee())
            .partyMemberCapacity(party.getPartyMemberCapacity())
            .currentMember(party.getCurrentMember())
            .startDate(party.getStartDate())
            .endDate(party.getEndDate())
            .startsIn((int) DAYS.between(LocalDate.now(), party.getStartDate()))
            .period((int) MONTHS.between(party.getStartDate(), party.getEndDate()))
            .mustFilled(party.isMustFilled())
            .build();
    }

    private static final class Builder {

        private Long partyId;

        private String grade;

        private Integer totalPrice;

        private Integer partyMemberCapacity;

        private Integer currentMember;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer startsIn;

        private Integer period;

        private boolean mustFilled;

        public Builder() {
        }

        public Builder partyId(Long partyId) {
            this.partyId = partyId;
            return this;
        }

        public Builder grade(String grade) {
            this.grade = grade;
            return this;
        }

        public Builder totalPrice(Integer totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder partyMemberCapacity(Integer partyMemberCapacity) {
            this.partyMemberCapacity = partyMemberCapacity;
            return this;
        }

        public Builder currentMember(Integer currentMember) {
            this.currentMember = currentMember;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder startsIn(Integer startsIn) {
            this.startsIn = startsIn;
            return this;
        }

        public Builder period(Integer period) {
            this.period = period;
            return this;
        }

        public Builder mustFilled(boolean mustFilled) {
            this.mustFilled = mustFilled;
            return this;
        }

        public PartyBriefResponse build() {
            return new PartyBriefResponse(this);
        }

    }

}
