/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.naver.template.social;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.naver.template.social.connect.SimpleConnectionFactory;
import com.naver.template.social.user.UserCookieGenerator;

/**
 * Simple little @Controller that invokes Facebook and renders the result.
 * The injected {@link Facebook} reference is configured with the required authorization credentials for the current user behind the scenes.
 * @author Keith Donald
 */
@Controller
public class HomeController {

	final static Logger log = LoggerFactory.getLogger(HomeController.class);

	@Inject
	SimpleConnectionFactory simpleConnectionFactory;

	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request) {
		String userId = getUserIdFromServiceSecurity(model, request);
		Facebook facebook = null;
		try {
			facebook = simpleConnectionFactory.getFacebook(userId);
		} catch (IllegalArgumentException e) {
			model.addAttribute("message", "Login Please");
			return "home";
		} catch (Exception e) {
			model.addAttribute("message", "Connect to SNS please.");
			return "home";
		}
		model.addAttribute("message", "Connected to SNS.");
		List<Reference> friends = facebook.friendOperations().getFriends();
		model.addAttribute("friends", friends);
		return "home";
	}

	/**
	 * @param model
	 * @param request
	 * @return
	 */
	private String getUserIdFromServiceSecurity(Model model, HttpServletRequest request) {
		String userId = userCookieGenerator.readCookieValue(request);
		model.addAttribute("userId", userId);
		return userId;
	}

	@RequestMapping(value = "/friends", method = RequestMethod.GET)
	public String friends(Model model, HttpServletRequest request) {
		String userId = getUserIdFromServiceSecurity(model, request);
		Facebook facebook = simpleConnectionFactory.getFacebook(userId);
		log.debug("facebook{}", facebook);
		List<Reference> friends = facebook.friendOperations().getFriends();
		model.addAttribute("friends", friends);

		return "friends";
	}

	UserCookieGenerator userCookieGenerator = new UserCookieGenerator();

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String userId, HttpServletResponse response) {
		userCookieGenerator.addCookie(userId, response);
		return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model, HttpServletResponse response) {
		userCookieGenerator.removeCookie(response);
		return "redirect:/";
	}

}