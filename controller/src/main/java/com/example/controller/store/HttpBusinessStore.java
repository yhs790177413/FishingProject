package com.example.controller.store;


import com.example.controller.bean.ActiveListBean;
import com.example.controller.bean.ActivityDetailBean;
import com.example.controller.bean.AddFooterprintBean;
import com.example.controller.bean.AttentionFriendsBean;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.CityListBean;
import com.example.controller.bean.CommentsBean;
import com.example.controller.bean.FeedsFunListBean;
import com.example.controller.bean.FishCityListBean;
import com.example.controller.bean.FishRankBean;
import com.example.controller.bean.FishTypeBean;
import com.example.controller.bean.FishingDetialBean;
import com.example.controller.bean.FishingInfoBean;
import com.example.controller.bean.FishingInfoDetailBean;
import com.example.controller.bean.FishingListBean;
import com.example.controller.bean.FooterprintDetailBean;
import com.example.controller.bean.FooterprintListBean;
import com.example.controller.bean.JoinActiveBean;
import com.example.controller.bean.JoinFeedFunBean;
import com.example.controller.bean.JoinUsersListBean;
import com.example.controller.bean.LoginBean;
import com.example.controller.bean.MenuListBean;
import com.example.controller.bean.MyInfoBean;
import com.example.controller.bean.MyMessageBean;
import com.example.controller.bean.PictrueTokenBean;
import com.example.controller.bean.RecommendsListBean;
import com.example.controller.bean.TagsListBean;
import com.example.controller.bean.WeatherDetailBean;
import com.example.controller.controller.IBusinessController;
import com.example.controller.http.HttpProjectProtocolFactory;
import com.example.controller.http.HttpProtocol;

import retrofit.Callback;

/**
 * Created by yanghs on 2016/5/18.
 * 业务接口仓库
 */
public class HttpBusinessStore implements IBussinessStore {

    private IBusinessController mController;

    public HttpBusinessStore(IBusinessController controller) {
        mController = controller;
    }

