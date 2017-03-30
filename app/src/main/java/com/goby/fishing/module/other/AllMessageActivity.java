package com.goby.fishing.module.other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.emojilib.emoji.Emoji;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.emojilib.emoji.FaceFragment;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.MyMessageBean;
import com.example.controller.bean.MyMessageBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;


public class AllMessageActivity extends AbsBaseActivity implements
        OnClickListener, FaceFragment.OnEmojiClickListener {

    private ListView lv_allMessage;

    private MessageAdapter adapter;

    private ImageView iv_sendMessage;

    private ImageView iv_back;

    private LinearLayout ll_leftBack;

    private EditText et_message;

    private TextView tv_commentBtn;

    private TextView tv_emoji;

    private int page = 1;

    private int number = 20;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private ArrayList<List> dataList = new ArrayList<List>();

    private String active = "init";

    private String userID;

    private FrameLayout fm_emoji;

    private boolean isShowEmoji = false;

    private boolean isPush = false;

    public static void launch(Activity activity, String user_id) {

        Intent intent = new Intent(activity, AllMessageActivity.class);
        intent.putExtra("id", user_id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_message);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        userID = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(userID)) {
            isPush = true;
            Bundle data = getIntent().getExtras();
            userID = data.getString("id");
        }
        initView();
        initData();
        FaceFragment faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.Container, faceFragment).commit();
    }

    public void initView() {

        et_message = (EditText) findViewById(R.id.edit_comment);

        iv_sendMessage = (ImageView) findViewById(R.id.send_btn);
        iv_sendMessage.setOnClickListener(this);

        iv_back = (ImageView) findViewById(R.id.left_back);
        iv_back.setOnClickListener(this);

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);

        lv_allMessage = (ListView) findViewById(R.id.all_message);
        adapter = new MessageAdapter();
        lv_allMessage.setAdapter(adapter);

        tv_commentBtn = (TextView) findViewById(R.id.comment_btn);
        tv_commentBtn.setOnClickListener(this);

        tv_emoji = (TextView) findViewById(R.id.emoji_text);
        tv_emoji.setOnClickListener(this);

        fm_emoji = (FrameLayout) findViewById(R.id.Container);

        et_message.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fm_emoji.setVisibility(View.GONE);
                isShowEmoji = false;
            }
        });
        et_message.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean onFocus) {
                // TODO Auto-generated method stub
                if (onFocus) {
                } else {
                    tv_emoji.setVisibility(View.GONE);
                    iv_sendMessage.setVisibility(View.GONE);
                    tv_commentBtn.setVisibility(View.VISIBLE);
                    et_message.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_message.getWindowToken(), 0);
                    // hideSoftInput();
                }
                fm_emoji.setVisibility(View.GONE);
                isShowEmoji = false;
            }
        });

    }

    public void initData() {

        UserBusinessController.getInstance().getMessageSession(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, userID, new com.example.controller.controller.Listener<MyMessageBean>() {
            @Override
            public void onStart(Object... params) {
                if (active.equals("init")) {
                    showProgressDialog("正在获取数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(MyMessageBean bean, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                }

                dataList.addAll(bean.data.list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(String msg, Object... params) {
                if (active.equals("init")) {
                    dismissProgressDialog();
                }
                ToastHelper.showToast(AllMessageActivity.this, msg);
            }
        });
    }

    private class MessageAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(AllMessageActivity.this)
                        .inflate(R.layout.item_message, null, false);

                viewholder.icv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_time = (TextView) convertView
                        .findViewById(R.id.time);
                viewholder.tv_message = (TextView) convertView
                        .findViewById(R.id.message);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (dataList.get(position).head_pic.startsWith("http://")) {
                ImageLoaderWrapper.getDefault().displayImage(
                        dataList.get(position).head_pic,
                        viewholder.icv_userHeader);
            } else {
                ImageLoaderWrapper.getDefault().displayImage(
                        Constant.HOST_URL + dataList.get(position).head_pic,
                        viewholder.icv_userHeader);
            }
            viewholder.tv_userName.setText(dataList.get(position).user_name);
            viewholder.tv_time.setText(TimeUtil.getTimeString(dataList
                    .get(position).time * 1000));
            try {
                viewholder.tv_message
                        .setText(EmojiUtil.handlerEmojiText(
                                dataList.get(position).message,
                                AllMessageActivity.this));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView icv_userHeader;
        private TextView tv_userName;
        private TextView tv_time;
        private TextView tv_message;

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.send_btn:
                // 发送私信
                if (TextUtils.isEmpty(et_message.getText().toString().trim())) {
                    ToastHelper.showToast(this, "请编辑信息");
                    return;
                }
                hideSoftInput();
                UserBusinessController.getInstance().sendMessage(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", userID, et_message.getText().toString().trim(), new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在发送私信中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();

                        dataList.clear();
                        initData();
                        et_message.setText(null);
                        tv_commentBtn.setVisibility(View.VISIBLE);
                        tv_emoji.setVisibility(View.GONE);
                        et_message.setVisibility(View.GONE);
                        iv_sendMessage.setVisibility(View.GONE);
                        fm_emoji.setVisibility(View.GONE);
                        isShowEmoji = false;
                        hideSoftInput();
                        ToastHelper.showToast(AllMessageActivity.this, "发送成功");


                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(AllMessageActivity.this, msg);
                    }
                });
                break;
            case R.id.left_back:
                if (isPush) {
                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    finish();
                }
                break;
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.comment_btn:
                tv_commentBtn.setVisibility(View.GONE);
                et_message.setVisibility(View.VISIBLE);
                iv_sendMessage.setVisibility(View.VISIBLE);
                tv_emoji.setVisibility(View.VISIBLE);
                showSoftInput();
                et_message.requestFocus();
                break;
            case R.id.emoji_text:
                isShowEmoji = !isShowEmoji;
                hideSoftInput();
                if (isShowEmoji) {
                    fm_emoji.setVisibility(View.VISIBLE);
                } else {
                    fm_emoji.setVisibility(View.GONE);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onEmojiDelete() {
        // TODO Auto-generated method stub

        String text = et_message.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                et_message.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                return;
            }
            et_message.getText().delete(index, text.length());
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        et_message.onKeyDown(KeyEvent.KEYCODE_DEL, event);

    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            try {
                et_message.append(EmojiUtil.handlerEmojiText(
                        emoji.getContent(), this));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
