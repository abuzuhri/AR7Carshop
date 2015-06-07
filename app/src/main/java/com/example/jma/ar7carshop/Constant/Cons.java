package com.example.jma.ar7carshop.Constant;

import android.os.Environment;

/**
 * Created by JMA on 5/30/2015.
 */
public class Cons {
    public static String CARS_IMAGE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AR7_cars/Image";
    //public static String CARS_IMAGE_DIRECTORY = ;
    public enum OrderType {
        Manufacture(0),
        Model(1),
        Price(2);
        public int id;

        private OrderType(int id) {
            this.id = id;
        }

        public static String getOrderType(int id) {
            switch (id) {
                case 0:
                    return "manufacture";
                case 1:
                    return "modelYear";
                case 2:
                    return "price";
                default:
                    return "_ID";
            }
        }
    }

    public enum OrderDirection {
        ASC(0),
        DESC(1);
        public int id;

        private OrderDirection(int id) {
            this.id = id;
        }

        public static String getOrderDirection(int id) {
            switch (id) {
                case 0:
                    return "ASC";
                case 1:
                    return "DESC";
                default:
                    return "DESC";
            }
        }
    }
}
