package com.goby.fishing.module.fishing;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearch.OnWeatherSearchListener;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.baidu.android.common.logging.Log;
import com.example.controller.controller.UserBusinessController;
import com.example.qiniu.storage.UpCompletionHandler;
import com.example.qiniu.storage.UploadManager;
import com.goby.emojilib.emoji.Emoji;
import com.goby.emojilib.emoji.EmojiUtil;
import com.goby.emojilib.emoji.FaceFragment;
import com.goby.fishing.R;
import com.goby.fishing.application.FishingApplication;
import com.example.controller.bean.AddFooterprintBean;
import com.example.controller.bean.BaseBean;
import com.goby.fishing.common.photochoose.Bimp;
import com.goby.fishing.common.photochoose.PictureBar;
import com.goby.fishing.common.photochoose.crop.BitmapUtils;
import com.goby.fishing.common.photochoose.crop.Crop;
import com.goby.fishing.common.utils.helper.android.util.DialogBuilder;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.qiniu.android.http.ResponseInfo;

@SuppressLint("NewApi")
public class AddFooterprintActivity extends AbsBaseActivity implements
        OnClickListener, OnWeatherSearchListener, OnLayoutChangeListener,
        FaceFragment.OnEmojiClickListener {

    private RelativeLayout rl_addGps;
    private RelativeLayout rl_fishingType;
    private RelativeLayout rl_fishingCount;
    private RelativeLayout rl_fishingTackle;
    private PictureBar mPictureBar;
    private EditText et_info;
    private Button btn_addFooterprint;
    private TextView tv_fishPointInfo;
    private TextView tv_fishCount;
    private ImageView iv_back;
    private TextView tv_fishPointName;
    private TextView tv_distance;
    private TextView tv_shareWeather;
    private TextView tv_fishingTackleInfo;
    // private GridView gv_addFeedTag;
    // private TagAdapter adapter;
    private LinearLayout ll_emojiLayout;
    private ImageView iv_emoji;
    private FrameLayout fm_emoji;
    private View v_bottom;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private WeatherSearchQuery mquery;

    private WeatherSearch mweathersearch;

    private String locationWeather;

    private String weather = "";

    private boolean isPush = true;

    private final static int FISH_CATCH_INFO = 104;
    private final static int FISH_POINT_INFO = 105;
    private final static int FISH_TACKLE_INFO = 106;

    private double mLongitude = 0;

    private double mLatitude = 0;

    private int mFishPointId;

    private int number = -99;

    private String fishPointName = "";

    private String info;

    private String type_ids = "";

    private int tag_id = 1;

    private int id;

    private LinearLayout ll_leftBack;

    private String code;

    private String tools = "";

    private int quality = 0;

    private int uploadImageCount = 0;

    private String selectTag = "日常";

    // private ArrayList<String> tagDataList = new ArrayList<String>();

    // Activity最外层的Layout视图
    private View activityRootView;
    // 屏幕高度
    private int screenHeight = 0;
    // 软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private boolean isShowEmoji = false;
    private boolean isOther = false;

    public static void launch(Activity activity, String name, double longitude,
                              double latitude, int fishPointId) {

        Intent intent = new Intent(activity, AddFooterprintActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        intent.putExtra("fishPointId", fishPointId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_footerprint);

        activityRootView = findViewById(R.id.root_layout);
        // 获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        // 阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        // initData();

        uploadImageCount = 0;
        fishPointName = getIntent().getStringExtra("name");
        int randomCode = (int) (Math.random() * 1000000 + 100000);
        code = String.valueOf(randomCode);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        try {
            mLongitude = getIntent().getDoubleExtra("longitude", 0);
            mLatitude = getIntent().getDoubleExtra("latitude", 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        mFishPointId = getIntent().getIntExtra("fishPointId", 0);
        initView();
        FaceFragment faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.Container, faceFragment).commit();

        // 检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        mquery = new WeatherSearchQuery(sharedPreferenceUtil.getGPSCity(),
                WeatherSearchQuery.WEATHER_TYPE_LIVE);
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); // 异步搜索
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(
                            this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            101);
        }
    }

    // public void initData() {
    // tagDataList.add("野钓");
    // tagDataList.add("黑坑");
    // tagDataList.add("装备");
    // tagDataList.add("饵料");
    // tagDataList.add("路亚");
    // tagDataList.add("海钓");
    // tagDataList.add("日常");
    // }

    public void initView() {

        mPictureBar = (PictureBar) findViewById(R.id.picture_bar);
        Bimp.setMax(9);
        mPictureBar.initData(this);
        mPictureBar.setListener(this);

        rl_addGps = (RelativeLayout) findViewById(R.id.add_gps_layout);
        rl_fishingType = (RelativeLayout) findViewById(R.id.fishpoint_info_layout);
        rl_fishingCount = (RelativeLayout) findViewById(R.id.fishing_count_layout);
        rl_fishingTackle = (RelativeLayout) findViewById(R.id.fishing_tackle_layout);
        btn_addFooterprint = (Button) findViewById(R.id.add_footerprint);
        et_info = (EditText) findViewById(R.id.edit_info);
        tv_fishPointInfo = (TextView) findViewById(R.id.fish_point_info);
        tv_fishCount = (TextView) findViewById(R.id.fish_count);
        tv_fishPointName = (TextView) findViewById(R.id.fishpoint_name);
        tv_fishPointName.setText(fishPointName);
        tv_distance = (TextView) findViewById(R.id.distance);
        try {
            tv_distance
                    .setText(distance(mLatitude, mLongitude,
                            Double.parseDouble(sharedPreferenceUtil
                                    .getGPSLatitude()), Double
                                    .parseDouble(sharedPreferenceUtil
                                            .getGPSLongitude())));
        } catch (Exception e) {
            // TODO: handle exception
        }
        tv_shareWeather = (TextView) findViewById(R.id.share_weather);
        if (sharedPreferenceUtil.getLocalWeather()) {
            tv_shareWeather.setTextColor(AddFooterprintActivity.this
                    .getResources().getColor(R.color.blue_35b2e1));
        } else {
            tv_shareWeather.setTextColor(AddFooterprintActivity.this
                    .getResources().getColor(R.color.gray_aaaaaa));
        }
        tv_fishingTackleInfo = (TextView) findViewById(R.id.fishing_tackle_info_text);
        iv_back = (ImageView) findViewById(R.id.left_back);

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        // gv_addFeedTag = (GridView) findViewById(R.id.addfeed_tag);
        // gv_addFeedTag.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // adapter = new TagAdapter();
        // gv_addFeedTag.setAdapter(adapter);
        // gv_addFeedTag.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1,
        // int position, long arg3) {
        // // TODO Auto-generated method stub
        // selectTag = tagDataList.get(position);
        // tagDataList.add("野钓");
        // tagDataList.add("黑坑");
        // tagDataList.add("装备");
        // tagDataList.add("饵料");
        // tagDataList.add("路亚");
        // tagDataList.add("海钓");
        // tagDataList.add("日常");
        // if (position == 0) {
        // tag_id = 2;
        // } else if (position == 1) {
        // tag_id = 3;
        // } else if (position == 2) {
        // tag_id = 4;
        // } else if (position == 3) {
        // tag_id = 5;
        // } else if (position == 4) {
        // tag_id = 6;
        // } else if (position == 5) {
        // tag_id = 7;
        // } else if (position == 6) {
        // tag_id = 1;
        // }
        // adapter.notifyDataSetChanged();
        // }
        // });
        ll_emojiLayout = (LinearLayout) findViewById(R.id.emoji_layout);
        iv_emoji = (ImageView) findViewById(R.id.emoji_icon);
        fm_emoji = (FrameLayout) findViewById(R.id.Container);
        v_bottom = (View) findViewById(R.id.bottom_view);

        ll_leftBack.setOnClickListener(this);
        rl_addGps.setOnClickListener(this);
        rl_fishingType.setOnClickListener(this);
        rl_fishingCount.setOnClickListener(this);
        rl_fishingTackle.setOnClickListener(this);
        btn_addFooterprint.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_shareWeather.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            // case R.id.add_gps_layout:
            // FishPointListActivity.launch(this, FISH_POINT_NAME);
            // break;
            case R.id.fishpoint_info_layout:

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (sharedPreferenceUtil.getGPSTag()) {
                        new DialogBuilder(this, "center")
                                .setMessage("是否允许应用访问gps权限")
                                .setButtons(
                                        "拒绝",
                                        "设置",
                                        new DialogBuilder.OnDialogButtonClickListener() {

                                            @Override
                                            public void onDialogButtonClick(
                                                    Context context,
                                                    DialogBuilder builder,
                                                    Dialog dialog, int dialogId,
                                                    int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                if (which == DialogBuilder.OnDialogButtonClickListener.BUTTON_RIGHT) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(
                                                            Settings.ACTION_SETTINGS);
                                                    startActivity(intent);
                                                    startActivity(intent);
                                                } else {
                                                    dialog.dismiss();
                                                }

                                            }
                                        }).create().show();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 102);
                    }
                } else {
                    if (TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
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
                                                        FishingApplication
                                                                .getContext());
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
                        mLocationOption.setNeedAddress(false);
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
                    }
                    if (TextUtils.isEmpty(fishPointName)
                            || fishPointName.endsWith("未知")) {
                        FishPointListActivity.launch(AddFooterprintActivity.this,
                                FISH_POINT_INFO);
                    } else {
                        FishPointInfoActivity.launch(AddFooterprintActivity.this,
                                FISH_POINT_INFO, mLongitude, mLatitude);
                    }
                }
                break;
            case R.id.fishing_count_layout:
                CatchInfoStepOneActivity.launch(this, FISH_CATCH_INFO);
                break;
            case R.id.fishing_tackle_layout:
                FishingTackleInfoActivity.launch(this, FISH_TACKLE_INFO);
                break;
            case R.id.add_footerprint:
                if (isPush) {
                    isPush = false;
                    if (Bimp.mSelectedList == null
                            || Bimp.mSelectedList.size() == 0) {
                        ToastHelper.showToast(this, "请添加图片");
                        isPush = true;
                        return;
                    }
                    info = et_info.getText().toString().trim();
                    if (sharedPreferenceUtil.getLocalWeather()) {
                        weather = locationWeather;
                    } else {
                        weather = "";
                    }
                    UserBusinessController.getInstance().postFishFeed(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", mLongitude, mLatitude, number, info, mFishPointId,
                            Bimp.mSelectedList.size(), code, sharedPreferenceUtil
                                    .getGPSCity().replace("市", ""), "", tools,
                            quality, tag_id, weather, type_ids, new com.example.controller.controller.Listener<AddFooterprintBean>() {
                                @Override
                                public void onStart(Object... params) {
                                    showProgressDialog("正在发布渔获中,请稍候...");
                                }

                                @Override
                                public void onComplete(AddFooterprintBean result, Object... params) {
                                    isPush = true;
                                    id = result.data.id;
                                    uploadImageCount = 0;
                                    for (int i = 0; i < result.data.pic_urls.size(); i++) {
                                        String imgUrl = "";
                                        if (Bimp.mSelectedList.get(i).startsWith("file://")) {
                                            imgUrl = Bimp.mSelectedList.get(i).replace("file://",
                                                    "");
                                        } else {
                                            imgUrl = Bimp.mSelectedList.get(i);
                                        }
                                        try {
                                            File file = BitmapUtils.resizeImage(imgUrl);
                                            uploadImg(result.data.picture_token.get(i),
                                                    file.getAbsolutePath(), result.data.pic_urls.get(i),
                                                    result.data.pic_urls.size());
                                        } catch (Exception e) {
                                            dismissProgressDialog();
                                            ToastHelper.showToast(AddFooterprintActivity.this,
                                                    "上传失败,请重新操作");
                                        }
                                    }

                                }

                                @Override
                                public void onFail(String msg, Object... params) {
                                    dismissProgressDialog();
                                    ToastHelper.showToast(AddFooterprintActivity.this, msg);
                                }
                            });
                }

                break;

            case R.id.left_back_layout:
                if (isShowEmoji) {
                    isShowEmoji = false;
                    isOther = false;
                    iv_emoji.setBackgroundResource(R.drawable.emoji_icon);
                    ll_emojiLayout.setVisibility(View.GONE);
                    btn_addFooterprint.setVisibility(View.VISIBLE);
                    v_bottom.setVisibility(View.VISIBLE);
                    fm_emoji.setVisibility(View.GONE);
                } else {
                    hideSoftInput();
                    finish();
                }
                break;
            case R.id.left_back:
                if (isShowEmoji) {
                    isShowEmoji = false;
                    isOther = false;
                    iv_emoji.setBackgroundResource(R.drawable.emoji_icon);
                    ll_emojiLayout.setVisibility(View.GONE);
                    btn_addFooterprint.setVisibility(View.VISIBLE);
                    v_bottom.setVisibility(View.VISIBLE);
                    fm_emoji.setVisibility(View.GONE);
                } else {
                    hideSoftInput();
                    finish();
                }
                break;
            case R.id.share_weather:
                new DialogBuilder(this, "center")
                        .setMessage("是否同意共享天气信息")
                        .setButtons("拒绝", "同意",
                                new DialogBuilder.OnDialogButtonClickListener() {

                                    @Override
                                    public void onDialogButtonClick(
                                            Context context, DialogBuilder builder,
                                            Dialog dialog, int dialogId, int which) {
                                        // TODO Auto-generated method stub
                                        if (which == DialogBuilder.OnDialogButtonClickListener.BUTTON_RIGHT) {
                                            dialog.dismiss();
                                            sharedPreferenceUtil
                                                    .setLocalWeather(true);
                                            tv_shareWeather
                                                    .setTextColor(AddFooterprintActivity.this
                                                            .getResources()
                                                            .getColor(
                                                                    R.color.blue_35b2e1));
                                        } else {
                                            dialog.dismiss();
                                            sharedPreferenceUtil
                                                    .setLocalWeather(false);
                                            tv_shareWeather
                                                    .setTextColor(AddFooterprintActivity.this
                                                            .getResources()
                                                            .getColor(
                                                                    R.color.gray_aaaaaa));
                                        }

                                    }
                                }).create().show();
                break;
            case R.id.emoji_icon:
                // 表情切换
                isShowEmoji = !isShowEmoji;
                isOther = true;
                if (isShowEmoji) {
                    iv_emoji.setBackgroundResource(R.drawable.keyboard_icon);
                    hideSoftInput();
                    fm_emoji.setVisibility(View.VISIBLE);

                } else {
                    isOther = false;
                    iv_emoji.setBackgroundResource(R.drawable.emoji_icon);
                    fm_emoji.setVisibility(View.GONE);
                    showSoftInput();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mPictureBar.refreshList();
        // 添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case FISH_CATCH_INFO:
                    number = data.getIntExtra("number", -99);
                    type_ids = data.getStringExtra("type_ids");
                    tv_fishCount.setText(data.getStringExtra("fishCatchInfo"));
                    tv_fishCount.setTextColor(this.getResources().getColor(
                            R.color.gray_333333));
                    break;
                case FISH_POINT_INFO:
                    mLatitude = data.getDoubleExtra("chooseLatitude", 0);
                    mLongitude = data.getDoubleExtra("chooseLongitude", 0);
                    if (!TextUtils.isEmpty(data.getStringExtra("fishPointName"))) {
                        fishPointName = data.getStringExtra("fishPointName");
                    }
                    if (TextUtils.isEmpty(data.getStringExtra("fishPointInfo"))) {
                        if (TextUtils
                                .isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
                            tv_fishPointInfo.setText(fishPointName + "暂无距离 ");
                        } else {
                            tv_fishPointInfo.setText(fishPointName
                                    + distance(mLatitude, mLongitude, Double
                                    .parseDouble(sharedPreferenceUtil
                                            .getGPSLatitude()), Double
                                    .parseDouble(sharedPreferenceUtil
                                            .getGPSLongitude())));
                        }
                        tv_fishPointInfo.setTextColor(this.getResources().getColor(
                                R.color.gray_333333));

                    } else {
                        if (TextUtils
                                .isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
                            tv_fishPointInfo.setText(fishPointName + "暂无距离 "
                                    + data.getStringExtra("fishPointInfo"));
                        } else {
                            tv_fishPointInfo.setText(fishPointName
                                    + distance(mLatitude, mLongitude, Double
                                    .parseDouble(sharedPreferenceUtil
                                            .getGPSLatitude()), Double
                                    .parseDouble(sharedPreferenceUtil
                                            .getGPSLongitude())) + " "
                                    + data.getStringExtra("fishPointInfo"));
                        }
                        tv_fishPointInfo.setTextColor(this.getResources().getColor(
                                R.color.gray_333333));
                    }
                    if (data.getIntExtra("fishPointId", 0) != 0) {
                        mFishPointId = data.getIntExtra("fishPointId", 0);
                    }
                    quality = data.getIntExtra("quality", 0);
                    break;
                case FISH_TACKLE_INFO:
                    tools = data.getStringExtra("fishingTackleInfo");
                    tv_fishingTackleInfo.setText(tools);
                    tv_fishingTackleInfo.setTextColor(this.getResources().getColor(
                            R.color.gray_333333));
                    break;
                case Crop.REQUEST_CAMERA: // 拍照
                    mPictureBar.onCameraResultHandle(requestCode, resultCode, data);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 上传图片到七牛
     */
    private void uploadImg(final String uploadToken, final String imgPath,
                           final String key, final int allCount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获得七牛上传凭证uploadToken
                String token = uploadToken;
                if (token != null) {
                    // 手机SD卡图片存放路径
                    String data = imgPath;
                    UploadManager uploadManager = new UploadManager();
                    uploadManager.put(data, key, token,
                            new UpCompletionHandler() {

                                @Override
                                public void complete(String key,
                                                     ResponseInfo info, JSONObject response) {
                                    if (info.isOK()) {
                                        uploadImageCount++;
                                        if (uploadImageCount == allCount) {
                                            UserBusinessController.getInstance().notifyFishLocation(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", id, new com.example.controller.controller.Listener<BaseBean>() {
                                                @Override
                                                public void onStart(Object... params) {

                                                }

                                                @Override
                                                public void onComplete(BaseBean result, Object... params) {
                                                    dismissProgressDialog();
                                                    ToastHelper.showToast(AddFooterprintActivity.this, "发布成功");
                                                    finish();

                                                }

                                                @Override
                                                public void onFail(String msg, Object... params) {
                                                    ToastHelper.showToast(AddFooterprintActivity.this, msg);
                                                }
                                            });
                                        }
                                    } else {
                                        // 失败的时候
                                        dismissProgressDialog();
                                        ToastHelper.showToast(
                                                AddFooterprintActivity.this,
                                                "上传失败,请重新操作");
                                        return;
                                    }
                                }
                            }, null);
                }
            }
        }).start();
    }

    // 计算两点的距离
    public String distance(double lat_a, double lng_a, double lat_b,
                           double lng_b) {
        if (lat_a <= 0 || lng_a <= 0) {
            return "";
        } else {
            final double EARTH_RADIUS = 6378137.0;
            double radLat1 = (lat_a * Math.PI / 180.0);
            double radLat2 = (lat_b * Math.PI / 180.0);
            double a = radLat1 - radLat2;
            double b = (lng_a - lng_b) * Math.PI / 180.0;
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                    + Math.cos(radLat1) * Math.cos(radLat2)
                    * Math.pow(Math.sin(b / 2), 2)));
            s = s * EARTH_RADIUS;
            s = Math.round(s * 10000) / 10000 / 1000;
            return " 钓点距您" + String.valueOf(s) + "公里";
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult arg0,
                                          int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult,
                                      int rCode) {

        if (weatherLiveResult != null
                && weatherLiveResult.getLiveResult() != null) {
            LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
            locationWeather = weatherlive.getWeather()
                    + weatherlive.getTemperature() + "°  "
                    + weatherlive.getWindDirection() + "风"
                    + weatherlive.getWindPower() + "级  " + "湿度"
                    + weatherlive.getHumidity() + "%";
        } else {
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Bimp.mSelectedList.clear();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (!isOther) {

            if (oldBottom != 0 && bottom != 0
                    && (oldBottom - bottom > keyHeight)) {
                // 弹起软键盘
                btn_addFooterprint.setVisibility(View.GONE);
                v_bottom.setVisibility(View.GONE);
                ll_emojiLayout.setVisibility(View.VISIBLE);
                iv_emoji.setBackgroundResource(R.drawable.emoji_icon);
            } else if (oldBottom != 0 && bottom != 0
                    && (bottom - oldBottom > keyHeight)) {
                // 隐藏软键盘
                isShowEmoji = false;
                iv_emoji.setBackgroundResource(R.drawable.emoji_icon);
                ll_emojiLayout.setVisibility(View.GONE);
                btn_addFooterprint.setVisibility(View.VISIBLE);
                v_bottom.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onEmojiDelete() {
        // TODO Auto-generated method stub

        String text = et_info.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                et_info.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                return;
            }
            et_info.getText().delete(index, text.length());
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        et_info.onKeyDown(KeyEvent.KEYCODE_DEL, event);

    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            try {
                et_info.append(EmojiUtil.handlerEmojiText(emoji.getContent(),
                        this));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == event.KEYCODE_BACK) {
            if (isShowEmoji) {
                isShowEmoji = false;
                isOther = false;
                iv_emoji.setBackgroundResource(R.drawable.emoji_icon);
                ll_emojiLayout.setVisibility(View.GONE);
                btn_addFooterprint.setVisibility(View.VISIBLE);
                v_bottom.setVisibility(View.VISIBLE);
                fm_emoji.setVisibility(View.GONE);
                return true;
            } else {
                hideSoftInput();
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
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
