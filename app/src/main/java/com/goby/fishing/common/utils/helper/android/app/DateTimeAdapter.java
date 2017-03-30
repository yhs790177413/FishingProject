package com.goby.fishing.common.utils.helper.android.app;

import java.util.List;

import com.goby.fishing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DateTimeAdapter extends BaseAdapter {

	private List<String> data;
	private Context mContext;

	public DateTimeAdapter(Context context, List<String> data) {

		this.mContext = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size() + 4;
	}

	@Override
	public Object getItem(int position) {

		if (position > 1 && position < data.size() + 2)
			return data.get(position - 2);

		return "";

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.item_ytpicker, null);

		TextView tv = (TextView) convertView.findViewById(R.id.tv_item);

		if (position == 0 || position == 1 || position == data.size() + 2
				|| position == data.size() + 3) {
			tv.setVisibility(View.INVISIBLE);
		} else {
			tv.setVisibility(View.VISIBLE);
		}

		if (position > 1 && position < data.size() + 2) {

			tv.setText(data.get(position - 2));
		}

		return convertView;
	}

}
