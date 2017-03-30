package com.goby.fishing.common.photochoose.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.goby.fishing.R;
import com.example.controller.bean.ImageGroupBean;
import com.goby.fishing.common.photochoose.ImageLoaderWrapper;
import com.goby.fishing.common.photochoose.ImageUrlUtils;
import com.goby.fishing.common.widget.imageview.MyImageView;

/**
 * 分组图片适配器
 *
 * @author likebamboo
 */
public class ImageGroupAdapter extends BaseAdapter {
    // 上下文
    private Context mContext = null;
    // 图片列表
    private List<ImageGroupBean> mDataList = null;
    // 容器
    private View mContainer = null;
    private ImageLoaderWrapper.DisplayConfig mConfig = new ImageLoaderWrapper.DisplayConfig.Builder()
            .build();

    public ImageGroupAdapter(Context context, List<ImageGroupBean> list,
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
    public ImageGroupBean getItem(int position) {
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
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.image_group_item, null);
            holder.mImageIv = (MyImageView) view
                    .findViewById(R.id.group_item_image_iv);
            holder.mTitleTv = (TextView) view
                    .findViewById(R.id.group_item_title_tv);
            holder.mCountTv = (TextView) view
                    .findViewById(R.id.group_item_count_tv);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ImageGroupBean item = getItem(position);
        if (item != null) {
            // 图片路径
            String path = item.getFirstImgPath();
            // 标题
            holder.mTitleTv.setText(item.getDirName());
            // 计数
            holder.mCountTv.setText(mContext.getString(R.string.image_count,
                    item.getImageCount()));
            holder.mImageIv.setTag(path);
            // 加载图片
            try {
                ImageLoaderWrapper.getDefault().displayImage(
                        ImageUrlUtils.getDisplayUrl(path), holder.mImageIv,
                        mConfig);
            } catch (Exception e) {
            }
        }
        return view;
    }

    static class ViewHolder {
        public MyImageView mImageIv;

        public TextView mTitleTv;

        public TextView mCountTv;
    }

}
