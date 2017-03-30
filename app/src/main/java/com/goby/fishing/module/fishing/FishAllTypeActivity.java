package com.goby.fishing.module.fishing;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controller.Constant;
import com.goby.fishing.R;
import com.example.controller.bean.FishingDetialBean.Data.FishTyes;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.widget.imageview.ImageCircleView;
import com.goby.fishing.framework.AbsBaseActivity;

public class FishAllTypeActivity extends AbsBaseActivity implements
		OnClickListener {

	private ListView lv_allType;

	private FishTypeAdapter adapter;

	private ImageView iv_back;

	private LinearLayout ll_leftBack;

	private ArrayList<FishTyes> mDataList = new ArrayList<FishTyes>();

	public static void launch(Activity activity, ArrayList<FishTyes> dataList) {

		Intent intent = new Intent(activity, FishAllTypeActivity.class);
		intent.putExtra("dataList", dataList);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fish_all_type);
		mDataList.clear();
		mDataList.addAll((ArrayList<FishTyes>) getIntent()
				.getSerializableExtra("dataList"));
		initView();
	}

	public void initView() {

		lv_allType = (ListView) findViewById(R.id.fish_all_type);
		adapter = new FishTypeAdapter();
		lv_allType.setAdapter(adapter);

		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.left_back);
		iv_back.setOnClickListener(this);
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

	private class FishTypeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataList.size();
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
				convertView = LayoutInflater.from(FishAllTypeActivity.this)
						.inflate(R.layout.item_fishing_type, null, false);

				viewholder.icv_fishImage = (ImageView) convertView
						.findViewById(R.id.fish_image);
				viewholder.tv_fishName = (TextView) convertView
						.findViewById(R.id.fish_name);
				viewholder.cb_select = (ImageView) convertView
						.findViewById(R.id.check_btn);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			viewholder.icv_fishImage.setImageResource(R.drawable.fish_point_view_icon);
			if (!TextUtils.isEmpty(mDataList.get(position).pic_url)) {
				if (mDataList.get(position).pic_url.startsWith("http://")) {
					ImageLoaderWrapper.getDefault().displayImage(
							mDataList.get(position).pic_url,
							viewholder.icv_fishImage);
				} else {
					ImageLoaderWrapper.getDefault().displayImage(
							Constant.HOST_URL + mDataList.get(position).pic_url,
							viewholder.icv_fishImage);
				}
			}

			viewholder.tv_fishName.setText(mDataList.get(position).name);
			viewholder.cb_select.setVisibility(View.GONE);
			return convertView;
		}
	}

	public class ViewHolder {

		private ImageView icv_fishImage;
		private TextView tv_fishName;
		private ImageView cb_select;
	}

}
