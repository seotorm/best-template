/*
 * @(#)ListUtil.java $version 2011. 11. 29.
 *
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.naver.template.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author swseo
 */
public class ChunkUtils {

	public static long[] toPrimitiveArray(List<Long> list) {
		Long[] array = list.toArray(new Long[0]);
		return ArrayUtils.toPrimitive(array);
	}

	public static <T> List<List<T>> chunk(List<T> list, int chunkSize) {
		List<List<T>> chunkedList = new ArrayList<List<T>>();
		int start = 0;
		while (start < list.size()) {
			int end = Math.min(chunkSize + start, list.size());
			chunkedList.add(list.subList(start, end));
			start = end;
		}
		return chunkedList;
	}

}
