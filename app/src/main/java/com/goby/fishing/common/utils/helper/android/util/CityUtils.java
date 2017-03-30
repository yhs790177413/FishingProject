package com.goby.fishing.common.utils.helper.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.controller.bean.CityListBean.Data.City;
import com.goby.fishing.common.widget.imageview.IOStreamManager;

public class CityUtils {

	public static int getCityIdByName(ArrayList<City> cityList, String cityName) {

		int cityId = -1;
		for (int i = 0; i < cityList.size(); i++) {
			if(!TextUtils.isEmpty(cityList.get(i).name)){
				if (cityList.get(i).name.contains(cityName)
						|| cityName.contains(cityList.get(i).name)) {
					cityId = cityList.get(i).id;
				}
			}
		}
		return cityId;
	}
}
