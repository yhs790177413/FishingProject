package com.goby.fishing.module.information;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.baidu.mobstat.StatService;
import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.fishing.R;
import com.example.controller.bean.ActiveListBean;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.FishingInfoBean;
import com.example.controller.bean.FooterprintListBean;
import com.example.controller.bean.ActiveListBean.Data.ActiveBean;
import com.example.controller.bean.FishingInfoBean.Data.List;
import com.example.controller.bean.FooterprintListBean.Data.List.Tag;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.BorderTextView;
import com.goby.fishing.common.utils.helper.android.app.WebViewActivity;
import com.goby.fishing.common.utils.helper.android.app.edit.ChannelActivity;
import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.framework.LazyLoadFragment;
import com.goby.fishing.module.fishing.BasicMapActivity;
import com.goby.fishing.module.fishing.FishingDetailActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.index.ActiveDetailActivity;
import com.goby.fishing.module.index.AsyncWebActivity;
import com.goby.fishing.module.index.GameActivity;
import com.goby.fishing.module.index.TagInfosActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.google.gson.Gson;


/**
 * Description：ViewPager切换的Fragment
 * <p>
 * Created by Mjj on 2016/11/19.
 */
public class NewsFragment extends LazyLoadFragment {

    private String activeString;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ListView mInfoList;
    private PullRefreshLayout mPullRefreshLayout;
    private InfoAdapter adapter;
    private ImageGridAdapter imageAdapter;
    private String active = "init";
    private ArrayList<String> imageData;
    private LayoutInflater mInflater;
    private int page = 1;
    private int number = 20;
    private LinearLayout ll_error;
    private Button btn_reload;
    public static boolean fish_refresh = false;
    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条
    public static boolean isUploadRefresh = false;
    private boolean is_liking = false;
    private int mPosition = 0;
    private String activeId = "";
    private ImageView iv_loading, iv_listTop;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    View frView;
    private LinearLayout ll_context;

