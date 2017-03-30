package com.goby.fishing.common.photochoose.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import com.goby.fishing.common.utils.helper.android.imageLoader.ImageUtils;
import com.goby.fishing.R;
import com.goby.fishing.common.photochoose.Bimp;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.photochoose.ImageUrlUtils;

/**
 * 某个图片组中图片列表适配器
 * 
 * @author likebamboo
 */
public class ImageListAdapter extends BaseAdapter {
	// 上下文对象
	private Activity mContext = null;
	// 图片列表
	private ArrayList<String> mDataList = new ArrayList<String>();
	// 容器
	private View mContainer = null;
	private ImageLoaderWrapper.DisplayConfig mConfig = new ImageLoaderWrapper.DisplayConfig.Builder()
			.build();

	public ImageListAdapter(Activity context, ArrayList<String> list,
			View container) {
		mDataList = list;
		mContext = context;
		mContainer = container;
		mConfig.stubImageRes = R.drawable.pic_thumb;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public String getItem(int position) {
		if (position < 0 || position > mDataList.size()) {
			return null;
		}
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.image_list_item, null);
			holder.mImageIv = (ImageView) view
					.findViewById(R.id.list_item_iv);
			holder.mClickArea = view.findViewById(R.id.list_item_cb_click_area);
			holder.mSelectedCb = (CheckBox) view
					.findViewById(R.id.list_item_cb);
			view.setTag(holder);
			// }
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final String path = getItem(position);
		
		//holder.mImageIv.setImageBitmap(ImageUtil.getimage(path));
		// 加载图片
		try {
			Uri uri = Uri.parse(path); 
            // 给imageview设置图片 
//			holder.mImageIv.setImageResource(R.drawable.loadding_icon);
//			ImageLoader.getInstance(3,Type.LIFO).loadImage(path, holder.mImageIv);
			//holder.mImageIv.setImageResource(R.drawable.loadding_icon);
			ImageUtils.getInstance().loadImage(mContext, ImageUrlUtils.getDisplayUrl(path), R.drawable.loadding_icon,holder.mImageIv);

//			ImageLoaderWrapper.getDefault()
//					.displayImage(ImageUrlUtils.getDisplayUrl(path),
//							holder.mImageIv);
		} catch (Exception e) {
		}
		holder.mSelectedCb.setChecked(false);
		// 该图片是否选中
		for (String selected : Bimp.mSelectedList) {
			if (selected.equals(path)) {
				holder.mSelectedCb.setChecked(true);
			}
		}
		// 如果是单选的话就不显示选择框
		if (Bimp.max == 1) {
			holder.mSelectedCb.setVisibility(View.GONE);
		} else {
			holder.mSelectedCb.setVisibility(View.VISIBLE);
		}

		// 可点区域单击事件
		holder.mClickArea.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = holder.mSelectedCb.isChecked();
				if (!checked) {
					if (Bimp.mSelectedList.size() >= Bimp.max) {
						Toast.makeText(mContext,
								" 您最多可以选择" + Bimp.max + "张图片 ",
								Toast.LENGTH_SHORT).show();
						return;
					}
					addImage(path);
				} else {
					deleteImage(path);
				}
				holder.mSelectedCb.setChecked(!checked);
				System.out.println("现在图片： " + Bimp.max + "/"
						+ Bimp.mSelectedList.size());
			}
		});

		return view;
	}

	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	//
	// if (resultCode == mContext.RESULT_OK) {
	// switch (requestCode) {
	// case Crop.REQUEST_CAMERA: //拍照
	// Uri uri = CropWrapper.onActivityResult(mContext,
	// BaseApplication.app.getCropTmpDir(), requestCode, resultCode, data);
	// if(uri!=null){
	// Bimp.mSelectedList.add(uri + "");
	// ToastUtils.show(mContext,"图片地址："+uri);
	// }
	//
	// break;
	// }
	// }
	//
	// }

	/**
	 * 将图片地址添加到已选择列表中
	 * 
	 * @param path
	 */
	private void addImage(String path) {
		if (Bimp.mSelectedList.contains(path)) {
			return;
		}
		Bimp.mSelectedList.add(path);
	}

	/**
	 * 将图片地址从已选择列表中删除
	 * 
	 * @param path
	 */
	private void deleteImage(String path) {
		Bimp.mSelectedList.remove(path);
	}

	/**
	 * 获取已选中的图片列表
	 * 
	 * @return
	 */
	public ArrayList<String> getSelectedImgs() {
		return Bimp.mSelectedList;
	}

	static class ViewHolder {
		public ImageView mImageIv;

		public View mClickArea;

		public CheckBox mSelectedCb;

	}

}
