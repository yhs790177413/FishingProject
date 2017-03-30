package com.goby.fishing.module.fishing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FishingListBean;
import com.example.controller.bean.FishingListBean.Data.List;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class SearchFishPointActivity extends AbsBaseActivity implements
        OnClickListener, OnItemClickListener {

    private LinearLayout ll_leftBack;

    private TextView tv_search;

    private ListView lv_fishPointName;

    private FishPointNameAdapter adapter;

    private ImageView iv_searchView;

    private RelativeLayout rl_searchLayout;

    private EditText et_search;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private int page = 1;

    private int number = 20;

    private String fishPointName;

    private int fishPointId;

    public static ArrayList<List> dataList = new ArrayList<List>();

    public static void launch(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, SearchFishPointActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fishpoint);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
    }

    public void initView() {

        et_search = (EditText) findViewById(R.id.search_edit);
        rl_searchLayout = (RelativeLayout) findViewById(R.id.search_layout_two);
        iv_searchView = (ImageView) findViewById(R.id.search_one_view);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        tv_search = (TextView) findViewById(R.id.search_text);
        lv_fishPointName = (ListView) findViewById(R.id.fish_point_list);
        adapter = new FishPointNameAdapter();
        lv_fishPointName.setAdapter(adapter);

        iv_searchView.setOnClickListener(this);
        ll_leftBack.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        lv_fishPointName.setOnItemClickListener(this);
    }


    private class FishPointNameAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(SearchFishPointActivity.this)
                        .inflate(R.layout.item_fishpoint_name, null, false);
                viewholder.tv_fishingName = (TextView) convertView
                        .findViewById(R.id.fishpoint_name);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.tv_fishingName.setText(dataList.get(position).name);
            return convertView;
        }
    }

    public class ViewHolder {

        private TextView tv_fishingName;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // TODO Auto-generated method stub
        fishPointName = dataList.get(position).name;
        fishPointId = dataList.get(position).id;
        FishPointInfoActivity.launch(this, 1003,
                dataList.get(position).longitude,
                dataList.get(position).latitude);
        Log.d("selcet", dataList.get(position).longitude + "==" + dataList.get(position).latitude);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, data);
        if (data != null) {
            Intent intent;
            intent = new Intent();
            intent.putExtra("fishPointId", fishPointId);
            intent.putExtra("fishPointName", fishPointName);
            intent.putExtra("quality", data.getIntExtra("quality", 0));
            intent.putExtra("fishPointInfo",
                    data.getStringExtra("fishPointInfo"));
            intent.putExtra("chooseLongitude",
                    data.getDoubleExtra("chooseLongitude", 0));
            intent.putExtra("chooseLatitude",
                    data.getDoubleExtra("chooseLatitude", 0));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.search_one_view:
                iv_searchView.setVisibility(View.GONE);
                rl_searchLayout.setVisibility(View.VISIBLE);
                tv_search.setVisibility(View.VISIBLE);
                break;
            case R.id.search_text:
                if (TextUtils.isEmpty(et_search.getText().toString().trim())) {
                    ToastHelper.showToast(this, "请输入关键字再搜索");
                    return;
                }
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    manager.hideSoftInputFromWindow(getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                dataList.clear();
                double latitude = 0, longitude = 0;
                if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
                    latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
                    longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
                }
                UserBusinessController.getInstance().searchFishPoints(sharedPreferenceUtil.getUserToken(),
                        getVersionCode(), "2", 1, 20, et_search
                                .getText().toString().trim(), latitude, longitude, new com.example.controller.controller.Listener<FishingListBean>() {
                            @Override
                            public void onStart(Object... params) {
                                showProgressDialog("正在获取数据中,请稍候...");
                            }

                            @Override
                            public void onComplete(FishingListBean bean, Object... params) {
                                dismissProgressDialog();


                                dataList.addAll(bean.data.list);
                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                dismissProgressDialog();
                                ToastHelper.showToast(SearchFishPointActivity.this, msg);
                            }
                        });
                break;
            default:
                break;
        }
    }

}
