package com.goby.fishing.module.me;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.controller.Constant;
import com.example.controller.bean.RecommendsListBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.MyInfoBean;
import com.goby.fishing.common.photochoose.ImageBrowseActivity;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.BaseFragment;
import com.goby.fishing.module.index.AsyncWebActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.goby.fishing.module.other.OtherInfoActivity;
import com.umeng.analytics.MobclickAgent;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_userName;

    private Button btn_modifyInfo;

    private TextView tv_fansCount;

    private TextView tv_attentionCount;

    private TextView tv_praiseCount;

    private TextView tv_integral;

    private ImageView icv_userHeader;

    private ImageView iv_sex;

    private RelativeLayout rl_myFooterPrint;

    private RelativeLayout rl_myCollection;

    private RelativeLayout rl_myAttention;

    private RelativeLayout rl_myMessage;

    private RelativeLayout rl_setting;

    private RelativeLayout rl_share;

    private RelativeLayout rl_feekback;

    private RelativeLayout rl_aboutUs;

    private ScrollView ll_parent;

    private LinearLayout ll_error;

    private LinearLayout ll_follow;

    private Button btn_reload;

    private Button btn_login;

    private ShareDialogUtils dialog_share;

    private UIHandler uiHandler;

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static boolean is_refresh = false;

    public static boolean unLogin = false;

    private String mHeaderPic;

    private String mUserName;

    private int mSex;

    private String mBirthday;

    private String mCityName;

    private ImageView iv_redPoint;

    private ImageView iv_draftsRedPoint;

    private RelativeLayout rl_drafts;

    private RelativeLayout rl_myOrder;

    //private GridView gv_typeGrid;

    private boolean isShowLoading = true;

    private PullRefreshLayout mPullRefreshLayout;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);

        uiHandler = new UIHandler();
        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity());
        initView(view);
        if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
            ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
            LoginActivity.launch(getActivity(), "meFragment");
        } else {
            initData();
        }
        return view;
    }

    public void initView(View view) {

        mPullRefreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fish_refresh_layout);// 下拉刷新，第三方控件
        iv_redPoint = (ImageView) view.findViewById(R.id.me_red_point);
        iv_draftsRedPoint = (ImageView) view
                .findViewById(R.id.drafts_red_point);
        ll_parent = (ScrollView) view.findViewById(R.id.parent_layout);
        ll_error = (LinearLayout) view.findViewById(R.id.error_layout);
        ll_follow = (LinearLayout) view.findViewById(R.id.follow_layout);
        icv_userHeader = (ImageView) view.findViewById(R.id.user_header);
        iv_sex = (ImageView) view.findViewById(R.id.sex_image);
        tv_userName = (TextView) view.findViewById(R.id.user_name);
        tv_fansCount = (TextView) view.findViewById(R.id.fans_count);
        tv_attentionCount = (TextView) view.findViewById(R.id.attention_count);
        tv_praiseCount = (TextView) view.findViewById(R.id.praise_count);
        tv_integral = (TextView) view.findViewById(R.id.integral_count);
        rl_myFooterPrint = (RelativeLayout) view
                .findViewById(R.id.my_footer_print_layout);
        rl_myCollection = (RelativeLayout) view
                .findViewById(R.id.my_collection_layout);
        rl_myAttention = (RelativeLayout) view
                .findViewById(R.id.my_attention_layout);
        rl_myMessage = (RelativeLayout) view
                .findViewById(R.id.my_message_layout);
        rl_setting = (RelativeLayout) view.findViewById(R.id.setting_layout);
        rl_share = (RelativeLayout) view.findViewById(R.id.share_layout);
        rl_feekback = (RelativeLayout) view.findViewById(R.id.feekback_layout);
        rl_aboutUs = (RelativeLayout) view.findViewById(R.id.about_us_layout);
        rl_drafts = (RelativeLayout) view.findViewById(R.id.drafts_layout);
        rl_myOrder = (RelativeLayout) view.findViewById(R.id.my_order_layout);

        btn_modifyInfo = (Button) view.findViewById(R.id.modify_info);
        btn_reload = (Button) view.findViewById(R.id.reload_btn);
        btn_login = (Button) view.findViewById(R.id.login_btn);

