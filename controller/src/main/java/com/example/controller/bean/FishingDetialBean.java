package com.example.controller.bean;

import com.example.controller.bean.CommentsBean.Data.Comment;

import java.io.Serializable;
import java.util.ArrayList;

public class FishingDetialBean extends BaseBean {

	public Data data;

	public static class Data {

		public String summary;
		public String address;
		public int comment_number;
		public ArrayList<Comment> comments;
		public int is_favorite;
		public String distance;
		public int like_number;
		public String user_name;
		public double latitude;
		public ArrayList<FishTyes> fish_types;
		public ArrayList<Like> likes;
		public int recommend;
		public ArrayList<PicUrls> pic_urls;
		public String user_id;
		public int is_like;
		public String name;
		public int id;
		public long time;
		public int location_number;
		public String info;
		public double longitude;
		public Weather weather;
		public FishFeed fish_feed;

		public static class Weather implements Serializable {

			public int temp;
			public int temp_rise;
			public float pressure;
			public String time;
			public String cond;
			public int pressure_rise;
		}

		public static class FishTyes implements Serializable {

			public String name;
			public int id;
			public String pic_url;
		}

		public static class Like implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public String user_id;
			public String user_name;
			public String head_pic;
			public int id;
		}

		public static class PicUrls {

			public String smallpic_url;
			public int id;
			public String pic_url;
		}

		public static class FishFeed {

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
			
			public static class Tag{
				public int user_likes;
				public String tag_name;
				public int tag_id;
			}
		}

	}
}
