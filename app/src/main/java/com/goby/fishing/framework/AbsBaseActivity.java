package com.goby.fishing.framework;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.baidu.mobstat.StatService;
import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.app.ActivitiesHelper;
import com.goby.fishing.common.utils.helper.android.util.ProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Activity公共类
 * 
 * @author Administrator
 * 
 */
public class AbsBaseActivity extends FragmentActivity {

	protected HandlerThread mWorker;

	private LinkedList<Activity> mActs;
	
	public static ArrayList<Integer> color_list = new ArrayList<Integer>() {};

	// start--------------软键盘控制代码----------------
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		color_list.add(getResources().getColor(R.color.color_35B2E1));
		color_list.add(getResources().getColor(R.color.color_37C935));
		color_list.add(getResources().getColor(R.color.color_383F38));
		color_list.add(getResources().getColor(R.color.color_FFCC00));
		color_list.add(getResources().getColor(R.color.color_EF58A3));
		color_list.add(getResources().getColor(R.color.color_FC8C09));
		color_list.add(getResources().getColor(R.color.color_277BA2));
		PushAgent.getInstance(this).onAppStart();
		ActivitiesHelper.getInstance().addActivity(this);
		mActs = new LinkedList<Activity>();
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInput() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * 控制软键盘的显示隐藏
	 */
	public void showSoftInput() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	// end----------------软键盘控制代码----------------

	// start--------------显示等待progress代码----------------
	protected ProgressDialog mProgressDialog;

	/**
	 * 可以中途关闭
	 * 
	 * @param message
	 */
	public void showProgressDialog(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this, R.style.dialog,
					message);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
		}
		if (!mProgressDialog.isShowing()) {
			mProgressDialog = new ProgressDialog(this, R.style.dialog,
					message);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
		}
	}

	/**
	 * 可以中途关闭 监听后退
	 * 
	 * @param message
	 */
//	public void showProgressKeyDialog(final Handler handler, String message) {
//		if (mProgressDialog == null) {
//			mProgressDialog = new ProgressDialog(this);
//			mProgressDialog.setOnKeyListener(new OnKeyListener() {
//
//				@Override
//				public boolean onKey(DialogInterface arg0, int keyCode,
//						KeyEvent arg2) {
//					if (keyCode == KeyEvent.KEYCODE_BACK) {
//						// handler.sendEmptyMessage(ConstantValue.ERROR);
//					}
//					return false;
//				}
//			});
//			mProgressDialog.setCancelable(true);
//			mProgressDialog.setCanceledOnTouchOutside(false);
//			mProgressDialog.setMessage(message);
//		}
//		if (!mProgressDialog.isShowing()) {
//			mProgressDialog.setCanceledOnTouchOutside(false);
//			mProgressDialog.setMessage(message);
//			mProgressDialog.setOnKeyListener(new OnKeyListener() {
//
//				@Override
//				public boolean onKey(DialogInterface arg0, int keyCode,
//						KeyEvent arg2) {
//					if (keyCode == KeyEvent.KEYCODE_BACK) {
//						// handler.sendEmptyMessage(ConstantValue.ERROR);
//					}
//					return false;
//				}
//			});
//			mProgressDialog.show();
//		}
//	}

	/**
	 * 可以中途关闭
	 * 
	 * @param message
	 */
//	public ProgressDialog showDialog(String message) {
//		mProgressDialog = new ProgressDialog(this);
//		mProgressDialog.setCancelable(true);
//		mProgressDialog.setCanceledOnTouchOutside(true);
//		mProgressDialog.setMessage(message);
//		mProgressDialog.show();
//		return mProgressDialog;
//	}

	/**
	 * 不可中途关闭
	 * 
	 * @param message
	 */
