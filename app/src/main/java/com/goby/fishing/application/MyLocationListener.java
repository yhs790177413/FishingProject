package com.goby.fishing.application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;

import android.text.TextUtils;

public class MyLocationListener implements BDLocationListener {

	@Override
	public void onReceiveLocation(final BDLocation location) {
		if (location == null) {
			return;
		}
		double y = location.getLatitude();
		double x = location.getLongitude();
		String city = location.getCity();
		String province = location.getProvince();
		String cityCode = location.getCityCode();
		String address = location.getAddrStr();
		// if (city != null && city.endsWith("市")) {
		// city = city.substring(0, city.length() - 1);
		// }
		SharedPreferenceUtil sharedPreference = new SharedPreferenceUtil(
				FishingApplication.getContext());
		if (!TextUtils.isEmpty(String.valueOf(x))) {
			sharedPreference.setGPSLongitude(String.valueOf(x));
		}
		if (!TextUtils.isEmpty(String.valueOf(y))) {
			sharedPreference.setGPSLatitude(String.valueOf(y));
		}
		if (!TextUtils.isEmpty(city)) {
			sharedPreference.setGPSCity(city);
		}
		if (!TextUtils.isEmpty(address)) {
			sharedPreference.setLocationAddress(address);
		}
		BDLocationClient.stopLocation();
		BDLocationClient.unRegisterLocationListener();
	}

}
