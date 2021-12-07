package com.prgrms.modi.user.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    protected User() {
    }

    public User(String username, Role role, Long points, String provider, String providerId, LocalDate dateOfBirth) {
        checkArgument(isNotEmpty(username), "username must be provided.");
        checkArgument(
            username.length() >= 1 && username.length() <= 8,
            "username length must be between 1 to 8 characters."
        );
        checkNotNull(role, "role must be provided.");
        checkNotNull(points, "points must be provided.");
        checkNotNull(provider, "provider must be provided.");
        checkNotNull(providerId, "providerId must be provided.");

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

    public String getUsername() {
        return username;
    }

    public Long getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("username", username)
            .append("role", role)
            .append("points", points)
            .append("provider", provider)
            .append("providerId", providerId)
            .append("dateOfBirth", dateOfBirth)
            .append("deletedAt", deletedAt)
            .toString();
    }

}
