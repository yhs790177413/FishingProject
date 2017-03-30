package com.goby.fishing.common.utils.helper.java.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesHelper {

	public static String desDecrypt( final String message)
			throws Exception {
		// DES/ECB CBC CFB OFB /PKCS5Padding NoPadding 加密/模式/填充
		Cipher cipher = Cipher.getInstance("DES");// 默认就是
		// DES/ECB/PKCS5Padding
		String key = "vry65fbg";
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(1, secretKey);
		return Base64Helper.encode(cipher.doFinal(message.getBytes("UTF-8")));
	}
}
