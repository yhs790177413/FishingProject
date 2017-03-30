package com.goby.fishing;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.text.TextUtils;

import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.framework.AbsBaseActivity;
import com.umeng.message.PushAgent;

/**
 * 启动页
 * 
 * @author yhs
 * 
 */
public class StartAppActivity extends AbsBaseActivity {

	private Timer mTimer;
	private TimerTask mTimerTask;
	private SharedPreferenceUtil sharedPreferenceUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_app);
		// 兼容部分机型home重启启动页面的问题
		if (!isTaskRoot()) {
			finish();
			return;
		}	
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		sharedPreferenceUtil = new SharedPreferenceUtil(this);

		mTimer = new Timer();
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
					GuideActivity.launch(StartAppActivity.this);
				} else {
					MainActivity.launch(StartAppActivity.this, false);
				}

				finish();
			}

		};
		mTimer.schedule(mTimerTask, 1000);
	}

}
