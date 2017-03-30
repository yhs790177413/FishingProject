package com.example.controller.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentsBean extends BaseBean {

	public Data data;
	
	public static class Data{
		
		public ArrayList<Comment> list;
		
		public static class Comment implements Serializable{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public String user_id;
			public String user_name;
			public String head_pic;
			public String comment;
			public int id;
			public long time;
			public int jing;
		}
	}
}
