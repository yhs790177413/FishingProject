package com.example.controller.bean;

import java.util.ArrayList;

public class FishRankBean extends BaseBean {

	public Data data;

	public static class Data {

		public MeBean me;

		public ArrayList<List> list;

		public static class MeBean {
			public String user_like;
			public String user_id;
			public String user_name;
			public String head_pic;
			public int rank;
		}

		public static class List {

			public String user_like;
			public String user_id;
			public String user_name;
			public String head_pic;
			public int rank;
		}
	}
}
