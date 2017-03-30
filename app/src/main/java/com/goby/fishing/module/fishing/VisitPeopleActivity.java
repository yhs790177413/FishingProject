package com.goby.fishing.module.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.goby.fishing.R;
import com.goby.fishing.framework.AbsBaseActivity;

public class VisitPeopleActivity extends AbsBaseActivity implements
		OnClickListener {

	private ListView lv_visitPeople;

	private VisitAdapter adapter;

	private LinearLayout ll_leftBack;

	public static void launch(Activity activity) {

		Intent intent = new Intent(activity, VisitPeopleActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visit_people);

		initView();
	}

	public void initView() {

		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		lv_visitPeople = (ListView) findViewById(R.id.visit_people_list);
		adapter = new VisitAdapter();
		lv_visitPeople.setAdapter(adapter);

		ll_leftBack.setOnClickListener(this);
	}

	private class VisitAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder viewholder;
			if (convertView == null) {
				viewholder = new ViewHolder();
				convertView = LayoutInflater.from(VisitPeopleActivity.this)
						.inflate(R.layout.item_visit_people, null, false);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}

			return convertView;
		}
	}

	public class ViewHolder {

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_back_layout:
			finish();
			break;

		default:
			break;
		}
	}

}
