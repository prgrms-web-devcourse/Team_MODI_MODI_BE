package com.prgrms.modi.user.domain;

import com.prgrms.modi.common.domain.DeletableEntity;
import com.prgrms.modi.error.exception.NotEnoughPointException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Table(name = "users")
public class User extends DeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름은 필수입니다")
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @PositiveOrZero(message = "포인트는 0이상의 값만 가능합니다")
    private Integer points;

    @NotBlank(message = "로그인은 naver나 kakao만 가능합니다")
    private String provider;

    @NotBlank(message = "providerId는 필수입니다")
    private String providerId;

    protected User() {
    }

    public User(String username, Role role, Integer points, String provider, String providerId) {
        checkArgument(isNotEmpty(username));
        this.username = username;
        this.role = role;
        this.points = points;
        this.provider = provider;
        this.providerId = providerId;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getPoints() {
        return points;
    }

    public void addPoints(Integer points) {
        if (points < 0) {
            throw new IllegalArgumentException("포인트는 양수여야 합니다.");
        }
        this.points += points;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("username", username)
            .append("role", role)
            .append("points", points)
            .append("provider", provider)
            .append("providerId", providerId)
            .toString();
    }

    public void deductPoint(Integer points) {
        if (this.points < points) {
            throw new NotEnoughPointException("포인트가 부족합니다.");
        }
        this.points -= points;
    }

}
