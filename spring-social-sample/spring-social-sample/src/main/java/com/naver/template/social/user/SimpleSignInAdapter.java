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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Signs the user in by setting the currentUser property on the {@link SecurityContext}.
 * Remembers the sign-in after the current request completes by storing the user's id in a cookie.
 * This is cookie is read in {@link UserInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)} on subsequent requests.
 * @author Keith Donald
 * @see UserInterceptor
 */
public final class SimpleSignInAdapter implements SignInAdapter {

	/**
	 * SNS Provider 가 OAuth 마지막 단계인 Grant Access Token 을 Server로 전송하는데
	 * 이때 Server 에서 UserConnection 정보를 Insert 혹은 Update 한 후 호출된다.
	 * 
	 * SNS Connection 정보를 어딘가에(cache 등) 저장해야 한다면 여기서 처리하면 되겠다.
	 * 
	 * @param userId
	 * @param connection
	 * @param request
	 * @return
	 * @see org.springframework.social.connect.web.SignInAdapter#signIn(java.lang.String, org.springframework.social.connect.Connection, org.springframework.web.context.request.NativeWebRequest)
	 */
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		return null;
	}

}