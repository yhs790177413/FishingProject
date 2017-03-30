package com.example.controller.bean;

import java.util.ArrayList;

public class MyInfoBean extends BaseBean {

	public Data data;

	public static class Data {

		public String birthday;
		public String city_name;
		public String user_id;
		public int city;
		public String user_name;
		public int sex;
		public String mobile;
		public String head_pic;
		public int age;
		public String user_sign;
		public String real_name;
		public int follower; // -- 我关注
		public int follow; // -- 被关注
		public int status;
		public ArrayList<Tags> tags;
		public int popular;
		public int integral;
		public int member;

		public static class Tags {
			public int user_likes;
			public String tag_name;
			public int tag_id;
		}
	}

}
