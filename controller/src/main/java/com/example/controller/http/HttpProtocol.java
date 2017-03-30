package com.example.controller.http;


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

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;


/**
 * Created by yanghs on 2016/5/18.
 */
public interface HttpProtocol {


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
    @FormUrlEncoded
    @POST("/service/user/login")
    void login(@Header("Token") String access_token,
               @Header("agent") String version_code,
               @Header("network") String network,
               @Field("mobile") String mobile,
               @Field("password") String password,
               @Field("device_token") String device_token,
               Callback<LoginBean> callback);

    /**
     * 注销
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param device_token
     * @param callback
     */
    @GET("/service/user/logout")
    void logout(@Header("Token") String access_token,
                @Header("agent") String version_code,
                @Header("network") String network,
                @Query("device_token") String device_token,
                Callback<BaseBean> callback);

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
    @GET("/service/user/verifyCode")
    void verifyCodeJson(@Header("Token") String access_token,
                        @Header("agent") String version_code,
                        @Header("network") String network,
                        @Query("mobile") String mobile,
                        @Query("type") int type,
                        Callback<BaseBean> callback);

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
    @FormUrlEncoded
    @POST("/service/user/register")
    void registerJson(@Header("Token") String access_token,
                      @Header("agent") String version_code,
                      @Header("network") String network,
                      @Field("mobile") String mobile,
                      @Field("verify_code") String verify_code,
                      @Field("user_name") String user_name,
                      @Field("password") String password,
                      @Field("head_pic") String head_pic,
                      @Field("device_token") String device_token,
                      Callback<BaseBean> callback);

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
    @GET("/service/goby/info/getInfos")
    void fishingInfoJson(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("page") int page,
                         @Query("number") int number,
                         @Query("type") int type,
                         Callback<FishingInfoBean> callback);

