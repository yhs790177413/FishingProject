package com.goby.fishing.module.login;

import java.io.File;
import java.util.Map;

import org.json.JSONObject;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.controller.bean.BaseBean;
import com.example.controller.controller.UserBusinessController;
import com.example.qiniu.storage.UpCompletionHandler;
import com.example.qiniu.storage.UploadManager;
import com.goby.fishing.R;
import com.example.controller.bean.PictrueTokenBean;
import com.example.controller.bean.RegisterBean;
import com.goby.fishing.common.utils.helper.android.util.DialogBuilder;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.utils.helper.java.security.DesHelper;
import com.goby.fishing.common.utils.helper.java.util.FileTool;
import com.goby.fishing.common.utils.helper.java.util.MD5Helper;
import com.goby.fishing.common.utils.helper.java.util.Mail;
import com.goby.fishing.common.widget.imageview.ImageUtil;
import com.goby.fishing.framework.AbsBaseActivity;
import com.qiniu.android.http.ResponseInfo;

/**
 * 注册
 *
 * @author yhs
 */
public class RegisterActivity extends AbsBaseActivity implements
        OnClickListener {

    private TextView tv_leftBack;

    private ImageView iv_takePhone;

    private ImageView iv_userHeader;

    private Button btn_register;

    private File file;

    private EditText et_code;

    private EditText et_password;

    private EditText et_surePassword;

    private EditText et_nick;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private final int TAKE_PHOTO = 101;
    private final int CROP_PHOTO = 102;
    private final int PICK_PHOTO = 103;

    public static void launch(Activity activity, String mobile) {

        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra("mobile", mobile);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
    }

    public void initView() {

        tv_leftBack = (TextView) findViewById(R.id.left_back_btn);
        iv_takePhone = (ImageView) findViewById(R.id.take_phone);
        iv_userHeader = (ImageView) findViewById(R.id.user_header);
        btn_register = (Button) findViewById(R.id.register_btn);

        et_code = (EditText) findViewById(R.id.code_edit);
        et_password = (EditText) findViewById(R.id.password);
        et_surePassword = (EditText) findViewById(R.id.password_again);
        et_nick = (EditText) findViewById(R.id.nick_edit);

        tv_leftBack.setOnClickListener(this);
        iv_takePhone.setOnClickListener(this);
        iv_userHeader.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back_btn:
                finish();
                break;
            case R.id.take_phone:
                showPhotoDialog();
                break;
            case R.id.user_header:
                showPhotoDialog();
                break;
            case R.id.register_btn:
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
                if (TextUtils.isEmpty(et_nick.getText().toString().trim())) {
                    ToastHelper.showToast(this, "昵称不能为空");
                    return;
                }
                if (!et_password.getText().toString().trim()
                        .equals(et_surePassword.getText().toString().trim())) {
                    ToastHelper.showToast(this, "两次输入密码不一致");
                    return;
                }
                try {
                    UserBusinessController.getInstance().registerJson(null, getVersionCode(), "2", getIntent().getStringExtra("mobile"), et_code.getText().toString().trim(), et_nick.getText().toString().trim(),
                            DesHelper.desDecrypt(et_password.getText().toString().trim()), null, sharedPreferenceUtil.getDeviceToken(), new com.example.controller.controller.Listener<BaseBean>() {
                                @Override
                                public void onStart(Object... params) {
                                    showProgressDialog("提交数据中,请稍候...");
                                }

                                @Override
                                public void onComplete(BaseBean bean, Object... params) {
                                    dismissProgressDialog();

                                        sharedPreferenceUtil.setBindDeviceToken(true);
                                        ToastHelper.showToast(RegisterActivity.this, "注册成功");
                                        finish();

                                }

                                @Override
                                public void onFail(String msg, Object... params) {
                                    dismissProgressDialog();
                                    ToastHelper.showToast(RegisterActivity.this, msg);
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

    private void showPhotoDialog() {
        DialogBuilder dialog = new DialogBuilder(this, "center");
        dialog.setItems(R.array.sign,
                new DialogBuilder.OnDialogItemClickListener() {

                    @Override
                    public void OnDialogItemClick(Context context,
                                                  DialogBuilder builder, Dialog dialog, int position) {
                        if (position == 0) {
                            takePhoto();
                        } else {
                            pickPhoto();
                        }

                    }

                });
        dialog.create().show();
    }

    private void takePhoto() {
        try {
            String pathName = FileTool.getImageCachePathName();
            if (pathName == null) {
                return;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(new File(pathName));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, TAKE_PHOTO);
            Mail.putMail("photo_file", uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");

        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:// 拍照上传
                    // 截图
                    Uri uri = (Uri) Mail.getMail("photo_file");
                    String _url = uri.getPath();
                    ImageUtil.revealImageOrientation(_url);
                    cropImageUri(uri);
                    break;
                case PICK_PHOTO:// 从相册中取图片
                    cropImageUri(data.getData());
                    break;
                case CROP_PHOTO:
                    Uri uriC = (Uri) Mail.getMail("photo_file_c");
                    if (uriC == null) {
                        return;
                    }
                    String _urlC = uriC.getPath();
                    ImageUtil.revealImageOrientation(_urlC);

                    file = new File(_urlC);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    iv_takePhone.setVisibility(View.GONE);
                    iv_userHeader.setVisibility(View.VISIBLE);
                    ImageUtil.getimage(file.getAbsolutePath());
                    iv_userHeader.setImageBitmap(ImageUtil.getimage(file
                            .getAbsolutePath()));
                    break;
                default:
                    break;
            }
        }
    }

    private void cropImageUri(Uri uri) {
        String pathName = FileTool.getImagePathName();
        if (pathName == null) {
            return;
        }
        Uri uriC = Uri.fromFile(new File(pathName));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriC);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO);
        Mail.putMail("photo_file_c", uriC);
    }

}
