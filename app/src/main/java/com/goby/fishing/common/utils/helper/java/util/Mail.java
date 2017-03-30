package com.goby.fishing.common.utils.helper.java.util;

import java.util.HashMap;

public class Mail {

	private static HashMap<String, Object> mail = new HashMap<String, Object>();

	public static void putMail(String key, Object obj) {
		mail.put(key, obj);
	}

	public static Object getMail(String key) {
		Object obj = mail.get(key);
		mail.remove(key);
		return obj;
	}

	public static Object getMail(String key, Object defaultObj) {
		Object obj = null;
		if ((obj = getMail(key)) == null) {
			return defaultObj;
		} else {
			return obj;
		}
	}

	public static boolean getBooleanMail(String key, boolean defaultBool) {
		Object obj = null;
		if ((obj = getMail(key)) != null) {
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			} else {
				return defaultBool;
			}
		} else {
			return defaultBool;
		}
	}

}