    /**
     * 渔讯详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    @GET("/service/goby/info/getInfo")
    void fishingInfoDetailJson(@Header("Token") String access_token,
                               @Header("agent") String version_code,
                               @Header("network") String network,
                               @Query("id") int id,
                               Callback<FishingInfoDetailBean> callback);

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
    @GET("/service/goby/fish/getFishPoints")
    void fishingListJson(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("water_type") int water_type,
                         @Query("city") int city,
                         @Query("page") int page,
                         @Query("number") int number,
                         @Query("sort") int sort,
                         @Query("latitude") double latitude,
                         @Query("longitude") double longitude,
                         Callback<FishingListBean> callback);

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
    @GET("/service/goby/fish/getFishPoint")
    void fishingDetailJson(@Header("Token") String access_token,
                           @Header("agent") String version_code,
                           @Header("network") String network,
                           @Query("id") int id,
                           @Query("latitude") double latitude,
                           @Query("longitude") double longitude,
                           Callback<FishingDetialBean> callback);

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
    @FormUrlEncoded
    @POST("/service/user/update")
    void updateUserInfo(@Header("Token") String access_token,
                        @Header("agent") String version_code,
                        @Header("network") String network,
                        @Field("head_pic") String head_pic,
                        @Field("user_name") String user_name,
                        @Field("sex") int sex,
                        @Field("birthday") String birthday,
                        @Field("city") int city,
                        Callback<BaseBean> callback);

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
    @GET("/service/user/forgetPassword")
    void forgetPassword(@Header("Token") String access_token,
                        @Header("agent") String version_code,
                        @Header("network") String network,
                        @Query("mobile") String mobile,
                        @Query("verify_code") String verify_code,
                        @Query("password") String password,
                        Callback<BaseBean> callback);

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
    @GET("/service/goby/fish/getFishFeeds")
    void getFishFeeds(@Header("Token") String access_token,
                      @Header("agent") String version_code,
                      @Header("network") String network,
                      @Query("id") int id,
                      @Query("page") int page,
                      @Query("number") int number,
                      @Query("type") int type,
                      @Query("latitude") double latitude,
                      @Query("longitude") double longitude,
                      Callback<FooterprintListBean> callback);

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
    @GET("/service/goby/fish/getFishFeed")
    void footerprintDetail(@Header("Token") String access_token,
                           @Header("agent") String version_code,
                           @Header("network") String network,
                           @Query("id") int id,
                           @Query("latitude") double latitude,
                           @Query("longitude") double longitude,
                           Callback<FooterprintDetailBean> callback);

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
    @GET("/service/goby/fish/getFavoriteFishPoints")
    void favoriteFishingPoint(@Header("Token") String access_token,
                              @Header("agent") String version_code,
                              @Header("network") String network,
                              @Query("page") int page,
                              @Query("number") int number,
                              @Query("latitude") double latitude,
                              @Query("longitude") double longitude,
                              Callback<FishingListBean> callback);

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
    @GET("/service/goby/fish/getFavoriteFishFeeds")
    void favoriteFooterprint(@Header("Token") String access_token,
                             @Header("agent") String version_code,
                             @Header("network") String network,
                             @Query("page") int page,
                             @Query("number") int number,
                             @Query("latitude") double latitude,
                             @Query("longitude") double longitude,
                             Callback<FooterprintListBean> callback);

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
    @FormUrlEncoded
    @POST("/service/goby/op/favorite")
    void collection(@Header("Token") String access_token,
                    @Header("agent") String version_code,
                    @Header("network") String network,
                    @Field("id") int id,
                    @Field("type") int type,
                    @Field("op") int op,
                    Callback<BaseBean> callback);

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
    @GET("/service/goby/op/getFollowers")
    void getAttentionList(@Header("Token") String access_token,
                          @Header("agent") String version_code,
                          @Header("network") String network,
                          @Query("user_id") String user_id,
                          @Query("page") int page,
                          @Query("number") int number,
                          @Query("type") int type,
                          Callback<AttentionFriendsBean> callback);

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
    @GET("/service/goby/fish/getUserFishFeeds")
    void getMyFooterprintList(@Header("Token") String access_token,
                              @Header("agent") String version_code,
                              @Header("network") String network,
                              @Query("user_id") String user_id,
                              @Query("page") int page,
                              @Query("number") int number,
                              @Query("latitude") double latitude,
                              @Query("longitude") double longitude,
                              Callback<FooterprintListBean> callback);

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
    @FormUrlEncoded
    @POST("/service/goby/op/getMessages")
    void getMyMessageList(@Header("Token") String access_token,
                          @Header("agent") String version_code,
                          @Header("network") String network,
                          @Query("page") int page,
                          @Query("number") int number,
                          @Field("type") int type,
                          Callback<MyMessageBean> callback);

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
    @GET("/service/goby/fish/getFishTypes")
    void fishTypeJson(@Header("Token") String access_token,
                      @Header("agent") String version_code,
                      @Header("network") String network,
                      @Query("page") int page,
                      @Query("number") int number,
                      Callback<FishTypeBean> callback);

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
    @FormUrlEncoded
    @POST("/service/goby/op/like")
    void getLike(@Header("Token") String access_token,
                 @Header("agent") String version_code,
                 @Header("network") String network,
                 @Field("id") int id,
                 @Field("type") int type,
                 @Field("op") int op,
                 Callback<BaseBean> callback);

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
    @FormUrlEncoded
    @POST("/service/goby/op/postComment")
    void comment(@Header("Token") String access_token,
                 @Header("agent") String version_code,
                 @Header("network") String network,
                 @Field("id") int id,
                 @Field("type") int type,
                 @Field("comment") String comment,
                 @Field("reply_id") String reply_id,
                 @Field("reply_user_id") String reply_user_id,
                 Callback<BaseBean> callback);

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
    @FormUrlEncoded
    @POST("/service/goby/op/follow")
    void follow(@Header("Token") String access_token,
                @Header("agent") String version_code,
                @Header("network") String network,
                @Field("user_id") String user_id,
                @Field("op") int op,
                Callback<BaseBean> callback);


    /**
     * 添加足迹 -- 上传图片完回调
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    @GET("/service/goby/fish/notifyFishFeed")
    void notifyFishLocation(@Header("Token") String access_token,
                            @Header("agent") String version_code,
                            @Header("network") String network,
                            @Query("id") int id,
                            Callback<BaseBean> callback);

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
    @GET("/service/goby/info/searchList")
    void searchList(@Header("Token") String access_token,
                    @Header("agent") String version_code,
                    @Header("network") String network,
                    @Query("page") int page,
                    @Query("number") int number,
                    @Query("keyword") String keyword,
                    @Query("type") int type,
                    Callback<FishingInfoBean> callback);

    /**
     * 获取用户的信息
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param callback
     */
    @GET("/service/user/getUserInfo")
    void getOtherUserInfo(@Header("Token") String access_token,
                          @Header("agent") String version_code,
                          @Header("network") String network,
                          @Query("user_id") String user_id,
                          Callback<MyInfoBean> callback);

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
    @GET("/service/goby/fish/getUserFishFeeds")
    void getUserFishLocations(@Header("Token") String access_token,
                              @Header("agent") String version_code,
                              @Header("network") String network,
                              @Query("page") int page,
                              @Query("number") int number,
                              @Query("user_id") String user_id,
                              @Query("longitude") double longitude,
                              @Query("latitude") double latitude,
                              Callback<FooterprintListBean> callback);

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
    @GET("/service/goby/op/getMessageSession")
    void getMessageSession(@Header("Token") String access_token,
                           @Header("agent") String version_code,
                           @Header("network") String network,
                           @Query("page") int page,
                           @Query("number") int number,
                           @Query("user_id") String user_id,
                           Callback<MyMessageBean> callback);

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
    @FormUrlEncoded
    @POST("/service/goby/op/sendMessage")
    void sendMessage(@Header("Token") String access_token,
                     @Header("agent") String version_code,
                     @Header("network") String network,
                     @Field("user_id") String user_id,
                     @Field("message") String message,
                     Callback<BaseBean> callback);

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
    @GET("/service/user/bindUser")
    void getAutoLogin(@Header("Token") String access_token,
                      @Header("agent") String version_code,
                      @Header("network") String network,
                      @Query("outer_id") String outer_id,
                      @Query("type") int type,
                      @Query("sex") int sex,
                      @Query("head_pic") String head_pic,
                      @Query("user_name") String user_name,
                      @Query("device_token") String device_token,
                      Callback<LoginBean> callback);

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
    @GET("/service/goby/op/getComments")
    void getComments(@Header("Token") String access_token,
                     @Header("agent") String version_code,
                     @Header("network") String network,
                     @Query("id") int id,
                     @Query("type") int type,
                     @Query("page") int page,
                     @Query("number") int number,
                     Callback<CommentsBean> callback);

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
    @GET("/service/goby/fish/searchFishPoints")
    void searchFishPoints(@Header("Token") String access_token,
                          @Header("agent") String version_code,
                          @Header("network") String network,
                          @Query("page") int page,
                          @Query("number") int number,
                          @Query("keyword") String keyword,
                          @Query("longitude") double longitude,
                          @Query("latitude") double latitude,
                          Callback<FishingListBean> callback);

