package com.prgrms.modi.user.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @PositiveOrZero
    private Long points;

    @NotBlank
    private String provider;

    @NotBlank
    private String providerId;

    @Past
    private LocalDate dateOfBirth;

    @PastOrPresent
    private LocalDateTime deletedAt;

    protected User(){}

    public User(String username, Role role, Long points, String provider, String providerId, LocalDate dateOfBirth) {
        this.username = username;
        this.role = role;
        this.points = points;
        this.provider = provider;
        this.providerId = providerId;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

}
