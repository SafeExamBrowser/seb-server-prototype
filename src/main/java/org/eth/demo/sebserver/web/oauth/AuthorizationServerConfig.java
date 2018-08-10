/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.oauth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    public static final String[] GUI_CLIENT_GRANT_TYPES = new String[] { "password", "refresh_token" };
    public static final String[] GUI_CLIENT_SCOPES = new String[] { "web-service-api-read", "web-service-api-write" };
    public static final String SEB_SERVER_API_RESOURCE_ID = "seb-server-web-service-api";

    @Value("${sebserver.oauth.clients.guiClient.id}")
    private String guiClientId;
    @Value("${sebserver.oauth.clients.guiClient.secret}")
    private String guiClientSecret;
    @Value("${sebserver.oauth.clients.guiClient.accessTokenValiditySeconds}")
    private Integer guiClientAccessTokenValiditySeconds;
    @Value("${sebserver.oauth.clients.guiClient.refreshTokenValiditySeconds}")
    private Integer guiClientRefreshTokenValiditySeconds;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private InternalUserDetailsService userDetailsService;
    @Autowired
    private ClientPasswordEncoder clientPasswordEncoder;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(
            final AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {

        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(this.clientPasswordEncoder);
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
      //@formatter:off
        clients.inMemory()
                .withClient(this.guiClientId)
                    .secret(this.clientPasswordEncoder.encode(this.guiClientSecret))
                    .authorizedGrantTypes(GUI_CLIENT_GRANT_TYPES)
                    .scopes(GUI_CLIENT_SCOPES)
                    .resourceIds(SEB_SERVER_API_RESOURCE_ID)
                    .accessTokenValiditySeconds(this.guiClientAccessTokenValiditySeconds)
                    .refreshTokenValiditySeconds(this.guiClientRefreshTokenValiditySeconds)
                .and();
      //@formatter:on
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(this.dataSource);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(this.authenticationManager)
                .userDetailsService(this.userDetailsService)
                .accessTokenConverter(new JwtAccessTokenConverter());
    }

}
