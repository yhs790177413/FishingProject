package com.example.controller.bean;

import java.util.ArrayList;

public class ChannelListBean {

	public ArrayList<ChannelItem> data;
	
	public static class ChannelItem{
		
		/**
	     * 栏目对应ID
	     */
	    public Integer id;
	    /**
	     * 栏目对应name
	     */
	    public String name;
	    /**
	     * 栏目在整体中的排序顺序  rank
	     */
	    public Integer orderId;
	    /**
	     * 栏目是否选中
	     */
	    public Integer selected;
	}
}
