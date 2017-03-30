package com.goby.fishing.framework;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.baidu.mobstat.StatService;
import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.util.ProgressDialog;
import com.umeng.analytics.MobclickAgent;

public abstract class LazyLoadFragment extends Fragment {
	
	private ProgressDialog dialog_loading;
	
	public static ArrayList<Integer> color_list = new ArrayList<Integer>() {};
	
	/** Fragment当前状态是否可见 */
    protected boolean isVisible;
     
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
         
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
     
     
    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();     
    }
     
    /**
     * 不可见
     */
    protected void onInvisible() {}
     
    /** 
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

	public Context getContext() {
		return getActivity();
	}

	public Context getApplicationContext() {
		return getActivity().getApplicationContext();
	}

	public void runOnUiThread(Runnable runnable) {
		try {
			getActivity().runOnUiThread(runnable);
		} catch (Exception e) {
		}
	}

	// public Account getLoginAccount() {
	// return AccountManager.getInstance().getLoginAccount();
	// }
	/**
	 * 显示进度框
	 */
	public void showProgressDialog(String title) {
		
		dialog_loading = new ProgressDialog(getActivity(), R.style.dialog,
				title);
		dialog_loading.setCanceledOnTouchOutside(false);
		dialog_loading.setCancelable(true);
		dialog_loading.show();
	
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
	
	/**
	 * 显示进度框
	 */
//	public void showProgressdisCanDialog(String title) {
//		mProgressDialog = new ProgressDialog(getActivity());
//		mProgressDialog.setMessage(title);
//		mProgressDialog.setCanceledOnTouchOutside(false);
//		mProgressDialog.show();
//	}

	/**
	 * 隐藏进度框
	 */
	public void dismissProgressDialog() {
		if (dialog_loading != null) {
			dialog_loading.dismiss();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(""); // 友盟统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。)
		MobclickAgent.onResume(getActivity()); // 友盟统计时长
		StatService.onPageStart(getActivity(), getClass().getSimpleName());// 百度统计
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(""); // 友盟统计（仅有Activity的应用中SDK自动调用，不需要单独写）保证
										// onPageEnd 在onPause
										// 之前调用,因为 onPause
										// 中会保存信息。
		MobclickAgent.onPause(getActivity());
		StatService.onPageEnd(getActivity(), getClass().getSimpleName());// 百度统计
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

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersionCode() {
		String versionCode = null;
		try {
			PackageManager manager = getActivity().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getActivity()
					.getPackageName(), 0);
			versionCode = String.valueOf(info.versionCode);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}

}
