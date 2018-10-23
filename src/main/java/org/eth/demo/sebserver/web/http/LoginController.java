/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eth.demo.util.Const;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gui")
public class LoginController {

    // NOTE: this is not working with RAP
//    @GetMapping("/login/{institution}")
//    public ModelAndView redirectWithUsingForwardPrefix(final ModelMap model) {
//        return new ModelAndView("forward:/gui", model);
//    }

    // NOTE: this is working with RAP
//    @GetMapping("/login/{institution}")
//    public ModelAndView redirectWithUsingRedirectPrefix(
//            final ModelMap model,
//            @PathVariable final String institution) {
//
//        return new ModelAndView("redirect:/gui?institutionName=" + institution, model);
//    }

    @GetMapping("/login/**")
    public String redirectWithUsingRedirectPrefix(
            final HttpServletRequest request,
            final HttpServletResponse response,
            @PathVariable final String institution) {

        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("institutionName", institution);
        response.setDateHeader("Expires", 0);
        return "redirect:/gui?" + Const.INSTITUTION_ID + "=" + institution;

    }

}