//	public void showCannotCacenlProgressDialog(String message) {
//		if (mProgressDialog == null) {
//			mProgressDialog = new ProgressDialog(this);
//			mProgressDialog.setCanceledOnTouchOutside(false);
//			mProgressDialog.setCancelable(true);
//			mProgressDialog.setMessage(message);
//		}
//		if (!mProgressDialog.isShowing()) {
//			mProgressDialog = new ProgressDialog(this);
//			mProgressDialog.setCanceledOnTouchOutside(false);
//			mProgressDialog.setCancelable(true);
//			mProgressDialog.setMessage(message);
//			mProgressDialog.show();
//		}
//	}

	public void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	public boolean isProgressDialogShowing() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			return true;
		}
		return false;
	}

	// end----------------显示等待progress代码----------------

	@Override
	protected void onDestroy() {
		if (mWorker != null) {
			mWorker.quit();
		}
		if (isProgressDialogShowing()) {
			dismissProgressDialog();
		}
		super.onDestroy();
	}

	/**
	 * 关闭其他activity，唯独排除activityClass指定的activity
	 * 
	 * @param activityClass
	 */
	public void closeExcept(Class<?> activityClass) {
		synchronized (getApplicationContext()) {
			Activity act;
			Iterator<Activity> activityIterator = mActs.iterator();
			while (activityIterator.hasNext()) {
				act = activityIterator.next();
				if (!act.getClass().getName().equals(activityClass.getName())) {
					act.finish();
					activityIterator.remove();
				}
			}
		}
	}

	/**
	 * 关闭activityClass指定的activity
	 * 
	 * @param activityClass
	 */
	public void closeTarget(Class<?> activityClass) {
		synchronized (getApplicationContext()) {
			Activity act;
			Iterator<Activity> activityIterator = mActs.iterator();
			while (activityIterator.hasNext()) {
				act = activityIterator.next();
				if (act.getClass().getName().equals(activityClass.getName())) {
					act.finish();
					activityIterator.remove();
				}
			}
		}
	}

	/**
	 * 安装下载完成的APK
	 * 
	 * @param savedFile
	 */
	private void installAPK(File savedFile) {
		// 调用系统的安装方法
		Intent intent = new Intent();
		intent.setAction(intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(savedFile),
				"application/vnd.android.package-archive");
		startActivity(intent);
		finish();
	}

	// 设置liestview的高度
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 获取版本
	 * 
	 * @return 当前应用的版本
	 */
	public String getVersion() {
		String versionCode = null;
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			versionCode = info.versionName;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersionCode() {
		String versionCode = null;
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			versionCode = String.valueOf(info.versionCode);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	// 设置gridview的高度
	public void setGridViewHeightBasedOnChildren(GridView gridView, int num) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		if (listAdapter.getCount() < num) {
			for (int i = 0; i < 1; i++) {
				View listItem = listAdapter.getView(i, null, gridView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight() + 20;
			}
		} else {
			for (int i = 0; i < ((listAdapter.getCount() - 1) / num) + 1; i++) {
				View listItem = listAdapter.getView(i, null, gridView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight() + 20;
			}
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(""); // 友盟统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。)
		MobclickAgent.onResume(this); // 友盟统计时长
		StatService.onPageStart(this, getClass().getSimpleName());// 百度统计
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(""); // 友盟统计（仅有Activity的应用中SDK自动调用，不需要单独写）保证
										// onPageEnd 在onPause
										// 之前调用,因为 onPause
										// 中会保存信息。
		MobclickAgent.onPause(this);
		StatService.onPageEnd(this, getClass().getSimpleName());// 百度统计
	}

	public ArrayList<Integer> initColor(){
		color_list.add(getResources().getColor(R.color.color_35B2E1));
		color_list.add(getResources().getColor(R.color.color_37C935));
		color_list.add(getResources().getColor(R.color.color_383F38));
		color_list.add(getResources().getColor(R.color.color_FFCC00));
		color_list.add(getResources().getColor(R.color.color_EF58A3));
		color_list.add(getResources().getColor(R.color.color_FC8C09));
		color_list.add(getResources().getColor(R.color.color_277BA2));
		return color_list;
	}
	
	 public int dip2px(Context context, float dpValue) {
	        final float scale = context.getResources().getDisplayMetrics().density;
	        return (int) (dpValue * scale + 0.5f);
	 }
}
