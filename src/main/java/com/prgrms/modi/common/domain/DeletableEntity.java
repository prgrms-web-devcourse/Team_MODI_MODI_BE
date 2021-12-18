package com.prgrms.modi.common.domain;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.PastOrPresent;

@MappedSuperclass
public abstract class DeletableEntity extends BaseEntity {

    @PastOrPresent
    public LocalDateTime deletedAt;

}
