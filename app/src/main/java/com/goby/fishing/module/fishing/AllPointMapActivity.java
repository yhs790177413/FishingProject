package com.goby.fishing.module.fishing;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.goby.fishing.R;
import com.example.controller.bean.FishingListBean.Data.List;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.DialogBuilder;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class AllPointMapActivity extends Activity implements
		OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter,
		OnMapClickListener, OnCameraChangeListener, OnClickListener,
		LocationSource, AMapLocationListener, OnCheckedChangeListener {
	private MapView mapView;
	private ImageView iv_gpsMap;
	private ImageView iv_myLocation;
	private AMap aMap;
	private Marker mCurrentMarker;
	private View view;
	private SharedPreferenceUtil sharedPreferenceUtil;
	private ArrayList<List> mDataList = new ArrayList<List>();
	private int mId;
	private String mPic_url;
	private boolean isChange = true;

	public static void launch(Context activity, ArrayList<List> dataList) {

		Intent intent = new Intent(activity, AllPointMapActivity.class);
		intent.putExtra("dataList", dataList);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_point);
		mDataList.addAll((ArrayList<List>) getIntent().getSerializableExtra(
				"dataList"));
		LinearLayout ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(this);
		sharedPreferenceUtil = new SharedPreferenceUtil(this);
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
		iv_gpsMap = (ImageView) findViewById(R.id.gps_map);
		iv_myLocation = (ImageView) findViewById(R.id.my_location);
		iv_gpsMap.setOnClickListener(this);
		iv_myLocation.setOnClickListener(this);
		aMap.getUiSettings().setZoomControlsEnabled(false);
		LatLng marker1 = new LatLng(mDataList.get(0).latitude,
				mDataList.get(0).longitude);
		// 设置中心点和缩放比例
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		for (int i = 0; i < mDataList.size(); i++) {
			LatLng marker = new LatLng(mDataList.get(i).latitude,
					mDataList.get(i).longitude);
			// 在地图上添加标记
			MarkerOptions markerOption = new MarkerOptions()
					.anchor(0.5f, 0f)
					.position(marker)
					.title(mDataList.get(i).name)
					.snippet(mDataList.get(i).summary)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.redpoint)).draggable(true);
			aMap.addMarker(markerOption);
		}
		aMap.getUiSettings().setScaleControlsEnabled(true); 
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);
		aMap.setOnMapClickListener(this);
		aMap.setOnCameraChangeListener(this);

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
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (view == null) {
			mCurrentMarker = marker;
			// getInfoWindow(marker);
			marker.showInfoWindow();
		}

		return false;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = getLayoutInflater().inflate(
				R.layout.infowindow_layout, null);
		LatLng lat = marker.getPosition();
		for (int i = 0; i < mDataList.size(); i++) {
			if (mDataList.get(i).latitude == lat.latitude
					|| mDataList.get(i).longitude == lat.longitude) {
				render(marker, infoContent, mDataList.get(i).pic_url,
						mDataList.get(i).name, mDataList.get(i).summary,
						mDataList.get(i).id);
			}
		}
		return infoContent;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		if (mCurrentMarker != null) {
			view = null;
			mCurrentMarker.hideInfoWindow();
		}
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
		case R.id.daohang_view:
			DialogBuilder dialog = new DialogBuilder(this, "center");
			dialog.setItems(R.array.map,
					new DialogBuilder.OnDialogItemClickListener() {

						@Override
						public void OnDialogItemClick(Context context,
								DialogBuilder builder, Dialog dialog,
								int position) {
							if (position == 0) {
								try {
									Intent intent = Intent
											.getIntent("intent://map/direction?origin=latlng:"
													+ getIntent()
															.getDoubleExtra(
																	"latitude",
																	0.0)
													+ ","
													+ getIntent()
															.getDoubleExtra(
																	"longitude",
																	0.0)
													+ "|name:出发地&destination="
													+ sharedPreferenceUtil
															.getLocationAddress()
													+ "&mode=driving®ion="
													+ getIntent()
															.getStringExtra(
																	"fishPointName")
													+ "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
									if (isInstallByread("com.baidu.BaiduMap")) {
										startActivity(intent); // 启动调用

									} else {
										ToastHelper.showToast(
												AllPointMapActivity.this,
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
											AllPointMapActivity.this,
											"没有安装高德地图客户端");
								}
							}
						}
					});
			dialog.create().show();
			break;
		case R.id.parent_layout:
			FishingDetailActivity.launch(this, mId, -1, null, mPic_url);
			break;
		case R.id.left_back_layout:
			finish();
			break;
		case R.id.gps_map:
			if (isChange) {
				isChange = !isChange;
				iv_gpsMap.setBackgroundResource(R.drawable.norma_map);
				aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
			} else {
				isChange = !isChange;
				iv_gpsMap.setBackgroundResource(R.drawable.gps_map);
				aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
			}

			break;
		case R.id.my_location:
			// 设置中心点和缩放比例
			LatLng marker1 = new LatLng(Double.parseDouble(sharedPreferenceUtil
					.getGPSLatitude()), Double.parseDouble(sharedPreferenceUtil
					.getGPSLongitude()));
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
			aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
			MarkerOptions markerOption = new MarkerOptions()
					.anchor(0.5f, 0.5f)
					.position(marker1)
					.title("")
					.snippet("")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.bulepoint))
					.draggable(true);
			markerOption.setFlat(true);
			aMap.addMarker(markerOption);
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
	public void render(Marker marker, View view, String mPic_url,
			String mPointName, String mPointInfo, int id) {
		LinearLayout ll_parent = (LinearLayout) view
				.findViewById(R.id.parent_layout);
		ll_parent.setOnClickListener(this);
		ImageView icv_markerIcon = (ImageView) view
				.findViewById(R.id.marker_icon);
		TextView tv_pointName = (TextView) view.findViewById(R.id.point_name);
		TextView tv_pointContent = (TextView) view
				.findViewById(R.id.point_info);
		ImageLoaderWrapper.getDefault().displayImage(mPic_url, icv_markerIcon);
		tv_pointName.setText(mPointName);
		tv_pointContent.setText(mPointInfo);
		mId = id;
		mPic_url = mPic_url;
	}

}
