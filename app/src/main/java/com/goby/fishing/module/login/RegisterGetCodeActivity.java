package com.goby.fishing.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

/**
 * 注册--获取验证码
 *
 * @author yhs
 */
public class RegisterGetCodeActivity extends AbsBaseActivity implements
        OnClickListener {

    private TextView tv_leftBack;

    private EditText et_mobile;

    private Button btn_getCode;

    private int mType;

    public static void launch(Activity activity, int type) {

        Intent intent = new Intent(activity, RegisterGetCodeActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_getcode);

        mType = getIntent().getIntExtra("type", -1);
        initView();
    }

    public void initView() {

        et_mobile = (EditText) findViewById(R.id.phone_number);
        tv_leftBack = (TextView) findViewById(R.id.left_back_btn);
        btn_getCode = (Button) findViewById(R.id.get_code);

        tv_leftBack.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_btn:
                finish();
                break;
            case R.id.get_code:
                if (TextUtils.isEmpty(et_mobile.getText().toString().trim())) {
                    ToastHelper.showToast(RegisterGetCodeActivity.this, "手机号不能为空");
                    return;
                }
                if (et_mobile.getText().toString().trim().length() != 11) {
                    ToastHelper.showToast(RegisterGetCodeActivity.this,
                            "输入有误,请输入正确的11位号码");
                    return;
                }
                UserBusinessController.getInstance().verifyCodeJson(null, getVersionCode(), "2", et_mobile.getText().toString().trim(), mType, new com.example.controller.controller.Listener<BaseBean>() {
                    @Override
                    public void onStart(Object... params) {
                        showProgressDialog("正在获取验证码中,请稍候...");
                    }

                    @Override
                    public void onComplete(BaseBean bean, Object... params) {
                        dismissProgressDialog();

                            ToastHelper.showToast(RegisterGetCodeActivity.this, "验证码已发送");
                            if (mType == 0) {
                                RegisterActivity.launch(RegisterGetCodeActivity.this, et_mobile
                                        .getText().toString().trim());
                                finish();
                            } else {
                                FindOutPasswordActivity.launch(RegisterGetCodeActivity.this, et_mobile
                                        .getText().toString().trim());
                                finish();
                            }


                    }

                    @Override
                    public void onFail(String msg, Object... params) {
                        dismissProgressDialog();
                        ToastHelper.showToast(RegisterGetCodeActivity.this, msg);
                    }
                });
                break;
            default:
                break;
        }
    }
}
