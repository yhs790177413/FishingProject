package com.goby.fishing.module.footerprint;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.util.RadioButtonGroup;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.fishing.AddFooterprintActivity;
import com.goby.fishing.module.login.LoginActivity;

public class FeedcircleFragment extends BaseFragment {

	private RadioButtonGroup rdoBtnGrp;
	// radio btn
	private RadioButton rb_hotFeed;
	private RadioButton rb_newFeed;
	public static ViewPager vPager;
	private TextView tv_addFooterprint;
	private TextView tv_rankText;
	private CircleFragmentsAdapter mAdapter;
	private SharedPreferenceUtil sharedPreferenceUtil;

	public static FeedcircleFragment newInstance() {
		return new FeedcircleFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feed_circle, null);

		sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
		initView(view);
		return view;
	}

	public void initView(View view) {
		initSubFragments(view);
		tv_rankText = (TextView) view.findViewById(R.id.rank_text);
		tv_rankText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				OrderActivity.launch(getActivity());
			}
		});
		tv_addFooterprint = (TextView) view
				.findViewById(R.id.add_footerprint_btn);
		tv_addFooterprint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
					LoginActivity.launch(getActivity(), "meFragment", 0);
				} else {
					if (!TextUtils.isEmpty(sharedPreferenceUtil
							.getGPSLongitude())) {
						AddFooterprintActivity.launch(getActivity(), "", Double
								.parseDouble(sharedPreferenceUtil
										.getGPSLongitude()), Double
								.parseDouble(sharedPreferenceUtil
										.getGPSLatitude()), -1);
					} else {
						AddFooterprintActivity.launch(getActivity(), "", 0, 0,
								-1);
					}
				}
			}
		});
	}

	private void initSubFragments(View view) {
		initFragments(view);
		initTabs(view);
		setCurrentTab(0);
	}

	/**
	 * 设置view+radiobutton
	 * 
	 * @param index
	 */
	private void setCurrentTab(int index) {
		setCurrentTag(index);
		setCurrentView(index);
	}

	/**
	 * 只设置radioButton
	 * 
	 * @param index
	 */
	private void setCurrentTag(int index) {
		if (rdoBtnGrp.getCurrentIndex() != index) {
			rdoBtnGrp.setCurrentChecked(index);
		}
	}

	/**
	 * 只设置view
	 * 
	 * @param index
	 */
	public static void setCurrentView(int index) {
		vPager.setCurrentItem(index, true);

	}

	/**
	 * 设置fragment
	 */
	private void initFragments(View view) {
		vPager = (ViewPager) view.findViewById(R.id.vpager_circle);
		ArrayList<String> fNames = new ArrayList<String>();
		fNames.add(FooterPrintFragment.class.getName());
		fNames.add(NewFeedFragment.class.getName());
		mAdapter = new CircleFragmentsAdapter(getActivity(),
				getChildFragmentManager(), fNames);
		vPager.setAdapter(mAdapter);

		vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				setCurrentTag(index);
			}

			@Override
			public void onPageScrolled(int index, float f, int i) {

			}

			@Override
			public void onPageScrollStateChanged(int index) {

			}
		});
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			mScroller.set(vPager, mScroller);
		} catch (Exception e) {

		}

	}

	// Tabs related
	private void initTabs(View view) {
		// tab bar
		rb_hotFeed = (RadioButton) view.findViewById(R.id.hot_feed_rdobtn);
		rb_newFeed = (RadioButton) view.findViewById(R.id.new_feed_rdobtn);

		rb_hotFeed.setClickable(false);
		rb_newFeed.setClickable(false);

		rdoBtnGrp = new RadioButtonGroup();
		rdoBtnGrp.addRadioButton(rb_hotFeed);
		rdoBtnGrp.addRadioButton(rb_newFeed);

		OnTabsClickedListener listener = new OnTabsClickedListener();
		rb_hotFeed.setOnClickListener(listener);
		rb_newFeed.setOnClickListener(listener);
	}

	// Tabs related
	class OnTabsClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.new_feed_rdobtn:
				onSkillTabClicked();
				break;
			case R.id.hot_feed_rdobtn:
				onAllTabClicked();
				break;
			}
		}

		private void onAllTabClicked() {
			setCurrentTab(0);
		}

		private void onSkillTabClicked() {
			setCurrentTab(1);
		}
	}

	// View pager
	class CircleFragmentsAdapter extends FragmentPagerAdapter {

		private Context context = null;
		private ArrayList<Fragment> list = new ArrayList<Fragment>();

		public CircleFragmentsAdapter(Context context, FragmentManager fm,
				ArrayList<String> fNames) {
			super(fm);
			this.context = context;
			init(fNames);
		}

		private void init(ArrayList<String> fNames) {
			int size = fNames.size();
			Bundle args = new Bundle();
			for (int i = 0; i < size; i++) {
				list.add(Fragment.instantiate(context, fNames.get(i)));
			}
		}

		@Override
		public Fragment getItem(int index) {
			return list.get(index);
		}

		@Override
		public int getCount() {
			return list.size();
		}
	}

}
