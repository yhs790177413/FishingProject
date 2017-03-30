package com.example.controller.bean;

import java.util.ArrayList;

public class FooterprintListBean extends BaseBean {

	public Data data;

	public static class Data {

		public ArrayList<List> list;

		public static class List {

			public int id;
			public long time;
			public String user_id;
			public String user_name;
			public String head_pic;
			public ArrayList<String> pic_urls;
			public int like_number;
			public int is_like;
			public int comment_number;
			public int picture_number;
			public String info;
			public double latitude;
			public double longitude;
			public String address_info;
			public int recommend;
			public String tools;
			public String fish_info;
			public String weather;
			public String city_name;
			public Tag tag;
			public int jing;

			public static class Tag {
				public int user_likes;
				public String tag_name;
				public int tag_id;
			}
			// public String distance;
		}
	}
}
