package com.goby.fishing.module.me;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

import com.example.controller.Constant;
import com.example.controller.controller.UserBusinessController;
import com.example.qiniu.storage.UpCompletionHandler;
import com.example.qiniu.storage.UploadManager;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.example.controller.bean.CityListBean;
import com.example.controller.bean.PictrueTokenBean;
import com.example.controller.bean.CityListBean.Data.City;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.CityUtils;
import com.goby.fishing.common.utils.helper.android.util.DialogBuilder;
import com.goby.fishing.common.utils.helper.android.util.ListDialogUtil;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.util.TgDateTimePicker;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.common.utils.helper.java.util.FileTool;
import com.goby.fishing.common.utils.helper.java.util.Mail;
import com.goby.fishing.common.widget.imageview.ImageUtil;
import com.goby.fishing.framework.AbsBaseActivity;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;

public class ModifyActivity extends AbsBaseActivity implements OnClickListener {

    private RelativeLayout rl_userHeader;

    private RelativeLayout rl_modifyNick;

    private RelativeLayout rl_sex;

    private RelativeLayout rl_city;

    private RelativeLayout rl_birthday;

    private TextView tv_nick;

    private TextView tv_sex;

    private TextView tv_cityName;

    private TextView tv_birthday;

    private ImageView icv_userHeader;

    private ImageView iv_back;

    private UiHandler uiHandler;

    private String mHeaderPic;

    private String mUserName;

    private int mSex;

    private String mBirthday;

    private String mCityName;

    private int cityNo;

    private LinearLayout ll_leftBack;

    private ArrayList<City> cityList = new ArrayList<City>();

    private SharedPreferenceUtil sharedPreferenceUtil;

    private String picturePath;

    private boolean updateUserHeader = false;

    private final int TAKE_PHOTO = 101;
    private final int CROP_PHOTO = 102;
    private final int PICK_PHOTO = 103;
    private final int NICK = 104;

    public static void launch(Activity activity, String head_pic,
                              String user_name, int sex, String birthday, String city_name) {

        Intent intent = new Intent(activity, ModifyActivity.class);
        intent.putExtra("head_pic", head_pic);
        intent.putExtra("user_name", user_name);
        intent.putExtra("sex", sex);
        intent.putExtra("birthday", birthday);
        intent.putExtra("city_name", city_name);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);

        mHeaderPic = getIntent().getStringExtra("head_pic");
        mUserName = getIntent().getStringExtra("user_name");
        mSex = getIntent().getIntExtra("sex", -1);
        mBirthday = getIntent().getStringExtra("birthday");
        mCityName = getIntent().getStringExtra("city_name");

