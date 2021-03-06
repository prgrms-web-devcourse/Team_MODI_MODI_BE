package com.prgrms.modi.common.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jwt {

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm).withIssuer(issuer).build();
    }

    public String sign(Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (expirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
        }
        builder.withClaim("userId", claims.userId);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    public Claims verify(String token) {
        return new Claims(jwtVerifier.verify(token));
    }

    static public class Claims {

        Long userId;

        String[] roles;

        Date iat;

        Date exp;

        private Claims() {
        }

        Claims(DecodedJWT decodedJWT) {
            Claim userId = decodedJWT.getClaim("userId");
            if (!userId.isNull()) {
                this.userId = userId.asLong();
            }

            Claim roles = decodedJWT.getClaim("roles");
            if (!roles.isNull()) {
                this.roles = roles.asArray(String.class);
            }
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(Long userId, String[] roles) {
            Claims claims = new Claims();
            claims.userId = userId;
            claims.roles = roles;
            return claims;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append("userId", userId)
                .append("roles", Arrays.toString(roles))
                .append("iat", iat)
                .append("exp", exp)
                .toString();
        }

    }

}
