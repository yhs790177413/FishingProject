package com.goby.fishing.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.utils.helper.java.security.DesHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class FindOutPasswordActivity extends AbsBaseActivity implements
        OnClickListener {

    private TextView tv_leftBack;

    private Button btn_sure;

    private EditText et_code;

    private EditText et_password;

    private EditText et_surePassword;

    public static void launch(Activity activity, String mobile) {

        Intent intent = new Intent(activity, FindOutPasswordActivity.class);
        intent.putExtra("mobile", mobile);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findout_password);

        initView();
    }

    public void initView() {

        tv_leftBack = (TextView) findViewById(R.id.left_back_btn);
        btn_sure = (Button) findViewById(R.id.sure_btn);

        et_code = (EditText) findViewById(R.id.code_edit);
        et_password = (EditText) findViewById(R.id.password);
        et_surePassword = (EditText) findViewById(R.id.password_again);

        tv_leftBack.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_btn:
                finish();
                break;
            case R.id.sure_btn:
                if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                    ToastHelper.showToast(this, "验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
                    ToastHelper.showToast(this, "密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et_surePassword.getText().toString().trim())) {
                    ToastHelper.showToast(this, "确认密码不能为空");
                    return;
                }
                if (!et_password.getText().toString().trim()
                        .equals(et_surePassword.getText().toString().trim())) {
                    ToastHelper.showToast(this, "两次输入密码不一致");
                    return;
                }
                try {
                    UserBusinessController.getInstance().forgetPassword(null, getVersionCode(), "2", getIntent().getStringExtra("mobile"), et_code.getText().toString().trim(),
                            DesHelper.desDecrypt(et_password.getText().toString().trim()), new com.example.controller.controller.Listener<BaseBean>() {
                                @Override
                                public void onStart(Object... params) {
                                    showProgressDialog("提交数据中,请稍候...");
                                }

                                @Override
                                public void onComplete(BaseBean bean, Object... params) {
                                    dismissProgressDialog();

                                        ToastHelper.showToast(FindOutPasswordActivity.this, "设置密码成功");
                                        finish();

                                }

                                @Override
                                public void onFail(String msg, Object... params) {
                                    dismissProgressDialog();
                                    ToastHelper.showToast(FindOutPasswordActivity.this, msg);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
