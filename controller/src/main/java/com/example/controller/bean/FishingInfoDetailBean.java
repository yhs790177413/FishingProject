package com.example.controller.bean;

import com.example.controller.bean.TagsListBean.Data.TagBean;

import java.io.Serializable;
import java.util.ArrayList;

public class FishingInfoDetailBean extends BaseBean implements Serializable {

	public Data data;

	public static class Data implements Serializable {

		public int info_type;
		public int is_favorite;
		public int like_number;
		public int is_like;
		public int comment_number;
		public String user_name;
		public ArrayList<String> pic_urls;
		public ArrayList<Comment> comments;
		public String title;
		public String content;
		public long time;
		public ArrayList<Like> likes;
		public ArrayList<TagBean> tags;
		public int external_comment_number;
		public ArrayList<Externalcomments> external_comments;

		public static class Like implements Serializable {

			public String user_id;
			public String user_name;
			public String head_pic;
			public int id;
		}

		public static class Comment {

			public String user_id;
			public String user_name;
			public String head_pic;
			public String comment;
			public int id;
			public long time;
		}
		
		public static class Externalcomments implements Serializable{
			
			public String user_name;
			public String comment;
		}
	}
}
