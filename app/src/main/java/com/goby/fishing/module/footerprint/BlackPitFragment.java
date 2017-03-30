package com.goby.fishing.module.footerprint;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.FishRankBean;
import com.example.controller.bean.FishRankBean.Data.List;
import com.example.controller.bean.FishRankBean.Data.MeBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.footerprint.AllOrderFragment.ViewHolder;
import com.goby.fishing.module.other.OtherInfoActivity;

public class BlackPitFragment extends BaseFragment {

    private TextView tv_myOrder;

    private ImageView iv_myHeader;

    private TextView tv_myName;

    private TextView tv_myUserLike;

    private ListView lv_order;

    private OrderAdapter adapter;

    private int page = 1;

    private int number = 20;

    private ArrayList<List> dataList = new ArrayList<List>();

    private LinearLayout ll_bottomLayout;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    private MeBean meBean;

    private boolean isMoreData = false;

    public static AllOrderFragment newInstance() {
        return new AllOrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_order, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initFooter();
        initView(view);
        lv_order.setVisibility(View.GONE);
        if (dataList.size() == 0) {
            initData();
        } else {
            lv_order.setVisibility(View.VISIBLE);
            if (meBean != null) {
                ll_bottomLayout.setVisibility(View.VISIBLE);
                tv_myOrder.setText(String.valueOf(meBean.rank));
                ImageLoaderWrapper.getDefault().displayImage(meBean.head_pic,
                        iv_myHeader);
                tv_myName.setText(meBean.user_name);
                tv_myUserLike.setText(meBean.user_like + "人气");
            }
            if (isMoreData) {
                footerView.setVisibility(View.VISIBLE);
                loadMore.setVisibility(View.VISIBLE);
                lv_order.addFooterView(footerView);
                lv_order.setOnScrollListener(new UpdateListener());
                footerView.setVisibility(View.VISIBLE);
            }
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
        footerView.setVisibility(View.GONE);
    }

    public void initView(View view) {

        ll_bottomLayout = (LinearLayout) view.findViewById(R.id.bottom_layout);
        tv_myOrder = (TextView) view.findViewById(R.id.my_order);
        iv_myHeader = (ImageView) view.findViewById(R.id.my_header);
        tv_myName = (TextView) view.findViewById(R.id.my_name);
        tv_myUserLike = (TextView) view.findViewById(R.id.my_rank_count);
        lv_order = (ListView) view.findViewById(R.id.order_list);
        if (dataList.size() == 0) {
            lv_order.addFooterView(footerView);
        }
        adapter = new OrderAdapter();
        lv_order.setAdapter(adapter);
    }

    public void initData() {
        UserBusinessController.getInstance().getFishRankJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, 3, new com.example.controller.controller.Listener<FishRankBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(FishRankBean bean, Object... params) {

                    lv_order.setVisibility(View.VISIBLE);
                    if (bean.data.list.size() == 0) {
                        lv_order.setOnScrollListener(null);
                        lv_order.removeFooterView(footerView);
                        isMoreData = false;
                    } else if (bean.data.list.size() < 20) {
                        lv_order.setOnScrollListener(null);
                        lv_order.removeFooterView(footerView);
                        isMoreData = false;
                    } else {
                        footerView.setVisibility(View.VISIBLE);
                        loadMore.setVisibility(View.VISIBLE);
                        lv_order.setOnScrollListener(new UpdateListener());
                        footerView.setVisibility(View.VISIBLE);
                        isMoreData = true;
                    }
                    try {
                        if (bean.data.me != null) {
                            meBean = bean.data.me;
                            ll_bottomLayout.setVisibility(View.VISIBLE);
                            tv_myOrder.setText(String.valueOf(meBean.rank));
                            iv_myHeader.setImageResource(R.drawable.loadding_icon);
                            ImageLoaderWrapper.getDefault().displayImage(
                                    bean.data.me.head_pic, iv_myHeader);
                            tv_myName.setText(bean.data.me.user_name);
                            tv_myUserLike.setText(bean.data.me.user_like + "人气");
                        }
                    } catch (Exception e) {
                    }
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(String msg, Object... params) {
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
                    page++;
                    initData();
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    private class OrderAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_order_list, null, false);
                viewholder.tv_order = (TextView) convertView
                        .findViewById(R.id.order_text);
                viewholder.iv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_rankCount = (TextView) convertView
                        .findViewById(R.id.rank_count);
                viewholder.ll_orderLayout = (LinearLayout) convertView
                        .findViewById(R.id.order_layout);
                viewholder.iv_orderIcon = (ImageView) convertView
                        .findViewById(R.id.order_icon);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                viewholder.tv_order.setVisibility(View.GONE);
                viewholder.ll_orderLayout.setVisibility(View.VISIBLE);
                viewholder.iv_orderIcon
                        .setBackgroundResource(R.drawable.order_icon_1);
            } else if (position == 1) {
                viewholder.tv_order.setVisibility(View.GONE);
                viewholder.ll_orderLayout.setVisibility(View.VISIBLE);
                viewholder.iv_orderIcon
                        .setBackgroundResource(R.drawable.order_icon_2);
            } else if (position == 2) {
                viewholder.tv_order.setVisibility(View.GONE);
                viewholder.ll_orderLayout.setVisibility(View.VISIBLE);
                viewholder.iv_orderIcon
                        .setBackgroundResource(R.drawable.order_icon_3);
            } else {
                viewholder.tv_order.setVisibility(View.VISIBLE);
                viewholder.ll_orderLayout.setVisibility(View.GONE);
                viewholder.tv_order.setTextSize(16);
                viewholder.tv_rankCount.setTextColor(getActivity()
                        .getResources().getColor(R.color.gray_aaaaaa));
            }
            viewholder.tv_order.setText(String.valueOf(dataList.get(position).rank));
            viewholder.iv_userHeader.setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    dataList.get(position).head_pic, viewholder.iv_userHeader);
            viewholder.tv_userName.setText(dataList.get(position).user_name);
            viewholder.tv_rankCount.setText(dataList.get(position).user_like
                    + "人气");
            viewholder.iv_userHeader.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (dataList.get(position).user_id
                            .equals(sharedPreferenceUtil.getUserID())) {
                        MainActivity.launch(getActivity(), "meFragment");
                    } else {
                        OtherInfoActivity.launch(getActivity(),
                                dataList.get(position).user_id);
                    }
                }
            });
            return convertView;
        }
    }

    public class ViewHolder {

        private TextView tv_order;
        private ImageView iv_userHeader;
        private TextView tv_userName;
        private TextView tv_rankCount;
        private LinearLayout ll_orderLayout;
        private ImageView iv_orderIcon;
    }
}
