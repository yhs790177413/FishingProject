package com.goby.fishing.common.utils.helper.android.util;

import android.util.Base64;

/**
 * Base64加密工具
 * 
 * @author
 */
public class Base64Helper {

	/**
	 * 对内容进行base64加密
	 * 
	 * @param text
	 *            内容
	 * @return 加密结果
	 */
	public final static String base64(String text) {
		return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
	}

	public final static String base64(byte[] text) {
		return Base64.encodeToString(text, Base64.DEFAULT);
	}
}
