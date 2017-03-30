package com.example.controller.controller;

import android.util.Log;

import com.example.controller.ErrorUtil;
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
import com.example.controller.exception.ClientException;
import com.example.controller.store.HttpBusinessStore;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 业务控制器
 * <p/>
 * Created by yanghs on 2016/5/18.
 */
public class UserBusinessController implements IBusinessController {

    private HttpBusinessStore mRemoteBusinessStore;

    private UserBusinessController() {
    }

    public static UserBusinessController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public HttpBusinessStore getRemoteBusinessStore() {
        if (mRemoteBusinessStore == null) {
            mRemoteBusinessStore = new HttpBusinessStore(getInstance());
        }

        return mRemoteBusinessStore;
    }

    private static class SingletonHolder {
        public static final UserBusinessController INSTANCE = new UserBusinessController();
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
     * @param listener
     */
    public void login(final String access_token, final String version_code, final String network, final String mobile, final String password, final String device_token, final Listener<LoginBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().login(access_token, version_code, network, mobile, password, device_token,
                    new Callback<LoginBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(LoginBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * 注销
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param device_token
     * @param listener
     */
    public void logout(String access_token, String version_code, String network, String device_token, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().logout(access_token, version_code, network, device_token,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * 获取验证码
     *
     * @param access_token
     * @param version_code
     * @param network
     * @param mobile
     * @param type
     * @param listener
     */
    public void verifyCodeJson(String access_token, String version_code, String network, String mobile, int type, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().verifyCodeJson(access_token, version_code, network, mobile, type,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param mobile
     * @param verify_code
     * @param user_name
     * @param password
     * @param head_pic
     * @param device_token
     * @param listener
     */
    public void registerJson(String access_token, String version_code, String network, String mobile, String verify_code, String user_name, String password, String head_pic, String device_token, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().registerJson(access_token, version_code, network, mobile, verify_code, user_name, password, head_pic, device_token,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param type
     * @param listener
     */
    public void fishingInfoJson(String access_token, String version_code, String network, int page, int number, int type, final Listener<FishingInfoBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().fishingInfoJson(access_token, version_code, network, page, number, type,
                    new Callback<FishingInfoBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingInfoBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param listener
     */
    public void fishingInfoDetailJson(String access_token, String version_code, String network, int id, final Listener<FishingInfoDetailBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().fishingInfoDetailJson(access_token, version_code, network, id,
                    new Callback<FishingInfoDetailBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingInfoDetailBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
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
     * @param listener
     */
    public void fishingListJson(String access_token, String version_code, String network, int water_type, int city, int page, int number, int sort, double latitude, double longitude, final Listener<FishingListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().fishingListJson(access_token, version_code, network, water_type, city, page, number, sort, latitude, longitude,
                    new Callback<FishingListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param latitude
     * @param longitude
     * @param listener
     */
    public void fishingDetailJson(String access_token, String version_code, String network, int id, double latitude, double longitude, final Listener<FishingDetialBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().fishingDetailJson(access_token, version_code, network, id, latitude, longitude,
                    new Callback<FishingDetialBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingDetialBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param head_pic
     * @param user_name
     * @param sex
     * @param birthday
     * @param city
     * @param listener
     */
    public void updateUserInfo(String access_token, String version_code, String network, String head_pic, String user_name, int sex, String birthday, int city, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().updateUserInfo(access_token, version_code, network, head_pic, user_name, sex, birthday, city,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param mobile
     * @param verify_code
     * @param password
     * @param listener
     */
    public void forgetPassword(String access_token, String version_code, String network, String mobile, String verify_code, String password, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().forgetPassword(access_token, version_code, network, mobile, verify_code, password,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param page
     * @param number
     * @param type
     * @param latitude
     * @param longitude
     * @param listener
     */
    public void getFishFeeds(String access_token, String version_code, String network, int id, int page, int number, int type, double latitude, double longitude, final Listener<FooterprintListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getFishFeeds(access_token, version_code, network, id, page, number, type, latitude, longitude,
                    new Callback<FooterprintListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FooterprintListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param latitude
     * @param longitude
     * @param listener
     */
    public void footerprintDetail(String access_token, String version_code, String network, int id, double latitude, double longitude, final Listener<FooterprintDetailBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().footerprintDetail(access_token, version_code, network, id, latitude, longitude,
                    new Callback<FooterprintDetailBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FooterprintDetailBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param latitude
     * @param longitude
     * @param listener
     */
    public void favoriteFishingPoint(String access_token, String version_code, String network, int page, int number, double latitude, double longitude, final Listener<FishingListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().favoriteFishingPoint(access_token, version_code, network, page, number, latitude, longitude,
                    new Callback<FishingListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param latitude
     * @param longitude
     * @param listener
     */
    public void favoriteFooterprint(String access_token, String version_code, String network, int page, int number, double latitude, double longitude, final Listener<FooterprintListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().favoriteFooterprint(access_token, version_code, network, page, number, latitude, longitude,
                    new Callback<FooterprintListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FooterprintListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type
     * @param op
     * @param listener
     */
    public void collection(String access_token, String version_code, String network, int id, int type, int op, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().collection(access_token, version_code, network, id, type, op,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param page
     * @param number
     * @param type
     * @param listener
     */
    public void getAttentionList(String access_token, String version_code, String network, String user_id, int page, int number, int type, final Listener<AttentionFriendsBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getAttentionList(access_token, version_code, network, user_id, page, number, type,
                    new Callback<AttentionFriendsBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(AttentionFriendsBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param page
     * @param number
     * @param latitude
     * @param longitude
     * @param listener
     */
    public void getMyFooterprintList(String access_token, String version_code, String network, String user_id, int page, int number, double latitude, double longitude, final Listener<FooterprintListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getMyFooterprintList(access_token, version_code, network, user_id, page, number, latitude, longitude,
                    new Callback<FooterprintListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FooterprintListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param type
     * @param listener
     */
    public void getMyMessageList(String access_token, String version_code, String network, int page, int number, int type, final Listener<MyMessageBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getMyMessageList(access_token, version_code, network, page, number, type,
                    new Callback<MyMessageBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(MyMessageBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param listener
     */
    public void fishTypeJson(String access_token, String version_code, String network, int page, int number, final Listener<FishTypeBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().fishTypeJson(access_token, version_code, network, page, number,
                    new Callback<FishTypeBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishTypeBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type
     * @param op
     * @param listener
     */
    public void getLike(String access_token, String version_code, String network, int id, int type, int op, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getLike(access_token, version_code, network, id, type, op,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type
     * @param comment
     * @param reply_id
     * @param reply_user_id
     * @param listener
     */
    public void comment(String access_token, String version_code, String network, int id, int type, String comment, String reply_id, String reply_user_id, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().comment(access_token, version_code, network, id, type, comment, reply_id, reply_user_id,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param op
     * @param listener
     */
    public void follow(String access_token, String version_code, String network, String user_id, int op, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().follow(access_token, version_code, network, user_id, op,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param listener
     */
    public void notifyFishLocation(String access_token, String version_code, String network, int id, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().notifyFishLocation(access_token, version_code, network, id,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param keyword
     * @param listener
     */
    public void searchList(String access_token, String version_code, String network, int page, int number, String keyword, int type, final Listener<FishingInfoBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().searchList(access_token, version_code, network, page, number, keyword, type,
                    new Callback<FishingInfoBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingInfoBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param listener
     */
    public void getOtherUserInfo(String access_token, String version_code, String network, String user_id, final Listener<MyInfoBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getOtherUserInfo(access_token, version_code, network, user_id,
                    new Callback<MyInfoBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(MyInfoBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param user_id
     * @param longitude
     * @param latitude
     * @param listener
     */
    public void getUserFishLocations(String access_token, String version_code, String network, int page, int number, String user_id, double longitude, double latitude, final Listener<FooterprintListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getUserFishLocations(access_token, version_code, network, page, number, user_id, longitude, latitude,
                    new Callback<FooterprintListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FooterprintListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param user_id
     * @param listener
     */
    public void getMessageSession(String access_token, String version_code, String network, int page, int number, String user_id, final Listener<MyMessageBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getMessageSession(access_token, version_code, network, page, number, user_id,
                    new Callback<MyMessageBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(MyMessageBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param user_id
     * @param message
     * @param listener
     */
    public void sendMessage(String access_token, String version_code, String network, String user_id, String message, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().sendMessage(access_token, version_code, network, user_id, message,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param outer_id
     * @param type
     * @param sex
     * @param head_pic
     * @param user_name
     * @param device_token
     * @param listener
     */
    public void getAutoLogin(String access_token, String version_code, String network, String outer_id, int type, int sex, String head_pic, String user_name, String device_token, final Listener<LoginBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getAutoLogin(access_token, version_code, network, outer_id, type, sex, head_pic, user_name, device_token,
                    new Callback<LoginBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(LoginBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type
     * @param page
     * @param number
     * @param listener
     */
    public void getComments(String access_token, String version_code, String network, int id, int type, int page, int number, final Listener<CommentsBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getComments(access_token, version_code, network, id, type, page, number,
                    new Callback<CommentsBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(CommentsBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param keyword
     * @param longitude
     * @param latitude
     * @param listener
     */
    public void searchFishPoints(String access_token, String version_code, String network, int page, int number, String keyword, double longitude, double latitude, final Listener<FishingListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().searchFishPoints(access_token, version_code, network, page, number, keyword, longitude, latitude,
                    new Callback<FishingListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param type
     * @param listener
     */
    public void getPictureToken(String access_token, String version_code, String network, int type, final Listener<PictrueTokenBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getPictureToken(access_token, version_code, network, type,
                    new Callback<PictrueTokenBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(PictrueTokenBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param listener
     */
    public void delFishLocation(String access_token, String version_code, String network, int id, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().delFishLocation(access_token, version_code, network, id,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param listener
     */
    public void getFishPointWeather(String access_token, String version_code, String network, int id, final Listener<WeatherDetailBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getFishPointWeather(access_token, version_code, network, id,
                    new Callback<WeatherDetailBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(WeatherDetailBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param keyword
     * @param type
     * @param listener
     */
    public void SearchInfoListJson(String access_token, String version_code, String network, int page, int number, String keyword, int type, final Listener<FishingInfoBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().SearchInfoListJson(access_token, version_code, network, page, number, keyword, type,
                    new Callback<FishingInfoBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingInfoBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
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
     * @param listener
     */
    public void postFishFeed(String access_token, String version_code, String network, double longitude, double latitude, int number, String info, int fishPoint_id, int picture_number, String code, String city_name,
                             String address, String tools, int quality, int tag_id, String weather, String type_ids, final Listener<AddFooterprintBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().postFishFeed(access_token, version_code, network, longitude, latitude, number, info, fishPoint_id,
                    picture_number, code, city_name, address, tools, quality, tag_id, weather, type_ids,
                    new Callback<AddFooterprintBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(AddFooterprintBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param tag_id
     * @param listener
     */
    public void getFishRankJson(String access_token, String version_code, String network, int page, int number, int tag_id, final Listener<FishRankBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getFishRankJson(access_token, version_code, network, page, number, tag_id,
                    new Callback<FishRankBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishRankBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param device_token
     * @param listener
     */
    public void getBindDeviceTokenJson(String access_token, String version_code, String network, String device_token, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getBindDeviceTokenJson(access_token, version_code, network, device_token,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param listener
     */
    public void getActivityJson(String access_token, String version_code, String network, String id, final Listener<ActivityDetailBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getActivityJson(access_token, version_code, network, id,
                    new Callback<ActivityDetailBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(ActivityDetailBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param code
     * @param listener
     */
    public void joinActivityJson(String access_token, String version_code, String network, String id, String code, final Listener<JoinActiveBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().joinActivityJson(access_token, version_code, network, id, code,
                    new Callback<JoinActiveBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(JoinActiveBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param type
     * @param listener
     */
    public void sign(String access_token, String version_code, String network, String type, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().sign(access_token, version_code, network, type,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param id
     * @param ids
     * @param listener
     */
    public void getTagInfosJson(String access_token, String version_code, String network, int page, int number, String id, String ids, final Listener<FishingInfoBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getTagInfosJson(access_token, version_code, network, page, number, id, ids,
                    new Callback<FishingInfoBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingInfoBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param listener
     */
    public void getActivitysJson(String access_token, String version_code, String network, int page, int number, final Listener<ActiveListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getActivitysJson(access_token, version_code, network, page, number,
                    new Callback<ActiveListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(ActiveListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param type
     * @param op
     * @param listener
     */
    public void addJingJson(String access_token, String version_code, String network, int id, int type, int op, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().addJingJson(access_token, version_code, network, id, type, op,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param id
     * @param page
     * @param number
     * @param listener
     */
    public void getJoinUsers(String access_token, String version_code, String network, String id, int page, int number, final Listener<JoinUsersListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getJoinUsers(access_token, version_code, network, id, page, number,
                    new Callback<JoinUsersListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(JoinUsersListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param listener
     */
    public void getInfoTags(String access_token, String version_code, String network, int page, int number, final Listener<TagsListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getInfoTags(access_token, version_code, network, page, number,
                    new Callback<TagsListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(TagsListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param code
     * @param listener
     */
    public void getjoinFishFun(String access_token, String version_code, String network, String code, final Listener<JoinFeedFunBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getjoinFishFun(access_token, version_code, network, code,
                    new Callback<JoinFeedFunBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(JoinFeedFunBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param listener
     */
    public void getFishFunFeeds(String access_token, String version_code, String network, int page, int number, final Listener<FeedsFunListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getFishFunFeeds(access_token, version_code, network, page, number,
                    new Callback<FeedsFunListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FeedsFunListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param page
     * @param number
     * @param listener
     */
    public void getMyFishFunFeeds(String access_token, String version_code, String network, int page, int number, final Listener<FeedsFunListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getMyFishFunFeeds(access_token, version_code, network, page, number,
                    new Callback<FeedsFunListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FeedsFunListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    /**
     * @param access_token
     * @param version_code
     * @param network
     * @param listener
     */
    public void getCityList(String access_token, String version_code, String network, final Listener<FishCityListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getCityList(access_token, version_code, network,
                    new Callback<FishCityListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishCityListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    public void getMenus(String access_token, String version_code, String network, final Listener<MenuListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getMenus(access_token, version_code, network,
                    new Callback<MenuListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(MenuListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    public void getRecommends(String access_token, String version_code, String network, final Listener<RecommendsListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getRecommends(access_token, version_code, network,
                    new Callback<RecommendsListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(RecommendsListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    public void getUserInfo(String access_token, String version_code, String network, final Listener<MyInfoBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getUserInfo(access_token, version_code, network,
                    new Callback<MyInfoBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(MyInfoBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    public void getFavoriteInfos(String access_token, String version_code, String network, int page, int number, int type, final Listener<FishingInfoBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getFavoriteInfos(access_token, version_code, network, page, number, type,
                    new Callback<FishingInfoBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(FishingInfoBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    public void readAll(String access_token, String version_code, String network, final Listener<BaseBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().readAll(access_token, version_code, network,
                    new Callback<BaseBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(BaseBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    public void getCityIds(String access_token, String version_code, String network, final Listener<CityListBean> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {
            getRemoteBusinessStore().getCityIds(access_token, version_code, network,
                    new Callback<CityListBean>() {
                        @Override
                        public void failure(RetrofitError error) {
                            listener.onFail(ErrorString());
                        }

                        @Override
                        public void success(CityListBean result, Response arg1) {
                            if (result != null) {
                                if (result.ok()) {
                                    listener.onComplete(result);
                                } else {
                                    listener.onFail(ErrorUtil.getServerError(result.message));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }
    }

    public String ErrorString() {
        if (ClientException.mExceptionCode == ClientException.NET_WORK_ERROR) {
            //网络异常
            return ErrorUtil.getNetworkError();
        } else if (ClientException.mExceptionCode == ClientException.HTTP_ERROR) {
            //非200错误, 如返回码为40x 50x
            return ErrorUtil.getHttpError();
        } else if (ClientException.mExceptionCode == ClientException.CONVERSION_ERROR) {
            //转化异常，解析数据时异常
            return ErrorUtil.getConversionError();
        } else if (ClientException.mExceptionCode == ClientException.UNEXPECTED_ERROR) {
            //未知异常
            return ErrorUtil.getUnExceptionError();
        }
        return ErrorUtil.getUnExceptionError();
    }
}
