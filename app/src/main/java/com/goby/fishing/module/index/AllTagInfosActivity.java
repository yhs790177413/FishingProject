package com.goby.fishing.module.index;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FishingInfoBean;
import com.example.controller.bean.TagsListBean;
import com.example.controller.bean.FishingInfoBean.Data.List;
import com.example.controller.bean.TagsListBean.Data.TagBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.BorderTextView;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.google.gson.Gson;
import com.umeng.common.message.Log;

public class AllTagInfosActivity extends AbsBaseActivity implements
        OnClickListener {

    private ListView mInfoList;

    private ImageGridAdapter imageAdapter;

    public static ArrayList<List> infoData = new ArrayList<List>();
    ;

    public static String active = "search";

    private ArrayList<String> imageData;

    private int page = 1;

    private int number = 20;

    public static boolean all_refresh = true;

    private View footerView;

    private View loadMore; // 加载更多的view

    private View loading; // 加载进度条

    private EditText et_searchEdit;

    private TextView tv_search;

    public static boolean isShowSearchResult = false;

    public static boolean isUploadRefresh = false;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private GridView gv_tags;

    private TagsAdapter mTagsAdapter;

    private InfoAdapter adapter;

    private ArrayList<TagBean> tagsList = new ArrayList<TagBean>();

    private LinearLayout ll_leftBack;

    private boolean searchFlag = false;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AllTagInfosActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_tag_infos);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getTags())) {
            Gson gson = new Gson();
            TagsListBean bean = gson.fromJson(sharedPreferenceUtil.getTags(),
                    TagsListBean.class);
            tagsList.addAll(bean.data.list);
            mTagsAdapter.notifyDataSetChanged();
        } else {
            UserBusinessController.getInstance().getInfoTags(null, getVersionCode(), "2", 1, 200, new com.example.controller.controller.Listener<TagsListBean>() {
                @Override
                public void onStart(Object... params) {
                    showProgressDialog("正在获取数据中,请稍候...");
                }

                @Override
                public void onComplete(TagsListBean bean, Object... params) {
                    dismissProgressDialog();

                        if (bean.data.list != null && bean.data.list.size() > 0) {
                            tagsList.clear();
                            tagsList.addAll(bean.data.list);
                            mTagsAdapter.notifyDataSetChanged();
                            Gson gson = new Gson();
                            sharedPreferenceUtil.setTags(gson.toJson(bean));
                        }

                }

                @Override
                public void onFail(String msg, Object... params) {
                    dismissProgressDialog();
                    ToastHelper.showToast(AllTagInfosActivity.this, msg);
                }
            });
        }
    }

    public void initView() {

        et_searchEdit = (EditText) findViewById(R.id.search_edit);
        tv_search = (TextView) findViewById(R.id.search_text);
        gv_tags = (GridView) findViewById(R.id.tags_grid);
        mTagsAdapter = new TagsAdapter();
        gv_tags.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_tags.setAdapter(mTagsAdapter);
        gv_tags.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                TagInfosActivity.launch(AllTagInfosActivity.this,
                        String.valueOf(tagsList.get(position).tag_id),
                        tagsList.get(position).tag_name, null);
            }
        });
        tv_search.setOnClickListener(this);
        mInfoList = (ListView) findViewById(R.id.info_list);
        adapter = new InfoAdapter();
        mInfoList.setAdapter(adapter);
        initFooter();
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
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

    private class TagsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tagsList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(AllTagInfosActivity.this)
                        .inflate(R.layout.item_tag_view, null, false);
                viewholder.btv_tagImage = (BorderTextView) convertView
                        .findViewById(R.id.tag_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.btv_tagImage.setVisibility(View.VISIBLE);
            viewholder.btv_tagImage.setText(tagsList.get(position).tag_name);
            int color_tag = tagsList.get(position).tag_id;
            viewholder.btv_tagImage.setColor(initColor().get(color_tag % 7));
            viewholder.btv_tagImage
                    .setTextColor(initColor().get(color_tag % 7));
            return convertView;
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
        public View getView(final int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(AllTagInfosActivity.this)
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
                viewholder.btv_typeImage = (BorderTextView) convertView
                        .findViewById(R.id.type_image);
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
//			viewholder.iv_infoImageOne
//					.setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    infoData.get(position).pic_urls.get(0),
                    viewholder.iv_infoImageOne);
//			viewholder.iv_infoImageThree
//					.setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    infoData.get(position).pic_urls.get(0),
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
                viewholder.btv_typeImage.setVisibility(View.VISIBLE);
                viewholder.btv_typeImage
                        .setText(infoData.get(position).tag.tag_name);
                int color_tag = infoData.get(position).tag.tag_id;
                Log.e("=====", color_tag % 7 + "");
                viewholder.btv_typeImage.setColor(initColor()
                        .get(color_tag % 7));
                viewholder.btv_typeImage.setTextColor(initColor().get(
                        color_tag % 7));
            } catch (Exception e) {
                viewholder.btv_typeImage.setVisibility(View.GONE);
            }
            viewholder.btv_typeImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TagInfosActivity.launch(AllTagInfosActivity.this,
                            String.valueOf(infoData.get(position).tag.tag_id),
                            infoData.get(position).tag.tag_name, null);
                }
            });
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
        private BorderTextView btv_typeImage, btv_tagImage;
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
                convertView = LayoutInflater.from(AllTagInfosActivity.this)
                        .inflate(R.layout.item_info_grid, null, false);
                viewholder.icv_InfoImage = (ImageView) convertView
                        .findViewById(R.id.info_image);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
