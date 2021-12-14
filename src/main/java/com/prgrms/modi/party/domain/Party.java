package com.prgrms.modi.party.domain;

import static java.time.temporal.ChronoUnit.MONTHS;

import com.prgrms.modi.common.domain.DeletableEntity;
import com.prgrms.modi.error.exception.NotEnoughPartyCapacityException;
import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "parties")
@Where(clause = "deleted_at is null")
public class Party extends DeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private Integer partyMemberCapacity;

    @Positive
    private Integer currentMember;

    @PositiveOrZero
    private Integer totalPrice;

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

    @Positive
    private int period;

    @ManyToOne
    @JoinColumn(name = "ott_id")
    private OTT ott;

    @OneToMany(mappedBy = "party", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<PartyRule> partyRules = new ArrayList<>();

    protected Party() {
    }

    private Party(Builder builder) {
        id = builder.id;
        partyMemberCapacity = builder.partyMemberCapacity;
        currentMember = builder.currentMember;
        monthlyReimbursement = builder.monthlyReimbursement;
        remainingReimbursement = builder.remainingReimbursement;
        startDate = builder.startDate;
        endDate = builder.endDate;
        period = (int) MONTHS.between(builder.startDate, builder.endDate);
        mustFilled = builder.mustFilled;
        sharedId = builder.sharedId;
        sharedPasswordEncrypted = builder.sharedPasswordEncrypted;
        status = builder.status;
        ott = builder.ott;
        totalPrice = ott.getMonthlyPrice() * period;
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

    public Integer getTotalPrice() {
        return totalPrice;
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

    public int getPeriod() {
        return period;
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

    public OTT getOtt() {
        return ott;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<PartyRule> getPartyRules() {
        return partyRules;
    }

    public void setOtt(OTT ott) {
        this.ott = ott;
    }

    public void setRules(List<Rule> rules) {
        if (!this.partyRules.isEmpty()) {
            partyRules = new ArrayList<>();
        }

        for (Rule rule : rules) {
            PartyRule partyRule = new PartyRule(this, rule);
            this.partyRules.add(partyRule);
        }
    }

    public void setLeaderMember(User user) {
        Member leader = new Member(this, user, true);
        this.members.add(leader);
    }

    public void addMember(User user, int monthlyPrice, int totalPrice) {
        Member member = new Member(this, user, false);
        this.members.add(member);
        this.increaseCurrentMember();
        this.increaseMonthlyReimbursement(monthlyPrice);
        this.increaseRemainingReimbursement(totalPrice);
    }

    public void reimburse() {
        remainingReimbursement -= monthlyReimbursement;
    }

    private void increaseMonthlyReimbursement(Integer monthlyReimbursement) {
        if (monthlyReimbursement < 0) {
            throw new IllegalArgumentException("월 상환금은 양수여야 합니다.");
        }
        this.monthlyReimbursement += monthlyReimbursement;
    }

    private void increaseRemainingReimbursement(Integer reimbursement) {
        if (reimbursement < 0) {
            throw new IllegalArgumentException("상환금은 양수여야 합니다.");
        }
        this.remainingReimbursement += reimbursement;
    }

    private void increaseCurrentMember() {
        if (this.currentMember >= this.partyMemberCapacity) {
            throw new NotEnoughPartyCapacityException("파티 정원이 다 찼습니다.");
        }
        this.currentMember++;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("startDate", startDate)
            .append("endDate", endDate)
            .toString();
    }

    public static final class Builder {

        private Long id;

        private Integer partyMemberCapacity;

        private Integer currentMember;

        private Integer monthlyReimbursement;

        private Integer remainingReimbursement;

        private LocalDate startDate;

        private LocalDate endDate;

        private boolean mustFilled;

        private String sharedId;

        private String sharedPasswordEncrypted;

        private PartyStatus status;

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

        public Builder ott(OTT ott) {
            this.ott = ott;
            return this;
        }

        public Party build() {
            return new Party(this);
        }

    }

}
