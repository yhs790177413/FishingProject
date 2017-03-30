/**
 * 
 */
package com.example.imagelib.imageLoad;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

import android.content.Context;

/**
 * @author shan
 * @Description: Glide 配置文件
 * @date 2017年1月11日 下午2:04:09 
 */
public class MyGlideModule implements GlideModule {
	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		// InternalCacheDiskCacheFactory to place your cache in your
		// applications private internal cache directory
		// 磁盘缓存位置  packageName/cache/${Constance.PrivateCashName}
		 builder.setDiskCache(new InternalCacheDiskCacheFactory(context,Constance.PrivateCashName, 500 * 1024 * 1024));
		
		// ExternalCacheDiskCacheFactory to place your cache in your
		// applications public cache directory on the sd card
		// bulider.setDiskCache(new ExternalCacheDiskCacheFactory(context,Constance.PublicCashName, 500 * 1024 * 1024));
		 
		builder.setMemoryCache(new LruResourceCache(150 * 1024 * 1024));
		 //100M用于缓存图片，50M用于其他内存如小视频，文件缓存
        builder.setBitmapPool(new LruBitmapPool(100 * 1024 * 1024));
		builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
	
	}

	@Override
	public void registerComponents(Context context, Glide glide) {
		// TODO Auto-generated method stub

	}

}
