/*
 * @(#)TwitterBO.java $version 2012. 2. 7.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.ApiException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import com.naver.template.common.ChunkUtils;

/**
 * @author swseo
 */
@Service
public class TwitterBO {
	Logger log = LoggerFactory.getLogger(TwitterBO.class);

	@Inject
	SimpleSocialConnectionFactory simpleSocialConnectionFactory;

	/**
	 * 병렬호출기.
	 */
	ExecutorService pool = Executors.newCachedThreadPool();

	/**
	 * @param userId 
	 * @return
	 * @throws STrendException if NotAuthorizedException or ApiException occur.
	 */
	private CursoredList<Long> getFriendIdList(String userId) {
		Twitter twitter = getTwitterConnection(userId);
		try {
			return twitter.friendOperations().getFriendIds();
		} catch (NotAuthorizedException ex) {
			log.error("", ex);
		} catch (ApiException ex) {
			log.error("", ex);
		}
		return null;
	}

	/**
	 * @param userId
	 * @return
	 */
	private Twitter getTwitterConnection(String userId) {
		Twitter twitter = simpleSocialConnectionFactory.getTwitter(userId);
		return twitter;
	}

	private List<TwitterProfile> getTwitterProfiles(String userId, long[] ids) {
		Twitter twitter = getTwitterConnection(userId);
		try {
			return twitter.userOperations().getUsers(ids);
		} catch (NotAuthorizedException ex) {
			log.error("", ex);
		} catch (ApiException ex) {
			log.error("", ex);
		}
		return null;
	}

	public List<TwitterProfile> getFriendList(String userId) {
		CursoredList<Long> friendIdList = getFriendIdList(userId);
		List<List<Long>> chunkedList = ChunkUtils.chunk(friendIdList, 10);

		List<TwitterProfile> twitterFriends = new ArrayList<TwitterProfile>();
		for (List<Long> eachList : chunkedList) {
			Long[] array = eachList.toArray(new Long[0]);
			twitterFriends.addAll(getTwitterProfiles(userId, ArrayUtils.toPrimitive(array)));
		}
		
		return twitterFriends;
	}

	public List<TwitterProfile> getFriendListParallel(String userId) {
		// get chunked id list
		CursoredList<Long> friendIdList = getFriendIdList(userId);
		List<List<Long>> chunkedList = ChunkUtils.chunk(friendIdList, 100);

		Twitter twitter = getTwitterConnection(userId);

		List<Future<List<TwitterProfile>>> futureList = new ArrayList<Future<List<TwitterProfile>>>();

		// call parallel
		for (List<Long> eachList : chunkedList) {
			Long[] array = eachList.toArray(new Long[0]);
			long[] ids = ArrayUtils.toPrimitive(array);
			futureList.add(pool.submit(new TwitterFriendsCallable(twitter, ids)));
		}

		// async get result and merge
		List<TwitterProfile> twitterFriends = new ArrayList<TwitterProfile>();
		for (Future<List<TwitterProfile>> future : futureList) {
			List<TwitterProfile> resultList = null;
			try {
				resultList = future.get(3000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				//
			} catch (ExecutionException e) {
				//
			} catch (TimeoutException e) {
				//
			}
			
			if (CollectionUtils.isNotEmpty(resultList)) {
				twitterFriends.addAll(resultList);
			}
		}

		return twitterFriends;
	}

}
