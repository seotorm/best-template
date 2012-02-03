/*
 * @(#)UserFactory.java $version 2012. 2. 1.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social.user;

import javax.servlet.http.HttpServletRequest;

/**
 * @author swseo
 */
public class UserFactory {
	public String getUserId(HttpServletRequest request) {
		User user = getUser(request);
		if (user == null) {
			return null;
		}
		return user.getId();
	}

	public User getUser(HttpServletRequest request) {
		return null;
	}
}
