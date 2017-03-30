package com.goby.fishing.common.photochoose;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/1.
 */
public class ImageUrlUtils {
    public final static String URL_PREFIX = "http://file.tgimg.cn/Image/Show?fid=";
    public final static String SUFFIX = "@300w_300h_100Q";
    public final static String FILE_PREFIX = "file://";
    public final static String STORAGE_PREFIX = "storage";

    /**
     * 获取小图片
     *
     * @return
     */
    public static String getSmallImageUrl(String imageUrl) {
        if (imageUrl.startsWith(URL_PREFIX) && !imageUrl.endsWith(SUFFIX)) {
            return imageUrl + SUFFIX;
        } else {
            return imageUrl;
        }
    }

    /**
     * 获取大图
     *
     * @param imageUrl
     * @return
     */
    public static String getBigImageUrl(String imageUrl) {
        if (imageUrl.startsWith(URL_PREFIX) && imageUrl != null && imageUrl.endsWith(SUFFIX)) {
            return imageUrl.substring(0, imageUrl.length() - SUFFIX.length());
        } else {
            return imageUrl;
        }
    }

    /**
     * 界面显示图片的时候统一先调用这个M格式化图片路径
     * @param imageUrl
     * @return
     */
    public static String getDisplayUrl(String imageUrl) {
        if (imageUrl != null) {
            if (isHttpPath(imageUrl)) {//http://
                return imageUrl;
            } else if (isFilePath(imageUrl)) {//file://
                return imageUrl;
            } else if (isStoragePath(imageUrl)) {//storage/xxx
                return ImageDownloader.Scheme.FILE.wrap(imageUrl);
            } else {//xxx.png
                return URL_PREFIX + imageUrl;
            }
        }
        return imageUrl;
    }


    /**
     * 获取图片显示的路径
     */
//    public static String getDisplayUrl(String url) {
//        if(url == null) return url;
//        String transformUrl = getImageUrl(url);
//        if (isHttpPath(transformUrl) || isFilePath(transformUrl)) {
//            return transformUrl;
//        }
//        return URL_PREFIX + url;
//    }
//
//
//    /**
//     * 获取图片显示的路径
//     */
//    public static List<String> getDisplayUrl(List<String> url) {
//        if (url == null) {
//            List<String> list = new ArrayList<>();
//            return list;
//        }
//        List<String> displayUrl = new ArrayList<>();
//        for (int i = 0; i < url.size(); i++) {
//            if (url.get(i) == null || isHttpPath(url.get(i)) || isFilePath(url.get(i))) {
//                continue;
//            }
//            displayUrl.add(URL_PREFIX + url.get(i));
//        }
//        return displayUrl;
//    }

    /**
     * 图片名 转成 服务器完整地址
     * 这个是在保存数据库用到，之前没重构前可能很多地方都调用了，没删，不过不影响
     * 57646181775-888c7875-86e2-41f4-a1a3-3eed9df7defa.png  -> http://file.tgimg.cn/Image/Show?fid=57646181775-888c7875-86e2-41f4-a1a3-3eed9df7defa.png
     * @param url
     * @return
     */
    public static List<String> transfromCompleteUrl(List<String> url) {
        if (url == null) {
            List<String> list = new ArrayList<String>();
            return list;
        }
        List<String> transfromList = new ArrayList<String>();
        for (int i = 0; i < url.size(); i++) {
            if (url.get(i).startsWith("http")) {
                continue;
            }
            transfromList.add(URL_PREFIX + url.get(i));
        }
        return transfromList;
    }

    /**
     * 服务器完整地址 转成 图片名
     * 这个方法基本没用，因为已经在store里面做了判断，如果是服务器路径直接上传服务器路径，方法是transfromToServerUrl(String url)
     * http://file.tgimg.cn/Image/Show?fid=57646181775-888c7875-86e2-41f4-a1a3-3eed9df7defa.png  -> 57646181775-888c7875-86e2-41f4-a1a3-3eed9df7defa.png
     *
     * @param url
     * @return
     */
    @Deprecated
    public static List<String> transfromToServerUrl(List<String> url) {
        if (url == null) {
            List<String> list = new ArrayList<String>();
            return list;
        }
        List<String> transfromList = new ArrayList<String>();
        for (int i = 0; i < url.size(); i++) {
            if (url.get(i).startsWith(URL_PREFIX)) {
                transfromList.add(url.get(i).replace(URL_PREFIX, ""));
            } else {
                transfromList.add(url.get(i));
            }
        }
        return transfromList;
    }

    /**
     * 这个方法是在store里面调用，上传之前判断是否是服务器路径，如果是的话就上传服务器路径，不要再上传一次图片了
     * @param url
     * @return
     */
    public static String transfromToServerUrl(String url){
        if(url == null){
            return "";
        }
        return url.replace(URL_PREFIX, "");
    }


    public static boolean isHttpPath(String url) {
        return url.startsWith("http");
    }

    public static boolean isFilePath(String url) {
        return url.startsWith(FILE_PREFIX);
    }

    public static boolean isStoragePath(String url) {
        return url.contains(STORAGE_PREFIX);
    }

    public static boolean isServerUrl(String url) {
        if(isHttpPath(url)){
            return true;
        } else if(isFilePath(url) || isStoragePath(url)){
            return false;
        }
        return true;
    }

}
