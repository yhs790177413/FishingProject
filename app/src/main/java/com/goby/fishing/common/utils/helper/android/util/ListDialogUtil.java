package com.goby.fishing.common.utils.helper.android.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.app.CityList;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 列表对话框
 * 
 * @author yhs
 * 
 */
public class ListDialogUtil extends Dialog {

	private final Context mContext;

	private ListView provinceListView;

	private ListView cityListView;

	private CityListAdapter cityAdapter;

	private ProvinceListAdapter provinceAdapter;

	private ArrayList<String> provinceDataList;

	private ArrayList<String> cityDataList;

	private LayoutInflater inflater;

	private Message message;

	private String action;
	
	private Handler mUiHandler;

	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	public ProvinceViewHolder provinceViewholder;

	public CityViewHolder cityViewholder;

	private String provinceName, cityName;

	public ListDialogUtil(Context mContext, int theme,
			Handler uiHandler) {
		super(mContext, theme);
		// 加载布局文件
		this.setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.dialog_list, null));
		this.mContext = mContext;
		mUiHandler = uiHandler;
		inflater = LayoutInflater.from(mContext);
		initData();
		initView();
	}

	public void initView() {

		provinceListView = (ListView) findViewById(R.id.province_list);
		provinceAdapter = new ProvinceListAdapter();
		provinceListView.setAdapter(provinceAdapter);
		cityListView = (ListView) findViewById(R.id.city_list);
		cityAdapter = new CityListAdapter();
		cityListView.setAdapter(cityAdapter);
		provinceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				for (int i = 0; i < list.size(); i++)
				{
					if (i == position)
					{
						list.get(i).put("flag", "true");
					}
					else
					{
						list.get(i).put("flag", "false");
					}
				}
				provinceName = provinceDataList.get(position);
				provinceAdapter.notifyDataSetChanged();

				cityDataList.clear();
				cityDataList.addAll(CityList.Select_city(provinceDataList
						.get(position)));
				cityAdapter.notifyDataSetChanged();
			}
		});
		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				cityName = cityDataList.get(position);
				message = new Message();
				message.obj = cityName;
				mUiHandler.sendMessage(message);
				dismiss();
			}
		});
	}

	public void initData() {

		provinceDataList = new ArrayList<String>();
		cityDataList = new ArrayList<String>();
		provinceDataList.addAll(CityList.Province_List);
		provinceName = provinceDataList.get(0);
		cityDataList.addAll(CityList.Select_city(provinceDataList.get(0)));
		for (int i = 0; i < provinceDataList.size(); i++)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			if (i == 0)
			{
				map.put("flag", "true");
			}
			else
			{
				map.put("flag", "false");
			}
			list.add(map);
		}
	}

	/**
	 * 城市Adapter
	 * 
	 * @author yhs
	 * 
	 */
	class CityListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cityDataList.size();
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

			if (convertView == null)
			{
				cityViewholder = new CityViewHolder();
				convertView = inflater.inflate(R.layout.item_city, null, false);
				cityViewholder.cityTextView = (TextView) convertView
						.findViewById(R.id.city_name);
				convertView.setTag(cityViewholder);
			}
			else
			{
				cityViewholder = (CityViewHolder) convertView.getTag();
			}
			cityViewholder.cityTextView.setText(cityDataList.get(position));
			return convertView;
		}

	}

	/**
	 * 省份Adapter
	 * 
	 * @author yhs
	 * 
	 */
	class ProvinceListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return provinceDataList.size();
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

			if (convertView == null)
			{
				provinceViewholder = new ProvinceViewHolder();
				convertView = inflater.inflate(R.layout.item_province, null,
						false);
				provinceViewholder.provinceTextView = (TextView) convertView
						.findViewById(R.id.province_name);
				convertView.setTag(provinceViewholder);
			}
			else
			{
				provinceViewholder = (ProvinceViewHolder) convertView.getTag();
			}

			if (position % 2 == 0)
			{
				if (list.get(position).get("flag").equals("true"))
				{
					provinceViewholder.provinceTextView
							.setBackgroundResource(R.drawable.item_bg_gray_select);
					 provinceViewholder.provinceTextView.setTextColor(mContext
					 .getResources().getColor(R.color.blue_35b2e1));
				}
				else
				{
					provinceViewholder.provinceTextView
							.setBackgroundResource(R.drawable.item_bg_gray_normal);
					 provinceViewholder.provinceTextView.setTextColor(mContext
					 .getResources().getColor(R.color.black));
				}
			}
			else
			{
				if (list.get(position).get("flag").equals("true"))
				{
					provinceViewholder.provinceTextView
							.setBackgroundResource(R.drawable.item_bg_white_select);
					 provinceViewholder.provinceTextView.setTextColor(mContext
					 .getResources().getColor(R.color.blue_35b2e1));
				}
				else
				{
					provinceViewholder.provinceTextView
							.setBackgroundResource(R.drawable.item_bg_white_normal);
					 provinceViewholder.provinceTextView.setTextColor(mContext
					 .getResources().getColor(R.color.black));
				}
			}
			provinceViewholder.provinceTextView.setText(provinceDataList
					.get(position));
			return convertView;
		}

	}

	public class ProvinceViewHolder {

		public TextView provinceTextView;
	}

	public class CityViewHolder {

		public TextView cityTextView;
	}

}
