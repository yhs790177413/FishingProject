package com.example.controller.bean;

import java.util.ArrayList;

public class FishTypeBean extends BaseBean{

	public Data data;
	
	public static class Data{
		
		public ArrayList<List> list;
		
		public static class List{
			
			public String name;
			public int id;
			public String pic_url;
			public int category;
		}
	}
}
