package com.example.imagelib.imageLoad;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

/**
 * @author shan
 * @Description: 图片加载框架
 * @date 2017年1月11日 下午2:07:10
 */
public class ImageUtils {

	private static ImageUtils imageUtils = new ImageUtils();

	private ImageUtils() {
	}

	public static ImageUtils getInstance() {
		return imageUtils;
	}

	/**
	 * @param acvitity
	 * @param url
	 * @param imageView
	 */
	public void loadImage(Object acvitity, String url, int defaultImageId,ImageView imageView) {
		getDrawableTypeRequest(acvitity, url).placeholder(defaultImageId)
        .crossFade().into(imageView) ;
	}
	
	/**
	 * 加载本地资源
	 * @param acvitity
	 * @param resouceId
	 * @param imageView
	 */
	public void loadResouceImage(Activity acvitity, int resouceId,ImageView imageView) {
		Glide.with(acvitity).load(resouceId).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);  

	}

	/**
	 * @param acvitity
	 * @param url
	 * @param imageView
	 *            占位图与错误显示图
	 */
	public void loadImage(Object acvitity, String url, ImageView imageView, int holderResId) {
		getDrawableTypeRequest(acvitity, url).error(holderResId).placeholder(holderResId).into(imageView);
	}

	/**
	 * @param acvitity
	 * @param url
	 * @param imageView
	 *            占位图与错误显示图
	 */
	public void loadImage(Object acvitity, String url, ImageView imageView, int holderResId, int errResId) {
		getDrawableTypeRequest(acvitity, url).error(errResId).placeholder(holderResId).into(imageView);
	}

	/**
	 * @param url
	 * @param imageView
	 */
	public void loadRoudeImage(Context context, String url, ImageView imageView) {
		loadRoudeImage(context, url, 10, imageView);
	}

	/**
	 * @param url
	 * @param imageView
	 */
	public void loadRoudeImage(Context context, String url, int dp, ImageView imageView) {
		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideRoundTransform(context, dp)).into(imageView);
	}

	/**
	 * @param context
	 * @param url
	 * @param imageView
	 * @param holderResId
	 *            占位图
	 */
	public void loadRoudeImage(Context context, String url, ImageView imageView, int holderResId) {
		loadRoudeImage(context, url, 10, imageView, holderResId);
	}

	/**
	 * @param context
	 * @param url
	 * @param imageView
	 * @param holderResId
	 *            占位图
	 */
	public void loadRoudeImage(Context context, String url, int dp, ImageView imageView, int holderResId) {
		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideRoundTransform(context, dp)).placeholder(holderResId)
				.into(imageView);
	}

	/**
	 * @param context
	 * @param url
	 * @param imageView
	 *            圆角
	 * @param holderResId
	 *            占位图
	 * @param errResId
	 *            错误显示图
	 */
	public void loadRoudeImage(Context context, String url, ImageView imageView, int holderResId, int errResId) {
		loadRoudeImage(context, url, imageView, 10, holderResId, errResId);
	}

	/**
	 * @param context
	 * @param url
	 * @param imageView
	 * @param dp
	 *            圆角
	 * @param holderResId
	 *            占位图
	 * @param errResId
	 *            错误显示图
	 */
	public void loadRoudeImage(Context context, String url, ImageView imageView, int dp, int holderResId,
			int errResId) {
		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideRoundTransform(context, dp)).error(errResId)
				.placeholder(holderResId).into(imageView);
	}

	/**
	 * @param url
	 * @param imageView
	 */
	public void loadCircleImage(Context context, String url, ImageView imageView) {
		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideCircleTransfrom(context)).into(imageView);
	}

	/**
	 * @param context
	 * @param url
	 * @param imageView
	 * @param holderResId
	 *            占位图
	 */
	public void loadCircleImage(Context context, String url, ImageView imageView, int holderResId) {
		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideCircleTransfrom(context)).placeholder(holderResId)
				.into(imageView);
	}

	/**
	 * @param url
	 * @param imageView
	 *            占位图 错误显示图
	 */
	public void loadCircleImage(Context context, String url, ImageView imageView, int holderResId, int errResId) {
		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideCircleTransfrom(context)).error(errResId)
				.placeholder(holderResId).into(imageView);
	}

	/**
	 * @param object
	 * @param url
	 * @return
	 */
	private DrawableRequestBuilder<String> getDrawableTypeRequest(Object object, String url) {
		DrawableTypeRequest<String> typeRequest;
		if (object instanceof Activity) {
			typeRequest = Glide.with((Activity) object).load(url);
		} else if (object instanceof Context) {
			typeRequest = Glide.with((Context) object).load(url);
		} else if (object instanceof android.support.v4.app.Fragment) {
			typeRequest = Glide.with((android.support.v4.app.Fragment) object).load(url);
		} else if (object instanceof Fragment) {
			typeRequest = Glide.with((Fragment) object).load(url);
		} else if (object instanceof FragmentActivity) {
			typeRequest = Glide.with((FragmentActivity) object).load(url);
		} else {
			throw new RuntimeException("object 必须是 Activity,Context,Fragment or FragmentActivity之一");
		}
		return typeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE);
	}

}
