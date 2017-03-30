package com.goby.fishing.module.information;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.emojilib.emoji.Emoji;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.emojilib.emoji.FaceFragment;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.CommentsBean;
import com.example.controller.bean.FishingInfoDetailBean;
import com.example.controller.bean.CommentsBean.Data.Comment;
import com.example.controller.bean.FishingInfoDetailBean.Data.Externalcomments;
import com.example.controller.bean.FishingInfoDetailBean.Data.Like;
import com.example.controller.bean.TagsListBean.Data.TagBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.BorderTextView;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TgDateTimePicker;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.widget.imageview.ImageCircleView;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.index.AllTagInfosActivity;
import com.goby.fishing.module.index.TagInfosActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.goby.fishing.module.me.MyFishingInfoFragment;
import com.goby.fishing.module.other.OtherInfoActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;


public class FishingInfoDetailActivity extends AbsBaseActivity implements
        OnClickListener, OnItemClickListener, FaceFragment.OnEmojiClickListener {

    private TextView tv_title;

    private WebView tv_content;

    private GridView gv_userHeader;

    private UserHeaderAdapter adapter;

    private CommentAdapter commnentAdapter;

    private TagsAdapter mTagsAdapter;

    private RelativeLayout rl_parent_layout;

    private LinearLayout ll_parent;

    private ImageView iv_collection;

    private ImageView iv_back;

    private TextView tv_commentTips;

    private EditText et_comment;

    private ImageView iv_comment;

    private ListView lv_comment;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private boolean is_collection;

    private int mId, currentPosition;

    private boolean isFirst = true;

    private int is_like;

    private TextView tv_time;

    private TextView tv_seeComment;

    private ArrayList<Like> fishingInfoLikeList = new ArrayList<Like>();

    private ArrayList<Comment> fishingInfoCommentList = new ArrayList<Comment>();

    private ArrayList<TagBean> tagsDataList = new ArrayList<TagBean>();

    private int mCurrentPosition;

    private ShareDialogUtils dialog_share;

    private ImageView iv_share;

    private UIHandler uiHandler;

    private LinearLayout ll_leftBack;

    private TextView tv_commentCount;

    private View headerView;
    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条
    private LinearLayout ll_searchComment;

    private String active = "init", newContent;

    private int page = 1;

    private String mShareTitle = "";

    private String mShareContent = "去哪钓鱼精彩推荐";

    private String mShareImageUrl = "";

    private int mRequestCode = 0;

    private int likeNumber = 0;

    private String reply_id; // 回复帖子id

    private String reply_user_id; // 回复人id

    private boolean isReply = false; // 是否回复

    private String replyName = ""; // 被回复人的名字

    private boolean isShowEmoji = false;

    private TextView tv_emoji;
    private FrameLayout fm_emoji;

    private TextView tv_commentBtn;

    private ImageView iv_sendBtn;

    private FrameLayout fl_commentCount;

    private boolean isPush = false;

    public static boolean isRefresh = false;

    private String fromActivity = "main";

    private GridView gv_tagsList;

    private String comment;

    private String ids = "";

    private ImageView iv_searchComment;

    private int externalCommentNumber = 0;

    private ArrayList<Externalcomments> externalCommentList = new ArrayList<Externalcomments>();

    public static void launch(Context mContxt, int id, int currentPosition,
                              String fromActivity, String imageUrl, int requestCode) {

        Intent intent = new Intent(mContxt, FishingInfoDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("fromActivity", fromActivity);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("requestCode", requestCode);
        mContxt.startActivity(intent);
    }

    public static void launch(Context mContxt, int id, int currentPosition,
                              String fromActivity, String imageUrl) {

        Intent intent = new Intent(mContxt, FishingInfoDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("fromActivity", fromActivity);
        intent.putExtra("imageUrl", imageUrl);
        mContxt.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_info_detail);

        try {
            fromActivity = getIntent().getStringExtra("fromActivity");
        } catch (Exception e) {
            // TODO: handle exception
        }
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        uiHandler = new UIHandler();
        mId = getIntent().getIntExtra("id", -1);
        if (mId < 0) {
            isPush = true;
            sharedPreferenceUtil.setRedPointIsVisible(false);
            Bundle data = getIntent().getExtras();
            mId = Integer.parseInt(data.getString("id"));
        }
        mRequestCode = getIntent().getIntExtra("requestCode", 0);
        mShareImageUrl = getIntent().getStringExtra("imageUrl");
        mCurrentPosition = getIntent().getIntExtra("currentPosition", -1);
        initView();
        initData();
        FaceFragment faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.Container, faceFragment).commit();
    }

    public void initView() {

        rl_parent_layout = (RelativeLayout) findViewById(R.id.activity_parent_layout);
        headerView = LayoutInflater.from(this).inflate(
                R.layout.fish_info_detail_header, null);
        ll_parent = (LinearLayout) headerView.findViewById(R.id.parent_layout);
        iv_collection = (ImageView) findViewById(R.id.collection_view);
        iv_back = (ImageView) findViewById(R.id.left_back);
        gv_tagsList = (GridView) headerView.findViewById(R.id.tags_grid);
        mTagsAdapter = new TagsAdapter();
        gv_tagsList.setAdapter(mTagsAdapter);

        gv_userHeader = (GridView) headerView
                .findViewById(R.id.user_header_grid);
        adapter = new UserHeaderAdapter();
        gv_userHeader.setAdapter(adapter);

        tv_title = (TextView) headerView.findViewById(R.id.title);
        tv_content = (WebView) headerView.findViewById(R.id.content);

        tv_commentTips = (TextView) headerView.findViewById(R.id.comment_tips);
        tv_seeComment = (TextView) findViewById(R.id.right_title);
        tv_commentCount = (TextView) findViewById(R.id.comment_count);
        et_comment = (EditText) findViewById(R.id.edit_comment);
        iv_comment = (ImageView) findViewById(R.id.comment_view);

        iv_share = (ImageView) findViewById(R.id.detail_share_view);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        tv_emoji = (TextView) findViewById(R.id.emoji_text);
        fm_emoji = (FrameLayout) findViewById(R.id.Container);
        tv_commentBtn = (TextView) findViewById(R.id.comment_btn);
        iv_sendBtn = (ImageView) findViewById(R.id.send_btn);
        fl_commentCount = (FrameLayout) findViewById(R.id.fragment_send);

        ll_leftBack.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
        gv_userHeader.setOnItemClickListener(this);
        iv_comment.setOnClickListener(this);
        iv_back.setOnClickListener(this);
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
                    // hideSoftInput();
                }
                fm_emoji.setVisibility(View.GONE);
                isShowEmoji = false;
            }
        });
        tv_seeComment.setOnClickListener(this);
        lv_comment = (ListView) findViewById(R.id.comment_list);
        commnentAdapter = new CommentAdapter();
        lv_comment.addHeaderView(headerView);
        lv_comment.setAdapter(commnentAdapter);

        tv_time = (TextView) headerView.findViewById(R.id.time);

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
        gv_tagsList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position == tagsDataList.size() - 1) {
                    AllTagInfosActivity.launch(FishingInfoDetailActivity.this);
                } else if (position == 0) {
                    ids = "";
                    for (int i = 1; i < tagsDataList.size() - 1; i++) {
                        ids = ids + tagsDataList.get(i).tag_id + ",";
                    }
                    TagInfosActivity.launch(FishingInfoDetailActivity.this,
                            null, tagsDataList.get(position).tag_name,
                            ids.substring(0, ids.length() - 1));
                } else {
                    TagInfosActivity.launch(FishingInfoDetailActivity.this,
                            String.valueOf(tagsDataList.get(position).tag_id),
                            tagsDataList.get(position).tag_name, null);
                }
            }
        });
    }

    public void initData() {
        UserBusinessController.getInstance().fishingInfoDetailJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, new com.example.controller.controller.Listener<FishingInfoDetailBean>() {
            @Override
            public void onStart(Object... params) {
                if (isFirst) {
                    showProgressDialog("正在加载数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(FishingInfoDetailBean bean, Object... params) {
                if (isFirst) {
                    dismissProgressDialog();
                }

                externalCommentNumber = bean.data.external_comment_number;
                if (bean.data.external_comments != null
                        && bean.data.external_comments.size() > 0) {
                    externalCommentList.addAll(bean.data.external_comments);
                }
                isFirst = false;
                likeNumber = bean.data.like_number;
                ll_parent.setVisibility(View.VISIBLE);
                if (bean.data.is_favorite == 0) {
                    is_collection = false;
                    iv_collection
                            .setBackgroundResource(R.drawable.collection_icon);
                } else {
                    is_collection = true;
                    iv_collection
                            .setBackgroundResource(R.drawable.had_collection_icon);
                }
                mShareTitle = "资讯分享";
                mShareContent = bean.data.title;
                tv_title.setText(bean.data.title);
                tv_time.setText(TimeUtil.getTimeString(bean.data.time * 1000));
                // 得到WebView组件
                // 能够的调用JavaScript代码
                tv_content.getSettings().setJavaScriptEnabled(true);
                tv_content.getSettings().setLayoutAlgorithm(
                        LayoutAlgorithm.SINGLE_COLUMN);
                tv_content.setWebChromeClient(m_chromeClient);
                newContent = "<html><head><style>img{max-width:320px !important;}</style></head><body>"
                        + bean.data.content + "</body><ml>";
                tv_content.loadDataWithBaseURL(null, newContent, "text/html",
                        "UTF-8", null);
                is_like = bean.data.is_like;
                if (bean.data.likes != null && bean.data.likes.size() > 0) {
                    fishingInfoLikeList.clear();
                    fishingInfoLikeList.addAll(bean.data.likes);
                    adapter.notifyDataSetChanged();
                }
                if (bean.data.comment_number > 0) {
                    tv_commentCount.setVisibility(View.VISIBLE);
                    tv_commentCount.setText(String
                            .valueOf(bean.data.comment_number));
                    tv_commentTips.setVisibility(View.VISIBLE);
                    UserBusinessController.getInstance().getComments(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, page, 20, new com.example.controller.controller.Listener<CommentsBean>() {
                        @Override
                        public void onStart(Object... params) {

                        }

                        @Override
                        public void onComplete(CommentsBean bean, Object... params) {

                            if (active.equals("init")) {
                                fishingInfoCommentList.addAll(bean.data.list);
                                commnentAdapter.notifyDataSetChanged();
                                if (bean.data.list.size() < 20) {
                                    lv_comment.setOnScrollListener(null);
                                    if (externalCommentNumber > 0) {
                                        if (lv_comment.getFooterViewsCount() < 1) {
                                            initFooter();
                                            loading.setVisibility(View.GONE);
                                            loadMore.setVisibility(View.GONE);
                                            ll_searchComment.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    initFooter();
                                    if (externalCommentNumber > 0) {
                                        ll_searchComment.setVisibility(View.VISIBLE);
                                    }
                                    lv_comment.setOnScrollListener(new UpdateListener());
                                }
                            } else if (active.equals("update")) {
                                fishingInfoCommentList.addAll(bean.data.list);
                                commnentAdapter.notifyDataSetChanged();
                                if (bean.data.list.size() < 20) {
                                    lv_comment.setOnScrollListener(null);
                                    if (externalCommentNumber > 0) {
                                        ll_searchComment.setVisibility(View.VISIBLE);
                                        loading.setVisibility(View.GONE);
                                        loadMore.setVisibility(View.GONE);
                                    } else {
                                        lv_comment.removeFooterView(footerView);
                                    }
                                    ToastHelper.showToast(FishingInfoDetailActivity.this,
                                            "所有评论已加载完");
                                }
                            }

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
                        }
                    });
                } else {
                    tv_commentCount.setVisibility(View.GONE);
                    tv_commentTips.setVisibility(View.GONE);
                    if (externalCommentNumber > 0) {
                        initFooter();
                        ll_searchComment.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                    }
                }
                if (bean.data.tags != null && bean.data.tags.size() > 0) {
                    gv_tagsList.setVisibility(View.VISIBLE);
                    tagsDataList.clear();
                    tagsDataList.addAll(bean.data.tags);
                    TagBean mTagBean = new TagBean();
                    mTagBean.tag_name = "全部";
                    mTagBean.tag_id = 0;
                    tagsDataList.add(mTagBean);
                    TagBean sameTagBean = new TagBean();
                    sameTagBean.tag_name = "找相似";
                    sameTagBean.tag_id = 0;
                    tagsDataList.add(0, sameTagBean);
                    mTagsAdapter.notifyDataSetChanged();
                    setGridViewHeightBasedOnChildren(gv_tagsList, 5);
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
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
        ll_searchComment = (LinearLayout) footerView
                .findViewById(R.id.search_comment_layout);
        iv_searchComment = (ImageView) footerView
                .findViewById(R.id.search_comment);
        iv_searchComment.setOnClickListener(this);
        lv_comment.addFooterView(footerView);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // tv_content.onPause();
    }

    private WebChromeClient m_chromeClient = new WebChromeClient() {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // TODO Auto-generated method stub
        }
    };

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
                        UserBusinessController.getInstance().collection(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, 0, new com.example.controller.controller.Listener<BaseBean>() {
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
                                    if (fromActivity.equals("collection")) {
                                        MyFishingInfoFragment.is_refresh = true;
                                    }
                                    ToastHelper.showToast(FishingInfoDetailActivity.this,
                                            "取消收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.collection_icon);
                                } else {
                                    // 收藏成功
                                    is_collection = true;
                                    ToastHelper.showToast(FishingInfoDetailActivity.this,
                                            "收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.had_collection_icon);
                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
                            }
                        });
                    } else {
                        // 调用收藏接口
                        UserBusinessController.getInstance().collection(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, 1, new com.example.controller.controller.Listener<BaseBean>() {
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
                                    if (fromActivity.equals("collection")) {
                                        MyFishingInfoFragment.is_refresh = true;
                                    }
                                    ToastHelper.showToast(FishingInfoDetailActivity.this,
                                            "取消收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.collection_icon);
                                } else {
                                    // 收藏成功
                                    is_collection = true;
                                    ToastHelper.showToast(FishingInfoDetailActivity.this,
                                            "收藏成功");
                                    iv_collection
                                            .setBackgroundResource(R.drawable.had_collection_icon);
                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
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
                }
                UserBusinessController.getInstance().comment(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, comment, reply_id, reply_user_id, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在提交评论中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();
                        fishingInfoCommentList.clear();
                        commnentAdapter.notifyDataSetChanged();
                        et_comment.setText(null);
                        et_comment.setVisibility(View.GONE);
                        fl_commentCount.setVisibility(View.VISIBLE);
                        iv_sendBtn.setVisibility(View.GONE);
                        fm_emoji.setVisibility(View.GONE);
                        tv_emoji.setVisibility(View.GONE);
                        tv_commentBtn.setVisibility(View.VISIBLE);
                        iv_collection.setVisibility(View.VISIBLE);
                        iv_share.setVisibility(View.VISIBLE);
                        UserBusinessController.getInstance().getComments(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, 1, 20, new com.example.controller.controller.Listener<CommentsBean>() {
                            @Override
                            public void onStart(Object... params) {

                            }

                            @Override
                            public void onComplete(CommentsBean bean, Object... params) {

                                if (active.equals("init")) {
                                    fishingInfoCommentList.addAll(bean.data.list);
                                    commnentAdapter.notifyDataSetChanged();
                                    if (bean.data.list.size() < 20) {
                                        lv_comment.setOnScrollListener(null);
                                        if (externalCommentNumber > 0) {
                                            if (lv_comment.getFooterViewsCount() < 1) {
                                                initFooter();
                                                loading.setVisibility(View.GONE);
                                                loadMore.setVisibility(View.GONE);
                                                ll_searchComment.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } else {
                                        initFooter();
                                        if (externalCommentNumber > 0) {
                                            ll_searchComment.setVisibility(View.VISIBLE);
                                        }
                                        lv_comment.setOnScrollListener(new UpdateListener());
                                    }
                                } else if (active.equals("update")) {
                                    fishingInfoCommentList.addAll(bean.data.list);
                                    commnentAdapter.notifyDataSetChanged();
                                    if (bean.data.list.size() < 20) {
                                        lv_comment.setOnScrollListener(null);
                                        if (externalCommentNumber > 0) {
                                            ll_searchComment.setVisibility(View.VISIBLE);
                                            loading.setVisibility(View.GONE);
                                            loadMore.setVisibility(View.GONE);
                                        } else {
                                            lv_comment.removeFooterView(footerView);
                                        }
                                        ToastHelper.showToast(FishingInfoDetailActivity.this,
                                                "所有评论已加载完");
                                    }
                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
                            }
                        });
                        ToastHelper.showToast(FishingInfoDetailActivity.this, "评论成功");
                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
                    }
                });
                break;
            case R.id.comment_view:
                lv_comment.setSelection(1);
                break;
            case R.id.left_back_layout:
                if (isShowEmoji) {
                    isShowEmoji = false;
                    fm_emoji.setVisibility(View.GONE);
                    iv_collection.setVisibility(View.GONE);
                    iv_share.setVisibility(View.GONE);
                } else {
                    hideSoftInput();
                    tv_content.clearCache(true);
                    tv_content.clearHistory();
                    if (mRequestCode > 0) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                    }
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
            case R.id.left_back:
                if (isShowEmoji) {
                    isShowEmoji = false;
                    fm_emoji.setVisibility(View.GONE);
                    iv_collection.setVisibility(View.GONE);
                    iv_share.setVisibility(View.GONE);
                } else {
                    hideSoftInput();
                    tv_content.clearCache(true);
                    tv_content.clearHistory();
                    if (mRequestCode > 0) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                    }
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
            case R.id.right_title:
                lv_comment.setSelection(1);
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
            case R.id.search_comment:
                ExternalCommentActivity.launch(this, externalCommentList, mId);
                break;
            default:
                break;
        }
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

    private class UserHeaderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fishingInfoLikeList.size() + 2;
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
                        FishingInfoDetailActivity.this).inflate(
                        R.layout.item_fishinginfo_detail_userheader, null,
                        false);
                viewholder.icv_fishingInfoLikeHeader = (ImageView) convertView
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
                viewholder.ll_likeBtn.setVisibility(View.GONE);
                viewholder.icv_fishingInfoLikeHeader.setVisibility(View.GONE);
                viewholder.tv_likeCount.setText(String.valueOf(likeNumber));
            } else if (position == fishingInfoLikeList.size() + 1
                    || position == 7) {
                viewholder.tv_likeCount.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.VISIBLE);
                viewholder.icv_fishingInfoLikeHeader.setVisibility(View.GONE);
            } else {
                viewholder.tv_likeCount.setVisibility(View.GONE);
                viewholder.ll_likeBtn.setVisibility(View.GONE);
                viewholder.icv_fishingInfoLikeHeader
                        .setVisibility(View.VISIBLE);
                if (fishingInfoLikeList.get(position - 1).head_pic
                        .startsWith("http://")) {
                    ImageLoaderWrapper.getDefault().displayImage(
                            fishingInfoLikeList.get(position - 1).head_pic,
                            viewholder.icv_fishingInfoLikeHeader);
                } else {
                    ImageLoaderWrapper
                            .getDefault()
                            .displayImage(
                                    Constant.HOST_URL
                                            + fishingInfoLikeList
                                            .get(position - 1).head_pic,
                                    viewholder.icv_fishingInfoLikeHeader);
                }
            }
            return convertView;
        }

    }

    private class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fishingInfoCommentList.size();
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
                        FishingInfoDetailActivity.this).inflate(
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

            if (fishingInfoCommentList.get(position).head_pic
                    .startsWith("http://")) {
                ImageLoaderWrapper.getDefault().displayImage(
                        fishingInfoCommentList.get(position).head_pic,
                        viewholder.icv_commnetUserHeader);
            } else {
                ImageLoaderWrapper
                        .getDefault()
                        .displayImage(
                                Constant.HOST_URL
                                        + fishingInfoCommentList.get(position).head_pic,
                                viewholder.icv_commnetUserHeader);
            }

            viewholder.tv_commentUserName.setText(fishingInfoCommentList
                    .get(position).user_name);
            viewholder.tv_commentTime
                    .setText(TimeUtil.getTimeString(fishingInfoCommentList
                            .get(position).time * 1000));
            viewholder.icv_commnetUserHeader
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            if (fishingInfoCommentList.get(position).user_id
                                    .equals(sharedPreferenceUtil.getUserID())) {
                                MainActivity.launch(
                                        FishingInfoDetailActivity.this,
                                        "meFragment");
                            } else {
                                OtherInfoActivity
                                        .launch(FishingInfoDetailActivity.this,
                                                fishingInfoCommentList
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
                    replyName = fishingInfoCommentList.get(position).user_name;
                    et_comment.setHint("回复:" + replyName);
                    showSoftInput();
                    reply_id = String.valueOf(fishingInfoCommentList
                            .get(position).id);
                    reply_user_id = fishingInfoCommentList.get(position).user_id;
                }
            });
            if (fishingInfoCommentList.get(position).jing == 0) {
                try {
                    viewholder.tv_commentContent
                            .setText(EmojiUtil.handlerEmojiText(
                                    fishingInfoCommentList.get(position).comment,
                                    FishingInfoDetailActivity.this));
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
                                    fishingInfoCommentList.get(position).comment,
                                    FishingInfoDetailActivity.this));
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
                    if (fishingInfoCommentList.get(position).jing == 0) {
                        currentPosition = position;
                        UserBusinessController.getInstance().addJingJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", fishingInfoCommentList.get(position).id, 6, 1, new com.example.controller.controller.Listener<BaseBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在加精中,请稍候...");
                            }

                            @Override
                            public void onComplete(BaseBean bean, Object... params) {
                                dismissProgressDialog();

                                fishingInfoCommentList.get(currentPosition).jing = 1;
                                adapter.notifyDataSetChanged();
                                ToastHelper.showToast(FishingInfoDetailActivity.this, "加精成功");

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
                            }
                        });
                    }
                }
            });
            return convertView;
        }

    }

    private class TagsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tagsDataList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(
                        FishingInfoDetailActivity.this).inflate(
                        R.layout.item_tag_view, null, false);
                viewholder.btv_tagImage = (BorderTextView) convertView
                        .findViewById(R.id.tag_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.btv_tagImage.setVisibility(View.VISIBLE);
            viewholder.btv_tagImage
                    .setText(tagsDataList.get(position).tag_name);
            int color_tag = tagsDataList.get(position).tag_id;
            viewholder.btv_tagImage.setColor(initColor().get(color_tag % 7));
            viewholder.btv_tagImage
                    .setTextColor(initColor().get(color_tag % 7));
            return convertView;
        }

    }

    public class ViewHolder {

        private ImageView icv_commnetUserHeader;
        private TextView tv_commentUserName;
        private TextView tv_commentTime;
        private TextView tv_commentContent;

        private ImageView icv_fishingInfoLikeHeader;
        private LinearLayout ll_likeBtn;
        private TextView tv_likeCount;
        private TextView tv_reply;
        // private ImageView iv_jing;
        private TextView tv_addJing;
        private TextView tv_commentSeverTips;
        private BorderTextView btv_tagImage;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // TODO Auto-generated method stub
        if (position == fishingInfoLikeList.size() + 1 || position == 7) {
            if (!TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                if (is_like == 1) {
                    ToastHelper.showToast(this, "您已点赞过");
                } else {
                    // 点赞接口
                    UserBusinessController.getInstance().getLike(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, 1, new com.example.controller.controller.Listener<BaseBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在点赞中,请稍候...");
                        }

                        @Override
                        public void onComplete(BaseBean bean, Object... params) {
                            dismissProgressDialog();

                            NewsFragment.fish_refresh = true;
                            ToastHelper.showToast(FishingInfoDetailActivity.this, "点赞成功");
                            fishingInfoCommentList.clear();
                            commnentAdapter.notifyDataSetChanged();
                            initData();

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
                        }
                    });
                }

            } else {
                ToastHelper.showToast(this, "您还未登录,请先登录");
                LoginActivity.launch(this, "footerprintdetail");
            }

        } else if (position == 0) {
            AllPrisntPeopleActivity.launch(this, fishingInfoLikeList);
        } else {
            if (fishingInfoLikeList.get(position - 1).user_id
                    .equals(sharedPreferenceUtil.getUserID())) {
                MainActivity.launch(FishingInfoDetailActivity.this,
                        "meFragment");
            } else {
                OtherInfoActivity.launch(FishingInfoDetailActivity.this,
                        fishingInfoLikeList.get(position - 1).user_id);
            }
        }
    }

    public class UIHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            MobclickAgent.onEvent(FishingInfoDetailActivity.this,
                    "fish_info_share");
            dialog_share.initPlatform();
            String shareUrl = "http://www.nardiaoyu.com/mobile/info/" + mId;
            switch (msg.what) {
                case 0:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 0,
                            mShareImageUrl, shareUrl);
                    break;
                case 1:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 1,
                            mShareImageUrl, shareUrl);
                    break;
                case 2:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 2,
                            mShareImageUrl, shareUrl);
                    break;
                case 3:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 3,
                            mShareImageUrl, shareUrl);
                    break;
                case 4:
                    dialog_share.dismiss();
                    selectSharePaltform(mShareTitle, mShareContent, 4,
                            mShareImageUrl, shareUrl);
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
                                    String shareUrl, String url) {
        dialog_share.startShare(title, content, null, position, shareUrl, url);
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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        tv_content.clearCache(true);
        tv_content.clearHistory();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        tv_content.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this **/
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

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        tv_content.loadDataWithBaseURL(null, newContent, "text/html", "UTF-8",
                null);
        if (isRefresh) {
            isRefresh = false;
            fishingInfoCommentList.clear();
            UserBusinessController.getInstance().getComments(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, 1, 20, new com.example.controller.controller.Listener<CommentsBean>() {
                @Override
                public void onStart(Object... params) {

                }

                @Override
                public void onComplete(CommentsBean bean, Object... params) {

                    if (active.equals("init")) {
                        fishingInfoCommentList.addAll(bean.data.list);
                        commnentAdapter.notifyDataSetChanged();
                        if (bean.data.list.size() < 20) {
                            lv_comment.setOnScrollListener(null);
                            if (externalCommentNumber > 0) {
                                if (lv_comment.getFooterViewsCount() < 1) {
                                    initFooter();
                                    loading.setVisibility(View.GONE);
                                    loadMore.setVisibility(View.GONE);
                                    ll_searchComment.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            initFooter();
                            if (externalCommentNumber > 0) {
                                ll_searchComment.setVisibility(View.VISIBLE);
                            }
                            lv_comment.setOnScrollListener(new UpdateListener());
                        }
                    } else if (active.equals("update")) {
                        fishingInfoCommentList.addAll(bean.data.list);
                        commnentAdapter.notifyDataSetChanged();
                        if (bean.data.list.size() < 20) {
                            lv_comment.setOnScrollListener(null);
                            if (externalCommentNumber > 0) {
                                ll_searchComment.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                                loadMore.setVisibility(View.GONE);
                            } else {
                                lv_comment.removeFooterView(footerView);
                            }
                            ToastHelper.showToast(FishingInfoDetailActivity.this,
                                    "所有评论已加载完");
                        }
                    }

                }

                @Override
                public void onFail(String msg, Object... params) {
                    ToastHelper.showToast(FishingInfoDetailActivity.this, msg);
                }
            });
        }
    }
}
