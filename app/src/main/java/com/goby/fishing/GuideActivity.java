package com.goby.fishing;

import java.util.ArrayList;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.baidu.android.common.logging.Log;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.goby.fishing.application.FishingApplication;
import com.example.controller.bean.LoginBean;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.login.LoginActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 欢迎页
 *
 * @author yhs
 */
public class GuideActivity extends AbsBaseActivity implements OnClickListener {

    // private ViewPager vp;
    // private ArrayList<View> viewList = new ArrayList<View>();
    // private View layout1;
    // private View layout2;
    // private View layout3;
    // private View layout4;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int mCurrent = 0;
    private String open_id = "";
    private int sex;
    private String srceenName;
    private String headerImageUrl;
    // private ImageView iv_one, iv_two, iv_three;
    private UMShareAPI mShareAPI = null;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, GuideActivity.class);
        activity.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mShareAPI = UMShareAPI.get(GuideActivity.this);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
    }

    private void initView() {
        // LayoutInflater inflater = LayoutInflater.from(GuideActivity.this);
        // layout1 = inflater.inflate(R.layout.layout_first_load_1, null);
        // layout2 = inflater.inflate(R.layout.layout_first_load_2, null);
        // layout3 = inflater.inflate(R.layout.layout_first_load_3, null);
        // layout4 = inflater.inflate(R.layout.layout_frist_load_4, null);
        // iv_one = (ImageView) layout1.findViewById(R.id.image_one);
        // iv_two = (ImageView) layout2.findViewById(R.id.image_two);
        // iv_three = (ImageView) layout3.findViewById(R.id.image_three);
        // viewList = new ArrayList<View>();
        // viewList.add(layout1);
        // viewList.add(layout2);
        // viewList.add(layout3);
        // viewList.add(layout4);
        // vp = (ViewPager) findViewById(R.id.vp_picture);
        // vp.setAdapter(new ViewPagerAdapter());
        // vp.clearAnimation();
        // vp.setCurrentItem(mCurrent);
        // vp.setOnPageChangeListener(new PageChangListener());
        // iv_one.setOnClickListener(this);
        // iv_two.setOnClickListener(this);
        // iv_three.setOnClickListener(this);
        try {
            findViewById(R.id.phone_login).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            LoginActivity.launch(GuideActivity.this, "login");
                        }
                    });
            findViewById(R.id.enter_btn).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            MainActivity.launch(GuideActivity.this, false);
                            finish();
                        }
                    });
            findViewById(R.id.qq_login).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            QQ_Login();
                        }
                    });
            findViewById(R.id.weixin_login).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            WEIXIN_Login();
                        }
                    });
        } catch (Exception e) {
        }
    }


    /**
     * 微信授权登录
     */
    public void WEIXIN_Login() {
        mShareAPI.getPlatformInfo(GuideActivity.this, SHARE_MEDIA.WEIXIN,
                new UMAuthListener() {

                    @Override
                    public void onCancel(SHARE_MEDIA arg0, int arg1) {
                        // TODO Auto-generated method stub
                        ToastHelper.showToast(GuideActivity.this, "授权取消");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int status,
                                           Map<String, String> info) {
                        // TODO Auto-generated method stub
                        ToastHelper.showToast(GuideActivity.this, "授权成功");
                        UserBusinessController.getInstance().getAutoLogin(null, getVersionCode(), "2", info.get("openid").toString(), 1, Integer.parseInt(info.get("gender").toString()), info.get("profile_image_url").toString(),
                                info.get("screen_name").toString(), sharedPreferenceUtil.getDeviceToken(), new com.example.controller.controller.Listener<LoginBean>() {
                                    @Override
                                    public void onStart(Object... params) {

                                    }

                                    @Override
                                    public void onComplete(LoginBean bean, Object... params) {

                                            try {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(GuideActivity.this, "登录成功");
                                                MainActivity.launch(GuideActivity.this, false);
                                                finish();
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                                finish();
                                            }

                                    }

                                    @Override
                                    public void onFail(String msg, Object... params) {
                                        ToastHelper.showToast(GuideActivity.this, msg);
                                    }
                                });
                    }

                    @Override
                    public void onError(SHARE_MEDIA arg0, int arg1,
                                        Throwable arg2) {
                        ToastHelper.showToast(GuideActivity.this, "授权失败");
                    }

                });
    }

    /**
     * QQ授权登录
     */
    public void QQ_Login() {
        UMShareAPI.get(GuideActivity.this).getPlatformInfo(GuideActivity.this,
                SHARE_MEDIA.QQ, new UMAuthListener() {

                    @Override
                    public void onCancel(SHARE_MEDIA arg0, int arg1) {
                        // TODO Auto-generated method stub
                        ToastHelper.showToast(GuideActivity.this, "授权取消");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int status,
                                           Map<String, String> info) {
                        ToastHelper.showToast(GuideActivity.this, "授权成功");
                        // TODO Auto-generated method stub
                        if (info.get("gender").toString().equals("男")) {
                            sex = 1;
                        } else if (info.get("gender").toString().equals("女")) {
                            sex = 2;
                        }
                        UserBusinessController.getInstance().getAutoLogin(null, getVersionCode(), "2", info.get("openid").toString(), 3, sex, info.get("profile_image_url").toString(),info.get("screen_name").toString(),
                                sharedPreferenceUtil.getDeviceToken(), new com.example.controller.controller.Listener<LoginBean>() {
                                    @Override
                                    public void onStart(Object... params) {

                                    }

                                    @Override
                                    public void onComplete(LoginBean bean, Object... params) {

                                            try {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(GuideActivity.this, "登录成功");
                                                MainActivity.launch(GuideActivity.this, false);
                                                finish();
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                                finish();
                                            }

                                    }

                                    @Override
                                    public void onFail(String msg, Object... params) {
                                        ToastHelper.showToast(GuideActivity.this, msg);
                                    }
                                });
                    }

                    @Override
                    public void onError(SHARE_MEDIA arg0, int arg1,
                                        Throwable arg2) {
                        // TODO Auto-generated method stub
                    }

                });

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            // case R.id.image_one:
            // mCurrent = 1;
            // vp.setCurrentItem(mCurrent);
            // break;
            // case R.id.image_two:
            // mCurrent = 2;
            // vp.setCurrentItem(mCurrent);
            // break;
            // case R.id.image_three:
            // mCurrent = 3;
            // vp.setCurrentItem(mCurrent);
            // break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
