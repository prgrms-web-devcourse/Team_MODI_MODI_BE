package com.prgrms.modi.user.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.prgrms.modi.common.jwt.JwtAuthentication;
import com.prgrms.modi.common.jwt.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtAuthenticationSecurityContextFactory implements
    WithSecurityContextFactory<WithMockJwtAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        JwtAuthenticationToken authentication =
            new JwtAuthenticationToken(
                new JwtAuthentication(annotation.token(), annotation.id()),
                null,
                createAuthorityList(annotation.role())
            );
        context.setAuthentication(authentication);
        return context;
    }

}
