package com.example.controller.bean;

/**
 * 上传图片实体
 * @author Administrator
 *
 */
public class UploadHeadBean {

	public ParentData data;
	
	public static class ParentData{
		
		public String result;
		public String desc;
		public Data data;
		
		public static class Data{
			
			public String path;
		}
	}
}