    public static NewsFragment newInstance(String active, String flag,
                                           String activeId) {
        Bundle bundle = new Bundle();
        bundle.putString("active", active);
        bundle.putString("flag", flag);
        bundle.putString("activeId", activeId);
        NewsFragment testFm = new NewsFragment();
        testFm.setArguments(bundle);
        return testFm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (frView == null) {
            frView = inflater.inflate(R.layout.fragment_info_item, null);
            if (bundle != null) {
                activeString = bundle.getString("active");
                activeId = bundle.getString("activeId");
            }
            isPrepared = true;
            lazyLoad();
        }

        // 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) frView.getParent();
        if (parent != null) {
            parent.removeView(frView);
        }
        return frView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (fish_refresh) {
            fish_refresh = false;
            if (NewInformationFragment.userChannelList
                    .get(NewInformationFragment.columnSelectIndex).name
                    .equals("圈子")) {
                NewInformationFragment.feedData.get(sharedPreferenceUtil
                        .getCurrentPosition()).like_number = NewInformationFragment.feedData
                        .get(sharedPreferenceUtil.getCurrentPosition()).like_number + 1;
            } else {
                NewInformationFragment.infoDataMap
                        .get(NewInformationFragment.userChannelList
                                .get(NewInformationFragment.columnSelectIndex).name)
                        .get(sharedPreferenceUtil.getCurrentPosition()).like_number = NewInformationFragment.infoDataMap
                        .get(NewInformationFragment.userChannelList
                                .get(NewInformationFragment.columnSelectIndex).name)
                        .get(sharedPreferenceUtil.getCurrentPosition()).like_number + 1;
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void initView(View view) {

        ll_context = (LinearLayout) view.findViewById(R.id.context_layout);
        iv_loading = (ImageView) view.findViewById(R.id.loading_image);
        iv_listTop = (ImageView) view.findViewById(R.id.list_top_btn);
        ImageUtils.getInstance().loadResouceImage(getActivity(),
                R.drawable.loading, iv_loading);
        mPullRefreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        mInfoList = (ListView) view.findViewById(R.id.info_list);
        mInflater = LayoutInflater.from(getActivity());
        adapter = new InfoAdapter();
        mInfoList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mInfoList.setDividerHeight(1);
        if (mInfoList.getFooterViewsCount() < 1) {
            mInfoList.addFooterView(footerView);
        }
        // mInfoList.setAdapter(adapter);
        mInfoList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                sharedPreferenceUtil.setCurrentPosition(position);
                mPosition = position;
                if (activeString.equals("活动")) {
                    if (!TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                        if (position < NewInformationFragment.activeData.size()) {
                            ActiveDetailActivity
                                    .launch(getActivity(),
                                            NewInformationFragment.activeData
                                                    .get(position).id + "");
                        }
                    } else {
                        ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                        LoginActivity.launch(getActivity(), "NewsFragment");
                    }
                } else if (activeString.equals("圈子")) {
                    if (position - 1 <= NewInformationFragment.feedData.size()) {
                        FooterprintDetailActivity
                                .launch(getActivity(),
                                        NewInformationFragment.feedData
                                                .get(position).id,
                                        NewInformationFragment.feedData
                                                .get(position).pic_urls.get(0),
                                        position, "footerfragment");
                    }
                } else {
                    if (position < NewInformationFragment.infoDataMap.get(
                            activeString).size()) {
                        if (NewInformationFragment.infoDataMap
                                .get(activeString).get(position).content_url
                                .split("://")[1].split("/")[0].equals("info")) {
                            FishingInfoDetailActivity.launch(
                                    getActivity(),
                                    Integer.parseInt(NewInformationFragment.infoDataMap
                                            .get(activeString).get(position).content_url
                                            .split("://")[1].split("/")[1]),
                                    position,
                                    "fish",
                                    NewInformationFragment.infoDataMap.get(
                                            activeString).get(position).pic_urls
                                            .get(0), 0);
                        } else if (NewInformationFragment.infoDataMap.get(
                                activeString).get(position).content_url
                                .split("://")[1].split("/")[0]
                                .equals("fishpoint")) {
                            FishingDetailActivity.launch(
                                    getActivity(),
                                    Integer.parseInt(NewInformationFragment.infoDataMap
                                            .get(activeString).get(position).content_url
                                            .split("://")[1].split("/")[1]),
                                    position,
                                    "info_fragmrnt",
                                    NewInformationFragment.infoDataMap.get(
                                            activeString).get(position).pic_urls
                                            .get(0));
                        } else if (NewInformationFragment.infoDataMap.get(
                                activeString).get(position).content_url
                                .split("://")[1].split("/")[0].equals("feed")) {
                            FooterprintDetailActivity.launch(
                                    getActivity(),
                                    Integer.parseInt(NewInformationFragment.infoDataMap
                                            .get(activeString).get(position).content_url
                                            .split("://")[1].split("/")[1]),
                                    NewInformationFragment.infoDataMap.get(
                                            activeString).get(position).pic_urls
                                            .get(0), position, "info_fragment");
                        } else if (NewInformationFragment.infoDataMap.get(
                                activeString).get(position).content_url
                                .split("://")[1].split("/")[0]
                                .equals("activity")) {
                            ActiveDetailActivity
                                    .launch(getActivity(),
                                            NewInformationFragment.infoDataMap
                                                    .get(activeString).get(
                                                    position).content_url
                                                    .split("://")[1].split("/")[1]);
                        } else if (NewInformationFragment.infoDataMap.get(
                                activeString).get(position).content_url
                                .split("://")[1].equals("fishfun")) {
                            if (TextUtils.isEmpty(sharedPreferenceUtil
                                    .getUserToken())) {
                                ToastHelper.showToast(getActivity(),
                                        "您还未登录,请先登录");
                                LoginActivity.launch(getActivity(),
                                        "indexFragment");
                            } else {
                                GameActivity.launch(getActivity());
                            }
                        } else if (NewInformationFragment.infoDataMap.get(
                                activeString).get(position).content_url
                                .startsWith("http")) {
                            if (NewInformationFragment.infoDataMap.get(
                                    activeString).get(position).content_url
                                    .startsWith("https://wap.koudaitong.com")) {
                                AsyncWebActivity
                                        .launch(getActivity(),
                                                NewInformationFragment.infoDataMap
                                                        .get(activeString).get(
                                                        position).content_url,
                                                NewInformationFragment.infoDataMap
                                                        .get(activeString).get(
                                                        position).title);
                            } else {
                                WebViewActivity
                                        .launch(getActivity(),
                                                NewInformationFragment.infoDataMap
                                                        .get(activeString).get(
                                                        position).content_url,
                                                NewInformationFragment.infoDataMap
                                                        .get(activeString).get(
                                                        position).title);
                            }
                        }
                    }
                }
            }

        });
        ll_error = (LinearLayout) view.findViewById(R.id.error_layout);
        btn_reload = (Button) view.findViewById(R.id.reload_btn);
        // btn_reload.setOnClickListener(this);
        mPullRefreshLayout
                .setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        Glide.with(getActivity()).pauseRequests();
                        // 刷新
                        page = 1;
                        if (mInfoList.getCount() > 0) {
                            active = "refresh";
                        } else {
                            active = "init";
                        }
                        getRemote();

                    }
                });
        iv_listTop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mInfoList.setSelection(0);
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
    }

    public void initData() {

        if (!NewInformationFragment.uploadMap.get(activeString)) {
            getRemote();
        } else {
            mInfoList.setOnScrollListener(new UpdateListener());
            adapter.notifyDataSetChanged();
            mInfoList.setVisibility(View.VISIBLE);
        }
    }

    public void getRemote() {
        // 发送百度统计事件
        StatService.onEvent(getActivity(), "tag_" + activeString + "_count",
                "tag_" + activeString + "_count", 1);
        if (activeString.equals("全部")) {
            UserBusinessController.getInstance().fishingInfoJson(null, getVersionCode(), "2", page, number, 0, new com.example.controller.controller.Listener<FishingInfoBean>() {
                @Override
                public void onStart(Object... params) {

                }

                @Override
                public void onComplete(FishingInfoBean bean, Object... params) {
                    mHasLoadedOnce = true;
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                    mPullRefreshLayout.setRefreshing(false);

                    mPullRefreshLayout.setVisibility(View.VISIBLE);
                    if (active.equals("init")) {
                        if (bean.data.list.size() < 20) {
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }

                        NewInformationFragment.infoDataMap.put(activeString,
                                bean.data.list);
                        mInfoList.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setInfoFish(gson.toJson(bean));
                    } else if (active.equals("update")) {
                        if (bean.data.list.size() < 20) {
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        NewInformationFragment.infoDataMap.get(activeString)
                                .addAll(bean.data.list);
                        adapter.notifyDataSetChanged();
                    } else if (active.equals("refresh")) {
                        if (bean.data.list.size() < 20) {
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        if (NewInformationFragment.infoDataMap.get(activeString) != null
                                && NewInformationFragment.infoDataMap.get(
                                activeString).size() > 0) {
                            NewInformationFragment.infoDataMap.get(activeString)
                                    .clear();
                            NewInformationFragment.infoDataMap.get(activeString)
                                    .addAll(bean.data.list);
                        } else {
                            NewInformationFragment.infoDataMap.put(activeString,
                                    bean.data.list);
                        }
                        adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setInfoFish(gson.toJson(bean));
                    }
                    NewInformationFragment.uploadMap.put(activeString, true);
                    // infoMap.put(activeString,
                    // NewInformationFragment.infoDataMap.get(activeString));

                }

                @Override
                public void onFail(String msg, Object... params) {
                    ToastHelper.showToast(getActivity(), msg);
                    mPullRefreshLayout.setRefreshing(false);
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                }
            });
        } else if (activeString.equals("圈子")) {
            double latitude = 0, longitude = 0;
            if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLongitude())) {
                latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
                longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
            }
            UserBusinessController.getInstance().getFishFeeds(null, getVersionCode(), "2", 0, page, number, 2, latitude, longitude, new com.example.controller.controller.Listener<FooterprintListBean>() {
                @Override
                public void onStart(Object... params) {

                }

                @Override
                public void onComplete(FooterprintListBean bean, Object... params) {
                    mHasLoadedOnce = true;
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                    mPullRefreshLayout.setRefreshing(false);

                    if (active.equals("init") || active.equals("refresh")) {
                        if (bean.data.list.size() == 0) {
                            ToastHelper.showToast(getActivity(), "暂无数据");
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                        } else if (bean.data.list.size() < 20) {
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                            ToastHelper.showToast(getActivity(), "数据已全部加载完");
                        } else {
                            footerView.setVisibility(View.VISIBLE);
                            loadMore.setVisibility(View.VISIBLE);
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        NewInformationFragment.feedData.clear();
                        NewInformationFragment.feedData.addAll(bean.data.list);
                        // adapter.notifyDataSetChanged();
                        mInfoList.setAdapter(adapter);
                    } else if (active.equals("update")) {
                        if (bean.data.list.size() == 0
                                || bean.data.list.size() < 20) {
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                            ToastHelper.showToast(getActivity(), "数据已全部加载完");
                            mInfoList.setOnScrollListener(null);
                        } else {
                            footerView.setVisibility(View.VISIBLE);
                            loadMore.setVisibility(View.VISIBLE);
                            mInfoList.setOnScrollListener(new UpdateListener());
                            loadMore.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                        NewInformationFragment.feedData.addAll(bean.data.list);
                        adapter.notifyDataSetChanged();
                    }
                    NewInformationFragment.uploadMap.put(activeString, true);

                }

                @Override
                public void onFail(String msg, Object... params) {
                    ToastHelper.showToast(getActivity(), msg);
                    mPullRefreshLayout.setRefreshing(false);
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                }
            });
        } else if (activeString.equals("活动")) {
            UserBusinessController.getInstance().getActivitysJson(null, getVersionCode(), "2", page, number, new com.example.controller.controller.Listener<ActiveListBean>() {
                @Override
                public void onStart(Object... params) {

                }

                @Override
                public void onComplete(ActiveListBean bean, Object... params) {
                    mHasLoadedOnce = true;
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                    mPullRefreshLayout.setRefreshing(false);

                    mPullRefreshLayout.setVisibility(View.VISIBLE);
                    if (active.equals("init")) {
                        if (bean.data.list.size() < 20) {
                            mInfoList.setOnScrollListener(null);
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        NewInformationFragment.activeData.clear();
                        NewInformationFragment.activeData.addAll(bean.data.list);
                        mInfoList.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setInfoActive(gson.toJson(bean));
                    } else if (active.equals("update")) {
                        if (bean.data.list.size() < 20) {
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        NewInformationFragment.activeData.addAll(bean.data.list);
                        adapter.notifyDataSetChanged();
                    } else if (active.equals("refresh")) {
                        if (bean.data.list.size() < 20) {
                            mInfoList.setOnScrollListener(null);
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        NewInformationFragment.activeData.clear();
                        NewInformationFragment.activeData.addAll(bean.data.list);
                        adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setInfoActive(gson.toJson(bean));
                    }
                    NewInformationFragment.uploadMap.put(activeString, true);
                }

                @Override
                public void onFail(String msg, Object... params) {
                    ToastHelper.showToast(getActivity(), msg);
                    mPullRefreshLayout.setRefreshing(false);
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                }
            });
        } else {
            UserBusinessController.getInstance().getTagInfosJson(null, getVersionCode(), "2", page, number, "", activeId, new com.example.controller.controller.Listener<FishingInfoBean>() {
                @Override
                public void onStart(Object... params) {

                }

                @Override
                public void onComplete(FishingInfoBean bean, Object... params) {
                    mHasLoadedOnce = true;
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                    mPullRefreshLayout.setRefreshing(false);

                    mPullRefreshLayout.setVisibility(View.VISIBLE);
                    if (active.equals("init")) {
                        if (bean.data.list.size() < 20) {
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }

                        NewInformationFragment.infoDataMap.put(activeString,
                                bean.data.list);
                        mInfoList.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setInfoFish(gson.toJson(bean));
                    } else if (active.equals("update")) {
                        if (bean.data.list.size() < 20) {
                            if (mInfoList.getFooterViewsCount() > 0) {
                                mInfoList.removeFooterView(footerView);
                            }
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        NewInformationFragment.infoDataMap.get(activeString)
                                .addAll(bean.data.list);
                        adapter.notifyDataSetChanged();
                    } else if (active.equals("refresh")) {
                        if (bean.data.list.size() < 20) {
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        if (NewInformationFragment.infoDataMap.get(activeString) != null
                                && NewInformationFragment.infoDataMap.get(
                                activeString).size() > 0) {
                            NewInformationFragment.infoDataMap.get(activeString)
                                    .clear();
                            NewInformationFragment.infoDataMap.get(activeString)
                                    .addAll(bean.data.list);
                        } else {
                            NewInformationFragment.infoDataMap.put(activeString,
                                    bean.data.list);
                        }
                        adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setInfoFish(gson.toJson(bean));
                    }
                    NewInformationFragment.uploadMap.put(activeString, true);
                    // infoMap.put(activeString,
                    // NewInformationFragment.infoDataMap.get(activeString));

                }

                @Override
                public void onFail(String msg, Object... params) {
                    ToastHelper.showToast(getActivity(), msg);
                    mPullRefreshLayout.setRefreshing(false);
                    ll_context.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                }
            });
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
                    getRemote();
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

    private class InfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (activeString.equals("活动")) {
                return NewInformationFragment.activeData.size();
            } else if (activeString.equals("圈子")) {
                return NewInformationFragment.feedData.size();
            } else {
                if (NewInformationFragment.infoDataMap.get(activeString) != null
                        && NewInformationFragment.infoDataMap.get(activeString)
                        .size() > 0) {
                    return NewInformationFragment.infoDataMap.get(activeString)
                            .size();
                } else {
                    return 0;
                }

            }
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
                convertView = mInflater.inflate(R.layout.item_info_fragment,
                        null, false);
                // 资讯
                viewholder.ll_stypeLayoutOne = (LinearLayout) convertView
                        .findViewById(R.id.stype_layout_one);
                viewholder.tv_infoNameOne = (TextView) convertView
                        .findViewById(R.id.info_name_one);
                viewholder.tv_infoNameTwo = (TextView) convertView
                        .findViewById(R.id.info_name_two);
                viewholder.tv_infoNameThree = (TextView) convertView
                        .findViewById(R.id.info_name_three);
                viewholder.iv_infoImageOne = (ImageView) convertView
                        .findViewById(R.id.info_image_one);
                viewholder.iv_infoImageThree = (ImageView) convertView
                        .findViewById(R.id.info_image_three);
                viewholder.rl_layoutOne = (RelativeLayout) convertView
                        .findViewById(R.id.layout_one);
                viewholder.ll_layoutTwo = (LinearLayout) convertView
                        .findViewById(R.id.layout_two);
                viewholder.ll_layoutThree = (LinearLayout) convertView
                        .findViewById(R.id.layout_three);
                viewholder.tv_commentCount = (TextView) convertView
                        .findViewById(R.id.comment_count);
                viewholder.tv_time = (TextView) convertView
                        .findViewById(R.id.time);
                viewholder.gv_infoImage = (GridView) convertView
                        .findViewById(R.id.info_image_grid);
                viewholder.gv_infoImage.setClickable(false);
                viewholder.gv_infoImage.setPressed(false);
                viewholder.gv_infoImage.setEnabled(false);
                viewholder.gv_infoImage.setSelector(new ColorDrawable(
                        Color.TRANSPARENT));
                viewholder.tv_prase = (TextView) convertView
                        .findViewById(R.id.praise_count);
                viewholder.btv_typeImage = (BorderTextView) convertView
                        .findViewById(R.id.type_image);
                viewholder.btv_jianImage = (BorderTextView) convertView
                        .findViewById(R.id.jian_image);
                viewholder.tv_visit = (TextView) convertView
                        .findViewById(R.id.visit_text);
                // 活动
                viewholder.rl_stypeLayoutTwo = (RelativeLayout) convertView
                        .findViewById(R.id.stype_layout_two);
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
                // 圈子
                viewholder.ll_stypeLayoutThree = (LinearLayout) convertView
                        .findViewById(R.id.stype_layout_three);
                viewholder.iv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_friendCity = (TextView) convertView
                        .findViewById(R.id.city_name);
                viewholder.tv_feedTime = (TextView) convertView
                        .findViewById(R.id.feed_time);
                viewholder.tv_content = (TextView) convertView
                        .findViewById(R.id.content);
                viewholder.gv_feedImage = (GridView) convertView
                        .findViewById(R.id.footer_image_grid);
                viewholder.gv_feedImage.setClickable(false);
                viewholder.gv_feedImage.setPressed(false);
                viewholder.gv_feedImage.setEnabled(false);
                viewholder.gv_feedImage.setSelector(new ColorDrawable(
                        Color.TRANSPARENT));
                viewholder.tv_imageCount = (TextView) convertView
                        .findViewById(R.id.image_count);
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
                viewholder.iv_gpsIcon = (ImageView) convertView
                        .findViewById(R.id.gps_cricle);
                viewholder.iv_tagIcon = (ImageView) convertView
                        .findViewById(R.id.tag_icon);
                viewholder.tv_tagCount = (TextView) convertView
                        .findViewById(R.id.tag_count);
                viewholder.iv_jing = (ImageView) convertView
                        .findViewById(R.id.jing_image);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (position < 4) {
                iv_listTop.setVisibility(View.GONE);
            } else {
                iv_listTop.setVisibility(View.VISIBLE);
            }
            if (activeString.equals("活动")) {
                viewholder.ll_stypeLayoutOne.setVisibility(View.GONE);
                viewholder.rl_stypeLayoutTwo.setVisibility(View.VISIBLE);
                viewholder.ll_stypeLayoutThree.setVisibility(View.GONE);
                viewholder.tv_activeName
                        .setText(NewInformationFragment.activeData
                                .get(position).name);
                if (NewInformationFragment.activeData.get(position).status == 0
                        || NewInformationFragment.activeData.get(position).status == 1) {
                    viewholder.iv_status
                            .setBackgroundResource(R.drawable.active_unstart_status);
                } else if (NewInformationFragment.activeData.get(position).status == 3) {
                    viewholder.iv_status
                            .setBackgroundResource(R.drawable.active_start_status);
                } else if (NewInformationFragment.activeData.get(position).status == 5) {
                    viewholder.iv_status
                            .setBackgroundResource(R.drawable.active_end_status);
                }
                ImageUtils
                        .getInstance()
                        .loadImage(
                                getActivity(),
                                NewInformationFragment.activeData.get(position).pic_url,
                                R.drawable.loadding_icon,
                                viewholder.iv_activeImage);
                viewholder.tv_startTime
                        .setText("总需"
                                + NewInformationFragment.activeData
                                .get(position).total
                                + "/剩余"
                                + NewInformationFragment.activeData
                                .get(position).remain);
                viewholder.tv_endTime.setText(NewInformationFragment.activeData
                        .get(position).visit + "阅读");
            } else if (activeString.equals("圈子")) {
                viewholder.ll_stypeLayoutOne.setVisibility(View.GONE);
                viewholder.rl_stypeLayoutTwo.setVisibility(View.GONE);
                viewholder.ll_stypeLayoutThree.setVisibility(View.VISIBLE);
                viewholder.iv_userHeader
                        .setImageResource(R.drawable.loadding_icon);

                if (NewInformationFragment.feedData.get(position).head_pic
                        .startsWith("http://")) {
                    ImageLoaderWrapper
                            .getDefault()
                            .displayImage(
                                    NewInformationFragment.feedData
                                            .get(position).head_pic,
                                    viewholder.iv_userHeader,
                                    new ImageLoaderWrapper.DisplayConfig.Builder()
                                            .buildRounded(12),
                                    R.drawable.loadding_icon);
                } else {
                    ImageLoaderWrapper
                            .getDefault()
                            .displayImage(
                                    Constant.HOST_URL
                                            + NewInformationFragment.feedData
                                            .get(position).head_pic,
                                    viewholder.iv_userHeader,
                                    new ImageLoaderWrapper.DisplayConfig.Builder()
                                            .buildRounded(12),
                                    R.drawable.loadding_icon);
                }

                viewholder.tv_userName.setText(NewInformationFragment.feedData
                        .get(position).user_name);
                if (TextUtils.isEmpty(NewInformationFragment.feedData
                        .get(position).city_name)) {
                    viewholder.tv_friendCity.setVisibility(View.GONE);
                } else {
                    viewholder.tv_friendCity.setVisibility(View.VISIBLE);
                    viewholder.tv_friendCity
                            .setText(NewInformationFragment.feedData
                                    .get(position).city_name);
                }
                viewholder.tv_feedTime.setText(TimeUtil
                        .getTimeString(NewInformationFragment.feedData
                                .get(position).time * 1000));
                if (TextUtils.isEmpty(NewInformationFragment.feedData
                        .get(position).info)) {
                    viewholder.tv_content.setVisibility(View.GONE);
                } else {
                    viewholder.tv_content.setVisibility(View.VISIBLE);
                    try {
                        viewholder.tv_content.setText(EmojiUtil
                                .handlerEmojiText(
                                        NewInformationFragment.feedData
                                                .get(position).info,
                                        getActivity()));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (NewInformationFragment.feedData.get(position).picture_number > 3) {
                    viewholder.tv_imageCount.setVisibility(View.VISIBLE);
                    viewholder.tv_imageCount
                            .setText(NewInformationFragment.feedData
                                    .get(position).picture_number + " 图");
                } else {
                    viewholder.tv_imageCount.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(NewInformationFragment.feedData
                        .get(position).fish_info)) {
                    viewholder.tv_fishName.setVisibility(View.GONE);
                } else {
                    viewholder.tv_fishName.setVisibility(View.VISIBLE);
                    viewholder.tv_fishName
                            .setText(NewInformationFragment.feedData
                                    .get(position).fish_info);
                }
                if (TextUtils.isEmpty(NewInformationFragment.feedData
                        .get(position).tools)) {
                    viewholder.tv_fishType.setVisibility(View.GONE);
                } else {
                    viewholder.tv_fishType.setVisibility(View.VISIBLE);
                    viewholder.tv_fishType
                            .setText(NewInformationFragment.feedData
                                    .get(position).tools);
                }
                if (TextUtils.isEmpty(NewInformationFragment.feedData
                        .get(position).weather)) {
                    viewholder.tv_clound.setVisibility(View.GONE);
                } else {
                    viewholder.tv_clound.setVisibility(View.VISIBLE);
                    viewholder.tv_clound
                            .setText(NewInformationFragment.feedData
                                    .get(position).weather);
                }
                if (TextUtils.isEmpty(NewInformationFragment.feedData
                        .get(position).address_info)) {
                    viewholder.tv_distance.setVisibility(View.GONE);
                    viewholder.iv_gpsIcon.setVisibility(View.GONE);
                } else {
                    viewholder.iv_gpsIcon.setVisibility(View.VISIBLE);
                    viewholder.tv_distance.setVisibility(View.VISIBLE);
                    viewholder.tv_distance
                            .setText(NewInformationFragment.feedData
                                    .get(position).address_info);
                }
                viewholder.tv_distance
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                BasicMapActivity.launch(getActivity(),
                                        NewInformationFragment.feedData
                                                .get(position).longitude,
                                        NewInformationFragment.feedData
                                                .get(position).latitude,
                                        NewInformationFragment.feedData
                                                .get(position).info,
                                        NewInformationFragment.feedData
                                                .get(position).pic_urls.get(0),
                                        "");
                            }
                        });
                viewholder.tv_prise.setText(NewInformationFragment.feedData
                        .get(position).like_number + " 赞");
                viewholder.tv_prise.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        if (TextUtils.isEmpty(sharedPreferenceUtil
                                .getUserToken())) {
                            ToastHelper.showNewToast(getActivity(),
                                    "您还未登录,请先登录再点赞");
                        } else {
                            if (!is_liking) {
                                is_liking = true;
                                if (NewInformationFragment.feedData
                                        .get(position).is_like == 0) {
                                    mPosition = position;
                                    UserBusinessController.getInstance().getLike(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", NewInformationFragment.feedData.get(position).id, 3, 1, new com.example.controller.controller.Listener<BaseBean>() {
                                        @Override
                                        public void onStart(Object... params) {
                                            showProgressDialog("正在点赞中,请稍候...");
                                        }

                                        @Override
                                        public void onComplete(BaseBean bean, Object... params) {
                                            dismissProgressDialog();
                                            mPullRefreshLayout.setRefreshing(false);
                                            is_liking = false;

                                            NewInformationFragment.feedData.get(mPosition).is_like = 1;
                                            NewInformationFragment.feedData.get(mPosition).like_number = NewInformationFragment.feedData
                                                    .get(mPosition).like_number + 1;
                                            ToastHelper.showToast(getActivity(), "点赞成功");
                                            try {
                                                NewInformationFragment.feedData.get(mPosition).tag.user_likes = NewInformationFragment.feedData
                                                        .get(mPosition).tag.user_likes + 1;
                                            } catch (Exception e) {
                                                Tag tag = new Tag();
                                                tag.user_likes = 1;
                                                NewInformationFragment.feedData.get(mPosition).tag = tag;
                                            }

                                            adapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onFail(String msg, Object... params) {
                                            dismissProgressDialog();
                                            ToastHelper.showToast(getActivity(), msg);
                                        }
                                    });
                                } else if (NewInformationFragment.feedData
                                        .get(position).is_like == 1) {
                                    ToastHelper.showNewToast(getActivity(),
                                            "您已点过赞了");
                                    is_liking = false;
                                }
                            }
                        }
                    }
                });
                viewholder.tv_comment.setText(NewInformationFragment.feedData
                        .get(position).comment_number + " 评论");
                imageData = new ArrayList<String>();
                imageData
                        .addAll(NewInformationFragment.feedData.get(position).pic_urls);
                imageAdapter = new ImageGridAdapter(imageData);
                viewholder.gv_feedImage.setAdapter(imageAdapter);
                try {
                    if (NewInformationFragment.feedData.get(position).tag.tag_id == 1) {
                        viewholder.iv_tagIcon
                                .setBackgroundResource(R.drawable.daily_type_icon);
                    } else if (NewInformationFragment.feedData.get(position).tag.tag_id == 2) {
                        viewholder.iv_tagIcon
                                .setBackgroundResource(R.drawable.wild_fish_type_icon);
                    } else if (NewInformationFragment.feedData.get(position).tag.tag_id == 3) {
                        viewholder.iv_tagIcon
                                .setBackgroundResource(R.drawable.black_pit_type_icon);
                    } else if (NewInformationFragment.feedData.get(position).tag.tag_id == 4) {
                        viewholder.iv_tagIcon
                                .setBackgroundResource(R.drawable.equipment_type_icon);
                    } else if (NewInformationFragment.feedData.get(position).tag.tag_id == 5) {
                        viewholder.iv_tagIcon
                                .setBackgroundResource(R.drawable.food_type_icon);
                    } else if (NewInformationFragment.feedData.get(position).tag.tag_id == 6) {
                        viewholder.iv_tagIcon
                                .setBackgroundResource(R.drawable.lures_type_icon);
                    } else if (NewInformationFragment.feedData.get(position).tag.tag_id == 7) {
                        viewholder.iv_tagIcon
                                .setBackgroundResource(R.drawable.fishing_type_icon);
                    }
                    viewholder.tv_tagCount.setText(String
                            .valueOf(NewInformationFragment.feedData
                                    .get(position).tag.user_likes)
                            + "人气");
                } catch (Exception e) {
                    // TODO: handle exception
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.daily_type_icon);
                    viewholder.tv_tagCount.setText("0人气");
                }
                if (NewInformationFragment.feedData.get(position).jing == 0) {
                    viewholder.iv_jing.setVisibility(View.GONE);
                } else {
                    viewholder.iv_jing.setVisibility(View.VISIBLE);
                }
            } else {
                viewholder.ll_stypeLayoutOne.setVisibility(View.VISIBLE);
                viewholder.rl_stypeLayoutTwo.setVisibility(View.GONE);
                viewholder.ll_stypeLayoutThree.setVisibility(View.GONE);
                if (NewInformationFragment.infoDataMap.get(activeString).get(
                        position).preview_style == 1) {
                    viewholder.rl_layoutOne.setVisibility(View.VISIBLE);
                    viewholder.ll_layoutTwo.setVisibility(View.GONE);
                    viewholder.ll_layoutThree.setVisibility(View.GONE);
                } else if (NewInformationFragment.infoDataMap.get(activeString)
                        .get(position).preview_style == 2) {
                    viewholder.rl_layoutOne.setVisibility(View.GONE);
                    viewholder.ll_layoutTwo.setVisibility(View.VISIBLE);
                    viewholder.ll_layoutThree.setVisibility(View.GONE);
                } else if (NewInformationFragment.infoDataMap.get(activeString)
                        .get(position).preview_style == 3) {
                    viewholder.rl_layoutOne.setVisibility(View.GONE);
                    viewholder.ll_layoutTwo.setVisibility(View.GONE);
                    viewholder.ll_layoutThree.setVisibility(View.VISIBLE);
                }

                ImageUtils.getInstance().loadImage(
                        getActivity(),
                        NewInformationFragment.infoDataMap.get(activeString)
                                .get(position).pic_urls.get(0),
                        R.drawable.loadding_icon, viewholder.iv_infoImageOne);
                ImageUtils.getInstance().loadImage(
                        getActivity(),
                        NewInformationFragment.infoDataMap.get(activeString)
                                .get(position).pic_urls.get(0),
                        R.drawable.loadding_icon, viewholder.iv_infoImageThree);
                viewholder.tv_infoNameOne
                        .setText(NewInformationFragment.infoDataMap.get(
                                activeString).get(position).title);
                viewholder.tv_infoNameTwo
                        .setText(NewInformationFragment.infoDataMap.get(
                                activeString).get(position).title);
                viewholder.tv_infoNameThree
                        .setText(NewInformationFragment.infoDataMap.get(
                                activeString).get(position).title);
                viewholder.tv_commentCount
                        .setText(NewInformationFragment.infoDataMap.get(
                                activeString).get(position).comment_number
                                + " 评论");
                viewholder.tv_prase.setText(NewInformationFragment.infoDataMap
                        .get(activeString).get(position).like_number + " 赞");
                viewholder.tv_visit.setText(NewInformationFragment.infoDataMap
                        .get(activeString).get(position).visit_number + " 阅读");
                viewholder.tv_time.setText(TimeUtil
                        .getTimeString(NewInformationFragment.infoDataMap.get(
                                activeString).get(position).time * 1000));
                try {
                    viewholder.btv_typeImage.setVisibility(View.VISIBLE);
                    viewholder.btv_typeImage
                            .setText(NewInformationFragment.infoDataMap.get(
                                    activeString).get(position).tag.tag_name);
                    int color_tag = NewInformationFragment.infoDataMap.get(
                            activeString).get(position).tag.tag_id;
                    viewholder.btv_typeImage.setColor(initColor().get(
                            color_tag % 7));
                    viewholder.btv_typeImage.setTextColor(initColor().get(
                            color_tag % 7));
                } catch (Exception e) {
                    viewholder.btv_typeImage.setVisibility(View.GONE);
                }
                viewholder.btv_typeImage
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                TagInfosActivity.launch(
                                        getActivity(),
                                        String.valueOf(NewInformationFragment.infoDataMap
                                                .get(activeString)
                                                .get(position).tag.tag_id),
                                        NewInformationFragment.infoDataMap.get(
                                                activeString).get(position).tag.tag_name,
                                        null);
                            }
                        });
                if (NewInformationFragment.infoDataMap.get(activeString).get(
                        position).recommend == 1) {
                    viewholder.btv_jianImage.setVisibility(View.VISIBLE);
                    viewholder.btv_jianImage.setText("荐");
                    viewholder.btv_jianImage.setColor(getActivity()
                            .getResources().getColor(R.color.red_d30549));
                    viewholder.btv_jianImage.setTextColor(getActivity()
                            .getResources().getColor(R.color.red_d30549));
                } else {
                    viewholder.btv_jianImage.setVisibility(View.GONE);
                }
                imageData = new ArrayList<String>();
                imageData.addAll(NewInformationFragment.infoDataMap.get(
                        activeString).get(position).pic_urls);
                imageAdapter = new ImageGridAdapter(imageData);
                viewholder.gv_infoImage.setAdapter(imageAdapter);
            }
            return convertView;
        }
    }

    public class ViewHolder {
        // 资讯
        private LinearLayout ll_stypeLayoutOne;
        private ImageView icv_InfoImage;
        private TextView tv_infoNameOne, tv_infoNameTwo, tv_infoNameThree;
        private GridView gv_infoImage;
        private RelativeLayout rl_layoutOne;
        private LinearLayout ll_layoutTwo, ll_layoutThree;
        private TextView tv_commentCount;
        private TextView tv_time;
        private TextView tv_prase;
        private TextView tv_visit;
        private ImageView iv_infoImageOne, iv_infoImageThree;
        private BorderTextView btv_typeImage, btv_jianImage;
        // 活动
        private RelativeLayout rl_stypeLayoutTwo;
        private ImageView iv_activeImage;
        private TextView tv_activeName;
        private ImageView iv_status;
        private TextView tv_startTime;
        private TextView tv_endTime;
        // 圈子
        private LinearLayout ll_stypeLayoutThree;
        private ImageView iv_userHeader;
        private TextView tv_userName;
        private TextView tv_friendCity;
        private TextView tv_feedTime;
        private TextView tv_content;
        private TextView tv_imageCount;
        private GridView gv_feedImage;
        private ImageView iv_footerImage;
        private TextView tv_fishName;
        private TextView tv_fishType;
        private TextView tv_clound;
        private TextView tv_distance;
        private TextView tv_prise;
        private TextView tv_comment;
        private ImageView iv_gpsIcon;
        private ImageView iv_tagIcon;
        private TextView tv_tagCount;
        private ImageView iv_jing;
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
                convertView = mInflater.inflate(R.layout.item_info_grid, null,
                        false);
                viewholder.icv_InfoImage = (ImageView) convertView
                        .findViewById(R.id.info_image);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            // viewholder.icv_InfoImage.setImageResource(R.drawable.loadding_icon);
            // if (!mImageList.get(position).equals(
            // viewholder.icv_InfoImage.getTag())) {
            // viewholder.icv_InfoImage.setTag(mImageList.get(position));
            // ImageLoaderWrapper.getDefault().displayImage(
            // mImageList.get(position),
            // viewholder.icv_InfoImage,
            // new ImageLoaderWrapper.DisplayConfig.Builder()
            // .buildRounded(12), R.drawable.loadding_icon);
            // }
            ImageUtils.getInstance().loadImage(getActivity(),
                    mImageList.get(position), R.drawable.loadding_icon,
                    viewholder.icv_InfoImage);
            // ImageLoaderWrapper.getDefault().displayImage(
            // mImageList.get(position), viewholder.icv_InfoImage);
            return convertView;
        }
    }

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initFooter();
        initView(frView);
        initData();
    }

}
