package com.example.jma.ar7carshop.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jma.ar7carshop.Database.Dao.CarsDao;
import com.example.jma.ar7carshop.Database.Models.Car;
import com.example.jma.ar7carshop.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by JMA on 5/31/2015.
 */
public class DetailsActivity extends Activity {
    private ImageView img_result, img_like;
    private Button btn_map;
    private TextView /*add_car_manuf,*/ add_car_model, add_car_model_year, add_car_price, add_car_millage,
            add_car_sellerName, add_car_sellerMobile, add_car_position;
    private ProgressBar add_pbPosition;
    private long position;
    private CarsDao carsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcar);
        position = getIntent().getExtras().getLong("ID");
        carsDao = new CarsDao();

        initializeViews();
        fillData();
    }


    private void fillData() {
        final Car car = Car.load(Car.class, position);
        if (car == null)
            finish();
        //add_car_manuf.setText(car.manufacture);
        add_car_model.setText(car.model);
        add_car_model_year.setText(car.modelYear + "");
        add_car_price.setText(formatNumber(car.price) + " $");
        add_car_millage.setText(formatNumber(car.millage) + " KM");
        add_car_sellerName.setText(car.sellerName);
        add_car_sellerMobile.setText(car.mobile);
        add_car_position.setText(car.location);

        try {
            File f = new File(car.pictureURL);
            if (f.exists()) {
                InputStream is = new FileInputStream(f);
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inSampleSize = 3;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, op);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                img_result.setBackgroundDrawable(d);
            } else
                img_result.setBackgroundResource(R.drawable.car);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage() + "", Toast.LENGTH_LONG).show();
        }

        if (car.isLiked)
            img_like.setImageResource(R.drawable.dislike);
        else
            img_like.setImageResource(R.drawable.like);
        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean res = carsDao.ToggleLikeCar(position);
                if (res)
                    img_like.setImageResource(R.drawable.dislike);
                else
                    img_like.setImageResource(R.drawable.like);
            }
        });
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f", car.lat, car.lng, car.lat, car.lng);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        img_result = (ImageView) findViewById(R.id.img_result);
        img_like = (ImageView) findViewById(R.id.img_like);

        //add_car_manuf = (TextView) findViewById(R.id.add_car_manuf);
        //add_car_manuf.setEnabled(false);
        add_car_model = (TextView) findViewById(R.id.add_car_model);
        add_car_model.setEnabled(false);
        add_car_model_year = (TextView) findViewById(R.id.add_car_model_year);
        add_car_model_year.setEnabled(false);
        add_car_price = (TextView) findViewById(R.id.add_car_price);
        add_car_price.setEnabled(false);
        add_car_millage = (TextView) findViewById(R.id.add_car_millage);
        add_car_millage.setEnabled(false);
        add_car_sellerName = (TextView) findViewById(R.id.add_car_sellerName);
        add_car_sellerName.setEnabled(false);
        add_car_sellerMobile = (TextView) findViewById(R.id.add_car_sellerMobile);
        add_car_sellerMobile.setEnabled(false);
        add_car_position = (TextView) findViewById(R.id.add_car_position);
        add_car_position.setEnabled(false);

        btn_map = (Button) findViewById(R.id.btn_show_map);


        add_pbPosition = (ProgressBar) findViewById(R.id.add_pbPosition);
        add_pbPosition.setVisibility(View.GONE);
    }

    private String formatNumber(int num) {
        StringBuilder res;
        String temp = "";
        res = new StringBuilder(num + "").reverse();
        for (int i = 0; i < res.length(); i++) {
            temp += res.charAt(i);
            if ((i + 1) % 3 == 0 && (i + 1) != res.length())
                temp += ",";
        }
        res = new StringBuilder(temp).reverse();
        return res.toString();
    }
}
