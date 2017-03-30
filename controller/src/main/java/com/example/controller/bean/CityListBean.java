package com.example.controller.bean;

import java.util.ArrayList;

public class CityListBean extends BaseBean{
	
	public Data data;
	
	public static class Data{
		
		public ArrayList<City> list;
		
		public static class City{
			
			public String code;
			public String name;
			public int id;
			public int province_id;
		}
	}

}
