package com.example.jma.ar7carshop.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{


	private static String DB_NAME = "ramadan.db";
	public SQLiteDatabase myDataBase;
	private Context mycontext;
	private String DB_PATH = "";
	private static final int DATABASE_VERSION = 1;
	 
	private static String CATEGORY_TABLE="categories";
	private static String OBJECT_TABLE="cities";
	
	public DataBaseHelper(Context context) throws IOException {
        super(context,DB_NAME,null,DATABASE_VERSION);
        this.mycontext=context;
        DB_PATH = "/data/data/"+mycontext.getApplicationContext().getPackageName()+"/databases/";
        boolean dbexist = checkdatabase();
        if (dbexist) {
            //System.out.println("Database exists");
            opendatabase(); 
        } else {
            System.out.println("Database doesn't exist");
            createdatabase();
        }
        close();
    }

	
	 public void createdatabase() throws IOException {
	        boolean dbexist = checkdatabase();
	        if(dbexist) {
	            //System.out.println(" Database exists.");
	        } else {
	            this.getReadableDatabase();
	            try {
	                copydatabase();
	            } catch(IOException e) {
	                throw new Error("Error copying database");
	            }
	        }
	    }   

	    private boolean checkdatabase() {
	        //SQLiteDatabase checkdb = null;
	        boolean checkdb = false;
	        try {
	            String myPath = DB_PATH + DB_NAME;
	            File dbfile = new File(myPath);
	            //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
	            checkdb = dbfile.exists();
	        } catch(SQLiteException e) {
	            System.out.println("Database doesn't exist");
	        }
	        return checkdb;
	    }

	    private void copydatabase() throws IOException {
	        //Open your local db as the input stream
	        InputStream myinput = mycontext.getAssets().open(DB_NAME);

	        // Path to the just created empty db
	        String outfilename = DB_PATH + DB_NAME;

	        //Open the empty db as the output stream
	        OutputStream myoutput = new FileOutputStream(outfilename);

	        // transfer byte to inputfile to outputfile
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = myinput.read(buffer))>0) {
	            myoutput.write(buffer,0,length);
	        }

	        //Close the streams
	        myoutput.flush();
	        myoutput.close();
	        myinput.close();
	    }
	    public void opendatabase() throws SQLException {
	        //Open the database
	        String mypath = DB_PATH + DB_NAME;
	        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
	    }
		public ArrayList<CityDB> getCityName(double lat, double lng) {
			ArrayList<CityDB> CityList = new ArrayList<CityDB>();
	        // Select All Query
	        String selectQuery = "SELECT  city,city_ar FROM cities where latitude like '" +lat+"%'"
	        		+" and longitude like '"+lng+"%'";
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	        CityDB obj= null;
	        if (cursor.moveToFirst()) {
	            do {
	            	obj = new CityDB();
	            	obj.setNameEng(cursor.getString(0));
	            	obj.setNameAr(cursor.getString(1));
	            	CityList.add(obj);
	            } while (cursor.moveToNext());
	        }
	        return CityList;
	    }
		public ArrayList<CityDB> getAllCities(String txt) {
			ArrayList<CityDB> CityList = new ArrayList<CityDB>();
	        // Select All Query
	        String selectQuery = "SELECT  city,city_ar,latitude,longitude FROM cities"
	        		+" where city_ar like '%"+txt+"%'"+" or city like '%"+txt+"%'";
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	        CityDB obj= null;
	        if (cursor.moveToFirst()) {
	            do {
	            	obj = new CityDB();
	            	obj.setNameEng(cursor.getString(0));
	            	obj.setNameAr(cursor.getString(1));
	            	obj.setLatitude(cursor.getDouble(2));
	            	obj.setLongitude(cursor.getDouble(3));
	            	CityList.add(obj);
	            } while (cursor.moveToNext());
	        }
	        return CityList;
	    }
		/*
		public MineCraftInfo getObjectFullInfo(int ID) {
			MineCraftInfo info =new MineCraftInfo();
			SQLiteDatabase db = this.getReadableDatabase();
			
			String selectQuery = "select information._id,object,information,name,dec,category from information join objects on information.object=objects._id where object ="+ID;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				info.setID(Integer.parseInt(cursor.getString(0)));
				info.setObject(Integer.parseInt(cursor.getString(1)));
				info.setInformation(cursor.getString(2));
				info.setName(cursor.getString(3));
				info.setDec(cursor.getString(4));
				info.setCategory(cursor.getString(5));
	        }

			selectQuery = "select image_name from images where object ="+ID;
			Cursor cursorImg = db.rawQuery(selectQuery, null);
			if (cursorImg.moveToFirst()) {
				info.setImage_name(cursorImg.getString(0));
	        }			
			
			selectQuery = "select recipe from recipe_brewing where object ="+ID;
			Cursor cursorBrewing = db.rawQuery(selectQuery, null);
			if (cursorBrewing.moveToFirst()) {
				ArrayList<String> brewingAry=new ArrayList<String>();
				do {
					brewingAry.add(cursorBrewing.getString(0));
				
				}while (cursorBrewing.moveToNext());
				info.setBrewing(brewingAry);
	        }
			
			selectQuery = "select recipe from recipe_crafting where object ="+ID;
			Cursor cursorCrafting = db.rawQuery(selectQuery, null);
			if (cursorCrafting.moveToFirst()) {
				ArrayList<String> craftingAry=new ArrayList<String>();
				do {
					craftingAry.add(cursorCrafting.getString(0));
				
				}while (cursorCrafting.moveToNext());
				info.setCrafting(craftingAry);
	        }						
			
			selectQuery = "select recipe from recipe_smelting where object ="+ID;
			Cursor cursorSmelting = db.rawQuery(selectQuery, null);
			if (cursorSmelting.moveToFirst()) {
				ArrayList<String> smeltingAry=new ArrayList<String>();
				do {
					smeltingAry.add(cursorSmelting.getString(0));
				
				}while (cursorSmelting.moveToNext());
				info.setSmelting(smeltingAry);
	        }
			
	        return info;
		}		

		public ArrayList<MineCraftCategory> getAllMineCraftCategory() {
	    	ArrayList<MineCraftCategory> mineCraftCategoryList = new ArrayList<MineCraftCategory>();
	        // Select All Query
	        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;
	 
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	MineCraftCategory catgry = new MineCraftCategory();
	            	catgry.setID(Integer.parseInt(cursor.getString(0)));
	            	catgry.setName(cursor.getString(1));
	            	catgry.setImage(Integer.parseInt(cursor.getString(2)));
	            	catgry.setObject_category(cursor.getString(3));
	            	Log.i("tag","catg>"+catgry.toString());
	                // Adding contact to list
	            	mineCraftCategoryList.add(catgry);
	            } while (cursor.moveToNext());
	        }
	 
	        // return contact list
	        return mineCraftCategoryList;
	    }
*/	    
	    public synchronized void close() {
	        if(myDataBase != null) {
	            myDataBase.close();
	        }
	        super.close();
	    }

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}





}
