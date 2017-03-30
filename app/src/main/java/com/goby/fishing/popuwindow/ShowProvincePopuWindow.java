package com.goby.fishing.popuwindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.goby.fishing.R;
import com.example.controller.bean.FishCityListBean;
import com.example.controller.bean.FishCityListBean.ParentData.CityBean;
import com.goby.fishing.common.utils.helper.android.app.CharacterParser;
import com.goby.fishing.common.utils.helper.android.app.PinyinComparator;
import com.goby.fishing.common.utils.helper.android.app.SideBar;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.module.fishing.FishingFragment;
import com.goby.fishing.module.fishing.FishingFragment.FishingFragmentUiHander;
import com.google.gson.Gson;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class ShowProvincePopuWindow extends PopupWindow {

	private View popView;

	private PopupWindow poupuWindow;

	private Context mContext;

	private Message message;

	private ListView lv_city;

	private CityAdapter adapter;

	private ArrayList<CityBean> cityData = new ArrayList<CityBean>();

	private Handler mUiHandler;

	private String mCitySeletet;

	private TextView tv_city;

	private TextView mDialog;

	private SharedPreferenceUtil sharedPreferenceUtil;

	private SideBar mSb;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	public ShowProvincePopuWindow(final Context mContext, View popLayout,
			Handler handler, String city_selete) {

		super(mContext);

		View popupView = LayoutInflater.from(mContext).inflate(
				R.layout.popu_city, null);

		this.mContext = mContext;
		this.mUiHandler = handler;
		this.mCitySeletet = city_selete;

		sharedPreferenceUtil = new SharedPreferenceUtil(mContext);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		popView = (LinearLayout) popupView.findViewById(R.id.province_layout);
		tv_city = (TextView) popupView.findViewById(R.id.selete_city);
		tv_city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				poupuWindow.dismiss();
				mCitySeletet = "全部";
				adapter.notifyDataSetChanged();
				message = new Message();
				message.what = FishingFragment.PROVINCE;
				Bundle bundle = new Bundle();
				bundle.putString("citySeletet", mCitySeletet);
				bundle.putInt("cityNo", 0);
				message.obj = bundle;

				mUiHandler.sendMessage(message);
			}
		});
		mDialog = (TextView) popupView.findViewById(R.id.dialog);
		mSb = (SideBar) popupView.findViewById(R.id.sidrbar);
		mSb.setTextView(mDialog);
		// 设置右侧触摸监听
		mSb.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				if (cityData != null && cityData.size() > 0) {
					if (s.equals("")) {
						lv_city.setSelection(0);
						return;
					}
					// 该字母首次出现的位置
					int position = adapter.getPositionForSection(s.charAt(0));
					if (position != -1) {
						lv_city.setSelection(position);
					}
				}
			}
		});
		lv_city = (ListView) popupView.findViewById(R.id.lv_city);
		adapter = new CityAdapter();
		lv_city.setAdapter(adapter);
		lv_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				poupuWindow.dismiss();
				mCitySeletet = cityData.get(position).name;
				adapter.notifyDataSetChanged();
				message = new Message();
				message.what = FishingFragment.PROVINCE;
				Bundle bundle = new Bundle();
				bundle.putString("citySeletet", mCitySeletet);
				bundle.putInt("cityNo", cityData.get(position).id);
				message.obj = bundle;

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
				message.what = FishingFragment.PROVINCE_DISMISS;
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

	public void initData() {
		Gson gson = new Gson();
		cityData = gson.fromJson(sharedPreferenceUtil.getFishCityJson(),
				FishCityListBean.class).data.list;

		filledData(cityData);
		// // 根据a-z进行排序源数据
		Collections.sort(cityData, pinyinComparator);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 为ListView填充数据，设置model中的首字母
	 * 
	 * @param data
	 * @return
	 */
	private List<CityBean> filledData(List<CityBean> data) {
		for (int i = 0; i < data.size(); i++) {
			if (!TextUtils.isEmpty(data.get(i).name)
					&& !data.get(i).name.equals("''")) {
				// 汉字转换成拼音
				String pinyin = characterParser.getSelling(data.get(i).name);
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					data.get(i).sortLetters = sortString.toUpperCase();
				} else {
					data.get(i).sortLetters = "#";
				}
			}
		}
		return data;
	}

	private class CityAdapter extends BaseAdapter implements SectionIndexer {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cityData.size();
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
			CityBean mContent = cityData.get(position);
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
			viewholder.tv_cityName.setText(cityData.get(position).name);
			if (TextUtils.isEmpty(mCitySeletet)) {
				mCitySeletet = "全部";
			}
			if (mCitySeletet.equals(cityData.get(position).name)) {
				viewholder.tv_cityName.setTextColor(mContext.getResources()
						.getColor(R.color.red_d30549));
			} else {
				viewholder.tv_cityName.setTextColor(mContext.getResources()
						.getColor(R.color.gray_aaaaaa));
			}
			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);
			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				viewholder.tv_title.setVisibility(View.VISIBLE);
				viewholder.tv_title.setText(mContent.sortLetters);
			} else {
				viewholder.tv_title.setVisibility(View.GONE);

			}
			if (position < cityData.size() - 1) {
				int section2 = getSectionForPosition(position + 1);
				if (section != section2) {
					viewholder.v_line.setVisibility(View.GONE);
				} else {
					viewholder.v_line.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}

		/**
		 * 根据ListView的当前位置获取分类的首字母的Char ascii值
		 */
		public int getSectionForPosition(int position) {
			if (!TextUtils.isEmpty(cityData.get(position).sortLetters)) {
				return cityData.get(position).sortLetters.charAt(0);
			} else {
				String letter = "#";
				return letter.charAt(0);
			}

		}

		/**
		 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
		 */
		public int getPositionForSection(int section) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = cityData.get(i).sortLetters;
				if (sortStr == null || sortStr.length() == 0) {
					continue;
				}
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}

			return -1;
		}

		/**
		 * 提取英文的首字母，非英文字母用#代替。
		 * 
		 * @param str
		 * @return
		 */
		private String getAlpha(String str) {
			String sortStr = str.trim().substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortStr.matches("[A-Z]")) {
				return sortStr;
			} else {
				return "#";
			}
		}

		@Override
		public Object[] getSections() {
			return null;
		}
	}

	public class ViewHolder {

		private TextView tv_cityName;
		private TextView tv_title;
		private View v_line;
	}
}
