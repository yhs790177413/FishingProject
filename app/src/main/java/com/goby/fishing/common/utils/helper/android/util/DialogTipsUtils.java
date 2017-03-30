package com.goby.fishing.common.utils.helper.android.util;


import com.goby.fishing.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class DialogTipsUtils extends Dialog implements View.OnClickListener {
	
	private Activity mContext;
	
	private ImageView iv_playBtn;
	
	public DialogTipsUtils(Activity mContext, int theme) {
		super(mContext, theme);
		this.mContext = mContext;
		// 加载布局文件
		this.setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.dialog_tip, null));	
		
		iv_playBtn = (ImageView) findViewById(R.id.play_btn);
		iv_playBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.play_btn:
			dismiss();
			break;
		default:
			break;
		}
	}
}
