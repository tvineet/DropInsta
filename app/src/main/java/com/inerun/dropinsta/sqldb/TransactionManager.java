package com.inerun.dropinsta.sqldb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by vineet on 12/1/2016.
 */

public class TransactionManager {

    protected Context mContext;
    protected SQLiteDatabase mSQLiteDatabase;
    protected SQLiteStatement mInsertStatement;
    private OpenHelper lOpenHelper;



    public TransactionManager(Context mContext) {
        this.mContext = mContext;
        closeDatabase();
        lOpenHelper = new OpenHelper(mContext);
        mSQLiteDatabase = lOpenHelper.getWritableDatabase();

    }

    // code to insert the Delivery Info


    public void insertData(String tablename, ContentValues values) {
        Log.i("insertDeliveryStatus", "start " + System.currentTimeMillis());
        mSQLiteDatabase = lOpenHelper.getWritableDatabase();


//        values.put(DataUtils.POD_RECEIVER_NAME, pod.getReceiverName() );

        Log.i("values", values.toString());

//        // Inserting Row
        mSQLiteDatabase.insert(tablename, null, values);

        //2nd argument is String containing nullColumnHack
        mSQLiteDatabase.close(); // Closing database connection
        Log.i("insert", "end " + System.currentTimeMillis());
    }

//    public void insertMultipleDeliveryStatus(ParcelListingData.ParcelData parcelData, ArrayList<ParcelStatus> parcelStatus) {
////        Log.i("insertDeliveryStatus", "start " + System.currentTimeMillis());
//        String query = " INSERT INTO " + DataUtils.TABLE_NAME_STATUS + "(" + DataUtils.STATUS_TYPE + "," + DataUtils.STATUS_COMMENT + "," + DataUtils.STATUS_TIME_STAMP + "," + DataUtils.PARCEL_BARCODE + ") VALUES ";
//        for (int i=0;i<parcelStatus.size();i++) {
//            ParcelStatus status = parcelStatus.get(i);
//            query+="('"+status.getStatus_type()+"','"+status.getStatus_comment()+"','"+status.getStatus_timestamp()+"','"+parcelData.getBarcode()+ "')";
//            if(i+1!=parcelStatus.size()) {
//                query+=",";
//            }
//
//        }
////        Log.i("INSERTMultipleDelivery", query);
//        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
//        //Log.i("StatusDao","Deleting Table"+  DataUtils.TABLE_NAME_STATUS);
////        Log.i("insertDeliveryStatus", "execSQL " + System.currentTimeMillis());
//        mSQLiteDatabase.execSQL(query);
////        Log.i("insertDeliveryStatus", "end " + System.currentTimeMillis());
//
//    }
//
//
//    // code to update the single POD STATUS
//    public int updatePODStatus(int id, String podNameOnServer, int status) {
//
//        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(DataUtils.POD_STATUS, status);
//        values.put(DataUtils.POD_UPDATED_TIME, DIHelper.getDateTime(AppConstant.DATEIME_FORMAT));
//
//        if (podNameOnServer != null && podNameOnServer.trim().length() > 0) {
//            values.put(DataUtils.POD_NAME_ON_SERVER, podNameOnServer);
//        }
//        // updating row
//        int value = mSQLiteDatabase.update(DataUtils.TABLE_NAME_POD, values, DataUtils.POD_ROW_ID + " = " + id, null);
//
//        mSQLiteDatabase.close();
//
//        return value;
//
//    }
//
    public void closeDatabase() {
        if ((mSQLiteDatabase != null) && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
    }
//
//    public ArrayList<ParcelStatus> getStatusById(Context context, String barcode) {
//
//        ArrayList<ParcelStatus> list = new ArrayList<>();
//
//        // Select All Query
//        String selectQuery = "SELECT  *  FROM " + DataUtils.TABLE_NAME_STATUS + " WHERE " + DataUtils.PARCEL_BARCODE + " = '" + barcode.toString() + "'";
//        //Log.i("getStatusById", selectQuery);
//
//        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
//        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//
//                ParcelStatus statusData = new ParcelStatus(cursor.getString(1), cursor.getString(2), cursor.getString(4), cursor.getString(3));
////                ParcelListingData.ParcelData v = p.new ParcelData(cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12));
//
//                list.add(statusData);
//            } while (cursor.moveToNext());
//        }
//        //Log.i("getStatusById",""+ list.size());
//
//
//        return list;
//    }
//
//
//    // Deleting Table Transaction
//    public void deleteStatusTable() {
//        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
//        //Log.i("StatusDao","Deleting Table"+  DataUtils.TABLE_NAME_STATUS);
//
//        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataUtils.TABLE_NAME_STATUS);
//
//        lOpenHelper.onCreate(mSQLiteDatabase);
//
//        mSQLiteDatabase.close();
//    }
}