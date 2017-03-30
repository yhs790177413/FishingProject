package com.goby.fishing.common.utils.helper.android.util;

import com.goby.fishing.R;

import android.app.Dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 用于创建对话框
 */
public class DialogBuilder {
	Dialog dialog;
	Context context;

	int dialogId;
	private String position = "";

	public DialogBuilder(Context context, int dialogId, String position) {

		Dialog dialog = new Dialog(context, R.style.Dialog);
		// 先设置要显示的内容
		dialog.setContentView(R.layout.dialog);
		// 调节对话框的宽度
		this.dialog = dialog;
		this.context = context;
		this.dialogId = dialogId;
		this.position = position;
	}

	public DialogBuilder(Context context, String position) {
		this(context, 0, position);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题内容(字符串 或者 资源id)
	 */
	public DialogBuilder setTitle(Object title) {

		TextView titleView = getView(R.id.title);
		titleView.setText(parseParam(title));
		titleView.setVisibility(View.VISIBLE);

		return this;
	}

	/**
	 * 设置中间的内容
	 */
	public DialogBuilder setMessage(Object message) {
		TextView messageView = getView(R.id.message);
		messageView.setText(parseParam(message));
		messageView.setTextSize(16);
		// 设置对话框中间的内容居中 --zhonghw
		if (position.equals("left")) {
			messageView.setGravity(Gravity.LEFT);
		} else if (position.equals("center")) {
			messageView.setGravity(Gravity.CENTER_HORIZONTAL);
		}
		return this;
	}

	public DialogBuilder setCheckBox(Object tips) {
		LinearLayout ll_extra = getView(R.id.ll_extra);
		CheckBox cb_extra = getView(R.id.cb_extra);
		ll_extra.setVisibility(View.VISIBLE);
		cb_extra.setText(parseParam(tips));
		cb_extra.setChecked(false);
		return this;
	}

	public boolean isCheck() {
		return ((CheckBox) getView(R.id.cb_extra)).isChecked();
	}

	Button left;

	public DialogBuilder setButtons(Object leftBtn, Object rightBtn,
			final OnDialogButtonClickListener listener) {
		// 设置左边按钮的文字
		Button left = getView(R.id.left);
		String textl = parseParam(leftBtn);
		if (null == textl) {
			left.setVisibility(View.GONE);
		} else {
			left.setText(textl);
		}

		// 给按钮绑定监听器
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

				if (listener != null) { // 有监听器
					listener.onDialogButtonClick(context, DialogBuilder.this,
							dialog, dialogId,
							OnDialogButtonClickListener.BUTTON_LEFT);
				}
			}
		});
		this.left = left;

		// 设置右边按钮的文字
		Button right = getView(R.id.right);
		String textr = parseParam(rightBtn);
		if (null == textr) {
			right.setVisibility(View.GONE);
		} else {
			right.setText(textr);
		}
		// 给按钮绑定监听器
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

				if (listener != null) { // 有监听器
					listener.onDialogButtonClick(context, DialogBuilder.this,
							dialog, dialogId,
							OnDialogButtonClickListener.BUTTON_RIGHT);
				}
			}
		});
		return this;
	}

	/**
	 * 通过id找到对话框中对应的子控件
	 * 
	 * @param id
	 *            子控件的id
	 * @return 子控件
	 */
	public <T extends View> T getView(int id) {
		return (T) dialog.findViewById(id);
	}

	/**
	 * 解析参数
	 * 
	 * @param param
	 *            字符串 或者 资源id
	 * @return 统一返回字符串
	 */
	private String parseParam(Object param) {
		if (param instanceof Integer) {
			return context.getString((Integer) param);
		} else if (param instanceof String) {
			return param.toString();
		}
		return null;
	}

	/**
	 * 说明已经确定对话框的界面参数
	 * 
	 * @return
	 */
	public Dialog create() {
		if (left == null) { // 说明不需要按钮
			// 得到按钮所在的布局
			ViewGroup btnsLayout = getView(R.id.btns_layout);
			// 得到根节点
			ViewGroup root = getView(R.id.root);
			// 移除按钮所在的布局
			root.removeView(btnsLayout);
		}

		return dialog;
	}

	public interface OnDialogButtonClickListener {
		public static final int BUTTON_LEFT = 0;
		public static final int BUTTON_RIGHT = 1;

		public void onDialogButtonClick(Context context, DialogBuilder builder,
										Dialog dialog, int dialogId, int which);
	}

	/**
	 * 将view放进对话框中
	 */
	public DialogBuilder setView(View view) {
		// 1.获得message所在的布局
		LinearLayout messageLayout = getView(R.id.message_layout);
		// 2.移除TextView
		messageLayout.removeAllViews();
		// 3.添加新的View
		LayoutParams lp = new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		messageLayout.addView(view);
		return this;
	}

	/**
	 * 将布局文件对应的View放进对话框中
	 * 
	 * @param layout
	 * @return
	 */
	public DialogBuilder setView(int layout) {
		ViewGroup parent = getView(R.id.message_layout);
		parent.removeAllViews();
		View view = LayoutInflater.from(context).inflate(layout, parent, false);
		return setView(view);
	}

	public DialogBuilder setItems(String[] items,
			final OnDialogItemClickListener lister) {
		// 把对话框放进对话框
		setView(R.layout.dialog_list_view);

		// 设置ListView数据
		ListView listview = getView(android.R.id.list);
		listview.setSelector(R.drawable.selector_list_bg);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.dialog_item_text_listview, items);

		listview.setAdapter(adapter);

		if (items.length > 4) {
			try {
				View listItem = adapter.getView(0, null, listview);
				listItem.measure(0, 0);
				int totalHeight = (listItem.getMeasuredHeight() + listview
						.getDividerHeight()) * 8;
				listview.getLayoutParams().height = totalHeight;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 绑定监听器
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				dialog.dismiss();

				if (lister != null) {
					lister.OnDialogItemClick(context, DialogBuilder.this,
							dialog, arg2);
				}

			}

		});

		return this;
	}

	public DialogBuilder setItems(int arrayId,
			final OnDialogItemClickListener lister) {
		String[] items = context.getResources().getStringArray(arrayId);

		return setItems(items, lister);
	}

	public interface OnDialogItemClickListener {
		public void OnDialogItemClick(Context context, DialogBuilder builder,
									  Dialog dialog, int position);
	}

	public void dismiss() {
		dialog.dismiss();
	}

}
