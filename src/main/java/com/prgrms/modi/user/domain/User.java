package com.prgrms.modi.user.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import com.prgrms.modi.error.exception.NotEnoughPointException;
import com.prgrms.modi.history.domain.CommissionHistory;
import com.prgrms.modi.history.domain.PointHistory;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름은 필수입니다")
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @PositiveOrZero(message = "포인트는 0이상의 값만 가능합니다")
    private Long points;

    @NotBlank(message = "로그인은 naver나 kakao만 가능합니다")
    private String provider;

    @NotBlank(message = "providerId는 필수입니다")
    private String providerId;

    @Past
    private LocalDate dateOfBirth;

    @PastOrPresent
    private LocalDateTime deletedAt;

    protected User() {
    }

    public User(String username, Role role, Long points, String provider, String providerId, LocalDate dateOfBirth) {
        checkArgument(isNotEmpty(username));
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

    public void deductPoint(Long points) {
        if (this.points < points) {
            throw new NotEnoughPointException("포인트가 부족합니다.");
        }
        this.points -= points;
    }

}
