package com.goby.fishing.common.photochoose;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import com.goby.fishing.R;
import com.example.controller.bean.ImageGroupBean;
import com.goby.fishing.common.photochoose.adapter.ImageGroupAdapter;
import com.goby.fishing.common.utils.helper.android.widget.ToastHelper;
import com.goby.fishing.framework.AbsBaseActivity;

public class PhotoAllActivity extends AbsBaseActivity implements
		AdapterView.OnItemClickListener {
	public static void launch(Activity activity) {
		Intent intent = new Intent(activity, PhotoAllActivity.class);
		activity.startActivity(intent);
	}

	public final static String EXTRA_FROM_CHAT = "extra_from_chat";

	public static void launchFromChat(Activity activity) {
		Bimp.clear();
		Intent intent = new Intent(activity, PhotoAllActivity.class);
		intent.putExtra(EXTRA_FROM_CHAT, true);
		activity.startActivity(intent);
	}

	// loading布局
	// private LoadingLayout mLoadingLayout = null;
	// 图片组GridView
	private GridView mGroupImagesGv = null;
	// 适配器
	private ImageGroupAdapter mGroupAdapter = null;
	// 存放图片<文件夹,该文件夹下的图片列表>键值对
	private ArrayList<ImageGroupBean> mGruopList = new ArrayList<ImageGroupBean>();
	private TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_photo);
		ImageListActivity.launch(PhotoAllActivity.this, "最近图片", getIntent()
				.getBooleanExtra(EXTRA_FROM_CHAT, false), null,
				ImageListActivity.LaunchEnum.loadLastLaunch);
		initView();
		initListener();
		if (!SDcardUtil.hasExternalStorage()) {
			// mLoadingLayout.showEmpty(getString(R.string.donot_has_sdcard));
			ToastHelper.showToast(this, "检查到该设备没有内存卡");
			return;
		}
		// 获取所有图片
		getAllImage();
	}

	/**
	 * 初始化界面元素
	 */
	private void initView() {
		// mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
		mGroupImagesGv = (GridView) findViewById(R.id.images_gv);
	}

	/**
	 * 获取最近图片的
	 */
	private void getAllImage() {
		// LocalController.getInstance().getLocalImage(
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
		// setImageAdapter(result);
		// }
		//
		// @Override
		// public void onFail(String msg, Object... params) {
		// dismissProgressDialog();
		// ToastUtils.show(PhotoAllActivity.this,
		// getString(R.string.loaded_fail));
		// }
		// });
	}

	private void initListener() {
		mTitle = (TextView) findViewById(R.id.center_title);
		mTitle.setText("相册");
		findViewById(R.id.left_back_image_botton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	/**
	 * 构建GridView的适配器
	 * 
	 * @param data
	 */
	private void setImageAdapter(ArrayList<ImageGroupBean> data) {
		if (data == null || data.size() == 0) {
			// mLoadingLayout.showEmpty(getString(R.string.no_images));
		}
		mGroupAdapter = new ImageGroupAdapter(this, data, mGroupImagesGv);
		mGroupImagesGv.setAdapter(mGroupAdapter);
		mGroupImagesGv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		ImageGroupBean imageGroup = mGroupAdapter.getItem(position);
		if (imageGroup == null) {
			return;
		}
		ArrayList<String> childList = imageGroup.getImages();
		boolean isFromChat = getIntent()
				.getBooleanExtra(EXTRA_FROM_CHAT, false);
		ImageListActivity.launch(PhotoAllActivity.this,
				imageGroup.getDirName(), isFromChat, childList,
				ImageListActivity.LaunchEnum.clickLaunch);
	}

	//
	// public void onEventMainThread(final ChatGetSendPhotoSuccess event) {
	// finish();
	// }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private class GetImageTask extends
			AsyncTask<Integer, Integer, ArrayList<ImageGroupBean>> {

		// 该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
		// 主要用于进行异步操作
		@Override
		protected ArrayList<ImageGroupBean> doInBackground(Integer... params) {

			ArrayList<ImageGroupBean> mGruopList = new ArrayList<ImageGroupBean>();
			Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			ContentResolver mContentResolver = PhotoAllActivity.this
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
						selection.toString(), new String[] { "image/jpeg",
								"image/png", "image/jpg" },
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
						if (imageGroup.getImageCount() < 100) {
							imageGroup.addImage(path);
						}
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
			setImageAdapter(result);
		}

	}
}