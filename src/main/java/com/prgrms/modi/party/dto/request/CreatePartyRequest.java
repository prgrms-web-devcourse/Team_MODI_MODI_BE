package com.prgrms.modi.party.dto.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreatePartyRequest {

    @NotNull
    @Positive
    private Long ottId;

    private String ottName;

    private String grade;

    @NotNull
    @Positive
    private Integer partyMemberCapacity;

    @NotNull
    @Future
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Boolean mustFilled;

    @NotNull
    @Valid
    private List<RuleRequest> rules;

    @NotBlank
    private String sharedId;

    @NotBlank
    private String sharedPassword;

    protected CreatePartyRequest() {
    }

    private CreatePartyRequest(Builder builder) {
        ottId = builder.ottId;
        ottName = builder.ottName;
        grade = builder.grade;
        partyMemberCapacity = builder.partyMemberCapacity;
        startDate = builder.startDate;
        endDate = builder.endDate;
        mustFilled = builder.mustFilled;
        rules = builder.rules;
        sharedId = builder.sharedId;
        sharedPassword = builder.sharedPassword;
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

    public Integer getPartyMemberCapacity() {
        return partyMemberCapacity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Boolean isMustFilled() {
        return mustFilled;
    }

    public List<RuleRequest> getRules() {
        return rules;
    }

    public String getSharedId() {
        return sharedId;
    }

    public String getSharedPassword() {
        return sharedPassword;
    }


    public static final class Builder {

        private Long ottId;

        private String ottName;

        private String grade;

        private Integer partyMemberCapacity;

        private LocalDate startDate;

        private LocalDate endDate;

        private Boolean mustFilled;

        private List<RuleRequest> rules;

        private String sharedId;

        private String sharedPassword;

        public Builder() {
        }

        public Builder ottId(Long ottId) {
            this.ottId = ottId;
            return this;
        }

        public Builder ottName(String ottName) {
            this.ottName = ottName;
            return this;
        }

        public Builder grade(String grade) {
            this.grade = grade;
            return this;
        }

        public Builder partyMemberCapacity(Integer partyMemberCapacity) {
            this.partyMemberCapacity = partyMemberCapacity;
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

        public Builder mustFilled(Boolean mustFilled) {
            this.mustFilled = mustFilled;
            return this;
        }

        public Builder rules(List<RuleRequest> rules) {
            this.rules = rules;
            return this;
        }

        public Builder sharedId(String sharedId) {
            this.sharedId = sharedId;
            return this;
        }

        public Builder sharedPassword(String sharedPassword) {
            this.sharedPassword = sharedPassword;
            return this;
        }

        public CreatePartyRequest build() {
            return new CreatePartyRequest(this);
        }

    }

}
