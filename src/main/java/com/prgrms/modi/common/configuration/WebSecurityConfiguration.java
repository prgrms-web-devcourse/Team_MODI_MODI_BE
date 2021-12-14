package com.prgrms.modi.common.configuration;

import com.prgrms.modi.common.jwt.Jwt;
import com.prgrms.modi.common.jwt.JwtAuthenticationFilter;
import com.prgrms.modi.common.jwt.JwtConfigure;
import com.prgrms.modi.common.oauth2.OAuth2AuthenticationSuccessHandler;
import com.prgrms.modi.common.oauth2.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.prgrms.modi.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtConfigure jwtConfigure;

    private final UserService userService;

    public WebSecurityConfiguration(JwtConfigure jwtConfigure, UserService userService) {
        this.jwtConfigure = jwtConfigure;
        this.userService = userService;
    }

    private static final String[] AUTH_WHITELIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/swagger-resources",
        "/h2-console/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(AUTH_WHITELIST)
                    .permitAll()
                .antMatchers("api/users/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
            .and()
                .csrf().disable()
                .headers().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .rememberMe().disable()
                .logout().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
            .successHandler(oAuth2AuthenticationSuccessHandler())
                .and()
            .exceptionHandling()
                .and()
            .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(AUTH_WHITELIST);
    }

    @Bean
    public Jwt jwt() {
        return new Jwt(
            jwtConfigure.getIssuer(),
            jwtConfigure.getClientSecret(),
            jwtConfigure.getExpirySeconds()
        );
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwt());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        Jwt jwt = getApplicationContext().getBean(Jwt.class);
        return new OAuth2AuthenticationSuccessHandler(
            jwt,
            userService,
            oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

}
