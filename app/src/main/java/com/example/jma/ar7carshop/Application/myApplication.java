package com.example.jma.ar7carshop.Application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import java.util.Locale;

/**
 * Created by Tareq on 03/03/2015.
 */
public class myApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initializeDB();
        settingLanguage("ar");
    }

    public static Context getContext(){
        return mContext;
    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        //configurationBuilder.addModelClasses(Test.class);

        ActiveAndroid.initialize(configurationBuilder.create());
    }
    public void settingLanguage(String langCode) {
        Resources res = getBaseContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(langCode.toLowerCase());
        res.updateConfiguration(conf, dm);
    }

}
