package com.example.liguopeng.li_weather.databases;

public class WeatherDbSchema {
    public static final class WeatherTable {
        public static final String NAME = "weathers";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String INITDATE="initdate";
            public static final String ID = "id";
            public static final String LAT = "lat";
            public static final String LON = "lon";
            public static final String UPDATETIME = "updatetime";
            public static final String LOCATION = "location";
            public static final String TEP_MIN = "tmp_min";
            public static final String TEP_MAX = "tmp_max";
            public static final String HUM = "hum";
            public static final String PRES = "pres";
            public static final String WIND_SPD="wind_spd";
            public static final String WIND_DIR="wind_dir";
            public static final String DAY = "day";
            public static final String IMAGETEXTD = "imagetextd";
            public static final String IMAGETEXTN = "imagetextn";
            public static final String SRC_D = "src_d";
            public static final String SRC_N = "src_n";

        }
    }
        public static final class SettingTable {
            public static final String NAME = "setting";

            public static final class Cols {
                public static final String CITY = "city";

                public static final String UNTI = "unti";

                public static final String NOTIFICATION = "notification";
            }
        }


    }
