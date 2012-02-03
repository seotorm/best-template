/*
 * @(#)SimpleConnectionFactory.java $version 2012. 2. 1.
 *
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

/**
 * Social Connection을 가져오는 클래스. 
 * 실제 서비스코드에서 이 클래스만 Inject 하여 사용하면 된다.
 * 
 * @author swseo
 */
@Service
public class SimpleSocialConnectionFactory {

	@Inject
	UsersConnectionRepository usersConnectionRepository;

	/**
	 * @param userNo
	 * @return
	 * @throws STrendException if NotConnectedException occured.
	 */
	public Facebook getFacebook(String userNo) {
		return getConnection(Facebook.class, userNo).getApi();
	}

	/**
	 * @param userNo
	 * @return
	 * @throws STrendException if NotConnectedException occured.
	 */
	public Twitter getTwitter(String userNo) {
		return getConnection(Twitter.class, userNo).getApi();
	}

	/**
	 * @param apiType
	 * @param userNo
	 * @return
	 * @throws STrendException if NotConnectedException occured.
	 */
	private <A> Connection<A> getConnection(Class<A> apiType, String userNo) {
		ConnectionRepository connectionRepository = getConnectionRepository(userNo);
		return connectionRepository.getPrimaryConnection(apiType);

	}

	public ConnectionRepository getConnectionRepository(String userNo) {
		return usersConnectionRepository.createConnectionRepository(userNo);
	}
}
