package com.example.controller.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FishingInfoBean extends BaseBean {

	public Data data;

	public static class Data {

		public ArrayList<List> list;

		public static class List implements Serializable{

			public String title;
			public int id;
			public long time;
			public String user_id;
			public String user_name;
			public int visit_number;
			public int like_number; // 0 -- 不喜欢 1 -- 喜欢
			public int comment_number;
			public ArrayList<String> pic_urls;
			public String type_name; // 类型
			public TagBean tag;
			public int preview_style;
			public String content_url;
			public int recommend = 0;
			
			public static class TagBean{
				public String tag_name;
				public int tag_id;
			}
		}
	}
}
