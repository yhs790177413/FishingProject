package com.example.controller.bean;

import java.util.ArrayList;

public class IndexInfoBean extends BaseBean{

	public Data data;
	
	public static class Data{
		
		public ArrayList<Slider> slider;
		public ArrayList<Recommend> recommends;
		
		public static class Slider{
			
			public String title;
			public String type;
			public String object_id;
			public String web_url;
			public String pic_url;
		}
		
		public static class Recommend{
			
			public int id;
			public String title;
			public String content;
			public String time;
			public String user_id;
			public String user_name;
			public String pic_url;
			public int type;
		}
		
	}
}
