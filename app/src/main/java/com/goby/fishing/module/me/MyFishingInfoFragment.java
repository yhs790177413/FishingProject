package com.goby.fishing.module.me;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.controller.bean.MyInfoBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.FishingInfoBean;
import com.example.controller.bean.FishingInfoBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.app.BorderTextView;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.information.FishingInfoDetailActivity;
import com.umeng.common.message.Log;

public class MyFishingInfoFragment extends BaseFragment implements
        View.OnClickListener {

    private ListView mInfoList;

    private InfoAdapter adapter;

    private ImageGridAdapter imageAdapter;

    private ArrayList<List> infoData = new ArrayList<List>();

    private ArrayList<String> imageData;

    private LayoutInflater mInflater;

    private int page = 1;

    private int number = 20;

    private String active = "init";

    public static boolean is_refresh = false;

    private boolean is_firstLoading = true;

    private View footerView;
    private View loadMore; // 加载更多的view
    private View loading; // 加载进度条

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static MyFishingInfoFragment newInstance() {
        return new MyFishingInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info_item, null);

        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initFooter();
        initView(view);
        if (is_firstLoading) {
            is_firstLoading = false;
            initData();
        }

        return view;
    }

    /**
     * 初始化footer
     */
    private void initFooter() {
        footerView = LayoutInflater.from(getActivity()).inflate(
                R.layout.footer_view, null);
        loadMore = footerView.findViewById(R.id.load_more);
        loading = footerView.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
    }

    public void initView(View view) {

        mInflater = LayoutInflater.from(getActivity());

        mInfoList = (ListView) view.findViewById(R.id.info_list);
        adapter = new InfoAdapter();
        // mListView = mInfoList.getRefreshableView();
        mInfoList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mInfoList.setDividerHeight(1);
        mInfoList.setAdapter(adapter);
        mInfoList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                FishingInfoDetailActivity.launch(getActivity(),
                        infoData.get(position).id, position, "collection",
                        infoData.get(position).pic_urls.get(0));
            }
        });
    }

    public void initData() {
        UserBusinessController.getInstance().getFavoriteInfos(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", page, number, -1, new com.example.controller.controller.Listener<FishingInfoBean>() {
            @Override
            public void onStart(Object... params) {
                if (active.equals("init")) {
                    showProgressDialog("正在获取数据中,请稍候...");
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
                ToastHelper.showToast(getActivity(), msg);
            }
        });

    }

    private class UpdateListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            // 当不滚动时
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                Glide.with(getActivity()).resumeRequests();
                // 判断是否滚动到底部
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 加载更多
                    loadMore.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    active = "update";
                    page++;
                    initData();
                }
            } else {
                Glide.with(getActivity()).pauseRequests();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // 设置当前屏幕显示的起始index和结束index
        }
    }

    @Override
    public void onClick(View view) {

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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_info_fragment,
                        null, false);
                viewholder.tv_infoNameOne = (TextView) convertView
                        .findViewById(R.id.info_name_one);
                viewholder.tv_infoNameTwo = (TextView) convertView
                        .findViewById(R.id.info_name_two);
                viewholder.tv_infoNameThree = (TextView) convertView
                        .findViewById(R.id.info_name_three);
                viewholder.iv_infoImageOne = (ImageView) convertView.findViewById(R.id.info_image_one);
                viewholder.iv_infoImageThree = (ImageView) convertView.findViewById(R.id.info_image_three);
                viewholder.rl_layoutOne = (RelativeLayout) convertView.findViewById(R.id.layout_one);
                viewholder.ll_layoutTwo = (LinearLayout) convertView.findViewById(R.id.layout_two);
                viewholder.ll_layoutThree = (LinearLayout) convertView.findViewById(R.id.layout_three);
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
            viewholder.iv_infoImageOne
                    .setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    infoData.get(position).pic_urls.get(0),
                    viewholder.iv_infoImageOne);
            viewholder.iv_infoImageThree
                    .setImageResource(R.drawable.loadding_icon);
            ImageLoaderWrapper.getDefault().displayImage(
                    infoData.get(position).pic_urls.get(0),
                    viewholder.iv_infoImageThree);
            viewholder.tv_infoNameOne.setText(infoData.get(position).title);
            viewholder.tv_infoNameTwo.setText(infoData.get(position).title);
            viewholder.tv_infoNameThree.setText(infoData.get(position).title);
            viewholder.tv_commentCount
                    .setText(infoData.get(position).comment_number + " 评论");
            viewholder.tv_prase.setText(infoData.get(position).like_number
                    + "赞");
            try {
                viewholder.iv_typeImage.setVisibility(View.VISIBLE);
                viewholder.iv_typeImage
                        .setText(infoData.get(position).tag.tag_name);
                int color_tag = infoData.get(position).tag.tag_id;
                Log.e("=====", color_tag % 7 + "");
                viewholder.iv_typeImage.setColor(initColor().get(color_tag % 7));
                viewholder.iv_typeImage.setTextColor(initColor().get(
                        color_tag % 7));
            } catch (Exception e) {
                viewholder.iv_typeImage.setVisibility(View.GONE);
            }
            viewholder.tv_time.setText(TimeUtil.getTimeString(infoData
                    .get(position).time * 1000));
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
        private BorderTextView iv_typeImage;
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
                convertView = mInflater.inflate(R.layout.item_info_grid, null,
                        false);
                viewholder.icv_InfoImage = (ImageView) convertView
                        .findViewById(R.id.info_image);

                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderWrapper.getDefault().displayImage(
                    mImageList.get(position), viewholder.icv_InfoImage);
            return convertView;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (is_refresh) {
            is_refresh = false;
            infoData.clear();
            initData();

        }
    }

}