package com.goby.fishing.application;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.content.Context;


public class BDLocationClient {

	private static LocationClient mLocationClient = null;
	private static BDLocationListener myListener = new MyLocationListener();
	public static final String BAIDU_API_KEY = "tCLTtZdEsGIbpFQEMKvNuziF";

	/**
	 * @return
	 */
	public static LocationClient getInstance() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(FishingApplication.getContext());
			initBaiduLocation();
		}
		return mLocationClient;
	}

	private static void initBaiduLocation() {
		// mLocationClient.setAK(BAIDU_API_KEY);
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
//		option.setPoiNumber(5); // 最多返回POI个数
//		option.setPoiDistance(1000); // poi查询距离
//		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	/**
	 * 请求定位
	 */
	public static void requestLocation() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		} else {
			mLocationClient.start();

		}
	}

/*	public static void requestPoi() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestPoi();
		} else {
			mLocationClient.start();

		}
	}*/

	public static void unRegisterLocationListener() {
		if (myListener != null && mLocationClient != null) {
			mLocationClient.unRegisterLocationListener(myListener);
		}
	}
	
	public static void stopLocation(){
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}
}
