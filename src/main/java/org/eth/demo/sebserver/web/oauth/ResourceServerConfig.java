/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eth.demo.sebserver.web.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${server.address}")
    private String webServerAdress;
    @Value("${server.port}")
    private String webServerPort;
    @Value("${sebserver.webservice.protocol}")
    private String webProtocol;
    @Value("${sebserver.oauth.clients.guiClient.id}")
    private String guiClientId;
    @Value("${sebserver.oauth.clients.guiClient.secret}")
    private String guiClientSecret;

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
                UriComponentsBuilder
                        .fromHttpUrl(this.webProtocol + "://" + this.webServerAdress)
                        .port(this.webServerPort)
                        .path("oauth/check_token")
                        .toUriString());
        tokenService.setClientId(this.guiClientId);
        tokenService.setClientSecret(this.guiClientSecret);
        return tokenService;
    }

    @Override

    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.resourceId(AuthorizationServerConfig.SEB_SERVER_API_RESOURCE_ID);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        restAPIAuthenticationEntryPoint(),
                        WebSecurityConfig.PROTECTED_URLS)
            .and()
                //.authenticationProvider(this.restAPIAuthenticationProvider)
                //.addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .csrf().disable(); // TODO enable for RAP gui?
      //@formatter:on
    }

    @Bean
    public AuthenticationEntryPoint restAPIAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(final HttpServletRequest request, final HttpServletResponse response,
                    final AuthenticationException authenticationException) throws IOException, ServletException {

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().println("{ \"error\": \"" + authenticationException.getMessage() + "\" }");

            }
        };
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(
                    final HttpServletRequest request,
                    final HttpServletResponse response,
                    final String url) throws IOException {

                System.out.println("************************************** no redirect");
                // No redirect!
            }
        });
        return successHandler;
    }

}