//		gv_typeGrid = (GridView) view.findViewById(R.id.type_grid);
//		gv_typeGrid.setClickable(false);
//		gv_typeGrid.setPressed(false);
//		gv_typeGrid.setEnabled(false);
//		gv_typeGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));

        rl_myFooterPrint.setOnClickListener(this);
        rl_myCollection.setOnClickListener(this);
        rl_myAttention.setOnClickListener(this);
        rl_myMessage.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        rl_feekback.setOnClickListener(this);
        rl_aboutUs.setOnClickListener(this);
        rl_drafts.setOnClickListener(this);
        rl_myOrder.setOnClickListener(this);

        btn_modifyInfo.setOnClickListener(this);
        btn_reload.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        icv_userHeader.setOnClickListener(this);
        mPullRefreshLayout
                .setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        isShowLoading = false;
                        // 刷新
                        initData();

                    }
                });
    }

    public void initData() {
        UserBusinessController.getInstance().getUserInfo(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", new com.example.controller.controller.Listener<MyInfoBean>() {
            @Override
            public void onStart(Object... params) {
                if (isShowLoading) {
                    isShowLoading = false;
                    showProgressDialog("正在获取数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(MyInfoBean bean, Object... params) {
                dismissProgressDialog();
                mPullRefreshLayout.setRefreshing(false);
                ll_parent.setVisibility(View.VISIBLE);
                btn_modifyInfo.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.GONE);
                sharedPreferenceUtil.setUserID(bean.data.user_id);
                mHeaderPic = bean.data.head_pic;
                mUserName = bean.data.user_name;
                sharedPreferenceUtil.setUserName(mUserName);
                sharedPreferenceUtil.setUserHeadUrl(mHeaderPic);
                sharedPreferenceUtil.setUserPhone(bean.data.mobile);
                sharedPreferenceUtil.setUserSex(bean.data.sex);
                mSex = bean.data.sex;
                mBirthday = bean.data.birthday;
                mCityName = bean.data.city_name;
                if (mHeaderPic.startsWith("http://")) {
                    ImageLoaderWrapper.getDefault().displayImage(mHeaderPic,
                            icv_userHeader);

                } else {
                    ImageLoaderWrapper.getDefault().displayImage(
                            Constant.HOST_URL + mHeaderPic, icv_userHeader);
                }

                tv_userName.setText(bean.data.user_name);
                iv_sex.setVisibility(View.VISIBLE);
                if (bean.data.sex == 1) {
                    iv_sex.setBackgroundResource(R.drawable.man_icon);
                } else {
                    iv_sex.setBackgroundResource(R.drawable.woman_icon);
                }
                ll_follow.setVisibility(View.VISIBLE);
                tv_fansCount.setText("粉丝 " + bean.data.follow);
                tv_attentionCount.setText("关注 " + bean.data.follower);
                tv_praiseCount.setText("人气 " + bean.data.popular);
                tv_integral.setText("鱼票 " + bean.data.integral);
                sharedPreferenceUtil.setMember(bean.data.member);
            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(getActivity(), msg);
                mPullRefreshLayout.setRefreshing(false);
                ll_error.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_info:
                ModifyActivity.launch(getActivity(), mHeaderPic, mUserName, mSex,
                        mBirthday, mCityName);
                break;
            case R.id.my_footer_print_layout:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "meFragment");
                } else {
                    MyFooterprintActivity.launch(getActivity());
                }
                break;
            case R.id.my_collection_layout:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "meFragment");
                } else {
                    MyCollectionActivity.launch(getActivity());
                }
                break;
            case R.id.my_attention_layout:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "meFragment");
                } else {
                    MyAttentionActivity.launch(getActivity());
                }
                break;
            case R.id.my_message_layout:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "meFragment");
                } else {
                    MyMessageActivty.launch(getActivity());
                }

                break;
            case R.id.setting_layout:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "meFragment");
                } else {
                    SettingActivity.launch(getActivity());
                }

                break;
            case R.id.share_layout:
                // 弹出分享
                dialog_share = new ShareDialogUtils(getActivity(), R.style.dialog,
                        uiHandler, true);
                dialog_share.setCanceledOnTouchOutside(true);
                dialog_share.show();
                break;
            case R.id.feekback_layout:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "meFragment");
                } else {
                    FeekbackActivity.launch(getActivity());
                }
                break;
            case R.id.about_us_layout:
                AboutUsActivity.launch(getActivity());
                break;
            case R.id.reload_btn:
                ll_error.setVisibility(View.GONE);
                initData();
                break;
            case R.id.login_btn:
                LoginActivity.launch(getActivity(), "meFragment");
                break;
            case R.id.drafts_layout:
                //DraftsActivity.launch(getActivity());
                break;
            case R.id.user_header:
                if (!TextUtils.isEmpty(mHeaderPic)) {
                    ArrayList<String> imgs = new ArrayList<String>();
                    if (mHeaderPic.startsWith("http://")) {
                        imgs.add(mHeaderPic);
                    } else {
                        imgs.add(Constant.HOST_URL + mHeaderPic);
                    }
                    ImageBrowseActivity.launch(getActivity(), imgs, 0, "", "", "", "", "", "", "", false);
                }
                break;
            case R.id.my_order_layout:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    ToastHelper.showToast(getActivity(), "您还未登录,请先登录");
                    LoginActivity.launch(getActivity(), "meFragment");
                } else {
                    AsyncWebActivity.launch(getActivity(),
                            "https://wap.koudaitong.com/v2/usercenter/1azly7epr",
                            "我的订单");
                }
                break;
            default:
                break;
        }
    }

    public class UIHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            dialog_share.initPlatform();
            String mTitle = "去哪钓鱼,很好玩，你也一起来吧！";
            String shareUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.goby.fishing";
            switch (msg.what) {
                case 0:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl, 0, shareUrl);
                    break;
                case 1:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl, 1, shareUrl);
                    break;
                case 2:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl, 2, shareUrl);
                    break;
                case 3:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl, 3, shareUrl);
                    break;
                case 4:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl, 4, shareUrl);
                    break;
            }

        }
    }

    /**
     * 分享平台的选择
     *
     * @param position
     */
    public void selectSharePaltform(String content, int position,
                                    String shareUrl) {
        dialog_share.startShare("去哪钓鱼,很好玩，你也一起来吧！", content, null, position, null, shareUrl);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("MeFragment"); // 友盟统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。)
        MobclickAgent.onResume(getActivity()); // 友盟统计时长
        if (is_refresh) {
            is_refresh = false;
            if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                btn_modifyInfo.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
                iv_sex.setVisibility(View.GONE);
                tv_userName.setText("未知");
                ll_follow.setVisibility(View.GONE);
                ImageLoaderWrapper.getDefault().displayImage(null,
                        icv_userHeader);
                //gv_typeGrid.setVisibility(View.GONE);
            } else {
                isShowLoading = true;
                initData();
            }
        }
        // 我的消息红点
        if (sharedPreferenceUtil.getRedPointIsVisible()) {
            iv_redPoint.setVisibility(View.VISIBLE);
        } else {
            iv_redPoint.setVisibility(View.GONE);
        }
        // 草稿箱红点
        if (sharedPreferenceUtil.getDraftsRedPointIsVisible()) {
            iv_draftsRedPoint.setVisibility(View.VISIBLE);
        } else {
            iv_draftsRedPoint.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("MeFragment"); // 友盟统计（仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息。
        MobclickAgent.onPause(getActivity());
    }
}
