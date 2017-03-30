package com.goby.fishing.module.fishing;

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

public class FishAllFooterprintActivity extends AbsBaseActivity implements
		OnClickListener {

	private RadioButtonGroup rdoBtnGrp;
	// radio btn
	private RadioButton rb_attention;
	private RadioButton rb_fans;
	public static ViewPager vPager;
	private CircleFragmentsAdapter mAdapter;
	private ImageView iv_back;
	private LinearLayout ll_leftBack;
	public static int mId;

	public static void launch(Activity activity, int id) {

		Intent intent = new Intent(activity, FishAllFooterprintActivity.class);
		intent.putExtra("id", id);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fish_footerprint);

		mId = getIntent().getIntExtra("id", -1);
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
		fNames.add(FishNewFooterprintFragment.class.getName());
		fNames.add(FishRecommendFooterprintFragment.class.getName());
		mAdapter = new CircleFragmentsAdapter(this,
				getSupportFragmentManager(), fNames);
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
	private void initTabs() {
		// tab bar
		rb_attention = (RadioButton) findViewById(R.id.attention_rdobtn);
		rb_fans = (RadioButton) findViewById(R.id.fans_rdobtn);

		rb_attention.setClickable(false);
		rb_fans.setClickable(false);

		rdoBtnGrp = new RadioButtonGroup();
		rdoBtnGrp.addRadioButton(rb_attention);
		rdoBtnGrp.addRadioButton(rb_fans);

		OnTabsClickedListener listener = new OnTabsClickedListener();
		findViewById(R.id.attention_rdobtn).setOnClickListener(listener);
		findViewById(R.id.fans_rdobtn).setOnClickListener(listener);
	}

	// Tabs related
	class OnTabsClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.attention_rdobtn:
				onAllTabClicked();
				break;
			case R.id.fans_rdobtn:
				onSkillTabClicked();
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
