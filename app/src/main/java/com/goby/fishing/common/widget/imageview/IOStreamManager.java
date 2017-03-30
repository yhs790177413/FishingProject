package com.goby.fishing.common.widget.imageview;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 
 * @ClassName: IOStreamManager 图片处理�? --  估计用不到，到时候再�? * @Description: TODO 
 * @author ljj
 * @date 2014-10-9 上午10:27:48 
 *
 */
public class IOStreamManager {

	public static void closeInputStreams(InputStream... inputs) {
		for (InputStream input : inputs) {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// JUST close
				}
			}
		}
	}

	public static void closeOutputStreams(OutputStream... outputs) {
		for (OutputStream output : outputs) {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// JUST close
				}
			}
		}
	}
	
}
