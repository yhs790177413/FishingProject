package com.goby.fishing.module.me;

import java.util.ArrayList;
import java.util.Map;

import com.example.controller.Constant;
import com.example.controller.bean.MyInfoBean;
import com.example.controller.controller.UserBusinessController;
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
import com.goby.fishing.module.fishing.FishingDetailActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.information.FishingInfoDetailActivity;
import com.goby.fishing.module.other.AllMessageActivity;
import com.goby.fishing.module.other.OtherInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyMessageActivty extends AbsBaseActivity implements
        OnClickListener {

    private ListView lv_message;

    private MessageAdapter adapter;

    private int page = 1;

    private int number = 20;

    private ArrayList<List> dataList = new ArrayList<List>();

    private SharedPreferenceUtil sharedPreferenceUtil;

    private ImageView iv_back;

    private LinearLayout ll_leftBack;

    public static void launch(Activity activity) {

        Intent intent = new Intent(activity, MyMessageActivty.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        initData();
    }

    public void initView() {
        lv_message = (ListView) findViewById(R.id.my_message_list);
        adapter = new MessageAdapter();
        lv_message.setAdapter(adapter);
        iv_back = (ImageView) findViewById(R.id.left_back);
        iv_back.setOnClickListener(this);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
        lv_message.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (dataList.get(position).type == 100) {
                    AllMessageActivity.launch(MyMessageActivty.this,
                            dataList.get(position).object_id);
                } else if (dataList.get(position).type == 4) {
                    if (dataList.get(position).object_id
                            .equals(sharedPreferenceUtil.getUserID())) {
                        MainActivity
                                .launch(MyMessageActivty.this, "meFragment");
                    } else {
                        OtherInfoActivity.launch(MyMessageActivty.this,
                                dataList.get(position).object_id);
                    }
                } else if (dataList.get(position).type == 3) {
                    FooterprintDetailActivity.launch(MyMessageActivty.this,
                            Integer.parseInt(dataList.get(position).object_id),
                            null, 0, "myMessage");
                } else if (dataList.get(position).type == 2) {
                    FishingDetailActivity.launch(MyMessageActivty.this,
                            Integer.parseInt(dataList.get(position).object_id),
                            -1, "myMessage", null);
                } else if (dataList.get(position).type == 1) {
                    FishingInfoDetailActivity.launch(MyMessageActivty.this,
                            Integer.parseInt(dataList.get(position).object_id),
                            -1, "myMessage", null);
                }
            }
        });
    }

    public void initData() {

        UserBusinessController.getInstance().readAll(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", new com.example.controller.controller.Listener<BaseBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("正在获取数据中,请稍候...");
            }

            @Override
            public void onComplete(BaseBean bean, Object... params) {
                dismissProgressDialog();

                    sharedPreferenceUtil.setRedPointIsVisible(false);
                    UserBusinessController.getInstance().getMyMessageList(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, 0, new com.example.controller.controller.Listener<MyMessageBean>() {
                        @Override
                        public void onStart(Object... params) {

                        }

                        @Override
                        public void onComplete(MyMessageBean bean, Object... params) {

                                if (bean.data.list != null && bean.data.list.size() > 0) {
                                    dataList.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    ToastHelper.showToast(MyMessageActivty.this, "暂无任何消息");
                                }

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            ToastHelper.showToast(MyMessageActivty.this, msg);
                        }
                    });

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(MyMessageActivty.this, msg);
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
                convertView = LayoutInflater.from(MyMessageActivty.this)
                        .inflate(R.layout.item_my_message, null, false);

                viewholder.icv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_title = (TextView) convertView
                        .findViewById(R.id.message_type);
                viewholder.tv_time = (TextView) convertView
                        .findViewById(R.id.time);
                viewholder.tv_message = (TextView) convertView
                        .findViewById(R.id.message_content);
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
            viewholder.tv_title.setText(dataList.get(position).title);
            viewholder.tv_time.setText(TimeUtil.getTimeString(dataList
                    .get(position).time * 1000));
            viewholder.tv_message.setText(dataList.get(position).message);
            return convertView;
        }
    }

    private class MyMessageImageAdapter extends BaseAdapter {

        private String mImageUrl;

        public MyMessageImageAdapter(String imageUrl) {
            mImageUrl = imageUrl;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 1;
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
                convertView = LayoutInflater.from(MyMessageActivty.this)
                        .inflate(R.layout.item_message_header, null, false);
                viewholder.icv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }

            if (mImageUrl.startsWith("http://")) {
                ImageLoaderWrapper.getDefault().displayImage(mImageUrl,
                        viewholder.icv_userHeader);
            } else {
                ImageLoaderWrapper.getDefault()
                        .displayImage(Constant.HOST_URL + mImageUrl,
                                viewholder.icv_userHeader);
            }
            return convertView;
        }

    }

    public class ViewHolder {

        private ImageView icv_userHeader;
        private TextView tv_userName;
        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_message;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.left_back_layout:
                finish();
                break;
            default:
                break;
        }
    }
}
