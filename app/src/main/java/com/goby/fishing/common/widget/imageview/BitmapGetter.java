package com.goby.fishing.common.widget.imageview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

/**
 * 
 * @ClassName: BitmapGetter 图片处理类 -- 估计用不到，到时候再算
 * @Description: TODO
 * @author lzh
 * @date 2014-10-9 上午9:38:15
 * 
 */
public class BitmapGetter {

	public static Bitmap getUnderMaxSizeImage(String pathName, int maxWidth,
			int maxHeight) {
		// BitmapFactory.Options ops = new BitmapFactory.Options();
		// ops.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(pathName, ops);
		//
		// int actWidth = ops.outWidth;
		// int actHeight = ops.outHeight;
		//
		// int scale = 1;
		// if (actWidth > actHeight) {
		// if (maxWidth < actWidth) {
		// scale = maxWidth / actWidth;
		// }
		// } else {
		// if (maxHeight < actHeight) {
		// scale = maxHeight / actHeight;
		// }
		// }
		//
		// ops.inJustDecodeBounds = false;
		// ops.inScaled = true;
		// ops.inSampleSize = scale;
		//
		// Bitmap bm = BitmapFactory.decodeFile(pathName, ops);

		Bitmap bitmap = null;
		//if (maxWidth == 1200 && maxHeight == 1200) {
			Options newOpts = new Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(pathName, newOpts);// 此时返回bm为空
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			int hh = maxHeight;// 这里设置高度为1280
			int ww = maxWidth;// 这里设置宽度为720
			int be = 1;// be=1表示不缩放
			if (w > ww || h > hh) {
				// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可

				if (w * hh > h * ww) {// 如果宽度大的话根据宽度固定大小缩放
					hh = h * ww / w;
				} else {// 如果高度高的话根据宽度固定大小缩放
					ww = w * hh / h;
				}
			}
			be = (int) Math.ceil((double) w / (double) ww);
			if (be <= 0)
				be = 1;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			newOpts.inSampleSize = be;// 设置缩放比例
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			try {
				bitmap = BitmapFactory.decodeFile(pathName, newOpts);
				// return saveBitmap2file(compressImage(bitmap));
			} catch (OutOfMemoryError e) {
				return null;
			}
		// } else if (maxWidth == 480 && maxHeight == 800) {
		// BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(pathName, opts);
		// opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		// opts.inJustDecodeBounds = false;
		// try {
		// bitmap = BitmapFactory.decodeFile(pathName, opts);
		// } catch (OutOfMemoryError err) {
		// return null;
		// }
		// }

		return bitmap;
	}

	public static Bitmap getImageFromNet(String url, String desPath,
			String desName, int maxWidth, int maxHeight) {
		downloadImageAndSave(url, desPath, desName);
		return getUnderMaxSizeImage(desPath + File.separator + desName,
				maxWidth, maxHeight);
	}

	private static void downloadImageAndSave(String url, String desPath,
			String desName) {
		File filePath = new File(desPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		File filePathName = new File(filePath.getAbsolutePath()
				+ File.separator + desName);

		InputStream input = null;
		FileOutputStream fileOutput = null;
		try {
			HttpURLConnection hURLConn = (HttpURLConnection) new URL(url)
					.openConnection();
			input = hURLConn.getInputStream();
			fileOutput = new FileOutputStream(filePathName);
			int temp = -1;
			while ((temp = input.read()) != -1) {
				fileOutput.write(temp);
			}
		} catch (Exception e) {
			Log.d("log", e.getMessage());
		} finally {
			IOStreamManager.closeInputStreams(input);
			IOStreamManager.closeOutputStreams(fileOutput);
		}
	}

	public static int computeSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