    /**
     * 获取图片的token
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param type
     * @param callback
     */
    @GET("/service/user/getPictureToken")
    void getPictureToken(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("type") int type,
                         Callback<PictrueTokenBean> callback);

    /**
     * 删除我的足迹
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    @GET("/service/goby/fish/delFishLocation")
    void delFishLocation(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("id") int id,
                         Callback<BaseBean> callback);

    /**
     * 获取天气详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    @GET("/service/goby/fish/getFishPointWeather")
    void getFishPointWeather(@Header("Token") String access_token,
                             @Header("agent") String version_code,
                             @Header("network") String network,
                             @Query("id") int id,
                             Callback<WeatherDetailBean> callback);

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
    @GET("/service/goby/info/searchList")
    void SearchInfoListJson(@Header("Token") String access_token,
                            @Header("agent") String version_code,
                            @Header("network") String network,
                            @Query("page") int page,
                            @Query("number") int number,
                            @Query("keyword") String keyword,
                            @Query("type") int type,
                            Callback<FishingInfoBean> callback);


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
    @FormUrlEncoded
    @POST("/service/goby/fish/postFishFeed")
    void postFishFeed(@Header("Token") String access_token,
                      @Header("agent") String version_code,
                      @Header("network") String network,
                      @Field("longitude") double longitude,
                      @Field("latitude") double latitude,
                      @Field("number") int number,
                      @Field("info") String info,
                      @Field("fishPoint_id") int fishPoint_id,
                      @Field("picture_number") int picture_number,
                      @Field("code") String code,
                      @Field("city_name") String city_name,
                      @Field("address") String address,
                      @Field("tools") String tools,
                      @Field("quality") int quality,
                      @Field("tag_id") int tag_id,
                      @Field("weather") String weather,
                      @Field("type_ids") String type_ids,
                      Callback<AddFooterprintBean> callback);

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
    @GET("/service/goby/fish/getFishRank")
    void getFishRankJson(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("page") int page,
                         @Query("number") int number,
                         @Query("tag_id") int tag_id,
                         Callback<FishRankBean> callback);

    /**
     * 绑定设备
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param device_token
     * @param callback
     */
    @GET("/service/user/bindDeviceToken")
    void getBindDeviceTokenJson(@Header("Token") String access_token,
                                @Header("agent") String version_code,
                                @Header("network") String network,
                                @Query("device_token") String device_token,
                                Callback<BaseBean> callback);

    /**
     * 获取活动详情
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param callback
     */
    @GET("/service/goby/activity/getActivity")
    void getActivityJson(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("id") String id,
                         Callback<ActivityDetailBean> callback);

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
    @GET("/service/goby/activity/joinActivity")
    void joinActivityJson(@Header("Token") String access_token,
                          @Header("agent") String version_code,
                          @Header("network") String network,
                          @Query("id") String id,
                          @Query("code") String code,
                          Callback<JoinActiveBean> callback);

