package com.goby.fishing.common.utils.helper.android.util;

import java.util.ArrayList;
import java.util.List;

import com.goby.fishing.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TgDateTimePicker {

	private Activity mContext;

	private int year = 1950;
	private int month = 1;
	private int day = 1;

	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;

	private List<String> data_year;
	private List<String> data_month;
	private List<String> data_day;

	private DialogBuilder dialog;

	private OnDateTimeSelectListener listener;

	public TgDateTimePicker(Activity context) {

		mContext = context;

		intDateView();
	}

	public TgDateTimePicker(Activity context, int year, int month, int day) {

		mContext = context;
		this.year = year;
		this.month = month;
		this.day = day;

		intDateView();
	}

	private void intDateView() {

		initData();

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_datetime_picker, null);
		wv_year = (WheelView) view.findViewById(R.id.wv_year);
		wv_month = (WheelView) view.findViewById(R.id.wv_month);
		wv_day = (WheelView) view.findViewById(R.id.wv_day);

		wv_year.setDrawText("年");
		wv_month.setDrawText("月");
		wv_day.setDrawText("日");

		DateTimeAdapter adapter_year = new DateTimeAdapter(mContext, data_year);
		DateTimeAdapter adapter_month = new DateTimeAdapter(mContext,
				data_month);
		DateTimeAdapter adapter_day = new DateTimeAdapter(mContext, data_day);

		wv_year.setAdapter(adapter_year);
		wv_month.setAdapter(adapter_month);
		wv_day.setAdapter(adapter_day);

		wv_month.setOnSelectItemListener(new WheelView.OnSelectItemListener() {

			@Override
			public void onSelectItem(String item) {

				try {

					int value = Integer.parseInt(item);
					if (value != month) {

						month = value;
					}

					int days = getDays(year, month);

					if (day > 28 || data_day.size() != days) {
						data_day = new ArrayList<String>();
						for (int i = 1; i < days + 1; i++) {
							data_day.add(i + "");
						}

						day = 1;

						DateTimeAdapter adapter_day = new DateTimeAdapter(
								mContext, data_day);
						wv_day.setAdapter(adapter_day);
						wv_day.setDefaultItem(day + "");
					}

				} catch (Exception e) {

				}
			}
		});

		wv_day.setOnSelectItemListener(new WheelView.OnSelectItemListener() {

			@Override
			public void onSelectItem(String item) {

				try {

					int value = Integer.parseInt(item);
					day = value;

				} catch (Exception e) {

				}
			}
		});
		wv_year.setOnSelectItemListener(new WheelView.OnSelectItemListener() {

			@Override
			public void onSelectItem(String item) {

				try {

					int value = Integer.parseInt(item);
					year = value;

					int days = getDays(year, month);

					if (day > 28 || data_day.size() != days) {
						data_day = new ArrayList<String>();
						for (int i = 1; i < days + 1; i++) {
							data_day.add(i + "");
						}

						day = 1;

						DateTimeAdapter adapter_day = new DateTimeAdapter(
								mContext, data_day);
						wv_day.setAdapter(adapter_day);
						wv_day.setDefaultItem(day + "");
					}

				} catch (Exception e) {

				}
			}
		});

		wv_year.setDefaultItem(year + "");
		wv_month.setDefaultItem(month + "");
		wv_day.setDefaultItem(day + "");

		TextView tv_cancel = (TextView) view.findViewById(R.id.cancel_text);
		TextView tv_sure = (TextView) view.findViewById(R.id.sure_text);

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (null != listener)
					if (null != listener)
						listener.onSelect(year, month, day);
			}
		});

		dialog = new DialogBuilder(mContext, "center");
		dialog.setTitle("选择日期");
		dialog.setView(view);
		dialog.create().show();

		// DialogBuilder.show(mContext, "选择日期", view, "确定", "取消", new
		// MaterialDialog.ButtonCallback() {
		// @Override
		// public void onPositive(MaterialDialog dialog) {
		// super.onPositive(dialog);
		// if (null != listener)
		// if(null != listener)
		// listener.onSelect(year, month, day);
		// }
		//
		//
		// });
	}

	private void initData() {
		long time = System.currentTimeMillis();
		String yearS = TimeUtil.getYear(time);
		int year = Integer.parseInt(yearS);
		data_year = new ArrayList<String>();
		int size = year - 1950 + 1;
		for (int i = 0; i < size; i++) {
			data_year.add(1950 + i + "");
		}

		data_month = new ArrayList<String>();
		for (int i = 1; i < 13; i++) {
			data_month.add(i + "");
		}

		data_day = new ArrayList<String>();
		int days = getDays(year, month) + 1;
		for (int i = 1; i < days; i++) {
			data_day.add(i + "");
		}
	}

	public void setOnDateTimeSelectListener(OnDateTimeSelectListener listener) {

		this.listener = listener;
	}

	public interface OnDateTimeSelectListener {

		public void onSelect(int year, int month, int day);
	}

	private int getDays(int year, int month) {

		int days = 0;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:

			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				days = 29;
			} else {
				days = 28;
			}
			break;
		}

		return days;
	}
}
