package com.goby.fishing.module.fishing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.baoyz.widget.PullRefreshLayout;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FooterprintListBean;
import com.example.controller.bean.FooterprintListBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.login.LoginActivity;

public class FishPointDynamicListAcitivty extends AbsBaseActivity implements
        OnClickListener {

    private ListView lv_footerPrint;

    private PullRefreshLayout mPullRefreshLayout;

    private LinearLayout ll_error;

    private LinearLayout ll_leftBack;

    private TextView tv_addDynamic;

    private Button btn_reload;

    private FooterprintAdapter adapter;

    private ArrayList<String> imageData;

    private ImageGridAdapter imageAdapter;

    public static ArrayList<List> dataList = new ArrayList<List>();

    private int page = 1;

    private int number = 20;

    public static String active = "init";

    private double latitude = 0, longitude = 0;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    public static void launch(Activity activity, int fishPointId,
                              String fishPointName, double longitude, double latitude) {

        Intent intent = new Intent(activity, FishPointDynamicListAcitivty.class);
        intent.putExtra("fishPointId", fishPointId);
        intent.putExtra("fishPointName", fishPointName);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_point_dynamic_list);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initFooter();
        initView();
        initData();
    }

    public void initView() {

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        ll_error = (LinearLayout) findViewById(R.id.error_layout);
        lv_footerPrint = (ListView) findViewById(R.id.footerprint_list);
        lv_footerPrint.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv_footerPrint.addFooterView(footerView);
        lv_footerPrint.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position - 1 <= dataList.size()) {
                    FooterprintDetailActivity.launch(
                            FishPointDynamicListAcitivty.this,
                            dataList.get(position).id,
                            dataList.get(position).pic_urls.get(0), position,
                            "footerfragment");
                }
            }
        });
        btn_reload = (Button) findViewById(R.id.reload_btn);
        btn_reload.setOnClickListener(this);

        mPullRefreshLayout
                .setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {

                        // 刷新
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                        if (lv_footerPrint.getFooterViewsCount() > 0) {
                            lv_footerPrint.removeFooterView(footerView);
                        }
                        page = 1;
                        active = "refresh";
                        initData();

                    }
                });
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        tv_addDynamic = (TextView) findViewById(R.id.add_dynamic);

        ll_leftBack.setOnClickListener(this);
        tv_addDynamic.setOnClickListener(this);
    }

    public void initData() {
        dataList.clear();
        if (active.equals("init")) {
            showProgressDialog("正在获取数据中,请稍候...");
        }
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
        }
        UserBusinessController.getInstance().getFishFeeds(null, getVersionCode(), "2", getIntent().getIntExtra("fishPointId", 0), page, number, 3, latitude, longitude, new com.example.controller.controller.Listener<FooterprintListBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(FooterprintListBean bean, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                } else if (active.equals("refresh")) {
                    if (lv_footerPrint.getFooterViewsCount() > 0) {
                    } else {
                        lv_footerPrint.addFooterView(footerView);
                    }
                }
                mPullRefreshLayout.setRefreshing(false);
                lv_footerPrint.setVisibility(View.VISIBLE);
                if (active.equals("init") || active.equals("refresh")) {
                    if (bean.data.list.size() == 0) {
                        ToastHelper.showToast(
                                FishPointDynamicListAcitivty.this, "暂无数据");
                    } else if (bean.data.list.size() < 20) {
                        if (lv_footerPrint.getFooterViewsCount() > 0) {
                            try {
                                lv_footerPrint.removeFooterView(footerView);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                        dataList.addAll(bean.data.list);
                    } else {
                        footerView.setVisibility(View.VISIBLE);
                        loadMore.setVisibility(View.VISIBLE);
                        lv_footerPrint
                                .setOnScrollListener(new UpdateListener());
                        dataList.addAll(bean.data.list);
                    }
                    adapter = new FooterprintAdapter();
                    lv_footerPrint.setAdapter(adapter);
                } else if (active.equals("update")) {
                    if (bean.data.list.size() == 0
                            || bean.data.list.size() < 20) {
                        lv_footerPrint.removeFooterView(footerView);
                        ToastHelper.showToast(
                                FishPointDynamicListAcitivty.this, "数据已全部加载完");
                        lv_footerPrint.setOnScrollListener(null);
                    } else {
                        footerView.setVisibility(View.VISIBLE);
                        loadMore.setVisibility(View.VISIBLE);
                        lv_footerPrint
                                .setOnScrollListener(new UpdateListener());
                        loadMore.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String msg, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                } else if (active.equals("refresh")) {
                    mPullRefreshLayout.setRefreshing(false);
                }
                lv_footerPrint.setVisibility(View.GONE);
                ll_error.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 初始化footer
     */
    private void initFooter() {
        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view,
                null);
        loadMore = footerView.findViewById(R.id.load_more);
        // loadMore.setVisibility(View.GONE);
        loading = footerView.findViewById(R.id.loading);
        // loading.setVisibility(View.GONE);
        // mListView.addFooterView(footerView);
        footerView.setVisibility(View.GONE);
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
                    initData();
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
            case R.id.reload_btn:
                ll_error.setVisibility(View.GONE);
                initData();
                break;
            case R.id.add_footerprint:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    LoginActivity.launch(this, "meFragment", 0);
                } else {
                    if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLongitude())) {
                        AddFooterprintActivity.launch(this, "",
                                Double.parseDouble(sharedPreferenceUtil
                                        .getGPSLongitude()), Double
                                        .parseDouble(sharedPreferenceUtil
                                                .getGPSLatitude()), -1);
                    } else {
                        AddFooterprintActivity.launch(this, "", 0, 0, -1);
                    }
                }
                break;
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.add_dynamic:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    LoginActivity.launch(this, "meFragment", 0);
                } else {
                    AddFooterprintActivity.launch(this,
                            getIntent().getStringExtra("fishPointName"),
                            getIntent().getDoubleExtra("longitude", 0), getIntent()
                                    .getDoubleExtra("latitude", 0), getIntent()
                                    .getIntExtra("fishPointId", 0));
                }
                break;
            default:
                break;
        }
    }

    private class FooterprintAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataList.size();
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
                convertView = LayoutInflater.from(
                        FishPointDynamicListAcitivty.this).inflate(
                        R.layout.item_friend_circle, null, false);
                viewholder.iv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_friendCity = (TextView) convertView
                        .findViewById(R.id.city_name);
                viewholder.tv_time = (TextView) convertView
                        .findViewById(R.id.time);
                viewholder.tv_content = (TextView) convertView
                        .findViewById(R.id.content);
                viewholder.gv_infoImage = (GridView) convertView
                        .findViewById(R.id.footer_image_grid);
                viewholder.gv_infoImage.setClickable(false);
                viewholder.gv_infoImage.setPressed(false);
                viewholder.gv_infoImage.setEnabled(false);
                viewholder.gv_infoImage.setSelector(new ColorDrawable(
                        Color.TRANSPARENT));
                viewholder.tv_imageCount = (TextView) convertView
                        .findViewById(R.id.image_count);
                viewholder.tv_fishCount = (TextView) convertView
                        .findViewById(R.id.fish_count);
                viewholder.tv_fishName = (TextView) convertView
                        .findViewById(R.id.fish_name);
                viewholder.tv_fishType = (TextView) convertView
                        .findViewById(R.id.fish_tool);
                viewholder.tv_clound = (TextView) convertView
                        .findViewById(R.id.weather);
                viewholder.tv_distance = (TextView) convertView
                        .findViewById(R.id.distance);
                viewholder.tv_prise = (TextView) convertView
                        .findViewById(R.id.prise);
                viewholder.tv_comment = (TextView) convertView
                        .findViewById(R.id.commends);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.iv_userHeader
                    .setImageResource(R.drawable.header_circle_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    dataList.get(position).head_pic, viewholder.iv_userHeader);
            viewholder.tv_userName.setText(dataList.get(position).user_name);
            if (TextUtils.isEmpty(dataList.get(position).city_name)) {
                viewholder.tv_friendCity.setVisibility(View.GONE);
            } else {
                viewholder.tv_friendCity.setVisibility(View.VISIBLE);
                viewholder.tv_friendCity
                        .setText(dataList.get(position).city_name);
            }
            viewholder.tv_time.setText(TimeUtil.getTimeString(dataList
                    .get(position).time * 1000));
            viewholder.tv_content.setText(dataList.get(position).info);
            if (dataList.get(position).picture_number > 3) {
                viewholder.tv_imageCount.setVisibility(View.VISIBLE);
                viewholder.tv_imageCount
                        .setText(dataList.get(position).picture_number + " 图");
            } else {
                viewholder.tv_imageCount.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(dataList.get(position).fish_info)) {
                viewholder.tv_fishName.setVisibility(View.GONE);
            } else {
                viewholder.tv_fishName.setVisibility(View.VISIBLE);
                viewholder.tv_fishName
                        .setText(dataList.get(position).fish_info);
            }
            if (TextUtils.isEmpty(dataList.get(position).tools)) {
                viewholder.tv_fishType.setVisibility(View.GONE);
            } else {
                viewholder.tv_fishType.setVisibility(View.VISIBLE);
                viewholder.tv_fishType.setText(dataList.get(position).tools);
            }
            if (TextUtils.isEmpty(dataList.get(position).weather)) {
                viewholder.tv_clound.setVisibility(View.GONE);
            } else {
                viewholder.tv_clound.setVisibility(View.VISIBLE);
                viewholder.tv_clound.setText(dataList.get(position).weather);
            }
            if (TextUtils.isEmpty(dataList.get(position).address_info)) {
                viewholder.tv_distance.setVisibility(View.GONE);
            } else {
                viewholder.tv_distance.setVisibility(View.VISIBLE);
                viewholder.tv_distance
                        .setText(dataList.get(position).address_info);
            }
            viewholder.tv_distance.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    BasicMapActivity.launch(FishPointDynamicListAcitivty.this,
                            dataList.get(position).longitude,
                            dataList.get(position).latitude,
                            dataList.get(position).info,
                            dataList.get(position).pic_urls.get(0), "");
                }
            });
            viewholder.tv_prise.setText(dataList.get(position).like_number
                    + " 赞");
            viewholder.tv_comment.setText(dataList.get(position).comment_number
                    + " 评论");
            imageData = new ArrayList<String>();
            imageData.addAll(dataList.get(position).pic_urls);
            imageAdapter = new ImageGridAdapter(imageData);
            viewholder.gv_infoImage.setAdapter(imageAdapter);
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView iv_userHeader;
        private TextView tv_userName;
        private TextView tv_friendCity;
        private TextView tv_time;
        private TextView tv_content;
        private GridView gv_infoImage;
        private TextView tv_imageCount;
        private ImageView iv_footerImage;
        private TextView tv_fishCount;
        private TextView tv_fishName;
        private TextView tv_fishType;
        private TextView tv_clound;
        private TextView tv_distance;
        private TextView tv_prise;
        private TextView tv_comment;
    }

    private class ImageGridAdapter extends BaseAdapter {

        private ArrayList<String> mImageList;

        public ImageGridAdapter(ArrayList<String> imageList) {
            mImageList = imageList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mImageList.size();
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
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(
                        FishPointDynamicListAcitivty.this).inflate(
                        R.layout.item_footer_image, null, false);
                viewholder.iv_footerImage = (ImageView) convertView
                        .findViewById(R.id.footer_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderWrapper.getDefault().displayImage(
                    imageData.get(position), viewholder.iv_footerImage);
            return convertView;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (active.equals("refresh")) {
            active = "init";
            dataList.clear();
            initData();
        } else if (active.equals("update")) {
            active = "init";
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
