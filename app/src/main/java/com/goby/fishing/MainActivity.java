package com.goby.fishing;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.bumptech.glide.Glide;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.goby.fishing.application.FishingApplication;
import com.example.controller.bean.MyInfoBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.module.fishing.AddFooterprintActivity;
import com.goby.fishing.module.fishing.FishingFragment;
import com.goby.fishing.module.footerprint.FeedcircleFragment;
import com.goby.fishing.module.index.IndexFragment;
import com.goby.fishing.module.information.NewInformationFragment;
import com.goby.fishing.module.login.LoginActivity;
import com.goby.fishing.module.me.MeFragment;
import com.meelive.ingkee.sdk.plugin.IInkeCallback;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.meelive.ingkee.sdk.plugin.entity.ShareInfo;
import com.meelive.ingkee.sdk.plugin.entity.UserInfo;
import com.umeng.socialize.UMShareAPI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import static android.content.ContentValues.TAG;

/**
 * 主界面
 */
@SuppressLint({"HandlerLeak", "NewApi"})
public class MainActivity extends FragmentActivity implements OnClickListener {

    ViewFlipper mViewFlipper;

    private long mExitTime;

    private ImageView iv_redPoint, iv_mainAdd;

    private SharedPreferenceUtil sharedPreferenceUtil;

    public static View mIndexLayout, mInfornationLayout, mFooterPrintLayout,
            mFishingLayout, mMeLayout;

    public static UiHandler uiHandler;

    // APP_ID 替换为你申请的APPID
    private static final String APP_ID = "1000510001";

    public static enum MenuType {
        Index, Information, FooterPrint, Fishing, Me
    }

    private MenuType mCurrentMenuType;

    private IndexFragment mIndexFragment;
    private NewInformationFragment mInformationFragment;
    private FeedcircleFragment mFooterPrintFragment;
    private FishingFragment mFishingFragment;
    private MeFragment mMeFragment;

