package com.prgrms.modi.party.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import com.prgrms.modi.ott.domain.OTT;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@Entity
@Table(name = "parties")
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private Integer maxMemberCapacity;

    @Positive
    private Integer currentMemberCapacity;

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
        maxMemberCapacity = builder.maxMemberCapacity;
        currentMemberCapacity = builder.currentMemberCapacity;
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

    public Integer getMaxMemberCapacity() {
        return maxMemberCapacity;
    }

    public Integer getCurrentMemberCapacity() {
        return currentMemberCapacity;
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

    public static final class Builder {

        private Long id;

        private Integer maxMemberCapacity;

        private Integer currentMemberCapacity;

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

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder maxMemberCapacity(Integer val) {
            maxMemberCapacity = val;
            return this;
        }

        public Builder currentMemberCapacity(Integer val) {
            currentMemberCapacity = val;
            return this;
        }

        public Builder totalFee(Integer val) {
            totalFee = val;
            return this;
        }

        public Builder monthlyReimbursement(Integer val) {
            monthlyReimbursement = val;
            return this;
        }

        public Builder remainingReimbursement(Integer val) {
            remainingReimbursement = val;
            return this;
        }

        public Builder startDate(LocalDate val) {
            startDate = val;
            return this;
        }

        public Builder endDate(LocalDate val) {
            endDate = val;
            return this;
        }

        public Builder mustFilled(boolean val) {
            mustFilled = val;
            return this;
        }

        public Builder sharedId(String val) {
            sharedId = val;
            return this;
        }

        public Builder sharedPasswordEncrypted(String val) {
            sharedPasswordEncrypted = val;
            return this;
        }

        public Builder status(PartyStatus val) {
            status = val;
            return this;
        }

        public Builder deletedAt(LocalDateTime val) {
            deletedAt = val;
            return this;
        }

        public Builder ott(OTT val) {
            ott = val;
            return this;
        }

        public Party build() {
            return new Party(this);
        }

    }

}
