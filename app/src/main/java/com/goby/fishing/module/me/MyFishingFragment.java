package com.goby.fishing.module.me;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.bumptech.glide.Glide;
import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FishingListBean;
import com.example.controller.bean.FishingListBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.widget.imageview.ImageCircleView;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.fishing.FishingDetailActivity;
import com.goby.fishing.module.fishing.FishingFragment.ViewHolder;

public class MyFishingFragment extends BaseFragment implements
        View.OnClickListener {

    private ListView lv_fishing;

    private FishingAdapter adapter;

    public static ArrayList<List> dataList = new ArrayList<List>();

    private int page = 1;

    private int number = 20;

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static boolean is_refresh = false;

    private boolean is_firstLoading = true;

    public static String active = "init";

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    public static MyFishingFragment newInstance() {
        return new MyFishingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_fishing, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initFooter();
        initView(view);
        if (is_firstLoading) {
            initData();
            is_firstLoading = false;
        }
        return view;
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
    }

    public void initView(View view) {

        lv_fishing = (ListView) view.findViewById(R.id.fishing_list);
        adapter = new FishingAdapter();
        lv_fishing.setAdapter(adapter);
        lv_fishing.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                FishingDetailActivity.launch(getActivity(),
                        dataList.get(position).id, position, "collection",
                        dataList.get(position).pic_url);
            }
        });
    }

    public void initData() {

        double latitude = 0, longitude = 0;
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
        }
        UserBusinessController.getInstance().favoriteFishingPoint(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, latitude, longitude, new com.example.controller.controller.Listener<FishingListBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(FishingListBean bean, Object... params) {


                if (active.equals("init")) {
                    if (bean.data.list.size() < 20) {
                        lv_fishing.setOnScrollListener(null);
                    } else {
                        lv_fishing.addFooterView(footerView);
                        lv_fishing.setOnScrollListener(new UpdateListener());
                    }
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                } else if (active.equals("update")) {
                    if (bean.data.list.size() < 20) {
                        lv_fishing.removeFooterView(footerView);
                        lv_fishing.setOnScrollListener(null);
                    } else {
                        lv_fishing.setOnScrollListener(new UpdateListener());
                    }
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                ToastHelper.showToast(getActivity(), msg);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            default:
                break;
        }
    }

    private class UpdateListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            // 当不滚动时
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                Glide.with(getActivity()).resumeRequests();
                // 判断是否滚动到底部
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 加载更多
                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    active = "update";
                    page++;
                    initData();
                }
            } else {
                Glide.with(getActivity()).pauseRequests();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // 设置当前屏幕显示的起始index和结束index
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
        public View getView(int position, View convertView, ViewGroup arg2) {
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

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderWrapper.getDefault()
                    .displayImage(dataList.get(position).pic_url,
                            viewholder.icv_fishingImage);
            viewholder.tv_fishingName.setText(dataList.get(position).name);
            viewholder.tv_distance.setText(dataList.get(position).distance);
            viewholder.tv_info.setText(dataList.get(position).summary);
            viewholder.tv_comment.setText(dataList.get(position).comment_number
                    + " 评论");
            // view
            if (dataList.get(position).location_number != 0) {
                viewholder.tv_footerprintCount
                        .setText(dataList.get(position).location_number + "个足迹");
                viewholder.tv_footerprintCount
                        .setBackgroundResource(R.drawable.has_footer_print_bg);
            } else {
                viewholder.tv_footerprintCount.setText("添加足迹");
                viewholder.tv_footerprintCount
                        .setBackgroundResource(R.drawable.no_footer_print_bg);
            }
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView icv_fishingImage;
        private TextView tv_fishingName;
        private ImageView iv_gpsImage;
        private TextView tv_distance;
        private TextView tv_info;
        private TextView tv_comment;
        private TextView tv_footerprintCount;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (is_refresh) {
            is_refresh = false;
            dataList.clear();
            initData();
        } else if (active.equals("update")) {
            adapter.notifyDataSetChanged();
        }
    }

}
