package com.goby.fishing.module.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class ModifyNickActivity extends AbsBaseActivity implements
		OnClickListener {

	private EditText et_nick;

	private TextView tv_commit;

	private ImageView iv_back;
	
	private LinearLayout ll_leftBack;

	public static void launch(Activity activity, String nick_name,
			int requestCode) {

		Intent intent = new Intent(activity, ModifyNickActivity.class);
		intent.putExtra("nick_name", nick_name);
		intent.putExtra("requestCode", requestCode);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_nick);

		initView();
	}

	public void initView() {

		et_nick = (EditText) findViewById(R.id.edit_nick);
		et_nick.setText(getIntent().getStringExtra("nick_name"));

		tv_commit = (TextView) findViewById(R.id.text_right);
		tv_commit.setOnClickListener(this);

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
		case R.id.text_right:
			if (et_nick.getText().toString().trim()
					.equals(getIntent().getStringExtra("nick_name"))) {
				ToastHelper.showToast(this, "请修改昵称,再保存");
			} else {
				Intent intent = new Intent();
				intent.putExtra("nick_name", et_nick.getText().toString()
						.trim());
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

}
