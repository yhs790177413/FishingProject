package com.goby.fishing.module.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.framework.AbsBaseActivity;

public class SummaryDetailActivity extends AbsBaseActivity implements
		OnClickListener {

	private String mInfo;

	private TextView tv_summary;

	private ImageView iv_back;
	
	private LinearLayout ll_leftBack;

	public static void launch(Activity activity, String content) {

		Intent intent = new Intent(activity, SummaryDetailActivity.class);
		intent.putExtra("info", content);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary_detail);

		mInfo = getIntent().getStringExtra("info");
		initView();
	}

	public void initView() {

		tv_summary = (TextView) findViewById(R.id.summary);
		tv_summary.setText(mInfo);

		iv_back = (ImageView) findViewById(R.id.left_back);
		iv_back.setOnClickListener(this);
		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_back:
			finish();
			break;
		case R.id.left_back_layout:
			finish();
			break;
		default:
			break;
		}
	}

}
