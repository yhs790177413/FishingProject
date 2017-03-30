package com.goby.fishing.module.fishing;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.goby.fishing.R;
import com.example.controller.bean.WeatherDetailBean;
import com.example.controller.bean.WeatherDetailBean.Data.WeatherTime;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.ShareDialogUtils;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TimeUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.umeng.socialize.UMShareAPI;

@SuppressLint("HandlerLeak")
public class WeatherDetailActivity extends AbsBaseActivity implements
        OnClickListener {

    private SharedPreferenceUtil sharedPreferenceUtil;

    private ImageView iv_fishpoint;

    private TextView tv_fishpointName;

    private TextView tv_weather;

    private TextView tv_time;

    private TextView tv_pressure;

    private TextView tv_temp;

    private TextView tv_humidity;

    private TextView tv_windDeg;

    private TextView tv_windLevel;

    private TextView tv_clouds;

    private TextView tv_meteorologicalTips;

    private TextView tv_cloundAndRainTips;

    private TextView tv_springTips;

    private TextView tv_summaryTips;

    private TextView tv_autumnTips;

    private TextView tv_winterTips;

    private TextView tv_proverbTips;

    private TextView tv_dayOne;

    private TextView tv_dayTwo;

    private TextView tv_dayThree;

    private TextView tv_dayFour;

    private TextView tv_dayFive;

    private TextView tv_dateOne;

    private TextView tv_dateTwo;

    private TextView tv_dateThree;

    private TextView tv_dateFour;

    private TextView tv_dateFive;

    private ImageView iv_meteorologicalLine;

    private ImageView iv_cloundAndRainLine;

    private ImageView iv_springLine;

    private ImageView iv_summaryLine;

    private ImageView iv_autumnLine;

    private ImageView iv_winterLine;

    private ImageView iv_dayOne;

    private ImageView iv_dayTwo;

    private ImageView iv_dayThree;

    private ImageView iv_dayFour;

    private ImageView iv_dayFive;

    private LineChart mLineChart;

    private LinearLayout ll_temp;

    private LinearLayout ll_pressure;

    private ImageView iv_pressure;

    private ImageView iv_temp;

    private TextView tv_chooseTemp;

    private TextView tv_chooseHumidity;

    private TextView tv_choosePressure;

    private TextView tv_chooseWeather;

    private TextView tv_chooseWind;

    private TextView tv_chooseClound;

    private LinearLayout ll_layoutDayOne;

    private LinearLayout ll_layoutDayTwo;

    private LinearLayout ll_layoutDayThree;

    private LinearLayout ll_layoutDayFour;

    private LinearLayout ll_layoutDayFive;

    private LinearLayout ll_leftBack;

    private TextView tv_shareBtn;

    private WeatherDetailBean dataBean;

    private boolean isTemp = false;

    private LineData mLineData;

    private int mTempMax = 0, mTempMin = 0;

    private double mPressureMax = 0, mPressureMin = 0;

    private ArrayList<WeatherTime> chooseDataBean = new ArrayList<WeatherTime>();

    private ShareDialogUtils dialog_share;

    private UIHandler uiHandler;

    private String dateString = "";

    public static void launch(Activity activity, String imgSrc, int mId,
                              String fishPointName) {

        Intent intent = new Intent(activity, WeatherDetailActivity.class);
        intent.putExtra("imgSrc", imgSrc);
        intent.putExtra("mId", mId);
        intent.putExtra("fishPointName", fishPointName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        uiHandler = new UIHandler();
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
        initData();
    }

    public void initView() {

        iv_fishpoint = (ImageView) findViewById(R.id.fishpoint_image);
        ImageLoaderWrapper.getDefault().displayImage(
                getIntent().getStringExtra("imgSrc"), iv_fishpoint);
        tv_fishpointName = (TextView) findViewById(R.id.fishpoint_name);
        tv_weather = (TextView) findViewById(R.id.weather_text);
        tv_time = (TextView) findViewById(R.id.time);
        tv_pressure = (TextView) findViewById(R.id.pressure_text);
        tv_temp = (TextView) findViewById(R.id.temp_text);
        tv_humidity = (TextView) findViewById(R.id.humidity_text);
        tv_windDeg = (TextView) findViewById(R.id.wind_deg_text);
        tv_windLevel = (TextView) findViewById(R.id.wind_level_text);
        tv_clouds = (TextView) findViewById(R.id.clouds_text);

        tv_meteorologicalTips = (TextView) findViewById(R.id.meteorological_tips);
        tv_cloundAndRainTips = (TextView) findViewById(R.id.clound_and_rain_tips);
        tv_springTips = (TextView) findViewById(R.id.spring_tips);
        tv_summaryTips = (TextView) findViewById(R.id.summary_tips);
        tv_autumnTips = (TextView) findViewById(R.id.autumn_tips);
        tv_winterTips = (TextView) findViewById(R.id.winter_tips);
        tv_proverbTips = (TextView) findViewById(R.id.proverb_tips);

        iv_meteorologicalLine = (ImageView) findViewById(R.id.meteorological_line);
        iv_cloundAndRainLine = (ImageView) findViewById(R.id.cloud_and_rain_line);
        iv_springLine = (ImageView) findViewById(R.id.spring_line);
        iv_summaryLine = (ImageView) findViewById(R.id.summary_line);
        iv_autumnLine = (ImageView) findViewById(R.id.autumn_line);
        iv_winterLine = (ImageView) findViewById(R.id.winter_line);

        tv_dayOne = (TextView) findViewById(R.id.day_one_text);
        tv_dayTwo = (TextView) findViewById(R.id.day_two_text);
        tv_dayThree = (TextView) findViewById(R.id.day_three_text);
        tv_dayFour = (TextView) findViewById(R.id.day_four_text);
        tv_dayFive = (TextView) findViewById(R.id.day_five_text);

        tv_dateOne = (TextView) findViewById(R.id.date_one);
        tv_dateTwo = (TextView) findViewById(R.id.date_two);
        tv_dateThree = (TextView) findViewById(R.id.date_three);
        tv_dateFour = (TextView) findViewById(R.id.date_four);
        tv_dateFive = (TextView) findViewById(R.id.date_five);

        iv_dayOne = (ImageView) findViewById(R.id.day_1);
        iv_dayTwo = (ImageView) findViewById(R.id.day_2);
        iv_dayThree = (ImageView) findViewById(R.id.day_3);
        iv_dayFour = (ImageView) findViewById(R.id.day_4);
        iv_dayFive = (ImageView) findViewById(R.id.day_5);

        iv_pressure = (ImageView) findViewById(R.id.pressure_icon);
        iv_temp = (ImageView) findViewById(R.id.temp_icon);

        ll_pressure = (LinearLayout) findViewById(R.id.pressure_layout);
        ll_temp = (LinearLayout) findViewById(R.id.temp_layout);

        tv_chooseTemp = (TextView) findViewById(R.id.choose_temp_text);
        tv_chooseHumidity = (TextView) findViewById(R.id.choose_humidity_text);
        tv_choosePressure = (TextView) findViewById(R.id.choose_pressure_text);
        tv_chooseWeather = (TextView) findViewById(R.id.choose_weather_text);
        tv_chooseWind = (TextView) findViewById(R.id.choose_wind_text);
        tv_chooseClound = (TextView) findViewById(R.id.choose_clound_text);

        ll_layoutDayOne = (LinearLayout) findViewById(R.id.layout_day_one);
        ll_layoutDayTwo = (LinearLayout) findViewById(R.id.layout_day_two);
        ll_layoutDayThree = (LinearLayout) findViewById(R.id.layout_day_three);
        ll_layoutDayFour = (LinearLayout) findViewById(R.id.layout_day_four);
        ll_layoutDayFive = (LinearLayout) findViewById(R.id.layout_day_five);

        tv_shareBtn = (TextView) findViewById(R.id.weather_share_btn);
        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);

        tv_meteorologicalTips.setOnClickListener(this);
        tv_cloundAndRainTips.setOnClickListener(this);
        tv_springTips.setOnClickListener(this);
        tv_summaryTips.setOnClickListener(this);
        tv_autumnTips.setOnClickListener(this);
        tv_winterTips.setOnClickListener(this);

        ll_pressure.setOnClickListener(this);
        ll_temp.setOnClickListener(this);

        ll_layoutDayOne.setOnClickListener(this);
        ll_layoutDayTwo.setOnClickListener(this);
        ll_layoutDayThree.setOnClickListener(this);
        ll_layoutDayFour.setOnClickListener(this);
        ll_layoutDayFive.setOnClickListener(this);

        tv_shareBtn.setOnClickListener(this);
        ll_leftBack.setOnClickListener(this);
    }

    public void initData() {

        UserBusinessController.getInstance().getFishPointWeather(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", getIntent().getIntExtra("mId", 0), new com.example.controller.controller.Listener<WeatherDetailBean>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("正在获取数据中,请稍候...");
            }

            @Override
            public void onComplete(WeatherDetailBean bean, Object... params) {
                dismissProgressDialog();


                dataBean = bean;

                tv_fishpointName.setText(getIntent().getStringExtra(
                        "fishPointName"));
                tv_weather.setText(bean.data.weather_current.cond);
                tv_time.setText(TimeUtil.formatTime(
                        System.currentTimeMillis(),
                        "yyyy/MM/dd HH:mm"));
                tv_pressure.setText(String
                        .valueOf(bean.data.weather_current.pressure) + "Ph");
                tv_temp.setText(String.valueOf(bean.data.weather_current.temp)
                        + "°C");
                tv_humidity.setText(String
                        .valueOf(bean.data.weather_current.humidity) + "%");
                tv_windDeg.setText(bean.data.weather_current.wind_deg_name);
                tv_windLevel.setText(bean.data.weather_current.wind_speed_name);
                tv_clouds.setText(String
                        .valueOf(bean.data.weather_current.clouds) + "%");
                tv_dayOne.setText("星期"
                        + TimeUtil.getWeek(TimeUtil.getFormatTime(TimeUtil
                        .getDateAfter(new Date(
                                        System.currentTimeMillis()),
                                0), "yyyy-MM-dd")));
                tv_dayTwo.setText("星期"
                        + TimeUtil.getWeek(TimeUtil.getFormatTime(TimeUtil
                        .getDateAfter(new Date(
                                        System.currentTimeMillis()),
                                1), "yyyy-MM-dd")));
                tv_dayThree.setText("星期"
                        + TimeUtil.getWeek(TimeUtil.getFormatTime(TimeUtil
                        .getDateAfter(new Date(
                                        System.currentTimeMillis()),
                                2), "yyyy-MM-dd")));
                tv_dayFour.setText("星期"
                        + TimeUtil.getWeek(TimeUtil.getFormatTime(TimeUtil
                        .getDateAfter(new Date(
                                        System.currentTimeMillis()),
                                3), "yyyy-MM-dd")));
                tv_dayFive.setText("星期"
                        + TimeUtil.getWeek(TimeUtil.getFormatTime(TimeUtil
                        .getDateAfter(new Date(
                                        System.currentTimeMillis()),
                                4), "yyyy-MM-dd")));

                tv_dateOne.setText(TimeUtil.getFormatTime(TimeUtil
                                .getDateAfter(new Date(
                                        System.currentTimeMillis()), 0),
                        "MM/dd"));
                tv_dateTwo.setText(TimeUtil.getFormatTime(TimeUtil
                                .getDateAfter(new Date(
                                        System.currentTimeMillis()), 1),
                        "MM/dd"));
                tv_dateThree.setText(TimeUtil.getFormatTime(TimeUtil
                                .getDateAfter(new Date(
                                        System.currentTimeMillis()), 2),
                        "MM/dd"));
                tv_dateFour.setText(TimeUtil.getFormatTime(TimeUtil
                                .getDateAfter(new Date(
                                        System.currentTimeMillis()), 3),
                        "MM/dd"));
                tv_dateFive.setText(TimeUtil.getFormatTime(TimeUtil
                                .getDateAfter(new Date(
                                        System.currentTimeMillis()), 4),
                        "MM/dd"));
                for (int i = 0; i < bean.data.weather_time.size(); i++) {
                    if (String.valueOf(bean.data.weather_time.get(i).date)
                            .length() == 1) {
                        dateString = "0" + bean.data.weather_time.get(i).date;
                    } else {
                        dateString = String.valueOf(bean.data.weather_time
                                .get(i).date);
                    }
                    if (dateString.equals(TimeUtil.getFormatTime(
                            TimeUtil.getDateAfter(new Date(
                                    System.currentTimeMillis()), 0),
                            "MM/dd").split("/")[1])) {
                        chooseDataBean.add(bean.data.weather_time.get(i));

                    }
                }

                tv_chooseTemp.setText(String
                        .valueOf(bean.data.weather_current.temp) + "°C");
                tv_chooseHumidity.setText(String
                        .valueOf(bean.data.weather_current.humidity) + "%");
                tv_choosePressure.setText(String
                        .valueOf(bean.data.weather_current.pressure) + "Ph");
                tv_chooseWeather.setText(bean.data.weather_current.cond);
                tv_chooseWind.setText(bean.data.weather_current.wind_deg_name);
                tv_chooseClound.setText(String
                        .valueOf(bean.data.weather_current.clouds) + "%");
                if (chooseDataBean != null && chooseDataBean.size() > 0) {
                    mLineChart = (LineChart) findViewById(R.id.chart1);
                    LineData mLineData = getLineData(chooseDataBean.size());
                    showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(WeatherDetailActivity.this, msg);
            }
        });
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.meteorological_tips:
                tv_proverbTips.setText(getResources().getString(
                        R.string.meteorological));
                ShowLine(R.id.meteorological_line);
                break;
            case R.id.clound_and_rain_tips:
                tv_proverbTips.setText(getResources().getString(
                        R.string.clouds_and_rain));
                ShowLine(R.id.cloud_and_rain_line);
                break;
            case R.id.spring_tips:
                tv_proverbTips.setText(getResources().getString(R.string.spring));
                ShowLine(R.id.spring_line);
                break;
            case R.id.summary_tips:
                tv_proverbTips.setText(getResources().getString(R.string.summary));
                ShowLine(R.id.summary_line);
                break;
            case R.id.autumn_tips:
                tv_proverbTips.setText(getResources().getString(R.string.autumn));
                ShowLine(R.id.autumn_line);
                break;
            case R.id.winter_tips:
                tv_proverbTips.setText(getResources().getString(R.string.winter));
                ShowLine(R.id.winter_line);
                break;
            case R.id.layout_day_one:
                ShowDayLine(R.id.day_1);
                chooseDataBean.clear();
                mTempMax = 0;
                mTempMin = 0;
                mPressureMax = 0;
                mPressureMin = 0;
                dateString = "";
                for (int i = 0; i < dataBean.data.weather_time.size(); i++) {
                    if (String.valueOf(dataBean.data.weather_time.get(i).date)
                            .length() == 1) {
                        dateString = "0" + dataBean.data.weather_time.get(i).date;
                    } else {
                        dateString = String.valueOf(dataBean.data.weather_time
                                .get(i).date);
                    }
                    if (dateString.equals(TimeUtil.getFormatTime(
                            TimeUtil.getDateAfter(new Date(
                                    System.currentTimeMillis()), 0),
                            "MM/dd").split("/")[1])) {
                        chooseDataBean.add(dataBean.data.weather_time.get(i));

                    }
                }

                LineData mLineData = getLineData(chooseDataBean.size());
                showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
                break;
            case R.id.layout_day_two:
                ShowDayLine(R.id.day_2);
                chooseDataBean.clear();
                mTempMax = 0;
                mTempMin = 0;
                mPressureMax = 0;
                mPressureMin = 0;
                dateString = "";
                for (int i = 0; i < dataBean.data.weather_time.size(); i++) {
                    if (String.valueOf(dataBean.data.weather_time.get(i).date)
                            .length() == 1) {
                        dateString = "0" + dataBean.data.weather_time.get(i).date;
                    } else {
                        dateString = String.valueOf(dataBean.data.weather_time
                                .get(i).date);
                    }
                    if (dateString.equals(TimeUtil.getFormatTime(
                            TimeUtil.getDateAfter(new Date(
                                    System.currentTimeMillis()), 1),
                            "MM/dd").split("/")[1])) {
                        chooseDataBean.add(dataBean.data.weather_time.get(i));

                    }
                }
                mLineData = getLineData(chooseDataBean.size());
                showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
                break;
            case R.id.layout_day_three:
                ShowDayLine(R.id.day_3);
                chooseDataBean.clear();
                mTempMax = 0;
                mTempMin = 0;
                mPressureMax = 0;
                mPressureMin = 0;
                dateString = "";
                for (int i = 0; i < dataBean.data.weather_time.size(); i++) {
                    if (String.valueOf(dataBean.data.weather_time.get(i).date)
                            .length() == 1) {
                        dateString = "0" + dataBean.data.weather_time.get(i).date;
                    } else {
                        dateString = String.valueOf(dataBean.data.weather_time
                                .get(i).date);
                    }
                    if (dateString.equals(TimeUtil.getFormatTime(
                            TimeUtil.getDateAfter(new Date(
                                    System.currentTimeMillis()), 2),
                            "MM/dd").split("/")[1])) {
                        chooseDataBean.add(dataBean.data.weather_time.get(i));

                    }
                }
                mLineData = getLineData(chooseDataBean.size());
                showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
                break;
            case R.id.layout_day_four:
                ShowDayLine(R.id.day_4);
                chooseDataBean.clear();
                mTempMax = 0;
                mTempMin = 0;
                mPressureMax = 0;
                mPressureMin = 0;
                dateString = "";
                for (int i = 0; i < dataBean.data.weather_time.size(); i++) {
                    if (String.valueOf(dataBean.data.weather_time.get(i).date)
                            .length() == 1) {
                        dateString = "0" + dataBean.data.weather_time.get(i).date;
                    } else {
                        dateString = String.valueOf(dataBean.data.weather_time
                                .get(i).date);
                    }
                    if (dateString.equals(TimeUtil.getFormatTime(
                            TimeUtil.getDateAfter(new Date(
                                    System.currentTimeMillis()), 3),
                            "MM/dd").split("/")[1])) {
                        chooseDataBean.add(dataBean.data.weather_time.get(i));

                    }
                }
                mLineData = getLineData(chooseDataBean.size());
                showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
                break;
            case R.id.layout_day_five:
                ShowDayLine(R.id.day_5);
                chooseDataBean.clear();
                mTempMax = 0;
                mTempMin = 0;
                mPressureMax = 0;
                mPressureMin = 0;
                dateString = "";
                for (int i = 0; i < dataBean.data.weather_time.size(); i++) {
                    if (String.valueOf(dataBean.data.weather_time.get(i).date)
                            .length() == 1) {
                        dateString = "0" + dataBean.data.weather_time.get(i).date;
                    } else {
                        dateString = String.valueOf(dataBean.data.weather_time
                                .get(i).date);
                    }
                    if (dateString.equals(TimeUtil.getFormatTime(
                            TimeUtil.getDateAfter(new Date(
                                    System.currentTimeMillis()), 4),
                            "MM/dd").split("/")[1])) {
                        chooseDataBean.add(dataBean.data.weather_time.get(i));

                    }
                }
                mLineData = getLineData(chooseDataBean.size());
                showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
                break;
            case R.id.temp_layout:
                isTemp = true;
                iv_temp.setBackgroundResource(R.drawable.weather_green_ball);
                iv_pressure.setBackgroundResource(R.drawable.weather_white_boll);
                LineData mLineDataTemp = getLineData(chooseDataBean.size());
                showChart(mLineChart, mLineDataTemp, Color.rgb(114, 188, 223));
                break;
            case R.id.pressure_layout:
                isTemp = false;
                iv_temp.setBackgroundResource(R.drawable.weather_white_boll);
                iv_pressure.setBackgroundResource(R.drawable.weather_green_ball);
                LineData mLineDataPressure = getLineData(chooseDataBean.size());
                showChart(mLineChart, mLineDataPressure, Color.rgb(114, 188, 223));
                break;
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.weather_share_btn:
                // 弹出分享
                dialog_share = new ShareDialogUtils(this, R.style.dialog,
                        uiHandler, true);
                dialog_share.setCanceledOnTouchOutside(true);
                dialog_share.show();
                break;
            default:
                break;
        }
    }

    public void ShowLine(int id) {
        iv_meteorologicalLine.setVisibility(View.INVISIBLE);
        iv_cloundAndRainLine.setVisibility(View.INVISIBLE);
        iv_springLine.setVisibility(View.INVISIBLE);
        iv_summaryLine.setVisibility(View.INVISIBLE);
        iv_autumnLine.setVisibility(View.INVISIBLE);
        iv_winterLine.setVisibility(View.INVISIBLE);
        switch (id) {

            case R.id.meteorological_line:
                iv_meteorologicalLine.setVisibility(View.VISIBLE);
                break;
            case R.id.cloud_and_rain_line:
                iv_cloundAndRainLine.setVisibility(View.VISIBLE);
                break;
            case R.id.spring_line:
                iv_springLine.setVisibility(View.VISIBLE);
                break;
            case R.id.summary_line:
                iv_summaryLine.setVisibility(View.VISIBLE);
                break;
            case R.id.autumn_line:
                iv_autumnLine.setVisibility(View.VISIBLE);
                break;
            case R.id.winter_line:
                iv_winterLine.setVisibility(View.VISIBLE);
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
            String mTitle = "去哪里钓鱼,很好玩，你也一起来吧！";
            String shareUrl = "";
            switch (msg.what) {
                case 0:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(WeatherDetailActivity.this), 0);
                    break;
                case 1:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(WeatherDetailActivity.this), 1);
                    break;
                case 2:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(WeatherDetailActivity.this), 2);
                    break;
                case 3:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(WeatherDetailActivity.this), 3);
                    break;
                case 4:
                    dialog_share.dismiss();
                    selectSharePaltform(mTitle + shareUrl,
                            takeScreenShot(WeatherDetailActivity.this), 4);
                    break;
            }

        }
    }

    /**
     * 分享平台的选择
     *
     * @param position
     */
    public void selectSharePaltform(String content, Bitmap bitmap, int position) {
        dialog_share.startShare(content, bitmap, position);
    }

    // 获取指定Activity的截屏，保存到png文件
    private Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public void ShowDayLine(int id) {
        iv_dayOne.setVisibility(View.INVISIBLE);
        iv_dayTwo.setVisibility(View.INVISIBLE);
        iv_dayThree.setVisibility(View.INVISIBLE);
        iv_dayFour.setVisibility(View.INVISIBLE);
        iv_dayFive.setVisibility(View.INVISIBLE);
        switch (id) {

            case R.id.day_1:
                iv_dayOne.setVisibility(View.VISIBLE);
                break;
            case R.id.day_2:
                iv_dayTwo.setVisibility(View.VISIBLE);
                break;
            case R.id.day_3:
                iv_dayThree.setVisibility(View.VISIBLE);
                break;
            case R.id.day_4:
                iv_dayFour.setVisibility(View.VISIBLE);
                break;
            case R.id.day_5:
                iv_dayFive.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        // lineChart.setDrawBorders(false); // 是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart
                .setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); //
        // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        lineChart.setDoubleTapToZoomEnabled(false);// 设置是否可以通过双击屏幕放大图表。默认是true
        lineChart.setDragEnabled(false);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);//
        // lineChart.setHighlightEnabled(true);
        lineChart.setDragEnabled(true);

        for (int i = 0; i < chooseDataBean.size(); i++) {
            if (isTemp) {
                if (chooseDataBean.get(i).temp > mTempMax) {
                    mTempMax = chooseDataBean.get(i).temp;
                    if (i == 0) {
                        mTempMin = chooseDataBean.get(i).temp;
                    }
                } else if (chooseDataBean.get(i).temp < mTempMin) {
                    mTempMin = chooseDataBean.get(i).temp;
                }
            } else {
                if (chooseDataBean.get(i).pressure > mPressureMax) {
                    mPressureMax = chooseDataBean.get(i).pressure;
                    if (i == 0) {
                        mPressureMin = chooseDataBean.get(i).pressure;
                    }
                } else if (chooseDataBean.get(i).pressure < mPressureMin) {
                    mPressureMin = chooseDataBean.get(i).pressure;
                }
            }

        }

        YAxis leftAxis = lineChart.getAxisLeft();
        // leftAxis.setEnabled(false);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawAxisLine(isFinishing());
        rightAxis.setEnabled(false);
        if (isTemp) {
            // Y坐标轴最大值
            leftAxis.setAxisMaxValue(mTempMax + 5);
            // Y坐标轴最小值
            leftAxis.setAxisMinValue(mTempMin - 5);

        } else {
            // Y坐标轴最大值
            leftAxis.setAxisMaxValue((int)mPressureMax + 5);
            // Y坐标轴最小值
            leftAxis.setAxisMinValue((int)mPressureMin - 5);

        }

        leftAxis.setStartAtZero(false);

        leftAxis.setDrawLabels(true);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,
                isTemp);
        // set the marker to the chart
        lineChart.setMarkerView(mv);
        lineChart.setBackgroundColor(color);// 设置背景
        // add data
        lineChart.setData(lineData); // 设置数据
        lineChart.animateX(2000); // 立即执行的动画,x轴

        Legend mLegend = lineChart.getLegend();
        mLegend.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        // 将X坐标轴的标尺刻度移动底部。
        xAxis.setPosition(XAxisPosition.BOTTOM);
        // X轴之间数值的间隔
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setEnabled(true); // 是否显示X坐标轴 及 对应的刻度竖线，默认是true
        xAxis.setDrawAxisLine(true); // 是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxis.setDrawGridLines(true); // 是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setDrawLabels(true); // 是否显示X坐标轴上的刻度，默认是true

    }

    /**
     * 生成一个数据
     *
     * @param count 表示图表中有多少个坐标点
     * @return
     */
    private LineData getLineData(int count) {
        ArrayList<String> xValues = new ArrayList<String>();

        xValues.add("2点");
        xValues.add("5点");
        xValues.add("8点");
        xValues.add("11点");
        xValues.add("14点");
        xValues.add("17点");
        xValues.add("20点");
        xValues.add("23点");

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        if (isTemp) {
            yValues.clear();
            for (int i = 0; i < count; i++) {
                yValues.add(new Entry(chooseDataBean.get(i).temp, i));
            }
        } else {
            yValues.clear();
            for (int i = 0; i < count; i++) {
                yValues.add(new Entry((int) chooseDataBean.get(i).pressure, i));
            }
        }

        tv_chooseTemp
                .setText(String.valueOf(chooseDataBean.get(0).temp) + "°C");
        tv_chooseHumidity
                .setText(String.valueOf(chooseDataBean.get(0).humidity) + "%");
        tv_choosePressure
                .setText(String.valueOf(chooseDataBean.get(0).pressure) + "Ph");
        tv_chooseWeather.setText(chooseDataBean.get(0).cond);
        tv_chooseWind.setText(chooseDataBean.get(0).wind_deg_name);
        tv_chooseClound.setText(String.valueOf(chooseDataBean.get(0).clouds)
                + "%");

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, null);

        // 用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(getResources().getColor(R.color.green_3be74f));// 显示颜色
        lineDataSet.setCircleColor(getResources()
                .getColor(R.color.green_3be74f));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE & 0x70FFFFFF); // 高亮的线的颜色
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawValues(false); // 设置不显示折线上的值
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
        LineData lineData = new LineData(xValues, lineDataSets);
        lineData.setValueTextSize(5f);
        return lineData;
    }

    class MyMarkerView extends MarkerView {

        private TextView tvContent;
        private boolean isTemp;

        public MyMarkerView(Context context, int layoutResource, boolean isTemp) {
            super(context, layoutResource);
            this.isTemp = isTemp;
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update
        // the
        // content (user-interface)

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            tv_chooseTemp.setText(String.valueOf(chooseDataBean.get(e
                    .getXIndex()).temp) + "°C");
            tv_chooseHumidity.setText(String.valueOf(chooseDataBean.get(e
                    .getXIndex()).humidity) + "%");
            tv_choosePressure.setText(String.valueOf(chooseDataBean.get(e
                    .getXIndex()).pressure) + "Ph");
            tv_chooseWeather.setText(chooseDataBean.get(e.getXIndex()).cond);
            tv_chooseWind
                    .setText(chooseDataBean.get(e.getXIndex()).wind_deg_name);
            tv_chooseClound.setText(String.valueOf(chooseDataBean.get(e
                    .getXIndex()).clouds) + "%");

            if (e instanceof CandleEntry) {

                CandleEntry ce = (CandleEntry) e;
                if (isTemp) {
                    tvContent.setText("温度:"
                            + Utils.formatNumber(ce.getHigh(), 0, true) + "°C");
                } else {
                    tvContent.setText("气压:"
                            + Utils.formatNumber(ce.getHigh(), 0, true) + "Ph");
                }

            } else {
                if (isTemp) {
                    tvContent.setText("温度:"
                            + Utils.formatNumber(e.getVal(), 0, true) + "°C");
                } else {
                    tvContent.setText("气压:"
                            + Utils.formatNumber(e.getVal(), 0, true) + "Ph");
                }
            }
        }

        @Override
        public int getXOffset(float xpos) {
            // TODO Auto-generated method stub
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset(float ypos) {
            // TODO Auto-generated method stub
            return -getHeight();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
