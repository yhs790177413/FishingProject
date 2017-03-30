package com.goby.fishing.common.photochoose;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.goby.fishing.R;
import com.example.controller.bean.ImageGroupBean;
import com.goby.fishing.common.photochoose.adapter.ImageListAdapter;
import com.goby.fishing.common.utils.helper.android.app.ActivitiesHelper;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * 某个文件夹下的所有图片列表
 *
 * @author likebamboo
 */
public class ImageListActivity extends AbsBaseActivity implements
        AdapterView.OnItemClickListener {
    public enum LaunchEnum {
        clickLaunch, // 普通点击
        loadLastLaunch, // 需要加载最近图片
    }

    // title
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_IMAGES_DATAS = "extra_images";
    public static final String EXTRA_FROM_CHAT = "extra_from_chat";
    public static final String EXTRA_LAUNCH_ENUM = "extra_launch_enum";

    // title
    // 图片列表GridView
    private GridView mImagesGv = null;
    // 图片地址数据源
    private ArrayList<String> mImages = new ArrayList<String>();
    // 适配器
    private ImageListAdapter mImageAdapter = null;

    // 是否从聊天页面的图片
    private boolean isFromChat;
    private String mTitle;
    private TextView mTitleTv;

    private LaunchEnum mLaunch = LaunchEnum.clickLaunch;

    public static void launch(Activity activity, String title,
                              boolean from_chat, ArrayList<String> imgs, LaunchEnum launchEnum) {
        Intent mIntent = new Intent(activity, ImageListActivity.class);
        mIntent.putExtra(ImageListActivity.EXTRA_TITLE, title);
        mIntent.putExtra(EXTRA_FROM_CHAT, from_chat);
        mIntent.putStringArrayListExtra(ImageListActivity.EXTRA_IMAGES_DATAS,
                imgs);
        mIntent.putExtra(EXTRA_LAUNCH_ENUM, launchEnum);
        activity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        initView();
        initData();
        initTitleBar(mTitle);
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mImagesGv = (GridView) findViewById(R.id.images_gv);
    }

    private void initData() {
        isFromChat = getIntent().getBooleanExtra(EXTRA_FROM_CHAT, false);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mLaunch = (LaunchEnum) getIntent().getSerializableExtra(
                EXTRA_LAUNCH_ENUM);
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
        if (mLaunch == LaunchEnum.clickLaunch) {
            ArrayList<String> imgsList = getIntent().getStringArrayListExtra(
                    EXTRA_IMAGES_DATAS);
            if (imgsList != null && imgsList.size() > 0) {
                mImages = getIntent().getStringArrayListExtra(
                        EXTRA_IMAGES_DATAS);
                setAdapter(mImages);
            }
        } else if (mLaunch == LaunchEnum.loadLastLaunch) {
            getLastImage();
        }

    }

    private void initTitleBar(String title) {
        mTitleTv = (TextView) findViewById(R.id.center_title);
        mTitleTv.setText(title);
        findViewById(R.id.left_back_image_botton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageLoaderWrapper.getDefault().stop();
                        ActivitiesHelper.getInstance().closeTarget(
                                ImageListActivity.class);
                        finish();
                    }
                });
        findViewById(R.id.right_text_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageLoaderWrapper.getDefault().stop();
                        ActivitiesHelper.getInstance().closeTarget(
                                ImageListActivity.class);
                        finish();
                    }
                });
    }

    /**
     * 获取最近图片的
     */
    private void getLastImage() {
        showProgressDialog("加载图片中,请稍候...");
        GetImageTask getImageTask = new GetImageTask();
        getImageTask.execute();
        // LocalController.getInstance().getLastLocalImage(
        // new Listener<ArrayList<ImageGroupBean>>() {
        // @Override
        // public void onStart(Object... params) {
        // showProgressDialog("请稍候...");
        // }
        //
        // @Override
        // public void onComplete(ArrayList<ImageGroupBean> result,
        // Object... params) {
        // dismissProgressDialog();
        // // 如果加载成功
        // if (result == null || result.size() <= 0
        // || result.get(0).getImages() == null
        // || result.get(0).getImages().size() <= 0) {
        // ToastUtils.show(ImageListActivity.this,
        // getString(R.string.no_images));
        // return;
        // }
        // ImageGroupBean imageGroup = result.get(0);
        // mImages = imageGroup.getImages();
        // setAdapter(mImages);
        // }
        //
        // @Override
        // public void onFail(String msg, Object... params) {
        // dismissProgressDialog();
        // ToastUtils.show(ImageListActivity.this,
        // getString(R.string.loaded_fail));
        // }
        // });
    }

    /**
     * 构建并初始化适配器
     *
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mImageAdapter = new ImageListAdapter(this, datas, mImagesGv);
        mImagesGv.setAdapter(mImageAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        if (Bimp.max == 1) {
            Bimp.clear();
            Bimp.mSelectedList.add(mImages.get(position));
            // EventBusUtil.getInstance().getCommonEventBus()
            // .post(new RefreshImageListEvent());
            ImageLoaderWrapper.getDefault().stop();
            ActivitiesHelper.getInstance().closeTarget(ImageListActivity.class);
            finish();
        } else {
            // // ImageBrowseActivity.launch(ImageListActivity.this, mImages,
            // // position, ImageBrowseActivity.LaunchEnum.selected);
            // if (position == 0) {
            // // EventBusUtil.getInstance().getCommonEventBus()
            // // .post(new TakePhotoEvent());
            // finish();
            // } else {
            ImageBrowseActivity.launch(ImageListActivity.this, mImages,
                    position, ImageBrowseActivity.LaunchEnum.selected);
            // }
        }
    }

    @Override
    public void onBackPressed() {
        if (mImageAdapter != null) {
            Util.saveSelectedImags(this, mImageAdapter.getSelectedImgs());
        }
        ImageLoaderWrapper.getDefault().stop();
        ActivitiesHelper.getInstance().closeTarget(ImageListActivity.class);
        super.onBackPressed();
    }

    // public void onEventMainThread(BimpSyncEvent e) {
    // if (mImageAdapter != null) {
    // mImageAdapter.notifyDataSetChanged();
    // }
    // }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EventBusUtnstance().getCommonEventBus().unregister(this);
    }

    private class GetImageTask extends
            AsyncTask<Integer, Integer, ArrayList<ImageGroupBean>> {

        // 该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
        // 主要用于进行异步操作
        @Override
        protected ArrayList<ImageGroupBean> doInBackground(Integer... params) {

            ArrayList<ImageGroupBean> mGruopList = new ArrayList<ImageGroupBean>();
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = ImageListActivity.this
                    .getContentResolver();
            // 构建查询条件，且只查询jpeg和png的图片
            StringBuilder selection = new StringBuilder();
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
            selection.append(" or ");
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
            selection.append(" or ");
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");

            Cursor mCursor = null;
            try {
                // 初始化游标
                mCursor = mContentResolver.query(mImageUri, null,
                        selection.toString(), new String[]{"image/jpeg",
                                "image/png", "image/jpg"},
                        MediaStore.Images.Media.DATE_TAKEN + " DESC");
                // 遍历结果
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    // 获取该图片的所在文件夹的路径
                    File file = new File(path);
                    String parentName = "";
                    if (file.getParentFile() != null) {
                        parentName = file.getParentFile().getName();
                    } else {
                        parentName = file.getName();
                    }
                    // 构建一个所有图片的文件夹
                    ImageGroupBean allItem = new ImageGroupBean();
                    allItem.setDirName("最近图片");
                    // 是否包含所有图片文件夹,包含侧返回下标
                    int hasAllItemDirectory = mGruopList.indexOf(allItem);
                    if (hasAllItemDirectory >= 0) {
                        // 如果是，该组的图片数量+1
                        ImageGroupBean imageGroup = mGruopList
                                .get(hasAllItemDirectory);
                        imageGroup.addImage(path);
                        // if (imageGroup.getImageCount() < 100) {
                        // imageGroup.addImage(path);
                        // }
                    } else {
                        // 否则，将该对象加入到groupList中
                        allItem.addImage(path);
                        mGruopList.add(allItem);
                    }
                    // 构建一个imageGroup对象
                    ImageGroupBean item = new ImageGroupBean();
                    // 设置imageGroup的文件夹名称
                    item.setDirName(parentName);
                    // 寻找该imageGroup是否是其所在的文件夹中的第一张图片
                    int searchIdx = mGruopList.indexOf(item);
                    if (searchIdx >= 0) {
                        // 如果是，该组的图片数量+1
                        ImageGroupBean imageGroup = mGruopList.get(searchIdx);
                        imageGroup.addImage(path);
                    } else {
                        // 否则，将该对象加入到groupList中
                        item.addImage(path);
                        mGruopList.add(item);
                    }
                }
                return mGruopList;
            } catch (Exception e) {
            } finally {
                // 关闭游标
                if (mCursor != null && !mCursor.isClosed()) {
                    mCursor.close();
                }
            }
            return null;
        }

        // 该方法运行在Ui线程内，可以对UI线程内的控件设置和修改其属性
        @Override
        protected void onPreExecute() {
        }

        // 在doInBackground方法当中，每次调用publishProgrogress()方法之后，都会触发该方法
        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        // 在doInBackground方法执行结束后再运行，并且运行在UI线程当中
        // 主要用于将异步操作任务执行的结果展示给用户
        @Override
        protected void onPostExecute(ArrayList<ImageGroupBean> result) {

            dismissProgressDialog();
            // 如果加载成功
            if (result == null || result.size() <= 0
                    || result.get(0).getImages() == null
                    || result.get(0).getImages().size() <= 0) {
                ToastHelper.showToast(ImageListActivity.this, "找不到系统图片资源");
                return;
            }
            ImageGroupBean imageGroup = result.get(0);
            mImages = imageGroup.getImages();
            setAdapter(mImages);
        }

    }
}
