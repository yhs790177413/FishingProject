package com.goby.fishing.common.widget.imageview;

import java.io.ByteArrayInputStream;




import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.java.util.FileTool;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

public class ImageUtil {

	private static final String CACHDIR = FileTool.DIR_IMAGE;

	/**
	 * 获取本地图片的缩略图
	 * 
	 * @param res
	 * @param filePath
	 *            图片路径
	 * @param width
	 *            宽度 �?使用默认宽度
	 * @param height
	 *            高度 �?使用默认高度
	 * @return
	 */
	public static Bitmap getBitmapFromFile(Resources res, String filePath,
			int width, int height) {

		if (null == filePath)
			return null;

		if (0 == width)
			width = res.getDimensionPixelSize(R.dimen.default_120);
		if (0 == height)
			height = res.getDimensionPixelSize(R.dimen.default_120);

		Bitmap bitmap = getimage(filePath);

		return getBitmapThumbnail(res, bitmap, width, height);
	}

	/**
	 * 获取本地图片的缩略图
	 * 
	 * @param res
	 * @param bitmap
	 * @param newWidth
	 *            宽度 �?使用默认宽度
	 * @param newHeight
	 *            高度 �?使用默认高度
	 * @return
	 */
	public static Bitmap getBitmapThumbnail(Resources res, Bitmap bitmap,
			int newWidth, int newHeight) {

		// 获得图片的宽�?
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (0 == newWidth)
			newWidth = res.getDimensionPixelSize(R.dimen.default_180);
		if (0 == newHeight)
			newHeight = res.getDimensionPixelSize(R.dimen.default_180);

		// 计算缩放比例
		float scale;
		if (newWidth > newHeight) {
			scale = ((float) newWidth) / width;
		} else {
			scale = ((float) newHeight) / height;
		}

		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
				true);

		return newbm;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
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

