package com.goby.fishing.common.photochoose;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.baidu.android.common.logging.Log;
import com.goby.fishing.R;
import com.goby.fishing.application.FishingApplication;
import com.goby.fishing.common.photochoose.adapter.ImagePagerAdapter;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.framework.AbsBaseActivity;

/**
 * 大图图片浏览器
 */
public class ImageBrowseActivity extends AbsBaseActivity {
	
	public static HashMap<String,Bitmap> bitmapHash = new HashMap<String, Bitmap>();
	
	public enum LaunchEnum {
		noSelected, // 不需要显示checkbox
		selected// 需要显示checkbox
	}

	public static void launch(Activity activity, ArrayList<String> list,
			int index,String userHeader, String userName,String fishType,String fishTips,String fishArea,String fishTime,String fishComment,boolean isShow) {
		Intent i = new Intent(activity, ImageBrowseActivity.class);
		i.putExtra(ImageBrowseActivity.EXTRA_IMAGES, list);
		i.putExtra(ImageBrowseActivity.EXTRA_INDEX, index);
		i.putExtra(ImageBrowseActivity.EXTRA_LAUNCH_MODEL,
				LaunchEnum.noSelected);
		i.putExtra("userHeader",userHeader);
		i.putExtra("userName",userName);
		i.putExtra("fishType",fishType);
		i.putExtra("fishTips",fishTips);
		i.putExtra("fishArea",fishArea);
		i.putExtra("fishTime",fishTime);
		i.putExtra("fishComment",fishComment);
		i.putExtra("isShow",isShow);
		activity.startActivity(i);
		// 设置启动的动画
		activity.overridePendingTransition(R.anim.zoom_in, R.anim.no_anim);
	}

	public static void launch(Activity activity, ArrayList<String> list,
			int index, LaunchEnum launchEnum) {
		Intent i = new Intent(activity, ImageBrowseActivity.class);
		i.putExtra(ImageBrowseActivity.EXTRA_IMAGES, list);
		i.putExtra(ImageBrowseActivity.EXTRA_INDEX, index);
		i.putExtra(ImageBrowseActivity.EXTRA_LAUNCH_MODEL, launchEnum);
		activity.startActivity(i);
		// 设置启动的动画
		activity.overridePendingTransition(R.anim.zoom_in, R.anim.no_anim);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.no_anim, R.anim.zoom_out);
	}

	public static final String EXTRA_LAUNCH_MODEL = "extra_launch_model";
	public static final String EXTRA_IMAGES = "extra_images";
	public static final String EXTRA_INDEX = "extra_index";

	private ArrayList<String> mDatas = new ArrayList<String>();
	private LaunchEnum mLaunchEnum = LaunchEnum.noSelected;
	private int mPageIndex = 0;
	public static int columnSelectIndex;
	private ImagePagerAdapter mImageAdapter = null;
	private ViewPager mViewPager = null;
	public static int screenWidth,screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_browse);
		mViewPager = (ViewPager) findViewById(R.id.image_vp);
		
		// 获取屏幕宽高（方法1）  
		screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
		screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高  
		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_IMAGES)) {
			mDatas = intent.getStringArrayListExtra(EXTRA_IMAGES);
			mLaunchEnum = (LaunchEnum) intent
					.getSerializableExtra(EXTRA_LAUNCH_MODEL);
			mPageIndex = intent.getIntExtra(EXTRA_INDEX, 0);
			columnSelectIndex = mPageIndex;
			mImageAdapter = new ImagePagerAdapter(this, mDatas,intent.getStringExtra("userHeader"),intent.getStringExtra("userName"),intent.getStringExtra("fishType"),intent.getStringExtra("fishTips"),intent.getStringExtra("fishArea"),intent.getStringExtra("fishTime"),intent.getStringExtra("fishComment"),intent.getBooleanExtra("isShow", false),mLaunchEnum);
			mViewPager.setAdapter(mImageAdapter);
			mViewPager.setCurrentItem(mPageIndex);
		}
		mViewPager.setOnPageChangeListener(pageListener);
	}

	/**
	 * ViewPager切换监听方法
	 */
	public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			columnSelectIndex = position;
		}
	};
}
