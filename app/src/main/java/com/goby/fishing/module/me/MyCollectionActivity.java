package com.goby.fishing.module.me;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.util.RadioButtonGroup;
import com.goby.fishing.framework.AbsBaseActivity;

public class MyCollectionActivity extends AbsBaseActivity implements
		OnClickListener {

	private RadioButtonGroup rdoBtnGrp;
	// radio btn
	private RadioButton rb_all;
	private RadioButton rb_hot;
	private RadioButton rb_fish;
	// Line view
	private ImageView iv_all;
	private ImageView iv_hot;
	private ImageView iv_fish;
	private ImageView iv_back;
	public static ViewPager vPager;
	private CircleFragmentsAdapter mAdapter;
	private LinearLayout ll_leftBack;

	public static void launch(Activity activity) {
		Intent intent = new Intent(activity, MyCollectionActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);

		initView();
	}

	public void initView() {
		initSubFragments();
		iv_back = (ImageView) findViewById(R.id.left_back);
		iv_back.setOnClickListener(this);
		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(this);
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

	private void initSubFragments() {
		initFragments();
		initTabs();
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
			switch (index) {
			case 0:
				iv_all.setVisibility(View.VISIBLE);
				iv_hot.setVisibility(View.GONE);
				iv_fish.setVisibility(View.GONE);
				break;
			case 1:

				iv_all.setVisibility(View.GONE);
				iv_hot.setVisibility(View.VISIBLE);
				iv_fish.setVisibility(View.GONE);
				break;
			case 2:

				iv_all.setVisibility(View.GONE);
				iv_hot.setVisibility(View.GONE);
				iv_fish.setVisibility(View.VISIBLE);
				break;
			}
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
	private void initFragments() {
		vPager = (ViewPager) findViewById(R.id.vpager_circle);
		ArrayList<String> fNames = new ArrayList<String>();
		fNames.add(MyFishingInfoFragment.class.getName());
		fNames.add(MyFishingFragment.class.getName());
		fNames.add(MyFooterprintFragment.class.getName());
		mAdapter = new CircleFragmentsAdapter(this,
				getSupportFragmentManager(), fNames);
		vPager.setAdapter(mAdapter);

		vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				setCurrentTag(index);
				switch (index) {
				case 0:
					iv_all.setVisibility(View.VISIBLE);
					iv_hot.setVisibility(View.GONE);
					iv_fish.setVisibility(View.GONE);
					break;
				case 1:

					iv_all.setVisibility(View.GONE);
					iv_hot.setVisibility(View.VISIBLE);
					iv_fish.setVisibility(View.GONE);
					break;
				case 2:

					iv_all.setVisibility(View.GONE);
					iv_hot.setVisibility(View.GONE);
					iv_fish.setVisibility(View.VISIBLE);
					break;
				}
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
	private void initTabs() {
		// tab bar
		rb_all = (RadioButton) findViewById(R.id.all_rdobtn);
		rb_hot = (RadioButton) findViewById(R.id.hot_rdobtn);
		rb_fish = (RadioButton) findViewById(R.id.fish_rdobtn);

		iv_all = (ImageView) findViewById(R.id.all_line);
		iv_hot = (ImageView) findViewById(R.id.hot_line);
		iv_fish = (ImageView) findViewById(R.id.fish_line);

		rb_all.setClickable(false);
		rb_hot.setClickable(false);
		rb_fish.setClickable(false);

		rdoBtnGrp = new RadioButtonGroup();
		rdoBtnGrp.addRadioButton(rb_all);
		rdoBtnGrp.addRadioButton(rb_hot);
		rdoBtnGrp.addRadioButton(rb_fish);

		OnTabsClickedListener listener = new OnTabsClickedListener();
		findViewById(R.id.all_layout).setOnClickListener(listener);
		findViewById(R.id.hot_layout).setOnClickListener(listener);
		findViewById(R.id.fish_layout).setOnClickListener(listener);
	}

	// Tabs related
	class OnTabsClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_layout:
				onAllTabClicked();
				break;
			case R.id.hot_layout:
				onSkillTabClicked();
				break;
			case R.id.fish_layout:
				onRemarkTabClicked();
				break;
			}
		}

		private void onAllTabClicked() {
			setCurrentTab(0);
		}

		private void onSkillTabClicked() {
			setCurrentTab(1);
		}

		private void onRemarkTabClicked() {
			setCurrentTab(2);
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
