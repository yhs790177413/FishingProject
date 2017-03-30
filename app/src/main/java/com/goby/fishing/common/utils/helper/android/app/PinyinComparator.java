package com.goby.fishing.common.utils.helper.android.app;

import java.util.Comparator;

import android.text.TextUtils;

import com.example.controller.bean.SortModel;

public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        if (!TextUtils.isEmpty(o1.sortLetters)
                && !TextUtils.isEmpty(o2.sortLetters)) {
            if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
                return -1;
            } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
                return 1;
            } else {
                return o1.sortLetters.compareTo(o2.sortLetters);
            }
        } else {
            return 1;
        }

    }

}
