package com.example.controller.bean;

import java.util.ArrayList;

public class WeatherBean {
	
	public ArrayList<Results> results;
	
	public static class Results{
		
		public Location location;
		public Now now;
		public String last_update;
		
		public static class Location{
			
			public String id;
			public String name;
			public String country;
			public String path;
			public String timezone;
			public String timezone_offset;
		}
		
		public static class Now{
			
			public String text;
			public String code;
			public String temperature;
		}
	}

}
