/*
 * @(#)IntegrationTestSupporter.java $version 2012. 2. 1.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author swseo
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"/applicationContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class IntegrationTestSupporter {
	private SimpleJdbcTemplate jdbcTemplate;
	
	@Inject
	public void createTemplate(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	public SimpleJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
}
