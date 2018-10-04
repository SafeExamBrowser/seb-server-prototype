/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/gui")
public class LoginController {

    // NOTE: this is not working with RAP
//    @GetMapping("/login/{institution}")
//    public ModelAndView redirectWithUsingForwardPrefix(final ModelMap model) {
//        return new ModelAndView("forward:/gui", model);
//    }

    // NOTE: this is working with RAP
    @GetMapping("/login/{institution}")
    public ModelAndView redirectWithUsingRedirectPrefix(final ModelMap model) {
        return new ModelAndView("redirect:/gui", model);
    }

}
