package com.goby.fishing.module.index;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.MenuListBean;
import com.example.controller.bean.MenuListBean.Data.MenuBean;
import com.example.controller.bean.RecommendsListBean;
import com.example.controller.bean.RecommendsListBean.Data.RecommendsBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.WebViewActivity;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.fishing.FishingDetailActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.information.FishingInfoDetailActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

public class IndexFragment extends BaseFragment {

    private ListView lv_index;

    private View headerView;

    private PullRefreshLayout mPullRefreshLayout;

    private LayoutInflater mInflater;

    private IndexAdapter adapter;

    private ArrayList<RecommendsBean> dataList = new ArrayList<RecommendsBean>();

    private GridView gv_indexMenu;

    private MenuAdapter menuAdapter;

    private ArrayList<MenuBean> menuDataList = new ArrayList<MenuBean>();

    private SharedPreferenceUtil sharedPreferenceUtil;

    private boolean isShowing = true;

    private TextView tv_market, tv_game;

    public static IndexFragment newInstance() {
        return new IndexFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_index, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

        tv_market = (TextView) view.findViewById(R.id.market_text);
        tv_market.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AsyncWebActivity.launch(getActivity(), "https://wap.koudaitong.com/v2/showcase/homepage?kdt_id=17866933", "商城");
            }
        });
        tv_game = (TextView) view.findViewById(R.id.game_text);
        tv_game.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "indexFragment");
                } else {
                    GameActivity.launch(getActivity());
                }
            }
        });
        lv_index = (ListView) view.findViewById(R.id.index_list);
        headerView = mInflater.inflate(R.layout.index_list_header, null);
        gv_indexMenu = (GridView) headerView.findViewById(R.id.index_menu_grid);
        menuAdapter = new MenuAdapter();
        gv_indexMenu.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_indexMenu.setAdapter(menuAdapter);
        mPullRefreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        lv_index.setVisibility(View.GONE);
        lv_index.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv_index.addHeaderView(headerView);
        adapter = new IndexAdapter();
        lv_index.setAdapter(adapter);

        mPullRefreshLayout
                .setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {

                        // 刷新
                        isShowing = false;
                        getRemote();

                    }
                });
        lv_index.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position > 0) {

                    if (!TextUtils.isEmpty(dataList.get(position - 1).content_url)) {
                        if (dataList.get(position - 1).content_url
                                .startsWith("http")) {
                            WebViewActivity.launch(getActivity(),
                                    dataList.get(position - 1).content_url,
                                    dataList.get(position - 1).title);
                        } else {
                            String activeString = dataList.get(position - 1).content_url
                                    .replace("gobyfish://", "");
                            if (activeString.contains("/")) {
                                if (activeString.split("/")[0].equals("fishfeed")) {// 单个动态
                                    FooterprintDetailActivity.launch(getActivity(),
                                            Integer.parseInt(activeString
                                                    .split("/")[1]), null, 0,
                                            "index");
                                } else if (activeString.split("/")[0]
                                        .equals("info")) {// 单个资讯
                                    FishingInfoDetailActivity.launch(getActivity(),
                                            Integer.parseInt(activeString
                                                    .split("/")[1]), 0, "index",
                                            null);
                                } else if (activeString.split("/")[0]
                                        .equals("fishpoint")) {// 单个钓点
                                    FishingDetailActivity.launch(getActivity(),
                                            Integer.parseInt(activeString
                                                    .split("/")[1]), 0, "index",
                                            null);
                                } else if (activeString.split("/")[0]
                                        .equals("activity")) {// 单个活动
                                    ActiveDetailActivity.launch(getActivity(),
                                            activeString.split("/")[1]);
                                } else if (activeString.split("/")[0]
                                        .equals("infotag")) {// 标签资讯列表
                                    TagInfosActivity.launch(getActivity(),
                                            activeString.split("/")[1],
                                            dataList.get(position - 1).title, null);
                                }
                            } else {
                                if (activeString.split("/")[0].equals("fishfeed")) {// 动态列表
                                    //MainActivity.mFooterPrintLayout.performClick();
                                } else if (activeString.split("/")[0]
                                        .equals("info")) {// 资讯列表
                                    MainActivity.mInfornationLayout.performClick();
                                } else if (activeString.split("/")[0]
                                        .equals("fishpoint")) {// 钓点列表
                                    MainActivity.mFishingLayout.performClick();
                                } else if (activeString.split("/")[0]
                                        .equals("activity")) {// 活动列表
                                    ActiveListActivity.launch(getActivity());
                                } else if (activeString.split("/")[0]
                                        .equals("fishfun")) {// 游戏列表
                                    if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                                        ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                                        LoginActivity.launch(getActivity(), "indexFragment");
                                    } else {
                                        GameActivity.launch(getActivity());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        gv_indexMenu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(menuDataList.get(position).content_url)) {
                    if (menuDataList.get(position).content_url
                            .startsWith("http")) {
                        if (menuDataList.get(position).target
                                == 1) {
                            try {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(menuDataList
                                        .get(position).content_url);
                                intent.setData(content_url);
                                intent.setClassName("com.tencent.mtt",
                                        "com.tencent.mtt.MainActivity");
                                startActivity(intent);
                            } catch (Exception e) {
                                // TODO: handle exception
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(menuDataList
                                        .get(position).content_url);
                                intent.setData(content_url);
                                startActivity(intent);
                            }
                        } else {
                            WebViewActivity.launch(getActivity(),
                                    menuDataList.get(position).content_url,
                                    menuDataList.get(position).name);
                        }
                    } else {
                        String activeString = menuDataList.get(position).content_url
                                .replace("gobyfish://", "");
                        if (activeString.contains("/")) {
                            if (activeString.split("/")[0].equals("fishfeed")) {// 单个动态
                                FooterprintDetailActivity.launch(getActivity(),
                                        Integer.parseInt(activeString
                                                .split("/")[1]), null, 0,
                                        "index");
                            } else if (activeString.split("/")[0]
                                    .equals("info")) {// 单个资讯
                                FishingInfoDetailActivity.launch(getActivity(),
                                        Integer.parseInt(activeString
                                                .split("/")[1]), 0, "index",
                                        null);
                            } else if (activeString.split("/")[0]
                                    .equals("fishpoint")) {// 单个钓点
                                FishingDetailActivity.launch(getActivity(),
                                        Integer.parseInt(activeString
                                                .split("/")[1]), 0, "index",
                                        null);
                            } else if (activeString.split("/")[0]
                                    .equals("activity")) {// 单个活动
                                ActiveDetailActivity.launch(getActivity(),
                                        activeString.split("/")[1]);
                            } else if (activeString.split("/")[0]
                                    .equals("infotag")) {// 标签资讯列表
                                TagInfosActivity.launch(getActivity(),
                                        activeString.split("/")[1],
                                        menuDataList.get(position).name, null);
                            }
                        } else {
                            if (activeString.split("/")[0].equals("fishfeed")) {// 动态列表
                                //MainActivity.mFooterPrintLayout.performClick();
                            } else if (activeString.split("/")[0]
                                    .equals("info")) {// 资讯列表
                                MainActivity.mInfornationLayout.performClick();
                            } else if (activeString.split("/")[0]
                                    .equals("fishpoint")) {// 钓点列表
                                MainActivity.mFishingLayout.performClick();
                            } else if (activeString.split("/")[0]
                                    .equals("activity")) {// 活动列表
                                ActiveListActivity.launch(getActivity());
                            } else if (activeString.split("/")[0]
                                    .equals("fishfun")) {// 游戏列表
                                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                                    LoginActivity.launch(getActivity(), "indexFragment");
                                } else {
                                    GameActivity.launch(getActivity());
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void initData() {
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getMenu())
                && !TextUtils.isEmpty(sharedPreferenceUtil.getRecommend())) {
            Gson gson = new Gson();
            MenuListBean mMenuListBean = gson.fromJson(
                    sharedPreferenceUtil.getMenu(), MenuListBean.class);
            menuDataList.addAll(mMenuListBean.data.list);
            RecommendsListBean mRecommendsListBean = gson.fromJson(
                    sharedPreferenceUtil.getRecommend(),
                    RecommendsListBean.class);
            dataList.addAll(mRecommendsListBean.data.list);
            headerView.setVisibility(View.VISIBLE);
            lv_index.setVisibility(View.VISIBLE);
            menuAdapter.notifyDataSetChanged();
            setGridViewHeightBasedOnChildren(gv_indexMenu, 4);
            adapter.notifyDataSetChanged();
            isShowing = false;
        }
        getRemote();
    }

    public void getRemote() {
        UserBusinessController.getInstance().getMenus(null, getVersionCode(), "2", new com.example.controller.controller.Listener<MenuListBean>() {
            @Override
            public void onStart(Object... params) {
                if (isShowing) {
                    showProgressDialog("正在获取数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(MenuListBean bean, Object... params) {

                menuDataList.clear();
                Gson gson = new Gson();
                sharedPreferenceUtil.setMenu(gson.toJson(bean));
                menuDataList.addAll(bean.data.list);
                UserBusinessController.getInstance().getRecommends(null, getVersionCode(), "2", new com.example.controller.controller.Listener<RecommendsListBean>() {
                    @Override
                    public void onStart(Object... params) {

                    }

                    @Override
                    public void onComplete(RecommendsListBean bean, Object... params) {

                        dismissProgressDialog();
                        mPullRefreshLayout.setRefreshing(false);
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setRecommend(gson.toJson(bean));
                        headerView.setVisibility(View.VISIBLE);
                        lv_index.setVisibility(View.VISIBLE);
                        dataList.clear();
                        dataList.addAll(bean.data.list);
                        menuAdapter.notifyDataSetChanged();
                        setGridViewHeightBasedOnChildren(gv_indexMenu, 4);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        ToastHelper.showToast(getActivity(), msg);
                    }
                });

            }

            @Override
            public void onFail(String msg, Object... params) {
                if (isShowing) {
                    dismissProgressDialog();
                }
                mPullRefreshLayout.setRefreshing(false);
                ToastHelper.showToast(getActivity(), msg);
            }
        });
    }

    private class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return menuDataList.size();
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
                convertView = mInflater.inflate(R.layout.item_index_menu, null,
                        false);
                viewholder.iv_menuIcon = (ImageView) convertView
                        .findViewById(R.id.menu_icon);
                // viewholder.tv_menuName = (TextView) convertView
                // .findViewById(R.id.menu_name);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.iv_menuIcon.setBackgroundResource(R.drawable.find_icon);
            ImageLoaderWrapper.getDefault()
                    .displayImage(menuDataList.get(position).icon_url,
                            viewholder.iv_menuIcon);
            // viewholder.tv_menuName.setText(menuDataList.get(position).name);
            return convertView;
        }
    }

    private class IndexAdapter extends BaseAdapter {

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
                convertView = mInflater.inflate(R.layout.item_index_fishing,
                        null, false);
                viewholder.icv_fishingImage = (ImageView) convertView
                        .findViewById(R.id.index_image);
                // viewholder.ll_fishingNameLayout =(LinearLayout)
                // convertView.findViewById(R.id.fishing_name_layout);
                // viewholder.lineView = (View)
                // convertView.findViewById(R.id.line);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
//			viewholder.icv_fishingImage
//					.setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault()
                    .displayImage(dataList.get(position).pic_url,
                            viewholder.icv_fishingImage);
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView icv_fishingImage;
        private ImageView iv_menuIcon;
        // private TextView tv_menuName;
        // private LinearLayout ll_fishingNameLayout;
        // private View lineView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("IndexFragment"); // 友盟统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。)
        MobclickAgent.onResume(getActivity()); // 友盟统计时长
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("IndexFragment"); // 友盟统计（仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息。
        MobclickAgent.onPause(getActivity());
    }
}
