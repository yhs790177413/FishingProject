package com.goby.fishing.common.photochoose.crop;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.goby.fishing.common.utils.helper.java.util.Mail;

/**
 * Created by hu on 15/1/27.
 */
public class CropWrapper {
	private static Uri sCameraSaveUri = null;

	public static void pickImage(Activity activity) {
		sCameraSaveUri = null;
		Crop.pickImage(activity);
	}

	public static void camera(File tmpDir, Activity activity) {
		File tmpFile = getTempFileWithTmpDir(tmpDir);
		if (tmpFile == null) {
			sCameraSaveUri = null;
			Crop.camera(activity);
		} else {
			sCameraSaveUri = Uri.fromFile(tmpFile);
			Crop.camera(activity, sCameraSaveUri);
		}
	}

	public static void pickImage(Fragment fragment) {
		sCameraSaveUri = null;
		Crop.pickImage(fragment);
	}

	public static void camera(File tmpDir, Fragment fragment) {
		File tmpFile = getTempFileWithTmpDir(tmpDir);
		if (tmpFile == null) {
			sCameraSaveUri = null;
			Crop.camera(fragment);
		} else {
			sCameraSaveUri = Uri.fromFile(tmpFile);
			Crop.camera(fragment, sCameraSaveUri);
		}
	}

	public static void pickImage(android.support.v4.app.Fragment fragment) {
		sCameraSaveUri = null;
		Crop.pickImage(fragment);
	}

	public static void camera(File tmpDir,
			android.support.v4.app.Fragment fragment) {
		File tmpFile = getTempFileWithTmpDir(tmpDir);
		if (tmpFile == null) {
			sCameraSaveUri = null;
			Crop.camera(fragment);
		} else {
			sCameraSaveUri = Uri.fromFile(tmpFile);
			Crop.camera(fragment, sCameraSaveUri);
		}
	}

	public static Uri onActivityResult(Context context, File tmpDir,
			int requestCode, int resultCode, Intent data) {
		if (requestCode == Crop.REQUEST_PICK
				&& resultCode == Activity.RESULT_OK) {
			return beginCropForUri(context, data.getData());
		} else if (requestCode == Crop.REQUEST_CAMERA
				&& resultCode == Activity.RESULT_OK) {
			if (data != null && data.getData() != null) {
				Uri uri = (Uri) Mail.getMail("photo_file");
				String sourcePath = uri.getPath();
				// String sourcePath = UriUtils.getFromMediaUri(context,
				// data.getData());
				if (!TextUtils.isEmpty(sourcePath)) {
					// 读取图片偏转角度
					int angle = BitmapUtils.getExifRotation(sourcePath);
					// 获取旋转后的图片
					Bitmap bitmap = BitmapUtils.rotaingBitmap(angle,
							BitmapUtils.decodeSampledBitmapFromFile(new File(
									sourcePath), 480, 480));
					return beginCropForBitmap(tmpDir, bitmap);
				}
			} else if (data != null && data.getExtras() != null
					&& data.getExtras().get("data") != null
					&& data.getExtras().get("data") instanceof Bitmap) {
				Bitmap sourceBitmap = (Bitmap) data.getExtras().get("data");
				// 读取图片偏转角度
				int angle = BitmapUtils.getExifRotation(
						getTempFileWithTmpDir(tmpDir), sourceBitmap);
				// 切小
				sourceBitmap = BitmapUtils.decodeSampledBitmapFromBitmap(
						sourceBitmap, 480, 480);
				// 获取旋转后的图片
				Bitmap bitmap = BitmapUtils.rotaingBitmap(angle, sourceBitmap);
				return beginCropForBitmap(tmpDir, bitmap);
			} else if (sCameraSaveUri != null) {
				String sourcePath = sCameraSaveUri.getPath();
				if (!TextUtils.isEmpty(sourcePath)) {
					// 读取图片偏转角度
					int angle = BitmapUtils.getExifRotation(sourcePath);
					// 获取旋转后的图片
					Bitmap bitmap = BitmapUtils.rotaingBitmap(angle,
							BitmapUtils.decodeSampledBitmapFromFile(new File(
									sourcePath), 480, 480));
					return beginCropForBitmap(tmpDir, bitmap);
				}
			}
		}

		sCameraSaveUri = null;
		return null;
	}

	private static File getTempFileWithTmpDir(File tmpDir) {
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}

		return new File(tmpDir, String.valueOf(System.currentTimeMillis())
				+ ".jpg");
	}

	private static Uri beginCropForUri(Context context, Uri sourceUri) {
		return Uri.fromFile(new File(UriUtils.getFromMediaUri(context,
				sourceUri)));
	}

	private static Uri beginCropForBitmap(File tmpDir, Bitmap source) {
		try {
			File tempFile = getTempFileWithTmpDir(tmpDir);
			FileOutputStream b = new FileOutputStream(tempFile);
			source.compress(Bitmap.CompressFormat.JPEG, 100, b);
			b.flush();
			b.close();

			return Uri.fromFile(tempFile);
		} catch (IOException e) {
			return null;
		}
	}
}
