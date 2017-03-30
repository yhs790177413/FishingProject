package com.goby.fishing.common.utils.helper.android.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.framework.AbsBaseActivity;

public class WebViewActivity extends AbsBaseActivity implements
		View.OnClickListener {

	private WebView detailsWebView;
	private String mUrl;
	private Handler handler;
	// private ProgressDialog pd;
	private String mTitle;
	private LinearLayout ll_leftBack;
	private TextView tv_title;

	public static void launch(Activity activity, String url, String title) {

		Intent intent = new Intent(activity, WebViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		mUrl = this.getIntent().getStringExtra("url");
		mTitle = this.getIntent().getStringExtra("title");
		detailsWebView = (WebView) findViewById(R.id.web_wv);
		detailsWebView.getSettings().setJavaScriptEnabled(true);
		detailsWebView.getSettings().setDomStorageEnabled(true);
		detailsWebView.requestFocus(View.FOCUS_DOWN);
		detailsWebView
				.getSettings()
				.setUserAgentString(
						"User-Agent: Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		// detailsWebView.getSettings().setUserAgentString("User-Agent");

		// String dir = this.getApplicationContext().getDir("database",
		// Context.MODE_PRIVATE).getPath();
		// //启用数据库
		// detailsWebView.getSettings().setDatabaseEnabled(true);
		// //启用地理定位
		// detailsWebView.getSettings().setGeolocationEnabled(true);
		// //设置定位的数据库路径
		// detailsWebView.getSettings().setGeolocationDatabasePath(dir);
		// //最重要的方法，一定要设置，这就是出不来的主要原因
		// detailsWebView.getSettings().setDomStorageEnabled(true);

		detailsWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;
			}
		});
		// 设置web视图客户端
		detailsWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发

				if (progress == 100) {
					handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框 }
					detailsWebView.setVisibility(View.VISIBLE);
				}
				super.onProgressChanged(view, progress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);

			}
		});

		detailsWebView.setVisibility(View.GONE);
		// pd = new ProgressDialog(this);
		// pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// pd.setMessage("数据载入中，请稍候！");

		handler = new Handler() {

			public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
				super.handleMessage(msg);
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case 0:
						showProgressDialog("正在加载数据中,请稍候...");// 显示进度对话框
						break;
					case 1:
						dismissProgressDialog();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
						break;
					}
				}

			}

		};
		loadurl(detailsWebView, mUrl);
		initView();
	}

	public void initView() {
		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		tv_title = (TextView) findViewById(R.id.center_title);
		ll_leftBack.setOnClickListener(this);
		tv_title.setText(mTitle);
	}

	public void loadurl(final WebView view, final String url) {

		handler.post(new Runnable() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
				view.loadUrl(url);// 载入网页
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_back_layout:
			finish();
		default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		detailsWebView.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		detailsWebView.destroy();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		detailsWebView.clearCache(true);
		detailsWebView.clearHistory();
	}
}
