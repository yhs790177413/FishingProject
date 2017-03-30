package com.goby.fishing.common.utils.helper.android.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

public class BorderTextView extends TextView {

	private int sroke_width = 1;
	
	public int color;
	
	public BorderTextView(Context context) {
		super(context);
	}

	public BorderTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setColor(int mColor){
		color = mColor;
	}
	
	public int getColor(){
		return color;
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		// 将边框设为黑色
		
		paint.setColor(color);
		// 画TextView的4个边
		canvas.drawLine(0, 0, this.getWidth() - sroke_width, 0, paint);
		canvas.drawLine(0, 0, 0, this.getHeight() - sroke_width, paint);
		canvas.drawLine(this.getWidth() - sroke_width, 0, this.getWidth()
				- sroke_width, this.getHeight() - sroke_width, paint);
		canvas.drawLine(0, this.getHeight() - sroke_width, this.getWidth()
				- sroke_width, this.getHeight() - sroke_width, paint);
		super.onDraw(canvas);
	}
}
