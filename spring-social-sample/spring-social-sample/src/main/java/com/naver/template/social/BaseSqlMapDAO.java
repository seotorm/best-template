/*
 * @(#)BaseSqlMapDAO.java $version 2011. 3. 21.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author 서상우
 */
public class BaseSqlMapDAO {
	private Logger log = LoggerFactory.getLogger(BaseSqlMapDAO.class);

	@Inject
	private SqlMapClientTemplate sqlMapClientTemplate;

	private void loggingResult(final Object result) {
		if (log.isDebugEnabled()) {
			log.debug("Query result: {}", result);
		}
	}

	private void loggingParams(String statementName, final Object parameterObject) {
		if (log.isDebugEnabled()) {
			loggingParams(statementName);
			log.debug("Input params: {}", parameterObject);
		}
	}

	private void loggingParams(String statementName) {
		if (log.isDebugEnabled()) {
			log.debug("Statement   : {}", statementName);
		}
	}

	protected Object queryForObject(final String statementName, final Object parameterObject) {
		loggingParams(statementName, parameterObject);
		Object result = getSqlMapClientTemplate().queryForObject(statementName, parameterObject);
		loggingResult(result);
		return result;
	}

	private SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	protected Object queryForObject(final String statementName) {
		loggingParams(statementName);
		Object result = getSqlMapClientTemplate().queryForObject(statementName);
		loggingResult(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> queryForList(final String statementName, final Object parameterObject) {
		loggingParams(statementName, parameterObject);
		List<T> result = (List<T>)getSqlMapClientTemplate().queryForList(statementName, parameterObject);
		loggingResult(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> queryForList(final String statementName) {
		loggingParams(statementName);
		List<T> result = (List<T>)getSqlMapClientTemplate().queryForList(statementName);
		loggingResult(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> queryForList(final String statementName, int skipResults, int maxResults) {
		loggingParams(statementName);
		List<T> result = (List<T>)getSqlMapClientTemplate().queryForList(statementName, skipResults, maxResults);
		loggingResult(result);
		return result;
	}

	protected int delete(String statementName, Object parameterObject) {
		loggingParams(statementName, parameterObject);
		return getSqlMapClientTemplate().delete(statementName, parameterObject);
	}

	protected int delete(String queryId) {
		return getSqlMapClientTemplate().delete(queryId);
	}

	protected Object insert(String statementName, final Object parameterObject) {
		loggingParams(statementName, parameterObject);
		return getSqlMapClientTemplate().insert(statementName, parameterObject);
	}

	protected Object insert(String queryId) {
		return getSqlMapClientTemplate().insert(queryId);
	}

	protected int update(String statementName, Object parameterObject) {
		loggingParams(statementName, parameterObject);
		return getSqlMapClientTemplate().update(statementName, parameterObject);
	}
}
