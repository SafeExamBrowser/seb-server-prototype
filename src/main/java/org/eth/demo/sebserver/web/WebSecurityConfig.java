/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web;

import org.eth.demo.sebserver.web.http.ClientConnectionAuthenticationFilter;
import org.eth.demo.sebserver.web.oauth.InternalUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1) // NOTE: places this HttpSecurity-Filter before the OAuth Security-Filter defined by the ResourceServerConfig
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            // TODO redirect to gui login
            new AntPathRequestMatcher("/error/**"),
            // NOTE: The integrated GUI is not covered within Spring-Security
            new AntPathRequestMatcher("/gui/**"),
            // RAP/RWT resources has to be accessible
            new AntPathRequestMatcher("/rwt-resources/**"));

    public static final RequestMatcher SEB_CLIENT_PROTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/client-connect/**"),
            new AntPathRequestMatcher("/ws/**"));

    public static final RequestMatcher SEB_SERVER_API_IGNORE_URL = new OrRequestMatcher(
            new AntPathRequestMatcher("/error/**"),
            new AntPathRequestMatcher("/client-connect/**"),
            new AntPathRequestMatcher("/ws/**"),
            new AntPathRequestMatcher("/gui/**"),
            new AntPathRequestMatcher("/rwt-resources/**"));

    public static final RequestMatcher SEB_SERVER_API_PROTECTED_URLS =
            new NegatedRequestMatcher(SEB_SERVER_API_IGNORE_URL);

    @Autowired
    private InternalUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder userPasswordEncoder;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher;

    @Override
    public void configure(final WebSecurity web) {
        web
                .ignoring()
                .requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
      //@formatter:off
        http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .requestMatcher(SEB_CLIENT_PROTECTED_URLS)
                    .addFilterBefore(
                            clientConnectionAuthenticationFilter(),
                            BasicAuthenticationFilter.class)
                    .authorizeRequests()
                    .requestMatchers(SEB_CLIENT_PROTECTED_URLS)
                    .authenticated()
                .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(
                            this.customAuthenticationEntryPoint,
                            WebSecurityConfig.SEB_CLIENT_PROTECTED_URLS)
                .and()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable()
                    .headers().frameOptions().disable()
                 .and()
                    .csrf().disable(); // TODO enable?
      //@formatter:on
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(this.userPasswordEncoder);
    }

    @Bean
    public FilterRegistrationBean<ClientConnectionAuthenticationFilter> registration(
            final ClientConnectionAuthenticationFilter filter) {

        final FilterRegistrationBean<ClientConnectionAuthenticationFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public ClientConnectionAuthenticationFilter clientConnectionAuthenticationFilter() throws Exception {
        return new ClientConnectionAuthenticationFilter(
                this.defaultAuthenticationEventPublisher);
    }

}
