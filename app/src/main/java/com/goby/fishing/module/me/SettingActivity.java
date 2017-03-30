package com.goby.fishing.module.me;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.example.controller.controller.UserBusinessController;
import com.goby.fishing.R;
import com.example.controller.bean.BaseBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.utils.helper.android.util.SharedPreferenceUtil;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;
import com.goby.fishing.module.login.LoginActivity;

public class SettingActivity extends AbsBaseActivity implements OnClickListener {

    private SharedPreferenceUtil sharedPreferenceUtil;

    private Button btn_logout;

    private ImageView iv_back;

    private LinearLayout ll_leftBack;

    private RelativeLayout rl_version, rl_clean;

    private TextView tv_versionCode;

    private String path = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "Android/data/com.goby.fishing";

    public static void launch(Activity activity) {

        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initView();
    }

    public void initView() {

        btn_logout = (Button) findViewById(R.id.logout_btn);
        btn_logout.setOnClickListener(this);

        iv_back = (ImageView) findViewById(R.id.left_back);
        iv_back.setOnClickListener(this);

        ll_leftBack = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_leftBack.setOnClickListener(this);

        rl_version = (RelativeLayout) findViewById(R.id.version_layout);
        rl_version.setOnClickListener(this);

        rl_clean = (RelativeLayout) findViewById(R.id.clean_layout);
        rl_clean.setOnClickListener(this);

        tv_versionCode = (TextView) findViewById(R.id.version_code);
        tv_versionCode.setText(getVersion() + "版本");
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
            case R.id.logout_btn:
                if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
                    // 跳转到登录界面
                    LoginActivity.launch(this, "setting");
                } else {
                    // 注销登录
                    UserBusinessController.getInstance().logout(sharedPreferenceUtil.getUserToken(), getVersionCode(), "2", sharedPreferenceUtil.getDeviceToken(), new com.example.controller.controller.Listener<BaseBean>() {
                        @Override
                        public void onStart(Object... params) {
                            showProgressDialog("正在退出登录中,请稍候...");
                        }

                        @Override
                        public void onComplete(BaseBean bean, Object... params) {
                            dismissProgressDialog();

                            btn_logout.setText("登录");
                            sharedPreferenceUtil.setUserToken("");
                            sharedPreferenceUtil.setMember(0);
                            sharedPreferenceUtil.setRedPointIsVisible(false);
                            MeFragment.is_refresh = true;
                            ToastHelper.showToast(SettingActivity.this, "退出登录成功");

                        }

                        @Override
                        public void onFail(String msg, Object... params) {
                            dismissProgressDialog();
                            ToastHelper.showToast(SettingActivity.this, msg);
                        }
                    });
                }
                break;
            case R.id.version_layout:
                BDAutoUpdateSDK.cpUpdateCheck(getBaseContext(),
                        new MyCPCheckUpdateCallback());
                break;
            case R.id.clean_layout:
                GetCleanTask getCleanTask = new GetCleanTask();
                getCleanTask.execute();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, data);
        if (data != null) {
            btn_logout.setText("退出登录");
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (TextUtils.isEmpty(sharedPreferenceUtil.getUserToken())) {
            btn_logout.setText("登录");
        } else {
            btn_logout.setText("退出登录");
        }
    }

    /**
     * 检查版本更新操作
     */
    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

        @Override
        public void onCheckUpdateCallback(AppUpdateInfo info,
                                          AppUpdateInfoForInstall infoForInstall) {
            // TODO Auto-generated method stub
            if (infoForInstall != null
                    && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {
                BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(),
                        infoForInstall.getInstallPath());
            } else if (info != null) {
                BDAutoUpdateSDK.uiUpdateAction(SettingActivity.this, new MyUICheckUpdateCallback());
            } else {
                ToastHelper.showToast(SettingActivity.this, "当前已是最新版!");
            }
        }
    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {

        @Override
        public void onCheckComplete() {

        }

    }


    public void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() &&
                directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    class GetCleanTask extends AsyncTask<String, Integer, String> {// 继承AsyncTask

        @Override
        protected String doInBackground(String... params) {
            /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * */
            ImageLoaderWrapper.getDefault().clearDefaultLoaderDiscCache();
            ImageLoaderWrapper.getDefault().clearDefaultLoaderMemoryCache();
            deleteFilesByDirectory(getCacheDir());
            delFolder(path);
            return null;// 处理后台执行的任务，在后台线程执行
        }

        protected void onProgressUpdate(Integer... progress) {// 在调用publishProgress之后被调用，在ui线程执行
        }

        protected void onPostExecute(String result) {// 后台任务执行完之后被调用，在ui线程执行
        }

        protected void onPreExecute() {// 在doInBackground(Params...)之前被调用，在ui线程执行
            ToastHelper.showToast(SettingActivity.this, "清除缓存完毕");
        }

        protected void onCancelled() {// 在ui线程执行
        }

    }

    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String 文件夹路径 如 c:/fqf
     */
    public void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }
}
