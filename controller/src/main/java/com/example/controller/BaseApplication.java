package com.example.controller;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import java.io.File;



public abstract class BaseApplication extends Application {

    public static BaseApplication app;
    public static Handler mainHandler;
    //上线前要关的
    public static final boolean DEBUG = true;



    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mainHandler = new Handler(Looper.getMainLooper());
    }




    /**
     * 获取日志文件临时目录
     *
     * @return
     */
    public File getLogTmpDir() {
        File dir = new File(getTmpDir(), "log_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    public File getTmpDir() {
        return getTmpDir(false);
    }

    /**
     * 获取临时目录
     *
     * @param isSdcard
     *            是否只取sd卡上的目录
     * @return
     */
    public File getTmpDir(boolean isSdcard) {
        File tmpDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdcard && !sdCardExist) {
            if (!sdCardExist) {
                return null;
            }
        }

        if (sdCardExist || isSdcard) {
            tmpDir = new File(Environment.getExternalStorageDirectory(),
                    getTmpDirName());
        } else {
            tmpDir = new File(getCacheDir(), getTmpDirName());
        }

        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        return tmpDir;
    }

    public abstract String getTmpDirName();
}
