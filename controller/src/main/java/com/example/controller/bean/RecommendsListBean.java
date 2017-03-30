package com.example.controller.bean;

import java.util.ArrayList;

public class RecommendsListBean extends BaseBean {

	public Data data;

	public static class Data {

		public ArrayList<RecommendsBean> list;

		public static class RecommendsBean {

			public int id;
			public String title;
			public String content_url;
			public String pic_url;
		}
	}
}
