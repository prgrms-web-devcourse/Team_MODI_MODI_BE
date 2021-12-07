package com.prgrms.modi.common.jwt;

import static java.util.Collections.emptyList;

import com.auth0.jwt.exceptions.TokenExpiredException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Jwt jwt;

    public JwtAuthenticationFilter(Jwt jwt) {
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getToken(request);
            if (token != null) {
                try {
                    Jwt.Claims claims = verify(token);
                    log.debug("Jwt parse result: {}", claims);

                    Long userId = claims.userId;
                    List<GrantedAuthority> authorities = getAuthorities(claims);

                    if (userId != null && authorities.size() > 0) {
                        JwtAuthenticationToken authentication
                            = new JwtAuthenticationToken(new JwtAuthentication(token, userId), null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                } catch (Exception e) {
                    log.error("Jwt processing failed: {}", e.getMessage());
                }
            }
        } else {
            log.debug(
                "SecurityContextHolder not populated with security token, as it already contained: {}",
                SecurityContextHolder.getContext().getAuthentication()
            );
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            log.debug("Jwt token detected: {}", token);
            try {
                String jwtToken = token.substring(7);
                return URLDecoder.decode(jwtToken, StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e) {
                log.debug("Unable to get JWT Token");
            } catch (TokenExpiredException e) {
                log.debug("JWT Token has expired");
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0
            ? emptyList()
            : Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

}
