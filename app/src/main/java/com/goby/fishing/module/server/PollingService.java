package com.goby.fishing.module.server;//package com.goby.fishing.module.server;
//
//import com.android.volley.VolleyError;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.goby.fishing.MainActivity;
//import com.goby.fishing.bean.CheckUserBean;
//import com.goby.fishing.common.http.volleyHelper.HttpAPI;
//import com.goby.fishing.common.http.volleyHelper.RequestError;
//import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.IBinder;
//import android.util.Log;
//import android.view.View;
//
//public class PollingService extends Service {
//
//	private SharedPreferenceUtil sharedPreferenceUtil;
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//		sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
//	}
//
//	@Override
//	public void onStart(Intent intent, int startId) {
//		new PollingThread().start();
//	}
//
//	/**
//	 * Polling thread 模拟向Server轮询的异步线程
//	 * 
//	 * @Author yanghs
//	 */
//	int count = 0;
//
//	class PollingThread extends Thread {
//		@Override
//		public void run() {
//			//
//			Log.d("==来的这里轮询==", "");
//			HttpAPI.encryptAndGetJsonRequest(getApplicationContext(),
//					HttpAPI.checkUser, null,
//					sharedPreferenceUtil.getUserToken(), getVersionCode(), null,
//					CheckUserBean.class, new MessageSuccessListener(),
//					new ErrorRequestListener());
//		}
//	}
//
//	/**
//	 * 获取版本号
//	 * 
//	 * @return 当前应用的版本号
//	 */
//	public String getVersionCode() {
//		String versionCode = null;
//		try {
//			PackageManager manager = getApplicationContext().getPackageManager();
//			PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
//			versionCode = String.valueOf(info.versionCode);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return versionCode;
//	}
//	
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		System.out.println("Service:onDestroy");
//	}
//
//	private class MessageSuccessListener implements Listener<CheckUserBean> {
//
//		@Override
//		public void onResponse(CheckUserBean bean) {
//			if (bean.code == 0) {
//				if (bean.data.message_unread > 0) {
//					sharedPreferenceUtil.setRedPointIsVisible(true);
//				} else {
//					sharedPreferenceUtil.setRedPointIsVisible(false);
//				}
//				if (MainActivity.uiHandler != null) {
//					MainActivity.uiHandler.sendEmptyMessage(100);
//				}
//			} else {
//			}
//		}
//
//	}
//
//	private class ErrorRequestListener implements ErrorListener {
//
//		@Override
//		public void onErrorResponse(VolleyError arg0) {
//			RequestError.Error(arg0);
//		}
//
//	}
//
//}
