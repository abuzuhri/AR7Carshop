package com.example.jma.ar7carshop.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jma.ar7carshop.Activities.DetailsActivity;
import com.example.jma.ar7carshop.Adapters.CarsAdapter;
import com.example.jma.ar7carshop.Adapters.IClickCardView;
import com.example.jma.ar7carshop.Constant.Cons;
import com.example.jma.ar7carshop.Database.Dao.CarsDao;
import com.example.jma.ar7carshop.Database.Models.Car;
import com.example.jma.ar7carshop.R;

import java.util.List;


/**
 * Created by Tareq on 03/12/2015.
 */
public class AllCarsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Button btn_manuf, btnPrice, btn_model;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Boolean filterToggle = true;
    private Typeface tf_roboto_light;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void fillData(int orderType, int orderDir) {
        CarsDao carsDao = new CarsDao();
        List<Car> cars = carsDao.getAllCars(orderType, orderDir);
        mAdapter = new CarsAdapter(cars, tf_roboto_light, getActivity(), new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                Intent in = new Intent(getActivity(), DetailsActivity.class);
                in.putExtra("ID", ID);
                startActivity(in);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);

        tf_roboto_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/jaz.otf");

        btn_manuf = (Button) rootView.findViewById(R.id.btnManuFilter);
        btnPrice = (Button) rootView.findViewById(R.id.btnPriceFilter);
        btn_model = (Button) rootView.findViewById(R.id.btnModelFilter);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        fillData(-1, -1);

//        btn_manuf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (filterToggle) {
//                    fillData(Cons.OrderType.Manufacture.id, Cons.OrderDirection.ASC.id);
//                    filterToggle = false;
//                } else {
//                    fillData(Cons.OrderType.Manufacture.id, Cons.OrderDirection.DESC.id);
//                    filterToggle = true;
//                }
//            }
//        });

        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterToggle) {
                    fillData(Cons.OrderType.Price.id, Cons.OrderDirection.ASC.id);
                    filterToggle = false;
                } else {
                    fillData(Cons.OrderType.Price.id, Cons.OrderDirection.DESC.id);
                    filterToggle = true;
                }
            }
        });

        btn_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterToggle) {
                    fillData(Cons.OrderType.Model.id, Cons.OrderDirection.ASC.id);
                    filterToggle = false;
                } else {
                    fillData(Cons.OrderType.Model.id, Cons.OrderDirection.DESC.id);
                    filterToggle = true;
                }
            }
        });

        return rootView;
    }

}
