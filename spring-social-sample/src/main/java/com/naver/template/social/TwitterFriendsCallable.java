/*
 * @(#)TwitterFriends.java $version 2011. 11. 24.
 *
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.ApiException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

/**
 * @author swseo
 */
public class TwitterFriendsCallable implements Callable<List<TwitterProfile>> {
	private Logger log = LoggerFactory.getLogger(TwitterFriendsCallable.class);

	final static int RETRY_COUNT = 3;

	private Twitter twitter;

	private long[] ids;

	/**
	 * 
	 */
	public TwitterFriendsCallable(Twitter twitter, long[] ids) {
		this.twitter = twitter;
		this.ids = ids;
	}

	/**
	 * @return
	 * @throws Exception
	 * @see java.util.concurrent.Callable#call()
	 */
	public List<TwitterProfile> call() throws Exception {
		return getTwitterFriends();
	}

	private List<TwitterProfile> getTwitterFriends() {
		try {
			return twitter.userOperations().getUsers(ids);
		} catch (NotAuthorizedException ex) {
			log.error("{}", ex);
		} catch (ApiException ex) {
			log.error("{}", ex);
		}
		return null;
	}

}
