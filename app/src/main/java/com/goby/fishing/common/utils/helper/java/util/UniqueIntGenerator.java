package com.goby.fishing.common.utils.helper.java.util;

public class UniqueIntGenerator {
	private static int i = 0;

	public static int next() {
		if (i >= Integer.MAX_VALUE) {
			i = 0;
		}
		return i++;
	}
}
