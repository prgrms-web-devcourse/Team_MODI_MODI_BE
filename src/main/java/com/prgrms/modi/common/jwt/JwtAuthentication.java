package com.prgrms.modi.common.jwt;

import org.apache.commons.lang3.builder.ToStringBuilder;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class JwtAuthentication {

    public final String token;

    public final Long userId;

    public JwtAuthentication(String token, Long userId) {
        checkArgument(isNotEmpty(token));
        checkArgument(userId != null);
        this.token = token;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("token", token)
            .append("userId", userId)
            .toString();
    }

}
