package com.goby.fishing.common.utils.helper.android.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

public class ActivitiesHelper {
	public static final int EXIT_APPLICATION = 0x0001;

	private LinkedList<Activity> mActs;
	private static ActivitiesHelper instance = null;

	private ActivitiesHelper() {
		mActs = new LinkedList<Activity>();
	};

	public synchronized static ActivitiesHelper getInstance() {
		if (instance == null) {
			instance = new ActivitiesHelper();
		}
		return instance;
	}

	public void addActivity(Activity act) {
		synchronized (ActivitiesHelper.this) {
			mActs.addFirst(act);
		}
	}

	public void removeActivity(Activity act) {
		synchronized (ActivitiesHelper.this) {
			if (mActs != null && mActs.indexOf(act) >= 0) {
				mActs.remove(act);
			}
		}
	}

	public Activity getTopActivity() {
		synchronized (ActivitiesHelper.this) {
			return (mActs == null || mActs.size() <= 0) ? null : mActs.get(0);
		}
	}

	public Activity getSecondActivity() {
		synchronized (ActivitiesHelper.this) {
			return (mActs == null || mActs.size() <= 1) ? null : mActs.get(1);
		}
	}

	public void close() {
		synchronized (ActivitiesHelper.this) {
			Activity act;
			while (mActs.size() != 0) {
				act = mActs.poll();
				act.finish();
			}
		}
	}

	/**
	 * 关闭其他activity，唯独排除activityClass指定的activity
	 * 
	 * @param activityClass
	 */
	public void closeExcept(Class<?> activityClass) {
		synchronized (ActivitiesHelper.this) {
			Activity act;
			Iterator<Activity> activityIterator = mActs.iterator();
			while (activityIterator.hasNext()) {
				act = activityIterator.next();
				if (!act.getClass().getName().equals(activityClass.getName())) {
					act.finish();
					activityIterator.remove();
				}
			}
		}
	}

	/**
	 * 关闭activityClass指定的activity
	 * 
	 * @param activityClass
	 */
	public void closeTarget(Class<?> activityClass) {
		synchronized (ActivitiesHelper.this) {
			Activity act;
			Iterator<Activity> activityIterator = mActs.iterator();
			while (activityIterator.hasNext()) {
				act = activityIterator.next();
				if (act.getClass().getName().equals(activityClass.getName())) {
					act.finish();
					activityIterator.remove();
				}
			}
		}
	}

	public ArrayList<Activity> getTargetActivity(Class<?> activityClass) {
		ArrayList<Activity> activities = new ArrayList<Activity>();
		synchronized (ActivitiesHelper.this) {
			Activity act;
			int size = mActs.size();
			for (int i = 0; i < size; i++) {
				act = mActs.get(i);
				if (act.getClass().getName().equals(activityClass.getName())) {
					activities.add(act);
				}
			}
		}

		return activities;
	}

	/**
	 * 关闭指定的activity集
	 */
	public void closeTarget(List<String> activityClassName) {
		synchronized (ActivitiesHelper.this) {
			Activity act;
			Iterator<Activity> activityIterator = mActs.iterator();
			while (activityIterator.hasNext()) {
				act = activityIterator.next();
				if (activityClassName.contains(act.getClass().getName())) {
					act.finish();
					activityIterator.remove();
				}

			}
		}
	}
}
