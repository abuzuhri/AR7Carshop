package com.example.jma.ar7carshop.Database.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by JMA on 5/30/2015.
 */

@Table(name = "Cars",id = "_ID")
public class Car extends Model {
    public Car() {
        super();
    }
    @Column
    public String manufacture;
    @Column
    public String model;
    @Column
    public int modelYear;
    @Column
    public int price;
    @Column
    public int millage;
    @Column
    public String pictureURL;
    @Column
    public String sellerName;
    @Column
    public String mobile;
    @Column
    public String location;
    @Column
    public Boolean isLiked;
    @Column
    public double lat;
    @Column
    public double lng;
}
