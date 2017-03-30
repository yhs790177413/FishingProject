package com.goby.fishing.module.index;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.bumptech.glide.Glide;
import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.fishing.R;
import com.example.controller.bean.FishingInfoBean;
import com.example.controller.bean.FishingInfoBean.Data.List;
import com.goby.fishing.common.utils.helper.android.app.BorderTextView;
import com.goby.fishing.common.utils.helper.android.app.WebViewActivity;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.fishing.FishingDetailActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.information.FishingInfoDetailActivity;
import com.goby.fishing.module.login.LoginActivity;

public class TagInfosActivity extends AbsBaseActivity implements
        OnClickListener {

    private ListView mInfoList;

    private InfoAdapter adapter;

    private ImageGridAdapter imageAdapter;

    private ArrayList<List> infoData = new ArrayList<List>();

    private ArrayList<String> imageData;

    private int page = 1;

    private int number = 20;

    private View footerView;

    private View loadMore; // 加载更多的view

    private View loading; // 加载进度条

    private String active = "init", id, titleName, ids;

    private TextView tv_centerTitle;

    private LinearLayout ll_leftBack;

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static void launch(Activity activity, String id, String titleName,
                              String ids) {
        Intent intent = new Intent(activity, TagInfosActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("ids", ids);
        intent.putExtra("titleName", titleName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_infos);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        id = getIntent().getStringExtra("id");
        ids = getIntent().getStringExtra("ids");
        titleName = getIntent().getStringExtra("titleName");
        initFooter();
        initView();
        getRemote();
    }

    public void initView() {

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        tv_centerTitle = (TextView) findViewById(R.id.center_title);
        if (!TextUtils.isEmpty(ids)) {
            tv_centerTitle.setText("找相似");
        } else {
            tv_centerTitle.setText(titleName);
        }
        mInfoList = (ListView) findViewById(R.id.info_list);
        adapter = new InfoAdapter();
        mInfoList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mInfoList.setAdapter(adapter);
        mInfoList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
//				if (position < infoData.size()) {
//					FishingInfoDetailActivity.launch(TagInfosActivity.this,
//							infoData.get(position).id, position, "tagInfos",
//							infoData.get(position).pic_urls.get(0));
//				}
                if (infoData.get(position).content_url
                        .split("://")[1].split("/")[0].equals("info")) {
                    FishingInfoDetailActivity.launch(
                            TagInfosActivity.this,
                            Integer.parseInt(infoData.get(position).content_url
                                    .split("://")[1].split("/")[1]),
                            position,
                            "fish",
                            infoData.get(position).pic_urls
                                    .get(0), 0);
                } else if (infoData.get(position).content_url
                        .split("://")[1].split("/")[0]
                        .equals("fishpoint")) {
                    FishingDetailActivity.launch(
                            TagInfosActivity.this,
                            Integer.parseInt(infoData.get(position).content_url
                                    .split("://")[1].split("/")[1]),
                            position,
                            "info_fragmrnt",
                            infoData.get(position).pic_urls
                                    .get(0));
                } else if (infoData.get(position).content_url
                        .split("://")[1].split("/")[0].equals("feed")) {
                    FooterprintDetailActivity.launch(
                            TagInfosActivity.this,
                            Integer.parseInt(infoData.get(position).content_url
                                    .split("://")[1].split("/")[1]),
                            infoData.get(position).pic_urls
                                    .get(0), position, "info_fragment");
                } else if (infoData.get(position).content_url
                        .split("://")[1].split("/")[0]
                        .equals("activity")) {
                    ActiveDetailActivity
                            .launch(TagInfosActivity.this,
                                    infoData.get(position).content_url
                                            .split("://")[1].split("/")[1]);
                } else if (infoData.get(position).content_url
                        .split("://")[1].equals("fishfun")) {
                    if (TextUtils.isEmpty(sharedPreferenceUtil
                            .getUserToken())) {
                        ToastHelper.showToast(TagInfosActivity.this,
                                "您还未登录,请先登录");
                        LoginActivity.launch(TagInfosActivity.this,
                                "indexFragment");
                    } else {
                        GameActivity.launch(TagInfosActivity.this);
                    }
                } else if (infoData.get(position).content_url
                        .startsWith("http")) {
                    if (infoData.get(position).content_url
                            .startsWith("https://wap.koudaitong.com")) {
                        AsyncWebActivity
                                .launch(TagInfosActivity.this,
                                        infoData.get(position).content_url,
                                        infoData.get(position).title);
                    } else {
                        WebViewActivity
                                .launch(TagInfosActivity.this,
                                        infoData.get(position).content_url,
                                        infoData.get(position).title);
                    }
                }
            }
        });
        ll_leftBack.setOnClickListener(this);
    }

    /**
     * 初始化footer
     */
    private void initFooter() {
        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view,
                null);
        loadMore = footerView.findViewById(R.id.load_more);
        loading = footerView.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
    }

    public void getRemote() {
        UserBusinessController.getInstance().getTagInfosJson(null, getVersionCode(), "2", page, number, id, ids, new com.example.controller.controller.Listener<FishingInfoBean>() {
            @Override
            public void onStart(Object... params) {
                if (active.equals("init")) {
                    showProgressDialog("获取数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(FishingInfoBean bean, Object... params) {
                dismissProgressDialog();

                    if (active.equals("init")) {
                        if (bean.data.list.size() < 20) {
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.addFooterView(footerView);
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        infoData.clear();
                        infoData.addAll(bean.data.list);
                        adapter.notifyDataSetChanged();
                    } else if (active.equals("update")) {
                        if (bean.data.list.size() < 20) {
                            mInfoList.removeFooterView(footerView);
                            mInfoList.setOnScrollListener(null);
                        } else {
                            mInfoList.setOnScrollListener(new UpdateListener());
                        }
                        infoData.addAll(bean.data.list);
                        adapter.notifyDataSetChanged();
                    }

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(TagInfosActivity.this, msg);
            }
        });
    }

    private class UpdateListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 当不滚动时
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                // 判断是否滚动到底部
                Glide.with(TagInfosActivity.this).resumeRequests();
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 加载更多
                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    active = "update";
                    page++;
                    getRemote();
                }
            } else {
                Glide.with(TagInfosActivity.this).pauseRequests();
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_back_layout:
                finish();
                break;

            default:
                break;
        }
    }

    private class InfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return infoData.size();
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
                convertView = LayoutInflater.from(TagInfosActivity.this)
                        .inflate(R.layout.item_info_fragment, null, false);
                viewholder.tv_infoNameOne = (TextView) convertView
                        .findViewById(R.id.info_name_one);
                viewholder.tv_infoNameTwo = (TextView) convertView
                        .findViewById(R.id.info_name_two);
                viewholder.tv_infoNameThree = (TextView) convertView
                        .findViewById(R.id.info_name_three);
                viewholder.iv_infoImageOne = (ImageView) convertView
                        .findViewById(R.id.info_image_one);
                viewholder.iv_infoImageThree = (ImageView) convertView
                        .findViewById(R.id.info_image_three);
                viewholder.rl_layoutOne = (RelativeLayout) convertView
                        .findViewById(R.id.layout_one);
                viewholder.ll_layoutTwo = (LinearLayout) convertView
                        .findViewById(R.id.layout_two);
                viewholder.ll_layoutThree = (LinearLayout) convertView
                        .findViewById(R.id.layout_three);
                viewholder.tv_commentCount = (TextView) convertView
                        .findViewById(R.id.comment_count);
                viewholder.tv_time = (TextView) convertView
                        .findViewById(R.id.time);
                viewholder.gv_infoImage = (GridView) convertView
                        .findViewById(R.id.info_image_grid);
                viewholder.gv_infoImage.setClickable(false);
                viewholder.gv_infoImage.setPressed(false);
                viewholder.gv_infoImage.setEnabled(false);
                viewholder.gv_infoImage.setSelector(new ColorDrawable(
                        Color.TRANSPARENT));
                viewholder.tv_prase = (TextView) convertView
                        .findViewById(R.id.praise_count);
                viewholder.iv_typeImage = (BorderTextView) convertView
                        .findViewById(R.id.type_image);
                viewholder.btv_jianImage = (BorderTextView) convertView
                        .findViewById(R.id.jian_image);
                viewholder.tv_visit = (TextView) convertView
                        .findViewById(R.id.visit_text);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }

            if (infoData.get(position).preview_style == 1) {
                viewholder.rl_layoutOne.setVisibility(View.VISIBLE);
                viewholder.ll_layoutTwo.setVisibility(View.GONE);
                viewholder.ll_layoutThree.setVisibility(View.GONE);
            } else if (infoData.get(position).preview_style == 2) {
                viewholder.rl_layoutOne.setVisibility(View.GONE);
                viewholder.ll_layoutTwo.setVisibility(View.VISIBLE);
                viewholder.ll_layoutThree.setVisibility(View.GONE);
            } else if (infoData.get(position).preview_style == 3) {
                viewholder.rl_layoutOne.setVisibility(View.GONE);
                viewholder.ll_layoutTwo.setVisibility(View.GONE);
                viewholder.ll_layoutThree.setVisibility(View.VISIBLE);
            }
            ImageUtils.getInstance().loadImage(TagInfosActivity.this,
                    infoData.get(position).pic_urls.get(0), R.drawable.loadding_icon,
                    viewholder.iv_infoImageOne);
            ImageUtils.getInstance().loadImage(TagInfosActivity.this,
                    infoData.get(position).pic_urls.get(0), R.drawable.loadding_icon,
                    viewholder.iv_infoImageThree);
            viewholder.tv_infoNameOne.setText(infoData.get(position).title);
            viewholder.tv_infoNameTwo.setText(infoData.get(position).title);
            viewholder.tv_infoNameThree.setText(infoData.get(position).title);
            viewholder.tv_commentCount
                    .setText(infoData.get(position).comment_number + " 评论");
            viewholder.tv_prase.setText(infoData.get(position).like_number
                    + " 赞");
            viewholder.tv_visit.setText(infoData.get(position).visit_number
                    + " 阅读");
            viewholder.tv_time.setText(TimeUtil.getTimeString(infoData
                    .get(position).time * 1000));
            try {
                viewholder.iv_typeImage.setVisibility(View.VISIBLE);
                viewholder.iv_typeImage
                        .setText(infoData.get(position).tag.tag_name);
                int color_tag = infoData.get(position).tag.tag_id;
                viewholder.iv_typeImage.setTextColor(color_list
                        .get(color_tag % 7));
                viewholder.iv_typeImage.color = color_list.get(color_tag % 7);
            } catch (Exception e) {
                viewholder.iv_typeImage.setVisibility(View.GONE);
            }
            if (infoData.get(position).recommend == 1) {
                viewholder.btv_jianImage.setVisibility(View.VISIBLE);
                viewholder.btv_jianImage.setText("荐");
                viewholder.btv_jianImage.setColor(TagInfosActivity.this
                        .getResources().getColor(R.color.red_d30549));
                viewholder.btv_jianImage.setTextColor(TagInfosActivity.this
                        .getResources().getColor(R.color.red_d30549));
            } else {
                viewholder.btv_jianImage.setVisibility(View.GONE);
            }
            imageData = new ArrayList<String>();
            imageData.addAll(infoData.get(position).pic_urls);
            imageAdapter = new ImageGridAdapter(imageData);
            viewholder.gv_infoImage.setAdapter(imageAdapter);
            return convertView;
        }
    }

    public class ViewHolder {

        private ImageView icv_InfoImage;
        private TextView tv_infoNameOne, tv_infoNameTwo, tv_infoNameThree;
        private GridView gv_infoImage;
        private RelativeLayout rl_layoutOne;
        private LinearLayout ll_layoutTwo, ll_layoutThree;
        private TextView tv_commentCount;
        private TextView tv_time;
        private TextView tv_prase;
        private TextView tv_visit;
        private ImageView iv_infoImageOne, iv_infoImageThree;
        private BorderTextView iv_typeImage, btv_jianImage;
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
                convertView = LayoutInflater.from(TagInfosActivity.this)
                        .inflate(R.layout.item_info_grid, null, false);
                viewholder.icv_InfoImage = (ImageView) convertView
                        .findViewById(R.id.info_image);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            // viewholder.icv_InfoImage.setImageResource(R.drawable.loadding_icon);
            // ImageLoaderWrapper.getDefault().displayImage(
            // mImageList.get(position), viewholder.icv_InfoImage);
            ImageUtils.getInstance().loadImage(TagInfosActivity.this,
                    mImageList.get(position), R.drawable.loadding_icon,
                    viewholder.icv_InfoImage);
            return convertView;
        }
    }
}
