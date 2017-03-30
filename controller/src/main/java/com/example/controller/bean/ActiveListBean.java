package com.example.controller.bean;

import java.util.ArrayList;

public class ActiveListBean extends BaseBean{

	public Data data;
	
	public static class Data {
		
		public ArrayList<ActiveBean> list;
		
		public static class ActiveBean{
			
			public long start_time;
			public int total;
			public int status_name;
			public int remain;
			public String name;
			public long end_time;
			public int id;
			public int visit;
			public long time;
			public String pic_url;
			public int status;
		}
	}
}
