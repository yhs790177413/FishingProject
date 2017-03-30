/*
 * author: lachang@youzan.com
 * Copyright (C) 2016 Youzan, Inc. All Rights Reserved.
 */
package com.goby.fishing.module.index;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.common.logging.Log;
import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.framework.AbsBaseActivity;
import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.YouzanUser;
import com.youzan.sdk.http.engine.OnRegister;
import com.youzan.sdk.http.engine.QueryError;
import com.youzan.sdk.model.goods.GoodsShareModel;
import com.youzan.sdk.web.bridge.IBridgeEnv;
import com.youzan.sdk.web.event.ShareDataEvent;
import com.youzan.sdk.web.plugin.YouzanBrowser;

/**
 * 异步请求用户信息. 打开网页前需要调用
 * {@link YouzanSDK#asyncRegisterUser(YouzanUser, OnRegister);}来 完成用户态同步.
 */
public class AsyncWebActivity extends AbsBaseActivity {

	private YouzanBrowser mWebView;

	private SharedPreferenceUtil sharedPreferenceUtil;

	private LinearLayout ll_leftBack;

	private TextView tv_title;

	public static void launch(Activity activity, String url, String title) {

		Intent intent = new Intent(activity, AsyncWebActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		sharedPreferenceUtil = new SharedPreferenceUtil(this);
		tv_title = (TextView) findViewById(R.id.center_title);
		tv_title.setText(getIntent().getStringExtra("title"));
		initWebView();
		invokeAsyncRegister();
	}

	private void initWebView() {
		mWebView = (YouzanBrowser) findViewById(R.id.webview);
		findViewById(R.id.btn_share).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						mWebView.sharePage();// 触发分享
					}
				});

		// 订阅分享回调
		mWebView.subscribe(new ShareDataEvent() {
			@Override
			public void call(IBridgeEnv iBridgeEnv, GoodsShareModel data) {

				String content = data.getDesc() + data.getLink();
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, content);
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
				sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});

		// 上传文件的回调
		mWebView.setOnChooseFileCallback(new YouzanBrowser.OnChooseFile() {
			@Override
			public void onWebViewChooseFile(Intent intent, int i)
					throws ActivityNotFoundException {
				startActivityForResult(intent, i);
			}
		});

		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 清空数据
				YouzanSDK.userLogout(AsyncWebActivity.this);
				finish();
			}
		});
	}

	private void invokeAsyncRegister() {
		YouzanUser user = new YouzanUser();
		Log.e("====id====", sharedPreferenceUtil.getUserID());
		user.setUserId(sharedPreferenceUtil.getUserID());
		user.setGender(sharedPreferenceUtil.getUserSex());
		user.setNickName(sharedPreferenceUtil.getUserName());
		user.setUserName(sharedPreferenceUtil.getUserName());
		user.setTelephone(sharedPreferenceUtil.getUserPhone());
		// ...其他参数说明请看API文档{@link }

		YouzanSDK.asyncRegisterUser(user, new OnRegister() {
			@Override
			public void onFailed(QueryError queryError) {
				Toast.makeText(AsyncWebActivity.this, queryError.getMsg(),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess() {

				// 打开店铺链接等
				// TODO-WARNING: 请修改成你们店铺的链接
				mWebView.loadUrl(getIntent().getStringExtra("url"));
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mWebView.isReceiveFileForWebView(requestCode, data);
		// 处理WebView上传文件, 就上面一句就行了
	}

	/**
	 * 页面回退 YouzanBrowser.pageGoBack()返回True表示处理的是网页的回退
	 */
	@Override
	public void onBackPressed() {
		if (!mWebView.pageGoBack()) {
			super.onBackPressed();
			// 清空数据
			YouzanSDK.userLogout(AsyncWebActivity.this);
		}
	}
}
