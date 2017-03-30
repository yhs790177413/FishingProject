package com.goby.fishing.module.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.framework.AbsBaseActivity;

public class CatchInfoStepOneActivity extends AbsBaseActivity implements
		OnClickListener {

	private LinearLayout ll_leftBack;

	private String chooseString = "";

	private TextView tv_nextBtn;

	private int number = -99;

	private final static int FISH_TYPE = 102;

	private ListView lv_fishCount;

	private FishCountAdapter adapter;

	public static void launch(Activity activity, int requestCode) {

		Intent intent = new Intent(activity, CatchInfoStepOneActivity.class);
		intent.putExtra("requestCode", requestCode);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catch_info_step_one);

		initView();
	}

	public void initView() {

		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		tv_nextBtn = (TextView) findViewById(R.id.next_btn);
		lv_fishCount = (ListView) findViewById(R.id.fish_count_list);
		adapter = new FishCountAdapter();
		lv_fishCount.setAdapter(adapter);

		tv_nextBtn.setOnClickListener(this);
		ll_leftBack.setOnClickListener(this);
		lv_fishCount.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (position == 0) {
					chooseString = "爆护";
					number = -1;
				} else if (position == 1) {
					chooseString = "空军";
					number = 0;
				} else if (position == 2) {
					chooseString = "有巨物";
					number = -2;
				} else {
					chooseString = position - 2 + "尾";
				}
				FishingTypeActivity.launch(CatchInfoStepOneActivity.this,
						FISH_TYPE);
			}
		});
	}

	private class FishCountAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 102;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder viewholder;
			if (convertView == null) {
				viewholder = new ViewHolder();
				convertView = LayoutInflater
						.from(CatchInfoStepOneActivity.this).inflate(
								R.layout.item_fish_count, null, false);
				viewholder.tv_fishCount = (TextView) convertView
						.findViewById(R.id.catch_choose);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				viewholder.tv_fishCount.setText("爆护");
			} else if (position == 1) {
				viewholder.tv_fishCount.setText("空军");
			} else if (position == 2) {
				viewholder.tv_fishCount.setText("有巨物");
			} else {
				viewholder.tv_fishCount.setText(position - 2 + "尾");
			}
			return convertView;
		}
	}

	public class ViewHolder {

		private TextView tv_fishCount;
	}

	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(request, result, data);
		if (result == RESULT_OK) {
			String typeName = data.getStringExtra("type_names");
			String typeIds = data.getStringExtra("type_ids");
			Intent intent = new Intent();
			intent.putExtra("fishCatchInfo", chooseString + " " + typeName);
			intent.putExtra("type_ids", typeIds);
			intent.putExtra("number", number);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_back_layout:
			finish();
			break;
		case R.id.next_btn:
			FishingTypeActivity.launch(this, FISH_TYPE);
			break;
		default:
			break;
		}
	}
}
