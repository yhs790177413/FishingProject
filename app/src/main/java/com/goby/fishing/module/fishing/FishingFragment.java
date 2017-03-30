package com.goby.fishing.module.fishing;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.model.Text;
import com.baoyz.widget.PullRefreshLayout;
import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.fishing.R;
import com.goby.fishing.application.FishingApplication;
import com.example.controller.bean.FishCityListBean;
import com.example.controller.bean.FishingInfoBean;
import com.example.controller.bean.FishingListBean;
import com.example.controller.bean.FishCityListBean.ParentData.CityBean;
import com.example.controller.bean.FishingListBean.Data.List;
import com.goby.fishing.common.utils.helper.android.util.DialogBuilder;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.login.LoginActivity;
import com.goby.fishing.popuwindow.ShowOrderPopuWindow;
import com.goby.fishing.popuwindow.ShowProvincePopuWindow;
import com.goby.fishing.popuwindow.ShowWaterPopuWindow;
import com.google.gson.Gson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FishingFragment extends BaseFragment implements
        OnClickListener {

    private ListView lv_fishing;

    private PullRefreshLayout mPullRefreshLayout;

    private FishingAdapter adapter;

    public static ArrayList<List> dataList = new ArrayList<List>();

    public static String active = "init";

    private LinearLayout ll_city, ll_waters, ll_order;

    private TextView tv_city, tv_waters, tv_order;

    private ImageView iv_city, iv_waters, iv_order;

    private FishingFragmentUiHander uiHandler;

    private ShowProvincePopuWindow poupuWindow;

    private ShowWaterPopuWindow waterPoupuWindow;

    private ShowOrderPopuWindow orderPoupuWindow;

    private LinearLayout popLayout;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private int water_type = 0; // 1--淡水，2--海水

    private int cityNo = 0;

    private int sort = 0; // 0--时间，1--距离，2--评论热度

    private int page = 1;

    private int number = 20;

    private TextView tv_locationAddress;

    private String city_selete, water_selete, order_selete;

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    private LinearLayout ll_error;

    private Button btn_reload;

    private TextView tv_allPointMap;

    private TextView tv_search;

    private RelativeLayout fl_gpsAddress;

    private RelativeLayout rl_searchLayoutOne;

    private ImageView iv_searchOne;

    private ImageView iv_refreshLocation;

    private RelativeLayout rl_searchLayoutTwo;

    private TextView tv_searchBtn;

    private TextView tv_back;

    private EditText et_search;

    private boolean search = false;

    private boolean isShowing = true;

    private double latitude = 0, longitude = 0;

    public final static int PROVINCE = 0X01;
    public final static int WATER = 0X02;
    public final static int ORDER = 0X03;
    public final static int PROVINCE_DISMISS = 0X04;
    public final static int WATER_DISMISS = 0X05;
    public final static int ORDER_DISMISS = 0X06;

    public static FishingFragment newInstance() {
        return new FishingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fishing, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getCityText())) {
            city_selete = sharedPreferenceUtil.getCityText();
            cityNo = sharedPreferenceUtil.getCityNo();
        }
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getWaterSelete())) {
            water_selete = sharedPreferenceUtil.getWaterSelete();
            water_type = sharedPreferenceUtil.getWaterType();
        }
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getOrderSelete())) {
            order_selete = sharedPreferenceUtil.getOrderSelete();
            sort = sharedPreferenceUtil.getSort();
        }
        initFooter();
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

        mPullRefreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        et_search = (EditText) view.findViewById(R.id.search_edit);
        tv_back = (TextView) view.findViewById(R.id.back);
        rl_searchLayoutTwo = (RelativeLayout) view
                .findViewById(R.id.search_layout_two);
        tv_searchBtn = (TextView) view.findViewById(R.id.search_text);
        iv_searchOne = (ImageView) view.findViewById(R.id.search_one_view);
        rl_searchLayoutOne = (RelativeLayout) view
                .findViewById(R.id.search_layout_one);
        tv_allPointMap = (TextView) view.findViewById(R.id.all_point_map);
        tv_search = (TextView) view.findViewById(R.id.search_point);
        fl_gpsAddress = (RelativeLayout) view
                .findViewById(R.id.gps_address_layout);
        popLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        ll_error = (LinearLayout) view.findViewById(R.id.error_layout);
        ll_city = (LinearLayout) view.findViewById(R.id.title_city_layout);
        ll_waters = (LinearLayout) view.findViewById(R.id.title_waters_layout);
        ll_order = (LinearLayout) view.findViewById(R.id.title_order_layout);

        tv_city = (TextView) view.findViewById(R.id.city_text);
        tv_waters = (TextView) view.findViewById(R.id.waters_text);
        tv_order = (TextView) view.findViewById(R.id.order_text);
        if (!TextUtils.isEmpty(city_selete)) {
            tv_city.setText(city_selete);
        }
        if (!TextUtils.isEmpty(water_selete)) {
            tv_waters.setText(water_selete);
        }
        if (!TextUtils.isEmpty(order_selete)) {
            tv_order.setText(order_selete);
        }
        tv_locationAddress = (TextView) view.findViewById(R.id.address);
        tv_locationAddress.setText(sharedPreferenceUtil.getLocationAddress());

        iv_city = (ImageView) view.findViewById(R.id.image_city);
        iv_waters = (ImageView) view.findViewById(R.id.image_waters);
        iv_order = (ImageView) view.findViewById(R.id.image_order);

        lv_fishing = (ListView) view.findViewById(R.id.fishing_list);
        // adapter = new FishingAdapter();
        lv_fishing.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv_fishing.addFooterView(footerView);
        // lv_fishing.setAdapter(adapter);
        iv_refreshLocation = (ImageView) view
                .findViewById(R.id.refresh_location_view);

        ll_city.setOnClickListener(this);
        ll_waters.setOnClickListener(this);
        ll_order.setOnClickListener(this);
        tv_allPointMap.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        iv_searchOne.setOnClickListener(this);
        tv_searchBtn.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        iv_refreshLocation.setOnClickListener(this);

        uiHandler = new FishingFragmentUiHander();

        lv_fishing.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position < dataList.size()) {
                    FishingDetailActivity.launch(getActivity(),
                            dataList.get(position).id, position,
                            "fishing_fragment", dataList.get(position).pic_url);
                }
            }
        });
        btn_reload = (Button) view.findViewById(R.id.reload_btn);
        btn_reload.setOnClickListener(this);

        mPullRefreshLayout
                .setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        // 刷新
                        if (lv_fishing.getFooterViewsCount() > 0) {
                            lv_fishing.removeFooterView(footerView);
                        }
                        if (search) {
                            dataList.clear();
                            loadMore.setVisibility(View.GONE);
                            active = "refresh";
                            if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
                                latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
                                longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
                            }
                            UserBusinessController.getInstance().searchFishPoints(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", 1, 20, et_search.getText().toString().trim(),
                                    latitude, longitude, new com.example.controller.controller.Listener<FishingListBean>() {
                                        @Override
                                        public void onStart(Object... params) {

                                        }

                                        @Override
                                        public void onComplete(FishingListBean bean, Object... params) {
                                            mPullRefreshLayout.setRefreshing(false);
                                            if (search) {
                                                dismissProgressDialog();

                                                    lv_fishing.setVisibility(View.VISIBLE);
                                                    if (active.endsWith("refresh")) {
                                                        dataList.clear();
                                                    }
                                                    if (bean.data.list.size() == 0) {
                                                        lv_fishing.removeFooterView(footerView);
                                                        ToastHelper.showToast(getActivity(), "暂无数据");
                                                    } else if (bean.data.list.size() < 20) {
                                                        lv_fishing.removeFooterView(footerView);
                                                        ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                                        lv_fishing.setOnScrollListener(null);
                                                        dataList.addAll(bean.data.list);
                                                        adapter.notifyDataSetChanged();
                                                    } else {
                                                        if (lv_fishing.getFooterViewsCount() > 0) {
                                                        } else {
                                                            lv_fishing.addFooterView(footerView);
                                                        }
                                                        loadMore.setVisibility(View.VISIBLE);
                                                        lv_fishing.setOnScrollListener(new UpdateListener());
                                                        dataList.addAll(bean.data.list);
                                                        adapter.notifyDataSetChanged();
                                                    }

                                            } else {
                                                if (isShowing) {
                                                    dismissProgressDialog();
                                                }

                                                    lv_fishing.setVisibility(View.VISIBLE);
                                                    if (active.equals("init")) {
                                                        if (bean.data.list.size() < 20) {
                                                            lv_fishing.removeFooterView(footerView);
                                                            ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                                            lv_fishing.setOnScrollListener(null);
                                                        } else {
                                                            if (lv_fishing.getFooterViewsCount() > 0) {
                                                            } else {
                                                                lv_fishing.addFooterView(footerView);
                                                            }
                                                            lv_fishing
                                                                    .setOnScrollListener(new UpdateListener());
                                                        }
                                                        dataList.clear();
                                                        dataList.addAll(bean.data.list);
                                                        adapter = new FishingAdapter();
                                                        lv_fishing.setAdapter(adapter);
                                                        Gson gson = new Gson();
                                                        sharedPreferenceUtil.setFishing(gson.toJson(bean));
                                                    } else if (active.equals("search")
                                                            || active.equals("refresh")
                                                            || active.equals("refreshLocation")) {
                                                        if (TextUtils.isEmpty(sharedPreferenceUtil
                                                                .getGPSLatitude())
                                                                && active.equals("refreshLocation")) {
                                                            ToastHelper
                                                                    .showToast(getActivity(), "获取不到定位,请稍候再试");
                                                        }
                                                        if (bean.data.list.size() == 0) {
                                                            lv_fishing.removeFooterView(footerView);
                                                            ToastHelper.showToast(getActivity(), "暂无数据");
                                                        } else if (bean.data.list.size() < 20) {
                                                            lv_fishing.removeFooterView(footerView);
                                                            ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                                            lv_fishing.setOnScrollListener(null);
                                                            dataList.addAll(bean.data.list);

                                                        } else {
                                                            if (lv_fishing.getFooterViewsCount() > 0) {
                                                            } else {
                                                                lv_fishing.addFooterView(footerView);
                                                            }
                                                            lv_fishing
                                                                    .setOnScrollListener(new UpdateListener());
                                                            dataList.addAll(bean.data.list);
                                                        }
                                                        adapter = new FishingAdapter();
                                                        lv_fishing.setAdapter(adapter);
                                                    } else if (active.equals("update")) {
                                                        if (bean.data.list.size() == 0
                                                                || bean.data.list.size() < 20) {
                                                            lv_fishing.removeFooterView(footerView);
                                                            ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                                            lv_fishing.setOnScrollListener(null);
                                                        } else {
                                                            footerView.setVisibility(View.VISIBLE);
                                                            loadMore.setVisibility(View.VISIBLE);
                                                            lv_fishing
                                                                    .setOnScrollListener(new UpdateListener());
                                                            loadMore.setVisibility(View.VISIBLE);
                                                            loading.setVisibility(View.GONE);
                                                        }
                                                        dataList.addAll(bean.data.list);
                                                        adapter.notifyDataSetChanged();
                                                    }


                                            }

                                        }

                                        @Override
                                        public void onFail(String msg, Object... params) {
                                            dismissProgressDialog();
                                            ToastHelper.showToast(getActivity(),msg);
                                        }
                                    });
                        } else {
                            page = 1;
                            active = "refresh";
                            isShowing = false;
                            getRemote();
                        }

                    }
                });

    }

    /**
     * 初始化footer
     */
    private void initFooter() {
        footerView = LayoutInflater.from(getActivity()).inflate(
                R.layout.footer_view, null);
        loadMore = footerView.findViewById(R.id.load_more);
        loading = footerView.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        // mListView.addFooterView(footerView);
        // footerView.setVisibility(View.GONE);
    }

    public void initData() {
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getFishing())) {

            Gson gson = new Gson();
            FishingListBean mFishingListBean = gson.fromJson(
                    sharedPreferenceUtil.getFishing(), FishingListBean.class);
            dataList.addAll(mFishingListBean.data.list);
            if (dataList.size() < 20) {
                lv_fishing.removeFooterView(footerView);
                lv_fishing.setOnScrollListener(null);
            } else {
                lv_fishing.setOnScrollListener(new UpdateListener());
            }
            isShowing = false;
            lv_fishing.setVisibility(View.VISIBLE);
            adapter = new FishingAdapter();
            lv_fishing.setAdapter(adapter);
        }
        getRemote();
    }

    public void getRemote() {
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
        }
        UserBusinessController.getInstance().fishingListJson(null, getVersionCode(), "2", water_type, cityNo, page,
                number, sort, latitude, longitude, new com.example.controller.controller.Listener<FishingListBean>() {
                    @Override
                    public void onStart(Object... params) {
                        if (isShowing) {
                            showProgressDialog("正在加载数据中,请稍候...");
                        }
                    }

                    @Override
                    public void onComplete(FishingListBean bean, Object... params) {
                        mPullRefreshLayout.setRefreshing(false);
                        if (search) {
                            dismissProgressDialog();

                                lv_fishing.setVisibility(View.VISIBLE);
                                if (active.endsWith("refresh")) {
                                    dataList.clear();
                                }
                                if (bean.data.list.size() == 0) {
                                    lv_fishing.removeFooterView(footerView);
                                    ToastHelper.showToast(getActivity(), "暂无数据");
                                } else if (bean.data.list.size() < 20) {
                                    lv_fishing.removeFooterView(footerView);
                                    ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                    lv_fishing.setOnScrollListener(null);
                                    dataList.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    if (lv_fishing.getFooterViewsCount() > 0) {
                                    } else {
                                        lv_fishing.addFooterView(footerView);
                                    }
                                    loadMore.setVisibility(View.VISIBLE);
                                    lv_fishing.setOnScrollListener(new UpdateListener());
                                    dataList.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                }

                        } else {
                            if (isShowing) {
                                dismissProgressDialog();
                            }

                                lv_fishing.setVisibility(View.VISIBLE);
                                if (active.equals("init")) {
                                    if (bean.data.list.size() < 20) {
                                        lv_fishing.removeFooterView(footerView);
                                        ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                        lv_fishing.setOnScrollListener(null);
                                    } else {
                                        if (lv_fishing.getFooterViewsCount() > 0) {
                                        } else {
                                            lv_fishing.addFooterView(footerView);
                                        }
                                        lv_fishing
                                                .setOnScrollListener(new UpdateListener());
                                    }
                                    dataList.clear();
                                    dataList.addAll(bean.data.list);
                                    adapter = new FishingAdapter();
                                    lv_fishing.setAdapter(adapter);
                                    Gson gson = new Gson();
                                    sharedPreferenceUtil.setFishing(gson.toJson(bean));
                                } else if (active.equals("search")
                                        || active.equals("refresh")
                                        || active.equals("refreshLocation")) {
                                    if (TextUtils.isEmpty(sharedPreferenceUtil
                                            .getGPSLatitude())
                                            && active.equals("refreshLocation")) {
                                        ToastHelper
                                                .showToast(getActivity(), "获取不到定位,请稍候再试");
                                    }
                                    if (bean.data.list.size() == 0) {
                                        lv_fishing.removeFooterView(footerView);
                                        ToastHelper.showToast(getActivity(), "暂无数据");
                                    } else if (bean.data.list.size() < 20) {
                                        lv_fishing.removeFooterView(footerView);
                                        ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                        lv_fishing.setOnScrollListener(null);
                                        dataList.addAll(bean.data.list);

                                    } else {
                                        if (lv_fishing.getFooterViewsCount() > 0) {
                                        } else {
                                            lv_fishing.addFooterView(footerView);
                                        }
                                        lv_fishing
                                                .setOnScrollListener(new UpdateListener());
                                        dataList.addAll(bean.data.list);
                                    }
                                    adapter = new FishingAdapter();
                                    lv_fishing.setAdapter(adapter);
                                } else if (active.equals("update")) {
                                    if (bean.data.list.size() == 0
                                            || bean.data.list.size() < 20) {
                                        lv_fishing.removeFooterView(footerView);
                                        ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                        lv_fishing.setOnScrollListener(null);
                                    } else {
                                        footerView.setVisibility(View.VISIBLE);
                                        loadMore.setVisibility(View.VISIBLE);
                                        lv_fishing
                                                .setOnScrollListener(new UpdateListener());
                                        loadMore.setVisibility(View.VISIBLE);
                                        loading.setVisibility(View.GONE);
                                    }
                                    dataList.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                }


                        }
                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(getActivity(),msg);
                    }
                });
    }


    private class UpdateListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 当不滚动时
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                // 判断是否滚动到底部
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 加载更多
                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    active = "update";
                    page++;
                    isShowing = false;
                    getRemote();
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.title_city_layout:
                Gson gson = new Gson();
                try {
                    ArrayList<CityBean> cityData = gson.fromJson(
                            sharedPreferenceUtil.getFishCityJson(),
                            FishCityListBean.class).data.list;
                    iv_city.setBackgroundResource(R.drawable.arrow_up);
                    poupuWindow = new ShowProvincePopuWindow(getActivity(),
                            popLayout, uiHandler, city_selete);
                } catch (Exception e) {
                    // TODO: handle exception
                    UserBusinessController.getInstance().getCityList(null, getVersionCode(), "2", new com.example.controller.controller.Listener<FishCityListBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在获取数据中,请稍候...");
                        }

                        @Override
                        public void onComplete(FishCityListBean bean, Object... params) {
                            dismissProgressDialog();

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(new Date());
                                long time1 = cal.getTimeInMillis();
                                sharedPreferenceUtil.setFishCityTime(time1);
                                Gson gson = new Gson();
                                String fishCityJson = gson.toJson(bean);
                                sharedPreferenceUtil.setFishCityJson(fishCityJson);

                                iv_city.setBackgroundResource(R.drawable.arrow_up);
                                poupuWindow = new ShowProvincePopuWindow(getActivity(),
                                        popLayout, uiHandler, city_selete);

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            mPullRefreshLayout.setRefreshing(false);
                            if (isShowing) {
                                dismissProgressDialog();
                            }
                        }
                    });
                }
                break;

            case R.id.title_waters_layout:
                iv_waters.setBackgroundResource(R.drawable.arrow_up);
                waterPoupuWindow = new ShowWaterPopuWindow(getActivity(),
                        popLayout, uiHandler, water_selete);
                break;

            case R.id.title_order_layout:
                iv_order.setBackgroundResource(R.drawable.arrow_up);
                orderPoupuWindow = new ShowOrderPopuWindow(getActivity(),
                        popLayout, uiHandler, order_selete);
                break;
            case R.id.reload_btn:
                ll_error.setVisibility(View.GONE);
                isShowing = true;
                getRemote();
                break;
            case R.id.all_point_map:
                // 地图列表
                if (dataList != null && dataList.size() > 0) {
                    AllPointMapActivity.launch(getActivity(), dataList);
                } else {
                    ToastHelper.showToast(getActivity(), "暂无数据");
                }
                break;
            case R.id.search_point:
                // 搜索
                tv_allPointMap.setVisibility(View.GONE);
                tv_search.setVisibility(View.GONE);
                popLayout.setVisibility(View.GONE);
                fl_gpsAddress.setVisibility(View.GONE);
                rl_searchLayoutOne.setVisibility(View.VISIBLE);
                lv_fishing.setVisibility(View.GONE);
                tv_back.setVisibility(View.VISIBLE);
                break;
            case R.id.search_one_view:
                iv_searchOne.setVisibility(View.GONE);
                rl_searchLayoutTwo.setVisibility(View.VISIBLE);
                tv_searchBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.search_text:
                search = true;
                if (TextUtils.isEmpty(et_search.getText().toString().trim())) {
                    ToastHelper.showToast(getActivity(), "请输入关键字再搜索");
                    return;
                }
                InputMethodManager manager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                if (getActivity().getCurrentFocus() != null) {
                    manager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                active = "search";
                if (lv_fishing.getFooterViewsCount() > 0) {
                    lv_fishing.removeFooterView(footerView);
                }
                dataList.clear();
                adapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
                    latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
                    longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
                }
                UserBusinessController.getInstance().searchFishPoints(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", 1, 20, et_search.getText().toString().trim(),
                        latitude, longitude, new com.example.controller.controller.Listener<FishingListBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在获取数据中,请稍候...");
                            }

                            @Override
                            public void onComplete(FishingListBean bean, Object... params) {
                                mPullRefreshLayout.setRefreshing(false);
                                if (search) {
                                    dismissProgressDialog();

                                        lv_fishing.setVisibility(View.VISIBLE);
                                        if (active.endsWith("refresh")) {
                                            dataList.clear();
                                        }
                                        if (bean.data.list.size() == 0) {
                                            lv_fishing.removeFooterView(footerView);
                                            ToastHelper.showToast(getActivity(), "暂无数据");
                                        } else if (bean.data.list.size() < 20) {
                                            lv_fishing.removeFooterView(footerView);
                                            ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                            lv_fishing.setOnScrollListener(null);
                                            dataList.addAll(bean.data.list);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            if (lv_fishing.getFooterViewsCount() > 0) {
                                            } else {
                                                lv_fishing.addFooterView(footerView);
                                            }
                                            loadMore.setVisibility(View.VISIBLE);
                                            lv_fishing.setOnScrollListener(new UpdateListener());
                                            dataList.addAll(bean.data.list);
                                            adapter.notifyDataSetChanged();
                                        }

                                } else {
                                    if (isShowing) {
                                        dismissProgressDialog();
                                    }
                                        lv_fishing.setVisibility(View.VISIBLE);
                                        if (active.equals("init")) {
                                            if (bean.data.list.size() < 20) {
                                                lv_fishing.removeFooterView(footerView);
                                                ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                                lv_fishing.setOnScrollListener(null);
                                            } else {
                                                if (lv_fishing.getFooterViewsCount() > 0) {
                                                } else {
                                                    lv_fishing.addFooterView(footerView);
                                                }
                                                lv_fishing
                                                        .setOnScrollListener(new UpdateListener());
                                            }
                                            dataList.clear();
                                            dataList.addAll(bean.data.list);
                                            adapter = new FishingAdapter();
                                            lv_fishing.setAdapter(adapter);
                                            Gson gson = new Gson();
                                            sharedPreferenceUtil.setFishing(gson.toJson(bean));
                                        } else if (active.equals("search")
                                                || active.equals("refresh")
                                                || active.equals("refreshLocation")) {
                                            if (TextUtils.isEmpty(sharedPreferenceUtil
                                                    .getGPSLatitude())
                                                    && active.equals("refreshLocation")) {
                                                ToastHelper
                                                        .showToast(getActivity(), "获取不到定位,请稍候再试");
                                            }
                                            if (bean.data.list.size() == 0) {
                                                lv_fishing.removeFooterView(footerView);
                                                ToastHelper.showToast(getActivity(), "暂无数据");
                                            } else if (bean.data.list.size() < 20) {
                                                lv_fishing.removeFooterView(footerView);
                                                ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                                lv_fishing.setOnScrollListener(null);
                                                dataList.addAll(bean.data.list);

                                            } else {
                                                if (lv_fishing.getFooterViewsCount() > 0) {
                                                } else {
                                                    lv_fishing.addFooterView(footerView);
                                                }
                                                lv_fishing
                                                        .setOnScrollListener(new UpdateListener());
                                                dataList.addAll(bean.data.list);
                                            }
                                            adapter = new FishingAdapter();
                                            lv_fishing.setAdapter(adapter);
                                        } else if (active.equals("update")) {
                                            if (bean.data.list.size() == 0
                                                    || bean.data.list.size() < 20) {
                                                lv_fishing.removeFooterView(footerView);
                                                ToastHelper.showToast(getActivity(), "数据已全部加载完");
                                                lv_fishing.setOnScrollListener(null);
                                            } else {
                                                footerView.setVisibility(View.VISIBLE);
                                                loadMore.setVisibility(View.VISIBLE);
                                                lv_fishing
                                                        .setOnScrollListener(new UpdateListener());
                                                loadMore.setVisibility(View.VISIBLE);
                                                loading.setVisibility(View.GONE);
                                            }
                                            dataList.addAll(bean.data.list);
                                            adapter.notifyDataSetChanged();
                                        }


                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(getActivity(),msg);
                            }
                        });

                break;
            case R.id.back:
                tv_allPointMap.setVisibility(View.VISIBLE);
                tv_search.setVisibility(View.VISIBLE);
                popLayout.setVisibility(View.VISIBLE);
                fl_gpsAddress.setVisibility(View.VISIBLE);
                rl_searchLayoutOne.setVisibility(View.GONE);
                lv_fishing.setVisibility(View.VISIBLE);
                tv_back.setVisibility(View.GONE);

                iv_searchOne.setVisibility(View.VISIBLE);
                rl_searchLayoutTwo.setVisibility(View.GONE);
                tv_searchBtn.setVisibility(View.GONE);
                et_search.setText(null);
                search = false;
                dataList.clear();
                isShowing = true;
                getRemote();
                break;
            case R.id.refresh_location_view:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (sharedPreferenceUtil.getGPSTag()) {
                        new DialogBuilder(getActivity(), "center")
                                .setMessage("是否允许应用访问gps权限")
                                .setButtons(
                                        "拒绝",
                                        "设置",
                                        new DialogBuilder.OnDialogButtonClickListener() {

                                            @Override
                                            public void onDialogButtonClick(
                                                    Context context,
                                                    DialogBuilder builder,
                                                    Dialog dialog, int dialogId,
                                                    int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                if (which == DialogBuilder.OnDialogButtonClickListener.BUTTON_RIGHT) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(
                                                            Settings.ACTION_SETTINGS);
                                                    startActivity(intent);
                                                    startActivity(intent);
                                                } else {
                                                    dialog.dismiss();
                                                }

                                            }
                                        }).create().show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION},
                                101);
                    }
                } else {
                    // 刷新定位
                    // 启动定位
                    AMapLocationClient mLocationClient = new AMapLocationClient(
                            getActivity());
                    // 设置定位回调监听
                    mLocationClient
                            .setLocationListener(new com.amap.api.location.AMapLocationListener() {

                                @Override
                                public void onLocationChanged(
                                        AMapLocation amapLocation) {
                                    if (amapLocation != null) {
                                        if (amapLocation.getErrorCode() == 0) {
                                            // 定位成功回调信息，设置相关消息
                                            amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
                                            amapLocation.getLatitude();// 获取纬度
                                            amapLocation.getLongitude();// 获取经度
                                            amapLocation.getAccuracy();// 获取精度信息
                                            amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果
                                            amapLocation.getCountry();// 国家信息
                                            amapLocation.getProvince();// 省信息
                                            amapLocation.getCity();// 城市信息
                                            amapLocation.getDistrict();// 城区信息
                                            amapLocation.getRoad();// 街道信息
                                            amapLocation.getCityCode();// 城市编码
                                            amapLocation.getAdCode();// 地区编码
                                            SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(
                                                    FishingApplication.getContext());
                                            sharedPreferenceUtil
                                                    .setLocationAddress(amapLocation
                                                            .getAddress());
                                            sharedPreferenceUtil
                                                    .setGPSCity(amapLocation
                                                            .getCity());
                                            sharedPreferenceUtil.setGPSLatitude(String
                                                    .valueOf(amapLocation
                                                            .getLatitude()));
                                            sharedPreferenceUtil.setGPSLongitude(String
                                                    .valueOf(amapLocation
                                                            .getLongitude()));
                                            // 停止定位服务
                                            if (FishingApplication.mLocationClient != null) {
                                                FishingApplication.mLocationClient
                                                        .stopLocation();
                                            }
                                        } else {
                                            // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                                            Log.e("AmapError",
                                                    "location Error, ErrCode:"
                                                            + amapLocation
                                                            .getErrorCode()
                                                            + ", errInfo:"
                                                            + amapLocation
                                                            .getErrorInfo());
                                        }
                                    }
                                }
                            });
                    // 初始化定位参数
                    AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                    // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                    mLocationOption
                            .setLocationMode(AMapLocationMode.Battery_Saving);
                    // 设置是否返回地址信息（默认返回地址信息）
                    mLocationOption.setNeedAddress(true);
                    // 设置是否只定位一次,默认为false
                    mLocationOption.setOnceLocation(true);
                    // 设置是否强制刷新WIFI，默认为强制刷新
                    mLocationOption.setWifiActiveScan(true);
                    // 设置是否允许模拟位置,默认为false，不允许模拟位置
                    mLocationOption.setMockEnable(false);
                    // 设置定位间隔,单位毫秒,默认为2000ms
                    mLocationOption.setInterval(2000);
                    // 给定位客户端对象设置定位参数
                    mLocationClient.setLocationOption(mLocationOption);
                    // 启动定位
                    mLocationClient.startLocation();

                    tv_locationAddress.setText(sharedPreferenceUtil
                            .getLocationAddress());
                    dataList.clear();
                    page = 1;
                    active = "refreshLocation";
                    isShowing = true;
                    getRemote();
                }
                break;
            default:
                break;
        }
    }

    private class FishingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_fishing, null, false);
                viewholder.icv_fishingImage = (ImageView) convertView
                        .findViewById(R.id.fishing_image);
                viewholder.tv_fishingName = (TextView) convertView
                        .findViewById(R.id.fishing_name);
                viewholder.tv_distance = (TextView) convertView
                        .findViewById(R.id.distance);
                viewholder.tv_info = (TextView) convertView
                        .findViewById(R.id.fishing_info);
                viewholder.tv_comment = (TextView) convertView
                        .findViewById(R.id.comment_count);
                viewholder.tv_footerprintCount = (TextView) convertView
                        .findViewById(R.id.footerprint_count);
                viewholder.tv_weather = (TextView) convertView
                        .findViewById(R.id.weather_text);
                viewholder.tv_temp = (TextView) convertView
                        .findViewById(R.id.temp_text);
                viewholder.iv_temp = (ImageView) convertView
                        .findViewById(R.id.temp_image);
                viewholder.tv_pressure = (TextView) convertView
                        .findViewById(R.id.pressure_text);
                viewholder.iv_pressure = (ImageView) convertView
                        .findViewById(R.id.pressure_image);
                viewholder.ll_weather = (LinearLayout) convertView
                        .findViewById(R.id.weather_layout);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            ImageUtils.getInstance().loadImage(getActivity(),
                    dataList.get(position).pic_url, R.drawable.loadding_icon,
                    viewholder.icv_fishingImage);
            // viewholder.icv_fishingImage.setImageURL(
            // dataList.get(position).pic_url, R.drawable.loadding_icon,
            // false);
            viewholder.tv_fishingName.setText(dataList.get(position).name);
            viewholder.tv_distance.setText(dataList.get(position).distance);
            viewholder.tv_info.setText(dataList.get(position).summary);
            viewholder.tv_comment.setText(dataList.get(position).comment_number
                    + " 评论");
            // view
            if (dataList.get(position).location_number != 0) {
                viewholder.tv_footerprintCount
                        .setText(dataList.get(position).location_number + "个渔获");
                viewholder.tv_footerprintCount
                        .setBackgroundResource(R.drawable.has_footer_print_bg);
            } else {
                viewholder.tv_footerprintCount.setText("添加渔获");
                viewholder.tv_footerprintCount
                        .setBackgroundResource(R.drawable.no_footer_print_bg);
            }
            viewholder.tv_footerprintCount
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            if (dataList.get(position).location_number == 0) {
                                if (TextUtils.isEmpty(sharedPreferenceUtil
                                        .getUserToken())) {
                                    LoginActivity.launch(getActivity(),
                                            "meFragment", 0);
                                } else {
                                    AddFooterprintActivity.launch(
                                            getActivity(),
                                            dataList.get(position).name,
                                            dataList.get(position).longitude,
                                            dataList.get(position).latitude,
                                            dataList.get(position).id);
                                }
                            } else {
                                FishPointDynamicListAcitivty.launch(
                                        getActivity(),
                                        dataList.get(position).id,
                                        dataList.get(position).name,
                                        dataList.get(position).longitude,
                                        dataList.get(position).latitude);
                            }
                        }
                    });
            try {
                if (dataList.get(position).weather != null) {
                    viewholder.ll_weather.setVisibility(View.VISIBLE);
                    viewholder.tv_weather
                            .setText(dataList.get(position).weather.cond);
                    viewholder.tv_temp
                            .setText(dataList.get(position).weather.temp + "°C");
                    if (dataList.get(position).weather.temp_rise == 0) {
                        viewholder.iv_temp.setVisibility(View.GONE);
                    } else if (dataList.get(position).weather.temp_rise == 1) {
                        viewholder.iv_temp.setVisibility(View.VISIBLE);
                        viewholder.iv_temp
                                .setBackgroundResource(R.drawable.weather_up);
                    } else if (dataList.get(position).weather.temp_rise == 2) {
                        viewholder.iv_temp.setVisibility(View.VISIBLE);
                        viewholder.iv_temp
                                .setBackgroundResource(R.drawable.weather_down);
                    }
                    viewholder.tv_pressure
                            .setText(dataList.get(position).weather.pressure
                                    + "Ph");
                    if (dataList.get(position).weather.pressure_rise == 0) {
                        viewholder.iv_pressure.setVisibility(View.GONE);
                    } else if (dataList.get(position).weather.pressure_rise == 1) {
                        viewholder.iv_pressure.setVisibility(View.VISIBLE);
                        viewholder.iv_pressure
                                .setBackgroundResource(R.drawable.weather_up);
                    } else if (dataList.get(position).weather.pressure_rise == 2) {
                        viewholder.iv_pressure.setVisibility(View.VISIBLE);
                        viewholder.iv_pressure
                                .setBackgroundResource(R.drawable.weather_down);
                    }
                } else {
                    viewholder.ll_weather.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                // TODO: handle exception
                viewholder.ll_weather.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

    // private class FishPointImageAdapter extends BaseAdapter {
    //
    // private String mImageUrl;
    //
    // public FishPointImageAdapter(String imageUrl) {
    // mImageUrl = imageUrl;
    // }
    //
    // @Override
    // public int getCount() {
    // // TODO Auto-generated method stub
    // return 1;
    // }
    //
    // @Override
    // public Object getItem(int arg0) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public long getItemId(int arg0) {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // @Override
    // public View getView(int position, View convertView, ViewGroup arg2) {
    // // TODO Auto-generated method stub
    // ViewHolder viewholder;
    // if (convertView == null) {
    // viewholder = new ViewHolder();
    // convertView = LayoutInflater.from(getActivity()).inflate(
    // R.layout.item_fish_point_image, null, false);
    // viewholder.icv_fishingImage = (ImageView) convertView
    // .findViewById(R.id.fishing_image);
    // convertView.setTag(viewholder);
    // } else {
    // viewholder = (ViewHolder) convertView.getTag();
    // }
    //
    // ImageLoaderWrapper.getDefault().displayImage(mImageUrl,
    // viewholder.icv_fishingImage);
    // // viewholder.icv_fishingImage.setImageURL(mImageUrl,
    // // R.drawable.loadding_icon, true);
    // return convertView;
    // }
    //
    // }

    public class ViewHolder {

        private ImageView icv_fishingImage;
        private TextView tv_fishingName;
        private TextView tv_distance;
        private TextView tv_info;
        private TextView tv_comment;
        private TextView tv_footerprintCount;
        private TextView tv_weather;
        private TextView tv_temp;
        private ImageView iv_temp;
        private TextView tv_pressure;
        private ImageView iv_pressure;
        private LinearLayout ll_weather;
    }

    @SuppressLint("HandlerLeak")
    public class FishingFragmentUiHander extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PROVINCE:
                    iv_city.setBackgroundResource(R.drawable.arrow_down);
                    Bundle bundle = (Bundle) msg.obj;
                    city_selete = bundle.getString("citySeletet");
                    cityNo = bundle.getInt("cityNo");
                    tv_city.setText(city_selete);
                    dataList.clear();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    page = 1;
                    lv_fishing.setOnScrollListener(null);
                    sharedPreferenceUtil.setCityText(city_selete);
                    sharedPreferenceUtil.setCityNo(cityNo);
                    isShowing = true;
                    getRemote();
                    break;
                case WATER:
                    iv_waters.setBackgroundResource(R.drawable.arrow_down);
                    water_selete = (String) msg.obj;
                    tv_waters.setText(water_selete);
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                    if (water_selete.equals("全部")) {
                        water_type = 0;
                    } else if (water_selete.equals("湖泊")) {
                        water_type = 3;
                    } else if (water_selete.equals("水库")) {
                        water_type = 4;
                    } else if (water_selete.equals("河流")) {
                        water_type = 1;
                    } else if (water_selete.equals("海洋")) {
                        water_type = 2;
                    }
                    page = 1;
                    lv_fishing.setOnScrollListener(null);
                    sharedPreferenceUtil.setWaterSelete(water_selete);
                    sharedPreferenceUtil.setWaterType(water_type);
                    isShowing = true;
                    getRemote();
                    break;
                case ORDER:
                    iv_order.setBackgroundResource(R.drawable.arrow_down);
                    order_selete = (String) msg.obj;
                    tv_order.setText(order_selete);
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                    if (order_selete.equals("最近")) {
                        sort = 0;
                    } else if (order_selete.equals("最热")) {
                        sort = 1;
                    } else if (order_selete.equals("最新")) {
                        sort = 2;
                    }
                    page = 1;
                    lv_fishing.setOnScrollListener(null);
                    sharedPreferenceUtil.setOrderSelete(order_selete);
                    sharedPreferenceUtil.setSort(sort);
                    isShowing = true;
                    getRemote();
                    break;
                case PROVINCE_DISMISS:
                    iv_city.setBackgroundResource(R.drawable.arrow_down);
                    break;
                case WATER_DISMISS:
                    iv_waters.setBackgroundResource(R.drawable.arrow_down);
                    break;
                case ORDER_DISMISS:
                    iv_order.setBackgroundResource(R.drawable.arrow_down);
                    break;
            }
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (active.equals("update")) {
            active = "init";
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 申请成功
                AMapLocationClient mLocationClient = new AMapLocationClient(
                        getApplicationContext());
                // 设置定位回调监听
                mLocationClient
                        .setLocationListener(new com.amap.api.location.AMapLocationListener() {

                            @Override
                            public void onLocationChanged(
                                    AMapLocation amapLocation) {
                                if (amapLocation != null) {
                                    if (amapLocation.getErrorCode() == 0) {
                                        // 定位成功回调信息，设置相关消息
                                        amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
                                        amapLocation.getLatitude();// 获取纬度
                                        amapLocation.getLongitude();// 获取经度
                                        amapLocation.getAccuracy();// 获取精度信息
                                        amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果
                                        amapLocation.getCountry();// 国家信息
                                        amapLocation.getProvince();// 省信息
                                        amapLocation.getCity();// 城市信息
                                        amapLocation.getDistrict();// 城区信息
                                        amapLocation.getRoad();// 街道信息
                                        amapLocation.getCityCode();// 城市编码
                                        amapLocation.getAdCode();// 地区编码
                                        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(
                                                FishingApplication.getContext());
                                        sharedPreferenceUtil
                                                .setLocationAddress(amapLocation
                                                        .getAddress());
                                        sharedPreferenceUtil
                                                .setGPSCity(amapLocation
                                                        .getCity());
                                        sharedPreferenceUtil.setGPSLatitude(String
                                                .valueOf(amapLocation
                                                        .getLatitude()));
                                        sharedPreferenceUtil.setGPSLongitude(String
                                                .valueOf(amapLocation
                                                        .getLongitude()));
                                        // 停止定位服务
                                        if (FishingApplication.mLocationClient != null) {
                                            FishingApplication.mLocationClient
                                                    .stopLocation();
                                        }
                                    } else {
                                        // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                                        Log.e("AmapError",
                                                "location Error, ErrCode:"
                                                        + amapLocation
                                                        .getErrorCode()
                                                        + ", errInfo:"
                                                        + amapLocation
                                                        .getErrorInfo());
                                    }
                                }
                            }
                        });
                // 初始化定位参数
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption
                        .setLocationMode(AMapLocationMode.Battery_Saving);
                // 设置是否返回地址信息（默认返回地址信息）
                mLocationOption.setNeedAddress(true);
                // 设置是否只定位一次,默认为false
                mLocationOption.setOnceLocation(true);
                // 设置是否强制刷新WIFI，默认为强制刷新
                mLocationOption.setWifiActiveScan(true);
                // 设置是否允许模拟位置,默认为false，不允许模拟位置
                mLocationOption.setMockEnable(false);
                // 设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);
                // 给定位客户端对象设置定位参数
                mLocationClient.setLocationOption(mLocationOption);
                // 启动定位
                mLocationClient.startLocation();
            } else {
                // 设置gps拒绝标志位
                sharedPreferenceUtil.setGPSTag(true);
            }
        }
    }
}