    /**
     * 每日签到
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param type
     * @param callback
     */
    @FormUrlEncoded
    @POST("/service/goby/op/checkout")
    void sign(@Header("Token") String access_token,
              @Header("agent") String version_code,
              @Header("network") String network,
              @Field("type") String type,
              Callback<BaseBean> callback);

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
    @GET("/service/goby/info/getTagInfos")
    void getTagInfosJson(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("page") int page,
                         @Query("number") int number,
                         @Query("id") String id,
                         @Query("ids") String ids,
                         Callback<FishingInfoBean> callback);

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
    @GET("/service/goby/activity/getActivitys")
    void getActivitysJson(@Header("Token") String access_token,
                          @Header("agent") String version_code,
                          @Header("network") String network,
                          @Query("page") int page,
                          @Query("number") int number,
                          Callback<ActiveListBean> callback);

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
    @GET("/service/goby/admin/addJing")
    void addJingJson(@Header("Token") String access_token,
                     @Header("agent") String version_code,
                     @Header("network") String network,
                     @Query("id") int id,
                     @Query("type") int type,
                     @Query("op") int op,
                     Callback<BaseBean> callback);

    /**
     * 参加活动人
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param page
     * @param number
     * @param callback
     */
    @GET("/service/goby/activity/getJoinUsers")
    void getJoinUsers(@Header("Token") String access_token,
                      @Header("agent") String version_code,
                      @Header("network") String network,
                      @Query("id") String id,
                      @Query("page") int page,
                      @Query("number") int number,
                      Callback<JoinUsersListBean> callback);

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
    @GET("/service/goby/info/getTags")
    void getInfoTags(@Header("Token") String access_token,
                     @Header("agent") String version_code,
                     @Header("network") String network,
                     @Query("page") int page,
                     @Query("number") int number,
                     Callback<TagsListBean> callback);

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
    @GET("/service/goby/activity/getFishFunFeeds")
    void getFishFunFeeds(@Header("Token") String access_token,
                         @Header("agent") String version_code,
                         @Header("network") String network,
                         @Query("page") int page,
                         @Query("number") int number,
                         Callback<FeedsFunListBean> callback);

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
    @GET("/service/goby/activity/getMyFishFunFeeds")
    void getMyFishFunFeeds(@Header("Token") String access_token,
                           @Header("agent") String version_code,
                           @Header("network") String network,
                           @Query("page") int page,
                           @Query("number") int number,
                           Callback<FeedsFunListBean> callback);

    /**
     * 參加活動
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param code
     * @param callback
     */
    @GET("/service/goby/activity/joinFishFun")
    void getjoinFishFun(@Header("Token") String access_token,
                        @Header("agent") String version_code,
                        @Header("network") String network,
                        @Query("code") String code,
                        Callback<JoinFeedFunBean> callback);

    /**
     * 获取城市列表
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param callback
     */
    @GET("/service/goby/common/getShowCitys")
    void getCityList(@Header("Token") String access_token,
                     @Header("agent") String version_code,
                     @Header("network") String network,
                     Callback<FishCityListBean> callback);

    @GET("/service/goby/discovery/getMenus")
    void getMenus(@Header("Token") String access_token,
                  @Header("agent") String version_code,
                  @Header("network") String network,
                  Callback<MenuListBean> callback);

    @GET("/service/goby/discovery/getRecommends")
    void getRecommends(@Header("Token") String access_token,
                       @Header("agent") String version_code,
                       @Header("network") String network,
                       Callback<RecommendsListBean> callback);

    @GET("/service/user/getMeInfo")
    void getUserInfo(@Header("Token") String access_token,
                     @Header("agent") String version_code,
                     @Header("network") String network,
                     Callback<MyInfoBean> callback);

    @GET("/service/goby/info/getFavoriteInfos")
    void getFavoriteInfos(@Header("Token") String access_token,
                          @Header("agent") String version_code,
                          @Header("network") String network,
                          @Query("page") int page,
                          @Query("number") int number,
                          @Query("type") int type,
                          Callback<FishingInfoBean> callback);

    @GET("/service/goby/op/readAll")
    void readAll(@Header("Token") String access_token,
                 @Header("agent") String version_code,
                 @Header("network") String network,
                 Callback<BaseBean> callback);

    @GET("/service/user/getCity")
    void getCityIds(@Header("Token") String access_token,
                    @Header("agent") String version_code,
                    @Header("network") String network,
                    Callback<CityListBean> callback);

}
