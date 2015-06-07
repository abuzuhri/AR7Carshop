package com.example.jma.ar7carshop.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jma.ar7carshop.Database.Models.Car;
import com.example.jma.ar7carshop.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Mohamaed El-lada on 3/21/2015.
 */
public class CarsAdapter extends RecyclerView.Adapter<CarViewHolder> {
    private final Typeface tf;
    public List<Car> mDataset;
    private Context context;
    private IClickCardView mListener;

    public CarsAdapter(List<Car> myDataset, Typeface tf, Context context, IClickCardView mListener) {
        mDataset = myDataset;
        this.context = context;
        this.mListener = mListener;
        this.tf = tf;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_car, null);

        CarViewHolder viewHolder = new CarViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CarViewHolder viewHolder, int position) {

        Car car = mDataset.get(position);

        viewHolder.setID(car.getId());
//        viewHolder.car_card_manufacture.setText(car.manufacture);
//        viewHolder.car_card_manufacture.setTypeface(tf);

        viewHolder.car_card_modelYear.setText(car.model + "(" + car.modelYear + ")");
        viewHolder.car_card_modelYear.setTypeface(tf);

        viewHolder.car_card_millage.setText(formatNumber(car.millage) + " KM");
        viewHolder.car_card_millage.setTypeface(tf);

        viewHolder.car_card_price.setText(formatNumber(car.price) + " AED");
        viewHolder.car_card_price.setTypeface(tf);

        try {
            File f = new File(car.pictureURL);
            if (f.exists()) {
                InputStream is = new FileInputStream(f);
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, op);
                Drawable d = new BitmapDrawable(context.getResources(), bitmap);
                viewHolder.img_car.setBackgroundDrawable(d);
            } else
                viewHolder.img_car.setBackgroundResource(R.drawable.car);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (car.isLiked)
//            viewHolder.img_like.setImageResource(R.drawable.dislike);
//        else
//            viewHolder.img_like.setImageResource(R.drawable.like);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
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
