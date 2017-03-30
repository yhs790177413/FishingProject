package com.goby.fishing.common.utils.helper.android.util;

import java.util.ArrayList;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class RadioButtonGroup implements OnCheckedChangeListener {

	private ArrayList<RadioButton> rdoBtns = new ArrayList<RadioButton>();

	private OnSelectedChangeListener lst = null;

	private int currentIndex = -1;

	public void addRadioButton(RadioButton rdoBtn) {
		rdoBtns.add(rdoBtn);
		rdoBtn.setOnCheckedChangeListener(this);
	}

	public void setOnSelectedChangeListener(OnSelectedChangeListener lst) {
		this.lst = lst;
	}

	public void setCurrentChecked(int index) {
		int size = rdoBtns.size();
		for (int i = 0; i < size; i++) {
			if (i == index) {
				rdoBtns.get(i).setChecked(true);
			} else {
				rdoBtns.get(i).setChecked(false);
			}
		}
		currentIndex = index;
	}

	@Override
	public void onCheckedChanged(CompoundButton rdoBtn, boolean checked) {
		if (!checked) {
			return;
		}
		CompoundButton rb = null;
		int size = rdoBtns.size();
		for (int i = 0; i < size; i++) {
			if ((rb = rdoBtns.get(i)) != null) {
				if (rb != rdoBtn) {
					rb.setChecked(false);
				} else {
					currentIndex = i;
				}
			}
		}
		if (lst != null) {
			lst.onSelectedChanged(currentIndex);
		}
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public interface OnSelectedChangeListener {
		void onSelectedChanged(int index);
	}

}