        uiHandler = new UiHandler();
        initView();
    }

    public void initView() {

        icv_userHeader = (ImageView) findViewById(R.id.user_header);
        tv_nick = (TextView) findViewById(R.id.nick_name);
        tv_sex = (TextView) findViewById(R.id.sex);
        tv_cityName = (TextView) findViewById(R.id.city_name);
        tv_birthday = (TextView) findViewById(R.id.birthday);

        if (mHeaderPic.startsWith("http://")) {
            ImageLoaderWrapper.getDefault().displayImage(mHeaderPic,
                    icv_userHeader);
        } else {
            ImageLoaderWrapper.getDefault().displayImage(
                    Constant.HOST_URL + mHeaderPic, icv_userHeader);
        }
        if (!TextUtils.isEmpty(mUserName)) {
            tv_nick.setText(mUserName);
        }

        if (mSex == 1) {
            tv_sex.setText("男");
        } else if (mSex == 2) {
            tv_sex.setText("女");
        }

        if (!TextUtils.isEmpty(mBirthday)) {
            tv_birthday.setText(mBirthday);
        }
        if (!TextUtils.isEmpty(mCityName)) {
            tv_cityName.setText(mCityName);
        }

        rl_userHeader = (RelativeLayout) findViewById(R.id.userheader_layout);
        rl_userHeader.setOnClickListener(this);

        rl_modifyNick = (RelativeLayout) findViewById(R.id.modify_nick_layout);
        rl_sex = (RelativeLayout) findViewById(R.id.sex_layout);

        rl_modifyNick.setOnClickListener(this);
        rl_sex.setOnClickListener(this);

        rl_city = (RelativeLayout) findViewById(R.id.city_layout);
        rl_city.setOnClickListener(this);

        rl_birthday = (RelativeLayout) findViewById(R.id.birthday_layout);
        rl_birthday.setOnClickListener(this);

        iv_back = (ImageView) findViewById(R.id.left_back);
        iv_back.setOnClickListener(this);

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.left_back_layout:
                finish();
                break;
            case R.id.userheader_layout:
                showPhotoDialog();
                break;
            case R.id.modify_nick_layout:
                ModifyNickActivity.launch(this,
                        tv_nick.getText().toString().trim(), NICK);
                break;
            case R.id.sex_layout:
                showGenderDialog();
                break;
            case R.id.city_layout:
                if (!TextUtils.isEmpty(sharedPreferenceUtil.getCityJson())) {
                    WindowManager m = getWindowManager();
                    Display display = m.getDefaultDisplay(); // 为获取屏幕宽、高
                    ListDialogUtil dialog_list = new ListDialogUtil(this,
                            R.style.testDialog, uiHandler);
                    Window window = dialog_list.getWindow();
                    window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
                    dialog_list.setCanceledOnTouchOutside(true);
                    dialog_list.getWindow().setLayout((int) (display.getWidth() * 0.9),
                            (int) (display.getHeight() * 0.5));
                    dialog_list.show();
                } else {
                    //获取网络数据
                    UserBusinessController.getInstance().getCityIds(null, getVersionCode(), "2", new com.example.controller.controller.Listener<CityListBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在获取数据,请稍候...");
                        }

                        @Override
                        public void onComplete(CityListBean bean, Object... params) {
                            dismissProgressDialog();

                            Gson gson = new Gson();
                            String cityJson = gson.toJson(bean);
                            sharedPreferenceUtil.setCityJson(cityJson);
                            WindowManager m = getWindowManager();
                            Display display = m.getDefaultDisplay(); // 为获取屏幕宽、高
                            ListDialogUtil dialog_list = new ListDialogUtil(ModifyActivity.this,
                                    R.style.testDialog, uiHandler);
                            Window window = dialog_list.getWindow();
                            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
                            dialog_list.setCanceledOnTouchOutside(true);
                            dialog_list.getWindow().setLayout((int) (display.getWidth() * 0.9),
                                    (int) (display.getHeight() * 0.5));
                            dialog_list.show();

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(ModifyActivity.this, msg);
                        }
                    });
                }
                break;
            case R.id.birthday_layout:
                showDatePicker();
                break;
            default:
                break;
        }
    }

    private void showDatePicker() {
        try {
            TgDateTimePicker picker = null;

            if (!TextUtils.isEmpty(tv_birthday.getText().toString().trim())
                    && !tv_birthday.getText().toString().trim().endsWith("请选择")) {
                String[] days = tv_birthday.getText().toString().trim()
                        .split("-");
                int year = Integer.parseInt(days[0]);
                int month = Integer.parseInt(days[1]);
                int day = Integer.parseInt(days[2]);
                picker = new TgDateTimePicker(this, year, month, day);
            } else {
                picker = new TgDateTimePicker(this);
            }

            picker.setOnDateTimeSelectListener(new TgDateTimePicker.OnDateTimeSelectListener() {

                @Override
                public void onSelect(int year, int month, int day) {

                    String birth = year + "-" + month + "-" + day;

                    if (!birth.equals(tv_birthday.getText().toString())) {
                        mBirthday = birth;
                        updateInfo(null, null, -1, mBirthday, -1);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * 弹出选择性别的对话框
     */
    private void showGenderDialog() {
        final DialogBuilder dialog = new DialogBuilder(this, "center");
        dialog.setTitle("修改性别");
        View v = LayoutInflater.from(this).inflate(
                R.layout.dialog_edit_personal_file_gender, null);
        final RadioButton rb1 = (RadioButton) v.findViewById(R.id.rb_dialog1);
        final RadioButton rb2 = (RadioButton) v.findViewById(R.id.rb_dialog2);
        if (tv_sex.getText().toString().trim() != null
                && !tv_sex.getText().toString().trim().equals("")) {
            if (tv_sex.getText().toString().trim().equals("男")) {
                rb1.setChecked(true);
            } else if (tv_sex.getText().toString().trim().equals("女")) {
                rb2.setChecked(true);
            }
        } else {
            rb1.setChecked(true);
        }
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.ll_dialog1:

                        mSex = 1;
                        rb1.setChecked(true);
                        rb2.setChecked(false);
                        break;
                    case R.id.ll_dialog2:
                        mSex = 2;
                        rb1.setChecked(false);
                        rb2.setChecked(true);

                        break;
                    case R.id.rb_dialog1:
                        mSex = 1;
                        rb2.setChecked(false);
                        break;
                    case R.id.rb_dialog2:
                        mSex = 2;
                        rb1.setChecked(false);
                        break;
                }

                dialog.dismiss();
                // tv_sex.setText(vaule);
                // 请求网络更新数据
                updateInfo(null, null, mSex, null, -1);
            }
        };
        rb1.setOnClickListener(listener);
        rb2.setOnClickListener(listener);
        v.findViewById(R.id.ll_dialog1).setOnClickListener(listener);
        v.findViewById(R.id.ll_dialog2).setOnClickListener(listener);
        dialog.setView(v);
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

                    File file = new File(_urlC);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    picturePath = file.getAbsolutePath();
                    icv_userHeader.setImageBitmap(ImageUtil.getimage(file
                            .getAbsolutePath()));
                    // 调用图片上传接口
                    updateUserHeader = true;
                    UserBusinessController.getInstance().getPictureToken(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", 1, new com.example.controller.controller.Listener<PictrueTokenBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在提交数据中,请稍候...");
                        }

                        @Override
                        public void onComplete(final PictrueTokenBean bean, Object... params) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // 获得七牛上传凭证uploadToken
                                    String token = bean.data.picture_token;
                                    if (token != null) {
                                        // 手机SD卡图片存放路径
                                        String data = picturePath;
                                        UploadManager uploadManager = new UploadManager();
                                        uploadManager.put(data, bean.data.path, token,
                                                new UpCompletionHandler() {

                                                    @Override
                                                    public void complete(String key,
                                                                         ResponseInfo info,
                                                                         JSONObject response) {
                                                        // 调用更新个人信息接口
                                                        updateInfo(bean.data.domain
                                                                        + bean.data.path, null, -1,
                                                                null, -1);
                                                    }
                                                }, null);
                                    } else {
                                        dismissProgressDialog();
                                        Log.i("fail", "上传头像失败");
                                    }
                                }
                            }).start();

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            ToastHelper.showToast(ModifyActivity.this, msg);
                        }
                    });
                    break;
                case NICK:
                    if (data != null) {
                        mUserName = data.getStringExtra("nick_name");
                        updateInfo(null, mUserName, -1, null, -1);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void cropImageUri(Uri uri) {
        String pathName = FileTool.getImagePathName();
        if (pathName == null) {
            Toast.makeText(this, "", Toast.LENGTH_LONG).show();
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

    private class UiHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            mCityName = (String) msg.obj;
            if (cityList == null || cityList.size() == 0) {
                Gson gson = new Gson();

                CityListBean bean = gson.fromJson(
                        sharedPreferenceUtil.getCityJson(), CityListBean.class);
                cityList.addAll(bean.data.list);
                cityNo = CityUtils.getCityIdByName(cityList, mCityName);


            }
            updateInfo(null, null, -1, null, cityNo);
        }
    }

    public void updateInfo(String header_pic, String user_name, int sex,
                           String birthday, int city_id) {
        UserBusinessController.getInstance().updateUserInfo(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", header_pic, user_name, sex, birthday, city_id, new com.example.controller.controller.Listener<BaseBean>() {
            @Override
            public void onStart(Object... params) {
                if (!updateUserHeader) {
                    showProgressDialog("正在提交数据中,请稍候...");
                }
            }

            @Override
            public void onComplete(BaseBean bean, Object... params) {
                dismissProgressDialog();

                updateUserHeader = false;
                MeFragment.is_refresh = true;
                ToastHelper.showToast(ModifyActivity.this, "修改成功");
                if (!TextUtils.isEmpty(mUserName)) {
                    tv_nick.setText(mUserName);
                }

                if (mSex == 1) {
                    tv_sex.setText("男");
                } else if (mSex == 2) {
                    tv_sex.setText("女");
                }

                if (!TextUtils.isEmpty(mBirthday)) {
                    tv_birthday.setText(mBirthday);
                }
                if (!TextUtils.isEmpty(mCityName)) {
                    tv_cityName.setText(mCityName);
                }

            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastHelper.showToast(ModifyActivity.this, msg);
            }
        });
    }

}
