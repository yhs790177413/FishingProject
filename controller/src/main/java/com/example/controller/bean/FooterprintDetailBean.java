package com.example.controller.bean;

import com.example.controller.bean.CommentsBean.Data.Comment;
import com.example.controller.bean.FishingDetialBean.Data.FishTyes;

import java.io.Serializable;
import java.util.ArrayList;

public class FooterprintDetailBean extends BaseBean {

	public Data data;

	public static class Data {

		public int fishPoint_id;
		public String address;
		public int comment_number;
		public int is_favorite;
		public ArrayList<Comment> comments;
		public String distance;
		public int like_number;
		public String user_name;
		public double latitude;
		public String fishPoint_name;
		public String head_pic;
		public ArrayList<FishTyes> fish_types;
		public int recommend;
		public ArrayList<PicUrl> pic_urls;
		public int number;
		public String user_id;
		public int is_like;
		public int price;
		public String name;
		public int id;
		public long time;
		public String info;
		public double longitude;
		public ArrayList<Like> likes;
		public String city_name;
		public String fishInfo;
		public String tools;
		public String weather;
		public String address_info;
		public Tag tag;
		public int jing;

		public static class PicUrl {

			public String smallpic_url;
			public int id;
			public String pic_url;
		}

		public static class Like implements Serializable {

			public String user_id;
			public String user_name;
			public String head_pic;
			public int id;
		}

		public static class Tag {
			public int user_likes;
			public String tag_name;
			public int tag_id;
		}
	}
}
