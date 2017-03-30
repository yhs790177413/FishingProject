package com.example.controller.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FishingListBean extends BaseBean {

    public Data data;

    public static class Data {

        public ArrayList<List> list;
        public MapCenter map_center;

        public static class MapCenter {
            public double latitude;
            public double longitude;
        }

        public static class List implements Serializable {

            public String summary;
            public int comment_number;
            public String distance;
            public int like_number; // 0 未喜欢 1 喜欢
            public double latitude;
            public double longitude;
            public int recommend; // 0 未推荐 1 推荐
            public int water_type;
            public int price;
            public String name;
            public Weather weather;
            public int id;
            public long time;
            public int visit;
            public String pic_url;
            public int location_number;

            public static class Weather implements Serializable {

                public int temp;
                public int temp_rise;
                public float pressure;
                public String time;
                public String cond;
                public int pressure_rise;
            }
        }
    }
}
