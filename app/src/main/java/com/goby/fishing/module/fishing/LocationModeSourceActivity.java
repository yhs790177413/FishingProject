package com.goby.fishing.module.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.goby.fishing.R;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.module.login.LoginActivity;

public class LocationModeSourceActivity extends Activity implements
		OnCameraChangeListener, OnClickListener {
	private AMap aMap;
	private MapView mapView;
	private ImageView iv_gpsMap;
	private ImageView iv_myLocation;
	private LinearLayout ll_leftBack;
	private Button btn_position;
	private TextView tv_unAdd;
	private boolean isChange = true;
	private double longitude;
	private double latitude;
	private double chooseLongitude;
	private double chooseLatitude;
	private SharedPreferenceUtil sharedPreferenceUtil;

	public static void launch(Activity activity, String name, double longitude,
			double latitude, int fishPointId) {

		Intent intent = new Intent(activity, LocationModeSourceActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("longitude", longitude);
		intent.putExtra("latitude", latitude);
		intent.putExtra("fishPointId", fishPointId);
		activity.startActivity(intent);
	}

	public static void launch(Activity activity, int requestCode,
			double longitude, double latitude) {

		Intent intent = new Intent(activity, LocationModeSourceActivity.class);
		intent.putExtra("requestCode", requestCode);
		intent.putExtra("longitude", longitude);
		intent.putExtra("latitude", latitude);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationmodesource_activity);

		sharedPreferenceUtil = new SharedPreferenceUtil(this);
		longitude = getIntent().getDoubleExtra("longitude", -1);
		latitude = getIntent().getDoubleExtra("latitude", -1);
		Log.d("map", longitude+"=="+latitude);
		ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
		ll_leftBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {

		iv_gpsMap = (ImageView) findViewById(R.id.gps_map);
		iv_myLocation = (ImageView) findViewById(R.id.my_location);
		btn_position = (Button) findViewById(R.id.check_position);
		tv_unAdd = (TextView) findViewById(R.id.unadd);

		iv_gpsMap.setOnClickListener(this);
		iv_myLocation.setOnClickListener(this);
		btn_position.setOnClickListener(this);
		tv_unAdd.setOnClickListener(this);

		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {

		aMap.getUiSettings().setZoomControlsEnabled(false);

		// 设置中心点和缩放比例
		LatLng marker1 = new LatLng(latitude, longitude);
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		aMap.getUiSettings().setScaleControlsEnabled(true);
		Marker marker = aMap
				.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
						.fromResource(R.drawable.redpoint)));
		WindowManager wm = this.getWindowManager();

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		marker.setPositionByPixels(width / 2, height / 2);
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
	public void onCameraChange(CameraPosition position) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCameraChangeFinish(CameraPosition position) {
		// TODO Auto-generated method stub
		LatLng target = position.target;
		chooseLongitude = target.longitude;
		chooseLatitude = target.latitude;
		Log.d("target-----", chooseLongitude+"=="+chooseLatitude);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
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
			LatLng marker1 = new LatLng(latitude, longitude);
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
			aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
			break;
		case R.id.check_position:
			if (TextUtils.isEmpty(getIntent().getStringExtra("name"))) {
				Intent intent = new Intent();
				intent.putExtra("chooseLongitude", chooseLongitude);
				intent.putExtra("chooseLatitude", chooseLatitude);
				setResult(RESULT_OK, intent);
				Log.d("map-----", chooseLongitude+"=="+chooseLatitude);
				finish();
			} else {
				if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
					LoginActivity.launch(this, "meFragment", 0);
				} else {
					AddFooterprintActivity.launch(this, getIntent()
							.getStringExtra("name"), chooseLongitude,
							chooseLatitude,
							getIntent().getIntExtra("fishPointId", 0));
				}
				finish();
			}
		case R.id.unadd:
			Intent intent = new Intent();
			intent.putExtra("chooseLongitude", longitude);
			intent.putExtra("chooseLatitude", latitude);
			setResult(RESULT_OK, intent);
			finish();

			break;
		default:
			break;
		}
	}

	public void backPressed() {
		finish();
	}

	// 监听返回键事件
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		backPressed();
	}
}
