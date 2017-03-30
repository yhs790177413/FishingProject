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

public class ShowOrderPopuWindow extends PopupWindow {

	private View popView;

	private PopupWindow poupuWindow;

	private Context mContext;

	private Message message;
	
	private GridView gv_order;
	
	private OrderAdapter adapter;
	
	private ArrayList<String> orderData = new ArrayList<String>();
	
	private Handler mUiHandler;
	
	private TextView tv_selete;
	
	private String mOrderSeletet;

	public ShowOrderPopuWindow(final Context mContext, View popLayout,
			Handler handler, String order_selete) {
		
		super(mContext);

		View popupView = LayoutInflater.from(mContext).inflate(
				R.layout.popu_order, null);

		this.mContext = mContext;
		this.mUiHandler = handler;
		this.mOrderSeletet = order_selete;
		popView = (LinearLayout) popupView.findViewById(R.id.order_layout);
		tv_selete = (TextView) popupView.findViewById(R.id.selete_order);
		if(TextUtils.isEmpty(mOrderSeletet)){
			tv_selete.setText("最近");
		}else{
			tv_selete.setText(mOrderSeletet);
		}
		gv_order = (GridView) popupView.findViewById(R.id.order_grid);
		gv_order.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new OrderAdapter();
		gv_order.setAdapter(adapter);
		gv_order.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				poupuWindow.dismiss();
				mOrderSeletet = orderData.get(position);
				adapter.notifyDataSetChanged();
				message = new Message();
				message.what = FishingFragment.ORDER;
				message.obj = mOrderSeletet;
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
				message.what = FishingFragment.ORDER_DISMISS;
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
		orderData.add("最近");
		orderData.add("最热");
		orderData.add("最新");
		adapter.notifyDataSetChanged();
	}
	
	private class OrderAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderData.size();
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
			viewholder.tv_cityName.setText(orderData.get(position));
			if(TextUtils.isEmpty(mOrderSeletet)){
				mOrderSeletet = "最近";
			}
			if(mOrderSeletet.equals(orderData.get(position))){
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