    /**
     * 登录
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param mobile
     * @param password
     * @param device_token
     * @param callback
     */
    public void login(String access_token, String version_code, String network, String mobile, String password, String device_token, Callback<LoginBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).login(access_token, version_code, network, mobile, password, device_token, callback);
    }

    /**
     * 注销
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param device_token
     * @param callback
     */
    public void logout(String access_token, String version_code, String network, String device_token, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).logout(access_token, version_code, network, device_token, callback);
    }

    /**
     * 获取验证码
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param mobile
     * @param type         -- 0:注册验证码；1：找回密码验证码
     * @param callback
     */
    public void verifyCodeJson(String access_token, String version_code, String network, String mobile, int type, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).verifyCodeJson(access_token, version_code, network, mobile, type, callback);
    }

    /**
     * 注册
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param mobile
     * @param verify_code
     * @param user_name
     * @param password
     * @param head_pic
     * @param device_token
     * @param callback
     */
    public void registerJson(String access_token, String version_code, String network, String mobile, String verify_code, String user_name, String password, String head_pic, String device_token, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).registerJson(access_token, version_code, network, mobile, verify_code, user_name, password, head_pic, device_token, callback);
    }

    /**
     * 渔讯列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param type
     * @param callback
     */
    public void fishingInfoJson(String access_token, String version_code, String network, int page, int number, int type, Callback<FishingInfoBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).fishingInfoJson(access_token, version_code, network, page, number, type, callback);
    }

    /**
     * 渔讯详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    public void fishingInfoDetailJson(String access_token, String version_code, String network, int id, Callback<FishingInfoDetailBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).fishingInfoDetailJson(access_token, version_code, network, id, callback);
    }

    /**
     * 钓点列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param water_type
     * @param city
     * @param page
     * @param number
     * @param sort
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void fishingListJson(String access_token, String version_code, String network, int water_type, int city, int page, int number, int sort, double latitude, double longitude, Callback<FishingListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).fishingListJson(access_token, version_code, network, water_type, city, page, number, sort, latitude, longitude, callback);
    }

    /**
     * 钓点详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void fishingDetailJson(String access_token, String version_code, String network, int id, double latitude, double longitude, Callback<FishingDetialBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).fishingDetailJson(access_token, version_code, network, id, latitude, longitude, callback);
    }

    /**
     * 更新用户信息
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param head_pic
     * @param user_name
     * @param sex
     * @param birthday
     * @param city
     * @param callback
     */
    public void updateUserInfo(String access_token, String version_code, String network, String head_pic, String user_name, int sex, String birthday, int city, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).updateUserInfo(access_token, version_code, network, head_pic, user_name, sex, birthday, city, callback);
    }

    /**
     * 忘记密码
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param mobile
     * @param verify_code
     * @param password
     * @param callback
     */
    public void forgetPassword(String access_token, String version_code, String network, String mobile, String verify_code, String password, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).forgetPassword(access_token, version_code, network, mobile, verify_code, password, callback);
    }

    /**
     * 获取足迹列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param page
     * @param number
     * @param type
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void getFishFeeds(String access_token, String version_code, String network, int id, int page, int number, int type, double latitude, double longitude, Callback<FooterprintListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getFishFeeds(access_token, version_code, network, id, page, number, type, latitude, longitude, callback);
    }

    /**
     * 获取足迹详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void footerprintDetail(String access_token, String version_code, String network, int id, double latitude, double longitude, Callback<FooterprintDetailBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).footerprintDetail(access_token, version_code, network, id, latitude, longitude, callback);
    }

    /**
     * 获取我的收藏钓点
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void favoriteFishingPoint(String access_token, String version_code, String network, int page, int number, double latitude, double longitude, Callback<FishingListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).favoriteFishingPoint(access_token, version_code, network, page, number, latitude, longitude, callback);
    }

    /**
     * 获取我的收藏足迹
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void favoriteFooterprint(String access_token, String version_code, String network, int page, int number, double latitude, double longitude, Callback<FooterprintListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).favoriteFooterprint(access_token, version_code, network, page, number, latitude, longitude, callback);
    }

    /**
     * 添加收藏
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type         1--资讯  2--钓点 3--足迹
     * @param op           1--喜欢    0--不喜欢
     * @param callback
     */
    public void collection(String access_token, String version_code, String network, int id, int type, int op, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).collection(access_token, version_code, network, id, type, op, callback);
    }

    /**
     * 获取关注人列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param page
     * @param number
     * @param type         --0 我关注 1--关注我
     * @param callback
     */
    public void getAttentionList(String access_token, String version_code, String network, String user_id, int page, int number, int type, Callback<AttentionFriendsBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getAttentionList(access_token, version_code, network, user_id, page, number, type, callback);
    }

    /**
     * 获取他人的足迹列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param page
     * @param number
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void getMyFooterprintList(String access_token, String version_code, String network, String user_id, int page, int number, double latitude, double longitude, Callback<FooterprintListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getMyFooterprintList(access_token, version_code, network, user_id, page, number, latitude, longitude, callback);
    }

    /**
     * 获取全部消息
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param type
     * @param callback
     */
    public void getMyMessageList(String access_token, String version_code, String network, int page, int number, int type, Callback<MyMessageBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getMyMessageList(access_token, version_code, network, page, number, type, callback);
    }

    /**
     * 获取所有鱼类
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param callback
     */
    public void fishTypeJson(String access_token, String version_code, String network, int page, int number, Callback<FishTypeBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).fishTypeJson(access_token, version_code, network, page, number, callback);
    }

    /**
     * 点赞
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type         1资讯 2钓点 3足迹
     * @param op           1喜欢 0不喜欢
     * @param callback
     */
    public void getLike(String access_token, String version_code, String network, int id, int type, int op, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getLike(access_token, version_code, network, id, type, op, callback);
    }

    /**
     * 评论
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type          1资讯 2钓点 3足迹
     * @param comment
     * @param reply_id
     * @param reply_user_id
     * @param callback
     */
    public void comment(String access_token, String version_code, String network, int id, int type, String comment, String reply_id, String reply_user_id, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).comment(access_token, version_code, network, id, type, comment, reply_id, reply_user_id, callback);
    }

    /**
     * 关注某个人
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param op           1 关注 0 取消关注
     * @param callback
     */
    public void follow(String access_token, String version_code, String network, String user_id, int op, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).follow(access_token, version_code, network, user_id, op, callback);
    }

    /**
     * 添加足迹 -- 上传图片完回调
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    public void notifyFishLocation(String access_token, String version_code, String network, int id, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).notifyFishLocation(access_token, version_code, network, id, callback);
    }

    /**
     * 获取钓点的所有日志
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param keyword
     * @param callback
     */
    public void searchList(String access_token, String version_code, String network, int page, int number, String keyword, int type, Callback<FishingInfoBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).searchList(access_token, version_code, network, page, number, keyword, type, callback);
    }

    /**
     * 获取用户的信息
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param callback
     */
    public void getOtherUserInfo(String access_token, String version_code, String network, String user_id, Callback<MyInfoBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getOtherUserInfo(access_token, version_code, network, user_id, callback);
    }

    /**
     * 获取某个人的钓位
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param user_id
     * @param longitude
     * @param latitude
     * @param callback
     */
    public void getUserFishLocations(String access_token, String version_code, String network, int page, int number, String user_id, double longitude, double latitude, Callback<FooterprintListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getUserFishLocations(access_token, version_code, network, page, number, user_id, longitude, latitude, callback);
    }

    /**
     * 获取某个人的聊天记录
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param user_id
     * @param callback
     */
    public void getMessageSession(String access_token, String version_code, String network, int page, int number, String user_id, Callback<MyMessageBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getMessageSession(access_token, version_code, network, page, number, user_id, callback);
    }

    /**
     * 发送私信
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param message
     * @param callback
     */
    public void sendMessage(String access_token, String version_code, String network, String user_id, String message, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).sendMessage(access_token, version_code, network, user_id, message, callback);
    }

    /**
     * 授权登录
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param outer_id
     * @param type
     * @param sex
     * @param head_pic
     * @param user_name
     * @param device_token
     * @param callback
     */
    public void getAutoLogin(String access_token, String version_code, String network, String outer_id, int type, int sex, String head_pic, String user_name, String device_token, Callback<LoginBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getAutoLogin(access_token, version_code, network, outer_id, type, sex, head_pic, user_name, device_token, callback);
    }

    /**
     * 获取评论列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type
     * @param page
     * @param number
     * @param callback
     */
    public void getComments(String access_token, String version_code, String network, int id, int type, int page, int number, Callback<CommentsBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getComments(access_token, version_code, network, id, type, page, number, callback);
    }

    /**
     * 搜索钓点
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param keyword
     * @param longitude
     * @param latitude
     * @param callback
     */
    public void searchFishPoints(String access_token, String version_code, String network, int page, int number, String keyword, double longitude, double latitude, Callback<FishingListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).searchFishPoints(access_token, version_code, network, page, number, keyword, longitude, latitude, callback);
    }

    /**
     * 获取图片的token
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param type
     * @param callback
     */
    public void getPictureToken(String access_token, String version_code, String network, int type, Callback<PictrueTokenBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getPictureToken(access_token, version_code, network, type, callback);
    }

    /**
     * 删除我的足迹
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    public void delFishLocation(String access_token, String version_code, String network, int id, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).delFishLocation(access_token, version_code, network, id, callback);
    }

    /**
     * 获取天气详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    public void getFishPointWeather(String access_token, String version_code, String network, int id, Callback<WeatherDetailBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getFishPointWeather(access_token, version_code, network, id, callback);
    }

    /**
     * 获取搜索资讯列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param keyword
     * @param type
     * @param callback
     */
    public void SearchInfoListJson(String access_token, String version_code, String network, int page, int number, String keyword, int type, Callback<FishingInfoBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).SearchInfoListJson(access_token, version_code, network, page, number, keyword, type, callback);
    }

    /**
     * 添加渔获
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param longitude
     * @param latitude
     * @param number
     * @param info
     * @param fishPoint_id
     * @param picture_number
     * @param code
     * @param city_name
     * @param address
     * @param tools
     * @param quality
     * @param tag_id
     * @param weather
     * @param type_ids
     * @param callback
     */
    public void postFishFeed(String access_token, String version_code, String network, double longitude, double latitude, int number, String info, int fishPoint_id, int picture_number, String code, String city_name,
                             String address, String tools, int quality, int tag_id, String weather, String type_ids, Callback<AddFooterprintBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).postFishFeed(access_token, version_code, network, longitude, latitude, number, info, fishPoint_id,
                picture_number, code, city_name, address, tools, quality, tag_id, weather, type_ids, callback);
    }

    /**
     * 获取赞数排名列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param tag_id
     * @param callback
     */
    public void getFishRankJson(String access_token, String version_code, String network, int page, int number, int tag_id, Callback<FishRankBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getFishRankJson(access_token, version_code, network, page, number, tag_id, callback);
    }

    /**
     * 绑定设备
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param device_token
     * @param callback
     */
    public void getBindDeviceTokenJson(String access_token, String version_code, String network, String device_token, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getBindDeviceTokenJson(access_token, version_code, network, device_token, callback);
    }

    /**
     * 获取活动详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    public void getActivityJson(String access_token, String version_code, String network, String id, Callback<ActivityDetailBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getActivityJson(access_token, version_code, network, id, callback);
    }

    /**
     * 报名活动详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param code
     * @param callback
     */
    public void joinActivityJson(String access_token, String version_code, String network, String id, String code, Callback<JoinActiveBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).joinActivityJson(access_token, version_code, network, id, code, callback);
    }

    /**
     * 每日签到
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param type
     * @param callback
     */
    public void sign(String access_token, String version_code, String network, String type, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).sign(access_token, version_code, network, type, callback);
    }

    /**
     * 获取标签资讯列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param id
     * @param id
     * @param callback
     */
    public void getTagInfosJson(String access_token, String version_code, String network, int page, int number, String id, String ids, Callback<FishingInfoBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getTagInfosJson(access_token, version_code, network, page, number, id, ids, callback);
    }

    /**
     * 获取活动列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param callback
     */
    public void getActivitysJson(String access_token, String version_code, String network, int page, int number, Callback<ActiveListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getActivitysJson(access_token, version_code, network, page, number, callback);
    }

    /**
     * 加精
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type
     * @param op
     * @param callback
     */
    public void addJingJson(String access_token, String version_code, String network, int id, int type, int op, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).addJingJson(access_token, version_code, network, id, type, op, callback);
    }

    /**
     * 获取参加活动列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param page
     * @param number
     * @param callback
     */
    public void getJoinUsers(String access_token, String version_code, String network, String id, int page, int number, Callback<JoinUsersListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getJoinUsers(access_token, version_code, network, id, page, number, callback);
    }

    /**
     * 获取资讯标签
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param callback
     */
    public void getInfoTags(String access_token, String version_code, String network, int page, int number, Callback<TagsListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getInfoTags(access_token, version_code, network, page, number, callback);
    }

    /**
     * 获取鱼塘列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param callback
     */
    public void getFishFunFeeds(String access_token, String version_code, String network, int page, int number, Callback<FeedsFunListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getFishFunFeeds(access_token, version_code, network, page, number, callback);
    }

    /**
     * 获取我的鱼塘列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param callback
     */
    public void getMyFishFunFeeds(String access_token, String version_code, String network, int page, int number, Callback<FeedsFunListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getMyFishFunFeeds(access_token, version_code, network, page, number, callback);
    }

    /**
     * 參加活動
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param code
     * @param callback
     */
    public void getjoinFishFun(String access_token, String version_code, String network, String code, Callback<JoinFeedFunBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getjoinFishFun(access_token, version_code, network, code, callback);
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param callback
     */
    public void getCityList(String access_token, String version_code, String network, Callback<FishCityListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getCityList(access_token, version_code, network, callback);
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param callback
     */
    public void getMenus(String access_token, String version_code, String network, Callback<MenuListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getMenus(access_token, version_code, network, callback);
    }

    public void getRecommends(String access_token, String version_code, String network, Callback<RecommendsListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getRecommends(access_token, version_code, network, callback);
    }

    public void getUserInfo(String access_token, String version_code, String network, Callback<MyInfoBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getUserInfo(access_token, version_code, network, callback);
    }


    public void getFavoriteInfos(String access_token, String version_code, String network, int page, int number, int type, Callback<FishingInfoBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getFavoriteInfos(access_token, version_code, network, page, number, type, callback);
    }

    public void readAll(String access_token, String version_code, String network, Callback<BaseBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).readAll(access_token, version_code, network, callback);
    }

    public void getCityIds(String access_token, String version_code, String network, Callback<CityListBean> callback) {

        HttpProjectProtocolFactory.getProtocol(HttpProjectProtocolFactory.HOST_URL_P,
                HttpProtocol.class).getCityIds(access_token, version_code, network, callback);
    }
}
