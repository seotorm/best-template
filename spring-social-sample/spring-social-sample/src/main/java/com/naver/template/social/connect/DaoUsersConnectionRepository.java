/*
 * @(#)DaoUsersConnectionRepository.java $version 2012. 1. 31.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social.connect;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;


/**
 * 
 * @author swseo
 */
public class DaoUsersConnectionRepository implements UsersConnectionRepository {


	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	private ConnectionSignUp connectionSignUp;
	
	@Inject
	private UserConnectionDAO userConnectionDAO;

	public DaoUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
		this.connectionSignUp = connectionSignUp;
	}
	
	public void setUserConnectionDAO(UserConnectionDAO userConnectionDAO) {
		this.userConnectionDAO = userConnectionDAO;
	}

	/**
	 * @param connection
	 * @return
	 * @see org.springframework.social.connect.UsersConnectionRepository#findUserIdsWithConnection(org.springframework.social.connect.Connection)
	 */
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		ConnectionKey key = connection.getKey();
		//List<String> localUserIds = jdbcTemplate.queryForList("select userId from " + tablePrefix + "UserConnection where providerId = ? and providerUserId = ?", String.class, key.getProviderId(), key.getProviderUserId());
		List<String> localUserIds = userConnectionDAO.selectUserIds(key);
		if (localUserIds.size() == 0) {
			if (connectionSignUp != null) {
				String newUserId = connectionSignUp.execute(connection);
				createConnectionRepository(newUserId).addConnection(connection);
				return Arrays.asList(newUserId);
			}
		}
		return localUserIds;
	}

	/**
	 * @param providerId
	 * @param providerUserIds
	 * @return
	 * @see org.springframework.social.connect.UsersConnectionRepository#findUserIdsConnectedTo(java.lang.String, java.util.Set)
	 */
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		//		MapSqlParameterSource parameters = new MapSqlParameterSource();
		//		parameters.addValue("providerId", providerId);
		//		parameters.addValue("providerUserIds", providerUserIds);
		return userConnectionDAO.selectUserIds(providerId, providerUserIds);
		//		return new NamedParameterJdbcTemplate(jdbcTemplate).query("select userId from " + tablePrefix + "UserConnection where providerId = :providerId and providerUserId in (:providerUserIds)", parameters,
		//			new ResultSetExtractor<Set<String>>() {
		//				public Set<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
		//					while (rs.next()) {
		//						localUserIds.add(rs.getString("userId"));
		//					}
		//					return localUserIds;
		//				}
		//			});
	}

	/**
	 * @param userId
	 * @return
	 * @see org.springframework.social.connect.UsersConnectionRepository#createConnectionRepository(java.lang.String)
	 */
	public ConnectionRepository createConnectionRepository(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new DaoConnectionRepository(userConnectionDAO, userId, connectionFactoryLocator, textEncryptor);
	}

}