	private static int computeInitialSampleSize(BitmapFactory.Options options,
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

	/**
	 * 图片切割
	 * 
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap CutBitmap(Bitmap bitmap, int x, int y, int width,
			int height) {

		try {
			return Bitmap.createBitmap(bitmap, x, y, width, height);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将本地图片转换成Drawable
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static BitmapDrawable getImageDrawable(String path)
			throws IOException {
		// 打开文件
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		Bitmap bitmap = getimage(path);// 生成位图
		BitmapDrawable bd = new BitmapDrawable(bitmap);

		return bd;
	}

	/**
	 * 获取本地图片的缩略图
	 * 
	 * @return
	 */
	public static Bitmap getThumbnailFromFile(String filePath, int width,
			int height) {

		File dst = new File(filePath);
		if (0 == width)
			width = 300;
		if (0 == height)
			height = 300;

		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;

			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(dst.getPath(), opts);
			// 计算图片缩放比例
			final int minSideLength = Math.min(width, height);
			opts.inSampleSize = computeSampleSize(opts, minSideLength, width
					* height);
			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPurgeable = true;

			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 缩略裁剪图片成正方形
	 * 
	 * @return
	 */
	public static Bitmap photoAdapteByFile(final String argPath,
			final int argWidth, final int argHeight) {
		Bitmap _from = ImageUtil.getThumbnailFromFile(argPath, argWidth,
				argHeight);
		Bitmap _to = null;
		if (_from != null) {
			int _width = _from.getWidth();
			int _height = _from.getHeight();
			if (_width > _height) {
				_to = Bitmap.createBitmap(_from, (_width - _height) / 2, 0,
						_height, _height);
			} else {
				_to = Bitmap.createBitmap(_from, 0, (_height - _width) / 2,
						_width, _width);
			}
			_to = Bitmap.createScaledBitmap(_to, argWidth, argHeight, true);
		}

		// Bitmap _to = null;
		// if(argWidth >argHeight){
		// _to =
		// getThumbnailFromFile(argPath,(argWidth-argHeight)/4,(argWidth-argHeight)/4);
		// }else{
		// _to = getThumbnailFromFile(argPath,(argHeight -
		// argWidth)/4,(argHeight - argWidth)/4);
		// }
		return _to;
	}

	/**
	 * 缩略裁剪图片成正方形
	 * 
	 * @return
	 */
	public static Bitmap photoAdapteByBitmap(Bitmap _from, final int argWidth,
			final int argHeight) {

		Bitmap _to = null;
		if (_from != null) {
			int _width = _from.getWidth();
			int _height = _from.getHeight();
			if (_width > _height) {
				_to = Bitmap.createBitmap(_from, (_width - _height) / 2, 0,
						_height, _height);
			} else {
				_to = Bitmap.createBitmap(_from, 0, (_height - _width) / 2,
						_width, _width);
			}
			_to = Bitmap.createScaledBitmap(_to, argWidth, argHeight, true);
		}

		// Bitmap _to = null;
		// if(argWidth >argHeight){
		// _to =
		// getThumbnailFromFile(argPath,(argWidth-argHeight)/4,(argWidth-argHeight)/4);
		// }else{
		// _to = getThumbnailFromFile(argPath,(argHeight -
		// argWidth)/4,(argHeight - argWidth)/4);
		// }
		return _to;
	}

	public static Bitmap getThumbnailFromFileOrder(String filePath, int width,
			int height) {

		File dst = new File(filePath);

		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;

			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(dst.getPath(), opts);
			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPurgeable = true;
			opts.outWidth = width;
			opts.outHeight = height;
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 读取本地资源的图�?
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 获取本地图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getimage(String path) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// �?��读入图片，此时把options.inJustDecodeBounds 设回true�?
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		float hh = 800f;// 这里设置高度�?00f
		float ww = 480f;// 这里设置宽度�?80f
		// 缩放比�?由于是固定比例缩放，只用高或者宽其中�?��数据进行计算即可
		int be = 1;// be=1表示不缩�?
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压�?
	}

	public static Bitmap compressImage(Bitmap image) {

		if (null == image)
			return null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这�?00表示不压缩，把压缩后的数据存放到baos�?
		int options = 100;
		while (baos.toByteArray().length / 1024 > 60) { // 循环判断如果压缩后图片是否大�?00kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
			options -= 10;// 每次都减�?0
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream�?
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static String compress(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		int tarHeight = 800;

		int height = options.outHeight;

		int scale = 1;
		if (height > tarHeight) {
			scale = (int) Math.ceil((double) height / (double) tarHeight);
		}

		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(path, options);

		long name = System.currentTimeMillis();
		String dir = getDirectory();
		if (null == dir)
			return null;
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(dir + "/" + name + ".jpg");
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			Log.w("ImageFileCache", "FileNotFoundException");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			Log.w("ImageFileCache", "IOException");
			return null;
		}
		return file.getAbsolutePath();
	}

	public static Bitmap compressForBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		int tarHeight = 800;

		int height = options.outHeight;

		int scale = 1;
		if (height > tarHeight) {
			scale = (int) Math.ceil((double) height / (double) tarHeight);
		}

		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(path, options);

		return bitmap;
	}

	/**
	 * 获得缓存目录 *
	 */
	private static String getDirectory() {
		String path = getSDPath();
		if (null == path) {
			return null;
		}
		String dir = path + "/" + CACHDIR;
		return dir;
	}

	/**
	 * 取SD卡路�?*
	 */
	private static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存�?
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory(); // 获取根目�?
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return null;
		}
	}

	public static void revealImageOrientation(String pathName) {
		if (pathName == null) {
			return;
		}
		Bitmap bitmap = compressForBitmap(pathName);
		if (bitmap == null) {
			return;
		}
		File file = new File(pathName);
		if (!file.exists()) {
			return;
		}
		ExifInterface exif;
		try {
			exif = new ExifInterface(pathName);
		} catch (IOException e) {
			return;
		}
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
		int rotation = 0;
		int width = 0;
		int height = 0;
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotation = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotation = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotation = 90;
			break;
		case ExifInterface.ORIENTATION_NORMAL:
			return;
		}

		Matrix matrix = new Matrix();
		matrix.preRotate(rotation);

		Bitmap bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);

		try {
			OutputStream outStream = new FileOutputStream(file);
			bitmapRotated.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
		} catch (FileNotFoundException e) {
			Log.w("ImageFileCache", "FileNotFoundException");
			return;
		}
		bitmap.recycle();
	}

	/**
	 * 将图片转成二进制
	 * 
	 * @param bitmap
	 * @return
	 */
	public byte[] getBitmapByte(final Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}
