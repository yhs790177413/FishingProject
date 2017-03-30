package com.goby.fishing.module.index;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.baoyz.widget.PullRefreshLayout;
import com.example.controller.Constant;
import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.ActivityDetailBean;
import com.example.controller.bean.ActivityDetailBean.Data.JoinUserBean;
import com.example.controller.bean.JoinActiveBean;
import com.example.controller.bean.JoinUsersListBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.SlideShowView;
import com.goby.fishing.common.utils.helper.android.app.WebViewActivity;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.goby.fishing.module.other.OtherInfoActivity;
import com.umeng.socialize.UMShareAPI;

public class ActiveDetailActivity extends AbsBaseActivity implements
        OnClickListener {

    private ListView lv_joinPeople;

    private JoinPeopleAdapter adapter;

    private SlideShowView slideShowView;

    private View headerView;

    private String id;

    private ImageView iv_status;

    private UIHandler uiHandler;

    private ShareDialogUtils dialog_share;

    private String giftUrl, activeUrl;

    private TextView tv_activeName, tv_activeJoinCount, tv_activeTotalCount,
            tv_activeNeedCount, tv_myJoinCount, tv_myIntegral, tv_activeDetail;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private LinearLayout ll_bottom, ll_leftBack;

    private View v_line;

    private TextView tv_joinBtn, tv_shareBtn, tv_joinPeopleTips;

    private ProgressBar mProgressBar;

    private int joinCount, totalCount, needCount;

    private boolean isFresh = false;

    private RelativeLayout rl_giftDetail;

    private FrameLayout fl_activeDetail, fl_myInfo;

    private ArrayList<JoinUserBean> dataList = new ArrayList<JoinUserBean>();

    private int statusCode;

    private boolean isJoinable = true;

    private PullRefreshLayout mPullRefreshLayout;

    private View footerView;

    private View loadMore; // 加载更多的view

    private View loading; // 加载进度条

    private int page = 1;

    private int number = 20;

    public static boolean isResume = false;

    public static void launch(Activity activity, String id) {

        Intent intent = new Intent(activity, ActiveDetailActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_detail);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        id = getIntent().getStringExtra("id");
        uiHandler = new UIHandler();
        initFooter();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (slideShowView != null) {
            slideShowView.destoryBitmaps();
        }
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
        footerView.setVisibility(View.GONE);
    }

    public void initView() {

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
        ll_bottom = (LinearLayout) findViewById(R.id.bottom_layout);
        v_line = (View) findViewById(R.id.comment_line);
        headerView = LayoutInflater.from(this).inflate(
                R.layout.active_detail_header, null);
        tv_activeDetail = (TextView) headerView
                .findViewById(R.id.active_detail_text);
        rl_giftDetail = (RelativeLayout) headerView
                .findViewById(R.id.gift_detail_layout);
        fl_activeDetail = (FrameLayout) headerView
                .findViewById(R.id.active_detail_layout);
        tv_joinPeopleTips = (TextView) headerView
                .findViewById(R.id.join_people_tips);
        iv_status = (ImageView) headerView
                .findViewById(R.id.active_status_image);
        fl_myInfo = (FrameLayout) headerView.findViewById(R.id.my_info_layout);
        rl_giftDetail.setOnClickListener(this);
        fl_activeDetail.setOnClickListener(this);
        mProgressBar = (ProgressBar) headerView
                .findViewById(R.id.pic_ProgressBar);
        tv_activeName = (TextView) headerView.findViewById(R.id.active_name);
        tv_activeJoinCount = (TextView) headerView
                .findViewById(R.id.join_count);
        tv_activeTotalCount = (TextView) headerView
                .findViewById(R.id.total_join_count);
        tv_activeNeedCount = (TextView) headerView
                .findViewById(R.id.need_join_count);
        tv_myJoinCount = (TextView) headerView.findViewById(R.id.me_join_count);
        tv_myIntegral = (TextView) headerView.findViewById(R.id.my_integral);
        slideShowView = (SlideShowView) headerView
                .findViewById(R.id.slideshowView);
        slideShowView.destoryBitmaps();
        lv_joinPeople = (ListView) findViewById(R.id.join_people_list);
        lv_joinPeople.addHeaderView(headerView);
        lv_joinPeople.addFooterView(footerView);
        adapter = new JoinPeopleAdapter();
        lv_joinPeople.setAdapter(adapter);
        headerView.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.GONE);
        v_line.setVisibility(View.GONE);
        tv_joinBtn = (TextView) findViewById(R.id.join_btn);
        tv_joinBtn.setOnClickListener(this);
        tv_shareBtn = (TextView) findViewById(R.id.share_btn);
        tv_shareBtn.setOnClickListener(this);
        mPullRefreshLayout
                .setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        isFresh = true;
                        if (lv_joinPeople.getFooterViewsCount() == 0) {
                            lv_joinPeople.addFooterView(footerView);
                        }
                        UserBusinessController.getInstance().getActivityJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", id, new com.example.controller.controller.Listener<ActivityDetailBean>() {
                            @Override
                            public void onStart(Object... params) {

                            }

                            @Override
                            public void onComplete(ActivityDetailBean bean, Object... params) {
                                dismissProgressDialog();
                                mPullRefreshLayout.setRefreshing(false);
                                dataList.clear();
                                headerView.setVisibility(View.VISIBLE);
                                ll_bottom.setVisibility(View.VISIBLE);
                                v_line.setVisibility(View.VISIBLE);
                                dataList.addAll(bean.data.join_users);
                                if (!isFresh) {
                                    String[] dataImage = new String[bean.data.pic_urls.size()];
                                    for (int i = 0; i < bean.data.pic_urls.size(); i++) {
                                        dataImage[i] = bean.data.pic_urls.get(i);
                                    }
                                    slideShowView.initData(dataImage, null);
                                }
                                int progress = 0;
                                if (bean.data.join_users_number > 0) {
                                    progress = bean.data.join_users_number * 100
                                            / bean.data.total;
                                }
                                mProgressBar.setProgress(progress);
                                tv_activeName.setText(bean.data.name);
                                tv_activeJoinCount.setText("已参与" + bean.data.join_users_number
                                        + "次");
                                tv_activeTotalCount.setText("总需" + bean.data.total + "次");
                                tv_activeNeedCount.setText("剩余" + bean.data.remain + "次");
                                tv_myJoinCount.setText("您参与了" + bean.data.me.joined_number
                                        + "次");
                                tv_myIntegral.setText("您剩余" + bean.data.me.integral + "个鱼票");
                                tv_activeDetail.setText("活动详情(每次参与消耗" + bean.data.integral
                                        + "鱼票)");
                                if (bean.data.me.integral < bean.data.integral) {
                                    isJoinable = false;
                                }
                                joinCount = bean.data.join_users_number;
                                totalCount = bean.data.total;
                                needCount = bean.data.remain;
                                giftUrl = bean.data.detail;
                                activeUrl = bean.data.rule_url;
                                adapter.notifyDataSetChanged();
                                statusCode = bean.data.status;
                                if (bean.data.status == 0 || bean.data.status == 1) {// 预告
                                    tv_joinBtn.setBackgroundResource(R.drawable.start_btn_icon);
                                    iv_status
                                            .setBackgroundResource(R.drawable.active_start_icon);
                                    fl_myInfo.setVisibility(View.GONE);
                                } else if (bean.data.status == 3) {// 开始
                                    tv_joinBtn.setBackgroundResource(R.drawable.join_btn_icon);
                                    iv_status
                                            .setBackgroundResource(R.drawable.active_opening_icon);
                                    fl_myInfo.setVisibility(View.VISIBLE);
                                } else if (bean.data.status == 5) {// 结束
                                    tv_joinBtn.setBackgroundResource(R.drawable.end_btn_icon);
                                    iv_status.setBackgroundResource(R.drawable.active_end_icon);
                                    fl_myInfo.setVisibility(View.GONE);
                                }
                                if (bean.data.join_users != null
                                        && bean.data.join_users.size() > 0) {
                                    tv_joinPeopleTips.setVisibility(View.VISIBLE);
                                } else {
                                    tv_joinPeopleTips.setVisibility(View.GONE);
                                }
                                if (dataList.size() < 20) {
                                    lv_joinPeople.removeFooterView(footerView);
                                    lv_joinPeople.setOnScrollListener(null);
                                } else {
                                    footerView.setVisibility(View.VISIBLE);
                                    lv_joinPeople.setOnScrollListener(new UpdateListener());
                                }
                            }


                            @Override
                            public void onFail(String msg, Object... params) {
                                ToastHelper.showToast(ActiveDetailActivity.this, msg);
                            }
                        });

                    }
                });
        if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
            tv_joinBtn.setText("登录");
        }
    }

    public void initData() {
        UserBusinessController.getInstance().getActivityJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", id, new com.example.controller.controller.Listener<ActivityDetailBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("正在获取数据中,请稍候...");
            }

            @Override
            public void onComplete(ActivityDetailBean bean, Object... params) {
                dismissProgressDialog();
                mPullRefreshLayout.setRefreshing(false);

                dataList.clear();
                headerView.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.VISIBLE);
                v_line.setVisibility(View.VISIBLE);
                dataList.addAll(bean.data.join_users);
                if (!isFresh) {
                    String[] dataImage = new String[bean.data.pic_urls.size()];
                    for (int i = 0; i < bean.data.pic_urls.size(); i++) {
                        dataImage[i] = bean.data.pic_urls.get(i);
                    }
                    slideShowView.initData(dataImage, null);
                }
                int progress = 0;
                if (bean.data.join_users_number > 0) {
                    progress = bean.data.join_users_number * 100
                            / bean.data.total;
                }
                mProgressBar.setProgress(progress);
                tv_activeName.setText(bean.data.name);
                tv_activeJoinCount.setText("已参与" + bean.data.join_users_number
                        + "次");
                tv_activeTotalCount.setText("总需" + bean.data.total + "次");
                tv_activeNeedCount.setText("剩余" + bean.data.remain + "次");
                tv_myJoinCount.setText("您参与了" + bean.data.me.joined_number
                        + "次");
                tv_myIntegral.setText("您剩余" + bean.data.me.integral + "个鱼票");
                tv_activeDetail.setText("活动详情(每次参与消耗" + bean.data.integral
                        + "鱼票)");
                if (bean.data.me.integral < bean.data.integral) {
                    isJoinable = false;
                }
                joinCount = bean.data.join_users_number;
                totalCount = bean.data.total;
                needCount = bean.data.remain;
                giftUrl = bean.data.detail;
                activeUrl = bean.data.rule_url;
                adapter.notifyDataSetChanged();
                statusCode = bean.data.status;
                if (bean.data.status == 0 || bean.data.status == 1) {// 预告
                    tv_joinBtn.setBackgroundResource(R.drawable.start_btn_icon);
                    iv_status
                            .setBackgroundResource(R.drawable.active_start_icon);
                    fl_myInfo.setVisibility(View.GONE);
                } else if (bean.data.status == 3) {// 开始
                    tv_joinBtn.setBackgroundResource(R.drawable.join_btn_icon);
                    iv_status
                            .setBackgroundResource(R.drawable.active_opening_icon);
                    fl_myInfo.setVisibility(View.VISIBLE);
                } else if (bean.data.status == 5) {// 结束
                    tv_joinBtn.setBackgroundResource(R.drawable.end_btn_icon);
                    iv_status.setBackgroundResource(R.drawable.active_end_icon);
                    fl_myInfo.setVisibility(View.GONE);
                }
                if (bean.data.join_users != null
                        && bean.data.join_users.size() > 0) {
                    tv_joinPeopleTips.setVisibility(View.VISIBLE);
                } else {
                    tv_joinPeopleTips.setVisibility(View.GONE);
                }
                if (dataList.size() < 20) {
                    lv_joinPeople.removeFooterView(footerView);
                    lv_joinPeople.setOnScrollListener(null);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                    lv_joinPeople.setOnScrollListener(new UpdateListener());
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(ActiveDetailActivity.this, msg);
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
                    UserBusinessController.getInstance().getJoinUsers(null, getVersionCode(), "2", id, page, number, new com.example.controller.controller.Listener<JoinUsersListBean>() {
                        @Override
                        public void onStart(Object... params) {

                        }

                        @Override
                        public void onComplete(JoinUsersListBean bean, Object... params) {

                            if (bean.data.list.size() < 20) {
                                lv_joinPeople.removeFooterView(footerView);
                                lv_joinPeople.setOnScrollListener(null);
                            } else {
                                lv_joinPeople.setOnScrollListener(new UpdateListener());
                            }
                            dataList.addAll(bean.data.list);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            ToastHelper.showToast(ActiveDetailActivity.this, msg);
                        }
                    });
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    private class JoinPeopleAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(ActiveDetailActivity.this)
                        .inflate(R.layout.item_active_detail_list, null, false);
                viewholder.iv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_joinTime = (TextView) convertView
                        .findViewById(R.id.join_time);
                viewholder.tv_joinNumber = (TextView) convertView
                        .findViewById(R.id.join_number);
                viewholder.iv_win = (ImageView) convertView
                        .findViewById(R.id.win_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.iv_userHeader.setImageResource(R.drawable.header_circle_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    dataList.get(position).head_pic, viewholder.iv_userHeader);
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
            if (dataList.get(position).add) {
                viewholder.tv_joinTime.setText("刚刚");
            } else {
                viewholder.tv_joinTime.setText(TimeUtil.getTimeString(dataList
                        .get(position).time * 1000));
            }
            viewholder.tv_joinNumber.setText("抽奖号码: "
                    + dataList.get(position).certificate);
            if (dataList.get(position).win == 0) {
                viewholder.iv_win.setVisibility(View.GONE);
            } else {
                viewholder.iv_win.setVisibility(View.VISIBLE);
            }
            viewholder.iv_userHeader.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (dataList.get(position).user_id
                            .equals(sharedPreferenceUtil.getUserID())) {
                        MainActivity.launch(ActiveDetailActivity.this,
                                "meFragment");
                    } else {
                        OtherInfoActivity.launch(ActiveDetailActivity.this,
                                dataList.get(position).user_id);
                    }
                }
            });
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView iv_userHeader;
        private TextView tv_userName;
        private TextView tv_joinTime;
        private TextView tv_joinNumber;
        private ImageView iv_win;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.join_btn:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    LoginActivity.launch(this, "active");
                } else {
                    if (statusCode == 0 || statusCode == 1) {// 预告
                        ToastHelper.showToast(this, "活动暂未开始");
                    } else if (statusCode == 3) {// 开始
                        if (isJoinable) {
                            int randomCode = (int) (Math.random() * 1000000 + 100000);
                            String code = String.valueOf(randomCode);
                            UserBusinessController.getInstance().joinActivityJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", id, code, new com.example.controller.controller.Listener<JoinActiveBean>() {
                                @Override
                                public void onStart(Object... params) {
                                    showProgressDialog("正在报名中,请稍候...");
                                }

                                @Override
                                public void onComplete(JoinActiveBean bean, Object... params) {
                                    dismissProgressDialog();

                                    ToastHelper.showToast(ActiveDetailActivity.this, "报名成功");
                                    joinCount = joinCount + 1;
                                    needCount = needCount - 1;
                                    int progress = joinCount * 100 / totalCount;
                                    mProgressBar.setProgress(progress);
                                    tv_activeJoinCount.setText("已参与" + joinCount + "次");
                                    tv_activeNeedCount.setText("剩余" + needCount + "次");
                                    tv_myJoinCount.setText("您参与了" + bean.data.join_number + "次");
                                    tv_myIntegral.setText("您剩余" + bean.data.integral + "个积分");
                                    JoinUserBean joinUserBean = new JoinUserBean();
                                    joinUserBean.user_name = sharedPreferenceUtil.getUserName();
                                    joinUserBean.head_pic = sharedPreferenceUtil.getUserHeadUrl();
                                    joinUserBean.add = true;
                                    joinUserBean.certificate = bean.data.certificate;
                                    dataList.add(0, joinUserBean);
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onFail(String msg, Object... params) {
                                    dismissProgressDialog();
                                    ToastHelper.showToast(ActiveDetailActivity.this, msg);
                                }
                            });
                        } else {
                            ToastHelper.showToast(this, "您没有足够的鱼票");
                        }
                    } else if (statusCode == 5) {// 结束
                        ToastHelper.showToast(this, "活动已结束");
                    }
                }
                break;
            case R.id.share_btn:
                // 弹出分享
                dialog_share = new ShareDialogUtils(this, R.style.dialog,
                        uiHandler, true);
                dialog_share.setCanceledOnTouchOutside(true);
                dialog_share.show();
                break;
            case R.id.gift_detail_layout:
                WebViewActivity.launch(this, giftUrl, "奖品详情");
                break;
            case R.id.active_detail_layout:
                WebViewActivity.launch(this, activeUrl, "活动详情");
                break;
            default:
                break;
        }
    }

    public class UIHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            dialog_share.initPlatform();
            String mTitle = "去哪里钓鱼,很好玩，你也一起来吧！";
            switch (msg.what) {
                case 0:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle,
                            takeScreenShot(ActiveDetailActivity.this), 0);
                    break;
                case 1:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle,
                            takeScreenShot(ActiveDetailActivity.this), 1);
                    break;
                case 2:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle,
                            takeScreenShot(ActiveDetailActivity.this), 2);
                    break;
                case 3:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle,
                            takeScreenShot(ActiveDetailActivity.this), 3);
                    break;
                case 4:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle,
                            takeScreenShot(ActiveDetailActivity.this), 4);
                    break;
            }

        }
    }

    /**
     * 分享平台的选择
     *
     * @param position
     */
    public void selectSharePaltform(String content, Bitmap bitmap, int position) {
        dialog_share.startShare(content, bitmap, position);
    }

    // 获取指定Activity的截屏，保存到png文件
    private Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isResume) {
            tv_joinBtn.setText(null);
            isResume = false;
            isFresh = true;
            initData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
