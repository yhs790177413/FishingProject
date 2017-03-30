package com.goby.fishing.module.me;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.FooterprintListBean;
import com.example.controller.bean.FooterprintListBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.widget.imageview.ImageCircleView;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.fishing.BasicMapActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.me.MyFooterprintFragment.ViewHolder;

public class MyFooterprintActivity extends AbsBaseActivity implements
        OnClickListener {

    private ListView lv_footerPrint;

    private FooterprintAdapter adapter;

    private ArrayList<List> dataList = new ArrayList<List>();

    private ArrayList<String> imageData;

    private ImageGridAdapter imageAdapter;

    private int page = 1;

    private int number = 20;

    private ImageView iv_back;

    private LinearLayout ll_leftBack;

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static boolean refresh = false;

    public static void launch(Activity activity) {

        Intent intent = new Intent(activity, MyFooterprintActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footerprint);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        intiView();
        initData();
    }

    public void intiView() {

        lv_footerPrint = (ListView) findViewById(R.id.footerprint_list);
        adapter = new FooterprintAdapter();
        lv_footerPrint.setAdapter(adapter);
        lv_footerPrint.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                FooterprintDetailActivity.launch(MyFooterprintActivity.this,
                        dataList.get(position).id,
                        dataList.get(position).pic_urls.get(0), position,
                        "myFooterprint");
            }
        });
        iv_back = (ImageView) findViewById(R.id.left_back);
        iv_back.setOnClickListener(this);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
    }

    public void initData() {
        double longitude = 0, latitude = 0;
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
            longitude = Double.parseDouble(sharedPreferenceUtil.getGPSLongitude());
            latitude = Double.parseDouble(sharedPreferenceUtil.getGPSLatitude());
        }
        UserBusinessController.getInstance().getMyFooterprintList(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", sharedPreferenceUtil.getUserID(), page, number, latitude, longitude, new com.example.controller.controller.Listener<FooterprintListBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("正在获取数据中,请稍候...");
            }

            @Override
            public void onComplete(FooterprintListBean bean, Object... params) {
                dismissProgressDialog();

                if (bean.data.list != null && bean.data.list.size() > 0) {
                    dataList.addAll(bean.data.list);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastHelper.showToast(MyFooterprintActivity.this, "暂无数据");
                }


            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(MyFooterprintActivity.this, msg);
            }
        });
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

    private class FooterprintAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(MyFooterprintActivity.this)
                        .inflate(R.layout.item_footer_print, null, false);
                viewholder.iv_userHeader = (ImageView) convertView
                        .findViewById(R.id.user_header);
                viewholder.tv_userName = (TextView) convertView
                        .findViewById(R.id.user_name);
                viewholder.tv_friendCity = (TextView) convertView
                        .findViewById(R.id.city_name);
                viewholder.tv_time = (TextView) convertView
                        .findViewById(R.id.time);
                viewholder.tv_content = (TextView) convertView
                        .findViewById(R.id.content);
                viewholder.gv_infoImage = (GridView) convertView
                        .findViewById(R.id.footer_image_grid);
                viewholder.gv_infoImage.setClickable(false);
                viewholder.gv_infoImage.setPressed(false);
                viewholder.gv_infoImage.setEnabled(false);
                viewholder.gv_infoImage.setSelector(new ColorDrawable(
                        Color.TRANSPARENT));
                viewholder.tv_imageCount = (TextView) convertView
                        .findViewById(R.id.image_count);
                viewholder.tv_fishName = (TextView) convertView
                        .findViewById(R.id.fish_name);
                viewholder.tv_fishType = (TextView) convertView
                        .findViewById(R.id.fish_tool);
                viewholder.tv_clound = (TextView) convertView
                        .findViewById(R.id.weather);
                viewholder.tv_distance = (TextView) convertView
                        .findViewById(R.id.distance);
                viewholder.tv_prise = (TextView) convertView
                        .findViewById(R.id.prise);
                viewholder.tv_comment = (TextView) convertView
                        .findViewById(R.id.commends);
                viewholder.iv_gpsIcon = (ImageView) convertView
                        .findViewById(R.id.gps_cricle);
                viewholder.iv_tagIcon = (ImageView) convertView
                        .findViewById(R.id.tag_icon);
                viewholder.tv_tagCount = (TextView) convertView
                        .findViewById(R.id.tag_count);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.iv_userHeader
                    .setImageResource(R.drawable.header_circle_icon);
            if (dataList.get(position).head_pic.startsWith("http://")) {
                ImageLoaderWrapper.getDefault().displayImage(
                        dataList.get(position).head_pic,
                        viewholder.iv_userHeader);
            } else {
                ImageLoaderWrapper.getDefault().displayImage(
                        Constant.HOST_URL + dataList.get(position).head_pic,
                        viewholder.iv_userHeader);
            }
            viewholder.tv_userName.setText(dataList.get(position).user_name);
            if (TextUtils.isEmpty(dataList.get(position).city_name)) {
                viewholder.tv_friendCity.setVisibility(View.GONE);
            } else {
                viewholder.tv_friendCity.setVisibility(View.VISIBLE);
                viewholder.tv_friendCity
                        .setText(dataList.get(position).city_name);
            }
            viewholder.tv_time.setText(TimeUtil.getTimeString(dataList
                    .get(position).time * 1000));
            try {
                if (dataList.get(position).tag.tag_id == 1) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.daily_type_icon);
                } else if (dataList.get(position).tag.tag_id == 2) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.wild_fish_type_icon);
                } else if (dataList.get(position).tag.tag_id == 3) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.black_pit_type_icon);
                } else if (dataList.get(position).tag.tag_id == 4) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.equipment_type_icon);
                } else if (dataList.get(position).tag.tag_id == 5) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.food_type_icon);
                } else if (dataList.get(position).tag.tag_id == 6) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.lures_type_icon);
                } else if (dataList.get(position).tag.tag_id == 7) {
                    viewholder.iv_tagIcon
                            .setBackgroundResource(R.drawable.fishing_type_icon);
                }
                viewholder.tv_tagCount.setText(String.valueOf(dataList
                        .get(position).tag.user_likes) + "人气");
            } catch (Exception e) {
                // TODO: handle exception
                viewholder.iv_tagIcon
                        .setBackgroundResource(R.drawable.daily_type_icon);
                viewholder.tv_tagCount.setText("0人气");
            }
            if (TextUtils.isEmpty(dataList.get(position).info)) {
                viewholder.tv_content.setVisibility(View.GONE);
            } else {
                viewholder.tv_content.setVisibility(View.VISIBLE);
                viewholder.tv_content.setText(dataList.get(position).info);
            }
            if (dataList.get(position).picture_number > 3) {
                viewholder.tv_imageCount.setVisibility(View.VISIBLE);
                viewholder.tv_imageCount
                        .setText(dataList.get(position).picture_number + " 图");
            } else {
                viewholder.tv_imageCount.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(dataList.get(position).fish_info)) {
                viewholder.tv_fishName.setVisibility(View.GONE);
            } else {
                viewholder.tv_fishName.setVisibility(View.VISIBLE);
                viewholder.tv_fishName
                        .setText(dataList.get(position).fish_info);
            }
            if (TextUtils.isEmpty(dataList.get(position).tools)) {
                viewholder.tv_fishType.setVisibility(View.GONE);
            } else {
                viewholder.tv_fishType.setVisibility(View.VISIBLE);
                viewholder.tv_fishType.setText(dataList.get(position).tools);
            }
            if (TextUtils.isEmpty(dataList.get(position).weather)) {
                viewholder.tv_clound.setVisibility(View.GONE);
            } else {
                viewholder.tv_clound.setVisibility(View.VISIBLE);
                viewholder.tv_clound.setText(dataList.get(position).weather);
            }
            if (TextUtils.isEmpty(dataList.get(position).address_info)) {
                viewholder.tv_distance.setVisibility(View.GONE);
                viewholder.iv_gpsIcon.setVisibility(View.GONE);
            } else {
                viewholder.iv_gpsIcon.setVisibility(View.VISIBLE);
                viewholder.tv_distance.setVisibility(View.VISIBLE);
                viewholder.tv_distance
                        .setText(dataList.get(position).address_info);
            }
            viewholder.tv_distance.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    BasicMapActivity.launch(MyFooterprintActivity.this,
                            dataList.get(position).longitude,
                            dataList.get(position).latitude,
                            dataList.get(position).info,
                            dataList.get(position).pic_urls.get(0), "");
                }
            });
            viewholder.tv_prise.setText(dataList.get(position).like_number
                    + " 赞");
            viewholder.tv_comment.setText(dataList.get(position).comment_number
                    + " 评论");
            imageData = new ArrayList<String>();
            imageData.addAll(dataList.get(position).pic_urls);
            imageAdapter = new ImageGridAdapter(imageData);
            viewholder.gv_infoImage.setAdapter(imageAdapter);
            return convertView;
        }
    }

    private class ImageGridAdapter extends BaseAdapter {

        private ArrayList<String> mImageList;

        public ImageGridAdapter(ArrayList<String> imageList) {
            mImageList = imageList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mImageList.size();
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
                convertView = LayoutInflater.from(MyFooterprintActivity.this)
                        .inflate(R.layout.item_footer_image, null, false);
                viewholder.iv_footerImage = (ImageView) convertView
                        .findViewById(R.id.footer_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderWrapper.getDefault().displayImage(
                    imageData.get(position), viewholder.iv_footerImage);
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView iv_userHeader;
        private TextView tv_userName;
        private TextView tv_friendCity;
        private TextView tv_time;
        private TextView tv_content;
        private GridView gv_infoImage;
        private TextView tv_imageCount;
        private ImageView iv_footerImage;
        private TextView tv_fishName;
        private TextView tv_fishType;
        private TextView tv_clound;
        private TextView tv_distance;
        private TextView tv_prise;
        private TextView tv_comment;
        private ImageView iv_gpsIcon;
        private ImageView iv_tagIcon;
        private TextView tv_tagCount;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (refresh) {
            refresh = false;
            dataList.clear();
            adapter.notifyDataSetChanged();
            initData();
        }
    }
}
