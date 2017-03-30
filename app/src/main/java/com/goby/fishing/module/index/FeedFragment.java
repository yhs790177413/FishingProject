package com.goby.fishing.module.index;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

import com.baoyz.widget.PullRefreshLayout;
import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FeedsFunListBean;
import com.example.controller.bean.FeedsFunListBean.Data.FeedsFunBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.umeng.analytics.MobclickAgent;

public class FeedFragment extends BaseFragment implements View.OnClickListener {

    private ListView mInfoList;

    public static InfoAdapter adapter;

    public static ArrayList<FeedsFunBean> infoData = new ArrayList<FeedsFunBean>();

    private int page = 1;

    private int number = 20;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private PullRefreshLayout mPullRefreshLayout;

    private View footerView;

    private View loadMore; // 加载更多的view

    private View loading; // 加载进度条

    private String active = "init";

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initFooter();
        initView(view);
        initData();
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

        mPullRefreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        mPullRefreshLayout.setEnabled(false);
        mInfoList = (ListView) view.findViewById(R.id.info_list);
        adapter = new InfoAdapter();
        mInfoList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mInfoList.setDividerHeight(1);
        mInfoList.setAdapter(adapter);
        mInfoList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position < infoData.size()) {
                    GameDetailActivity.launch(getActivity(),
                            infoData.get(position).user_name,
                            infoData.get(position).head_pic,
                            infoData.get(position).content,
                            infoData.get(position).pic_url,
                            infoData.get(position).message,
                            infoData.get(position).time);
                }
            }
        });
        mPullRefreshLayout
                .setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        // 刷新
                    }
                });
    }

    public void initData() {
        UserBusinessController.getInstance().getMyFishFunFeeds(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, new com.example.controller.controller.Listener<FeedsFunListBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(FeedsFunListBean bean, Object... params) {

                    if (active.equals("init")) {
                        if (bean.data.list.size() < 20) {
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.addFooterView(footerView);
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
                ToastHelper.showToast(getActivity(), msg);
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

    @Override
    public void onClick(View view) {

    }

    class InfoAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_live_fragment, null, false);
                viewholder.iv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_liveInfoContent = (TextView) convertView
                        .findViewById(R.id.live_info_content);
                viewholder.iv_pic = (ImageView) convertView
                        .findViewById(R.id.live_info_image);
                viewholder.tv_time = (TextView) convertView
                        .findViewById(R.id.live_info_time);
                viewholder.tv_message = (TextView) convertView
                        .findViewById(R.id.live_info_coin);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
//			viewholder.iv_userHeader
//			.setImageResource(R.drawable.loadding_icon);
            viewholder.iv_pic
                    .setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    infoData.get(position).head_pic, viewholder.iv_userHeader);
            String content = null;
            if (infoData.get(position).user_name.equals(sharedPreferenceUtil
                    .getUserName())) {
                content = "<font color='#ff0000'>"
                        + infoData.get(position).user_name + "</font>"
                        + infoData.get(position).content;
            } else {
                content = "<font color='#198ADE'>"
                        + infoData.get(position).user_name + "</font>"
                        + infoData.get(position).content;
            }
            viewholder.tv_liveInfoContent.setText(Html.fromHtml(content));
            ImageLoaderWrapper.getDefault().displayImage(
                    infoData.get(position).pic_url, viewholder.iv_pic);
            viewholder.tv_time.setText(TimeUtil.getTimeString(infoData
                    .get(position).time * 1000));
            viewholder.tv_message.setText(infoData.get(position).message);
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView iv_userHeader;
        private TextView tv_liveInfoContent;
        private ImageView iv_pic;
        private TextView tv_time;
        private TextView tv_message;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e("========", "==========");
        MobclickAgent.onPageStart("LiveFragment"); // 友盟统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。)
        MobclickAgent.onResume(getActivity()); // 友盟统计时长
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("LiveFragment"); // 友盟统计（仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息。
        MobclickAgent.onPause(getActivity());
    }
}
