package com.goby.fishing.module.fishing;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FishTypeBean;
import com.example.controller.bean.FishTypeBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class FishingTypeActivity extends AbsBaseActivity implements
        OnClickListener, OnItemClickListener {

    public ArrayList<String> typeIdsList = new ArrayList<String>();
    public ArrayList<String> typeNameList = new ArrayList<String>();
    private String type_ids = "";
    private String type_names = "";

    private ListView lv_fishing;

    private FishTypeAdapter adapter;

    private ArrayList<List> dataList = new ArrayList<List>();

    private int page = 1;

    private int number = 20;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

    private LinearLayout ll_leftBack;

    private TextView tv_rightBtn;

    public static void launch(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, FishingTypeActivity.class);
        intent.putExtra("requestCode", requestCode);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_type);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        initData();
    }

    public void initView() {

        lv_fishing = (ListView) findViewById(R.id.attention_list);
        adapter = new FishTypeAdapter();
        lv_fishing.setAdapter(adapter);
        lv_fishing.setOnItemClickListener(this);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
        tv_rightBtn = (TextView) findViewById(R.id.right_btn);
        tv_rightBtn.setOnClickListener(this);
    }

    public void initData() {

        UserBusinessController.getInstance().fishTypeJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, new com.example.controller.controller.Listener<FishTypeBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("正在加载数据中,请稍候...");
            }

            @Override
            public void onComplete(FishTypeBean bean, Object... params) {
                dismissProgressDialog();

                for (int i = 0; i < bean.data.list.size(); i++) {
                    map.put(i, false);
                }
                dataList.addAll(bean.data.list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(FishingTypeActivity.this, msg);
            }
        });
    }


    private class FishTypeAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(FishingTypeActivity.this)
                        .inflate(R.layout.item_fishing_type, null, false);

                viewholder.icv_fishImage = (ImageView) convertView
                        .findViewById(R.id.fish_image);
                viewholder.tv_fishName = (TextView) convertView
                        .findViewById(R.id.fish_name);
                viewholder.cb_select = (ImageView) convertView
                        .findViewById(R.id.check_btn);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.icv_fishImage
                    .setImageResource(R.drawable.fish_point_view_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    dataList.get(position).pic_url, viewholder.icv_fishImage);
            viewholder.tv_fishName.setText(dataList.get(position).name);
            if (map.get(position)) {
                viewholder.cb_select
                        .setBackgroundResource(R.drawable.check_fish);
            } else {
                viewholder.cb_select
                        .setBackgroundResource(R.drawable.un_check_fish);
            }
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView icv_fishImage;
        private TextView tv_fishName;
        private ImageView cb_select;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // TODO Auto-generated method stub
        if (map.get(position)) {
            map.put(position, false);
            typeIdsList.remove(String.valueOf(dataList.get(position).id));
            typeNameList.remove(dataList.get(position).name);

        } else {
            map.put(position, true);
            typeIdsList.add(String.valueOf(dataList.get(position).id));
            typeNameList.add(dataList.get(position).name);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.right_btn:
                if (typeIdsList.size() > 0) {
                    for (int i = 0; i < typeIdsList.size(); i++) {
                        type_ids = type_ids + typeIdsList.get(i) + ",";
                    }
                    for (int i = 0; i < typeNameList.size(); i++) {
                        type_names = type_names + typeNameList.get(i) + " ";
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("type_ids", type_ids);
                intent.putExtra("type_names", type_names);
                setResult(RESULT_OK, intent);
                finish();
                break;
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
