package com.example.controller.bean;

import java.util.ArrayList;

public class AttentionFriendsBean extends BaseBean {

	public Data data;
	
	public static class Data{
		
		public ArrayList<List> list;
		
		public static class List{
			
			public String user_id;
			public String user_name;
			public String head_pic;
		}
	}
}