//			viewholder.icv_InfoImage.setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    mImageList.get(position), viewholder.icv_InfoImage);
            return convertView;
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_layout:
                if (searchFlag) {
                    searchFlag = false;
                    gv_tags.setVisibility(View.VISIBLE);
                    mInfoList.setVisibility(View.GONE);
                    et_searchEdit.setText(null);
                } else {
                    finish();
                }
                break;
            case R.id.search_text:
                page = 1;
                if (TextUtils.isEmpty(et_searchEdit.getText().toString().trim())) {
                    ToastHelper.showToast(this, "请输入关键字再搜索");
                    return;
                }
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    manager.hideSoftInputFromWindow(getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                searchFlag = true;
                active = "search";
                infoData.clear();
                adapter.notifyDataSetChanged();
                UserBusinessController.getInstance().searchList(null, getVersionCode(), "2", page, number, et_searchEdit.getText().toString().trim(), 1, new com.example.controller.controller.Listener<FishingInfoBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在获取数据中,请稍候...");
                    }

                    @Override
                    public void onComplete(FishingInfoBean bean, Object... params) {
                        dismissProgressDialog();

                            gv_tags.setVisibility(View.GONE);
                            mInfoList.setVisibility(View.VISIBLE);
                            if (active.equals("update")) {
                                if (bean.data.list.size() < 20) {
                                    mInfoList.removeFooterView(footerView);
                                    mInfoList.setOnScrollListener(null);
                                } else {
                                    mInfoList.setOnScrollListener(new UpdateListener());
                                }
                                infoData.addAll(bean.data.list);
                                adapter.notifyDataSetChanged();
                            } else if (active.equals("search")) {
                                if (bean.data.list.size() < 20 && bean.data.list.size() > 0) {
                                    mInfoList.removeFooterView(footerView);
                                    mInfoList.setOnScrollListener(null);
                                } else if (bean.data.list == null
                                        || bean.data.list.size() == 0) {
                                    ToastHelper.showToast(AllTagInfosActivity.this,
                                            "搜索不到相应的结果");
                                } else {
                                    mInfoList.addFooterView(footerView);
                                    mInfoList.setOnScrollListener(new UpdateListener());
                                }
                                infoData.addAll(bean.data.list);
                                adapter.notifyDataSetChanged();
                            }

                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(AllTagInfosActivity.this, msg);
                    }
                });
                break;
            default:
                break;
        }
    }

    private class UpdateListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 当不滚动时
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                // 判断是否滚动到底部
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 加载更多
                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    active = "update";
                    page++;
                    UserBusinessController.getInstance().searchList(null, getVersionCode(), "2", page, number, et_searchEdit.getText().toString().trim(), 1, new com.example.controller.controller.Listener<FishingInfoBean>() {
                        @Override
                        public void onStart(Object... params) {
                        }

                        @Override
                        public void onComplete(FishingInfoBean bean, Object... params) {

                                gv_tags.setVisibility(View.GONE);
                                mInfoList.setVisibility(View.VISIBLE);
                                if (active.equals("update")) {
                                    if (bean.data.list.size() < 20) {
                                        mInfoList.removeFooterView(footerView);
                                        mInfoList.setOnScrollListener(null);
                                    } else {
                                        mInfoList.setOnScrollListener(new UpdateListener());
                                    }
                                    infoData.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                } else if (active.equals("search")) {
                                    if (bean.data.list.size() < 20 && bean.data.list.size() > 0) {
                                        mInfoList.removeFooterView(footerView);
                                        mInfoList.setOnScrollListener(null);
                                    } else if (bean.data.list == null
                                            || bean.data.list.size() == 0) {
                                        ToastHelper.showToast(AllTagInfosActivity.this,
                                                "搜索不到相应的结果");
                                    } else {
                                        mInfoList.addFooterView(footerView);
                                        mInfoList.setOnScrollListener(new UpdateListener());
                                    }
                                    infoData.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                }

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            ToastHelper.showToast(AllTagInfosActivity.this, msg);
                        }
                    });
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (searchFlag) {
                searchFlag = false;
                gv_tags.setVisibility(View.VISIBLE);
                mInfoList.setVisibility(View.GONE);
                et_searchEdit.setText(null);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}