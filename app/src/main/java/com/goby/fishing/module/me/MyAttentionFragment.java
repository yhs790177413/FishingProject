package com.goby.fishing.module.me;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controller.Constant;
import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.AttentionFriendsBean;
import com.example.controller.bean.AttentionFriendsBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.other.OtherInfoActivity;

public class MyAttentionFragment extends BaseFragment implements
        View.OnClickListener, OnItemClickListener {

    private ListView lv_fishing;

    private AttentionAdapter adapter;

    private ArrayList<List> dataList = new ArrayList<List>();

    private int page = 1;

    private int number = 20;

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static MyAttentionFragment newInstance() {
        return new MyAttentionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_attention, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

        lv_fishing = (ListView) view.findViewById(R.id.attention_list);
        adapter = new AttentionAdapter();
        lv_fishing.setAdapter(adapter);
        lv_fishing.setOnItemClickListener(this);
    }

    public void initData() {
        UserBusinessController.getInstance().getAttentionList(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", sharedPreferenceUtil.getUserID(), page, number, 0, new com.example.controller.controller.Listener<AttentionFriendsBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("正在获取数据中,请稍候...");
            }

            @Override
            public void onComplete(AttentionFriendsBean bean, Object... params) {
                dismissProgressDialog();

                dataList.addAll(bean.data.list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(getActivity(), msg);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            default:
                break;
        }
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
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_my_attention, null, false);

                viewholder.icv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.icv_userHeader
                    .setImageResource(R.drawable.header_circle_icon);
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
            MainActivity.launch(getActivity(), "meFragment");
        } else {
            OtherInfoActivity.launch(getActivity(),
                    dataList.get(position).user_id);
        }
    }
}
