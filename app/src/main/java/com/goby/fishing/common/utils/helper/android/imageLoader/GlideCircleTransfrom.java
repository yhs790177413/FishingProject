/**
 * 
 */
package com.goby.fishing.common.utils.helper.android.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * @author shan
 * @Description: 圆形转换器
 * @date 2017年1月11日 下午12:00:30 
 */
public class GlideCircleTransfrom  extends BitmapTransformation{
	    public GlideCircleTransfrom(Context context) {
	        super(context);
	    }

	    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
	        if (source == null) return null;

	        int size = Math.min(source.getWidth(), source.getHeight());
	        int x = (source.getWidth() - size) / 2;
	        int y = (source.getHeight() - size) / 2; 
	        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

	        Bitmap result = pool.get(size, size, Bitmap.Config.RGB_565);
	        if (result == null) {
	            result = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
	        }

	        Canvas canvas = new Canvas(result);
	        Paint paint = new Paint();
	        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
	        paint.setAntiAlias(true);
	        float r = size / 2f;
	        canvas.drawCircle(r, r, r, paint);
	        return result;
	    }

	    @Override public String getId() {
	        return getClass().getName();
	    }

	
		@Override
		protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int arg2, int arg3) {
			  return circleCrop(pool, toTransform);
		}
	
}