    public static void launch(Activity activity, boolean addFooter) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("addFooter", addFooter);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, String startFragment) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("startFragment", startFragment);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // //透明状态栏
        // if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
        // //透明状态栏
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        InKeSdkPluginAPI.register(inkeCallback, APP_ID, 0, getResources().getColor(R.color.blue_35b2e1));
        GetCleanTask getCleanTask = new GetCleanTask();
        getCleanTask.execute();
        // 一定要在主線程執行
        Glide.get(getApplicationContext()).clearMemory();
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        uiHandler = new UiHandler();
        setUi();
        try {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("startFragment"))
                    && getIntent().getStringExtra("startFragment").equals(
                    "meFragment")) {
                mMeLayout.performClick();
            } else {
                if (getIntent().getBooleanExtra("addFooter", false)) {
                    mFooterPrintLayout.performClick();
                } else {
                    mInfornationLayout.performClick();
                }
            }
        } catch (Exception e) {
            if (getIntent().getBooleanExtra("addFooter", false)) {
                mFooterPrintLayout.performClick();
            } else {
                mInfornationLayout.performClick();
            }
        }
        if (TextUtils.isEmpty(sharedPreferenceUtil.getUserHeadUrl())) {
            GetMyInfoTask getMyInfoTask = new GetMyInfoTask();
            getMyInfoTask.execute();
        }
        BDAutoUpdateSDK.cpUpdateCheck(getBaseContext(),
                new MyCPCheckUpdateCallback());
    }

    // IInkeCallback 接口的实现，用于SDK通知宿主需要执行登录、支付、分享操作
    IInkeCallback inkeCallback = new IInkeCallback() {
        @Override
        public void loginTrigger() {
            UserInfo userInfo = new UserInfo();
            userInfo.nickname = sharedPreferenceUtil.getUserName();
            userInfo.sex = sharedPreferenceUtil.getUserSex();
            userInfo.uId = sharedPreferenceUtil.getUserID();
            userInfo.portrait = sharedPreferenceUtil.getUserHeadUrl();
            InKeSdkPluginAPI.login(userInfo);
        }

        @Override
        public void payTrigger(String orderId, String callString) {
            Log.d(TAG, "pay:" + orderId + "|callString:" + callString);
            //TODO 处理支付逻辑
            //......
            //TODO 处理支付逻辑后，把orderId和支付状态传递给SDK
            InKeSdkPluginAPI.dealPay(orderId, true);
        }

        @Override
        public void shareTrigger(ShareInfo share) {

        }

        @Override
        public void createLiveReturnTrigger(String s) {
            Log.d(TAG, "createLiveReturnTrigger:callString:" + s);
            //TODO 开播返回直播ID 返回值格式如下：
//            {
//                   "liveid": 12345666,//直播ID
//                   "third_uid": "123123123"//宿主用户ID
//            }
        }

        @Override
        public void stopLiveTrigger(String s) {
            Log.d(TAG, "stopLiveTrigger:callString:" + s);
            //TODO 结束直播 返回值格式如下：
//            {
//                   "liveid": 12345666,//直播ID
//                   "third_uid": "123123123"//宿主用户ID
//            }
        }
    };

    /**
     * 检查版本更新操作
     */
    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

        @Override
        public void onCheckUpdateCallback(AppUpdateInfo info,
                                          AppUpdateInfoForInstall infoForInstall) {
            // TODO Auto-generated method stub
            if (infoForInstall != null
                    && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {
                BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(),
                        infoForInstall.getInstallPath());
            } else if (info != null) {
                // 判断是否超过了3天，如果是的话就弹出更新对话框
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                long time1 = cal.getTimeInMillis();
                long time2 = sharedPreferenceUtil.getUpdateTime();
                if (time2 <= 0) {
                    sharedPreferenceUtil.setUpdateTime(time1);
                    BDAutoUpdateSDK.uiUpdateAction(MainActivity.this,
                            new MyUICheckUpdateCallback());
                } else {
                    long between_days = (time1 - time2) / (1000 * 3600 * 24);
                    int expired = Integer
                            .parseInt(String.valueOf(between_days));
                    if (expired > 3) {
                        sharedPreferenceUtil.setUpdateTime(time1);
                        BDAutoUpdateSDK.uiUpdateAction(MainActivity.this,
                                new MyUICheckUpdateCallback());
                    }
                }
            }
        }
    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {

        @Override
        public void onCheckComplete() {

        }

    }

    @Override
    public void finish() {
        super.finish();

        mIndexFragment = null;
        mInformationFragment = null;
        mFooterPrintFragment = null;
        mFishingFragment = null;
        mMeFragment = null;

        ((FrameLayout) findViewById(R.id.main_layout_tab1)).removeAllViews();
        ((FrameLayout) findViewById(R.id.main_layout_tab2)).removeAllViews();
        ((FrameLayout) findViewById(R.id.main_layout_tab3)).removeAllViews();
        ((FrameLayout) findViewById(R.id.main_layout_tab4)).removeAllViews();
        ((FrameLayout) findViewById(R.id.main_layout_tab5)).removeAllViews();
    }

    private void setUi() {

        iv_redPoint = (ImageView) findViewById(R.id.main_red_point);

        mIndexLayout = findViewById(R.id.main_index_layout);
        mInfornationLayout = findViewById(R.id.main_information_layout);
        mFooterPrintLayout = findViewById(R.id.main_footer_print_layout);
        mFishingLayout = findViewById(R.id.main_fishing_layout);
        mMeLayout = findViewById(R.id.main_me_layout);

        mViewFlipper = (ViewFlipper) findViewById(R.id.main_switcher);
        mViewFlipper.setInAnimation(this, android.R.anim.fade_in);
        mViewFlipper.setOutAnimation(this, android.R.anim.fade_out);

        iv_mainAdd = (ImageView) findViewById(R.id.main_add_btn);

        mIndexLayout.setOnClickListener(this);
        mFishingLayout.setOnClickListener(this);
        mInfornationLayout.setOnClickListener(this);
        mFooterPrintLayout.setOnClickListener(this);
        mMeLayout.setOnClickListener(this);
        iv_mainAdd.setOnClickListener(this);

    }

    private void showIndexFragment() {
        if (mCurrentMenuType == MenuType.Index) {
            return;
        }

        if (mIndexFragment == null) {
            ((FrameLayout) findViewById(R.id.main_layout_tab1))
                    .removeAllViews();
            mIndexFragment = IndexFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_layout_tab1, mIndexFragment).commit();
        }
        mViewFlipper.setDisplayedChild(0);
        mCurrentMenuType = MenuType.Index;
        // 设置背景
        changeBottomBarImage(R.id.main_index_layout);
    }

    private void showInformationFragment() {
        if (mCurrentMenuType == MenuType.Information) {
            return;
        }

        if (mInformationFragment == null) {
            ((FrameLayout) findViewById(R.id.main_layout_tab2))
                    .removeAllViews();
            mInformationFragment = NewInformationFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_layout_tab2, mInformationFragment).commit();
        }

        mViewFlipper.setDisplayedChild(1);
        mCurrentMenuType = MenuType.Information;
        changeBottomBarImage(R.id.main_information_layout);

    }

    private void showFooterPrintFragment() {
        if (mCurrentMenuType == MenuType.FooterPrint) {
            return;
        }

        if (mFooterPrintFragment == null) {
            ((FrameLayout) findViewById(R.id.main_layout_tab3))
                    .removeAllViews();
            mFooterPrintFragment = FeedcircleFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_layout_tab3, mFooterPrintFragment).commit();
        }

        mViewFlipper.setDisplayedChild(2);
        mCurrentMenuType = MenuType.FooterPrint;
        changeBottomBarImage(R.id.main_footer_print_layout);

    }

    public void showFishingFragment() {
        if (mCurrentMenuType == MenuType.Fishing) {
            return;
        }

        if (mFishingFragment == null) {
            ((FrameLayout) findViewById(R.id.main_layout_tab4))
                    .removeAllViews();
            mFishingFragment = FishingFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_layout_tab4, mFishingFragment).commit();
        }

        mViewFlipper.setDisplayedChild(3);
        mCurrentMenuType = MenuType.Fishing;
        changeBottomBarImage(R.id.main_fishing_layout);
    }

    private void showMeFragment() {
        if (mCurrentMenuType == MenuType.Me) {
            return;
        }

        if (mMeFragment == null) {
            ((FrameLayout) findViewById(R.id.main_layout_tab5))
                    .removeAllViews();
            mMeFragment = MeFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_layout_tab5, mMeFragment).commit();
        }

        mViewFlipper.setDisplayedChild(4);
        mCurrentMenuType = MenuType.Me;
        changeBottomBarImage(R.id.main_me_layout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_index_layout:
                if (sharedPreferenceUtil.getRedPointIsVisible()) {
                    iv_redPoint.setVisibility(View.VISIBLE);
                } else {
                    iv_redPoint.setVisibility(View.GONE);
                }
                showIndexFragment();
                break;
            case R.id.main_information_layout:
                if (sharedPreferenceUtil.getRedPointIsVisible()) {
                    iv_redPoint.setVisibility(View.VISIBLE);
                } else {
                    iv_redPoint.setVisibility(View.GONE);
                }
                showInformationFragment();
                break;
            case R.id.main_footer_print_layout:
                if (sharedPreferenceUtil.getRedPointIsVisible()) {
                    iv_redPoint.setVisibility(View.VISIBLE);
                } else {
                    iv_redPoint.setVisibility(View.GONE);
                }
                showFooterPrintFragment();
                break;
            case R.id.main_fishing_layout:
                if (sharedPreferenceUtil.getRedPointIsVisible()) {
                    iv_redPoint.setVisibility(View.VISIBLE);
                } else {
                    iv_redPoint.setVisibility(View.GONE);
                }
                showFishingFragment();
                break;
            case R.id.main_me_layout:
                if (sharedPreferenceUtil.getRedPointIsVisible()) {
                    iv_redPoint.setVisibility(View.VISIBLE);
                } else {
                    iv_redPoint.setVisibility(View.GONE);
                }
                showMeFragment();
                break;
            case R.id.main_add_btn:
//                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
//                    LoginActivity.launch(this, "meFragment", 0);
//                } else {
//                    if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLongitude())) {
//                        AddFooterprintActivity.launch(this, "",
//                                Double.parseDouble(sharedPreferenceUtil
//                                        .getGPSLongitude()), Double
//                                        .parseDouble(sharedPreferenceUtil
//                                                .getGPSLatitude()), -1);
//                    } else {
//                        AddFooterprintActivity.launch(this, "", 0, 0, -1);
//                    }
//                }
                //InKeSdkPluginAPI.start(MainActivity.this, null, "");
//                IInkeCallback inkeCallback = new IInkeCallback() {
//                    @Override
//                    public void loginTrigger() {
//                        UserInfo userInfo = new UserInfo();
//                        userInfo.nickname = sharedPreferenceUtil.getUserName();
//                        userInfo.sex = sharedPreferenceUtil.getUserSex();
//                        userInfo.uId = sharedPreferenceUtil.getUserID();
//                        userInfo.portrait = sharedPreferenceUtil.getUserHeadUrl();
//                        InKeSdkPluginAPI.login(userInfo);
//                    }
//
//                    @Override
//                    public void payTrigger(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void shareTrigger(ShareInfo shareInfo) {
//
//                    }
//
//                    @Override
//                    public void createLiveReturnTrigger(String createId) {
//                        Log.e("============", createId);
//                        InKeSdkPluginAPI.createLive(MainActivity.this, null);
//                    }
//
//                    @Override
//                    public void stopLiveTrigger(String s) {
//
//                    }
//                };
//                InKeSdkPluginAPI.register(inkeCallback, "1000010001");
//                UserInfo userInfo = new UserInfo();
//                userInfo.nickname = sharedPreferenceUtil.getUserName();
//                userInfo.sex = sharedPreferenceUtil.getUserSex();
//                userInfo.uId = sharedPreferenceUtil.getUserID();
//                userInfo.portrait = sharedPreferenceUtil.getUserHeadUrl();
//                InKeSdkPluginAPI.login(userInfo);
                InKeSdkPluginAPI.start(MainActivity.this, null, "");
                //InKeSdkPluginAPI.createLive(MainActivity.this, userInfo);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (!TextUtils.isEmpty(getIntent().getStringExtra("startFragment"))
                && getIntent().getStringExtra("startFragment").equals(
                "meFragment")) {
            finish();
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastHelper.showToast(this, "再按一次返回键，就会退出应用");
                mExitTime = System.currentTimeMillis();
            } else {
                deleteDatabase("webview.db");
                deleteDatabase("webviewCache.db");
                toHomeLauncher();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                super.onBackPressed();
            }
        }
    }

    private void toHomeLauncher() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void changeBottomBarImage(int selectId) {
        ((ImageView) mIndexLayout.findViewById(R.id.main_index_icon))
                .setImageResource(R.drawable.index_unpress_icon);
        ((ImageView) mInfornationLayout
                .findViewById(R.id.main_information_icon))
                .setImageResource(R.drawable.information_unpress_icon);
        ((ImageView) mFooterPrintLayout
                .findViewById(R.id.main_footer_print_icon))
                .setImageResource(R.drawable.footer_print_unpress_icon);
        ((ImageView) mFishingLayout.findViewById(R.id.main_fishing_icon))
                .setImageResource(R.drawable.fishing_unpress_icon);
        ((ImageView) mMeLayout.findViewById(R.id.main_me_icon))
                .setImageResource(R.drawable.me_unpress_icon);

        ((TextView) mIndexLayout.findViewById(R.id.main_index_text))
                .setTextColor(getResources().getColor(R.color.gray_aaaaaa));

        ((TextView) mInfornationLayout.findViewById(R.id.main_information_text))
                .setTextColor(getResources().getColor(R.color.gray_aaaaaa));
        ((TextView) mFooterPrintLayout
                .findViewById(R.id.main_footer_print_text))
                .setTextColor(getResources().getColor(R.color.gray_aaaaaa));
        ((TextView) mFishingLayout.findViewById(R.id.main_fishing_text))
                .setTextColor(getResources().getColor(R.color.gray_aaaaaa));
        ((TextView) mMeLayout.findViewById(R.id.main_me_text))
                .setTextColor(getResources().getColor(R.color.gray_aaaaaa));

        switch (selectId) {
            case R.id.main_index_layout:
                ((ImageView) mIndexLayout.findViewById(R.id.main_index_icon))
                        .setImageResource(R.drawable.index_icon);
                ((TextView) mIndexLayout.findViewById(R.id.main_index_text))
                        .setTextColor(getResources().getColor(R.color.blue_35b2e1));
                break;

            case R.id.main_information_layout:
                ((ImageView) mInfornationLayout
                        .findViewById(R.id.main_information_icon))
                        .setImageResource(R.drawable.information_icon);
                ((TextView) mInfornationLayout
                        .findViewById(R.id.main_information_text))
                        .setTextColor(getResources().getColor(R.color.blue_35b2e1));
                break;
            case R.id.main_footer_print_layout:
                ((ImageView) mFooterPrintLayout
                        .findViewById(R.id.main_footer_print_icon))
                        .setImageResource(R.drawable.footer_print_icon);
                ((TextView) mFooterPrintLayout
                        .findViewById(R.id.main_footer_print_text))
                        .setTextColor(getResources().getColor(R.color.blue_35b2e1));
                break;
            case R.id.main_fishing_layout:
                ((ImageView) mFishingLayout.findViewById(R.id.main_fishing_icon))
                        .setImageResource(R.drawable.fishing_icon);
                ((TextView) mFishingLayout.findViewById(R.id.main_fishing_text))
                        .setTextColor(getResources().getColor(R.color.blue_35b2e1));
                break;
            case R.id.main_me_layout:
                ((ImageView) mMeLayout.findViewById(R.id.main_me_icon))
                        .setImageResource(R.drawable.me_icon);
                ((TextView) mMeLayout.findViewById(R.id.main_me_text))
                        .setTextColor(getResources().getColor(R.color.blue_35b2e1));
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (sharedPreferenceUtil.getRedPointIsVisible()
                || sharedPreferenceUtil.getDraftsRedPointIsVisible()) {
            iv_redPoint.setVisibility(View.VISIBLE);
        } else {
            iv_redPoint.setVisibility(View.GONE);
        }
    }

    public class UiHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (sharedPreferenceUtil.getRedPointIsVisible()
                    || sharedPreferenceUtil.getDraftsRedPointIsVisible()) {
                iv_redPoint.setVisibility(View.VISIBLE);
            } else {
                iv_redPoint.setVisibility(View.GONE);
            }
        }
    }

    class GetMyInfoTask extends AsyncTask<String, Integer, String> {// 继承AsyncTask

        @Override
        protected String doInBackground(String... params) {
            UserBusinessController.getInstance().getUserInfo(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", new com.example.controller.controller.Listener<MyInfoBean>() {
                @Override
                public void onStart(Object... params) {

                }

                @Override
                public void onComplete(MyInfoBean bean, Object... params) {

                    sharedPreferenceUtil.setUserID(bean.data.user_id);
                    sharedPreferenceUtil.setUserName(bean.data.user_name);
                    sharedPreferenceUtil.setUserHeadUrl(bean.data.head_pic);
                    sharedPreferenceUtil.setUserPhone(bean.data.mobile);
                    sharedPreferenceUtil.setUserSex(bean.data.sex);
                    sharedPreferenceUtil.setMember(bean.data.member);

                }

                @Override
                public void onFail(String msg, Object... params) {
                    ToastHelper.showToast(MainActivity.this, msg);
                }
            });
            return null;// 处理后台执行的任务，在后台线程执行
        }

        protected void onProgressUpdate(Integer... progress) {// 在调用publishProgress之后被调用，在ui线程执行
        }

        protected void onPostExecute(String result) {// 后台任务执行完之后被调用，在ui线程执行
        }

        protected void onPreExecute() {// 在doInBackground(Params...)之前被调用，在ui线程执行
        }

        protected void onCancelled() {// 在ui线程执行
        }

    }

    private void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersionCode() {
        String versionCode = null;
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionCode = String.valueOf(info.versionCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this **/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    class GetCleanTask extends AsyncTask<String, Integer, String> {// 继承AsyncTask

        @Override
        protected String doInBackground(String... params) {
            /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * */
            try {
                deleteFilesByDirectory(getCacheDir());
                // 一定要在背景線程執行
                Glide.get(getApplicationContext()).clearDiskCache();
                ImageLoaderWrapper.getDefault().clearDefaultLoaderDiscCache();
                ImageLoaderWrapper.getDefault().clearDefaultLoaderMemoryCache();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            return null;// 处理后台执行的任务，在后台线程执行
        }

        protected void onProgressUpdate(Integer... progress) {// 在调用publishProgress之后被调用，在ui线程执行
        }

        protected void onPostExecute(String result) {// 后台任务执行完之后被调用，在ui线程执行
        }

        protected void onPreExecute() {// 在doInBackground(Params...)之前被调用，在ui线程执行
        }

        protected void onCancelled() {// 在ui线程执行
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 申请成功
                AMapLocationClient mLocationClient = new AMapLocationClient(
                        getApplicationContext());
                // 设置定位回调监听
                mLocationClient
                        .setLocationListener(new com.amap.api.location.AMapLocationListener() {

                            @Override
                            public void onLocationChanged(
                                    AMapLocation amapLocation) {
                                if (amapLocation != null) {
                                    if (amapLocation.getErrorCode() == 0) {
                                        // 定位成功回调信息，设置相关消息
                                        amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
                                        amapLocation.getLatitude();// 获取纬度
                                        amapLocation.getLongitude();// 获取经度
                                        amapLocation.getAccuracy();// 获取精度信息
                                        amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果
                                        amapLocation.getCountry();// 国家信息
                                        amapLocation.getProvince();// 省信息
                                        amapLocation.getCity();// 城市信息
                                        amapLocation.getDistrict();// 城区信息
                                        amapLocation.getRoad();// 街道信息
                                        amapLocation.getCityCode();// 城市编码
                                        amapLocation.getAdCode();// 地区编码
                                        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(
                                                FishingApplication.getContext());
                                        sharedPreferenceUtil
                                                .setLocationAddress(amapLocation
                                                        .getAddress());
                                        sharedPreferenceUtil
                                                .setGPSCity(amapLocation
                                                        .getCity());
                                        sharedPreferenceUtil.setGPSLatitude(String
                                                .valueOf(amapLocation
                                                        .getLatitude()));
                                        sharedPreferenceUtil.setGPSLongitude(String
                                                .valueOf(amapLocation
                                                        .getLongitude()));
                                        // 停止定位服务
                                        if (FishingApplication.mLocationClient != null) {
                                            FishingApplication.mLocationClient
                                                    .stopLocation();
                                        }
                                    } else {
                                        // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                                        Log.e("AmapError",
                                                "location Error, ErrCode:"
                                                        + amapLocation
                                                        .getErrorCode()
                                                        + ", errInfo:"
                                                        + amapLocation
                                                        .getErrorInfo());
                                    }
                                }
                            }
                        });
                // 初始化定位参数
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption
                        .setLocationMode(AMapLocationMode.Battery_Saving);
                // 设置是否返回地址信息（默认返回地址信息）
                mLocationOption.setNeedAddress(true);
                // 设置是否只定位一次,默认为false
                mLocationOption.setOnceLocation(true);
                // 设置是否强制刷新WIFI，默认为强制刷新
                mLocationOption.setWifiActiveScan(true);
                // 设置是否允许模拟位置,默认为false，不允许模拟位置
                mLocationOption.setMockEnable(false);
                // 设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);
                // 给定位客户端对象设置定位参数
                mLocationClient.setLocationOption(mLocationOption);
                // 启动定位
                mLocationClient.startLocation();
            } else {
                // 设置gps拒绝标志位
                sharedPreferenceUtil.setGPSTag(true);
            }
        }
    }
}
