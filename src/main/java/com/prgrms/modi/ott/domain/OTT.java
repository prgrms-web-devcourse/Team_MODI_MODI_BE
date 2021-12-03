package com.prgrms.modi.ott.domain;

import com.prgrms.modi.common.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "otts")
public class OTT extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @PositiveOrZero
    private Integer subscriptionFee;

    @PositiveOrZero
    private Integer monthlyFee;

    @Positive
    private Integer maxMemberCapacity;

    @NotBlank
    private String grade;

}
