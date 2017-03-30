package com.example.controller.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FishCityListBean extends BaseBean {

	public ParentData data;
	
	public static class ParentData{
		
		public ArrayList<CityBean> list;

		public static class CityBean extends SortModel implements Serializable {

			public String name;
			public int id;
		}
	}

}
