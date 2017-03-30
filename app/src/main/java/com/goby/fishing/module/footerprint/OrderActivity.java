package com.goby.fishing.module.footerprint;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.util.RadioButtonGroup;
import com.goby.fishing.framework.AbsBaseActivity;

public class OrderActivity extends AbsBaseActivity {

	private RadioButtonGroup rdoBtnGrp;
	// radio btn
	private RadioButton rb_all;
	private RadioButton rb_wild_fish;
	private RadioButton rb_black_pit;
	private RadioButton rb_equipment;
	private RadioButton rb_food;
	private RadioButton rb_lures;
	private RadioButton rb_fishing;
	private RadioButton rb_daily;
	// Line view
	private ImageView iv_all;
	private ImageView iv_wild_fish;
	private ImageView iv_black_pit;
	private ImageView iv_equipment;
	private ImageView iv_food;
	private ImageView iv_lures;
	private ImageView iv_fishing;
	private ImageView iv_daily;
	private LinearLayout ll_leftBack;
	public static ViewPager vPager;
	private CircleFragmentsAdapter mAdapter;

	public static void launch(Activity activity) {

		Intent intent = new Intent(activity, OrderActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);

		initView();
	}

	public void initView() {
		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		initSubFragments();
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
				iv_wild_fish.setVisibility(View.GONE);
				iv_black_pit.setVisibility(View.GONE);
				iv_equipment.setVisibility(View.GONE);
				iv_food.setVisibility(View.GONE);
				iv_lures.setVisibility(View.GONE);
				iv_fishing.setVisibility(View.GONE);
				iv_daily.setVisibility(View.GONE);
				break;
			case 1:
				iv_all.setVisibility(View.GONE);
				iv_wild_fish.setVisibility(View.VISIBLE);
				iv_black_pit.setVisibility(View.GONE);
				iv_equipment.setVisibility(View.GONE);
				iv_food.setVisibility(View.GONE);
				iv_lures.setVisibility(View.GONE);
				iv_fishing.setVisibility(View.GONE);
				iv_daily.setVisibility(View.GONE);
				break;
			case 2:
				iv_all.setVisibility(View.GONE);
				iv_wild_fish.setVisibility(View.GONE);
				iv_black_pit.setVisibility(View.VISIBLE);
				iv_equipment.setVisibility(View.GONE);
				iv_food.setVisibility(View.GONE);
				iv_lures.setVisibility(View.GONE);
				iv_fishing.setVisibility(View.GONE);
				iv_daily.setVisibility(View.GONE);
				break;
			case 3:
				iv_all.setVisibility(View.GONE);
				iv_wild_fish.setVisibility(View.GONE);
				iv_black_pit.setVisibility(View.GONE);
				iv_equipment.setVisibility(View.VISIBLE);
				iv_food.setVisibility(View.GONE);
				iv_lures.setVisibility(View.GONE);
				iv_fishing.setVisibility(View.GONE);
				iv_daily.setVisibility(View.GONE);
				break;
			case 4:
				iv_all.setVisibility(View.GONE);
				iv_wild_fish.setVisibility(View.GONE);
				iv_black_pit.setVisibility(View.GONE);
				iv_equipment.setVisibility(View.GONE);
				iv_food.setVisibility(View.VISIBLE);
				iv_lures.setVisibility(View.GONE);
				iv_fishing.setVisibility(View.GONE);
				iv_daily.setVisibility(View.GONE);
				break;
			case 5:
				iv_all.setVisibility(View.GONE);
				iv_wild_fish.setVisibility(View.GONE);
				iv_black_pit.setVisibility(View.GONE);
				iv_equipment.setVisibility(View.GONE);
				iv_food.setVisibility(View.GONE);
				iv_lures.setVisibility(View.VISIBLE);
				iv_fishing.setVisibility(View.GONE);
				iv_daily.setVisibility(View.GONE);
				break;
			case 6:
				iv_all.setVisibility(View.GONE);
				iv_wild_fish.setVisibility(View.GONE);
				iv_black_pit.setVisibility(View.GONE);
				iv_equipment.setVisibility(View.GONE);
				iv_food.setVisibility(View.GONE);
				iv_lures.setVisibility(View.GONE);
				iv_fishing.setVisibility(View.VISIBLE);
				iv_daily.setVisibility(View.GONE);
				break;
			case 7:
				iv_all.setVisibility(View.GONE);
				iv_wild_fish.setVisibility(View.GONE);
				iv_black_pit.setVisibility(View.GONE);
				iv_equipment.setVisibility(View.GONE);
				iv_food.setVisibility(View.GONE);
				iv_lures.setVisibility(View.GONE);
				iv_fishing.setVisibility(View.GONE);
				iv_daily.setVisibility(View.VISIBLE);
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
		fNames.add(AllOrderFragment.class.getName());
		fNames.add(WildFishFragment.class.getName());
		fNames.add(BlackPitFragment.class.getName());
		fNames.add(EquipmentFragment.class.getName());
		fNames.add(FoodFragment.class.getName());
		fNames.add(LuresFragment.class.getName());
		fNames.add(FishingFragment.class.getName());
		fNames.add(DailyFragment.class.getName());
		mAdapter = new CircleFragmentsAdapter(this,
				getSupportFragmentManager(), fNames);
		vPager.setAdapter(mAdapter);

		vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				InputMethodManager manager = (InputMethodManager) OrderActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (OrderActivity.this.getCurrentFocus() != null) {
					manager.hideSoftInputFromWindow(OrderActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				setCurrentTag(index);
				switch (index) {
				case 0:
					iv_all.setVisibility(View.VISIBLE);
					iv_wild_fish.setVisibility(View.GONE);
					iv_black_pit.setVisibility(View.GONE);
					iv_equipment.setVisibility(View.GONE);
					iv_food.setVisibility(View.GONE);
					iv_lures.setVisibility(View.GONE);
					iv_fishing.setVisibility(View.GONE);
					iv_daily.setVisibility(View.GONE);
					break;
				case 1:
					iv_all.setVisibility(View.GONE);
					iv_wild_fish.setVisibility(View.VISIBLE);
					iv_black_pit.setVisibility(View.GONE);
					iv_equipment.setVisibility(View.GONE);
					iv_food.setVisibility(View.GONE);
					iv_lures.setVisibility(View.GONE);
					iv_fishing.setVisibility(View.GONE);
					iv_daily.setVisibility(View.GONE);
					break;
				case 2:
					iv_all.setVisibility(View.GONE);
					iv_wild_fish.setVisibility(View.GONE);
					iv_black_pit.setVisibility(View.VISIBLE);
					iv_equipment.setVisibility(View.GONE);
					iv_food.setVisibility(View.GONE);
					iv_lures.setVisibility(View.GONE);
					iv_fishing.setVisibility(View.GONE);
					iv_daily.setVisibility(View.GONE);
					break;
				case 3:
					iv_all.setVisibility(View.GONE);
					iv_wild_fish.setVisibility(View.GONE);
					iv_black_pit.setVisibility(View.GONE);
					iv_equipment.setVisibility(View.VISIBLE);
					iv_food.setVisibility(View.GONE);
					iv_lures.setVisibility(View.GONE);
					iv_fishing.setVisibility(View.GONE);
					iv_daily.setVisibility(View.GONE);
					break;
				case 4:
					iv_all.setVisibility(View.GONE);
					iv_wild_fish.setVisibility(View.GONE);
					iv_black_pit.setVisibility(View.GONE);
					iv_equipment.setVisibility(View.GONE);
					iv_food.setVisibility(View.VISIBLE);
					iv_lures.setVisibility(View.GONE);
					iv_fishing.setVisibility(View.GONE);
					iv_daily.setVisibility(View.GONE);
					break;
				case 5:
					iv_all.setVisibility(View.GONE);
					iv_wild_fish.setVisibility(View.GONE);
					iv_black_pit.setVisibility(View.GONE);
					iv_equipment.setVisibility(View.GONE);
					iv_food.setVisibility(View.GONE);
					iv_lures.setVisibility(View.VISIBLE);
					iv_fishing.setVisibility(View.GONE);
					iv_daily.setVisibility(View.GONE);
					break;
				case 6:
					iv_all.setVisibility(View.GONE);
					iv_wild_fish.setVisibility(View.GONE);
					iv_black_pit.setVisibility(View.GONE);
					iv_equipment.setVisibility(View.GONE);
					iv_food.setVisibility(View.GONE);
					iv_lures.setVisibility(View.GONE);
					iv_fishing.setVisibility(View.VISIBLE);
					iv_daily.setVisibility(View.GONE);
					break;
				case 7:
					iv_all.setVisibility(View.GONE);
					iv_wild_fish.setVisibility(View.GONE);
					iv_black_pit.setVisibility(View.GONE);
					iv_equipment.setVisibility(View.GONE);
					iv_food.setVisibility(View.GONE);
					iv_lures.setVisibility(View.GONE);
					iv_fishing.setVisibility(View.GONE);
					iv_daily.setVisibility(View.VISIBLE);
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
		rb_wild_fish = (RadioButton) findViewById(R.id.wild_fish_rdobtn);
		rb_black_pit = (RadioButton) findViewById(R.id.black_pit_rdobtn);
		rb_equipment = (RadioButton) findViewById(R.id.equipment_rdobtn);
		rb_food = (RadioButton) findViewById(R.id.food_rdobtn);
		rb_lures = (RadioButton) findViewById(R.id.lures_rdobtn);
		rb_fishing = (RadioButton) findViewById(R.id.fishing_rdobtn);
		rb_daily = (RadioButton) findViewById(R.id.daily_rdobtn);

		iv_all = (ImageView) findViewById(R.id.all_line);
		iv_wild_fish = (ImageView) findViewById(R.id.wild_fish_line);
		iv_black_pit = (ImageView) findViewById(R.id.black_pit_line);
		iv_equipment = (ImageView) findViewById(R.id.equipment_line);
		iv_food = (ImageView) findViewById(R.id.food_line);
		iv_lures = (ImageView) findViewById(R.id.lures_line);
		iv_fishing = (ImageView) findViewById(R.id.fishing_line);
		iv_daily = (ImageView) findViewById(R.id.daily_line);

		rb_all.setClickable(false);
		rb_wild_fish.setClickable(false);
		rb_black_pit.setClickable(false);
		rb_equipment.setClickable(false);
		rb_food.setClickable(false);
		rb_lures.setClickable(false);
		rb_fishing.setClickable(false);
		rb_daily.setClickable(false);

		rdoBtnGrp = new RadioButtonGroup();
		rdoBtnGrp.addRadioButton(rb_all);
		rdoBtnGrp.addRadioButton(rb_wild_fish);
		rdoBtnGrp.addRadioButton(rb_black_pit);
		rdoBtnGrp.addRadioButton(rb_equipment);
		rdoBtnGrp.addRadioButton(rb_food);
		rdoBtnGrp.addRadioButton(rb_lures);
		rdoBtnGrp.addRadioButton(rb_fishing);
		rdoBtnGrp.addRadioButton(rb_daily);

		OnTabsClickedListener listener = new OnTabsClickedListener();
		findViewById(R.id.all_layout).setOnClickListener(listener);
		findViewById(R.id.wild_fish_layout).setOnClickListener(listener);
		findViewById(R.id.black_pit_layout).setOnClickListener(listener);
		findViewById(R.id.equipment_layout).setOnClickListener(listener);
		findViewById(R.id.food_layout).setOnClickListener(listener);
		findViewById(R.id.lures_layout).setOnClickListener(listener);
		findViewById(R.id.fishing_layout).setOnClickListener(listener);
		findViewById(R.id.daily_layout).setOnClickListener(listener);
	}

	// Tabs related
	class OnTabsClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_layout:
				onAllTabClicked();
				break;
			case R.id.wild_fish_layout:
				onWildFishTabClicked();
				break;
			case R.id.black_pit_layout:
				onBlackPitTabClicked();
				break;
			case R.id.equipment_layout:
				onEquipmentTabClicked();
				break;
			case R.id.food_layout:
				onFoodTabClicked();
				break;
			case R.id.lures_layout:
				onLuresTabClicked();
				break;
			case R.id.fishing_layout:
				onFishingTabClicked();
				break;
			case R.id.daily_layout:
				onDailyTabClicked();
				break;
			}
		}

		private void onAllTabClicked() {
			setCurrentTab(0);
		}

		private void onWildFishTabClicked() {
			setCurrentTab(1);
		}

		private void onBlackPitTabClicked() {
			setCurrentTab(2);
		}

		private void onEquipmentTabClicked() {
			setCurrentTab(3);
		}

		private void onFoodTabClicked() {
			setCurrentTab(4);
		}

		private void onLuresTabClicked() {
			setCurrentTab(5);
		}

		private void onFishingTabClicked() {
			setCurrentTab(6);
		}

		private void onDailyTabClicked() {
			setCurrentTab(7);
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
