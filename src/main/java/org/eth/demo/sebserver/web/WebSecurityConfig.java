/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web;

import org.eth.demo.sebserver.web.clientauth.LMSClientAuthenticationFilter;
import org.eth.demo.sebserver.web.clientauth.SEBClientAuthenticationFilter;
import org.eth.demo.sebserver.web.oauth.ExternalAuthProvider;
import org.eth.demo.sebserver.web.oauth.InternalUserDetailsService;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
            new AntPathRequestMatcher("/rwt-resources/**"),
            // project specific static resources
            new AntPathRequestMatcher("/images/**"));

    public static final AntPathRequestMatcher SEB_HANDSHAKE_ENDPOINT =
            new AntPathRequestMatcher("/sebauth/sebhandshake/**");
    public static final AntPathRequestMatcher SEB_WEB_SOCKET_ENDPOINT =
            new AntPathRequestMatcher("/ws/**");
    public static final AntPathRequestMatcher LMS_HANDSHAKE_ENDPOINT =
            new AntPathRequestMatcher("/sebauth/lmshandshake/**");

    public static final RequestMatcher SEB_CLIENT_ENDPOINTS = new OrRequestMatcher(
            SEB_HANDSHAKE_ENDPOINT,
            SEB_WEB_SOCKET_ENDPOINT);

    public static final RequestMatcher SEB_CONNECTION_PROTECTED_URLS = new OrRequestMatcher(
            SEB_CLIENT_ENDPOINTS,
            LMS_HANDSHAKE_ENDPOINT);

    public static final RequestMatcher SEB_SERVER_API_IGNORE_URL = new OrRequestMatcher(
            new AntPathRequestMatcher("/error/**"),
            new AntPathRequestMatcher("/sebauth/**"),
            new AntPathRequestMatcher("/ws/**"),
            new AntPathRequestMatcher("/gui/**"),
            new AntPathRequestMatcher("/rwt-resources/**"));

    public static final RequestMatcher SEB_SERVER_API_PROTECTED_URLS =
            new NegatedRequestMatcher(SEB_SERVER_API_IGNORE_URL);

    @Autowired
    private ExternalAuthProvider externalAuthProvider;
    @Autowired
    private InternalUserDetailsService userDetailsService;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private SEBClientAuthenticationFilter sebClientAuthenticationFilter;
    @Autowired
    @Qualifier(EncodingConfig.USER_PASSWORD_ENCODER_BEAN_NAME)
    private PasswordEncoder userPasswordEncoder;
    @Value("${sebserver.encrypt.password}")
    private String encryptPass;

    @Override
    public void configure(final WebSecurity web) {
        web
                .ignoring()
                .requestMatchers(PUBLIC_URLS);
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
                .passwordEncoder(this.userPasswordEncoder)
                .and()
                .authenticationProvider(this.externalAuthProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
      //@formatter:off
        http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .requestMatcher(SEB_CLIENT_ENDPOINTS)
                    .addFilterBefore(
                            this.sebClientAuthenticationFilter,
                            BasicAuthenticationFilter.class)
                    .authorizeRequests()
                    .requestMatchers(SEB_CONNECTION_PROTECTED_URLS)
                    .authenticated()
                .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(
                            this.customAuthenticationEntryPoint,
                            WebSecurityConfig.SEB_CONNECTION_PROTECTED_URLS)
                .and()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable()
                    .headers().frameOptions().disable()
                 .and()
                    .csrf().disable(); // TODO enable?
      //@formatter:on
    }

    @Bean
    public FilterRegistrationBean<SEBClientAuthenticationFilter> sebClientAuthenticationFilterReg(
            final SEBClientAuthenticationFilter filter) {

        final FilterRegistrationBean<SEBClientAuthenticationFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean(name = "encryptorBean")
    public StringEncryptor stringEncryptor() {
        final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(this.encryptPass);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

    @Configuration
    @EnableWebSecurity
    @Order(2) // NOTE: Places this HttpSecurity-Filter before the OAuth Security-Filter defined by the ResourceServerConfig
              //       and after the SEBAuthConfig Security-Filter
    public static class LMSAuthConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
        @Autowired
        private LMSClientAuthenticationFilter lmsClientAuthenticationFilter;

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
          //@formatter:off
            http
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .requestMatcher(LMS_HANDSHAKE_ENDPOINT)
                        .addFilterBefore(
                                this.lmsClientAuthenticationFilter,
                                BasicAuthenticationFilter.class)
                        .authorizeRequests()
                        .requestMatchers(LMS_HANDSHAKE_ENDPOINT)
                        .authenticated()
                    .and()
                        .exceptionHandling()
                        .defaultAuthenticationEntryPointFor(
                                this.customAuthenticationEntryPoint,
                                LMS_HANDSHAKE_ENDPOINT)
                    .and()
                        .formLogin().disable()
                        .httpBasic().disable()
                        .logout().disable()
                        .headers().frameOptions().disable()
                     .and()
                        .csrf().disable(); // TODO enable?
          //@formatter:on
        }

        @Bean
        public FilterRegistrationBean<LMSClientAuthenticationFilter> lmsClientAuthenticationFilter(
                final LMSClientAuthenticationFilter filter) {

            final FilterRegistrationBean<LMSClientAuthenticationFilter> registration =
                    new FilterRegistrationBean<>(filter);
            registration.setEnabled(false);
            return registration;
        }
    }

    @Configuration
    public static class EncodingConfig {
        public static final String USER_PASSWORD_ENCODER_BEAN_NAME = "userPasswordEncoder";
        public static final String CLIENT_PASSWORD_ENCODER_BEAN_NAME = "clientPasswordEncoder";

        @Bean(USER_PASSWORD_ENCODER_BEAN_NAME)
        public PasswordEncoder userPasswordEncoder() {
            return new BCryptPasswordEncoder(8);
        }

        @Bean(CLIENT_PASSWORD_ENCODER_BEAN_NAME)
        public PasswordEncoder clientPasswordEncoder() {
            return new BCryptPasswordEncoder(4);
        }
    }

}
