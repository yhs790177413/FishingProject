package com.goby.fishing.module.fishing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.emojilib.emoji.Emoji;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.emojilib.emoji.FaceFragment;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.CommentsBean;
import com.example.controller.bean.FishingDetialBean;
import com.example.controller.bean.CommentsBean.Data.Comment;
import com.example.controller.bean.FishingDetialBean.Data.FishTyes;
import com.example.controller.bean.FishingDetialBean.Data.Like;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.SlideShowView;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.footerprint.AllPeopleActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity.ViewHolder;
import com.goby.fishing.module.login.LoginActivity;
import com.goby.fishing.module.me.MyFishingFragment;
import com.goby.fishing.module.other.OtherInfoActivity;
import com.umeng.socialize.UMShareAPI;


public class FishingDetailActivity extends AbsBaseActivity implements
        OnClickListener, FaceFragment.OnEmojiClickListener {

    private int mId;

    private SlideShowView slideShowView;

    private TextView tv_fishingName;

    private TextView tv_fishingInfo;

    private TextView tv_fishingAddress;

    private TextView tv_fishingDistance;

    private TextView tv_noCommentTips;

    private ListView lv_fishingComment;

    private ImageView iv_collection;

    private ImageView iv_back;

    private RelativeLayout rl_address;

    private LinearLayout ll_dynamicLayout;

    private FishingDetialBean fishingDetailBean;

    private LinearLayout ll_addFooterprint;
    private LinearLayout ll_allType;
    private LinearLayout ll_allFooterprint;
    private LinearLayout ll_allLog;

    private View headerView;

    private ImageView iv_comment;

    private EditText et_comment;

    private LinearLayout rl_bottomLayout;

    private CommentAdapter adapter;

    private String mContent;

    private ArrayList<Comment> dataList = new ArrayList<Comment>();

    private boolean is_collection;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private double longitude;

    private double latitude;

    private String mInfo;

    private String mPic_url;

    private String mFishingName;

    private boolean isFirst = true;

    private int mCurrentPosition;

    private ShareDialogUtils dialog_share;

    private ImageView iv_share;

    private UIHandler uiHandler;

    private LinearLayout ll_leftBack;

    private TextView tv_commentCount;

    private TextView tv_addFishDynamic;

    private boolean commentRefresh = false;

    private ArrayList<String> imageData;

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    private int page = 1;

    private String mShareTitle = "";

    private String mShareContent = "去哪钓鱼精彩推荐";

    private TextView tv_weather;
    private TextView tv_temp;
    private ImageView iv_temp;
    private TextView tv_pressure;
    private TextView tv_commentTips;
    private ImageView iv_pressure;
    private RelativeLayout rl_weather;
    private GridView gv_dynamicImage;
    private DynamicImageAdapter imageAdapter;
    private GoThereAdapter goThereAdapter;
    private ImageView iv_dynamicUserHeader;
    private TextView tv_dynamicUserName;
    private TextView tv_dynamicTime;
    private ImageView iv_dynamicTag;
    private TextView tv_dynamicTagCount;
    private TextView tv_dynamicPraise;
    private TextView tv_dynamicComment;
    private TextView tv_dynamicContent;
    private TextView tv_dynamicInfo;
    private TextView tv_dynamicImageCount;
    private LinearLayout ll_fishDynamicLayout;
    private GridView gv_gothere;

    private String reply_id = null; // 回复帖子id

    private String reply_user_id = null; // 回复人id

    private boolean isReply = false; // 是否回复

    private String replyName = ""; // 被回复人的名字

    private String comment = "";

    private boolean isShowEmoji = false;

    private TextView tv_emoji;
    private FrameLayout fm_emoji;

    private TextView tv_commentBtn;

    private ImageView iv_sendBtn;

    private FrameLayout fl_commentCount;

    private boolean isPush = false;

    private ArrayList<Like> fishingLikeList = new ArrayList<Like>();

    private int is_like;

    public static void launch(Context activity, int id, int currentPosition,
                              String fromActivity, String pic_url) {

        Intent intent = new Intent(activity, FishingDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("fromActivity", fromActivity);
        intent.putExtra("pic_url", pic_url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_detail);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        uiHandler = new UIHandler();
        mId = getIntent().getIntExtra("id", -1);
        if (mId < 0) {
            isPush = true;
            sharedPreferenceUtil.setRedPointIsVisible(false);
            Bundle data = getIntent().getExtras();
            mId = Integer.parseInt(data.getString("id"));
        }
        mCurrentPosition = getIntent().getIntExtra("currentPosition", -1);
        mPic_url = getIntent().getStringExtra("pic_url");
        initView();
        initData();
        FaceFragment faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.Container, faceFragment).commit();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (slideShowView != null) {
            slideShowView.destoryBitmaps();
        }
    }

    public void initView() {

        tv_addFishDynamic = (TextView) findViewById(R.id.add_footerprint);
        headerView = LayoutInflater.from(this).inflate(
                R.layout.fishing_detial_head, null);
        headerView.setVisibility(View.GONE);
        rl_weather = (RelativeLayout) headerView
                .findViewById(R.id.weather_layout);
        tv_weather = (TextView) headerView.findViewById(R.id.weather_text);
        tv_temp = (TextView) headerView.findViewById(R.id.temp_text);
        iv_temp = (ImageView) headerView.findViewById(R.id.temp_image);
        tv_pressure = (TextView) headerView.findViewById(R.id.pressure_text);
        iv_pressure = (ImageView) headerView.findViewById(R.id.pressure_image);
        iv_collection = (ImageView) findViewById(R.id.collection_view);
        rl_address = (RelativeLayout) headerView
                .findViewById(R.id.address_layout);
        tv_fishingName = (TextView) headerView.findViewById(R.id.fishing_name);
        tv_fishingInfo = (TextView) headerView.findViewById(R.id.fishing_info);
        tv_fishingAddress = (TextView) headerView
                .findViewById(R.id.fishing_address);
        tv_fishingDistance = (TextView) headerView.findViewById(R.id.distance);
        tv_commentTips = (TextView) headerView.findViewById(R.id.comment_tips);
        tv_commentCount = (TextView) findViewById(R.id.comment_count);
        tv_noCommentTips = (TextView) headerView
                .findViewById(R.id.no_comment_tips);
        ll_addFooterprint = (LinearLayout) headerView
                .findViewById(R.id.add_footerprint_layout);
        ll_allType = (LinearLayout) headerView
                .findViewById(R.id.all_type_layout);
        ll_dynamicLayout = (LinearLayout) headerView
                .findViewById(R.id.dynamic_layout);
        ll_allFooterprint = (LinearLayout) headerView
                .findViewById(R.id.all_footerprint_layout);
        ll_allLog = (LinearLayout) headerView.findViewById(R.id.all_log_layout);
        gv_dynamicImage = (GridView) headerView
                .findViewById(R.id.dynamic_image_grid);
        gv_dynamicImage.setClickable(false);
        gv_dynamicImage.setPressed(false);
        gv_dynamicImage.setEnabled(false);
        gv_dynamicImage.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gv_gothere = (GridView) headerView.findViewById(R.id.gothere_grid);
        gv_gothere.setSelector(new ColorDrawable(Color.TRANSPARENT));

        ll_fishDynamicLayout = (LinearLayout) headerView
                .findViewById(R.id.fish_dynamic_layout);
        lv_fishingComment = (ListView) findViewById(R.id.fishing_comment_list);
        iv_dynamicUserHeader = (ImageView) headerView
                .findViewById(R.id.dynamic_user_header);
        tv_dynamicUserName = (TextView) headerView
                .findViewById(R.id.dynamic_user_name);
        tv_dynamicTime = (TextView) headerView.findViewById(R.id.dynamic_time);
        iv_dynamicTag = (ImageView) headerView
                .findViewById(R.id.dynamic_tag_icon);
        tv_dynamicTagCount = (TextView) headerView
                .findViewById(R.id.dynamic_tag_count);
        tv_dynamicPraise = (TextView) headerView
                .findViewById(R.id.dynamic_praise_count);
        tv_dynamicComment = (TextView) headerView
                .findViewById(R.id.dynamic_comment_count);
        tv_dynamicContent = (TextView) headerView
                .findViewById(R.id.dynamic_content);
        tv_dynamicImageCount = (TextView) headerView
                .findViewById(R.id.dynamic_image_count);
        tv_dynamicInfo = (TextView) headerView.findViewById(R.id.dynamic_info);
        lv_fishingComment.addHeaderView(headerView);
        adapter = new CommentAdapter();
        lv_fishingComment.setAdapter(adapter);

        iv_comment = (ImageView) findViewById(R.id.comment_view);
        et_comment = (EditText) findViewById(R.id.edit_comment);
        iv_back = (ImageView) findViewById(R.id.left_back);

        rl_bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        iv_share = (ImageView) findViewById(R.id.detail_share_view);
        tv_emoji = (TextView) findViewById(R.id.emoji_text);
        fm_emoji = (FrameLayout) findViewById(R.id.Container);

        tv_commentBtn = (TextView) findViewById(R.id.comment_btn);
        iv_sendBtn = (ImageView) findViewById(R.id.send_btn);
        fl_commentCount = (FrameLayout) findViewById(R.id.fragment_send);

        // ll_likeBtn = (LinearLayout) findViewById(R.id.like_layout);
        // tv_likeCount = (TextView) findViewById(R.id.like_count);
        // ll_likeBtn.setOnClickListener(this);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
        iv_comment.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
        tv_fishingInfo.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        ll_addFooterprint.setOnClickListener(this);
        ll_allType.setOnClickListener(this);
        ll_allFooterprint.setOnClickListener(this);
        ll_allLog.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rl_weather.setOnClickListener(this);
        tv_commentTips.setOnClickListener(this);
        ll_fishDynamicLayout.setOnClickListener(this);
        tv_addFishDynamic.setOnClickListener(this);
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
        lv_fishingComment.setOnItemClickListener(new OnItemClickListener() {

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
        gv_gothere.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int posotion, long arg3) {
                // TODO Auto-generated method stub
                if (posotion == fishingLikeList.size() + 1 || posotion == 7) {
                    if (!TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                        if (is_like == 1) {
                            ToastHelper.showToast(FishingDetailActivity.this,
                                    "您已点过");
                        } else {
                            // 点赞接口
                            UserBusinessController.getInstance().getLike(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 2, 1, new com.example.controller.controller.Listener<BaseBean>() {
                                @Override
                                public void onStart(Object... params) {
                                    showProgressDialog("正在提交中,请稍候...");
                                }

                                @Override
                                public void onComplete(BaseBean bean, Object... params) {
                                    dismissProgressDialog();

                                    ToastHelper.showToast(FishingDetailActivity.this, "提交成功");
                                    commentRefresh = true;
                                    initData();

                                }

                                @Override
                                public void onFail(String msg, Object... params) {
                                    dismissProgressDialog();
                                    ToastHelper.showToast(FishingDetailActivity.this, msg);
                                }
                            });
                        }
                    } else {
                        ToastHelper.showToast(FishingDetailActivity.this,
                                "您还未登录,请先登录");
                        LoginActivity.launch(FishingDetailActivity.this,
                                "footerprintdetail");
                    }

                } else if (posotion == 0) {
                    if (fishingLikeList != null && fishingLikeList.size() > 0) {
                        AllPeopleGoThereActivity.launch(
                                FishingDetailActivity.this, fishingLikeList);
                    }
                } else {
                    if (fishingLikeList.get(posotion - 1).user_id
                            .equals(sharedPreferenceUtil.getUserID())) {
                        MainActivity.launch(FishingDetailActivity.this,
                                "meFragment");
                    } else {
                        OtherInfoActivity.launch(FishingDetailActivity.this,
                                fishingLikeList.get(posotion - 1).user_id);
                    }
                }
            }

        });
    }

    public void initData() {
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
        }
        UserBusinessController.getInstance().fishingDetailJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, latitude, longitude, new com.example.controller.controller.Listener<FishingDetialBean>() {
            @Override
            public void onStart(Object... params) {
                if (isFirst) {
                    showProgressDialog("正在获取数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(FishingDetialBean result, Object... params) {
                dismissProgressDialog();

                fishingDetailBean = result;
                isFirst = false;
                rl_bottomLayout.setVisibility(View.VISIBLE);
                headerView.setVisibility(View.VISIBLE);
                if (fishingDetailBean.data.is_favorite == 0) {
                    is_collection = false;
                    iv_collection
                            .setBackgroundResource(R.drawable.collection_icon);
                } else {
                    is_collection = true;
                    iv_collection
                            .setBackgroundResource(R.drawable.had_collection_icon);
                }
                if (!commentRefresh) {
                    String[] dataImage = new String[fishingDetailBean.data.pic_urls
                            .size()];
                    for (int i = 0; i < fishingDetailBean.data.pic_urls.size(); i++) {
                        dataImage[i] = fishingDetailBean.data.pic_urls.get(i).smallpic_url;
                    }
                    slideShowView = (SlideShowView) headerView
                            .findViewById(R.id.slideshowView);
                    slideShowView.destoryBitmaps();
                    slideShowView.initData(dataImage, null);
                }
                is_like = fishingDetailBean.data.is_like;
                mShareTitle = fishingDetailBean.data.name;
                mShareContent = fishingDetailBean.data.info;
                mFishingName = fishingDetailBean.data.name;
                tv_fishingName.setText(fishingDetailBean.data.name);
                mContent = fishingDetailBean.data.info;
                tv_fishingInfo.setText(fishingDetailBean.data.info);
                tv_fishingAddress.setText(fishingDetailBean.data.address);
                tv_fishingDistance.setText(fishingDetailBean.data.distance);
                if (fishingDetailBean.data.comments != null
                        && fishingDetailBean.data.comments.size() > 0) {
                    tv_commentCount.setVisibility(View.VISIBLE);
                    tv_commentCount.setText(String
                            .valueOf(fishingDetailBean.data.comment_number));
                    tv_commentTips.setVisibility(View.VISIBLE);
                    tv_noCommentTips.setVisibility(View.GONE);
                    dataList.addAll(fishingDetailBean.data.comments);
                    adapter.notifyDataSetChanged();
                    if (fishingDetailBean.data.comments.size() < 20) {
                        lv_fishingComment.setOnScrollListener(null);
                    } else {
                        initFooter();
                        lv_fishingComment
                                .setOnScrollListener(new UpdateListener());
                    }
                } else {
                    tv_commentCount.setVisibility(View.GONE);
                    tv_noCommentTips.setVisibility(View.VISIBLE);
                }
                longitude = fishingDetailBean.data.longitude;
                latitude = fishingDetailBean.data.latitude;
                mInfo = fishingDetailBean.data.info;
                try {
                    tv_weather.setText(fishingDetailBean.data.weather.cond);
                    tv_temp.setText(fishingDetailBean.data.weather.temp + "°C");
                    if (fishingDetailBean.data.weather.temp_rise == 0) {
                        iv_temp.setVisibility(View.GONE);
                    } else if (fishingDetailBean.data.weather.temp_rise == 1) {
                        iv_temp.setVisibility(View.VISIBLE);
                        iv_temp.setBackgroundResource(R.drawable.fishpoint_temp_up);
                    } else if (fishingDetailBean.data.weather.temp_rise == 2) {
                        iv_temp.setVisibility(View.VISIBLE);
                        iv_temp.setBackgroundResource(R.drawable.fishpoint_temp_down);
                    }
                    tv_pressure.setText(fishingDetailBean.data.weather.pressure
                            + "Ph");
                    if (fishingDetailBean.data.weather.pressure_rise == 0) {
                        iv_pressure.setVisibility(View.GONE);
                    } else if (fishingDetailBean.data.weather.pressure_rise == 1) {
                        iv_pressure.setVisibility(View.VISIBLE);
                        iv_pressure
                                .setBackgroundResource(R.drawable.fishpoint_temp_up);
                    } else if (fishingDetailBean.data.weather.pressure_rise == 2) {
                        iv_pressure.setVisibility(View.VISIBLE);
                        iv_pressure
                                .setBackgroundResource(R.drawable.fishpoint_temp_down);
                    }
                } catch (Exception e) {
                    rl_weather.setVisibility(View.GONE);
                }
                try {
                    if (result.data.fish_feed != null) {
                        ll_dynamicLayout.setVisibility(View.VISIBLE);
                        ImageLoaderWrapper.getDefault().displayImage(
                                result.data.fish_feed.head_pic,
                                iv_dynamicUserHeader);
                        tv_dynamicUserName
                                .setText(result.data.fish_feed.user_name);
                        tv_dynamicTime
                                .setText(TimeUtil
                                        .getTimeString(result.data.fish_feed.time * 1000));
                        try {
                            if (result.data.fish_feed.tag.tag_id == 1) {
                                iv_dynamicTag
                                        .setBackgroundResource(R.drawable.daily_type_icon);
                            } else if (result.data.fish_feed.tag.tag_id == 2) {
                                iv_dynamicTag
                                        .setBackgroundResource(R.drawable.wild_fish_type_icon);
                            } else if (result.data.fish_feed.tag.tag_id == 3) {
                                iv_dynamicTag
                                        .setBackgroundResource(R.drawable.black_pit_type_icon);
                            } else if (result.data.fish_feed.tag.tag_id == 4) {
                                iv_dynamicTag
                                        .setBackgroundResource(R.drawable.equipment_type_icon);
                            } else if (result.data.fish_feed.tag.tag_id == 5) {
                                iv_dynamicTag
                                        .setBackgroundResource(R.drawable.food_type_icon);
                            } else if (result.data.fish_feed.tag.tag_id == 6) {
                                iv_dynamicTag
                                        .setBackgroundResource(R.drawable.lures_type_icon);
                            } else if (result.data.fish_feed.tag.tag_id == 7) {
                                iv_dynamicTag
                                        .setBackgroundResource(R.drawable.fishing_type_icon);
                            }
                            tv_dynamicTagCount
                                    .setText(String
                                            .valueOf(result.data.fish_feed.tag.user_likes)
                                            + "人气");
                        } catch (Exception e) {
                            // TODO: handle exception
                            iv_dynamicTag
                                    .setBackgroundResource(R.drawable.daily_type_icon);
                            tv_dynamicTagCount.setText("0人气");
                        }
                        tv_dynamicPraise
                                .setText(result.data.fish_feed.like_number + " 赞");
                        tv_dynamicComment
                                .setText(result.data.fish_feed.comment_number
                                        + " 评论");
                        if (TextUtils.isEmpty(result.data.fish_feed.info)) {
                            tv_dynamicContent.setVisibility(View.GONE);
                        } else {
                            tv_dynamicContent.setVisibility(View.VISIBLE);
                            tv_dynamicContent.setText(result.data.fish_feed.info);
                        }

                        if (TextUtils.isEmpty(result.data.fish_feed.address_info)) {
                            tv_dynamicInfo.setVisibility(View.GONE);
                        } else {
                            tv_dynamicInfo.setVisibility(View.VISIBLE);
                            tv_dynamicInfo
                                    .setText(result.data.fish_feed.address_info);
                        }
                        tv_dynamicInfo
                                .setOnClickListener(FishingDetailActivity.this);
                        if (result.data.fish_feed.picture_number > 3) {
                            tv_dynamicImageCount.setVisibility(View.VISIBLE);
                            tv_dynamicImageCount
                                    .setText(result.data.fish_feed.picture_number
                                            + "图");
                        } else {
                            tv_dynamicImageCount.setVisibility(View.GONE);
                        }
                        imageData = new ArrayList<String>();
                        imageData.addAll(result.data.fish_feed.pic_urls);
                        imageAdapter = new DynamicImageAdapter(imageData);
                        gv_dynamicImage.setAdapter(imageAdapter);
                    }
                } catch (Exception e) {
                    ll_dynamicLayout.setVisibility(View.GONE);
                }
                if (result.data.likes != null && result.data.likes.size() > 0) {
                    fishingLikeList.clear();
                    fishingLikeList.addAll(result.data.likes);
                }
                goThereAdapter = new GoThereAdapter();
                gv_gothere.setAdapter(goThereAdapter);

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(FishingDetailActivity.this, msg);
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
        lv_fishingComment.addFooterView(footerView);
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
        UserBusinessController.getInstance().getComments(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 2, page, 20, new com.example.controller.controller.Listener<CommentsBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(CommentsBean bean, Object... params) {
                dismissProgressDialog();

                if (bean.data.list.size() < 20) {
                    ToastHelper.showToast(FishingDetailActivity.this,
                            "所有评论已加载完");
                    lv_fishingComment.setOnScrollListener(null);
                    lv_fishingComment.removeFooterView(footerView);
                    loading.setVisibility(View.GONE);
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(FishingDetailActivity.this, msg);
            }
        });
    }

    private class CommentAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(FishingDetailActivity.this)
                        .inflate(R.layout.item_fishing_detial_comment, null,
                                false);
                viewholder.icv_fishingImage = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_commentTime = (TextView) convertView
                        .findViewById(R.id.time);
                viewholder.tv_commentContent = (TextView) convertView
                        .findViewById(R.id.content);
                viewholder.tv_reply = (TextView) convertView
                        .findViewById(R.id.reply);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }

            if (dataList.get(position).head_pic.startsWith("http://")) {
                ImageLoaderWrapper.getDefault().displayImage(
                        dataList.get(position).head_pic,
                        viewholder.icv_fishingImage);
            } else {
                ImageLoaderWrapper.getDefault().displayImage(
                        Constant.HOST_URL + dataList.get(position).head_pic,
                        viewholder.icv_fishingImage);
            }
            viewholder.tv_userName.setText(dataList.get(position).user_name);
            viewholder.tv_commentTime.setText(TimeUtil.getTimeString(dataList
                    .get(position).time * 1000));
            try {
                viewholder.tv_commentContent.setText(EmojiUtil
                        .handlerEmojiText(dataList.get(position).comment,
                                FishingDetailActivity.this));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            viewholder.icv_fishingImage
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            if (dataList.get(position).user_id
                                    .equals(sharedPreferenceUtil.getUserID())) {
                                MainActivity.launch(FishingDetailActivity.this,
                                        "meFragment");
                            } else {
                                OtherInfoActivity.launch(
                                        FishingDetailActivity.this,
                                        dataList.get(position).user_id);
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
                    replyName = dataList.get(position).user_name;
                    et_comment.setHint("回复:" + replyName);
                    showSoftInput();
                    reply_id = String.valueOf(dataList.get(position).id);
                    reply_user_id = dataList.get(position).user_id;
                }
            });
            return convertView;
        }
    }

    private class DynamicImageAdapter extends BaseAdapter {

        private ArrayList<String> mImageList;

        public DynamicImageAdapter(ArrayList<String> imageList) {
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
                convertView = LayoutInflater.from(FishingDetailActivity.this)
                        .inflate(R.layout.item_footer_image, null, false);
                viewholder.iv_dynamicImage = (ImageView) convertView
                        .findViewById(R.id.footer_image);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderWrapper.getDefault().displayImage(
                    mImageList.get(position), viewholder.iv_dynamicImage);
            return convertView;
        }
    }

    private class GoThereAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (fishingLikeList.size() + 2 > 8) {
                return 8;
            } else {
                return fishingLikeList.size() + 2;
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
                convertView = LayoutInflater.from(FishingDetailActivity.this)
                        .inflate(R.layout.item_fishing_like, null, false);
                viewholder.icv_footerprintLikeHeader = (ImageView) convertView
                        .findViewById(R.id.item_user_header);
                viewholder.ll_likeBtn = (LinearLayout) convertView
                        .findViewById(R.id.like_layout);
                viewholder.tv_likeCount = (TextView) convertView
                        .findViewById(R.id.like_count);
                viewholder.iv_isGoImage = (ImageView) convertView
                        .findViewById(R.id.is_go_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (is_like == 0) {
                viewholder.iv_isGoImage
                        .setBackgroundResource(R.drawable.gothere_unclick);
            } else {
                viewholder.iv_isGoImage
                        .setBackgroundResource(R.drawable.gothere_click);
            }
            if (position == 0) {
                viewholder.tv_likeCount.setVisibility(View.VISIBLE);
                viewholder.icv_footerprintLikeHeader.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.GONE);
                viewholder.tv_likeCount.setText(String.valueOf(fishingLikeList
                        .size()));
            } else if (position == fishingLikeList.size() + 1 || position == 7) {
                viewholder.tv_likeCount.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.VISIBLE);
                viewholder.icv_footerprintLikeHeader.setVisibility(View.GONE);
            } else {
                viewholder.tv_likeCount.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.GONE);
                viewholder.icv_footerprintLikeHeader
                        .setVisibility(View.VISIBLE);

                if (fishingLikeList.get(position - 1).head_pic
                        .startsWith("http://")) {
                    ImageLoaderWrapper.getDefault().displayImage(
                            fishingLikeList.get(position - 1).head_pic,
                            viewholder.icv_footerprintLikeHeader);
                } else {
                    ImageLoaderWrapper
                            .getDefault()
                            .displayImage(
                                    Constant.HOST_URL
                                            + fishingLikeList.get(position - 1).head_pic,
                                    viewholder.icv_footerprintLikeHeader);
                }
            }

            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView icv_fishingImage;
        private ImageView iv_dynamicImage;
        private TextView tv_userName;
        private TextView tv_commentTime;
        private TextView tv_commentContent;
        private TextView tv_reply;

        private ImageView icv_footerprintLikeHeader;
        private LinearLayout ll_likeBtn;
        private TextView tv_likeCount;
        private ImageView iv_isGoImage;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.fishing_info:
                SummaryDetailActivity.launch(this, mContent);
                break;
            case R.id.collection_view:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(this, "您还未登录,请先登录");
                    LoginActivity.launch(this, "other");
                } else {
                    if (is_collection) {
                        // 调用取消收藏接口
                        UserBusinessController.getInstance().collection(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 2, 0, new com.example.controller.controller.Listener<BaseBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在取消收藏中,请稍候...");
                            }

                            @Override
                            public void onComplete(BaseBean bean, Object... params) {
                                dismissProgressDialog();

                                if (is_collection) {
                                    // 取消收藏成功
                                    if (!TextUtils.isEmpty(getIntent().getStringExtra(
                                            "fromActivity"))) {
                                        MyFishingFragment.is_refresh = true;
                                    }
                                    is_collection = false;
                                    ToastHelper.showToast(FishingDetailActivity.this, "取消收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.collection_icon);
                                } else {
                                    // 收藏成功
                                    is_collection = true;
                                    ToastHelper.showToast(FishingDetailActivity.this, "收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.had_collection_icon);
                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FishingDetailActivity.this, msg);
                            }
                        });
                    } else {
                        // 调用收藏接口
                        UserBusinessController.getInstance().collection(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 2, 1, new com.example.controller.controller.Listener<BaseBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在收藏中,请稍候...");
                            }

                            @Override
                            public void onComplete(BaseBean bean, Object... params) {
                                dismissProgressDialog();

                                if (is_collection) {
                                    // 取消收藏成功
                                    if (!TextUtils.isEmpty(getIntent().getStringExtra(
                                            "fromActivity"))) {
                                        MyFishingFragment.is_refresh = true;
                                    }
                                    is_collection = false;
                                    ToastHelper.showToast(FishingDetailActivity.this, "取消收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.collection_icon);
                                } else {
                                    // 收藏成功
                                    is_collection = true;
                                    ToastHelper.showToast(FishingDetailActivity.this, "收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.had_collection_icon);
                                }
                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FishingDetailActivity.this, msg);
                            }
                        });
                    }
                }

                break;
            case R.id.address_layout:
                BasicMapActivity.launch(this, longitude, latitude, mInfo, mPic_url,
                        mFishingName);
                break;
            case R.id.add_footerprint:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    LoginActivity.launch(this, "meFragment", 0);
                } else {
                    AddFooterprintActivity.launch(this, mFishingName, longitude,
                            latitude, mId);
                }
                break;

            case R.id.all_footerprint_layout:
                FishPointDynamicListAcitivty.launch(this, mId, mFishingName,
                        longitude, latitude);
                break;
            case R.id.like_layout:
                // 点赞接口
                UserBusinessController.getInstance().getLike(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, 1, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在提交中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean result, Object... params) {
                        dismissProgressDialog();

                        ToastHelper.showToast(FishingDetailActivity.this, "提交成功");
                        commentRefresh = true;
                        initData();

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(FishingDetailActivity.this, msg);
                    }
                });
                break;
            case R.id.send_btn:

                // 评论
                if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
                    ToastHelper.showToast(this, "请输入您的评论");
                    return;
                }
                if (isReply) {
                    isReply = false;
                    comment = "回复:" + replyName + "  " + et_comment.getText().toString().trim();
                } else {
                    comment = et_comment.getText().toString().trim();
                }
                UserBusinessController.getInstance().comment(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 2, comment, reply_id, reply_user_id, new com.example.controller.controller.Listener<BaseBean>() {
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
                        FishingFragment.active = "update";
                        MyFishingFragment.active = "update";
                        for (int i = 0; i < FishingFragment.dataList.size(); i++) {
                            if (FishingFragment.dataList.get(i).id == mId) {
                                FishingFragment.dataList.get(i).comment_number = FishingFragment.dataList
                                        .get(i).comment_number + 1;
                            }
                        }
                        for (int j = 0; j < MyFishingFragment.dataList.size(); j++) {
                            if (MyFishingFragment.dataList.get(j).id == mId) {
                                MyFishingFragment.dataList.get(j).comment_number = MyFishingFragment.dataList
                                        .get(j).comment_number + 1;
                            }
                        }
                        ToastHelper.showToast(FishingDetailActivity.this, "评论成功");
                        commentRefresh = true;
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                        initData();

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(FishingDetailActivity.this, msg);
                    }
                });
                break;
            case R.id.comment_view:
                lv_fishingComment.setSelection(1);
                break;
            case R.id.left_back_layout:
                if (isShowEmoji) {
                    isShowEmoji = false;
                    fm_emoji.setVisibility(View.GONE);
                    iv_collection.setVisibility(View.GONE);
                    iv_share.setVisibility(View.GONE);
                } else {
                    hideSoftInput();
                    if (isPush) {
                        finish();
                        Intent i = new Intent(this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        finish();
                    }
                }
                break;
            case R.id.detail_share_view:
                // 弹出分享
                dialog_share = new ShareDialogUtils(this, R.style.dialog,
                        uiHandler, true);
                dialog_share.setCanceledOnTouchOutside(true);
                dialog_share.show();
                break;
            case R.id.weather_layout:
                WeatherDetailActivity.launch(this, mPic_url, mId, mFishingName);
                break;
            case R.id.comment_tips:
                FishPointDynamicListAcitivty.launch(this, mId, mFishingName,
                        longitude, latitude);
                break;
            case R.id.fish_dynamic_layout:
                FooterprintDetailActivity.launch(this,
                        fishingDetailBean.data.fish_feed.id, mPic_url, -1,
                        "fishDetail");
                break;
            case R.id.dynamic_info:

                BasicMapActivity.launch(this,
                        fishingDetailBean.data.fish_feed.longitude,
                        fishingDetailBean.data.fish_feed.latitude,
                        fishingDetailBean.data.fish_feed.info,
                        fishingDetailBean.data.fish_feed.pic_urls.get(0), "");

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
            default:
                break;
        }
    }


    public class UIHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            dialog_share.initPlatform();
            String shareUrl = "http://www.nardiaoyu.com/mobile/fishpoint/"
                    + mId;
            switch (msg.what) {
                case 0:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 0, shareUrl);
                    break;
                case 1:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 1, shareUrl);
                    break;
                case 2:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 2, shareUrl);
                    break;
                case 3:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 3, shareUrl);
                    break;
                case 4:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 4, shareUrl);
                    break;
            }

        }
    }

    /**
     * 分享平台的选择
     *
     * @param position
     */
    public void selectSharePaltform(String title, String content, int position,
                                    String shareUrl) {
        dialog_share.startShare(title, content, null, position, null, shareUrl);
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
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            if (isShowEmoji) {
                isShowEmoji = false;
                fm_emoji.setVisibility(View.GONE);
                iv_collection.setVisibility(View.GONE);
                iv_share.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
