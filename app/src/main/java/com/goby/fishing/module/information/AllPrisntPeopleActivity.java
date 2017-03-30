package com.goby.fishing.module.information;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.controller.Constant;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.FishingInfoDetailBean.Data.Like;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.footerprint.AllPeopleActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.other.OtherInfoActivity;

public class AllPrisntPeopleActivity extends AbsBaseActivity implements
        OnClickListener, OnItemClickListener {

    private ListView lv_fishing;

    private AttentionAdapter adapter;

    private ArrayList<Like> dataList = new ArrayList<Like>();

    private int page = 1;

    private int number = 20;

    private LinearLayout ll_leftBack;

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static void launch(Activity activity, ArrayList<Like> dataList) {

        Intent intent = new Intent(activity, AllPrisntPeopleActivity.class);
        intent.putExtra("dataList", dataList);
        activity.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_people);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
    }

    public void initView() {

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
        dataList.addAll((ArrayList<Like>) getIntent().getSerializableExtra(
                "dataList"));
        lv_fishing = (ListView) findViewById(R.id.attention_list);
        adapter = new AttentionAdapter();
        lv_fishing.setAdapter(adapter);
        lv_fishing.setOnItemClickListener(this);
    }

    private class AttentionAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(AllPrisntPeopleActivity.this)
                        .inflate(R.layout.item_my_attention, null, false);

                viewholder.icv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
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
            viewholder.icv_userHeader.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (dataList.get(position).user_id
                            .equals(sharedPreferenceUtil.getUserID())) {
                        MainActivity.launch(AllPrisntPeopleActivity.this,
                                "meFragment");
                    } else {
                        OtherInfoActivity.launch(AllPrisntPeopleActivity.this,
                                dataList.get(position).user_id);
                    }
                }
            });
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView icv_userHeader;
        private TextView tv_userName;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // TODO Auto-generated method stub
        if (dataList.get(position).user_id.equals(sharedPreferenceUtil
                .getUserID())) {
            MainActivity.launch(AllPrisntPeopleActivity.this, "meFragment");
        } else {
            OtherInfoActivity.launch(AllPrisntPeopleActivity.this,
                    dataList.get(position).user_id);
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;

            default:
                break;
        }
    }
}
