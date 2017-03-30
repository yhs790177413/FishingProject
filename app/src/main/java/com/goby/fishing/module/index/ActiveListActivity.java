package com.goby.fishing.module.index;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.bumptech.glide.Glide;
import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.fishing.R;
import com.example.controller.bean.ActiveListBean;
import com.example.controller.bean.ActiveListBean.Data.ActiveBean;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class ActiveListActivity extends AbsBaseActivity implements
        OnClickListener {

    private ListView mInfoList;

    private InfoAdapter adapter;

    public static ArrayList<ActiveBean> infoData = new ArrayList<ActiveBean>();

    private int page = 1;

    private int number = 20;

    private View footerView;

    private View loadMore; // 加载更多的view

    private View loading; // 加载进度条

    private String active = "init";

    private TextView tv_centerTitle;

    private LinearLayout ll_leftBack;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ActiveListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_infos);

        initFooter();
        initView();
        getRemote();
    }

    public void initView() {

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        tv_centerTitle = (TextView) findViewById(R.id.center_title);
        tv_centerTitle.setText("活动");
        mInfoList = (ListView) findViewById(R.id.info_list);
        adapter = new InfoAdapter();
        mInfoList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mInfoList.addFooterView(footerView);
        mInfoList.setAdapter(adapter);
        mInfoList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position < infoData.size()) {
                    ActiveDetailActivity.launch(ActiveListActivity.this,
                            infoData.get(position).id + "");
                }
            }
        });
        ll_leftBack.setOnClickListener(this);
    }

    /**
     * 初始化footer
     */
    private void initFooter() {
        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view,
                null);
        loadMore = footerView.findViewById(R.id.load_more);
        loading = footerView.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
    }

    public void getRemote() {
        UserBusinessController.getInstance().getActivitysJson(null, getVersionCode(), "2", page, number, new com.example.controller.controller.Listener<ActiveListBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("获取数据中,请稍候...");
            }

            @Override
            public void onComplete(ActiveListBean bean, Object... params) {
                dismissProgressDialog();

                if (active.equals("init")) {
                    if (bean.data.list.size() < 20) {
                        mInfoList.removeFooterView(footerView);
                        mInfoList.setOnScrollListener(null);
                    } else {
                        mInfoList.setOnScrollListener(new UpdateListener());
                    }
                    infoData.clear();
                    infoData.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                } else if (active.equals("update")) {
                    if (bean.data.list.size() < 20) {
                        mInfoList.removeFooterView(footerView);
                        mInfoList.setOnScrollListener(null);
                    } else {
                        mInfoList.setOnScrollListener(new UpdateListener());
                    }
                    infoData.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(ActiveListActivity.this, msg);
            }
        });
    }

    private class UpdateListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 当不滚动时

            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                Glide.with(ActiveListActivity.this).resumeRequests();
                // 判断是否滚动到底部
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 加载更多
                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    active = "update";
                    page++;
                    UserBusinessController.getInstance().getActivitysJson(null, getVersionCode(), "2", page, number, new com.example.controller.controller.Listener<ActiveListBean>() {
                        @Override
                        public void onStart(Object... params) {

                        }

                        @Override
                        public void onComplete(ActiveListBean bean, Object... params) {

                            if (active.equals("init")) {
                                if (bean.data.list.size() < 20) {
                                    mInfoList.removeFooterView(footerView);
                                    mInfoList.setOnScrollListener(null);
                                } else {
                                    mInfoList.setOnScrollListener(new UpdateListener());
                                }
                                infoData.clear();
                                infoData.addAll(bean.data.list);
                                adapter.notifyDataSetChanged();
                            } else if (active.equals("update")) {
                                if (bean.data.list.size() < 20) {
                                    mInfoList.removeFooterView(footerView);
                                    mInfoList.setOnScrollListener(null);
                                } else {
                                    mInfoList.setOnScrollListener(new UpdateListener());
                                }
                                infoData.addAll(bean.data.list);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            ToastHelper.showToast(ActiveListActivity.this, msg);
                        }
                    });
                }
            } else {
                Glide.with(ActiveListActivity.this).pauseRequests();
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;

            default:
                break;
        }
    }

    private class InfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return infoData.size();
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
                convertView = LayoutInflater.from(ActiveListActivity.this)
                        .inflate(R.layout.item_active_fragment, null, false);

                viewholder.iv_activeImage = (ImageView) convertView
                        .findViewById(R.id.image_single);
                viewholder.iv_status = (ImageView) convertView
                        .findViewById(R.id.status_image);
                viewholder.tv_activeName = (TextView) convertView
                        .findViewById(R.id.info_content);
                viewholder.tv_startTime = (TextView) convertView
                        .findViewById(R.id.start_time);
                viewholder.tv_endTime = (TextView) convertView
                        .findViewById(R.id.end_time);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.tv_activeName.setText(infoData.get(position).name);
//			viewholder.iv_activeImage
//					.setImageResource(R.drawable.information_view_bg);
            ImageUtils.getInstance().loadImage(ActiveListActivity.this, infoData.get(position).pic_url, R.drawable.loadding_icon, viewholder.iv_activeImage);
            // ImageLoaderWrapper.getDefault().displayImage(
            // infoData.get(position).pic_url, viewholder.iv_activeImage);
            if (infoData.get(position).status == 0
                    || infoData.get(position).status == 1) {
                viewholder.iv_status
                        .setBackgroundResource(R.drawable.active_unstart_status);
            } else if (infoData.get(position).status == 3) {
                viewholder.iv_status
                        .setBackgroundResource(R.drawable.active_start_status);
            } else if (infoData.get(position).status == 5) {
                viewholder.iv_status
                        .setBackgroundResource(R.drawable.active_end_status);
            }
            viewholder.tv_startTime.setText("总需" + infoData.get(position).total
                    + "/剩余" + infoData.get(position).remain);
            viewholder.tv_endTime.setText(infoData.get(position).visit + "阅读");

            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView iv_activeImage;
        private TextView tv_activeName;
        private ImageView iv_status;
        private TextView tv_startTime;
        private TextView tv_endTime;
    }
}
