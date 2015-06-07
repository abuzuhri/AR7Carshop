package com.example.jma.ar7carshop.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jma.ar7carshop.R;

/**
 * Created by mohamad El-lada on 03/20/2015.
 */
public class CarViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

    public TextView car_card_modelYear;
    public TextView car_card_price;
    //public TextView car_card_manufacture;
    public TextView car_card_millage;
    //public ImageView img_like;
    public ImageView img_car;

    public IClickCardView mListener;

    public CarViewHolder(View itemLayoutView, IClickCardView listener) {
        super(itemLayoutView);
        mListener = listener;
        car_card_modelYear = (TextView) itemLayoutView.findViewById(R.id.car_card_modelYear);
        car_card_price = (TextView) itemLayoutView.findViewById(R.id.car_card_price);
        //car_card_manufacture = (TextView) itemLayoutView.findViewById(R.id.car_card_manufacture);
        car_card_millage = (TextView) itemLayoutView.findViewById(R.id.car_card_millage);
        //img_like = (ImageView) itemLayoutView.findViewById(R.id.img_like);
        img_car = (ImageView) itemLayoutView.findViewById(R.id.img_car);
        itemLayoutView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        mListener.onClick(v, getID());
    }


}
