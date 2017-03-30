package com.goby.fishing.module.information;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.fishing.R;
import com.example.controller.bean.FishingInfoBean;
import com.example.controller.bean.TagsListBean;
import com.example.controller.bean.FishingInfoBean.Data.List;
import com.example.controller.bean.TagsListBean.Data.TagBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.BorderTextView;
import com.goby.fishing.common.utils.helper.android.app.WebViewActivity;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.fishing.FishingDetailActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.index.ActiveDetailActivity;
import com.goby.fishing.module.index.AsyncWebActivity;
import com.goby.fishing.module.index.GameActivity;
import com.goby.fishing.module.index.TagInfosActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.google.gson.Gson;
import com.umeng.common.message.Log;

public class SearchActivity extends AbsBaseActivity implements OnClickListener {

    private GridView gv_allTag, gv_selectTag;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private TagsAdapter mTagsAdapter;

    private InfoAdapter adapter;

    private ImageGridAdapter imageAdapter;

    private ArrayList<TagBean> tagsList = new ArrayList<TagBean>();

    private ArrayList<TagBean> selectTagsList = new ArrayList<TagBean>();

    public static ArrayList<List> infoData = new ArrayList<List>();
    ;

    private ArrayList<String> imageData;

    private int page = 1, number = 20;

    private RelativeLayout rl_searchLayout;

    private TextView tv_rightBtn, tv_leftBack;

    private EditText et_search;

    private LinearLayout ll_tagLayout, ll_tagInfoLayout;

    private ListView mInfoList;

    private View footerView;

    private View loadMore; // 加载更多的view

    private View loading; // 加载进度条

    public static String active = "search";

    private String ids = "";

