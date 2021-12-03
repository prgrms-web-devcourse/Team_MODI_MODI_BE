package com.prgrms.modi.party.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import com.prgrms.modi.ott.domain.OTT;

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

    public void reimburse() {
        remainingReimbursement -= monthlyReimbursement;
    }

}
