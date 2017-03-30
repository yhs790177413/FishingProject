package com.goby.fishing.common.utils.helper.java.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileTool {

	public final static String DIR_TGNET = "tgact";
	public final static String DIR_IMAGE = DIR_TGNET + File.separator
			+ "images";
	public final static String DIR_VOICE = DIR_TGNET + File.separator
			+ "voices";
	public final static String DIR_FILE = DIR_TGNET + File.separator + "files";
	public final static String DIR_IMAGE_CACHE = DIR_TGNET + File.separator
			+ "cacheImage";

	public final static String IMAGE_SUFFIX = "@250w.jpg";

	private static String sdCardRootPath = null;

	public static String getImagePath() {
		if (isSDCardMounted()) {
			File file = new File(getSDCardRootPath() + File.separator
					+ DIR_IMAGE);
			if (!file.exists()) {
				file.mkdirs();
			}
			return file.getAbsolutePath();
		}
		return null;
	}

	public static String getImageCachePath() {
		if (isSDCardMounted()) {
			File file = new File(getSDCardRootPath() + File.separator
					+ DIR_IMAGE_CACHE);
			if (!file.exists()) {
				file.mkdirs();
			}

			return file.getAbsolutePath();
		}
		return null;
	}

	public synchronized static String getImagePathName() {
		String path = getImagePath();
		if (path != null) {
			return path + File.separator + System.currentTimeMillis()
					+ UniqueIntGenerator.next() + ".jpg";
		}
		return null;
	}

	public synchronized static String getImageCachePathName() {
		String path = getImageCachePath();
		if (path != null) {
			return path + File.separator + System.currentTimeMillis()
					+ UniqueIntGenerator.next() + ".jpg";
		}
		return null;
	}

	public synchronized static String getRadomImageNameJPG() {
		return System.currentTimeMillis() + "" + UniqueIntGenerator.next()
				+ ".jpg";
	}

	public static String getVoicePath() {
		if (isSDCardMounted()) {
			File file = new File(getSDCardRootPath() + File.separator
					+ DIR_VOICE);
			if (!file.exists()) {
				file.mkdir();
			}
			return file.getAbsolutePath();
		}
		return null;
	}

	public synchronized static String getVoicePathName() {
		String path = getVoicePath();
		if (path != null) {
			return path + File.separator + System.currentTimeMillis()
					+ UniqueIntGenerator.next() + ".amr";
		}
		return null;
	}

	public static String getFilePath() {
		if (isSDCardMounted()) {
			File file = new File(getSDCardRootPath() + File.separator
					+ DIR_FILE);
			if (!file.exists()) {
				file.mkdir();
			}
			return file.getAbsolutePath();
		}
		return null;
	}

	public static String getFilePath(String fileName) {
		String path = null;
		if ((path = getFilePath()) != null) {
			return path + File.separator + fileName;
		}
		return null;
	}

	public static String getCacheFilePath(Context context, String fileName) {
		String path = context.getCacheDir().getAbsolutePath();
		if (path != null) {
			return path + File.separator + fileName;
		}
		return null;
	}

	public static boolean isSDCardMounted() {
		boolean sdMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!sdMounted) {
			return false;
		}
		if (sdCardRootPath == null) {
			sdCardRootPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		}
		return sdMounted;
	}

	public static String getSDCardRootPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

}
