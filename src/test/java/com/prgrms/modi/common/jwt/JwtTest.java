package com.prgrms.modi.common.jwt;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Jwt jwt;

    @BeforeAll
    void setUp() {
        String issuer = "modi";
        String clientSecret = "test";
        int expirySeconds = 10;

        jwt = new Jwt(issuer, clientSecret, expirySeconds);
    }

    @Test
    @DisplayName("JWT 토큰을 생성하고 복호화 할 수 있다")
    void createTokenAndDecodeTest() {
        Jwt.Claims claims = Jwt.Claims.of(1L, new String[]{"ROLE_USER"});
        String encodedJWT = jwt.sign(claims);
        log.info("encodedJWT: {}", encodedJWT);

        Jwt.Claims decodedJWT = jwt.verify(encodedJWT);
        log.info("decodedJWT: {}", decodedJWT);

        assertThat(claims.userId, is(decodedJWT.userId));
        assertArrayEquals(claims.roles, decodedJWT.roles);
    }

}
