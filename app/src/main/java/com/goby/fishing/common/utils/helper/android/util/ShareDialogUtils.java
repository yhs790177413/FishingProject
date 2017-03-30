package com.goby.fishing.common.utils.helper.android.util;

import java.util.ArrayList;
import java.util.List;

import com.goby.fishing.R;
import com.goby.fishing.common.photochoose.crop.BitmapUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ShareDialogUtils extends Dialog implements View.OnClickListener {
	private Activity mContext;
	private TextView tv_weixin_circle, tv_qzone, tv_weixin, tv_qq, tv_weibo;
	private Handler uiHandler;
	private List<SHARE_MEDIA> mPlatforms;
	private UMShareListener umShareListener;
	private Message message;
	public static int WEIXIN_CIRCLE = 0;
	public static int QZONE = 1;
	public static int WEIXIN = 2;
	public static int QQ = 3;
	public static int SINA = 4;

	public ShareDialogUtils(Activity mContext, int theme, Handler uiHandler,
			boolean isSms) {
		super(mContext, theme);
		this.mContext = mContext;
		// 加载布局文件
		this.setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.dialog_share, null));

		mPlatforms = new ArrayList<SHARE_MEDIA>();
		initPlatform();

		this.uiHandler = uiHandler;
		message = new Message();

		tv_weixin_circle = (TextView) findViewById(R.id.tv_weixin_circle);
		tv_qzone = (TextView) findViewById(R.id.tv_qzone);
		tv_weixin = (TextView) findViewById(R.id.tv_weixin);
		tv_qq = (TextView) findViewById(R.id.tv_qq);
		tv_weibo = (TextView) findViewById(R.id.tv_weibo);

		tv_weixin_circle.setOnClickListener(this);
		tv_qzone.setOnClickListener(this);
		tv_weixin.setOnClickListener(this);
		tv_qq.setOnClickListener(this);
		tv_weibo.setOnClickListener(this);

		umShareListener = new UMShareListener() {
			@Override
			public void onResult(SHARE_MEDIA platform) {
			}

			@Override
			public void onError(SHARE_MEDIA platform, Throwable t) {
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}
		};
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_weixin_circle:

			message.what = WEIXIN_CIRCLE;
			uiHandler.sendMessage(message);
			break;
		case R.id.tv_qzone:
			message.what = QZONE;
			uiHandler.sendMessage(message);
			break;
		case R.id.tv_weixin:
			message.what = WEIXIN;
			uiHandler.sendMessage(message);
			break;
		case R.id.tv_qq:
			message.what = QQ;
			uiHandler.sendMessage(message);
			break;
		case R.id.tv_weibo:
			message.what = SINA;
			uiHandler.sendMessage(message);
			break;
		default:
			break;
		}
	}

	/**
	 * 开始分享操作
	 * 
	 * @param content
	 * @param bitmap
	 * @param platform
	 */
	public void startShare(String content, Bitmap bitmap, int platform) {
		if (platform == 0 || platform == 2) {
			new ShareAction(mContext)
					.setPlatform(mPlatforms.get(platform))
					.withMedia(
							new UMImage(mContext, BitmapUtils
									.bitmap2Bytes(bitmap))).share();
		} else {
			new ShareAction(mContext).setPlatform(mPlatforms.get(platform))
					.withMedia(new UMImage(mContext, bitmap)).share();
		}
	}

	/**
	 * 开始分享操作
	 * 
	 * @param content
	 * @param bitmap
	 * @param platform
	 */
	public void startShare(String title, String content, Bitmap bitmap,
			int platform, String Imageurl, String url) {
		if (!TextUtils.isEmpty(Imageurl)) {
			new ShareAction(mContext).setPlatform(mPlatforms.get(platform))
					.withMedia(new UMImage(mContext, Imageurl))
					.withText(content).withTitle(title).withTargetUrl(url)
					.setCallback(umShareListener).share();
		} else {
			new ShareAction(mContext).setPlatform(mPlatforms.get(platform))
					.withMedia(new UMImage(mContext, R.drawable.app_icon))
					.withText(content).withTitle(title).withTargetUrl(url)
					.setCallback(umShareListener).share();
		}
	}

	/**
	 * 开始分享操作
	 * 
	 * @param content
	 * @param bitmap
	 * @param
	 */
	// public void startShare(String content, String url, String location,
	// int position) {
	// // 设置分享内容
	// mController.setShareContent(content);
	// // 设置分享图片
	// if (!TextUtils.isEmpty(url)) {
	// mController.setShareMedia(new UMImage(mContext, url));
	// } else {
	// mController
	// .setShareMedia(new UMImage(mContext, R.drawable.app_icon));
	// }
	//
	// mController.getConfig().closeToast();// android关掉toast
	// // mController.directShare(mContext, mPlatforms.get(position), null);
	// }

	/**
	 * 初始化平台map
	 */
	public void initPlatform() {
		mPlatforms.add(SHARE_MEDIA.WEIXIN_CIRCLE);
		mPlatforms.add(SHARE_MEDIA.QZONE);
		mPlatforms.add(SHARE_MEDIA.WEIXIN);
		mPlatforms.add(SHARE_MEDIA.QQ);
		mPlatforms.add(SHARE_MEDIA.SINA);
	}
}
