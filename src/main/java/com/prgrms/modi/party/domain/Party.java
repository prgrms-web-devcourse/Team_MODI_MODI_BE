package com.prgrms.modi.party.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import com.prgrms.modi.error.exception.NotEnoughPartyCapacityException;
import com.prgrms.modi.ott.domain.OTT;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;

@Entity
@Table(name = "parties")
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private Integer partyMemberCapacity;

    @Positive
    private Integer currentMember;

    @PositiveOrZero
    private Integer totalFee;

    @PositiveOrZero
    private Integer monthlyReimbursement;

    @PositiveOrZero
    private Integer remainingReimbursement;

    @FutureOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate endDate;

    private boolean mustFilled;

    @NotBlank
    private String sharedId;

    @NotBlank
    private String sharedPasswordEncrypted;

    @Enumerated(EnumType.STRING)
    private PartyStatus status;

    @PastOrPresent
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "ott_id")
    private OTT ott;

    protected Party() {
    }

    private Party(Builder builder) {
        id = builder.id;
        partyMemberCapacity = builder.partyMemberCapacity;
        currentMember = builder.currentMember;
        totalFee = builder.totalFee;
        monthlyReimbursement = builder.monthlyReimbursement;
        remainingReimbursement = builder.remainingReimbursement;
        startDate = builder.startDate;
        endDate = builder.endDate;
        mustFilled = builder.mustFilled;
        sharedId = builder.sharedId;
        sharedPasswordEncrypted = builder.sharedPasswordEncrypted;
        status = builder.status;
        deletedAt = builder.deletedAt;
        ott = builder.ott;
    }

    public Long getId() {
        return id;
    }

    public Integer getPartyMemberCapacity() {
        return partyMemberCapacity;
    }

    public Integer getCurrentMember() {
        return currentMember;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public Integer getMonthlyReimbursement() {
        return monthlyReimbursement;
    }

    public Integer getRemainingReimbursement() {
        return remainingReimbursement;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isMustFilled() {
        return mustFilled;
    }

    public String getSharedId() {
        return sharedId;
    }

    public String getSharedPasswordEncrypted() {
        return sharedPasswordEncrypted;
    }

    public PartyStatus getStatus() {
        return status;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public OTT getOtt() {
        return ott;
    }

    public void reimburse() {
        remainingReimbursement -= monthlyReimbursement;
    }

    public void increaseMonthlyReimbursement(Integer monthlyReimbursement) {
        this.monthlyReimbursement += monthlyReimbursement;
    }

    public void increaseRemainingReimbursement(Integer reimbursement) {
        this.remainingReimbursement += reimbursement;
    }

    public void increaseCurrentMemberCapacity() {
        if (this.currentMember >= this.partyMemberCapacity) {
            throw new NotEnoughPartyCapacityException("파티 정원이 다 찼습니다.");
        }
        this.currentMember++;
    }

    public static final class Builder {

        private Long id;

        private Integer partyMemberCapacity;

        private Integer currentMember;

        private Integer totalFee;

        private Integer monthlyReimbursement;

        private Integer remainingReimbursement;

        private LocalDate startDate;

        private LocalDate endDate;

        private boolean mustFilled;

        private String sharedId;

        private String sharedPasswordEncrypted;

        private PartyStatus status;

        private LocalDateTime deletedAt;

        private OTT ott;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
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

        public Builder totalFee(Integer totalFee) {
            this.totalFee = totalFee;
            return this;
        }

        public Builder monthlyReimbursement(Integer monthlyReimbursement) {
            this.monthlyReimbursement = monthlyReimbursement;
            return this;
        }

        public Builder remainingReimbursement(Integer remainingReimbursement) {
            this.remainingReimbursement = remainingReimbursement;
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

        public Builder mustFilled(boolean mustFilled) {
            this.mustFilled = mustFilled;
            return this;
        }

        public Builder sharedId(String sharedId) {
            this.sharedId = sharedId;
            return this;
        }

        public Builder sharedPasswordEncrypted(String sharedPasswordEncrypted) {
            this.sharedPasswordEncrypted = sharedPasswordEncrypted;
            return this;
        }

        public Builder status(PartyStatus status) {
            this.status = status;
            return this;
        }

        public Builder deletedAt(LocalDateTime deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public Builder ott(OTT ott) {
            this.ott = ott;
            return this;
        }

        public Party build() {
            return new Party(this);
        }

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("startDate", startDate)
            .append("endDate", endDate)
            .toString();
    }

}
