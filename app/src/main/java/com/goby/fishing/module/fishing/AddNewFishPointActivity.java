package com.goby.fishing.module.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.framework.AbsBaseActivity;

public class AddNewFishPointActivity extends AbsBaseActivity implements
		OnClickListener {

	private TextView tv_submit;

	private LinearLayout ll_leftBack;

	private EditText et_content;

	public static void launch(Activity activity, int requestCode) {

		Intent intent = new Intent(activity, AddNewFishPointActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_fish_point);

		initView();
	}

	public void initView() {

		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		tv_submit = (TextView) findViewById(R.id.submit_title);
		et_content = (EditText) findViewById(R.id.edit_content);

		ll_leftBack.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_back_layout:
			finish();
			break;
		case R.id.submit_title:
			if (!TextUtils.isEmpty(et_content.getText().toString().trim())) {
				Intent intent = new Intent();
				intent.putExtra("fishPointName", et_content.getText()
						.toString().trim());
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

}
