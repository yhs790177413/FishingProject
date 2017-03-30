package com.goby.fishing.module.login;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.GuideActivity;
import com.goby.fishing.MainActivity;
import com.goby.fishing.R;
import com.example.controller.bean.LoginBean;
import com.goby.fishing.common.utils.helper.android.app.ActivitiesHelper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.utils.helper.java.security.DesHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.index.ActiveDetailActivity;
import com.goby.fishing.module.me.MeFragment;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 登录界面
 *
 * @author yhs
 */
public class LoginActivity extends AbsBaseActivity implements OnClickListener {

    private TextView tv_leftBack;

    private TextView tv_register;

    private EditText et_mobile;

    private EditText et_password;

    private Button btn_login;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private String mFromViewName;

    private TextView tv_forgetPassword;

    private String open_id = "";
    private int sex;
    private String srceenName;
    private String headerImageUrl;

    public static void launch(Activity activity, String fromViewName) {

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("fromViewName", fromViewName);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, String fromViewName,
                              int requestCode) {

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("fromViewName", fromViewName);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFromViewName = getIntent().getStringExtra("fromViewName");
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
    }

    public void initView() {

        tv_leftBack = (TextView) findViewById(R.id.left_back_btn);
        tv_register = (TextView) findViewById(R.id.register_btn);

        et_mobile = (EditText) findViewById(R.id.phone_number);
        et_password = (EditText) findViewById(R.id.password);

        btn_login = (Button) findViewById(R.id.login_btn);
        tv_forgetPassword = (TextView) findViewById(R.id.forget_password);

        tv_leftBack.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        if (!TextUtils.isEmpty(sharedPreferenceUtil.getUserPassword())) {
            et_mobile.setText(sharedPreferenceUtil.getUserPhone());
            et_password.setText(sharedPreferenceUtil.getUserPassword());
        }
        findViewById(R.id.qq_login).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                QQ_Login();
            }
        });
        findViewById(R.id.weixin_login).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        WEIXIN_Login();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_btn:
                if (mFromViewName.equals("meFragment")) {
                    MeFragment.unLogin = true;
                    setResult(RESULT_OK);
                    finish();
                } else {
                    finish();
                }

                break;
            case R.id.register_btn:
                RegisterGetCodeActivity.launch(this, 0);
                break;
            case R.id.login_btn:
                if (TextUtils.isEmpty(et_mobile.getText().toString().trim())) {
                    ToastHelper.showToast(this, "手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
                    ToastHelper.showToast(this, "密码不能为空");
                    return;
                }
                try {
                    UserBusinessController.getInstance().login(null, getVersionCode(), "2", et_mobile.getText().toString().trim(), DesHelper.desDecrypt(et_password.getText().toString().trim()), sharedPreferenceUtil.getDeviceToken(), new com.example.controller.controller.Listener<com.example.controller.bean.LoginBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在登录中...");
                        }

                        @Override
                        public void onComplete(com.example.controller.bean.LoginBean result, Object... params) {
                            dismissProgressDialog();

                                sharedPreferenceUtil.setBindDeviceToken(true);
                                if (mFromViewName.equals("login")) {
                                    sharedPreferenceUtil.setUserToken(result.data.token);
                                    sharedPreferenceUtil.setUserPhone(et_mobile.getText()
                                            .toString().trim());
                                    sharedPreferenceUtil.setUserPassword(et_password.getText()
                                            .toString().trim());
                                    ToastHelper.showToast(LoginActivity.this, "登录成功");
                                    MainActivity.launch(LoginActivity.this, false);
                                    ActivitiesHelper.getInstance().closeTarget(
                                            GuideActivity.class);
                                    finish();
                                } else if (mFromViewName.equals("meFragment")) {
                                    MeFragment.is_refresh = true;
                                    sharedPreferenceUtil.setUserToken(result.data.token);
                                    ToastHelper.showToast(LoginActivity.this, "登录成功");
                                    finish();
                                } else if (mFromViewName.equals("active")) {
                                    ActiveDetailActivity.isResume = true;
                                    sharedPreferenceUtil.setUserToken(result.data.token);
                                    ToastHelper.showToast(LoginActivity.this, "登录成功");
                                    finish();
                                } else if (mFromViewName.equals("collection")) {
                                    sharedPreferenceUtil.setUserToken(result.data.token);
                                    ToastHelper.showToast(LoginActivity.this, "登录成功");
                                    finish();
                                } else {
                                    sharedPreferenceUtil.setUserToken(result.data.token);
                                    ToastHelper.showToast(LoginActivity.this, "登录成功");
                                    finish();
                                }

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(LoginActivity.this, msg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.forget_password:
                RegisterGetCodeActivity.launch(this, 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (mFromViewName.equals("meFragment")) {
            MeFragment.unLogin = true;
            setResult(RESULT_OK);
            finish();
        } else {
            finish();
        }
    }

    /**
     * QQ授权登录
     */
    public void QQ_Login() {
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,
                SHARE_MEDIA.QQ, new UMAuthListener() {

                    @Override
                    public void onCancel(SHARE_MEDIA arg0, int arg1) {
                        // TODO Auto-generated method stub
                        ToastHelper.showToast(LoginActivity.this, "授权取消");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int status,
                                           Map<String, String> info) {
                        ToastHelper.showToast(LoginActivity.this, "授权成功");
                        // TODO Auto-generated method stub
                        if (info.get("gender").toString()
                                .equals("男")) {
                            sex = 1;
                        } else if (info.get("gender")
                                .toString().equals("女")) {
                            sex = 2;
                        }
                        UserBusinessController.getInstance().getAutoLogin(null, getVersionCode(), "2", info.get("openid").toString(), 3, sex, info.get("profile_image_url").toString(), info.get("screen_name").toString(),
                                sharedPreferenceUtil.getDeviceToken(), new com.example.controller.controller.Listener<LoginBean>() {
                                    @Override
                                    public void onStart(Object... params) {

                                    }

                                    @Override
                                    public void onComplete(LoginBean bean, Object... params) {
                                        dismissProgressDialog();

                                            if (mFromViewName.equals("login")) {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                sharedPreferenceUtil.setUserPhone(et_mobile.getText()
                                                        .toString().trim());
                                                sharedPreferenceUtil.setUserPassword(et_password.getText()
                                                        .toString().trim());
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                MainActivity.launch(LoginActivity.this, false);
                                                ActivitiesHelper.getInstance().closeTarget(
                                                        GuideActivity.class);
                                                finish();
                                            } else if (mFromViewName.equals("meFragment")) {
                                                MeFragment.is_refresh = true;
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            } else if (mFromViewName.equals("active")) {
                                                ActiveDetailActivity.isResume = true;
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            } else if (mFromViewName.equals("collection")) {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            } else {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            }

                                    }

                                    @Override
                                    public void onFail(String msg, Object... params) {
                                        ToastHelper.showToast(LoginActivity.this, msg);
                                    }
                                });
                    }

                    @Override
                    public void onError(SHARE_MEDIA arg0, int arg1,
                                        Throwable arg2) {
                        // TODO Auto-generated method stub
                    }

                });
    }

    /**
     * 微信授权登录
     */
    public void WEIXIN_Login() {

        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,
                SHARE_MEDIA.WEIXIN, new UMAuthListener() {

                    @Override
                    public void onCancel(SHARE_MEDIA arg0,
                                         int arg1) {
                        // TODO Auto-generated method stub
                        ToastHelper.showToast(
                                LoginActivity.this, "授权取消");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0,
                                           int status, Map<String, String> info) {
                        // TODO Auto-generated method stub
                        ToastHelper.showToast(
                                LoginActivity.this, "授权成功");
                        UserBusinessController.getInstance().getAutoLogin(null, getVersionCode(), "2", info.get("openid").toString(),1,Integer.parseInt(info.get("gender").toString()),info.get("profile_image_url").toString(),info.get("screen_name").toString(),
                                sharedPreferenceUtil.getDeviceToken(), new com.example.controller.controller.Listener<LoginBean>() {
                                    @Override
                                    public void onStart(Object... params) {

                                    }

                                    @Override
                                    public void onComplete(LoginBean bean, Object... params) {
                                        dismissProgressDialog();

                                            if (mFromViewName.equals("login")) {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                sharedPreferenceUtil.setUserPhone(et_mobile.getText()
                                                        .toString().trim());
                                                sharedPreferenceUtil.setUserPassword(et_password.getText()
                                                        .toString().trim());
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                MainActivity.launch(LoginActivity.this, false);
                                                ActivitiesHelper.getInstance().closeTarget(
                                                        GuideActivity.class);
                                                finish();
                                            } else if (mFromViewName.equals("meFragment")) {
                                                MeFragment.is_refresh = true;
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            } else if (mFromViewName.equals("active")) {
                                                ActiveDetailActivity.isResume = true;
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            } else if (mFromViewName.equals("collection")) {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            } else {
                                                sharedPreferenceUtil.setUserToken(bean.data.token);
                                                ToastHelper.showToast(LoginActivity.this, "登录成功");
                                                finish();
                                            }

                                    }

                                    @Override
                                    public void onFail(String msg, Object... params) {
                                        ToastHelper.showToast(LoginActivity.this, msg);
                                    }
                                });
                    }

                    @Override
                    public void onError(SHARE_MEDIA arg0,
                                        int arg1, Throwable arg2) {
                        ToastHelper.showToast(
                                LoginActivity.this, "授权失败");
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
