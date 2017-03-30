package com.goby.fishing.module.fishing;

import java.io.File;
import java.net.URISyntaxException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.goby.fishing.R;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.DialogBuilder;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;

/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class BasicMapActivity extends Activity implements AMap.OnMapClickListener,
		AMap.OnCameraChangeListener, OnClickListener, LocationSource,
		AMapLocationListener, OnCheckedChangeListener {
	private MapView mapView;
	private AMap aMap;
	private String mInfo;
	private String mPic_url;
	private Marker mCurrentMarker;
	private View view;
	private ImageView iv_map;
	private ImageView iv_otherIcon;
	private TextView tv_rightTitle;
	private SharedPreferenceUtil sharedPreferenceUtil;
	private boolean mapChange = true;
	private String otherChange = "fish_location";
	private boolean other_flag = false;

	public static void launch(Activity activity, double longitude,
			double latitude, String info, String pic_url, String fishPointName) {

		Intent intent = new Intent(activity, BasicMapActivity.class);
		intent.putExtra("longitude", longitude);
		intent.putExtra("latitude", latitude);
		intent.putExtra("info", info);
		intent.putExtra("pic_url", pic_url);
		intent.putExtra("fishPointName", fishPointName);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basicmap_activity);

		LinearLayout ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(this);
		sharedPreferenceUtil = new SharedPreferenceUtil(this);
		mInfo = getIntent().getStringExtra("info");
		mPic_url = getIntent().getStringExtra("pic_url");
		/*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 方法必须重写

		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null)
			aMap = mapView.getMap();

		aMap.getUiSettings().setZoomControlsEnabled(false);
		LatLng marker1 = new LatLng(
				getIntent().getDoubleExtra("latitude", 0.0), getIntent()
						.getDoubleExtra("longitude", 0.0));
		// 设置中心点和缩放比例
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		// 在地图上添加标记
		MarkerOptions markerOption = new MarkerOptions()
				.anchor(0.5f, 0f)
				.position(marker1)
				.title(mInfo)
				.snippet(mInfo)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.redpoint))
				.draggable(true);
		markerOption.setFlat(true);
		aMap.getUiSettings().setScaleControlsEnabled(true);
		mCurrentMarker = aMap.addMarker(markerOption);
		// mCurrentMarker.showInfoWindow();
		// aMap.setOnMapClickListener(this);
		aMap.setOnCameraChangeListener(this);

		iv_map = (ImageView) findViewById(R.id.normal_map);
		iv_otherIcon = (ImageView) findViewById(R.id.other_icon);
		iv_map.setOnClickListener(this);
		iv_otherIcon.setOnClickListener(this);
		tv_rightTitle = (TextView) findViewById(R.id.right_title);
		tv_rightTitle.setOnClickListener(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		// if (mCurrentMarker != null) {
		// view = null;
		// mCurrentMarker.hideInfoWindow();
		// }
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		// 拖动地图
		// if (mCurrentMarker != null) {
		// view = null;
		// mCurrentMarker.hideInfoWindow();
		// }
	}

	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {
		// TODO Auto-generated method stub
		// 拖动地图完成
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.normal_map:
			if (mapChange) {
				aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
				mapChange = !mapChange;
				iv_map.setBackgroundResource(R.drawable.norma_map);
			} else {
				aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
				mapChange = !mapChange;
				iv_map.setBackgroundResource(R.drawable.gps_map);
			}
			break;
		case R.id.other_icon:
			if (otherChange.equals("fish_location")) {
				iv_otherIcon.setBackgroundResource(R.drawable.nomal_icon);
				otherChange = "my_location";
				if (!TextUtils.isEmpty(sharedPreferenceUtil.getGPSLatitude())) {
					LatLng marker2 = new LatLng(
							Double.parseDouble(sharedPreferenceUtil
									.getGPSLatitude()),
							Double.parseDouble(sharedPreferenceUtil
									.getGPSLongitude()));
					// 设置中心点和缩放比例
					aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker2));
					MarkerOptions markerOption = new MarkerOptions()
							.anchor(0.5f, 0.5f)
							.position(marker2)
							.title(mInfo)
							.snippet(mInfo)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.bulepoint))
							.draggable(true);
					markerOption.setFlat(true);
					aMap.addMarker(markerOption);
				}
			} else if (otherChange.equals("my_location")) {
				iv_otherIcon.setBackgroundResource(R.drawable.my_location);
				otherChange = "other";
				other_flag = !other_flag;
				if (other_flag) {
					aMap.getUiSettings().setCompassEnabled(other_flag);
				}

			} else if (otherChange.equals("other")) {
				iv_otherIcon.setBackgroundResource(R.drawable.other_icon);
				otherChange = "fish_location";
				LatLng marker1 = new LatLng(getIntent().getDoubleExtra(
						"latitude", 0.0), getIntent().getDoubleExtra(
						"longitude", 0.0));
				// 设置中心点和缩放比例
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
			}
			break;
		case R.id.right_title:
			DialogBuilder dialog = new DialogBuilder(this, "center");
			dialog.setItems(R.array.map,
					new DialogBuilder.OnDialogItemClickListener() {

						@Override
						public void OnDialogItemClick(Context context,
								DialogBuilder builder, Dialog dialog,
								int position) {
							if (position == 0) {
								try {
									Intent intent = Intent.getIntent("intent://map/direction?origin="
											+ sharedPreferenceUtil
													.getLocationAddress()
											+ "&destination="
											+ getIntent().getStringExtra(
													"fishPointName")
											+ "&mode=driving®ion="
											+ sharedPreferenceUtil
													.getLocationAddress()
											+ "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
									if (isInstallByread("com.baidu.BaiduMap")) {
										startActivity(intent); // 启动调用

									} else {
										ToastHelper.showToast(
												BasicMapActivity.this,
												"没有安装百度地图客户端");
									}
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
							} else {
								if (isInstallByread("com.autonavi.minimap")) {
									Intent intentOther = new Intent(
											"android.intent.action.VIEW",
											android.net.Uri
													.parse("androidamap://navi?sourceApplication=amap&lat="
															+ getIntent()
																	.getDoubleExtra(
																			"latitude",
																			0.0)
															+ "&lon="
															+ getIntent()
																	.getDoubleExtra(
																			"longitude",
																			0.0)
															+ "&dev=1&stype=0"));
									intentOther
											.setPackage("com.autonavi.minimap");
									startActivity(intentOther);

								} else {
									ToastHelper.showToast(
											BasicMapActivity.this,
											"没有安装高德地图客户端");
								}
							}
						}
					});
			dialog.create().show();
			break;
		case R.id.parent_layout:
			finish();
			break;
		case R.id.left_back_layout:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
	}

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	private boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		LinearLayout ll_parent = (LinearLayout) view
				.findViewById(R.id.parent_layout);
		ll_parent.setOnClickListener(this);
		ImageView icv_markerIcon = (ImageView) view
				.findViewById(R.id.marker_icon);
		TextView tv_marker = (TextView) view.findViewById(R.id.marker_info);
		ImageLoaderWrapper.getDefault().displayImage(mPic_url, icv_markerIcon);
		tv_marker.setText(mInfo);
		ImageView iv_danHang = (ImageView) view.findViewById(R.id.daohang_view);
		iv_danHang.setOnClickListener(this);
	}

}
