package com.example.controller.bean;

import java.util.ArrayList;

public class AdBean {

	public ParentData data;
	
	public static class ParentData{
		
		public String result;
		public String desc;
		public ArrayList<Data> data;
		
		public static class Data{
			
			public String link;
			public String img;
		}
	}
}
