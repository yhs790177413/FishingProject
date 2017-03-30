package com.goby.fishing.common.utils.helper.android.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.goby.fishing.R;

public class ProgressDialog extends Dialog {
	private TextView tv_message;

	public ProgressDialog(Activity mContext, int theme, String message) {
		super(mContext, theme);
		// 加载布局文件
		this.setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.dialog_progress_view, null));

		tv_message = (TextView) findViewById(R.id.progress_text);
		tv_message.setText(message);
	}
}
