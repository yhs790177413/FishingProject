package com.example.controller.bean;

import java.util.ArrayList;

public class WeatherDetailBean extends BaseBean {

    public Data data;

    public static class Data {

        public int fish_point_id;
        public String country;
        public WeatherCurrent weather_current;
        public ArrayList<WeatherTime> weather_time;

        public static class WeatherCurrent {

            public String wind_deg_name;
            public String cond_icon;
            public int temp;
            public int code;
            public int humidity;
            public String wind_speed_name;
            public int temp_rise;
            public double pressure;
            public long time;
            public int clouds;
            public String cond;
            public int pressure_rise;
        }

        public static class WeatherTime {


            public int date;
            public String wind_deg_name;
            public String cond_icon;
            public float rain;
            public int temp;
            public String wind_speed_name;
            public int temp_rise;
            public double pressure;
            public int clouds;
            public int temp_max;
            public String cond;
            public int temp_min;
            public double wind_deg;
            public int snow;
            public int cond_id;
            public int humidity;
            public double wind_speed;
            public long time;
            public int pressure_rise;
        }
    }
}
