package com.goby.fishing.common.photochoose.adapter;

/**
 * ImagePagerAdapter.java
 * ImageChooser
 * <p/>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.fishing.R;
import com.goby.fishing.common.photochoose.ImageBrowseActivity;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.photochoose.ImageUrlUtils;
import com.goby.fishing.common.utils.helper.android.app.RoundImageView;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 查看大图的ViewPager适配器
 * 
 * @author likebamboo
 */
public class ImagePagerAdapter extends PagerAdapter {
	/**
	 * 数据源
	 */
	private List<String> mDatas = new ArrayList<String>();

	/**
	 * UIL的ImageLoader
	 */
	private ImageLoader mImageLoader = ImageLoader.getInstance();

	private ImageLoaderWrapper.DisplayConfig mConfig = new ImageLoaderWrapper.DisplayConfig.Builder()
			.build();

	private String mUserName, mUserHeader, mFishType, mFishTips, mFishArea,
			mFishTime, mFishComment;

	private ShareDialogUtils dialog_share;

	private boolean mIsShow = false;

	private UIHandler uiHandler;

	private Activity mActivity;

	private SharedPreferenceUtil sharedPreferenceUtil;

	public ImagePagerAdapter(Activity activity, ArrayList<String> dataList,
			String userHeader, String userName, String fishType,
			String fishTips, String fishArea, String fishTime,
			String fishComment, boolean isShow,
			ImageBrowseActivity.LaunchEnum launchEnum) {
		mDatas = dataList;
		mActivity = activity;
		mUserName = userName;
		mUserHeader = userHeader;
		mFishType = fishType;
		mFishTips = fishTips;
		mFishArea = fishArea;
		mFishTime = fishTime;
		mFishComment = fishComment;
		mIsShow = isShow;
		mConfig.stubImageRes = R.drawable.pic_thumb;
		mConfig.loadFailImageRes = R.drawable.icon_pic_errow;
		sharedPreferenceUtil = new SharedPreferenceUtil(mActivity);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public View instantiateItem(final ViewGroup container, int position) {
		String imgPath = (String) getItem(position);
		// 格式化一下路径
		final String transFormPath = ImageUrlUtils.getDisplayUrl(imgPath);
		if (ImageUrlUtils.isHttpPath(transFormPath)) {
			return handleHttpImage(transFormPath, container);
		} else {
			return handleLocalImage(imgPath, transFormPath, container);
		}

	}

	private View handleLocalImage(final String imgPath,
			final String transFormPath, final ViewGroup container) {
		final RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater
				.from(container.getContext()).inflate(
						R.layout.item_viewpager_photoview, null);
		final PhotoView photoView = (PhotoView) relativeLayout
				.findViewById(R.id.pv_big_image);

		photoView.setBackgroundColor(container.getContext().getResources()
				.getColor(android.R.color.black));
		ImageUtils.getInstance().loadImage(mActivity, transFormPath,
				R.drawable.loadding_icon, photoView);

		photoView
				.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
					@Override
					public void onPhotoTap(View view, float x, float y) {
						mActivity.finish();
					}

					@Override
					public void onOutsidePhotoTap() {
						// TODO Auto-generated method stub

					}
				});
		// Now just add PhotoView to ViewPager and return it
		container.addView(relativeLayout, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		return relativeLayout;
	}

	private View handleHttpImage(final String transFormPath,
			final ViewGroup container) {
		final RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater
				.from(container.getContext()).inflate(R.layout.item_viewpager,
						null);
		final RelativeLayout smallImageLayout = (RelativeLayout) relativeLayout
				.findViewById(R.id.rv_small_image_layout);
		final ImageView smallImage = (ImageView) relativeLayout
				.findViewById(R.id.iv_samll_image);
		final PhotoView photoView = (PhotoView) relativeLayout
				.findViewById(R.id.pv_big_image);
		final RelativeLayout ll_shuiyin = (RelativeLayout) relativeLayout
				.findViewById(R.id.shuiyin_layout);
		final RoundImageView civ_userHeader = (RoundImageView) relativeLayout
				.findViewById(R.id.user_header);
		final TextView tv_userName = (TextView) relativeLayout
				.findViewById(R.id.username_text);
		final TextView tv_fishComment = (TextView) relativeLayout
				.findViewById(R.id.comment_text);
		final TextView tv_fishType = (TextView) relativeLayout
				.findViewById(R.id.fish_type);
		final View v_line = (View) relativeLayout.findViewById(R.id.line_view);
		final TextView tv_fishTips = (TextView) relativeLayout
				.findViewById(R.id.fish_tips);
		final TextView tv_fishArea = (TextView) relativeLayout
				.findViewById(R.id.fish_area);
		final TextView tv_fishTime = (TextView) relativeLayout
				.findViewById(R.id.fish_time);

		// 大图的listener
		final ImageLoadingListener bigLoadingListener = new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				loadFailed(photoView, smallImageLayout);
			}

