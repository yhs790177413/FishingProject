package com.goby.fishing.popuwindow;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.goby.fishing.R;
import com.goby.fishing.module.fishing.FishingFragment;
import com.goby.fishing.module.fishing.FishingFragment.FishingFragmentUiHander;

public class ShowWaterPopuWindow extends PopupWindow {

	private View popView;

	private PopupWindow poupuWindow;

	private Context mContext;

	private Message message;
	
	private GridView gv_water;
	
	private WaterAdapter adapter;
	
	private ArrayList<String> waterData = new ArrayList<String>();
	
	private Handler mUiHandler;
	private String mWaterSelete;
	
	private TextView tv_water;

	public ShowWaterPopuWindow(final Context mContext, View popLayout,
			FishingFragmentUiHander handler, String water_selete) {
		
		super(mContext);

		View popupView = LayoutInflater.from(mContext).inflate(
				R.layout.popu_water, null);

		this.mContext = mContext;
		this.mUiHandler = handler;
		this.mWaterSelete = water_selete;
		popView = (LinearLayout) popupView.findViewById(R.id.water_layout);
		tv_water = (TextView) popupView.findViewById(R.id.selete_water);
		if(TextUtils.isEmpty(mWaterSelete)){
			tv_water.setText("全部");
		}else{
			tv_water.setText(mWaterSelete);
		}
		gv_water = (GridView) popupView.findViewById(R.id.water_grid);
		gv_water.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new WaterAdapter();
		gv_water.setAdapter(adapter);
		gv_water.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				poupuWindow.dismiss();
				mWaterSelete = waterData.get(position);
				adapter.notifyDataSetChanged();
				message = new Message();
				message.what = FishingFragment.WATER;
				message.obj = mWaterSelete;
				mUiHandler.sendMessage(message);
			}
		});
		
		initData();
		
		poupuWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		final ColorDrawable dw = new ColorDrawable(R.drawable.bg);
		poupuWindow.setBackgroundDrawable(dw);
		poupuWindow.setOutsideTouchable(true);
		popView.setFocusableInTouchMode(true);

		poupuWindow.showAsDropDown(popLayout, 0, 0);
		poupuWindow.update();
		poupuWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				message = new Message();
				message.what = FishingFragment.WATER_DISMISS;
				mUiHandler.sendMessage(message);
			}
		});

		// 重写onKeyListener
		popupView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					poupuWindow.dismiss();

					return true;
				}
				return false;
			}
		});
	}
	
	public void initData(){
		waterData.add("全部");
		waterData.add("湖泊");
		waterData.add("水库");
		waterData.add("河流");
		waterData.add("海洋");
		adapter.notifyDataSetChanged();
	}
	
	private class WaterAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return waterData.size();
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
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_fishing_popup, null, false);
				viewholder.tv_cityName = (TextView) convertView
						.findViewById(R.id.item_text);
				viewholder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewholder.v_line = (View) convertView
						.findViewById(R.id.v_line);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			viewholder.tv_title.setVisibility(View.GONE);
			viewholder.v_line.setVisibility(View.GONE);
			viewholder.tv_cityName.setText(waterData.get(position));
			if(TextUtils.isEmpty(mWaterSelete)){
				mWaterSelete = "全部";
			}
			if(mWaterSelete.equals(waterData.get(position))){
				viewholder.tv_cityName.setTextColor(mContext.getResources().getColor(R.color.red_d30549));
			}else{
				viewholder.tv_cityName.setTextColor(mContext.getResources().getColor(R.color.gray_aaaaaa));
			}
			return convertView;
		}
	}

	public class ViewHolder {

		private TextView tv_cityName;
		private TextView tv_title;
		private View v_line;
	}
}
