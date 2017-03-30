package com.goby.fishing.common.utils.helper.android.app.edit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import com.baidu.mobstat.StatService;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.ChannelListBean;
import com.example.controller.bean.ChannelListBean.ChannelItem;
import com.example.controller.bean.TagsListBean;
import com.goby.fishing.common.utils.helper.android.app.adapter.DragAdapter;
import com.goby.fishing.common.utils.helper.android.app.adapter.OtherAdapter;
import com.goby.fishing.common.utils.helper.android.app.view.DragGrid;
import com.goby.fishing.common.utils.helper.android.app.view.OtherGridView;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.module.information.NewInformationFragment;
import com.google.gson.Gson;

/**
 * Description：对tab进行添加删除排序操作 ScrollView嵌套两个GridView
 * <p>
 * Created by Mjj on 2016/11/18.
 */

public class ChannelActivity extends GestureDetectorActivity implements
        AdapterView.OnItemClickListener {

    /**
     * 用户栏目
     */
    private DragGrid userGridView; // GridView
    DragAdapter userAdapter; // 适配器
    private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    private ArrayList<ChannelItem> newUserChannelList = new ArrayList<ChannelItem>();
    /**
     * 其它栏目
     */
    private OtherGridView otherGridView; // GridView
    private OtherAdapter otherAdapter; // 适配器
    private ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>(); // 数据源
    private ArrayList<ChannelItem> newOtherChannelList = new ArrayList<ChannelItem>();
    private SharedPreferenceUtil sharedPreferenceUtil;
    /**
     * 是否在移动，由于是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;
    private ArrayList<String> tagNameList = new ArrayList<String>();
    private ScrollView sv_parent;
    private TextView tv_leftBack;
    private ChannelListBean userChannelListBean, otherChannelListBean;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ChannelActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Gson userGson = new Gson();
        userChannelListBean = new ChannelListBean();
        userChannelListBean = userGson.fromJson(
                sharedPreferenceUtil.getUserChannelJson(),
                ChannelListBean.class);
        userChannelList.addAll(userChannelListBean.data);
        if (TextUtils.isEmpty(sharedPreferenceUtil.getTags())) {
            // 第一次加载
            Log.e("=====", "1111111");
            showProgressDialog("正在加载数据中,请稍候...");
            getRemote();
        } else {
            Gson tagGson = new Gson();
            TagsListBean bean = tagGson.fromJson(
                    sharedPreferenceUtil.getTags(), TagsListBean.class);
            if (!TextUtils.isEmpty(sharedPreferenceUtil.getOtherChannelJson())) {
                Gson otherGson = new Gson();
                otherChannelListBean = new ChannelListBean();
                try {
                    otherChannelListBean = otherGson.fromJson(
                            sharedPreferenceUtil.getOtherChannelJson(),
                            ChannelListBean.class);
                    otherChannelList.addAll(otherChannelListBean.data);
                    Log.e("=====", "22222222");
                    // 更新緩存數據
                    tagNameList.clear();
                    for (int i = 0; i < userChannelList.size(); i++) {
                        tagNameList.add(userChannelList.get(i).name);
                    }
                    for (int j = 0; j < otherChannelList.size(); j++) {
                        tagNameList.add(otherChannelList.get(j).name);
                    }
                    for (int i = 0; i < bean.data.list.size(); i++) {
                        if (!tagNameList
                                .contains(bean.data.list.get(i).tag_name)) {
                            ChannelItem channelItem = new ChannelItem();
                            channelItem.id = bean.data.list.get(i).tag_id;
                            channelItem.name = bean.data.list.get(i).tag_name;
                            channelItem.orderId = otherChannelList.size();
                            channelItem.selected = 0;
                            newOtherChannelList.add(channelItem);
                        }
                    }
                    otherChannelList.addAll(newOtherChannelList);
                    otherChannelListBean = new ChannelListBean();
                    otherChannelListBean.data = otherChannelList;
                    sharedPreferenceUtil.setOtherChannelJson(otherGson
                            .toJson(otherChannelListBean));
                } catch (Exception e) {
                    // TODO: handle exception
                    getRemote();
                }
            } else {
                Log.e("=====", "33333333333");
                for (int i = 0; i < userChannelList.size(); i++) {
                    tagNameList.add(userChannelList.get(i).name);
                }
                for (int i = 0; i < bean.data.list.size(); i++) {
                    if (!tagNameList.contains(bean.data.list.get(i).tag_name)) {
                        ChannelItem channelItem = new ChannelItem();
                        channelItem.id = bean.data.list.get(i).tag_id;
                        channelItem.name = bean.data.list.get(i).tag_name;
                        channelItem.orderId = otherChannelList.size();
                        channelItem.selected = 0;
                        otherChannelList.add(channelItem);
                    }
                }
            }
            sv_parent.setVisibility(View.VISIBLE);
        }
        userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(otherAdapter);
        // 设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
    }

    public void getRemote() {
        UserBusinessController.getInstance().getInfoTags(null, getVersionCode(), "2", 1, 200, new com.example.controller.controller.Listener<TagsListBean>() {
            @Override
            public void onStart(Object... params) {

            }

            @Override
            public void onComplete(TagsListBean bean, Object... params) {

                    if (bean.data.list != null && bean.data.list.size() > 0) {
                        otherChannelList.clear();
                        Log.e("=====", "4444444444");
                        tagNameList.clear();
                        for (int i = 0; i < userChannelList.size(); i++) {
                            tagNameList.add(userChannelList.get(i).name);
                        }
                        for (int i = 0; i < bean.data.list.size(); i++) {
                            if (!tagNameList.contains(bean.data.list.get(i).tag_name)) {
                                ChannelItem channelItem = new ChannelItem();
                                channelItem.id = bean.data.list.get(i).tag_id;
                                channelItem.name = bean.data.list.get(i).tag_name;
                                channelItem.orderId = i;
                                channelItem.selected = 0;
                                otherChannelList.add(channelItem);
                            }
                        }
                        Gson gson = new Gson();
                        otherChannelListBean = new ChannelListBean();
                        otherChannelListBean.data = otherChannelList;
                        sharedPreferenceUtil.setOtherChannelJson(gson
                                .toJson(otherChannelList));
                        dismissProgressDialog();
                        sv_parent.setVisibility(View.VISIBLE);
                        otherAdapter.notifyDataSetChanged();
                        Gson tagGson = new Gson();
                        sharedPreferenceUtil.setTags(tagGson.toJson(bean));
                    }
            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(ChannelActivity.this, msg);
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        sv_parent = (ScrollView) findViewById(R.id.parent_scroll);
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
        tv_leftBack = (TextView) findViewById(R.id.left_back);
        tv_leftBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (userAdapter.isListChanged()) {
                    saveChannel();
                    NewInformationFragment.isSetChangel = true;
                }
                finish();
            }
        });
    }

    /**
     * GRIDVIEW对应的ITEM点击监听接口
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view,
                            final int position, long id) {
        // 如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                // position为 0 的不进行任何操作
                if (position != 0) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view
                                .findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = ((DragAdapter) parent
                                .getAdapter()).getItem(position);// 获取点击的频道内容
                        otherAdapter.setVisible(false);
                        // 添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    // 获取终点的坐标
                                    otherGridView.getChildAt(
                                            otherGridView.getLastVisiblePosition())
                                            .getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation,
                                            endLocation, channel, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                // 其它GridView
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view
                            .findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((OtherAdapter) parent.getAdapter())
                            .getItem(position);
                    userAdapter.setVisible(false);
                    // 添加到最后一个
                    newUserChannelList.add(channel);
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                // 获取终点的坐标
                                userGridView.getChildAt(
                                        userGridView.getLastVisiblePosition())
                                        .getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation,
                                        channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation,
                          int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        // 获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        // 得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView,
                initLocation);
        // 创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);// 动画时间
        // 动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet
                .setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        isMove = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        moveViewGroup.removeView(mMoveView);
                        // instanceof
                        // 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                        if (clickGridView instanceof DragGrid) {
                            otherAdapter.setVisible(true);
                            otherAdapter.notifyDataSetChanged();
                            userAdapter.remove();
                        } else {
                            userAdapter.setVisible(true);
                            userAdapter.notifyDataSetChanged();
                            otherAdapter.remove();
                        }
                        isMove = false;
                    }
                });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 退出时候保存选择后标签的设置
     */
    private void saveChannel() {
        for (int i = 0; i < newUserChannelList.size(); i++) {
            // 发送百度统计事件
            StatService.onEvent(ChannelActivity.this, "tag_"
                    + newUserChannelList.get(i).name, "tag_"
                    + newUserChannelList.get(i).name, 1);
        }
        Gson userGson = new Gson();
        userChannelListBean = new ChannelListBean();
        userChannelListBean.data = userChannelList;
        sharedPreferenceUtil.setUserChannelJson(userGson
                .toJson(userChannelListBean));

        Gson otherGson = new Gson();
        otherChannelListBean = new ChannelListBean();
        otherChannelListBean.data = otherChannelList;
        sharedPreferenceUtil.setOtherChannelJson(otherGson
                .toJson(otherChannelListBean));
    }

    @Override
    public void onBackPressed() {
        if (userAdapter.isListChanged()) {
            saveChannel();
            NewInformationFragment.isSetChangel = true;
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
