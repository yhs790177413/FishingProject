package com.goby.fishing.module.footerprint;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.emojilib.emoji.Emoji;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.emojilib.emoji.FaceFragment;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.CommentsBean;
import com.example.controller.bean.FooterprintDetailBean;
import com.example.controller.bean.CommentsBean.Data.Comment;
import com.example.controller.bean.FishingDetialBean.Data.FishTyes;
import com.example.controller.bean.FooterprintDetailBean.Data.Like;
import com.example.controller.bean.FooterprintDetailBean.Data.PicUrl;
import com.example.controller.bean.FooterprintListBean.Data.List.Tag;
import com.goby.fishing.common.photochoose.ImageBrowseActivity;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.fishing.BasicMapActivity;
import com.goby.fishing.module.fishing.FishAllTypeActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.goby.fishing.module.me.MyFooterprintActivity;
import com.goby.fishing.module.me.MyFooterprintFragment;
import com.goby.fishing.module.other.OtherInfoActivity;
import com.umeng.socialize.UMShareAPI;


public class FooterprintDetailActivity extends AbsBaseActivity implements
        OnClickListener, OnItemClickListener, FaceFragment.OnEmojiClickListener {

    private GridView gv_footerPrintImage;

    private GridView gv_userHeader;

    private FooterprintImageAdapter footerprintImageAdapter;

    private UserHeaderAdapter userHeaderAdapter;

    private CommentAdapter commnentAdapter;

    private ImageView icv_userHead;

    private TextView tv_userName;

    private TextView tv_time;

    private TextView tv_cityName;

    private TextView tv_info;

    private TextView tv_fishName;

    private ListView lv_comment;

    private TextView tv_commentTips;

    private TextView tv_commentCount;

    private ImageView iv_collection;

    private EditText et_comment;

    private ImageView iv_comment;

    private ImageView iv_location;

    private ImageView iv_fishType;

    private ImageView iv_fishNumber;

    private ImageView iv_isFree;

    private TextView tv_locationTips;

    private TextView tv_fishTypeTips;

    private TextView tv_fishTool;

    private TextView tv_weather;

    private TextView tv_fishPointInfo;

    private TextView tv_fishNumberTips;

    private TextView tv_isFreeTips;

    private LinearLayout ll_parent;

    private LinearLayout ll_fishType;

    private int mId, currentPosition;

    private boolean is_collection;

    private boolean isFirst = true;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private ArrayList<PicUrl> footerprintImageList = new ArrayList<PicUrl>();

    private ArrayList<Like> footerprintLikeList = new ArrayList<Like>();

    private ArrayList<Comment> footerprintCommentList = new ArrayList<Comment>();

    private ArrayList<FishTyes> fishTypeList = new ArrayList<FishTyes>();

    private int is_like;

    private int mCurrentPosition;

    private String user_id;

    private ShareDialogUtils dialog_share;

    private ImageView iv_share;

    private UIHandler uiHandler;

    private TextView tv_del;

    private LinearLayout ll_leftBack;

    private View headerView;

    private String mShareTitle = "";

    private String mShareContent = "去哪钓鱼精彩推荐";

    private int page = 1;

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条
    private boolean isLiking = false;

    private String reply_id = null; // 回复帖子id

    private String reply_user_id = null; // 回复人id

    private boolean isReply = false; // 是否回复

    private String replyName = ""; // 被回复人的名字

    private String comment;

    private boolean isShowEmoji = false;

    private TextView tv_emoji;
    private FrameLayout fm_emoji;

    private TextView tv_commentBtn;

    private ImageView iv_sendBtn;

    private FrameLayout fl_commentCount;

    private boolean isPush = false;

    private FooterprintDetailBean dataBean = new FooterprintDetailBean();

    private ImageView iv_tagIcon;

    private TextView tv_tagCount;

    private int likeNumber = 0;

    private TextView tv_addJing, tv_serverTips;

    private ImageView iv_jingIcon;

    private LinearLayout ll_gps;

    private String userHeader = "", userName = "", fishType = "",
            fishTips = "", fishArea = "", fishTime = "", fishComment = "";

    public static void launch(Activity activity, int id, String pic_url,
                              int currentPositiion, String fromActivity) {

        Intent intent = new Intent(activity, FooterprintDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("pic_url", pic_url);
        intent.putExtra("currentPositiion", currentPositiion);
        intent.putExtra("fromActivity", fromActivity);
        activity.startActivity(intent);
        // activity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footerprint_detail);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        uiHandler = new UIHandler();
        mId = getIntent().getIntExtra("id", -1);
        if (mId < 0) {
            isPush = true;
            sharedPreferenceUtil.setRedPointIsVisible(false);
            Bundle data = getIntent().getExtras();
            mId = Integer.parseInt(data.getString("id"));
        }
        mCurrentPosition = getIntent().getIntExtra("currentPositiion", -1);
        initView();
        initData();
        FaceFragment faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.Container, faceFragment).commit();
    }

    public void initView() {

        headerView = LayoutInflater.from(this).inflate(
                R.layout.footerprint_detail_header, null);
        ll_gps = (LinearLayout) headerView.findViewById(R.id.gps_layout);
        tv_addJing = (TextView) headerView.findViewById(R.id.add_jing_btn);
        tv_serverTips = (TextView) headerView.findViewById(R.id.server_tips);
        iv_jingIcon = (ImageView) headerView.findViewById(R.id.jing_image);
        if (sharedPreferenceUtil.getMember() == 9) {
            tv_addJing.setVisibility(View.VISIBLE);
        } else {
            tv_addJing.setVisibility(View.GONE);
        }
        iv_collection = (ImageView) findViewById(R.id.collection_view);
        ll_parent = (LinearLayout) headerView.findViewById(R.id.parent_layout);
        tv_del = (TextView) findViewById(R.id.del_btn);
        try {
            if (getIntent().getStringExtra("fromActivity").equals(
                    "myFooterprint")) {
                tv_del.setVisibility(View.VISIBLE);
            } else {
                tv_del.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            tv_del.setVisibility(View.GONE);
        }

        icv_userHead = (ImageView) headerView.findViewById(R.id.user_header);
        tv_userName = (TextView) headerView.findViewById(R.id.user_name);
        tv_time = (TextView) headerView.findViewById(R.id.time);
        tv_cityName = (TextView) headerView.findViewById(R.id.friend_city);
        tv_fishName = (TextView) headerView.findViewById(R.id.fish_name);
        tv_info = (TextView) headerView.findViewById(R.id.dynamic_content);
        tv_weather = (TextView) headerView.findViewById(R.id.weather);
        tv_fishPointInfo = (TextView) headerView
                .findViewById(R.id.fish_point_info);
        lv_comment = (ListView) findViewById(R.id.comment_list);
        lv_comment.setSelector(new ColorDrawable(Color.TRANSPARENT));
        tv_commentTips = (TextView) headerView.findViewById(R.id.comment_tips);
        tv_commentCount = (TextView) findViewById(R.id.comment_count);
        et_comment = (EditText) findViewById(R.id.edit_comment);
        tv_fishTool = (TextView) headerView.findViewById(R.id.fish_tool);
        gv_footerPrintImage = (GridView) headerView
                .findViewById(R.id.footerprint_image);
        gv_footerPrintImage.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_userHeader = (GridView) headerView
                .findViewById(R.id.user_header_grid);
        iv_location = (ImageView) headerView.findViewById(R.id.location_name);
        iv_fishType = (ImageView) headerView.findViewById(R.id.fish_type_view);
        iv_fishNumber = (ImageView) headerView
                .findViewById(R.id.fish_number_view);
        iv_isFree = (ImageView) headerView.findViewById(R.id.is_free_view);

        tv_locationTips = (TextView) headerView
                .findViewById(R.id.location_tips);
        tv_fishTypeTips = (TextView) headerView
                .findViewById(R.id.fish_type_tips);
        tv_fishNumberTips = (TextView) headerView
                .findViewById(R.id.fish_number_tips);
        tv_isFreeTips = (TextView) headerView.findViewById(R.id.free_tips);
        ll_fishType = (LinearLayout) headerView
                .findViewById(R.id.fish_type_layout);
        tv_emoji = (TextView) findViewById(R.id.emoji_text);
        fm_emoji = (FrameLayout) findViewById(R.id.Container);

        tv_commentBtn = (TextView) findViewById(R.id.comment_btn);
        iv_sendBtn = (ImageView) findViewById(R.id.send_btn);
        fl_commentCount = (FrameLayout) findViewById(R.id.fragment_send);

        footerprintImageAdapter = new FooterprintImageAdapter();
        userHeaderAdapter = new UserHeaderAdapter();
        commnentAdapter = new CommentAdapter();
        lv_comment.addHeaderView(headerView);
        gv_footerPrintImage.setAdapter(footerprintImageAdapter);
        gv_userHeader.setAdapter(userHeaderAdapter);
        lv_comment.setAdapter(commnentAdapter);
        iv_comment = (ImageView) findViewById(R.id.comment_view);
        iv_share = (ImageView) findViewById(R.id.detail_share_view);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        iv_tagIcon = (ImageView) headerView.findViewById(R.id.tag_icon);
        tv_tagCount = (TextView) headerView.findViewById(R.id.tag_count);

        ll_leftBack.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
        gv_userHeader.setOnItemClickListener(this);
        iv_comment.setOnClickListener(this);
        icv_userHead.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        tv_emoji.setOnClickListener(this);
        tv_commentBtn.setOnClickListener(this);
        iv_sendBtn.setOnClickListener(this);
        et_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fm_emoji.setVisibility(View.GONE);
                iv_collection.setVisibility(View.GONE);
                iv_share.setVisibility(View.GONE);
                isShowEmoji = false;
            }
        });
        et_comment.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean onFocus) {
                // TODO Auto-generated method stub
                if (onFocus) {
                    iv_collection.setVisibility(View.GONE);
                    iv_share.setVisibility(View.GONE);
                } else {
                    tv_emoji.setVisibility(View.GONE);
                    fl_commentCount.setVisibility(View.VISIBLE);
                    iv_sendBtn.setVisibility(View.GONE);
                    iv_collection.setVisibility(View.VISIBLE);
                    iv_share.setVisibility(View.VISIBLE);
                    tv_commentBtn.setVisibility(View.VISIBLE);
                    et_comment.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
                }
                fm_emoji.setVisibility(View.GONE);
                isShowEmoji = false;
            }
        });
        lv_comment.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                iv_collection.setVisibility(View.VISIBLE);
                iv_share.setVisibility(View.VISIBLE);
                tv_commentBtn.setVisibility(View.VISIBLE);
                et_comment.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
            }
        });
        tv_addJing.setOnClickListener(this);
    }

    public void initData() {
        footerprintLikeList.clear();
        footerprintImageList.clear();
        footerprintCommentList.clear();
        double latitude = 0, longitude = 0;
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLongitude())) {
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
        }
        UserBusinessController.getInstance().footerprintDetail(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, latitude, longitude, new com.example.controller.controller.Listener<FooterprintDetailBean>() {
            @Override
            public void onStart(Object... params) {
                if (isFirst) {
                    showProgressDialog("正在获取数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(FooterprintDetailBean bean, Object... params) {
                if (isFirst) {
                    dismissProgressDialog();
                }

                dataBean = bean;
                isFirst = false;
                user_id = bean.data.user_id;
                ll_parent.setVisibility(View.VISIBLE);
                if (bean.data.head_pic.startsWith("http://")) {
                    ImageLoaderWrapper.getDefault().displayImage(
                            bean.data.head_pic, icv_userHead);
                } else {
                    ImageLoaderWrapper.getDefault()
                            .displayImage(
                                    Constant.HOST_URL + bean.data.head_pic,
                                    icv_userHead);
                }
                mShareTitle = bean.data.user_name;
                tv_userName.setText(bean.data.user_name);
                tv_time.setText(TimeUtil.getTimeString(bean.data.time * 1000));
                if (TextUtils.isEmpty(bean.data.city_name)) {
                    tv_cityName.setVisibility(View.GONE);
                } else {
                    tv_cityName.setVisibility(View.VISIBLE);
                    tv_cityName.setText(bean.data.city_name);
                }
                if (TextUtils.isEmpty(bean.data.fishInfo)) {
                    tv_fishName.setVisibility(View.GONE);
                } else {
                    tv_fishName.setVisibility(View.VISIBLE);
                    tv_fishName.setText(bean.data.fishInfo);
                }
                if (TextUtils.isEmpty(bean.data.tools)) {
                    tv_fishTool.setVisibility(View.GONE);
                } else {
                    tv_fishTool.setVisibility(View.VISIBLE);
                    tv_fishTool.setText(bean.data.tools);
                }
                if (TextUtils.isEmpty(bean.data.weather)) {
                    tv_weather.setVisibility(View.GONE);
                } else {
                    tv_weather.setVisibility(View.VISIBLE);
                    tv_weather.setText(bean.data.weather);
                }
                if (!TextUtils.isEmpty(bean.data.address_info)) {
                    tv_fishPointInfo.setText(bean.data.address_info);
                } else {
                    ll_gps.setVisibility(View.GONE);
                }
                tv_fishPointInfo
                        .setOnClickListener(FooterprintDetailActivity.this);
                if (!TextUtils.isEmpty(bean.data.fishPoint_name)
                        && !bean.data.fishPoint_name.equals("未知")) {
                    iv_location.setBackgroundResource(R.drawable.location_icon);
                    tv_locationTips.setText(bean.data.fishPoint_name);
                }
                if (bean.data.fish_types != null
                        && bean.data.fish_types.size() > 0) {
                    fishTypeList.clear();
                    iv_fishType
                            .setBackgroundResource(R.drawable.many_types_icon);
                    fishTypeList.addAll(bean.data.fish_types);
                    ll_fishType
                            .setOnClickListener(FooterprintDetailActivity.this);
                }
                if (bean.data.number > 0) {
                    iv_fishNumber
                            .setBackgroundResource(R.drawable.many_fishes_icon);
                    tv_fishNumberTips.setText("渔获" + bean.data.number + "尾");

                } else if (bean.data.number == -1) {
                    iv_fishNumber
                            .setBackgroundResource(R.drawable.many_fishes_icon);
                    tv_fishNumberTips.setText("此处爆护");
                }
                if (bean.data.price > 0) {
                    iv_isFree.setBackgroundResource(R.drawable.no_free_icon);
                    tv_isFreeTips.setText("此处收费");
                }
                if (TextUtils.isEmpty(bean.data.info)) {
                    tv_info.setVisibility(View.GONE);
                } else {
                    try {
                        tv_info.setText(EmojiUtil.handlerEmojiText(
                                bean.data.info, FooterprintDetailActivity.this));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mShareContent = bean.data.info;
                }

                if (bean.data.is_favorite == 0) {
                    is_collection = false;
                    iv_collection
                            .setBackgroundResource(R.drawable.collection_icon);
                } else {
                    is_collection = true;
                    iv_collection
                            .setBackgroundResource(R.drawable.had_collection_icon);
                }
                is_like = bean.data.is_like;
                if (bean.data.likes != null && bean.data.likes.size() > 0) {
                    footerprintLikeList.clear();
                    likeNumber = bean.data.like_number;
                    footerprintLikeList.addAll(bean.data.likes);
                    userHeaderAdapter.notifyDataSetChanged();
                }
                if (bean.data.comments != null && bean.data.comments.size() > 0) {
                    tv_commentTips.setVisibility(View.VISIBLE);
                    lv_comment.setVisibility(View.VISIBLE);
                    tv_commentCount.setVisibility(View.VISIBLE);
                    tv_commentCount.setText(String
                            .valueOf(bean.data.comment_number));
                    footerprintCommentList.addAll(bean.data.comments);
                    commnentAdapter.notifyDataSetChanged();
                    if (bean.data.comments.size() < 20) {
                        lv_comment.setOnScrollListener(null);
                    } else {
                        initFooter();
                        lv_comment.setOnScrollListener(new UpdateListener());
                    }
                    // setListViewHeightBasedOnChildren(lv_comment);
                } else {
                    tv_commentCount.setVisibility(View.GONE);
                    tv_commentTips.setVisibility(View.GONE);
                }
                if (bean.data.pic_urls != null && bean.data.pic_urls.size() > 0) {
                    footerprintImageList.addAll(bean.data.pic_urls);
                    footerprintImageAdapter.notifyDataSetChanged();
                    setGridViewHeightBasedOnChildren(gv_footerPrintImage, 3);
                }
                try {
                    if (bean.data.tag.tag_id == 1) {
                        iv_tagIcon
                                .setBackgroundResource(R.drawable.daily_type_icon);
                    } else if (bean.data.tag.tag_id == 2) {
                        iv_tagIcon
                                .setBackgroundResource(R.drawable.wild_fish_type_icon);
                    } else if (bean.data.tag.tag_id == 3) {
                        iv_tagIcon
                                .setBackgroundResource(R.drawable.black_pit_type_icon);
                    } else if (bean.data.tag.tag_id == 4) {
                        iv_tagIcon
                                .setBackgroundResource(R.drawable.equipment_type_icon);
                    } else if (bean.data.tag.tag_id == 5) {
                        iv_tagIcon
                                .setBackgroundResource(R.drawable.food_type_icon);
                    } else if (bean.data.tag.tag_id == 6) {
                        iv_tagIcon
                                .setBackgroundResource(R.drawable.lures_type_icon);
                    } else if (bean.data.tag.tag_id == 7) {
                        iv_tagIcon
                                .setBackgroundResource(R.drawable.fishing_type_icon);
                    }
                    tv_tagCount.setText(String
                            .valueOf(bean.data.tag.user_likes) + "人气");
                } catch (Exception e) {
                    // TODO: handle exception
                    iv_tagIcon
                            .setBackgroundResource(R.drawable.daily_type_icon);
                    tv_tagCount.setText("0人气");
                }
                if (bean.data.jing == 0) {
                    tv_addJing.setText("加精");
                    iv_jingIcon.setVisibility(View.GONE);
                } else {
                    tv_addJing.setText("已加精");
                    iv_jingIcon.setVisibility(View.VISIBLE);
                    tv_serverTips.setVisibility(View.VISIBLE);
                }

                userHeader = bean.data.head_pic;
                userName = bean.data.user_name;
                if (!TextUtils.isEmpty(bean.data.info)) {
                    fishComment = "\"" + bean.data.info + "\"";
                }
                if (!TextUtils.isEmpty(bean.data.city_name)) {
                    fishArea = bean.data.city_name + "  ";
                }
                if (!TextUtils.isEmpty(bean.data.fishPoint_name)
                        && !bean.data.fishPoint_name.equals("未知")) {
                    fishArea = fishArea + bean.data.fishPoint_name;
                }
                if (!TextUtils.isEmpty(bean.data.fishInfo)) {
                    fishType = bean.data.fishInfo;
                }
                if (!TextUtils.isEmpty(bean.data.tools)) {
                    fishTips = bean.data.tools;
                }
                fishTime = TimeUtil.formatTime(bean.data.time * 1000,
                        "yyyy年MM月dd日");

            }

            @Override
            public void onFail(String msg, Object... params) {
                ToastHelper.showToast(FooterprintDetailActivity.this,
                        msg);
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
        loading = footerView.findViewById(R.id.loading);
        lv_comment.addFooterView(footerView);
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
                    getCommentData();
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    public void getCommentData() {
        UserBusinessController.getInstance().getComments(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 3, page, 20, new com.example.controller.controller.Listener<CommentsBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(CommentsBean bean, Object... params) {

                if (bean.data.list.size() < 20) {
                    ToastHelper.showToast(FooterprintDetailActivity.this,
                            "所有评论已加载完");
                    lv_comment.setOnScrollListener(null);
                    lv_comment.removeFooterView(footerView);
                    loading.setVisibility(View.GONE);
                    footerprintCommentList.addAll(bean.data.list);
                    commnentAdapter.notifyDataSetChanged();
                } else {
                    loading.setVisibility(View.GONE);
                    loadMore.setVisibility(View.VISIBLE);
                    footerprintCommentList.addAll(bean.data.list);
                    commnentAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                ToastHelper.showToast(FooterprintDetailActivity.this,
                        msg);
            }
        });
    }

    private class FooterprintImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return footerprintImageList.size();
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
                        FooterprintDetailActivity.this).inflate(
                        R.layout.item_footerprint_image, null, false);
                viewholder.icv_footerprintImage = (ImageView) convertView
                        .findViewById(R.id.item_user_header);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (footerprintImageList.get(position).smallpic_url
                    .startsWith("http://")) {
                ImageUtils.getInstance().loadImage(
                        FooterprintDetailActivity.this,
                        footerprintImageList.get(position).smallpic_url,
                        R.drawable.loadding_icon,
                        viewholder.icv_footerprintImage);
            } else {
                ImageUtils
                        .getInstance()
                        .loadImage(
                                FooterprintDetailActivity.this,
                                Constant.HOST_URL
                                        + footerprintImageList.get(position).smallpic_url,
                                R.drawable.loadding_icon,
                                viewholder.icv_footerprintImage);
            }
            viewholder.icv_footerprintImage
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            ArrayList<String> imgs = new ArrayList<String>();
                            for (int i = 0; i < footerprintImageList.size(); i++) {
                                imgs.add(footerprintImageList.get(i).pic_url);
                            }
                            ImageBrowseActivity.launch(
                                    FooterprintDetailActivity.this, imgs,
                                    position, userHeader, userName, fishType,
                                    fishTips, fishArea, fishTime, fishComment, true);
                        }
                    });

            return convertView;
        }
    }

    private class UserHeaderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (footerprintLikeList.size() + 2 > 8) {
                return 8;
            } else {
                return footerprintLikeList.size() + 2;
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
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(
                        FooterprintDetailActivity.this).inflate(
                        R.layout.item_fishinginfo_detail_userheader, null,
                        false);
                viewholder.icv_footerprintLikeHeader = (ImageView) convertView
                        .findViewById(R.id.item_user_header);
                viewholder.ll_likeBtn = (LinearLayout) convertView
                        .findViewById(R.id.like_layout);
                viewholder.tv_likeCount = (TextView) convertView
                        .findViewById(R.id.like_count);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                viewholder.tv_likeCount.setVisibility(View.VISIBLE);
                viewholder.icv_footerprintLikeHeader.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.GONE);

                viewholder.tv_likeCount.setText(String.valueOf(likeNumber));
            } else if (position == footerprintLikeList.size() + 1
                    || position == 7) {
                viewholder.tv_likeCount.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.VISIBLE);
                viewholder.icv_footerprintLikeHeader.setVisibility(View.GONE);
            } else {
                viewholder.tv_likeCount.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.GONE);
                viewholder.icv_footerprintLikeHeader
                        .setVisibility(View.VISIBLE);

                if (footerprintLikeList.get(position - 1).head_pic
                        .startsWith("http://")) {
                    ImageUtils.getInstance().loadImage(
                            FooterprintDetailActivity.this,
                            footerprintLikeList.get(position - 1).head_pic,
                            R.drawable.loadding_icon,
                            viewholder.icv_footerprintLikeHeader);
                } else {
                    ImageUtils
                            .getInstance()
                            .loadImage(
                                    FooterprintDetailActivity.this,
                                    Constant.HOST_URL
                                            + footerprintLikeList
                                            .get(position - 1).head_pic,
                                    R.drawable.loadding_icon,
                                    viewholder.icv_footerprintLikeHeader);
                }
            }
            return convertView;
        }

    }

    private class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return footerprintCommentList.size();
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
                        FooterprintDetailActivity.this).inflate(
                        R.layout.item_comment, null, false);
                viewholder.icv_commnetUserHeader = (ImageView) convertView
                        .findViewById(R.id.comment_user_header);
                viewholder.tv_commentUserName = (TextView) convertView
                        .findViewById(R.id.comment_user_name);
                viewholder.tv_commentTime = (TextView) convertView
                        .findViewById(R.id.comment_time);
                viewholder.tv_commentContent = (TextView) convertView
                        .findViewById(R.id.comment_content);
                viewholder.tv_reply = (TextView) convertView
                        .findViewById(R.id.reply);
                // viewholder.iv_jing = (ImageView) convertView
                // .findViewById(R.id.jing_icon);
                viewholder.tv_addJing = (TextView) convertView
                        .findViewById(R.id.add_jing_btn);
                viewholder.tv_commentSeverTips = (TextView) convertView
                        .findViewById(R.id.comment_server_tips);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (footerprintCommentList.get(position).head_pic
                    .startsWith("http://")) {
                ImageUtils.getInstance().loadImage(
                        FooterprintDetailActivity.this,
                        footerprintCommentList.get(position).head_pic,
                        R.drawable.loadding_icon,
                        viewholder.icv_commnetUserHeader);
            } else {
                ImageUtils
                        .getInstance()
                        .loadImage(
                                FooterprintDetailActivity.this,
                                Constant.HOST_URL
                                        + footerprintCommentList.get(position).head_pic,
                                R.drawable.loadding_icon,
                                viewholder.icv_commnetUserHeader);
            }
            viewholder.tv_commentUserName.setText(footerprintCommentList
                    .get(position).user_name);
            viewholder.tv_commentTime
                    .setText(TimeUtil.getTimeString(footerprintCommentList
                            .get(position).time * 1000));
            viewholder.icv_commnetUserHeader
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            if (footerprintCommentList.get(position).user_id
                                    .equals(sharedPreferenceUtil.getUserID())) {
                                MainActivity.launch(
                                        FooterprintDetailActivity.this,
                                        "meFragment");
                            } else {
                                OtherInfoActivity
                                        .launch(FooterprintDetailActivity.this,
                                                footerprintCommentList
                                                        .get(position).user_id);
                            }
                        }
                    });

            viewholder.tv_reply.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    fl_commentCount.setVisibility(View.GONE);
                    iv_sendBtn.setVisibility(View.VISIBLE);
                    tv_commentBtn.setVisibility(View.GONE);
                    tv_emoji.setVisibility(View.VISIBLE);
                    et_comment.setVisibility(View.VISIBLE);
                    isReply = true;
                    et_comment.requestFocus();
                    replyName = footerprintCommentList.get(position).user_name;
                    et_comment.setHint("回复:" + replyName);
                    showSoftInput();
                    reply_id = String.valueOf(footerprintCommentList
                            .get(position).id);
                    reply_user_id = footerprintCommentList.get(position).user_id;
                }
            });
            if (footerprintCommentList.get(position).jing == 0) {
                try {
                    viewholder.tv_commentContent
                            .setText(EmojiUtil.handlerEmojiText(
                                    footerprintCommentList.get(position).comment,
                                    FooterprintDetailActivity.this));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                viewholder.tv_commentSeverTips.setVisibility(View.GONE);
                viewholder.tv_addJing.setText("加精");
            } else {
                String html = "<img src='" + R.drawable.jing + "'/>  ";
                Html.ImageGetter imgGetter = new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        // TODO Auto-generated method stub
                        int id = Integer.parseInt(source);
                        Drawable d = getResources().getDrawable(id);
                        d.setBounds(0, 0, d.getIntrinsicWidth(),
                                d.getIntrinsicHeight());
                        return d;
                    }
                };
                viewholder.tv_commentContent.setText(Html.fromHtml(html,
                        imgGetter, null));
                try {
                    viewholder.tv_commentContent
                            .append(EmojiUtil.handlerEmojiText(
                                    footerprintCommentList.get(position).comment,
                                    FooterprintDetailActivity.this));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                viewholder.tv_commentSeverTips.setVisibility(View.VISIBLE);
                viewholder.tv_addJing.setText("已加精");
            }
            if (sharedPreferenceUtil.getMember() == 9) {
                viewholder.tv_addJing.setVisibility(View.VISIBLE);
            } else {
                viewholder.tv_addJing.setVisibility(View.GONE);
            }
            viewholder.tv_addJing.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (footerprintCommentList.get(position).jing == 0) {
                        currentPosition = position;
                        UserBusinessController.getInstance().addJingJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", footerprintCommentList.get(position).id, 6, 1, new com.example.controller.controller.Listener<BaseBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在加精中,请稍候...");
                            }

                            @Override
                            public void onComplete(BaseBean bean, Object... params) {
                                dismissProgressDialog();

                                footerprintCommentList.get(currentPosition).jing = 1;
                                commnentAdapter.notifyDataSetChanged();
                                ToastHelper.showToast(FooterprintDetailActivity.this, "加精成功");

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FooterprintDetailActivity.this,
                                        msg);
                            }
                        });
                    }
                }
            });
            return convertView;
        }

    }

    public class ViewHolder {

        private ImageView icv_footerprintImage;
        private ImageView icv_footerprintLikeHeader;
        private ImageView icv_commnetUserHeader;
        private TextView tv_commentUserName;
        private TextView tv_commentTime;
        private TextView tv_commentContent;
        private TextView tv_reply;

        private LinearLayout ll_likeBtn;
        private TextView tv_likeCount;
        // private ImageView iv_jing;
        private TextView tv_addJing;
        private TextView tv_commentSeverTips;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.collection_view:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(this, "您还未登录,请先登录");
                    LoginActivity.launch(this, "other");
                } else {
                    if (is_collection) {
                        // 调用取消收藏接口
                        UserBusinessController.getInstance().collection(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 3, 0, new com.example.controller.controller.Listener<BaseBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在取消收藏中,请稍候...");
                            }

                            @Override
                            public void onComplete(BaseBean bean, Object... params) {
                                dismissProgressDialog();

                                if (is_collection) {
                                    // 取消收藏成功
                                    is_collection = false;
                                    if (getIntent().getStringExtra("fromActivity").equals(
                                            "collection")) {
                                        MyFooterprintFragment.is_refresh = true;
                                    }
                                    ToastHelper.showToast(FooterprintDetailActivity.this,
                                            "取消收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.collection_icon);
                                } else {
                                    // 收藏成功
                                    is_collection = true;
                                    ToastHelper.showToast(FooterprintDetailActivity.this,
                                            "收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.had_collection_icon);
                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FooterprintDetailActivity.this,
                                        msg);
                            }
                        });
                    } else {
                        // 调用收藏接口
                        UserBusinessController.getInstance().collection(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 3, 1, new com.example.controller.controller.Listener<BaseBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在收藏中,请稍候...");
                            }

                            @Override
                            public void onComplete(BaseBean bean, Object... params) {
                                dismissProgressDialog();

                                if (is_collection) {
                                    // 取消收藏成功
                                    is_collection = false;
                                    if (getIntent().getStringExtra("fromActivity").equals(
                                            "collection")) {
                                        MyFooterprintFragment.is_refresh = true;
                                    }
                                    ToastHelper.showToast(FooterprintDetailActivity.this,
                                            "取消收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.collection_icon);
                                } else {
                                    // 收藏成功
                                    is_collection = true;
                                    ToastHelper.showToast(FooterprintDetailActivity.this,
                                            "收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.had_collection_icon);
                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FooterprintDetailActivity.this,
                                        msg);
                            }
                        });
                    }
                }
                break;

            case R.id.send_btn:
                // 评论
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(this, "您还未登录,请先登录再评论");
                    return;
                }
                if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
                    ToastHelper.showToast(this, "请输入您的评论");
                    return;
                }
                if (isReply) {
                    isReply = false;
                    comment = "回复:" + replyName + "  " + et_comment.getText().toString().trim();
                } else {
                    comment = et_comment.getText().toString().trim();
                    reply_id = "";
                    reply_user_id = "";
                }
                UserBusinessController.getInstance().comment(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 3, comment, reply_id, reply_user_id, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在提交评论中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();

                        et_comment.setText(null);
                        et_comment.setVisibility(View.GONE);
                        fl_commentCount.setVisibility(View.VISIBLE);
                        iv_sendBtn.setVisibility(View.GONE);
                        fm_emoji.setVisibility(View.GONE);
                        tv_emoji.setVisibility(View.GONE);
                        tv_commentBtn.setVisibility(View.VISIBLE);
                        iv_collection.setVisibility(View.VISIBLE);
                        iv_share.setVisibility(View.VISIBLE);
                        FooterPrintFragment.active = "update";
                        MyFooterprintFragment.active = "update";
                        for (int i = 0; i < FooterPrintFragment.dataList.size(); i++) {
                            if (FooterPrintFragment.dataList.get(i).id == mId) {
                                FooterPrintFragment.dataList.get(i).comment_number = FooterPrintFragment.dataList
                                        .get(i).comment_number + 1;
                            }
                        }
                        for (int j = 0; j < MyFooterprintFragment.dataList.size(); j++) {
                            if (MyFooterprintFragment.dataList.get(j).id == mId) {
                                MyFooterprintFragment.dataList.get(j).comment_number = MyFooterprintFragment.dataList
                                        .get(j).comment_number + 1;
                            }
                        }
                        ToastHelper.showToast(FooterprintDetailActivity.this, "评论成功");
                        footerprintCommentList.clear();
                        commnentAdapter.notifyDataSetChanged();
                        initData();

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(FooterprintDetailActivity.this, msg);
                    }
                });
                break;
            case R.id.comment_view:
                lv_comment.setSelection(1);
                break;

            case R.id.user_header:
                if (user_id.equals(sharedPreferenceUtil.getUserID())) {
                    MainActivity.launch(FooterprintDetailActivity.this,
                            "meFragment");
                } else {
                    OtherInfoActivity.launch(FooterprintDetailActivity.this,
                            user_id);
                }
                break;
            case R.id.left_back_layout:
                if (isPush) {
                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    finish();
                }
                break;
            case R.id.detail_share_view:
                // 弹出分享
                dialog_share = new ShareDialogUtils(this, R.style.dialog,
                        uiHandler, true);
                dialog_share.setCanceledOnTouchOutside(true);
                dialog_share.show();
                break;
            case R.id.fish_point_info:
                try {
                    BasicMapActivity.launch(this, dataBean.data.longitude,
                            dataBean.data.latitude, dataBean.data.info,
                            dataBean.data.pic_urls.get(0).pic_url,
                            dataBean.data.fishPoint_name);
                } catch (Exception e) {
                    // TODO: handle exception
                    ToastHelper.showToast(this, "数据异常，请退出再重新操作");
                }

                break;
            case R.id.fish_type_layout:
                FishAllTypeActivity.launch(this, fishTypeList);
                break;
            case R.id.del_btn:
                UserBusinessController.getInstance().delFishLocation(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在删除足迹中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();
                        MyFooterprintActivity.refresh = true;
                        ToastHelper.showToast(FooterprintDetailActivity.this, "删除动态成功");
                        finish();

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(FooterprintDetailActivity.this, msg);
                    }
                });
                break;
            case R.id.emoji_text:
                // 切换表情view
                isShowEmoji = !isShowEmoji;
                hideSoftInput();
                if (isShowEmoji) {
                    fl_commentCount.setVisibility(View.GONE);
                    iv_sendBtn.setVisibility(View.VISIBLE);
                    fm_emoji.setVisibility(View.VISIBLE);
                    iv_collection.setVisibility(View.GONE);
                    iv_share.setVisibility(View.GONE);
                } else {
                    fm_emoji.setVisibility(View.GONE);
                    iv_collection.setVisibility(View.GONE);
                    iv_share.setVisibility(View.GONE);
                }
                break;
            case R.id.comment_btn:
                isReply = false;
                fl_commentCount.setVisibility(View.GONE);
                iv_sendBtn.setVisibility(View.VISIBLE);
                tv_commentBtn.setVisibility(View.GONE);
                et_comment.setVisibility(View.VISIBLE);
                et_comment.setHint("说点什么");
                tv_emoji.setVisibility(View.VISIBLE);
                showSoftInput();
                et_comment.requestFocus();
                break;
            case R.id.add_jing_btn:
                if (dataBean.data.jing == 0) {
                    UserBusinessController.getInstance().addJingJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 3, 1, new com.example.controller.controller.Listener<BaseBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在加精中,请稍候...");
                        }

                        @Override
                        public void onComplete(BaseBean bean, Object... params) {
                            dismissProgressDialog();

                            dataBean.data.jing = 1;
                            iv_jingIcon.setVisibility(View.VISIBLE);
                            tv_addJing.setText("已加精");
                            ToastHelper.showToast(FooterprintDetailActivity.this, "加精成功");

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(FooterprintDetailActivity.this, msg);
                        }
                    });
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int posotion,
                            long arg3) {
        // TODO Auto-generated method stub
        if (posotion == footerprintLikeList.size() + 1 || posotion == 7) {
            if (!TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())
                    && !isLiking) {
                if (is_like == 1) {
                    ToastHelper.showToast(this, "您已点赞过");
                } else {
                    // 点赞接口
                    isLiking = true;
                    UserBusinessController.getInstance().getLike(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 3, 1, new com.example.controller.controller.Listener<BaseBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在点赞中,请稍候...");
                        }

                        @Override
                        public void onComplete(BaseBean bean, Object... params) {
                            dismissProgressDialog();
                            isLiking = false;
                            FooterPrintFragment.active = "update";
                            MyFooterprintFragment.active = "update";
                            for (int i = 0; i < FooterPrintFragment.dataList.size(); i++) {
                                if (FooterPrintFragment.dataList.get(i).id == mId) {
                                    FooterPrintFragment.dataList.get(i).like_number = FooterPrintFragment.dataList
                                            .get(i).like_number + 1;
                                    try {
                                        FooterPrintFragment.dataList.get(i).tag.user_likes = FooterPrintFragment.dataList
                                                .get(i).tag.user_likes + 1;
                                    } catch (Exception e) {
                                        Tag tag = new Tag();
                                        tag.user_likes = 1;
                                        FooterPrintFragment.dataList.get(i).tag = tag;
                                    }
                                }
                            }
                            for (int j = 0; j < MyFooterprintFragment.dataList.size(); j++) {
                                if (MyFooterprintFragment.dataList.get(j).id == mId) {
                                    MyFooterprintFragment.dataList.get(j).like_number = MyFooterprintFragment.dataList
                                            .get(j).like_number + 1;
                                }
                            }
                            ToastHelper.showToast(FooterprintDetailActivity.this, "点赞成功");
                            initData();
                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(FooterprintDetailActivity.this, msg);
                            isLiking = false;
                        }
                    });
                }

            } else {
                ToastHelper.showToast(this, "您还未登录,请先登录");
                LoginActivity.launch(this, "footerprintdetail");
            }

        } else if (posotion == 0) {
            AllPeopleActivity.launch(FooterprintDetailActivity.this,
                    footerprintLikeList);
        } else {
            if (footerprintLikeList.get(posotion - 1).user_id
                    .equals(sharedPreferenceUtil.getUserID())) {
                MainActivity.launch(FooterprintDetailActivity.this,
                        "meFragment");
            } else {
                OtherInfoActivity.launch(FooterprintDetailActivity.this,
                        footerprintLikeList.get(posotion - 1).user_id);
            }
        }
    }

    public class UIHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            dialog_share.initPlatform();
            String shareUrl = "http://www.nardiaoyu.com/mobile/fishlocation/"
                    + mId;
            switch (msg.what) {
                case 0:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareContent, 0, shareUrl);
                    break;
                case 1:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareContent, 1, shareUrl);
                    break;
                case 2:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareContent, 2, shareUrl);
                    break;
                case 3:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareContent, 3, shareUrl);
                    break;
                case 4:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareContent, 4, shareUrl);
                    break;
            }

        }
    }

    /**
     * 分享平台的选择
     *
     * @param position
     */
    public void selectSharePaltform(String content, int position,
                                    String shareUrl) {
        dialog_share.startShare(mShareTitle, content, null, position, null,
                shareUrl);
    }

    @Override
    public void onEmojiDelete() {
        // TODO Auto-generated method stub

        String text = et_comment.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                et_comment.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                return;
            }
            et_comment.getText().delete(index, text.length());
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        et_comment.onKeyDown(KeyEvent.KEYCODE_DEL, event);

    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            try {
                et_comment.append(EmojiUtil.handlerEmojiText(
                        emoji.getContent(), this));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this **/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
