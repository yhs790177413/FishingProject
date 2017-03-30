package com.goby.fishing.module.information;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.CommentsBean;
import com.example.controller.bean.CommentsBean.Data.Comment;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.other.OtherInfoActivity;

public class FishInformationCommentListActivity extends AbsBaseActivity
        implements OnClickListener {

    private int page = 1;

    private int mId;

    private int mCommentCount;

    private String reply_id = null;

    private String reply_user_id = null;

    private String comment;

    private String active = "init";

    private boolean isReply;

    private String replyName;

    private ListView lv_comment;

    private TextView tv_commentCount;

    private ImageView iv_sendComment;

    private EditText et_comment;

    private LinearLayout ll_leftBack;

    private CommentAdapter commnentAdapter;

    private ArrayList<Comment> fishingInfoCommentList = new ArrayList<Comment>();

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static void launch(Activity activity, int fishInfoId,
                              int commentCount, int requestCode) {
        Intent intent = new Intent(activity,
                FishInformationCommentListActivity.class);
        intent.putExtra("fishInfoId", fishInfoId);
        intent.putExtra("commentCount", commentCount);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_commentlist);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        mId = getIntent().getIntExtra("fishInfoId", -1);
        mCommentCount = getIntent().getIntExtra("commentCount", 0);
        initFooter();
        initView();
        if (mCommentCount > 0) {
            initData();
        } else {
            lv_comment.removeFooterView(footerView);
            ToastHelper.showToast(this, "赞无任何评论,请添加评论");
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
        footerView.setVisibility(View.GONE);
    }

    public void initView() {

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        et_comment = (EditText) findViewById(R.id.edit_comment);
        lv_comment = (ListView) findViewById(R.id.comment_list);
        iv_sendComment = (ImageView) findViewById(R.id.comment_view);
        tv_commentCount = (TextView) findViewById(R.id.comment_count);
        if (mCommentCount > 0) {
            tv_commentCount.setText(mCommentCount + "");
        } else {
            tv_commentCount.setVisibility(View.GONE);
        }
        commnentAdapter = new CommentAdapter();
        lv_comment.addFooterView(footerView);
        lv_comment.setAdapter(commnentAdapter);

        iv_sendComment.setOnClickListener(this);
        ll_leftBack.setOnClickListener(this);
    }

    public void initData() {
        UserBusinessController.getInstance().getComments(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, page, 20, new com.example.controller.controller.Listener<CommentsBean>() {
            @Override
            public void onStart(Object... params) {
                if (active.equals("init")) {
                    showProgressDialog("正在加载数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(CommentsBean bean, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                }

                footerView.setVisibility(View.VISIBLE);
                fishingInfoCommentList.addAll(bean.data.list);
                commnentAdapter.notifyDataSetChanged();
                if (active.equals("init")) {
                    if (bean.data.list.size() < 20) {
                        loadMore.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        lv_comment.setOnScrollListener(null);
                        lv_comment.removeFooterView(footerView);
                        ToastHelper.showToast(
                                FishInformationCommentListActivity.this,
                                "所有评论已加载完");
                    } else {
                        lv_comment.setOnScrollListener(new UpdateListener());
                    }
                } else if (active.equals("update")) {
                    if (bean.data.list.size() < 20) {
                        loadMore.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        lv_comment.setOnScrollListener(null);
                        lv_comment.removeFooterView(footerView);
                        ToastHelper.showToast(
                                FishInformationCommentListActivity.this,
                                "所有评论已加载完");
                    } else {
                        loadMore.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                }
                ToastHelper.showToast(FishInformationCommentListActivity.this, msg);
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
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                Intent intent = new Intent();
                intent.putExtra("commentCount", mCommentCount);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.comment_view:
                // 评论
                if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
                    ToastHelper.showToast(this, "请输入您的评论");
                    return;
                }
                showProgressDialog("正在提交评论中,请稍候...");
                Map<String, String> postCommentParams;
                if (isReply) {
                    isReply = false;
                    comment = "回复:" + replyName + "  " + et_comment.getText().toString().trim();
                } else {
                    comment = et_comment.getText().toString().trim();
                    reply_id = "";
                    reply_user_id = "";
                }
                UserBusinessController.getInstance().comment(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, comment, reply_id, reply_user_id, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在提交评论中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();

                        mCommentCount++;
                        tv_commentCount.setText(mCommentCount + "");
                        fishingInfoCommentList.clear();
                        commnentAdapter.notifyDataSetChanged();
                        et_comment.setText(null);
                        UserBusinessController.getInstance().getComments(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, 1, 20, new com.example.controller.controller.Listener<CommentsBean>() {
                            @Override
                            public void onStart(Object... params) {
                            }

                            @Override
                            public void onComplete(CommentsBean bean, Object... params) {

                                footerView.setVisibility(View.VISIBLE);
                                fishingInfoCommentList.addAll(bean.data.list);
                                commnentAdapter.notifyDataSetChanged();
                                if (active.equals("init")) {
                                    if (bean.data.list.size() < 20) {
                                        loadMore.setVisibility(View.GONE);
                                        loading.setVisibility(View.GONE);
                                        lv_comment.setOnScrollListener(null);
                                        lv_comment.removeFooterView(footerView);
                                        ToastHelper.showToast(
                                                FishInformationCommentListActivity.this,
                                                "所有评论已加载完");
                                    } else {
                                        lv_comment.setOnScrollListener(new UpdateListener());
                                    }
                                } else if (active.equals("update")) {
                                    if (bean.data.list.size() < 20) {
                                        loadMore.setVisibility(View.GONE);
                                        loading.setVisibility(View.GONE);
                                        lv_comment.setOnScrollListener(null);
                                        lv_comment.removeFooterView(footerView);
                                        ToastHelper.showToast(
                                                FishInformationCommentListActivity.this,
                                                "所有评论已加载完");
                                    } else {
                                        loadMore.setVisibility(View.VISIBLE);
                                        loading.setVisibility(View.GONE);
                                    }
                                }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                ToastHelper.showToast(FishInformationCommentListActivity.this, msg);
                            }
                        });
                        ToastHelper.showToast(FishInformationCommentListActivity.this,
                                "评论成功");

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        ToastHelper.showToast(FishInformationCommentListActivity.this, msg);
                    }
                });
                break;
            default:
                break;
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
                        FishInformationCommentListActivity.this).inflate(
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
            try {
                viewholder.tv_commentContent.setText(EmojiUtil
                        .handlerEmojiText(
                                fishingInfoCommentList.get(position).comment,
                                FishInformationCommentListActivity.this));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            viewholder.icv_commnetUserHeader
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            if (fishingInfoCommentList.get(position).user_id
                                    .equals(sharedPreferenceUtil.getUserID())) {
                                MainActivity
                                        .launch(FishInformationCommentListActivity.this,
                                                "meFragment");
                            } else {
                                OtherInfoActivity
                                        .launch(FishInformationCommentListActivity.this,
                                                fishingInfoCommentList
                                                        .get(position).user_id);
                            }
                        }
                    });
            viewholder.tv_reply.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
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
            return convertView;
        }

    }

    public class ViewHolder {

        private ImageView icv_commnetUserHeader;
        private TextView tv_commentUserName;
        private TextView tv_commentTime;
        private TextView tv_commentContent;
        private TextView tv_reply;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtra("commentCount", mCommentCount);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

}
