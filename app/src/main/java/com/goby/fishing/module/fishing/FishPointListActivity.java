package com.goby.fishing.module.fishing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FishingListBean;
import com.example.controller.bean.FishingListBean.Data.List;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class FishPointListActivity extends AbsBaseActivity implements
        OnClickListener, OnItemClickListener {

    private LinearLayout ll_leftBack;

    private ImageView iv_search;

    // private TextView tv_addFishPoint;
    private TextView tv_nextStep;

    private ListView lv_fishPointName;

    private FishPointNameAdapter adapter;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private int page = 1;

    private int number = 20;

    private String active = "init";

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    public static ArrayList<List> dataList = new ArrayList<List>();

    private String fishPointName;

    private int fishPointId;

    public static void launch(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, FishPointListActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishpoint_list);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        initData();
    }

    public void initView() {

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        iv_search = (ImageView) findViewById(R.id.search_btn);
        lv_fishPointName = (ListView) findViewById(R.id.fish_point_list);
        // tv_addFishPoint = (TextView) findViewById(R.id.add_fish_point);
        tv_nextStep = (TextView) findViewById(R.id.next_step);
        adapter = new FishPointNameAdapter();
        lv_fishPointName.setAdapter(adapter);

        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view,
                null);
        loadMore = footerView.findViewById(R.id.load_more);
        loading = footerView.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        ll_leftBack.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        lv_fishPointName.setOnItemClickListener(this);
        tv_nextStep.setOnClickListener(this);
        // tv_addFishPoint.setOnClickListener(this);
    }

    public void initData() {
        double latitude = 0, longitude = 0;
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
        }
        UserBusinessController.getInstance().fishingListJson(null, getVersionCode(), "2", 0, 0, page, number, 0, latitude, longitude, new com.example.controller.controller.Listener<FishingListBean>() {
            @Override
            public void onStart(Object... params) {
                if (active.equals("init")) {
                    showProgressDialog("正在搜索中,请稍候...");
                }
            }

            @Override
            public void onComplete(FishingListBean bean, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                }


                if (active.equals("init")) {
                    if (bean.data.list.size() < 20) {
                        ToastHelper.showToast(FishPointListActivity.this,
                                "数据全部加载完");
                    } else if (bean.data.list.size() >= 20) {
                        lv_fishPointName.addFooterView(footerView);
                        lv_fishPointName
                                .setOnScrollListener(new UpdateListener());
                    } else if (bean.data.list.size() == 0) {
                        ToastHelper.showToast(FishPointListActivity.this,
                                "暂无数据");
                    }
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                } else if (active.equals("update")) {
                    if (bean.data.list.size() < 20
                            || bean.data.list.size() == 0) {
                        ToastHelper.showToast(FishPointListActivity.this,
                                "数据全部加载完");
                        lv_fishPointName.setOnScrollListener(null);
                        lv_fishPointName.removeFooterView(footerView);
                    }
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                }
                ToastHelper.showToast(FishPointListActivity.this, msg);
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
                    initData();
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }


    private class FishPointNameAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(FishPointListActivity.this)
                        .inflate(R.layout.item_fishpoint_name, null, false);
                viewholder.tv_fishingName = (TextView) convertView
                        .findViewById(R.id.fishpoint_name);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.tv_fishingName.setText(dataList.get(position).name);
            return convertView;
        }
    }

    public class ViewHolder {

        private TextView tv_fishingName;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // TODO Auto-generated method stub
        // Intent intent = new Intent();
        fishPointName = dataList.get(position).name;
        fishPointId = dataList.get(position).id;
        // setResult(RESULT_OK, intent);
        FishPointInfoActivity.launch(this, 1002,
                dataList.get(position).longitude,
                dataList.get(position).latitude);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.search_btn:
                SearchFishPointActivity.launch(this, 1001);
                break;
            // case R.id.add_fish_point:
            // AddNewFishPointActivity.launch(this, 1003);
            // break;
            case R.id.next_step:
                fishPointName = "";
                fishPointId = 0;
                if (TextUtils.isEmpty(sharedPreferenceUtil.getGPSLongitude())) {
                    FishPointInfoActivity.launch(this, 1002, 0, 0);
                } else {
                    FishPointInfoActivity.launch(this, 1002, Double
                                    .parseDouble(sharedPreferenceUtil.getGPSLongitude()),
                            Double.parseDouble(sharedPreferenceUtil
                                    .getGPSLatitude()));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, data);
        if (data != null) {
            Intent intent;
            switch (arg0) {
                case 1001:
                    intent = new Intent();
                    intent.putExtra("fishPointName",
                            data.getStringExtra("fishPointName"));
                    intent.putExtra("fishPointId",
                            data.getIntExtra("fishPointId", 0));
                    intent.putExtra("fishPointInfo",
                            data.getStringExtra("fishPointInfo"));
                    intent.putExtra("chooseLongitude",
                            data.getDoubleExtra("chooseLongitude", 0));
                    intent.putExtra("chooseLatitude",
                            data.getDoubleExtra("chooseLatitude", 0));
                    intent.putExtra("quality", data.getIntExtra("quality", 0));
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 1002:
                    intent = new Intent();
                    intent.putExtra("fishPointName", fishPointName);
                    intent.putExtra("fishPointId", fishPointId);
                    intent.putExtra("fishPointInfo",
                            data.getStringExtra("fishPointInfo"));
                    intent.putExtra("chooseLongitude",
                            data.getDoubleExtra("chooseLongitude", 0));
                    intent.putExtra("chooseLatitude",
                            data.getDoubleExtra("chooseLatitude", 0));
                    intent.putExtra("quality", data.getIntExtra("quality", 0));
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 1003:
                    intent = new Intent();
                    intent.putExtra("fishPointName",
                            data.getStringExtra("fishPointName"));
                    setResult(RESULT_OK, intent);
                    finish();
            }
        }
    }
}
