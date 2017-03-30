package com.goby.fishing.module.footerprint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.baoyz.widget.PullRefreshLayout;
import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.FooterprintListBean;
import com.example.controller.bean.FooterprintListBean.Data.List;
import com.example.controller.bean.FooterprintListBean.Data.List.Tag;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.fishing.AddFooterprintActivity;
import com.goby.fishing.module.fishing.BasicMapActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.google.gson.Gson;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FooterPrintFragment extends BaseFragment implements
        OnClickListener {

    private ListView lv_footerPrint;

    private PullRefreshLayout mPullRefreshLayout;

    private LinearLayout ll_error;

    private Button btn_reload;

    private TextView tv_addFooterprint;

    private FooterprintAdapter adapter;

    private ArrayList<String> imageData;

    private ImageGridAdapter imageAdapter;

    public static ArrayList<List> dataList = new ArrayList<List>();

    private int page = 1;

    private int number = 20;

    public static String active = "init";

    private int mPosition = 0;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    private boolean is_liking = false;

    private boolean isShowing = true;

    public static FooterPrintFragment newInstance() {
        return new FooterPrintFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_footerprint, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initFooter();
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

        mPullRefreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        ll_error = (LinearLayout) view.findViewById(R.id.error_layout);
        lv_footerPrint = (ListView) view.findViewById(R.id.footerprint_list);
        // adapter = new FooterprintAdapter();
        lv_footerPrint.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv_footerPrint.addFooterView(footerView);
        // lv_footerPrint.setAdapter(adapter);
        lv_footerPrint.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position - 1 <= dataList.size()) {
                    FooterprintDetailActivity.launch(getActivity(),
                            dataList.get(position).id,
                            dataList.get(position).pic_urls.get(0), position,
                            "footerfragment");
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
                        if (lv_footerPrint.getFooterViewsCount() > 0) {
                            lv_footerPrint.removeFooterView(footerView);
                        }
                        page = 1;
                        active = "refresh";
                        isShowing = false;
                        getRemote();

                    }
                });
        tv_addFooterprint = (TextView) view.findViewById(R.id.add_footerprint);
        tv_addFooterprint.setOnClickListener(this);
    }

    public void initData() {

        if (!TextUtils.isEmpty(sharedPreferenceUtil.getHotFeed())) {
            Gson gson = new Gson();
            FooterprintListBean mFooterprintListBean = gson.fromJson(
                    sharedPreferenceUtil.getHotFeed(),
                    FooterprintListBean.class);
            dataList.addAll(mFooterprintListBean.data.list);
            if (dataList.size() < 20) {
                lv_footerPrint.removeFooterView(footerView);
                lv_footerPrint.setOnScrollListener(null);
            } else {
                lv_footerPrint.setOnScrollListener(new UpdateListener());
            }
            adapter = new FooterprintAdapter();
            lv_footerPrint.setAdapter(adapter);
            isShowing = false;
            lv_footerPrint.setVisibility(View.VISIBLE);
        }
        getRemote();
    }

    public void getRemote() {
        double latitude = 0, longitude = 0;
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLongitude())) {
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
        }
        UserBusinessController.getInstance().getFishFeeds(null, getVersionCode(), "2", 0, page, number, 2, latitude, longitude, new com.example.controller.controller.Listener<FooterprintListBean>() {
            @Override
            public void onStart(Object... params) {
                if (isShowing) {
                    showProgressDialog("正在获取数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(FooterprintListBean bean, Object... params) {
                mPullRefreshLayout.setRefreshing(false);
                if (isShowing) {
                    dismissProgressDialog();
                }
                if (active.equals("refresh")) {
                    if (lv_footerPrint.getFooterViewsCount() > 0) {
                    } else {
                        lv_footerPrint.addFooterView(footerView);
                    }
                }

                lv_footerPrint.setVisibility(View.VISIBLE);
                if (active.equals("init") || active.equals("refresh")) {
                    if (bean.data.list.size() == 0) {
                        ToastHelper.showToast(getActivity(), "暂无数据");
                    } else if (bean.data.list.size() < 20) {
                        ToastHelper.showToast(getActivity(), "数据已全部加载完");
                        if (lv_footerPrint.getFooterViewsCount() > 0) {
                            lv_footerPrint.removeFooterView(footerView);
                        }
                    } else {
                        footerView.setVisibility(View.VISIBLE);
                        loadMore.setVisibility(View.VISIBLE);
                        lv_footerPrint
                                .setOnScrollListener(new UpdateListener());
                    }
                    dataList.clear();
                    dataList.addAll(bean.data.list);
                    adapter = new FooterprintAdapter();
                    lv_footerPrint.setAdapter(adapter);
                    Gson gson = new Gson();
                    sharedPreferenceUtil.setHotFeed(gson.toJson(bean));
                } else if (active.equals("update")) {
                    if (bean.data.list.size() == 0
                            || bean.data.list.size() < 20) {
                        lv_footerPrint.removeFooterView(footerView);
                        ToastHelper.showToast(getActivity(), "数据已全部加载完");
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
                mPullRefreshLayout.setRefreshing(false);
                ToastHelper.showToast(getActivity(), msg);
                if (isHidden()) {
                    dismissProgressDialog();
                }
                is_liking = false;
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
            case R.id.reload_btn:
                ll_error.setVisibility(View.GONE);
                initData();
                break;
            case R.id.add_footerprint:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    LoginActivity.launch(getActivity(), "meFragment", 0);
                } else {
                    if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLongitude())) {
                        AddFooterprintActivity.launch(getActivity(), "",
                                Double.parseDouble(sharedPreferenceUtil
                                        .getGPSLongitude()), Double
                                        .parseDouble(sharedPreferenceUtil
                                                .getGPSLatitude()), -1);
                    } else {
                        AddFooterprintActivity.launch(getActivity(), "", 0, 0, -1);
                    }
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
                convertView = LayoutInflater.from(getActivity()).inflate(
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
                viewholder.iv_jing = (ImageView) convertView.findViewById(R.id.jing_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
//			viewholder.iv_userHeader
//					.setImageResource(R.drawable.header_circle_icon);
            if (dataList.get(position).head_pic.startsWith("http://")) {
                ImageLoaderWrapper.getDefault().displayImage(
                        dataList.get(position).head_pic,
                        viewholder.iv_userHeader);
            } else {
                ImageLoaderWrapper.getDefault().displayImage(
                        Constant.HOST_URL + dataList.get(position).head_pic,
                        viewholder.iv_userHeader);
            }
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
            if (TextUtils.isEmpty(dataList.get(position).info)) {
                viewholder.tv_content.setVisibility(View.GONE);
            } else {
                viewholder.tv_content.setVisibility(View.VISIBLE);
                try {
                    viewholder.tv_content.setText(EmojiUtil.handlerEmojiText(
                            dataList.get(position).info, getActivity()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
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
                viewholder.iv_gpsIcon.setVisibility(View.GONE);
            } else {
                viewholder.iv_gpsIcon.setVisibility(View.VISIBLE);
                viewholder.tv_distance.setVisibility(View.VISIBLE);
                viewholder.tv_distance
                        .setText(dataList.get(position).address_info);
            }
            viewholder.tv_distance.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    BasicMapActivity.launch(getActivity(),
                            dataList.get(position).longitude,
                            dataList.get(position).latitude,
                            dataList.get(position).info,
                            dataList.get(position).pic_urls.get(0), "");
                }
            });
            viewholder.tv_prise.setText(dataList.get(position).like_number
                    + " 赞");
            viewholder.tv_prise.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                        ToastHelper
                                .showNewToast(getActivity(), "您还未登录,请先登录再点赞");
                    } else {
                        if (!is_liking) {
                            is_liking = true;
                            if (dataList.get(position).is_like == 0) {
                                mPosition = position;
                                UserBusinessController.getInstance().getLike(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", dataList.get(position).id, 3, 1, new com.example.controller.controller.Listener<BaseBean>() {
                                    @Override
                                    public void onStart(Object... params) {
                                        showProgressDialog("正在点赞中,请稍候...");
                                    }

                                    @Override
                                    public void onComplete(BaseBean bean, Object... params) {
                                        dismissProgressDialog();
                                        mPullRefreshLayout.setRefreshing(false);
                                        is_liking = false;

                                        dataList.get(mPosition).is_like = 1;
                                        dataList.get(mPosition).like_number = dataList.get(mPosition).like_number + 1;
                                        ToastHelper.showToast(getActivity(), "点赞成功");
                                        try {
                                            dataList.get(mPosition).tag.user_likes = dataList
                                                    .get(mPosition).tag.user_likes + 1;
                                        } catch (Exception e) {
                                            Tag tag = new Tag();
                                            tag.user_likes = 1;
                                            dataList.get(mPosition).tag = tag;
                                        }

                                        adapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onFail(String msg, Object... params) {
                                        dismissProgressDialog();
                                        ToastHelper.showToast(getActivity(), msg);
                                    }
                                });
                            } else if (dataList.get(position).is_like == 1) {
                                ToastHelper.showNewToast(getActivity(),
                                        "您已点过赞了");
                                is_liking = false;
                            }
                        }
                    }
                }
            });
            viewholder.tv_comment.setText(dataList.get(position).comment_number
                    + " 评论");
            imageData = new ArrayList<String>();
            imageData.addAll(dataList.get(position).pic_urls);
            imageAdapter = new ImageGridAdapter(imageData);
            viewholder.gv_infoImage.setAdapter(imageAdapter);
            try {
                if (dataList.get(position).tag.tag_id == 1) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.daily_type_icon);
                } else if (dataList.get(position).tag.tag_id == 2) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.wild_fish_type_icon);
                } else if (dataList.get(position).tag.tag_id == 3) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.black_pit_type_icon);
                } else if (dataList.get(position).tag.tag_id == 4) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.equipment_type_icon);
                } else if (dataList.get(position).tag.tag_id == 5) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.food_type_icon);
                } else if (dataList.get(position).tag.tag_id == 6) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.lures_type_icon);
                } else if (dataList.get(position).tag.tag_id == 7) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.fishing_type_icon);
                }
                viewholder.tv_tagCount.setText(String.valueOf(dataList
                        .get(position).tag.user_likes) + "人气");
            } catch (Exception e) {
                // TODO: handle exception
                viewholder.iv_tagIcon
                        .setBackgroundResource(R.drawable.daily_type_icon);
                viewholder.tv_tagCount.setText("0人气");
            }
            if (dataList
                    .get(position).jing == 0) {
                viewholder.iv_jing.setVisibility(View.GONE);
            } else {
                viewholder.iv_jing.setVisibility(View.VISIBLE);
            }
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
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_footer_image, null, false);
                viewholder.iv_footerImage = (ImageView) convertView
                        .findViewById(R.id.footer_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
//			viewholder.iv_footerImage
//					.setImageResource(R.drawable.loadding_icon);
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
