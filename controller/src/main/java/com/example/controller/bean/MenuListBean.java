package com.example.controller.bean;

import java.util.ArrayList;

public class MenuListBean extends BaseBean {

	public Data data;

	public static class Data {

		public ArrayList<MenuBean> list;

		public static class MenuBean {

			public int id;
			public String name;
			public String icon_url;
			public String content_url;
			public String tag_url;
			public int target;
		}
	}
}
