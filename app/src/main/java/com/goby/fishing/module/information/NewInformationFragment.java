package com.goby.fishing.module.information;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.goby.fishing.application.FishingApplication;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.ChannelListBean;
import com.example.controller.bean.ChannelListBean.ChannelItem;
import com.example.controller.bean.TagsListBean;
import com.example.controller.bean.ActiveListBean.Data.ActiveBean;
import com.example.controller.bean.FishingInfoBean.Data.List;
import com.goby.fishing.common.utils.helper.android.app.Utils;
import com.goby.fishing.common.utils.helper.android.app.edit.ChannelActivity;
import com.goby.fishing.common.utils.helper.android.app.view.ColumnHorizontalScrollView;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.fishing.AddFooterprintActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.google.gson.Gson;
import com.umeng.common.message.Log;

public class NewInformationFragment extends BaseFragment {

    private ColumnHorizontalScrollView mColumnHorizontalScrollView; // 自定义HorizontalScrollView
    private LinearLayout mRadioGroup_content; // 每个标题

    private LinearLayout ll_more_columns; // 右边+号的父布局
    private ImageView button_more_columns; // 标题右边的+号

    private RelativeLayout rl_column; // +号左边的布局：包括HorizontalScrollView和左右阴影部分
    //public ImageView shade_left; // 左阴影部分
    public ImageView shade_right; // 右阴影部分

    public static int columnSelectIndex = 0; // 当前选中的栏目索引
    private int mItemWidth = 0; // Item宽度：每个标题的宽度

    private int mScreenWidth = 0; // 屏幕宽度

    public final static int CHANNELREQUEST = 1; // 请求码
    public final static int CHANNELRESULT = 10; // 返回码

    // tab集合：HorizontalScrollView的数据源
    public static ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    public static HashMap<String, Boolean> uploadMap = new HashMap<String, Boolean>();
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    public static boolean isSetChangel = false;
    private NewsFragmentPagerAdapter mAdapetr;
    private TextView tv_textBtn;
    private ImageView iv_searchBtn;
    private SharedPreferenceUtil sharedPreferenceUtil;
    //private ArrayList<List> infoData = new ArrayList<List>();
    public static ArrayList<ActiveBean> activeData = new ArrayList<ActiveBean>();
    public static ArrayList<com.example.controller.bean.FooterprintListBean.Data.List> feedData = new ArrayList<com.example.controller.bean.FooterprintListBean.Data.List>();
    public static HashMap<String, ArrayList<List>> infoDataMap = new HashMap<String, ArrayList<List>>();
    private Gson gson;
    private ChannelListBean userChannelListBean;


