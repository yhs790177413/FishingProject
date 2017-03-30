package com.goby.fishing.module.other;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.FooterprintListBean;
import com.example.controller.bean.MyInfoBean;
import com.example.controller.bean.FooterprintListBean.Data.List;
import com.example.controller.bean.MyInfoBean.Data.Tags;
import com.goby.fishing.common.photochoose.ImageBrowseActivity;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.widget.imageview.ImageCircleView;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.fishing.BasicMapActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.me.MeFragment;
import com.goby.fishing.module.me.MyFooterprintActivity;
import com.goby.fishing.module.me.MyFooterprintActivity.ViewHolder;

public class OtherInfoActivity extends AbsBaseActivity implements
        OnClickListener {

    private TextView tv_userName;

    private TextView tv_fansCount;

    private TextView tv_attentionCount;

    private TextView tv_praiseCount;

    private ImageView icv_userHeader;

    private ArrayList<List> dataList = new ArrayList<List>();

    private ArrayList<String> imageData;

    private ImageGridAdapter imageAdapter;

    private Button btn_addAttention;

    private Button btn_sendMessage;

    private TextView tv_cancelAttention;

    private ImageView iv_back;

    private LinearLayout ll_leftBack;

    private ListView lv_otherFooterprint;

    private LinearLayout ll_parent;

    private FooterprintAdapter adapter;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private int page = 1;

    private int number = 20;

    private String userHeaderUrl;

    private String userName;

    private int follow;

    private int follower;

    private int status;

    private String mActive;

    private View headerView;

    private GridView gv_type;

    private ArrayList<Tags> tagsList = new ArrayList<Tags>();

    private int rankCount = 0;

    private String mHeaderPic;

    public static void launch(Activity activity, String user_id) {
        Intent intent = new Intent(activity, OtherInfoActivity.class);
        intent.putExtra("user_id", user_id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        initData();
    }

    public void initView() {

        headerView = LayoutInflater.from(this).inflate(
                R.layout.otherinfo_header, null);
        icv_userHeader = (ImageView) headerView.findViewById(R.id.user_header);
        tv_userName = (TextView) headerView.findViewById(R.id.user_name);
        tv_fansCount = (TextView) headerView.findViewById(R.id.fans_count);
        tv_attentionCount = (TextView) headerView
                .findViewById(R.id.attention_count);
        tv_praiseCount = (TextView) headerView.findViewById(R.id.praise_count);
        lv_otherFooterprint = (ListView) findViewById(R.id.other_footerprint);
        lv_otherFooterprint.addHeaderView(headerView);
        adapter = new FooterprintAdapter();
        lv_otherFooterprint.setAdapter(adapter);

        gv_type = (GridView) headerView.findViewById(R.id.type_grid);
        gv_type.setClickable(false);
        gv_type.setPressed(false);
        gv_type.setEnabled(false);
        gv_type.setSelector(new ColorDrawable(Color.TRANSPARENT));

        btn_addAttention = (Button) findViewById(R.id.add_attention);
        btn_sendMessage = (Button) findViewById(R.id.send_message);
        tv_cancelAttention = (TextView) findViewById(R.id.cancel_title);

        ll_parent = (LinearLayout) findViewById(R.id.parent_layout);
        iv_back = (ImageView) findViewById(R.id.left_back);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
        btn_addAttention.setOnClickListener(this);
        btn_sendMessage.setOnClickListener(this);
        tv_cancelAttention.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        lv_otherFooterprint.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                FooterprintDetailActivity.launch(OtherInfoActivity.this,
                        dataList.get(position - 1).id,
                        dataList.get(position - 1).pic_urls.get(0), -1,
                        "otherInfo");
            }
        });
        icv_userHeader.setOnClickListener(this);
    }

    public void initData() {
        UserBusinessController.getInstance().getOtherUserInfo(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", getIntent().getStringExtra("user_id"), new com.example.controller.controller.Listener<MyInfoBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("正在获取数据中,请稍候...");
            }

            @Override
            public void onComplete(MyInfoBean bean, Object... params) {
                dismissProgressDialog();

                mHeaderPic = bean.data.head_pic;
                if (mHeaderPic.startsWith("http://")) {
                    userHeaderUrl = mHeaderPic + "?imageView2/1/w/120/h/120";
                } else {
                    userHeaderUrl = Constant.HOST_URL + mHeaderPic
                            + "?imageView2/1/w/120/h/120";
                }
                userName = bean.data.user_name;
                follow = bean.data.follow;
                follower = bean.data.follower;
                status = bean.data.status;
                tagsList = bean.data.tags;
                rankCount = bean.data.popular;
                UserBusinessController.getInstance().getUserFishLocations(null, getVersionCode(), "2", page, number, bean.data.user_id, Double.parseDouble(sharedPreferenceUtil.getGPSLongitude()), Double.parseDouble(sharedPreferenceUtil.getGPSLatitude()), new com.example.controller.controller.Listener<FooterprintListBean>() {
                    @Override
                    public void onStart(Object... params) {

                    }

                    @Override
                    public void onComplete(FooterprintListBean bean, Object... params) {

                        ll_parent.setVisibility(View.VISIBLE);
                        ImageLoaderWrapper.getDefault().displayImage(userHeaderUrl,
                                icv_userHeader);
                        tv_userName.setText(userName);
                        tv_fansCount.setText("粉丝 " + follow);
                        tv_attentionCount.setText("关注 " + follower);
                        tv_praiseCount.setText("人气 " + rankCount);
                        if (status == 1) {
                            btn_addAttention.setVisibility(View.GONE);
                            btn_sendMessage.setVisibility(View.VISIBLE);
                            tv_cancelAttention.setVisibility(View.VISIBLE);
                        } else {
                            btn_addAttention.setVisibility(View.VISIBLE);
                            btn_sendMessage.setVisibility(View.GONE);
                            tv_cancelAttention.setVisibility(View.GONE);
                        }
                        if (tagsList != null && tagsList.size() > 0) {
                            gv_type.setVisibility(View.VISIBLE);
                            FishingTypeAdapter typeAdapter = new FishingTypeAdapter(
                                    tagsList);
                            gv_type.setAdapter(typeAdapter);
                            setGridViewHeightBasedOnChildren(gv_type, 4);
                        } else {
                            gv_type.setVisibility(View.GONE);
                        }
                        dataList.addAll(bean.data.list);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        ToastHelper.showToast(OtherInfoActivity.this, msg);
                    }
                });

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(OtherInfoActivity.this, msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.add_attention:
                mActive = "addAttention";
                // 添加关注
                UserBusinessController.getInstance().follow(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", getIntent().getStringExtra("user_id"), 1, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在添加关注中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();

                        if (mActive.equals("addAttention")) {
                            // 添加关注成功
                            MeFragment.is_refresh = true;
                            follow = follow + 1;
                            tv_fansCount.setText("粉丝 " + follow);
                            tv_attentionCount.setText("关注 " + follower);
                            btn_addAttention.setVisibility(View.GONE);
                            btn_sendMessage.setVisibility(View.VISIBLE);
                            tv_cancelAttention.setVisibility(View.VISIBLE);
                        } else if (mActive.equals("cancelAttention")) {
                            // 取消关注成功
                            MeFragment.is_refresh = true;
                            follow = follow - 1;
                            tv_fansCount.setText("粉丝 " + follow);
                            btn_addAttention.setVisibility(View.VISIBLE);
                            btn_sendMessage.setVisibility(View.GONE);
                            tv_cancelAttention.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(OtherInfoActivity.this, msg);
                    }
                });
                break;
            case R.id.send_message:
                // 发私信
                AllMessageActivity.launch(this,
                        getIntent().getStringExtra("user_id"));
                break;
            case R.id.cancel_title:
                mActive = "cancelAttention";
                // 取消关注
                UserBusinessController.getInstance().follow(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", getIntent().getStringExtra("user_id"), 0, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在取消关注中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();

                        if (mActive.equals("addAttention")) {
                            // 添加关注成功
                            MeFragment.is_refresh = true;
                            follow = follow + 1;
                            tv_fansCount.setText("粉丝 " + follow);
                            tv_attentionCount.setText("关注 " + follower);
                            btn_addAttention.setVisibility(View.GONE);
                            btn_sendMessage.setVisibility(View.VISIBLE);
                            tv_cancelAttention.setVisibility(View.VISIBLE);
                        } else if (mActive.equals("cancelAttention")) {
                            // 取消关注成功
                            MeFragment.is_refresh = true;
                            follow = follow - 1;
                            tv_fansCount.setText("粉丝 " + follow);
                            btn_addAttention.setVisibility(View.VISIBLE);
                            btn_sendMessage.setVisibility(View.GONE);
                            tv_cancelAttention.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(OtherInfoActivity.this, msg);
                    }
                });
                break;
            case R.id.left_back:
                finish();
                break;
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.user_header:
                ArrayList<String> imgs = new ArrayList<String>();
                if (mHeaderPic.startsWith("http://")) {
                    imgs.add(mHeaderPic);
                } else {
                    imgs.add(Constant.HOST_URL + mHeaderPic);
                }
                ImageBrowseActivity.launch(this, imgs, 0, "", "", "", "", "", "", "", false);
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
                convertView = LayoutInflater.from(OtherInfoActivity.this)
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
                    BasicMapActivity.launch(OtherInfoActivity.this,
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

    private class FishingTypeAdapter extends BaseAdapter {

        private ArrayList<Tags> mTypeList;

        public FishingTypeAdapter(ArrayList<Tags> typeList) {
            mTypeList = typeList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mTypeList.size();
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
                convertView = LayoutInflater.from(OtherInfoActivity.this)
                        .inflate(R.layout.item_fish_type, null, false);
                viewholder.iv_typeIcon = (ImageView) convertView
                        .findViewById(R.id.type_image);
                viewholder.tv_priseText = (TextView) convertView
                        .findViewById(R.id.prise_text);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            if (mTypeList.get(position).tag_id == 1) {
                viewholder.iv_typeIcon
                        .setBackgroundResource(R.drawable.daily_type_icon);
            } else if (mTypeList.get(position).tag_id == 2) {
                viewholder.iv_typeIcon
                        .setBackgroundResource(R.drawable.wild_fish_type_icon);
            } else if (mTypeList.get(position).tag_id == 3) {
                viewholder.iv_typeIcon
                        .setBackgroundResource(R.drawable.black_pit_type_icon);
            } else if (mTypeList.get(position).tag_id == 4) {
                viewholder.iv_typeIcon
                        .setBackgroundResource(R.drawable.equipment_type_icon);
            } else if (mTypeList.get(position).tag_id == 5) {
                viewholder.iv_typeIcon
                        .setBackgroundResource(R.drawable.food_type_icon);
            } else if (mTypeList.get(position).tag_id == 6) {
                viewholder.iv_typeIcon
                        .setBackgroundResource(R.drawable.lures_type_icon);
            } else if (mTypeList.get(position).tag_id == 7) {
                viewholder.iv_typeIcon
                        .setBackgroundResource(R.drawable.fishing_type_icon);
            }
            viewholder.tv_priseText.setText("人气"
                    + mTypeList.get(position).user_likes);
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
                convertView = LayoutInflater.from(OtherInfoActivity.this)
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
        private ImageView iv_typeIcon;
        private TextView tv_priseText;
        private ImageView iv_tagIcon;
        private TextView tv_tagCount;
    }

}
