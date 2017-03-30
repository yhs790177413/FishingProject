package com.goby.fishing.common.photochoose;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goby.fishing.R;
import com.goby.fishing.application.Constant;
import com.goby.fishing.application.FishingApplication;
import com.goby.fishing.common.photochoose.crop.CropWrapper;
import com.goby.fishing.common.utils.helper.android.util.DialogBuilder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureBar extends LinearLayout {
	// gridview
	private NoScrollGridView gv_images;

	private List<String> imgList;// 图片按钮
	private AddGridAdapter imgAdapter;
	private Context mContext;
	public List<String> items;

	public PictureBar(Context context) {
		this(context, null);
		mContext = context;
	}

	public PictureBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public PictureBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.picturebar_view,
				this);
		Bimp.clear();
		if (!isInEditMode()) {
			gv_images = (NoScrollGridView) findViewById(R.id.gv_images);
		}
	}

	public void initData(Activity activity) {
		mContext = activity;
		setAdapter(mContext);
	}

	private void setAdapter(final Context mContext) {
		gv_images = (NoScrollGridView) findViewById(R.id.gv_images);
		imgList = new ArrayList<String>();
		imgAdapter = new AddGridAdapter(mContext, imgList);
		gv_images.setAdapter(imgAdapter);

	}

	private void showAddPictureDialog(final Activity activity) {
		// new
		// DialogUtils(activity,DialogUtils.LAYOUT_CENTER,true).setItems(R.array.sign,
		// new DialogUtils.OnDialogItemClickListener() {
		// @Override
		// public void OnDialogItemClick(Context context, DialogUtils builder,
		// Dialog dialog, int position) {
		// if (position == 0) {//拍照
		// CropWrapper.camera(BaseApplication.app.getCropTmpDir(), activity);
		// } else if (position == 1) {//从相册选择
		// PhotoAllActivity.launch(activity);
		// }
		// }
		// }).create().show();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
		File file = new File(Environment.getExternalStorageDirectory()
				+ Constant.PATH_ICON);
		ImageLoaderWrapper.initDefault(mContext, file, false);
		ImageListActivity.launch(activity, "最近图片", true, null,
				ImageListActivity.LaunchEnum.loadLastLaunch);
	}

	public void setListener(final Activity activity) {
		gv_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ---点击事件
				if(items.get(position).equals("add")){
					showPhotoDialog(position,activity);
				}else{
					if(Bimp.mSelectedList!=null&&Bimp.mSelectedList.size()>0){
						ImageBrowseActivity.launch(activity,
								Bimp.mSelectedList, position,"","","","","","","",false);
					}
				}
			}
		});
	}

	private void showPhotoDialog(final int mPosition, final Activity activity) {
		DialogBuilder dialog = new DialogBuilder(mContext, "center");
		dialog.setItems(R.array.sign,
				new DialogBuilder.OnDialogItemClickListener() {

					@Override
					public void OnDialogItemClick(Context context,
							DialogBuilder builder, Dialog dialog, int position) {
						if (position == 0) {
							CropWrapper.camera(
									new FishingApplication().getCropTmpDir(),
									activity);
						} else {
							if (imgAdapter.getItem(mPosition).equals("add")) {
								showAddPictureDialog(activity);

							} else {

								ImageBrowseActivity.launch(activity,
										Bimp.mSelectedList, position,"","","","","","","",false);
							}
						}

					}

				});
		dialog.create().show();
	}
	
	

	public void onCameraResultHandle(int requestCode, int resultCode,
			Intent data) {
		Uri uri = CropWrapper.onActivityResult(getContext(),
				new FishingApplication().getCropTmpDir(), requestCode,
				resultCode, data);
		if (uri != null) {
			Bimp.mSelectedList.add(uri + "");
		}
		imgAdapter.refreshList();
	}

	public void refreshList() {
		imgAdapter.refreshList();
	}

	/**
	 * 图片的适配器 Created by yhs on 2015/3/24.
	 */

	public class AddGridAdapter extends BaseAdapter {
		// 新建一个类继承BaseAdapter，实现视图与数据的绑定

		private Context context;
		private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
		

		public boolean isRemoving;
		private List<String> imgList;// 图片按钮
		private List<String> btn_list;// 加减按钮
		private List<String> allList;// 图片+按钮

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		public List<String> getItems() {
			return items;
		}

		public void setItems(ArrayList<String> mItems) {
			items = mItems;
		}

		public AddGridAdapter(Context context) {
			super();
			this.mInflater = LayoutInflater.from(context);
			this.setContext(context);
		}

		public AddGridAdapter(Context context, List<String> mItems) {
			super();
			this.mInflater = LayoutInflater.from(context);
			this.setContext(context);
			items = mItems;
			imgList = new ArrayList<String>();
			btn_list = new ArrayList<String>();
			allList = new ArrayList<String>();
			initList();
		}

		ViewHolder viewHolder = null;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.item_add_grid_photo, null);
			viewHolder = new ViewHolder();
			/** 得到各个控件的对象 */
			viewHolder.mIcon = (ImageView) convertView
					.findViewById(R.id.item_photo);
			viewHolder.tv_remove = (TextView) convertView
					.findViewById(R.id.tv_remove);

			this.setItem(viewHolder, position);

			return convertView;
		}

		private void setItem(ViewHolder viewHolder, final int position) {
			if (items.get(position) != null) {
				// if (position >= Bimp.max) {
				// viewHolder.mIcon.setVisibility(View.GONE);
				// } else {
				// if (!items.get(position).equals("add")) {
				// ImageLoaderWrapper.getDefault().displayImage(ImageDownloader.Scheme.FILE.wrap(items.get(position)),
				// viewHolder.mIcon);
				// }
				// }
				if (items.get(position).equals("add")) {
					ImageLoaderWrapper.getDefault().displayImage(
							ImageDownloader.Scheme.DRAWABLE.wrap(""
									+ R.drawable.add_photo), viewHolder.mIcon);
					// } else if (items.get(position).equals("remove")) {
					// ImageLoaderWrapper.getDefault().displayImage(ImageDownloader.Scheme.DRAWABLE.wrap(""
					// + R.drawable.del_photo_default), viewHolder.mIcon);
				} else {
					if (isRemoving) {
						viewHolder.tv_remove.setVisibility(View.VISIBLE);
					} else {
						viewHolder.tv_remove.setVisibility(View.GONE);
					}
					String imgUrl = ImageUrlUtils.getDisplayUrl(items
							.get(position));
					ImageLoaderWrapper.getDefault().displayImage(
							ImageUrlUtils.getSmallImageUrl(imgUrl),
							viewHolder.mIcon);
				}

				// 设置监听
				viewHolder.tv_remove
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								removeItem(position);
							}
						});

			}

		}

		@Override
		public int getCount() {
			if (items == null) {
				return 0;
			}
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		public void removeItem(int position) {
			Bimp.mSelectedList.remove(position);
			if (Bimp.mSelectedList.size() == 0) {
				isRemoving = false;
			}
			refreshList();
		}

		@Override
		public long getItemId(int id) {
			return 0;
		}

		private void initList() {
			imgList.clear();
			for (int i = 0; i < Bimp.mSelectedList.size(); i++) {
				if (Bimp.mSelectedList.get(i).startsWith("file://")) {
					String temp = Bimp.mSelectedList.get(i).replace("file://",
							"");
					imgList.add(temp);
				} else {
					imgList.add(Bimp.mSelectedList.get(i));
				}
			}
			btn_list.clear();
			if (imgList.size() < Bimp.max) {
				btn_list.add("add");
			}
			if (imgList.size() > 0) {
				isRemoving = true;
			}
			// if (imgList.size() > 0) {
			// btn_list.add("remove");
			// }
			allList.clear();
			allList.addAll(imgList);
			allList.addAll(btn_list);
			items.clear();
			items.addAll(allList);
		}

		public void refreshList() {
			initList();
			notifyDataSetChanged();
		}

		/**
		 * 存放控件
		 */
		class ViewHolder {

			public ImageView mIcon;
			public TextView tv_remove;

			public ViewHolder() {
				super();
			}
		}
	}
}
