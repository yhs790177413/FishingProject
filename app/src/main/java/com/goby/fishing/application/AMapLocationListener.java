package com.goby.fishing.application;

import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;

public class AMapLocationListener implements
		com.amap.api.location.AMapLocationListener {

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
				// 定位成功回调信息，设置相关消息
				amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
				amapLocation.getLatitude();// 获取纬度
				amapLocation.getLongitude();// 获取经度
				amapLocation.getAccuracy();// 获取精度信息
				// SimpleDateFormat df = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// Date date = new Date(amapLocation.getTime());
				// df.format(date);//定位时间
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
				sharedPreferenceUtil.setLocationAddress(amapLocation
						.getAddress());
				sharedPreferenceUtil.setGPSCity(amapLocation.getCity());
				sharedPreferenceUtil.setGPSLatitude(String.valueOf(amapLocation
						.getLatitude()));
				sharedPreferenceUtil.setGPSLongitude(String
						.valueOf(amapLocation.getLongitude()));
				// 停止定位服务
				if(FishingApplication.mLocationClient!=null){
					FishingApplication.mLocationClient.stopLocation();
				}
			} else {
				// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				Log.e("AmapError",
						"location Error, ErrCode:"
								+ amapLocation.getErrorCode() + ", errInfo:"
								+ amapLocation.getErrorInfo());
			}
		}
	}

}
