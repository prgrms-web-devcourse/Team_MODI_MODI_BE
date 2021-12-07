package com.prgrms.modi.user.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.prgrms.modi.user.domain.User;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserResponse {

    @ApiModelProperty(value = "유저 아이디(PK)", required = true)
    private Long userId;

    @ApiModelProperty(value = "이름", required = true)
    private String username;

    @ApiModelProperty(value = "포인트", required = true)
    private Long points;

    public UserResponse(Long userId, String username, Long points) {
        this.userId = userId;
        this.username = username;
        this.points = points;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getPoints());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("userId", userId)
            .append("username", username)
            .append("points", points)
            .toString();
    }

}
