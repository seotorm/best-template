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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author swseo
 */
public final class UserInterceptor extends HandlerInterceptorAdapter {

	static final Logger log = LoggerFactory.getLogger(UserInterceptor.class);

	private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator();

	private final UsersConnectionRepository connectionRepository;

	public UserInterceptor(UsersConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}

	/**
	 * SNS 인증
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String userId = getUserIdFromServiceSecurity(request);

		// Sign In 요청일 경우.
		if (requestForSignIn(request)) {
			// Provider 의 인증완료요청일경우.
			if (isGrantAccessTokenRequestFromProvider(request)) {
				// SecurityContext에 임시저장했다가 afterCommpletion 에서 삭제한다.
				// 임시저장된 값은 userConnectionData insert 시에 사용됨.
				// SimpleConnectionSignUp.excute() 참조.
				rememberUser(userId);
			}
		}

		// sign out 시 userConnectionData 삭제 처리.
		// TODO 요구사항에 따라 분리할 것.
		handleSignOut(request, response);

		return true;
	}

	/**
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (requestForSignIn(request)) {
			SecurityContext.remove();
		}
	}

	// internal helpers

	/**
	 * userConnectionData를 생성하기 위해 userId를 임시저장.
	 * 
	 * @param request
	 * @param response
	 */
	private void rememberUser(String userId) {
		log.debug("userId is {}", userId);
		if (userId == null) {
			return;
		}
		SecurityContext.setCurrentUser(new User(userId));
	}

	/**
	 * TODO 해당 Service Security 정책에 따라서 userId를 가져올수 있도록 수정할 것.
	 * @param request
	 * @return
	 */
	private String getUserIdFromServiceSecurity(HttpServletRequest request) {
		String userId = userCookieGenerator.readCookieValue(request);
		return userId;
	}

	/**
	 * OAuth 인증 마지막 단계인지 체크.
	 * 마지막 인증단계라면 Provider가 Service에 code(OAuth2) 혹은 oauth_token(OAuth2) 파라미터를 전달하게 된다.
	 * 
	 * @param request
	 * @return
	 */
	private boolean isGrantAccessTokenRequestFromProvider(HttpServletRequest request) {
		String code = request.getParameter("code");
		String oauthToken = request.getParameter("oauth_token");
		return StringUtils.isNotBlank(code) || StringUtils.isNotBlank(oauthToken);
	}

	private void handleSignOut(HttpServletRequest request, HttpServletResponse response) {
		if (SecurityContext.userSignedIn() && request.getServletPath().startsWith("/signout")) {
			connectionRepository.createConnectionRepository(SecurityContext.getCurrentUser().getId()).removeConnections("facebook");
			SecurityContext.remove();
		}
	}

	private boolean requestForSignIn(HttpServletRequest request) {
		return request.getServletPath().startsWith("/signin");
	}

}