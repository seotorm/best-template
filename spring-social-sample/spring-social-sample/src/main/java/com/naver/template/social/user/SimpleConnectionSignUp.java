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
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

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
	 * SNS connect(/signin) 시 UserConnectionData를 insert 하기 직전에 호출된다.(함께 저장할 userId를 가져오기 위해서다.)
	 * @see JdbcUsersConnectionRepository
	 *
	 * 여기서는 request를 직접 참조할 수 없으므로 이전 프로세스(Intercepter)에서 
	 * request에 있는 인증 정보(userId)를 SecurityContext(ThreadLocal)에 저장해주어야 한다. @see UserInterceptor
	 * 만약 SecurityContext에 저장된 userId가 없을 경우 IllegalStateException이 발생된다.
	 * 
	 * @param connection
	 * @return
	 * @see org.springframework.social.connect.ConnectionSignUp#execute(org.springframework.social.connect.Connection)
	 */
	public String execute(Connection<?> connection) {
		User currentUser = SecurityContext.getCurrentUser();
		log.debug("currentUser is {}", currentUser);
		return currentUser.getId();
	}

}
