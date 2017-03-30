package com.example.controller.bean;

import java.util.ArrayList;

public class MyMessageBean extends BaseBean {

	public Data data;

	public static class Data {

		public ArrayList<List> list;

		public static class List {

			public String user_id;
			public String user_name;
			public String head_pic;
			public int id;
			public long time;
			public String title;
			public String message;
			public int type;
			public String object_id;
			public int status;
		}
	}
}
