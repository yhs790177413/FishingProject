package com.goby.fishing.common.widget.imageview;

import java.io.File;

import java.util.WeakHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.goby.fishing.application.Constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageCircleView extends ImageView {

	private static WeakHashMap<String, Bitmap> weakImageMap = new WeakHashMap<String, Bitmap>();
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 3,
			20, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	private Context context = null;
	private String rootPath = null;

	private Handler hRedraw = null;
	private String currentUrl = null;
	private String currentUrlKey = null;
	private int maxWidth = 700;
	private int maxHeight = 1000;
	private File file;
	private boolean mQuality;

	public ImageCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + Constant.PATH_ICON;
	}

	public ImageCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + Constant.PATH_ICON;
	}

	public ImageCircleView(Context context) {
		super(context);
		this.context = context;
		rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + Constant.PATH_ICON;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		maxWidth = right - left;
		maxHeight = bottom - top;
	}

	public void setImageRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setMaxSize(int maxWidth, int maxHeight) {
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
	}

	public void setImageURL(String url, int defImgRes, boolean quality) {
		mQuality = quality;
		if (url != null && url.equals(currentUrl)) {
			return;
		}
		setBackgroundDrawable(context.getResources().getDrawable(defImgRes));
		if (rootPath == null || url == null) {
			return;
		}
		createCurrentUrlKey(url);
		setImageURL();
	}

	public void setImageURL(String url, String defImgPath,boolean quality) {
		mQuality = quality;
		if (url != null && url.equals(currentUrl)) {
			return;
		}

		Bitmap bitmap = BitmapFactory.decodeFile(defImgPath);
		setBackgroundDrawable(new BitmapDrawable(bitmap));
		if (rootPath == null || url == null) {
			return;
		}
		createCurrentUrlKey(url);
		setImageURL();
	}

	private void setImageURL() {
		if (hRedraw == null) {
			hRedraw = new Handler(new Handler.Callback() {

				@Override
				public boolean handleMessage(Message msg) {
					if (msg.what == 1 && msg.obj != null) {
						Bitmap bitmap = (Bitmap) msg.obj;
						setBackgroundDrawable(new BitmapDrawable(bitmap));
					}
					return false;
				}
			});
		}
		excuteURL();
	}

	private void excuteURL() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				Bitmap bm = getImage();
				if (bm != null) {
					Message msg = Message.obtain();
					msg.what = 1;
					msg.obj = bm;
					hRedraw.sendMessage(msg);
				}
			}
		});
	}

	private void createCurrentUrlKey(String url) {
		currentUrl = url;
		currentUrlKey = MD5.encode(url);
	}

	private Bitmap getImage() {
		Bitmap bm = getImageFromMemory();
		if (bm != null) {
			return bm;
		}
		bm = getImageFromExternalStorage();
		if (bm != null) {
			return bm;
		}
		bm = getImageFromNet();
		if (bm != null) {
			return bm;
		}
		return null;
	}

	private Bitmap getImageFromMemory() {
		return weakImageMap.get(currentUrlKey);
	}

	private Bitmap getImageFromExternalStorage() {
		Bitmap bm = null;
		if (rootPath == null || currentUrlKey == null) {
			return null;
		}
		try {
			if (mQuality) {
				bm = BitmapGetter.getUnderMaxSizeImage(rootPath
						+ File.separator + currentUrlKey, 1200, 1200);
			} else {
				bm = BitmapGetter.getUnderMaxSizeImage(rootPath
						+ File.separator + currentUrlKey, 480, 800);
			}
			weakImageMap.put(currentUrlKey, bm);
			return bm;
		} catch (Exception e) {
			return null;
		}
	}

	private Bitmap getImageFromNet() {
		if (rootPath == null || currentUrlKey == null || currentUrl == null) {
			return null;
		}
		try {
			Bitmap bm = BitmapGetter.getImageFromNet(currentUrl, rootPath,
					currentUrlKey, maxWidth, maxHeight);
			weakImageMap.put(currentUrlKey, bm);
			return bm;
		} catch (Exception e) {
			return null;
		}
	}
}