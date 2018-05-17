package com.inerun.dropinsta.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.inerun.dropinsta.constant.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by vineet on 12/1/16.
 */

public class OpenHelper extends SQLiteOpenHelper {


    public OpenHelper(Context context) {
        super(context, AppConstant.DB_NAME, null,
                AppConstant.DB_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PARCEL_TABLE);

        db.execSQL(CREATE_POD_TABLE);

        db.execSQL(CREATE_TRANSACTION_TABLE);

        db.execSQL(CREATE_STATUS_TABLE);

        db.execSQL(CREATE_PICKUP_DATA_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
//        db.execSQL("DROP TABLE IF EXISTS " + DataUtils.CATEGORY_TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + DataUtils.SUBCATEGORY_TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + DataUtils.WISHLIST_TABLE_NAME);

        onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();

    }




    public static void validateDatabase(Context mContext, String tablename, String... params) {
        try {


            OpenHelper lOpenHelper = new OpenHelper(mContext);
            SQLiteDatabase db = lOpenHelper.getWritableDatabase();
            db.execSQL(CREATE_PARCEL_TABLE);

            db.execSQL(CREATE_POD_TABLE);

            db.execSQL(CREATE_TRANSACTION_TABLE);

            db.execSQL(CREATE_STATUS_TABLE);

            db.execSQL(CREATE_PICKUP_DATA_TABLE);

            Cursor cursor = db.query(tablename, new String[]{}, null, null, null, null, null);
            ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
            if (params.length % 2 == 0) {
                for (int i = 0; i < params.length; i = i + 2) {
                    HashMap<String, String> hashMap = new HashMap();
                    hashMap.put("key", params[i]);
                    hashMap.put("query", "ALTER TABLE " + tablename + " ADD COLUMN " + params[i + 1]);
                    hashMapArrayList.add(hashMap);
                }

                for (int i = 0; i < hashMapArrayList.size(); i++) {

                    HashMap<String, String> hash = hashMapArrayList.get(i);
                    try {

                        Log.i("Cursor Index", "" + cursor.getColumnIndexOrThrow(hash.get("key")));
                    } catch (Exception e) {
                        Log.i("exception", hash.get("query") + "Doesnt Exists ");

                        db.execSQL(hash.get("query"));


                    }
                }
            } else {
                Log.i("validate db", "params Missing");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Exception", "exception in validating table: " + tablename);
        }

    }

    // ====Create Parcel Table====
    public static String CREATE_PARCEL_TABLE = "CREATE TABLE IF NOT EXISTS " + DataUtils.TABLE_NAME_PARCEL + "("
            + DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + DataUtils.PARCEL_BARCODE + " TEXT ,"
            + DataUtils.CONSIGNEE_NAME + " TEXT ,"

            + DataUtils.PARCEL_WEIGHT + " INTEGER DEFAULT -1,"
            + DataUtils.PARCEL_SPECIAL_INSTRUCTION + " TEXT ,"
            + DataUtils.PARCEL_DELIVERY_STATUS + " INTEGER DEFAULT -1,"
            + DataUtils.PARCEL_DELIVERY_COMMENT + " TEXT ,"
            + DataUtils.PARCEL_PAYMENT_TYPE + " INTEGER DEFAULT -1,"
            + DataUtils.PARCEL_PAYMENT_STATUS + " INTEGER DEFAULT -1,"
            + DataUtils.PARCEL_TYPE + " INTEGER DEFAULT -1,"
            + DataUtils.PARCEL_AMOUNT + " INTEGER DEFAULT -1,"
            + DataUtils.PARCEL_CURRENCY + " TEXT ,"
            + DataUtils.PARCEL_DATE + " TEXT ,"

            + DataUtils.SOURCE_ADDRESS1 + " TEXT,"
            + DataUtils.SOURCE_ADDRESS2 + " TEXT,"
            + DataUtils.SOURCE_CITY + " TEXT,"
            + DataUtils.SOURCE_STATE + " TEXT,"
            + DataUtils.SOURCE_PIN + " TEXT ,"
            + DataUtils.SOURCE_PHONE + " TEXT,"

            + DataUtils.DELIVERY_ADDRESS1 + " TEXT,"
            + DataUtils.DELIVERY_ADDRESS2 + " TEXT,"
            + DataUtils.DELIVERY_CITY + " TEXT,"
            + DataUtils.DELIVERY_STATE + " TEXT,"
            + DataUtils.DELIVERY_PIN + " TEXT,"
            + DataUtils.DELIVERY_PHONE + " TEXT,"

            + DataUtils.UPDATE_STATUS + " INTEGER DEFAULT -1,"
            + DataUtils.UPDATE_DATE + " TEXT ,"
            + DataUtils.CONSIGNEE_ID + " TEXT ,"
            + DataUtils.TRANSCROWID + " INTEGER DEFAULT -1 ,"
            + DataUtils.POD_ROW_ID + " INTEGER DEFAULT -1 " +
            ")";


    // =====Create POD Table====
    public static String CREATE_POD_TABLE = "CREATE TABLE IF NOT EXISTS " + DataUtils.TABLE_NAME_POD + "("
            + DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + DataUtils.POD_NAME + " TEXT ,"
            + DataUtils.POD_CREATED_TIME + " TEXT ,"
            + DataUtils.POD_UPDATED_TIME + " TEXT ,"
            + DataUtils.POD_NAME_ON_SERVER + " TEXT ,"
            + DataUtils.POD_STATUS + " INTEGER DEFAULT -1 ,"
            + DataUtils.PARCEL_BARCODE + " TEXT "
            + ")";


    // ====Create Transaction Table====
    public static String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + DataUtils.TABLE_NAME_TRANSACTION + "("
            + DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + DataUtils.TRANS_ID + " TEXT ,"
            + DataUtils.TRANS_TYPE + " TEXT ,"
            + DataUtils.TRANS_TIME_STAMP + " TEXT ,"
            + DataUtils.TRANS_RECEIVER_NAME + " TEXT ,"
            + DataUtils.TRANS_TOTAL_AMOUNT + " TEXT ,"
            + DataUtils.TRANS_CURRENCY + " TEXT ,"
            + DataUtils.TRANS_NATIONAL_ID + " TEXT "
            + ")";


    // =====Create Status Table====
    public static String CREATE_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + DataUtils.TABLE_NAME_STATUS + "("
            + DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + DataUtils.STATUS_TYPE + " TEXT ,"
            + DataUtils.STATUS_COMMENT + " TEXT ,"
            + DataUtils.STATUS_TIME_STAMP + " TEXT ,"
            + DataUtils.PARCEL_BARCODE + " TEXT ,"
            + DataUtils.UPDATE_STATUS + " INTEGER DEFAULT -1"
            + ")";

    // =====Create Pickup Table====
    public static String CREATE_PICKUP_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " + DataUtils.TABLE_NAME_PICKUP_DATA + "("
            + DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY," //0
            + DataUtils.PARCEL_BARCODE + " TEXT ,"          //1
            + DataUtils.CUSTOMER_ID + " INTEGER DEFAULT 0," //2
            + DataUtils.USER_FNAME + " TEXT ,"              //3
            + DataUtils.USER_LNAME + " TEXT ,"              //4
            + DataUtils.USER_EMAIL + " TEXT ,"              //5
            + DataUtils.USER_PHONE + " TEXT ,"              //6
            + DataUtils.USER_lANDLINE + " TEXT ,"           //7
            + DataUtils.USER_EXTENSION + " TEXT ,"          //8
            + DataUtils.PARCEL_PICKUP_STATUS + " INTEGER ," //9

            + DataUtils.PARCEL_LENGTH + " REAL ,"           //10
            + DataUtils.PARCEL_WIDTH + " REAL ,"            //11
            + DataUtils.PARCEL_HEIGHT + " REAL ,"           //12
            + DataUtils.PARCEL_VOLUME_WEIGHT + " REAL ,"    //13
            + DataUtils.PARCEL_ACTUAL_WEIGHT + " REAL ,"    //14
            + DataUtils.PARCEL_PRICE + " REAL ,"            //15
            + DataUtils.PARCEL_DESCRIPTION + " TEXT ,"      //16
            + DataUtils.PARCEL_SPECIAL_INS + " TEXT ,"      //17
            + DataUtils.PARCEL_ASSIGN_DATE + " TEXT ,"      //18
            + DataUtils.PARCEL_CREATED_ON + " TEXT ,"       //19
            + DataUtils.PARCEL_PICKUP_COMMENT + " TEXT ,"   //20

            + DataUtils.PICKUP_ADD_FNAME + " TEXT ,"        //21
            + DataUtils.PICKUP_ADD_LNAME + " TEXT ,"        //22
            + DataUtils.PICKUP_ADD_EMAIL + " TEXT ,"        //23
            + DataUtils.PICKUP_ADD_PHONE + " TEXT ,"        //24
            + DataUtils.PICKUP_ADD_lANDLINE + " TEXT ,"     //25
            + DataUtils.PICKUP_ADD_EXTENSION + " TEXT ,"    //26
            + DataUtils.PICKUP_ADD_ADDRESS1 + " TEXT ,"     //27
            + DataUtils.PICKUP_ADD_ADDRESS2 + " TEXT ,"     //28
            + DataUtils.PICKUP_ADD_COUNTRY + " TEXT ,"      //29
            + DataUtils.PICKUP_ADD_STATE + " TEXT ,"        //30
            + DataUtils.PICKUP_ADD_CITY + " TEXT ,"         //31
            + DataUtils.PICKUP_ADD_ZIP_CODE + " TEXT ,"     //32

            + DataUtils.DELIVERY_ADDRESS_FNAME + " TEXT ,"  //33
            + DataUtils.DELIVERY_ADDRESS_LNAME + " TEXT ,"  //34
            + DataUtils.DELIVERY_ADDRESS_EMAIL + " TEXT ,"  //35
            + DataUtils.DELIVERY_ADDRESS_PHONE + " TEXT ,"  //36
            + DataUtils.DELIVERY_ADDRESS_lANDLINE + " TEXT ,"//37
            + DataUtils.DELIVERY_ADDRESS_EXTENSION + " TEXT ,"//38
            + DataUtils.DELIVERY_ADDRESS_ADDRESS1 + " TEXT ,"//39
            + DataUtils.DELIVERY_ADDRESS_ADDRESS2 + " TEXT ,"//40
            + DataUtils.DELIVERY_ADDRESS_COUNTRY + " TEXT ,"//41
            + DataUtils.DELIVERY_ADDRESS_STATE + " TEXT ,"  //42
            + DataUtils.DELIVERY_ADDRESS_CITY + " TEXT ,"   //43
            + DataUtils.DELIVERY_ADDRESS_ZIP_CODE + " TEXT ,"//44

            + DataUtils.UPDATE_STATUS + " INTEGER DEFAULT -1,"//45
            + DataUtils.UPDATE_DATE + " TEXT "                //46
            + ")";



}

