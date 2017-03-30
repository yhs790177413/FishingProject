package com.goby.fishing.application;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.example.controller.controller.Listener;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.CheckUserBean;
import com.example.controller.bean.CityListBean;
import com.example.controller.bean.FishCityListBean;
import com.example.controller.bean.PushMessageBean;
import com.example.controller.bean.TagsListBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.module.fishing.FishingDetailActivity;
import com.goby.fishing.module.footerprint.FooterprintDetailActivity;
import com.goby.fishing.module.information.FishingInfoDetailActivity;
import com.goby.fishing.module.other.AllMessageActivity;
import com.google.gson.Gson;
import com.meelive.ingkee.sdk.plugin.IInkeCallback;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.meelive.ingkee.sdk.plugin.entity.ShareInfo;
import com.morgoo.droidplugin.PluginHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.youzan.sdk.YouzanSDK;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FishingApplication extends Application {
    /**
     * 全局环境
     */
    private static Context mContext;

    private SharedPreferenceUtil spUtil;

    // 声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;

    // 声明定位回调监听器
    public AMapLocationListener mLocationListener = null;

    // 声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    /**
     * 用户全局
     */
    private static ArrayList<Activity> list;
    public static PopupWindow showListDialogPop;
    // private File saveDir;
    private PushAgent mPushAgent;
    private static int statusCode = 4;
    // private static UpdateResponse updateInfo;
    // private String path = Environment.getExternalStorageDirectory()
    // .getAbsolutePath()
    // + File.separator
    // + "Android/data/com.goby.fishing";
    private String device_token = "";

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Config.DEBUG = true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxaaa1fae3d34f4c7f",
                "d4624c36b6795d1d99dcf0547af5443d");
        PlatformConfig.setQQZone("1104974130", "r06qD0LgeFjjCOEj");
        MobclickAgent.openActivityDurationTrack(true);
        Init();
        /**
         * 初始化
         *
         * @param Context
         *            application Context
         * @param userAgent
         *            用户代理, 填写UserAgent
         */
        YouzanSDK.init(this, "b5dfee98a086d9fa291476067998202");
        list = new ArrayList<Activity>();
        // 初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        mLocationListener = new AMapLocationListener();
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
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
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
        File file = new File(Environment.getExternalStorageDirectory()
                + Constant.PATH_ICON);
        ImageLoaderWrapper.initDefault(mContext, file, false);

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        mPushAgent.setDebugMode(true);
        device_token = UmengRegistrar.getRegistrationId(this);
        spUtil.setDeviceToken(device_token);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3 http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context,
                                              final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            // 自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext())
                                    .trackMsgClick(msg);
                        } else {
                            // 自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext())
                                    .trackMsgDismissed(msg);
                        }
                        spUtil.setRedPointIsVisible(true);
                        Gson gson = new Gson();
                        PushMessageBean messageBean = gson.fromJson(msg.custom,
                                PushMessageBean.class);
                        if (messageBean != null) {
                            NotificationManager nManager = (NotificationManager) getApplicationContext()
                                    .getSystemService(
                                            Context.NOTIFICATION_SERVICE);
                            Notification.Builder builder = new Notification.Builder(getApplicationContext());
                            builder.setContentText(msg.title);
                            builder.setContentTitle("去哪钓鱼");
                            builder.setSmallIcon(R.drawable.app_icon);
                            builder.setTicker("去哪钓鱼");
                            builder.setAutoCancel(true);
                            builder.setWhen(System.currentTimeMillis());
                            Intent notificationIntent = null;
                            if (messageBean.type.equals("1")) { // 资讯
                                notificationIntent = new Intent(
                                        getApplicationContext(),
                                        FishingInfoDetailActivity.class);
                            } else if (messageBean.type.equals("2")) {// 钓点
                                notificationIntent = new Intent(
                                        getApplicationContext(),
                                        FishingDetailActivity.class);
                            } else if (messageBean.type.equals("3")) {// 动态
                                notificationIntent = new Intent(
                                        getApplicationContext(),
                                        FooterprintDetailActivity.class);
                            } else if (messageBean.type.equals("100")) {// 私信列表
                                notificationIntent = new Intent(
                                        getApplicationContext(),
                                        AllMessageActivity.class);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("id", messageBean.objectId);
                            notificationIntent.putExtras(bundle);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                            builder.setContentIntent(pendingIntent);
                            Notification notification = builder.build();
                            notification.defaults |= Notification.DEFAULT_VIBRATE;
                            notification.defaults |= Notification.DEFAULT_LIGHTS;
                            notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击通知后清除通知
                            notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认
                            nManager.notify((int) (Math.random() * 1000000 + 100000), notification);
                        }
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4 http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                                context);
                        RemoteViews myNotificationView = new RemoteViews(
                                context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title,
                                msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text,
                                msg.text);
                        myNotificationView.setImageViewBitmap(
                                R.id.notification_large_icon,
                                getLargeIcon(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setContentTitle(msg.title).setContentText(msg.text)
                                .setTicker(msg.ticker).setAutoCancel(true);
                        Notification mNotification = builder.build();
                        // 由于Android
                        // v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        // 默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);


        BackgroundTask task = new BackgroundTask();
        task.execute();
        BackgroundCityTask cityTask = new BackgroundCityTask();
        cityTask.execute();
        BackgroundTagTask tagTask = new BackgroundTagTask();
        tagTask.execute();
        if (!spUtil.getsetBindDeviceToken()
                && !TextUtils.isEmpty(spUtil.getUserToken())
                && !TextUtils.isEmpty(device_token)) {
            BindDeviceTokenTask bindDeviceTokenTask = new BindDeviceTokenTask();
            bindDeviceTokenTask.execute();
        }
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
    }

    /**
     * 删除文件夹里面的所有文件
     * <p>
     * String 文件夹路径 如 c:/fqf
     */

    public static ArrayList<Activity> getActivitys() {
        return list;
    }

    private void Init() {
        // TODO Auto-generated method stub
        FishingApplication.mContext = getApplicationContext();
        spUtil = new SharedPreferenceUtil(this);
        // ImageCacheManager.init(FishingApplication.mContext);
    }

    public static Context getContext() {
        return FishingApplication.mContext;
    }

    public static int getStatusCode() {
        return statusCode;
    }

    public static void setStatusCode(int statusCode) {
        FishingApplication.statusCode = statusCode;
    }

    @SuppressWarnings("rawtypes")
    class BackgroundTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            try {
                UserBusinessController.getInstance().getCityIds(null, getVersionCode(), "2", new Listener<CityListBean>() {
                    @Override
                    public void onStart(Object... params) {

                    }

                    @Override
                    public void onComplete(CityListBean bean, Object... params) {

                            Gson gson = new Gson();
                            String cityJson = gson.toJson(bean);
                            spUtil.setCityJson(cityJson);

                    }

                    @Override
                    public void onFail(String msg, Object... params) {

                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

    }

    class BackgroundCityTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            try {
                UserBusinessController.getInstance().getCityList(null, getVersionCode(), "2", new Listener<FishCityListBean>() {
                    @Override
                    public void onStart(Object... params) {

                    }

                    @Override
                    public void onComplete(FishCityListBean bean, Object... params) {

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            long time1 = cal.getTimeInMillis();
                            spUtil.setFishCityTime(time1);
                            Gson gson = new Gson();
                            String fishCityJson = gson.toJson(bean);
                            spUtil.setFishCityJson(fishCityJson);

                    }

                    @Override
                    public void onFail(String msg, Object... params) {

                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

    }

    class BackgroundTagTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            try {
                UserBusinessController.getInstance().getInfoTags(null, getVersionCode(), "2", 1, 200, new Listener<TagsListBean>() {
                    @Override
                    public void onStart(Object... params) {

                    }

                    @Override
                    public void onComplete(TagsListBean bean, Object... params) {

                            if (bean.data.list != null && bean.data.list.size() > 0) {
                                Gson gson = new Gson();
                                spUtil.setTags(gson.toJson(bean));
                            }

                    }

                    @Override
                    public void onFail(String msg, Object... params) {

                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

    }

    class BindDeviceTokenTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            UserBusinessController.getInstance().getBindDeviceTokenJson(spUtil.getUserToken(), getVersionCode(), "2", device_token, new Listener<BaseBean>() {
                @Override
                public void onStart(Object... params) {

                }

                @Override
                public void onComplete(BaseBean bean, Object... params) {

                }

                @Override
                public void onFail(String msg, Object... params) {

                }
            });
            return null;
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
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    mContext.getPackageName(), 0);
            versionCode = String.valueOf(info.versionCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取裁剪图片临时目录
     *
     * @return
     */
    public File getCropTmpDir() {
        File dir = new File(getTmpDir(), "crop_image_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    public File getTmpDir() {
        return getTmpDir(false);
    }

    /**
     * 获取临时目录
     *
     * @param isSdcard 是否只取sd卡上的目录
     * @return
     */
    public File getTmpDir(boolean isSdcard) {
        File tmpDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdcard && !sdCardExist) {
            if (!sdCardExist) {
                return null;
            }
        }

        if (sdCardExist || isSdcard) {
            tmpDir = new File(Environment.getExternalStorageDirectory(),
                    getTmpDirName());
        } else {
            tmpDir = new File(getCacheDir(), getTmpDirName());
        }

        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        return tmpDir;
    }

    public String getTmpDirName() {
        return "fishing";
    }

}
