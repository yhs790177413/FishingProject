package com.goby.fishing.module.fishing;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FishTypeBean;
import com.example.controller.bean.FishTypeBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.widget.imageview.ImageCircleView;
import com.goby.fishing.framework.BaseFragment;

public class SeawaterFragment extends BaseFragment implements
        OnClickListener, OnItemClickListener {

    private ListView lv_fishing;

    private FishTypeAdapter adapter;

    private ArrayList<List> dataList = new ArrayList<List>();

    private int page = 1;

    private int number = 20;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

    public static SeawaterFragment newInstance() {
        return new SeawaterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_attention, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        FishingTypeAcitivtyOne.typeIdsList.clear();
        FishingTypeAcitivtyOne.typeNameList.clear();
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

        lv_fishing = (ListView) view.findViewById(R.id.attention_list);
        adapter = new FishTypeAdapter();
        lv_fishing.setAdapter(adapter);
        lv_fishing.setOnItemClickListener(this);
    }

    public void initData() {

        UserBusinessController.getInstance().fishTypeJson(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, new com.example.controller.controller.Listener<FishTypeBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(FishTypeBean bean, Object... params) {

                for (int i = 0; i < bean.data.list.size(); i++) {
                    map.put(i, false);
                }
                dataList.addAll(bean.data.list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(String msg, Object... params) {
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
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_fishing_type, null, false);

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
            FishingTypeAcitivtyOne.typeIdsList.remove(String.valueOf(dataList
                    .get(position).id));
            FishingTypeAcitivtyOne.typeNameList
                    .remove(dataList.get(position).name);

        } else {
            map.put(position, true);
            FishingTypeAcitivtyOne.typeIdsList.add(String.valueOf(dataList
                    .get(position).id));
            FishingTypeAcitivtyOne.typeNameList.add(dataList.get(position).name);
        }
        adapter.notifyDataSetChanged();
    }
}
