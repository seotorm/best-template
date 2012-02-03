/*
 * @(#)UserConnectionDAO.java $version 2012. 1. 31.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social.connect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import com.naver.template.social.BaseSqlMapDAO;

/**
 * @author swseo
 */
@Repository
public class UserConnectionDAO extends BaseSqlMapDAO {

	/**
	 * @param key (providerId + providerUserId)
	 * @return
	 */
	public List<String> selectUserIds(ConnectionKey key) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("providerId", key.getProviderId());
		params.put("providerUserId", key.getProviderUserId());

		return queryForList("social.selectUserIdsByProviderUserId", params);
	}

	/**
	 * @param providerId
	 * @param providerUserIds
	 * @return
	 */
	public Set<String> selectUserIds(String providerId, Set<String> providerUserIds) {
		return null;
	}

	/**
	 * @param userId
	 *            TODO
	 * @return
	 */
	public List<Connection<?>> selectAllConnections(String userId) {
		return null;
	}

	/**
	 * @param userId
	 * @param providerUsers
	 * @return
	 */
	public List<Connection<?>> selectConnections(String userId, MultiValueMap<String, String> providerUsers) {
		return null;
	}

	/**
	 * @param userId
	 * @param providerId
	 * @return
	 */
	public List<Connection<?>> selectConnections(String userId, String providerId) {
		return null;
	}

	/**
	 * @param userId
	 * @param connectionKey
	 * @return
	 */
	public Connection<?> selectConnection(String userId, ConnectionKey connectionKey) {
		return null;
	}

	/**
	 * 
	 * @param userId
	 * @param providerId
	 * @return
	 */
	public List<Connection<?>> selectPrimaryConnection(String userId, String providerId) {
		return null;
	}

	/**
	 * @param userId TODO
	 * @param data
	 */
	public void insert(String userId, ConnectionData data) {
	}

	/**
	 * @param userId TODO
	 * @param data
	 */
	public void update(String userId, ConnectionData data) {
	}

	/**
	 * @param userId TODO
	 * @param providerId
	 */
	public void delete(String userId, String providerId) {
	}

	/**
	 * @param userId
	 * @param connectionKey
	 */
	public void delete(String userId, ConnectionKey connectionKey) {
	}

}
