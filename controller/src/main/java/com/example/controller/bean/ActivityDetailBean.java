package com.example.controller.bean;

import java.util.ArrayList;

public class ActivityDetailBean extends BaseBean {

	public Data data;

	public static class Data {

		public String status_name;
		public int remain;
		public long end_time;
		public String certificate;
		public String rule_url;
		public ArrayList<String> pic_urls;
		public long start_time;
		public int total;
		public ArrayList<JoinUserBean> join_users;
		public int integral;
		public String name;
		public MeBean me;
		public int join_users_number;
		public int id;
		public long time;
		public String detail;
		public int status;

		public static class PicUrlsBean {

			public String pic_url;
		}

		public static class JoinUserBean {
			public String user_id;
			public String user_name;
			public String head_pic;
			public String certificate;
			public boolean add = false;
			public int id;
			public long time;
			public int win;
		}

		public static class MeBean {

			public int integral;
			public int joined;
			public int joined_number;
			public int win;
		}

	}
}
