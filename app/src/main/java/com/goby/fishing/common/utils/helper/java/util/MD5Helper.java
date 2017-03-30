package com.goby.fishing.common.utils.helper.java.util;

import java.security.MessageDigest;

/**
 * MD5加密工具
 * 
 * @author
 */
public class MD5Helper {

	/**
	 * 对字符串进行32位MD5加密
	 * 
	 * @param text
	 *            需要加密的文本
	 * @return 通过32位MD5加密的字符串,如果加密失败返回来源文本
	 */
	public final static String md5_32(String text) {
		String md5String = null;
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(text.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (messageDigest != null) {
			byte[] byteArray = messageDigest.digest();
			int byteArray_length = byteArray.length;
			StringBuilder md5Builder = new StringBuilder();
			for (int i = 0; i < byteArray_length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
					md5Builder.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				else
					md5Builder.append(Integer.toHexString(0xFF & byteArray[i]));
			}
			md5String = md5Builder.toString();
		} else {
			return text;
		}
		return md5String;
	}

	/**
	 * 对字符串进行16位MD5加密
	 * 
	 * @param text
	 *            需要加密的文本
	 * @return 通过16位MD5加密的字符串,如果加密失败返回来源文本
	 */
	public final static String md5_16(String text) {
		String md5_32 = md5_32(text);
		String md5_16 = null;
		if (md5_32 != text) {
			md5_16 = md5_32.substring(8, 24);
		} else {
			return text;
		}
		return md5_16;
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}