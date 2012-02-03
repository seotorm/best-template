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
package com.naver.template.social.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

/**
 * Simple little {@link ConnectionSignUp} command that allocates new userIds in
 * memory. Doesn't bother storing a user record in any local database, since
 * this quickstart just stores the user id in a cookie.
 * 
 * @author Keith Donald
 */
public final class SimpleConnectionSignUp implements ConnectionSignUp {
	Logger log = LoggerFactory.getLogger(SimpleConnectionSignUp.class);

	/**
	 * 최초로 SNS 연결을 할 경우 실행된다.
	 * UserConnection 데이터가 추가되는데 이때 저장할 UserId를 리턴한다.
	 * request scope으로 실행되지 않기 때문에
	 * 이전 프로세스(? - Intercepter or Controller)에서 인증정보(userId)를 SecurityContext에 저장해주어야 한다. 
	 * 
	 * @param connection
	 * @return
	 * @see org.springframework.social.connect.ConnectionSignUp#execute(org.springframework.social.connect.Connection)
	 */
	public String execute(Connection<?> connection) {
		User currentUser = null;
		try {
			currentUser = SecurityContext.getCurrentUser();
		} catch (Exception e) {
			log.error("shall return tester.", e);
			return "tester";
		}

		log.debug("currentUser is {}", currentUser);
		return currentUser.getId();
	}

}
