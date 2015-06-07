package com.example.jma.ar7carshop.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.jma.ar7carshop.Database.Dao.CarsDao;
import com.example.jma.ar7carshop.Fragments.AllCarsFragment;
import com.example.jma.ar7carshop.Fragments.WishFragment;
import com.example.jma.ar7carshop.R;
import com.example.jma.ar7carshop.SlidingTabs.SlidingTabLayout;


public class MainActivity extends ActionBarActivity {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private SharedPreferences sp;
    private Button btn_sell;
    private SharedPreferences.Editor spedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        spedit = sp.edit();

        btn_sell = (Button) findViewById(R.id.btn_sellnewcar);

        if (sp.getBoolean("isFirstTime", true)) {
            addFirst7Cars();
            spedit.putBoolean("isFirstTime", false).commit();
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        fillData();

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getBaseContext(),AddActivity.class);
                startActivity(in);
            }
        });
    }

    private void addFirst7Cars() {
        CarsDao carsDao = new CarsDao();
        carsDao.addCar(null, "Germany", "KIA", 2013, 22000, 500000, "", "Jaffer M Alagha", "0595114000", "Iran",34.34,51.22, false);
        carsDao.addCar(null, "India", "Golf", 2015, 19000, 550000, "", "Tareq abu zuhri", "0595114000", "Iran",34.34,51.22, false);
        carsDao.addCar(null, "Gaza", "Hyundai", 2011, 21000, 400000, "", "Ahmed Alhammadi", "0595114000", "Iran",34.34,51.22, false);
        carsDao.addCar(null, "China", "Micra", 2015, 15000, 0, "", "Ayman", "0595114000", "Iran",34.34,51.22, false);
        carsDao.addCar(null, "China", "KIA", 2011, 20000, 15000, "", "Jaffer M Alagha", "0595114000", "Iran",34.34,51.22, false);
        carsDao.addCar(null, "Germany", "KIA", 2013, 22000, 500000, "", "Jaffer M Alagha", "0595114000", "Iran",34.34,51.22, false);
        carsDao.addCar(null, "Germany", "KIA", 2013, 22000, 500000, "", "Jaffer M Alagha", "0595114000", "Iran",34.34,51.22, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    private void fillData() {
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setContentDescription(1, "Cars");
        mSlidingTabLayout.setContentDescription(2, "Wish List");

        mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);

        mSlidingTabLayout.setDistributeEvenly(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tabTitles[] = new String[]{"Cars", "Wish List"};
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(fragmentManager, tabTitles));
        mSlidingTabLayout.setViewPager(mViewPager);

    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[];

        public SampleFragmentPagerAdapter(FragmentManager fm, String tabTitles[]) {
            super(fm);
            this.tabTitles = tabTitles;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0)
                fragment = new AllCarsFragment();
            else
                fragment = new WishFragment();
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }


}
