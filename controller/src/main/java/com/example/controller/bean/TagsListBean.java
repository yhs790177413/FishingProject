package com.example.controller.bean;

import java.util.ArrayList;

public class TagsListBean extends BaseBean{

	public Data data;
	
	public static class Data{
		
		public ArrayList<TagBean> list;
		
		public static class TagBean{
			
			public String tag_name;
			public int tag_id;
			public boolean isSelect = false;
		}
	}
}
