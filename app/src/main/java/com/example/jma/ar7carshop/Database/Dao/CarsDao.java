package com.example.jma.ar7carshop.Database.Dao;

import com.activeandroid.query.Select;
import com.example.jma.ar7carshop.Constant.Cons;
import com.example.jma.ar7carshop.Database.Models.Car;

import java.util.Calendar;
import java.util.List;

/**
 * Created by JMA on 5/30/2015.
 */
public class CarsDao {
    public Car getCarByID(long id) {
        return Car.load(Car.class, id);
    }

    public void addCar(Long ID, String manufacture, String model, int modelYear, int price,
                       int millage, String picURL, String sellerName, String mobile, String location, double lat,
                       double lng, Boolean isLiked) {
        Car car = null;
        if (ID != null && ID != 0)
            car = Car.load(Car.class, ID.longValue());
        else
            car = new Car();

        car.manufacture = manufacture;
        car.model = model;
        car.modelYear = modelYear;
        car.price = price;
        car.millage = millage;
        car.pictureURL = picURL;
        car.sellerName = sellerName;
        car.mobile = mobile;
        car.location = location;
        car.isLiked = isLiked;
        car.lat = lat;
        car.lng = lng;

        car.save();
    }

    public Boolean ToggleLikeCar(Long ID) {
        Car car = null;
        if (ID != null && ID != 0)
            car = Car.load(Car.class, ID.longValue());
        else
            return false;

        Boolean res = car.isLiked = !car.isLiked;
        car.save();
        return res;
    }

    public List<Car> getAllCars(int orderType, int orderDir) {
        return new Select().from(Car.class).orderBy(Cons.OrderType.getOrderType(orderType) + " " +
                Cons.OrderDirection.getOrderDirection(orderDir)).execute();
    }

    public List<Car> getWishList(int orderType, int orderDir) {
        return new Select().from(Car.class).where("isLiked = ?", true).orderBy(Cons.OrderType.getOrderType(orderType) + " " +
                Cons.OrderDirection.getOrderDirection(orderDir)).execute();
    }
}
