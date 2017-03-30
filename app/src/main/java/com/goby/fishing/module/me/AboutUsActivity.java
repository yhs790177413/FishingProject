package com.goby.fishing.module.me;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.framework.AbsBaseActivity;

public class AboutUsActivity extends AbsBaseActivity implements OnClickListener {

	private TextView tv_text;

	private ImageView iv_back;
	
	private LinearLayout ll_leftBack;

	public static void launch(Activity activity) {

		Intent intent = new Intent(activity, AboutUsActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

		initView();
	}

	public void initView() {

		iv_back = (ImageView) findViewById(R.id.left_back);
		iv_back.setOnClickListener(this);
		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
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
