package com.goby.fishing.module.index;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.TextView;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.goby.fishing.common.utils.helper.android.util.RadioButtonGroup;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.me.MyCollectionActivity;
import com.ta.utdid2.android.utils.SharedPreferenceHelper;

public class GameActivity extends AbsBaseActivity {

    private RadioButtonGroup rdoBtnGrp;
    // radio btn
    private RadioButton rb_live;
    private RadioButton rb_feed;
    public static ViewPager vPager;
    private CircleFragmentsAdapter mAdapter;
    private LinearLayout ll_leftBack;
    private TextView tv_sign;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, GameActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
    }

    public void initView() {
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tv_sign = (TextView) findViewById(R.id.sign_btn);
        tv_sign.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UserBusinessController.getInstance().sign(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", "daily", new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("签到中，请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();

                        ToastHelper.showToast(GameActivity.this, "鱼票已领，您获得了100鱼票");

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(GameActivity.this, msg);
                    }
                });
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
    @SuppressWarnings("deprecation")
    private void initFragments() {
        vPager = (ViewPager) findViewById(R.id.vpager_circle);
        ArrayList<String> fNames = new ArrayList<String>();
        fNames.add(LiveFragment.class.getName());
        fNames.add(FeedFragment.class.getName());
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
        rb_live = (RadioButton) findViewById(R.id.live_rdobtn);
        rb_feed = (RadioButton) findViewById(R.id.feed_rdobtn);

        rb_live.setClickable(false);
        rb_feed.setClickable(false);

        rdoBtnGrp = new RadioButtonGroup();
        rdoBtnGrp.addRadioButton(rb_live);
        rdoBtnGrp.addRadioButton(rb_feed);

        OnTabsClickedListener listener = new OnTabsClickedListener();
        findViewById(R.id.live_layout).setOnClickListener(listener);
        findViewById(R.id.feed_layout).setOnClickListener(listener);
    }

    // Tabs related
    class OnTabsClickedListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_layout:
                    onAllTabClicked();
                    break;
                case R.id.feed_layout:
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