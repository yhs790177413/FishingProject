package com.goby.fishing.module.information;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.FishingInfoDetailBean.Data.Externalcomments;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class ExternalCommentActivity extends AbsBaseActivity implements
        OnClickListener {

    private ImageView iv_back;

    private LinearLayout ll_leftBack;

    private ListView lv_externalcomments;

    private ExternalcommentsAdapter adapter;

    private ArrayList<Externalcomments> dataList = new ArrayList<Externalcomments>();

    private SharedPreferenceUtil sharedPreferenceUtil;

    private int mId;

    public static void launch(Activity activity,
                              ArrayList<Externalcomments> externalCommentList, int m_id) {

        Intent intent = new Intent(activity, ExternalCommentActivity.class);
        intent.putExtra("external_comment", externalCommentList);
        intent.putExtra("mId", m_id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_comment);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initData();
        initView();
    }

    public void initData() {

        dataList.addAll((Collection<? extends Externalcomments>) getIntent().getSerializableExtra("external_comment"));
        mId = getIntent().getIntExtra("mId", 0);
    }

    public void initView() {

        iv_back = (ImageView) findViewById(R.id.left_back);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        lv_externalcomments = (ListView) findViewById(R.id.external_comment_list);
        adapter = new ExternalcommentsAdapter();
        lv_externalcomments.setAdapter(adapter);

        iv_back.setOnClickListener(this);
        ll_leftBack.setOnClickListener(this);
    }

    private class ExternalcommentsAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(ExternalCommentActivity.this)
                        .inflate(R.layout.item_external_comments, null, false);
                viewholder.tv_comment = (TextView) convertView
                        .findViewById(R.id.comment_text);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.username_text);
                viewholder.tv_commentBtn = (TextView) convertView
                        .findViewById(R.id.comment_btn);
                viewholder.tv_copyBtn = (TextView) convertView
                        .findViewById(R.id.copy_btn);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }

            viewholder.tv_comment.setText(dataList.get(position).comment);
            viewholder.tv_userName.setText("來源  "
                    + dataList.get(position).user_name);
            viewholder.tv_commentBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                        ToastHelper.showToast(ExternalCommentActivity.this,
                                "您还未登录,请先登录再评论");
                        return;
                    }
                    UserBusinessController.getInstance().comment(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mId, 1, dataList.get(position).comment, "", "", new com.example.controller.controller.Listener<BaseBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在提交评论中,请稍候...");
                        }

                        @Override
                        public void onComplete(BaseBean bean, Object... params) {
                            dismissProgressDialog();

                                ToastHelper.showToast(ExternalCommentActivity.this, "评论成功");
                                FishingInfoDetailActivity.isRefresh = true;
                                finish();

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(ExternalCommentActivity.this,msg);
                        }
                    });
                }
            });
            viewholder.tv_copyBtn.setOnClickListener(new OnClickListener() {

                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ClipboardManager cm = (ClipboardManager) ExternalCommentActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本数据复制到剪贴板
                    cm.setText(dataList.get(position).comment);
                    ToastHelper.showToast(ExternalCommentActivity.this, "已复制");
                }
            });
            return convertView;
        }
    }

    public class ViewHolder {

        private TextView tv_comment;
        private TextView tv_userName;
        private TextView tv_commentBtn, tv_copyBtn;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.left_back:
                finish();
                break;
            default:
                break;
        }
    }

}
