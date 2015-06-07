package com.example.jma.ar7carshop.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jma.ar7carshop.Constant.Cons;
import com.example.jma.ar7carshop.Database.CityDB;
import com.example.jma.ar7carshop.Database.Dao.CarsDao;
import com.example.jma.ar7carshop.Database.DataBaseHelper;
import com.example.jma.ar7carshop.R;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.FileUtils;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by JMA on 5/30/2015.
 */
public class AddActivity extends Activity implements LocationListener,ImageChooserListener {
    private ImageView img_result, img_like;
    private Button btn_add_takeImg, btn_save, add_car_position;
    private TextView /*add_car_manuf,*/ add_car_model, add_car_model_year, add_car_price, add_car_millage,
            add_car_sellerName, add_car_sellerMobile;
    private ProgressBar add_pbPosition;
    boolean successRecord = false;
    private String outputFile = "", country = "";
    private Uri fileUri;
    private double lat, lng;
    private int year;
    private static final int IMAGE_CAPTURE = 100;
    private LocationManager locationManager;
    private Location location;
    private boolean isGPSEnabled;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 1 minute
    private Context context;


    private String foldername="AR7_cars";
    protected Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcar);
        initializeViews();
        context = this;

        btn_add_takeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

        add_car_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_pbPosition.setVisibility(View.VISIBLE);
                btn_save.setEnabled(false);
                useWIFI();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar();
            }
        });
    }

    private void addCar() {
        if (add_car_model.getText().toString().trim().equals("")
                || add_car_model_year.getText().toString().trim().equals("") || add_car_millage.getText().toString().trim().equals("")
                || add_car_price.getText().toString().trim().equals("") || add_car_position.getText().toString().trim().equals("") ||
                add_car_sellerMobile.getText().toString().trim().equals("") || add_car_sellerName.getText().toString().trim().equals("")
                || outputFile.trim().equals("") || country.equals(""))
            Toast.makeText(getBaseContext(), "Please enter all fields", Toast.LENGTH_LONG).show();
        else {
            try {
                Log.i("tg","Add iii outputFile "+outputFile);

                new CarsDao().addCar(null, "", add_car_model.getText().toString().trim(),
                        year, Integer.parseInt(add_car_price.getText().toString().trim())
                        , Integer.parseInt(add_car_millage.getText().toString().trim()), outputFile, add_car_sellerName.getText().toString().trim()
                        , add_car_sellerMobile.getText().toString().trim(), country, lat, lng, false);
                Toast.makeText(getBaseContext(), "Car added successfully", Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void initializeViews() {
        img_result = (ImageView) findViewById(R.id.img_result);
        img_like = (ImageView) findViewById(R.id.img_like);

        //add_car_manuf = (TextView) findViewById(R.id.add_car_manuf);
        add_car_model = (TextView) findViewById(R.id.add_car_model);
        add_car_model_year = (TextView) findViewById(R.id.add_car_model_year);
        add_car_price = (TextView) findViewById(R.id.add_car_price);
        add_car_millage = (TextView) findViewById(R.id.add_car_millage);
        add_car_sellerName = (TextView) findViewById(R.id.add_car_sellerName);
        add_car_sellerMobile = (TextView) findViewById(R.id.add_car_sellerMobile);

        add_pbPosition = (ProgressBar) findViewById(R.id.add_pbPosition);
        add_pbPosition.setVisibility(View.GONE);

        btn_add_takeImg = (Button) findViewById(R.id.btn_add_takeImg);
        add_car_position = (Button) findViewById(R.id.add_car_position);
        btn_save = (Button) findViewById(R.id.btn_save);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {




            super.onActivityResult(requestCode, resultCode, data);


        Log.i("tg", "onActivityResult   finish");
        Log.i("tg", "IMAGE_CAPTURE   " + requestCode + "  " + IMAGE_CAPTURE);
        Log.i("tg", "RESULT_OK   " + resultCode + "  " + RESULT_OK);


        if (requestCode == IMAGE_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    Log.i("tg", "successRecord   true");
                    successRecord = true;
                    try {
                        File f = new File(outputFile);
                        if (f.exists()) {
                            //Bundle extras = data.getExtras();
                            //Bitmap mImageBitmap = (Bitmap) extras.get("data");
                            //Drawable d = new BitmapDrawable(context.getResources(), mImageBitmap);
                            //img_result.setBackgroundDrawable(d);

                            InputStream is = new FileInputStream(f);
                            BitmapFactory.Options op = new BitmapFactory.Options();
                            op.inSampleSize = 8;
                            Bitmap bitmap = BitmapFactory.decodeStream(is, null, op);
                            Drawable d = new BitmapDrawable(context.getResources(), bitmap);
                            img_result.setBackgroundDrawable(d);

                        } else {
                            img_result.setBackgroundResource(R.drawable.car);
                            Log.i("tg", "onActivityResult       " + outputFile);
                        }
                    } catch (Exception e) {
                        Log.i("tg", "onActivityResult       " + outputFile);
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    successRecord = false;
                } else {

                }
            }



    }

    public void show() {

        final Dialog d = new Dialog(AddActivity.this);
        d.setTitle("Select Year");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(2050);
        np.setMinValue(1990);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Do some thing when the picker number changed
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = np.getValue();
                add_car_model_year.setText(String.valueOf(year));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

    public void open() {

        Intent e = new Intent("android.media.action.IMAGE_CAPTURE");
        this.outputFile = FileUtils.getDirectory(this.foldername) + File.separator + Calendar.getInstance().getTimeInMillis() + ".jpg";

        Log.i("tg", outputFile);
        Log.i("tg", outputFile);
        e.putExtra("output", Uri.fromFile(new File(this.outputFile)));
        if(this.extras != null) {
            e.putExtras(this.extras);
        }


        startActivityForResult(e, IMAGE_CAPTURE);

        /*String s_directory = Cons.CARS_IMAGE_DIRECTORY;
        //outputFile = s_directory + "/IMG_" + (int) (Math.random() * 10000000) + ".jpg";
        try {
        File tempFile = File.createTempFile("car" + (int) (Math.random() * 10000000), ".jpg");
        outputFile = tempFile.getAbsolutePath();
        //outputFile = Environment.getExternalStorageDirectory().getName() + File.separatorChar + "Android/data/" + getPackageName() + "/files/abc" + (int) (Math.random() * 10000000) +  ".jpg";
        File mediaFile = new File(outputFile);

            if(mediaFile.exists() == false) {
                mediaFile.getParentFile().mkdirs();
                mediaFile.createNewFile();
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, IMAGE_CAPTURE);

        } catch (IOException e) {
            Log.e("tg", "Could not create file.", e);
        }
        Log.i("tg", outputFile);

        */
    }

    @Override
    public void onLocationChanged(Location location) {
        add_pbPosition.setVisibility(View.GONE);
        btn_save.setEnabled(true);
        lat = location.getLatitude();
        lng = location.getLongitude();

        double lat_1 = ((int) (lat * 10)) / 10.0;
        double long_1 = ((int) (lng * 10)) / 10.0;
        try {
            DataBaseHelper db = new DataBaseHelper(this);
            ArrayList<CityDB> Cities = db.getCityName(lat_1, long_1);
            Log.d("city", Cities.size() + "");
            for (CityDB city : Cities) {
                if (city != null && city.getNameEng() != null) {
                    country = city.getNameEng();
                } else if (city != null && city.getNameEng() == null) {
                    country = city.getNameAr();
                } else if (city != null && city.getNameEng() != null
                        && city.getNameAr() != null) {
                    country = city.getNameAr();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        add_car_position.setText(country);
        stopUsingGPS();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(AddActivity.this);
        }
    }

    private void useWIFI() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled && checkConn(context)) {
            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                    }
                }
            }
        } else {
            useGPS();
        }
    }

    private void useGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        /*
                         * Log.d("GGG", "Location not null!"); lat =
						 * location.getLatitude(); lng =
						 * location.getLongitude();
						 */
                    }
                }
            }
        } else
            showSettingsAlert();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Enable GPS");
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 1999);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public boolean checkConn(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() == null)
            return false;
        if (conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else if (!conMgr.getActiveNetworkInfo().isConnected()) {
            return false;
        }
        return false;
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    img_result.setImageURI(Uri.parse(new File(image.getFilePathOriginal()).toString()) );

                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Toast.makeText(AddActivity.this, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}