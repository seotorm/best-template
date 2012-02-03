/*
 * @(#)UserConnectionDAOTest.java $version 2012. 2. 1.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.social.connect;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.social.connect.ConnectionKey;

import com.naver.template.social.IntegrationTestSupporter;
import com.naver.template.social.UserConnectionDAO;

/**
 * @author swseo
 */
public class UserConnectionDAOTest extends IntegrationTestSupporter {

	@Inject
	UserConnectionDAO userConnectionDAO;

	@Before
	public void setUp() {
		//
	}
	
	@Test
	public void testSelectUserIds() {
		ConnectionKey key = new ConnectionKey("facebook", "1111");
		List<String> selectUserIds = userConnectionDAO.selectUserIds(key);
		
		System.out.println(selectUserIds);
	}

}
