package com.prgrms.modi.common.oauth2;

import static com.prgrms.modi.common.oauth2.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.prgrms.modi.common.jwt.Jwt;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.service.UserService;
import com.prgrms.modi.utils.CookieUtil;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Jwt jwt;

    private final UserService userService;

    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    public OAuth2AuthenticationSuccessHandler(
        Jwt jwt,
        UserService userService,
        OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository
    ) {
        this.jwt = jwt;
        this.userService = userService;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws ServletException, IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauth2Token.getPrincipal();
            String provider = oauth2Token.getAuthorizedClientRegistrationId();
            User user = processUserOAuth2UserJoin(oAuth2User, provider);
            String token = generateToken(user);
            String redirectUri = determineTargetUrl(request, response, authentication);
            String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .queryParam("userId", user.getId())
                .build().toUriString();
            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    protected String determineTargetUrl(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue);

        return redirectUri.orElse(getDefaultTargetUrl());
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private User processUserOAuth2UserJoin(OAuth2User oAuth2User, String provider) {
        return userService.join(oAuth2User, provider);
    }

    private String generateToken(User user) {
        return jwt.sign(Jwt.Claims.of(user.getId(), new String[]{"ROLE_USER"}));
    }

}
