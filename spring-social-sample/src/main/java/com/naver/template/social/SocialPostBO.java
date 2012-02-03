/*
 * @(#)SocialPostBO.java $version 2011. 11. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.ApiException;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

/**
 * Posting 예제 클래스.
 * @author swseo
 */
@Service
public class SocialPostBO {
	Logger log = LoggerFactory.getLogger(SocialPostBO.class);

	@Inject
	SimpleSocialConnectionFactory simpleSocialConnectionFactory;

	/**
	 * @param userId
	 * @param message
	 */
	public void postToTwitter(String userId, String message) {
		Twitter twitter = simpleSocialConnectionFactory.getTwitter(userId);

		try {
			twitter.timelineOperations().updateStatus(message);
		} catch (DuplicateStatusException ex) {
			//
		} catch (NotAuthorizedException ex) {
			//
		} catch (OperationNotPermittedException ex) {
			//
		} catch (ApiException ex) {
			//
		} catch (ResourceAccessException ex) {
			//
		}
	}

	public void postToFacebook(String userId) {
		Facebook facebook = simpleSocialConnectionFactory.getFacebook(userId);

		String imageUrl = "http://cfile9.uf.tistory.com/image/1771223E4E3EAE20032F8B";
		String linkUrl = "";
		String linkName = "상품명";
		String caption = "lovepin.it";
		String description = "상품설명 blah blah";
		String actions = "[{ name: 'Get the LOVEPIN App', link: 'http://lovepin.it/apps' }]";

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.set("picture", imageUrl);
		map.set("link", linkUrl);
		map.set("name", linkName);
		map.set("caption", caption);
		map.set("description", description);
		map.set("actions", actions);

		String providerUserId = simpleSocialConnectionFactory.getConnectionRepository(userId).getPrimaryConnection(Facebook.class).getKey().getProviderUserId();

		try {
			facebook.post(providerUserId, "feed", map);
		} catch (NotAuthorizedException ex) {
			//
		} catch (OperationNotPermittedException ex) {
			//
		} catch (ApiException ex) {
			//
		} catch (ResourceAccessException ex) {
			//
		}
	}

}
