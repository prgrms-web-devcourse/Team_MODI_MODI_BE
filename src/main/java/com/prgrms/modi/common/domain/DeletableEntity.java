package com.prgrms.modi.common.domain;

import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.PastOrPresent;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
public abstract class DeletableEntity extends BaseEntity {

    @PastOrPresent
    private LocalDateTime deletedAt;

}