			@SuppressLint("NewApi")
			@Override
			public void onLoadingComplete(String imageUri, View view,
					final Bitmap loadedImage) {
				// mBitmap = loadedImage;
				smallImageLayout.setVisibility(View.GONE);
				photoView.setVisibility(View.VISIBLE);
				if (mIsShow) {
					ll_shuiyin.setVisibility(View.VISIBLE);

					MarginLayoutParams margin = new MarginLayoutParams(
							ll_shuiyin.getLayoutParams());
					margin.setMargins(0,
							ImageBrowseActivity.screenHeight * 55 / 100, 0, 0);
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							margin);
					ll_shuiyin.setLayoutParams(layoutParams);
					RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) ll_shuiyin.getLayoutParams(); //取控件textView当前的布局参数  
					linearParams.height = ImageBrowseActivity.screenHeight * 45 / 100;;// 控件的高强制设成20  
					ll_shuiyin.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>  
				}
				photoView.setImageBitmap(loadedImage);
				if (ImageBrowseActivity.bitmapHash != null
						&& ImageBrowseActivity.bitmapHash.size() > 0) {
					if (!ImageBrowseActivity.bitmapHash.containsKey(imageUri)) {
						ImageBrowseActivity.bitmapHash.put(imageUri,
								loadedImage);
					}
				} else {
					ImageBrowseActivity.bitmapHash.put(imageUri, loadedImage);
				}
				ImageLoaderWrapper.getDefault().displayImage(mUserHeader,
						civ_userHeader);
				tv_userName.setText(mUserName);
				if (!TextUtils.isEmpty(mFishComment)) {
					
						try {
							tv_fishComment.setText(EmojiUtil.handlerEmojiText(
									mFishComment, mActivity));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
				} else {
					tv_fishComment.setVisibility(View.GONE);
					v_line.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(mFishType)) {
					tv_fishType.setText(mFishType);
				} else {
					tv_fishType.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(mFishTips)) {
					tv_fishTips.setText(mFishTips);
				} else {
					tv_fishTips.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(mFishArea)) {
					tv_fishArea.setText(mFishArea);
				} else {
					tv_fishArea.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(mFishTime)) {
					tv_fishTime.setText(mFishTime);
				} else {
					tv_fishTime.setVisibility(View.GONE);
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		};
		ImageLoaderWrapper.getDefault().displayImage(
				ImageUrlUtils.getSmallImageUrl(transFormPath), smallImage);
		ImageLoaderWrapper.getDefault()
				.loadImage(ImageUrlUtils.getBigImageUrl(transFormPath),
						bigLoadingListener);
		photoView
				.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
					@Override
					public void onPhotoTap(View view, float x, float y) {
						mActivity.finish();
					}

					@Override
					public void onOutsidePhotoTap() {
						// TODO Auto-generated method stub

					}
				});
		relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
		photoView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// 弹出分享
				if (mIsShow) {
					uiHandler = new UIHandler();
					dialog_share = new ShareDialogUtils(mActivity,
							R.style.dialog, uiHandler, true);
					dialog_share.setCanceledOnTouchOutside(true);
					dialog_share.show();
				}
				return false;
			}
		});
		// Now just add PhotoView to ViewPager and return it
		container.addView(relativeLayout, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		return relativeLayout;
	}

	private void loadFailed(ImageView big, View small) {
		small.setVisibility(View.GONE);
		big.setVisibility(View.VISIBLE);
		try {
			big.setImageResource(R.drawable.icon_pic_errow);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public Object getItem(int position) {
		if (position < mDatas.size()) {
			return mDatas.get(position);
		} else {
			return null;
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public class UIHandler extends Handler {

		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			dialog_share.initPlatform();
			String mTitle = null;
			String shareUrl = null;
			switch (msg.what) {
			case 0:
				dialog_share.dismiss();
				selectSharePaltform(mTitle + shareUrl,
						takeScreenShot(mActivity), 0);
				break;
			case 1:
				dialog_share.dismiss();
				selectSharePaltform(mTitle + shareUrl,
						takeScreenShot(mActivity), 1);
				break;
			case 2:
				dialog_share.dismiss();
				selectSharePaltform(mTitle + shareUrl,
						takeScreenShot(mActivity), 2);
				break;
			case 3:
				dialog_share.dismiss();
				selectSharePaltform(mTitle + shareUrl,
						takeScreenShot(mActivity), 3);
				break;
			case 4:
				dialog_share.dismiss();
				selectSharePaltform(mTitle + shareUrl,
						takeScreenShot(mActivity), 4);
				break;
			}

		}
	}

	/**
	 * 分享平台的选择
	 * 
	 * @param position
	 */
	public void selectSharePaltform(String content, Bitmap bitmap, int position) {
		dialog_share.startShare(content, bitmap, position);
	}

	// 获取指定Activity的截屏，保存到png文件
	private Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 去掉标题栏
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}
}
