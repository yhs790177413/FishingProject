package com.goby.fishing.common.utils.helper.android.util;

import java.util.ArrayList;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 
 * @ClassName: SharedPreferenceUtil
 * @Description: TODO sharedPreference的工具类，用到sharedpreference直接调用里面的方法
 *               如果要在sharedpreference里面增加新的变量，这里增加一个set和get方法就行， 调用方法：
 *               application.spUtil.setXXX, / getXXX
 * @author yhs
 * 
 */
@SuppressLint("NewApi")
public class SharedPreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private final String SP_NAME = "SP_Muke";
	private static Context context;

	public SharedPreferenceUtil(Context context) {
		this.context = context;
		sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	/**
	 * 设置用户头像url
	 */
	public void setUserHeadUrl(String userHeadUrl) {
		editor.putString("userHeadUrl", userHeadUrl);
		editor.commit();
	}

	/**
	 * 获取用户头像url
	 */
	public String getUserHeadUrl() {

		String userHeadUrl = sp.getString("userHeadUrl", "");
		return userHeadUrl;
	}

	/**
	 * 设置用户token
	 */
	public void setUserToken(String userToken) {
		editor.putString("userToken", userToken);
		editor.commit();
	}

	/**
	 * 获取用户token
	 */
	public String getUserToken() {

		String userToken = sp.getString("userToken", null);
		return userToken;
	}

	/**
	 * 设置用户昵称
	 */
	public void setUserName(String userName) {
		editor.putString("userName", userName);
		editor.commit();
	}

	/**
	 * 获取用户昵称
	 */
	public String getUserName() {

		String userName = sp.getString("userName", "");
		return userName;
	}
	
	/**
	 * 设置用户性别
	 */
	public void setUserSex(int UserSex) {
		editor.putInt("UserSex", UserSex);
		editor.commit();
	}

	/**
	 * 获取用户性别
	 */
	public int getUserSex() {

		int UserSex = sp.getInt("UserSex", 1);
		return UserSex;
	}

	/**
	 * 设置用户手机
	 */
	public void setUserPhone(String UserPhone) {
		editor.putString("UserPhone", UserPhone);
		editor.commit();
	}

	/**
	 * 获取用户手机
	 */
	public String getUserPhone() {

		String UserPhone = sp.getString("UserPhone", "");
		return UserPhone;
	}

	/**
	 * 设置用户ID
	 */
	public void setUserID(String userID) {
		editor.putString("userID", userID);
		editor.commit();
	}

	/**
	 * 获取用户ID
	 */
	public String getUserID() {

		String userID = sp.getString("userID", "0");
		return userID;
	}

	/**
	 * 设置gps经度
	 */
	public void setGPSLongitude(String Longitude) {
		editor.putString("Longitude", Longitude);
		editor.commit();
	}

	/**
	 * 获取gps经度
	 */
	public String getGPSLongitude() {

		String Longitude = sp.getString("Longitude", "");
		return Longitude;
	}

	/**
	 * 设置gps纬度
	 */
	public void setGPSLatitude(String Latitude) {
		editor.putString("Latitude", Latitude);
		editor.commit();
	}

	/**
	 * 获取gps纬度
	 */
	public String getGPSLatitude() {

		String Latitude = sp.getString("Latitude", "");
		return Latitude;
	}

	/**
	 * 设置gps城市
	 */
	public void setGPSCity(String city) {
		editor.putString("city", city);
		editor.commit();
	}

	/**
	 * 获取gps城市
	 */
	public String getGPSCity() {

		String city = sp.getString("city", "");
		return city;
	}

	/**
	 * 设置用户密码
	 */
	public void setLocationAddress(String address) {
		editor.putString("address", address);
		editor.commit();
	}

	/**
	 * 获取用户密码
	 */
	public String getLocationAddress() {

		String address = sp.getString("address", "");
		return address;
	}

	/**
	 * 设置全国车牌号
	 */
	public void setCarCodeJson(String carCodeJson) {
		editor.putString("carCodeJson", carCodeJson);
		editor.commit();
	}

	/**
	 * 获取全国车牌号
	 */
	public String getCarCodeJson() {

		String carCodeJson = sp.getString("carCodeJson", "");
		return carCodeJson;
	}

	/**
	 * 设置选择的城市
	 */
	public void setChoiceCity(String choiceCity) {
		editor.putString("choiceCity", choiceCity);
		editor.commit();
	}

	/**
	 * 获取选择的城市
	 */
	public String getChoiceCity() {

		String choiceCity = sp.getString("choiceCity", "");
		return choiceCity;
	}

	/**
	 * 设置选择的城市的经度
	 */
	public void setChoiceCityLocal_x(String choiceCityLocal_x) {
		editor.putString("choiceCityLocal_x", choiceCityLocal_x);
		editor.commit();
	}

	/**
	 * 获取选择的城市的经度
	 */
	public String getChoiceCityLocal_x() {

		String choiceCityLocal_x = sp.getString("choiceCityLocal_x", "");
		return choiceCityLocal_x;
	}

	/**
	 * 设置选择的城市的纬度
	 */
	public void setChoiceCityLocal_y(String choiceCityLocal_y) {
		editor.putString("choiceCityLocal_y", choiceCityLocal_y);
		editor.commit();
	}

	/**
	 * 获取选择的城市的纬度
	 */
	public String getChoiceCityLocal_y() {

		String choiceCityLocal_y = sp.getString("choiceCityLocal_y", "");
		return choiceCityLocal_y;
	}

	/**
	 * 设置用户账号
	 */
	public void setUserAccount(String userAccount) {
		editor.putString("userAccount", userAccount);
		editor.commit();
	}

	/**
	 * 获取用户账号
	 */
	public String getUserAccount() {

		String userAccount = sp.getString("userAccount", "");
		return userAccount;
	}

	/**
	 * 设置用户密码
	 */
	public void setUserPassword(String userPassword) {
		editor.putString("userPassword", userPassword);
		editor.commit();
	}

	/**
	 * 获取用户密码
	 */
	public String getUserPassword() {

		String userPassword = sp.getString("userPassword", "");
		return userPassword;
	}

	/**
	 * 设置用户是否记住密码
	 */
	public void setUserRemember(boolean isRemember) {
		editor.putBoolean("isRemember", isRemember);
		editor.commit();
	}

	/**
	 * 获取用户是否记住密码
	 */
	public boolean getUserRemember() {

		boolean isRemember = sp.getBoolean("isRemember", false);
		return isRemember;
	}

	/**
	 * 设置用户是否同意协议
	 */
	public void setAgreeFlag(boolean isAgree) {
		editor.putBoolean("isAgree", isAgree);
		editor.commit();
	}

	/**
	 * 获取用户是否同意协议
	 */
	public boolean getAgreeFlag() {

		boolean isAgree = sp.getBoolean("isAgree", false);
		return isAgree;
	}

	/**
	 * 设置上次获取钓点城市的时间
	 */
	public void setFishCityTime(long fishCityTime) {
		editor.putLong("fishCityTime", fishCityTime);
		editor.commit();
	}

	/**
	 * 获取上次获取钓点城市的时间
	 */
	public long getFishCityTime() {

		long fishCityTime = sp.getLong("fishCityTime", 0);
		return fishCityTime;
	}

	/**
	 * 设置钓点城市对象json
	 */
	public void setFishCityJson(String fishCityJson) {
		editor.putString("fishCityJson", fishCityJson);
		editor.commit();
	}

	/**
	 * 获取钓点城市对象json
	 */
	public String getFishCityJson() {

		String fishCityJson = sp.getString("fishCityJson", null);
		return fishCityJson;
	}

	/**
	 * 设置城市对象json
	 */
	public void setCityJson(String cityJson) {
		editor.putString("cityJson", cityJson);
		editor.commit();
	}

	/**
	 * 获取城市对象json
	 */
	public String getCityJson() {

		String cityJson = sp.getString("cityJson", null);
		return cityJson;
	}

	/**
	 * 设置红点是否显示
	 */
	public void setRedPointIsVisible(boolean isVisible) {
		editor.putBoolean("isVisible", isVisible);
		editor.commit();
	}

	/**
	 * 获取红点是否显示
	 */
	public boolean getRedPointIsVisible() {

		boolean isVisible = sp.getBoolean("isVisible", false);
		return isVisible;
	}

	/**
	 * 设置草稿箱红点是否显示
	 */
	public void setDraftsRedPointIsVisible(boolean isDraftsVisible) {
		editor.putBoolean("isDraftsVisible", isDraftsVisible);
		editor.commit();
	}

	/**
	 * 获取草稿箱红点是否显示
	 */
	public boolean getDraftsRedPointIsVisible() {

		boolean isDraftsVisible = sp.getBoolean("isDraftsVisible", false);
		return isDraftsVisible;
	}

	/**
	 * 设置上次更新的时间
	 */
	public void setUpdateTime(long updateTime) {
		editor.putLong("updateTime", updateTime);
		editor.commit();
	}

	/**
	 * 获取上次更新的时间
	 */
	public long getUpdateTime() {

		long updateTime = sp.getLong("updateTime", 0);
		return updateTime;
	}
	
	/**
	 * 设置本地天气是否显示
	 */
	public void setLocalWeather(boolean isVisible) {
		editor.putBoolean("localWeather", isVisible);
		editor.commit();
	}

	/**
	 * 获取本地天气是否显示
	 */
	public boolean getLocalWeather() {

		boolean isVisible = sp.getBoolean("localWeather", false);
		return isVisible;
	}

	/**
	 * 设置渔获对象json
	 */
	public void setFishFeedJson(String fishFeedJson) {
		editor.putString("fishFeedJson", fishFeedJson);
		editor.commit();
	}

	/**
	 * 获取渔获对象json
	 */
	public String getFishFeedJson() {

		String fishFeedJson = sp.getString("fishFeedJson", null);
		return fishFeedJson;
	}

	/**
	 * 设置草稿箱列表对象json
	 */
	public void setDraftsListJson(String draftsListJson) {
		editor.putString("draftsListJson", draftsListJson);
		editor.commit();
	}

	/**
	 * 获取草稿箱列表对象json
	 */
	public String getDraftsListJson() {
		String draftsListJson = sp.getString("draftsListJson", null);
		return draftsListJson;
	}

	/**
	 * 设置推送设备
	 */
	public void setDeviceToken(String device_token) {
		editor.putString("device_token", device_token);
		editor.commit();
	}

	/**
	 * 获取推送设备
	 */
	public String getDeviceToken() {
		String device_token = sp.getString("device_token", null);
		return device_token;
	}

	/**
	 * 设置湖泊类型
	 */
	public void setWaterType(int water_type) {
		editor.putInt("water_type", water_type);
		editor.commit();
	}

	/**
	 * 获取湖泊类型
	 */
	public int getWaterType() {
		int water_type = sp.getInt("water_type", -1);
		return water_type;
	}

	/**
	 * 设置sort
	 */
	public void setSort(int sort) {
		editor.putInt("sort", sort);
		editor.commit();
	}

	/**
	 * 获取sort
	 */
	public int getSort() {
		int sort = sp.getInt("sort", -1);
		return sort;
	}

	/**
	 * 设置CityNo
	 */
	public void setCityNo(int city_no) {
		editor.putInt("city_no", city_no);
		editor.commit();
	}

	/**
	 * 获取CityNo
	 */
	public int getCityNo() {
		int city_no = sp.getInt("city_no", -1);
		return city_no;
	}

	/**
	 * 设置CityText
	 */
	public void setCityText(String city_text) {
		editor.putString("city_text", city_text);
		editor.commit();
	}

	/**
	 * 获取CityText
	 */
	public String getCityText() {
		String city_text = sp.getString("city_text", null);
		return city_text;
	}

	/**
	 * 设置water_selete
	 */
	public void setWaterSelete(String water_selete) {
		editor.putString("water_selete", water_selete);
		editor.commit();
	}

	/**
	 * 获取water_selete
	 */
	public String getWaterSelete() {
		String water_selete = sp.getString("water_selete", null);
		return water_selete;
	}

	/**
	 * 设置order_selete
	 */
	public void setOrderSelete(String order_selete) {
		editor.putString("order_selete", order_selete);
		editor.commit();
	}

	/**
	 * 获取order_selete
	 */
	public String getOrderSelete() {
		String order_selete = sp.getString("order_selete", null);
		return order_selete;
	}

	/**
	 * 设置是否绑定过device_token
	 */
	public void setBindDeviceToken(boolean bindDeviceToken) {
		editor.putBoolean("bindDeviceToken", bindDeviceToken);
		editor.commit();
	}

	/**
	 * 获取红点是否显示
	 */
	public boolean getsetBindDeviceToken() {

		boolean bindDeviceToken = sp.getBoolean("bindDeviceToken", false);
		return bindDeviceToken;
	}
	
	/**
	 * 获取缓存菜单
	 */
	public String getMenu() {
		String menuJson = sp.getString("menuJson", null);
		return menuJson;
	}

	/**
	 * 保存缓存菜单
	 */
	public void setMenu(String menuJson) {
		editor.putString("menuJson", menuJson);
		editor.commit();
	}
	
	/**
	 * 获取首页推荐
	 */
	public String getRecommend() {
		String recommendJson = sp.getString("recommendJson", null);
		return recommendJson;
	}

	/**
	 * 缓存首页推荐
	 */
	public void setRecommend(String recommendJson) {
		editor.putString("recommendJson", recommendJson);
		editor.commit();
	}
	
	/**
	 * 获取全部资讯
	 */
	public String getInfoAll(String tag) {
		String infoAllJson = sp.getString(tag, null);
		return infoAllJson;
	}

	/**
	 * 缓存全部资讯
	 */
	public void setInfoAll(String tag,String infoAllJson) {
		editor.putString(tag, infoAllJson);
		editor.commit();
	}
	
	/**
	 * 获取热点资讯
	 */
	public String getInfoHot() {
		String infoHotJson = sp.getString("infoHotJson", null);
		return infoHotJson;
	}

	/**
	 * 缓存热点资讯
	 */
	public void setInfoHot(String infoHotJson) {
		editor.putString("infoHotJson", infoHotJson);
		editor.commit();
	}
	
	/**
	 * 获取技巧资讯
	 */
	public String getInfoFish() {
		String infoFishJson = sp.getString("infoFishJson", null);
		return infoFishJson;
	}

	/**
	 * 缓存技巧资讯
	 */
	public void setInfoFish(String infoFishJson) {
		editor.putString("infoFishJson", infoFishJson);
		editor.commit();
	}
	
	/**
	 * 获取游记资讯
	 */
	public String getInfoRoad() {
		String infoRoadson = sp.getString("infoRoadson", null);
		return infoRoadson;
	}

	/**
	 * 缓存游记资讯
	 */
	public void setInfoRoad(String infoRoadson) {
		editor.putString("infoRoadson", infoRoadson);
		editor.commit();
	}
	
	/**
	 * 获取游记资讯
	 */
	public String getInfoSea() {
		String infoSeason = sp.getString("infoSeason", null);
		return infoSeason;
	}

	/**
	 * 缓存游记资讯
	 */
	public void setInfoSea(String infoSeason) {
		editor.putString("infoSeason", infoSeason);
		editor.commit();
	}
	
	/**
	 * 获取活动资讯
	 */
	public String getInfoActive() {
		String infoActiveJson = sp.getString("infoActiveJson", null);
		return infoActiveJson;
	}

	/**
	 * 缓存活动资讯
	 */
	public void setInfoActive(String infoActiveJson) {
		editor.putString("infoActiveJson", infoActiveJson);
		editor.commit();
	}
	
	/**
	 * 获取收藏资讯
	 */
	public String getInfoCollection() {
		String infoCollectJson = sp.getString("infoCollectJson", null);
		return infoCollectJson;
	}

	/**
	 * 缓存收藏资讯
	 */
	public void setInfoCollection(String infoCollectJson) {
		editor.putString("infoCollectJson", infoCollectJson);
		editor.commit();
	}
	
	/**
	 * 获取钓点数据
	 */
	public String getFishing() {
		String fishingJson = sp.getString("fishingJson", null);
		return fishingJson;
	}

	/**
	 * 缓存钓点数据
	 */
	public void setFishing(String fishingJson) {
		editor.putString("fishingJson", fishingJson);
		editor.commit();
	}
	
	/**
	 * 获取最热动态数据
	 */
	public String getHotFeed() {
		String hotFeedJson = sp.getString("hotFeedJson", null);
		return hotFeedJson;
	}

	/**
	 * 缓存最热动态数据
	 */
	public void setHotFeed(String hotFeedJson) {
		editor.putString("hotFeedJson", hotFeedJson);
		editor.commit();
	}
	
	/**
	 * 获取最新动态数据
	 */
	public String getNewFeed() {
		String newFeedJson = sp.getString("newFeedJson", null);
		return newFeedJson;
	}

	/**
	 * 缓存最新动态数据
	 */
	public void setNewFeed(String newFeedJson) {
		editor.putString("newFeedJson", newFeedJson);
		editor.commit();
	}
	
	/**
	 * 账号身份
	 * @return
	 */
	public int getMember() {
		int member = sp.getInt("member", 0);
		return member;
	}

	/**
	 * 账号身份
	 */
	public void setMember(int member) {
		editor.putInt("member", member);
		editor.commit();
	}

	/**
	 * 获取标签集合
	 */
	public String getTags() {
		String tagsJson = sp.getString("tagsJson", null);
		return tagsJson;
	}

	/**
	 * 缓存标签集合
	 */
	public void setTags(String tagsJson) {
		editor.putString("tagsJson", tagsJson);
		editor.commit();
	}
	
	/**
	 * 获取是否第一次進來快樂釣魚
	 */
	public Boolean getFeedFunFlag() {
		boolean feedFunFlag = sp.getBoolean("feedFunFlag", true);
		return feedFunFlag;
	}

	/**
	 * 設置当前选择的下标
	 */
	public void setFeedFunFlag(boolean feedFunFlag) {
		editor.putBoolean("feedFunFlag", feedFunFlag);
		editor.commit();
	}
	
	/**
	 * 获取当前选择的下标
	 */
	public int getCurrentPosition() {
		int currentPosition = sp.getInt("currentPosition", 0);
		return currentPosition;
	}

	/**
	 * 設置当前选择的下标
	 */
	public void setCurrentPosition(int currentPosition) {
		editor.putInt("currentPosition", currentPosition);
		editor.commit();
	}
	
	/**
	 * 获取用戶定義的標籤
	 */
	public String getUserChannelJson() {
		String userChannelJson = sp.getString("userChannelJson", null);
		return userChannelJson;
	}

	/**
	 * 設置用戶定義的標籤
	 */
	public void setUserChannelJson(String userChannelJson) {
		editor.putString("userChannelJson", userChannelJson);
		editor.commit();
	}
	
	/**
	 * 获取系統其他標籤
	 */
	public String getOtherChannelJson() {
		String otherChannelJson = sp.getString("otherChannelJson", null);
		return otherChannelJson;
	}

	/**
	 * 設置系統其他標籤
	 */
	public void setOtherChannelJson(String otherChannelJson) {
		editor.putString("otherChannelJson", otherChannelJson);
		editor.commit();
	}
	
	public boolean getShowDetail() {
		boolean showDetail = sp.getBoolean("showDetail", true);
		return showDetail;
	}

	
	public void setShowDetail(boolean showDetail) {
		editor.putBoolean("showDetail", showDetail);
		editor.commit();
	}
	
	public boolean getGPSTag() {
		boolean getGpsTag = sp.getBoolean("getGpsTag", false);
		return getGpsTag;
	}

	
	public void setGPSTag(boolean getGpsTag) {
		editor.putBoolean("getGpsTag", getGpsTag);
		editor.commit();
	}
	
}
