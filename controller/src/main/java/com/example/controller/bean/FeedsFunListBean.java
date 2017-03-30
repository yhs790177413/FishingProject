package com.example.controller.bean;

import java.util.ArrayList;

public class FeedsFunListBean extends BaseBean {

	public Data data;

	public static class Data {

		public ArrayList<FeedsFunBean> list;
		public int join_integral;
		public int integral;

		public static class FeedsFunBean {

			public String user_id;
			public String user_name;
			public String head_pic;
			public int integral;
			public int id;
			public long time;
			public String pic_url;
			public String message;
			public String content;
		}
	}
}
