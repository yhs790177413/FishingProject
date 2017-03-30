package com.example.controller.bean;

import java.util.ArrayList;

public class AddFooterprintBean extends BaseBean {

	public Data data;

	public static class Data {

		public int id;
		public ArrayList<String> picture_token;
		public ArrayList<String> pic_urls;
	}
}
