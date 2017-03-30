package com.goby.fishing.common.utils.helper.android.app;

import com.goby.fishing.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SideBar extends Button {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public static String[] b = { "", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "#" };
	private int choose = -1;// 选中
	private Paint paint = new Paint();
	private Context context;

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public SideBar(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int interval = height / b.length;

		int textSize = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 13, context
						.getApplicationContext().getResources()
						.getDisplayMetrics());
		paint.setAntiAlias(true);

		Bitmap bm_star = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_up);
		float xPo = width / 2 - bm_star.getWidth() / 2;
		float yPo = interval - bm_star.getHeight();

		canvas.drawBitmap(bm_star, xPo, yPo, paint);
		int size = b.length;

		for (int i = 0; i < size; i++) {
			// 抗锯齿
			paint.setAntiAlias(true);
			paint.setTypeface(Typeface.DEFAULT);
			paint.setTextSize(textSize);
			paint.setColor(Color.parseColor("#999999"));
			float xPos = width / 2 - (paint.measureText(b[i]) / 2);
			float yPos = interval * i + interval;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}

		// // 获取焦点改变背景颜色.
		// int height = getHeight();// 获取对应高度
		// int width = getWidth(); // 获取对应宽度
		// int singleHeight = height / b.length;// 获取每一个字母的高度
		//
		// for (int i = 0; i < b.length; i++) {
		// paint.setColor(Color.rgb(33, 65, 98));
		// // paint.setColor(Color.WHITE);
		// paint.setTypeface(Typeface.DEFAULT_BOLD);
		// paint.setAntiAlias(true);
		// paint.setTextSize(20);
		// // 选中的状态
		// if (i == choose) {
		// paint.setColor(Color.parseColor("#3399ff"));
		// paint.setFakeBoldText(true);
		// }
		// // x坐标等于中间-字符串宽度的一半.
		// float xPos = width / 2 - paint.measureText(b[i]) / 2;
		// float yPos = singleHeight * i + singleHeight;
		// canvas.drawText(b[i], xPos, yPos, paint);
		// paint.reset();// 重置画笔
		// }

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.shape_sidebar_bg);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}

					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}