    public static NewInformationFragment newInstance() {
        return new NewInformationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_information, null);
        mScreenWidth = Utils.getWindowsWidth(getActivity());

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initView(view);
        selectTab(0);
        return view;
    }

    private void initView(View view) {
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view
                .findViewById(R.id.mColumnHorizontalScrollView);
        mColumnHorizontalScrollView.setHorizontalFadingEdgeEnabled(false);
        mRadioGroup_content = (LinearLayout) view
                .findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) view
                .findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);
        button_more_columns = (ImageView) view
                .findViewById(R.id.button_more_columns);
        //shade_left = (ImageView) view.findViewById(R.id.shade_left);
        shade_right = (ImageView) view.findViewById(R.id.shade_right);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        //关闭预加载，默认一次只加载一个Fragment
        mViewPager.setOffscreenPageLimit(1);
        tv_textBtn = (TextView) view.findViewById(R.id.text_btn);
        tv_textBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    LoginActivity.launch(getActivity(), "meFragment", 0);
                } else {
                    if (userChannelList.get(columnSelectIndex).name
                            .equals("圈子")) {

                        if (!TextUtils.isEmpty(sharedPreferenceUtil
                                .getGPSLongitude())) {
                            AddFooterprintActivity.launch(getActivity(), "",
                                    Double.parseDouble(sharedPreferenceUtil
                                            .getGPSLongitude()), Double
                                            .parseDouble(sharedPreferenceUtil
                                                    .getGPSLatitude()), -1);
                        } else {
                            AddFooterprintActivity.launch(getActivity(), "", 0,
                                    0, -1);
                        }
                    } else if (userChannelList.get(columnSelectIndex).name
                            .equals("活动")) {
                        UserBusinessController.getInstance().sign(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", "daily", new com.example.controller.controller.Listener<BaseBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("签到中，请稍候...");
                            }

                            @Override
                            public void onComplete(BaseBean bean, Object... params) {
                                dismissProgressDialog();

                                    ToastHelper.showToast(getActivity(), "鱼票已领，您获得了100鱼票");

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(getActivity(), msg);
                            }
                        });
                    }
                }
            }
        });
        // + 号监听
        button_more_columns.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ChannelActivity.launch(getActivity());
            }
        });
        iv_searchBtn = (ImageView) view.findViewById(R.id.search_one_view);
        iv_searchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SearchActivity.launch(getActivity());
            }
        });
        initColumnData();
        setChangelView();
    }

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        initTabColumn();
        initFragment();
    }

    /**
     * 获取Column栏目 数据
     */
    private void initColumnData() {
        if (TextUtils.isEmpty(sharedPreferenceUtil.getUserChannelJson())) {
            for (int i = 0; i < 15; i++) {
                ChannelItem channelItem = new ChannelItem();
                if (i == 0) {
                    channelItem.id = 0;
                    channelItem.name = "全部";
                    channelItem.orderId = 1;
                    channelItem.selected = 1;
                } else if (i == 1) {
                    channelItem.id = 1;
                    channelItem.name = "圈子";
                    channelItem.orderId = 2;
                    channelItem.selected = 1;
                } else if (i == 2) {
                    channelItem.id = 2;
                    channelItem.name = "活动";
                    channelItem.orderId = 3;
                    channelItem.selected = 1;
                } else if (i == 3) {
                    channelItem.id = 132;
                    channelItem.name = "技巧";
                    channelItem.orderId = 4;
                    channelItem.selected = 1;
                } else if (i == 4) {
                    channelItem.id = 141;
                    channelItem.name = "对象鱼";
                    channelItem.orderId = 5;
                    channelItem.selected = 1;
                } else if (i == 5) {
                    channelItem.id = 5;
                    channelItem.name = "饵料";
                    channelItem.orderId = 6;
                    channelItem.selected = 1;
                } else if (i == 6) {
                    channelItem.id = 129;
                    channelItem.name = "窝子";
                    channelItem.orderId = 7;
                    channelItem.selected = 1;
                } else if (i == 7) {
                    channelItem.id = 2;
                    channelItem.name = "野钓";
                    channelItem.orderId = 8;
                    channelItem.selected = 1;
                } else if (i == 8) {
                    channelItem.id = 3;
                    channelItem.name = "鱼坑";
                    channelItem.orderId = 9;
                    channelItem.selected = 1;
                } else if (i == 9) {
                    channelItem.id = 135;
                    channelItem.name = "鲫鱼";
                    channelItem.orderId = 10;
                    channelItem.selected = 1;
                } else if (i == 10) {
                    channelItem.id = 128;
                    channelItem.name = "鲤鱼";
                    channelItem.orderId = 11;
                    channelItem.selected = 1;
                } else if (i == 11) {
                    channelItem.id = 6;
                    channelItem.name = "路亚";
                    channelItem.orderId = 12;
                    channelItem.selected = 1;
                } else if (i == 12) {
                    channelItem.id = 142;
                    channelItem.name = "精华";
                    channelItem.orderId = 13;
                    channelItem.selected = 1;
                } else if (i == 13) {
                    channelItem.id = 1;
                    channelItem.name = "老司机";
                    channelItem.orderId = 14;
                    channelItem.selected = 1;
                } else if (i == 14) {
                    channelItem.id = 144;
                    channelItem.name = "长视频";
                    channelItem.orderId = 15;
                    channelItem.selected = 1;
                }
                userChannelList.add(channelItem);
            }
            gson = new Gson();
            userChannelListBean = new ChannelListBean();
            userChannelListBean.data = userChannelList;
            sharedPreferenceUtil.setUserChannelJson(gson.toJson(userChannelListBean));
        } else {
            userChannelList.clear();
            gson = new Gson();
            ChannelListBean bean = new ChannelListBean();
            bean = gson.fromJson(sharedPreferenceUtil.getUserChannelJson(), ChannelListBean.class);
            userChannelList.addAll(bean.data);
        }
    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth,
                mRadioGroup_content, shade_right, ll_more_columns,
                rl_column);
        for (int i = 0; i < count; i++) {
            uploadMap.put(userChannelList.get(i).name, false);
            LinearLayout.LayoutParams params;
            if (userChannelList.get(i).name.length() <= 2) {
                mItemWidth = mScreenWidth / 8;
            } else if (userChannelList.get(i).name.length() == 3) {
                mItemWidth = mScreenWidth / 6;
            } else if (userChannelList.get(i).name.length() >= 4) {
                mItemWidth = mScreenWidth / 4;
            }
            params = new LinearLayout.LayoutParams(
                    mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setGravity(Gravity.LEFT);
            //columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            columnTextView.setText(userChannelList.get(i).name);
            columnTextView.setTextColor(getResources().getColorStateList(
                    R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }

            // 单击监听
            columnTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v) {
                            localView.setSelected(false);
                        } else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();// 清空
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            NewsFragment newfragment = new NewsFragment().newInstance(
                    userChannelList.get(i).name, userChannelList.get(i).name, userChannelList.get(i).id + "");
            fragments.add(newfragment);
        }

        mAdapetr = new NewsFragmentPagerAdapter(getChildFragmentManager(),
                fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);


    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            columnSelectIndex = position;
            mViewPager.setCurrentItem(position);
            selectTab(position);
            // Toast.makeText(getApplicationContext(),
            // userChannelList.get(position).getName(),
            // Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        if (columnSelectIndex < userChannelList.size()) {
            if (userChannelList.get(columnSelectIndex).name.equals("圈子")) {
                iv_searchBtn.setVisibility(View.GONE);
                tv_textBtn.setVisibility(View.VISIBLE);
                tv_textBtn.setText("发帖子");
            } else if (userChannelList.get(columnSelectIndex).name
                    .equals("活动")) {
                iv_searchBtn.setVisibility(View.GONE);
                tv_textBtn.setVisibility(View.VISIBLE);
                tv_textBtn.setText("领鱼票");
            } else if (userChannelList.get(columnSelectIndex).name
                    .equals("全部")) {
                iv_searchBtn.setVisibility(View.VISIBLE);
                tv_textBtn.setVisibility(View.GONE);
            } else {
                iv_searchBtn.setVisibility(View.VISIBLE);
                tv_textBtn.setVisibility(View.GONE);
            }
            for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                View checkView = mRadioGroup_content.getChildAt(tab_postion);
                int k = checkView.getMeasuredWidth();
                int l = checkView.getLeft();
                int i2 = l + k / 2 - mScreenWidth / 2;
                mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            }
            // 判断是否选中
            for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
                View checkView = mRadioGroup_content.getChildAt(j);
                boolean ischeck;
                if (j == tab_postion) {
                    ischeck = true;
                } else {
                    ischeck = false;
                }
                checkView.setSelected(ischeck);
            }
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isSetChangel) {
            iv_searchBtn.setVisibility(View.VISIBLE);
            tv_textBtn.setVisibility(View.GONE);
            columnSelectIndex = 0;
            isSetChangel = false;
            mAdapetr.setFragments(fragments);
            initColumnData();
            for (int i = 0; i < userChannelList.size(); i++) {
                if (!uploadMap.containsKey(userChannelList.get(i).name)) {
                    uploadMap.put(userChannelList.get(i).name, false);
                }
            }
            setChangelView();
        }
    }

}