    public static void launch(Activity activity) {

        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_search);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        initData();
    }

    public void initView() {
        rl_searchLayout = (RelativeLayout) findViewById(R.id.search_layout);
        ll_tagLayout = (LinearLayout) findViewById(R.id.tag_layout);
        ll_tagInfoLayout = (LinearLayout) findViewById(R.id.tag_info_layout);
        tv_rightBtn = (TextView) findViewById(R.id.right_btn);
        tv_leftBack = (TextView) findViewById(R.id.left_back);
        et_search = (EditText) findViewById(R.id.search_edit);
        gv_allTag = (GridView) findViewById(R.id.tags_grid);
        mTagsAdapter = new TagsAdapter(tagsList);
        gv_allTag.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_allTag.setAdapter(mTagsAdapter);

        gv_selectTag = (GridView) findViewById(R.id.tags_select_grid);
        gv_selectTag.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_selectTag.setAdapter(mTagsAdapter);

        mInfoList = (ListView) findViewById(R.id.info_list);
        adapter = new InfoAdapter();
        mInfoList.setAdapter(adapter);
        initFooter();
        gv_allTag.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (selectTagsList.size() >= 5) {
                    if (selectTagsList.contains(tagsList.get(position))) {
                        // selectCount--;
                        selectTagsList.remove(tagsList.get(position));
                        // tagsList.get(position).isSelect = !tagsList
                        // .get(position).isSelect;
                        mTagsAdapter.notifyDataSetChanged();
                    } else {
                        ToastHelper.showNewToast(SearchActivity.this,
                                "最多选择5个标签");
                    }
                } else {
                    if (!selectTagsList.contains(tagsList.get(position))) {
                        selectTagsList.add(tagsList.get(position));
                        // selectCount++;
                    } else {
                        selectTagsList.remove(tagsList.get(position));
                        // selectCount--;
                    }
                    if (selectTagsList.size() == 0) {
                        rl_searchLayout.setVisibility(View.VISIBLE);
                        tv_rightBtn.setText("搜索");
                    } else {
                        rl_searchLayout.setVisibility(View.GONE);
                        tv_rightBtn.setText("搜索标签");
                    }
                    // tagsList.get(position).isSelect =
                    // !tagsList.get(position).isSelect;
                    mTagsAdapter.notifyDataSetChanged();
                }
            }
        });
        gv_selectTag.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                ll_tagInfoLayout.setVisibility(View.GONE);
                ll_tagLayout.setVisibility(View.VISIBLE);
                selectTagsList.remove(position);
                mTagsAdapter.mTagList = tagsList;
                mTagsAdapter.notifyDataSetChanged();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                ll_tagInfoLayout.setVisibility(View.GONE);
                if (s.length() > 0) {
                    ll_tagLayout.setVisibility(View.GONE);
                } else {
                    ll_tagLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mInfoList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if (position < infoData.size()) {
                    // FishingInfoDetailActivity.launch(SearchActivity.this,
                    // infoData.get(position).id, position,
                    // "searchActivity",
                    // infoData.get(position).pic_urls.get(0));

                    if (infoData.get(position).content_url.split("://")[1]
                            .split("/")[0].equals("info")) {
                        FishingInfoDetailActivity.launch(
                                SearchActivity.this,
                                Integer.parseInt(infoData.get(position).content_url
                                        .split("://")[1].split("/")[1]),
                                position, "fish",
                                infoData.get(position).pic_urls.get(0), 0);
                    } else if (infoData.get(position).content_url.split("://")[1]
                            .split("/")[0].equals("fishpoint")) {
                        FishingDetailActivity.launch(
                                SearchActivity.this,
                                Integer.parseInt(infoData.get(position).content_url
                                        .split("://")[1].split("/")[1]),
                                position, "info_fragmrnt", infoData
                                        .get(position).pic_urls.get(0));
                    } else if (infoData.get(position).content_url.split("://")[1]
                            .split("/")[0].equals("feed")) {
                        FooterprintDetailActivity.launch(
                                SearchActivity.this,
                                Integer.parseInt(infoData.get(position).content_url
                                        .split("://")[1].split("/")[1]),
                                infoData.get(position).pic_urls.get(0),
                                position, "info_fragment");
                    } else if (infoData.get(position).content_url.split("://")[1]
                            .split("/")[0].equals("activity")) {
                        ActiveDetailActivity
                                .launch(SearchActivity.this,
                                        infoData.get(position).content_url
                                                .split("://")[1].split("/")[1]);
                    } else if (infoData.get(position).content_url.split("://")[1]
                            .equals("fishfun")) {
                        if (TextUtils.isEmpty(sharedPreferenceUtil
                                .getUserToken())) {
                            ToastHelper.showToast(SearchActivity.this,
                                    "您还未登录,请先登录");
                            LoginActivity.launch(SearchActivity.this,
                                    "indexFragment");
                        } else {
                            GameActivity.launch(SearchActivity.this);
                        }
                    } else if (infoData.get(position).content_url
                            .startsWith("http")) {
                        if (infoData.get(position).content_url
                                .startsWith("https://wap.koudaitong.com")) {
                            AsyncWebActivity.launch(SearchActivity.this,
                                    infoData.get(position).content_url,
                                    infoData.get(position).title);
                        } else {
                            WebViewActivity.launch(SearchActivity.this,
                                    infoData.get(position).content_url,
                                    infoData.get(position).title);
                        }
                    }
                }
            }
        });
        tv_leftBack.setOnClickListener(this);
        tv_rightBtn.setOnClickListener(this);
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

    public void initData() {
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getTags())) {
            Gson gson = new Gson();
            TagsListBean bean = gson.fromJson(sharedPreferenceUtil.getTags(),
                    TagsListBean.class);
            tagsList.addAll(bean.data.list);
            mTagsAdapter.mTagList = tagsList;
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
                        mTagsAdapter.mTagList = tagsList;
                        mTagsAdapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        sharedPreferenceUtil.setTags(gson.toJson(bean));
                    }

                }

                @Override
                public void onFail(String msg, Object... params) {
                    dismissProgressDialog();
                    ToastHelper.showToast(SearchActivity.this, msg);
                }
            });
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
                    if (selectTagsList != null && selectTagsList.size() > 0) {
                        UserBusinessController.getInstance().getTagInfosJson(null, getVersionCode(), "2", page, number, null, ids, new com.example.controller.controller.Listener<FishingInfoBean>() {
                            @Override
                            public void onStart(Object... params) {

                            }

                            @Override
                            public void onComplete(FishingInfoBean bean, Object... params) {
                                    ll_tagLayout.setVisibility(View.GONE);
                                    ll_tagInfoLayout.setVisibility(View.VISIBLE);
                                    if (selectTagsList != null && selectTagsList.size() > 0) {
                                        gv_selectTag.setVisibility(View.VISIBLE);
                                        mTagsAdapter.mTagList = selectTagsList;
                                        mTagsAdapter.notifyDataSetChanged();
                                    } else {
                                        gv_selectTag.setVisibility(View.GONE);
                                    }
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
                                            ToastHelper.showToast(SearchActivity.this, "搜索不到相应的结果");
                                        } else {
                                            if (mInfoList.getFooterViewsCount() <= 0) {
                                                mInfoList.addFooterView(footerView);
                                            }
                                            mInfoList.setOnScrollListener(new UpdateListener());
                                        }
                                        infoData.addAll(bean.data.list);
                                        adapter.notifyDataSetChanged();
                                    }
                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                ToastHelper.showToast(SearchActivity.this, msg);
                            }
                        });
                    } else {
                        UserBusinessController.getInstance().SearchInfoListJson(null, getVersionCode(), "2", page, number, et_search.getText().toString().trim(), 1, new com.example.controller.controller.Listener<FishingInfoBean>() {
                            @Override
                            public void onStart(Object... params) {

                            }

                            @Override
                            public void onComplete(FishingInfoBean bean, Object... params) {

                                    ll_tagLayout.setVisibility(View.GONE);
                                    ll_tagInfoLayout.setVisibility(View.VISIBLE);
                                    if (selectTagsList != null && selectTagsList.size() > 0) {
                                        gv_selectTag.setVisibility(View.VISIBLE);
                                        mTagsAdapter.mTagList = selectTagsList;
                                        mTagsAdapter.notifyDataSetChanged();
                                    } else {
                                        gv_selectTag.setVisibility(View.GONE);
                                    }
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
                                            ToastHelper.showToast(SearchActivity.this, "搜索不到相应的结果");
                                        } else {
                                            if (mInfoList.getFooterViewsCount() <= 0) {
                                                mInfoList.addFooterView(footerView);
                                            }
                                            mInfoList.setOnScrollListener(new UpdateListener());
                                        }
                                        infoData.addAll(bean.data.list);
                                        adapter.notifyDataSetChanged();
                                    }

                            }

                            @Override
                            public void onFail(String msg, Object... params) {
                                ToastHelper.showToast(SearchActivity.this, msg);
                            }
                        });
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {

        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.right_btn:
                active = "search";
                infoData.clear();
                page = 1;
                if (mInfoList.getFooterViewsCount() > 0) {
                    mInfoList.removeFooterView(footerView);
                }
                mInfoList.setOnScrollListener(null);
                if (selectTagsList.size() == 0) {
                    // 搜索关键字
                    if (TextUtils.isEmpty(et_search.getText().toString().trim())) {
                        ToastHelper.showToast(this, "请输入关键字再搜索");
                        return;
                    }
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        manager.hideSoftInputFromWindow(getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    adapter.notifyDataSetChanged();
                    UserBusinessController.getInstance().SearchInfoListJson(null, getVersionCode(), "2", page, number, et_search.getText().toString().trim(), 1, new com.example.controller.controller.Listener<FishingInfoBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在获取数据中,请稍候...");
                        }

                        @Override
                        public void onComplete(FishingInfoBean bean, Object... params) {
                            dismissProgressDialog();
                                ll_tagLayout.setVisibility(View.GONE);
                                ll_tagInfoLayout.setVisibility(View.VISIBLE);
                                if (selectTagsList != null && selectTagsList.size() > 0) {
                                    gv_selectTag.setVisibility(View.VISIBLE);
                                    mTagsAdapter.mTagList = selectTagsList;
                                    mTagsAdapter.notifyDataSetChanged();
                                } else {
                                    gv_selectTag.setVisibility(View.GONE);
                                }
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
                                        ToastHelper.showToast(SearchActivity.this, "搜索不到相应的结果");
                                    } else {
                                        if (mInfoList.getFooterViewsCount() <= 0) {
                                            mInfoList.addFooterView(footerView);
                                        }
                                        mInfoList.setOnScrollListener(new UpdateListener());
                                    }
                                    infoData.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                }
                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(SearchActivity.this, msg);
                        }
                    });
                } else {
                    // 搜索标签
                    ids = "";
                    for (int i = 0; i < selectTagsList.size(); i++) {
                        ids = ids + selectTagsList.get(i).tag_id + ",";
                    }
                    // 获取接口数据
                    UserBusinessController.getInstance().getTagInfosJson(null, getVersionCode(), "2", page, number, null, ids.substring(0, ids.length() - 1), new com.example.controller.controller.Listener<FishingInfoBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("获取数据中,请稍候...");
                        }

                        @Override
                        public void onComplete(FishingInfoBean bean, Object... params) {
                            dismissProgressDialog();

                                ll_tagLayout.setVisibility(View.GONE);
                                ll_tagInfoLayout.setVisibility(View.VISIBLE);
                                if (selectTagsList != null && selectTagsList.size() > 0) {
                                    gv_selectTag.setVisibility(View.VISIBLE);
                                    mTagsAdapter.mTagList = selectTagsList;
                                    mTagsAdapter.notifyDataSetChanged();
                                } else {
                                    gv_selectTag.setVisibility(View.GONE);
                                }
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
                                        ToastHelper.showToast(SearchActivity.this, "搜索不到相应的结果");
                                    } else {
                                        if (mInfoList.getFooterViewsCount() <= 0) {
                                            mInfoList.addFooterView(footerView);
                                        }
                                        mInfoList.setOnScrollListener(new UpdateListener());
                                    }
                                    infoData.addAll(bean.data.list);
                                    adapter.notifyDataSetChanged();
                                }

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(SearchActivity.this, msg);
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    private class TagsAdapter extends BaseAdapter {

        private ArrayList<TagBean> mTagList = new ArrayList<TagBean>();

        public TagsAdapter(ArrayList<TagBean> tagList) {
            mTagList = tagList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mTagList.size();
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
                convertView = LayoutInflater.from(SearchActivity.this).inflate(
                        R.layout.item_tag_view_two, null, false);
                viewholder.tv_tagText = (TextView) convertView
                        .findViewById(R.id.text_item);
                viewholder.iv_closeImage = (ImageView) convertView
                        .findViewById(R.id.close_image);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.tv_tagText.setText(mTagList.get(position).tag_name);
            if (selectTagsList.contains(mTagList.get(position))) {
                viewholder.tv_tagText.setTextColor(getResources().getColor(
                        R.color.red_d30549));
                viewholder.iv_closeImage.setVisibility(View.VISIBLE);
            } else {
                viewholder.tv_tagText.setTextColor(getResources().getColor(
                        R.color.black));
                viewholder.iv_closeImage.setVisibility(View.GONE);
            }
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
                convertView = LayoutInflater.from(SearchActivity.this).inflate(
                        R.layout.item_info_fragment, null, false);
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
            // viewholder.iv_infoImageOne
            // .setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    infoData.get(position).pic_urls.get(0),
                    viewholder.iv_infoImageOne);
            // viewholder.iv_infoImageThree
            // .setImageResource(R.drawable.loadding_icon);
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
                    TagInfosActivity.launch(SearchActivity.this,
                            String.valueOf(infoData.get(position).tag.tag_id),
                            infoData.get(position).tag.tag_name, null);
                }
            });
            if (infoData.get(position).recommend == 1) {
                viewholder.btv_jianImage.setVisibility(View.VISIBLE);
                viewholder.btv_jianImage.setText("荐");
                viewholder.btv_jianImage.setColor(SearchActivity.this
                        .getResources().getColor(R.color.red_d30549));
                viewholder.btv_jianImage.setTextColor(SearchActivity.this
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
        private BorderTextView btv_typeImage, btv_jianImage;
        private TextView tv_tagText;
        // private FrameLayout fl_tag;
        private ImageView iv_closeImage;
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
                convertView = LayoutInflater.from(SearchActivity.this).inflate(
                        R.layout.item_info_grid, null, false);
                viewholder.icv_InfoImage = (ImageView) convertView
                        .findViewById(R.id.info_image);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            ImageUtils.getInstance().loadImage(SearchActivity.this,
                    mImageList.get(position), R.drawable.loadding_icon,
                    viewholder.icv_InfoImage);
            // viewholder.icv_InfoImage.setImageResource(R.drawable.loadding_icon);
            // ImageLoaderWrapper.getDefault().displayImage(
            // mImageList.get(position), viewholder.icv_InfoImage);
            return convertView;
        }
    }

}
