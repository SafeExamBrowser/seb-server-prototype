/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/rwt-resources/**", "/login*").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()

                //                .formLogin()
                //                .loginPage("/login")
                //                .loginProcessingUrl("/doLogin")
                //                .defaultSuccessUrl("/examview")
                //                .failureUrl("/login?error=true")

                .and()
                .logout()
                .permitAll()
                .and().csrf().disable() // TODO enable for RAP gui?
        ;
    }

}
