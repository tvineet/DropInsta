package com.inerun.dropinsta.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.inerun.dropinsta.constant.AppConstant;
import com.inerun.dropinsta.data.ParcelStatus;
import com.inerun.dropinsta.data.PickParcelDetailData;
import com.inerun.dropinsta.data.PickupAddress;
import com.inerun.dropinsta.data.PickupParcelData;
import com.inerun.dropinsta.data.UpdatedPickupParcelData;
import com.inerun.dropinsta.helper.DIHelper;

import java.util.ArrayList;

/**
 * Created by vineet on 10/5/2017.
 */

public class PickupDao {
    protected Context mContext;
    protected SQLiteDatabase mSQLiteDatabase;
    protected SQLiteStatement mInsertStatement;
    private OpenHelper lOpenHelper;

    public PickupDao(Context mContext) {
        this.mContext = mContext;
        closeDatabase();
        lOpenHelper = new OpenHelper(mContext);
        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
    }

    public void insertMultipleParcels(String multiplevalues) {
        String parcelmasterquery = " INSERT INTO " + DataUtils.TABLE_NAME_PICKUP_DATA + getParcelColumns() + multiplevalues + " ;";
        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
        //Log.i("StatusDao","Deleting Table"+  DataUtils.TABLE_NAME_STATUS);
//        Log.i("insertDeliveryStatus", "execSQL " + System.currentTimeMillis());
        mSQLiteDatabase.execSQL(parcelmasterquery);


    }

    // code to get count of all Pending Parcels for pickup
    public int getPickupPendingParcelCount() {

        String selectQuery = "SELECT count(*) FROM " + DataUtils.TABLE_NAME_PICKUP_DATA + " WHERE " + DataUtils.PARCEL_PICKUP_STATUS + " != " + AppConstant.StatusKeys.PICKEDUP_STATUS;
//        String selectQuery = "SELECT count(*) FROM " + DataUtils.TABLE_NAME_PICKUP_DATA ;

        //Log.i("getPendingParcelCount", selectQuery);

        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        int count = 0;
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }

        return count;
    }

    // code to get all Pending Parcels for Pickup Listing
    public ArrayList<PickupParcelData> getPickupPendingParcelForListing() {
        ArrayList<PickupParcelData> list = new ArrayList<>();

        // Select All Query
//        String selectQuery = "SELECT  * FROM " + DataUtils.TABLE_NAME_DELIVERY;
//        String selectQuery = "SELECT * FROM " + DataUtils.TABLE_NAME_PICKUP_DATA + " WHERE " + DataUtils.PARCEL_DELIVERY_STATUS + " != " + AppConstant.StatusKeys.DELIVERED_STATUS;
        String selectQuery = "SELECT * FROM " + DataUtils.TABLE_NAME_PICKUP_DATA + " WHERE " + DataUtils.PARCEL_PICKUP_STATUS + " != " + AppConstant.StatusKeys.PICKEDUP_STATUS;
        //Log.i("getPendingParcelForList", selectQuery);

        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Log.i("Parcel_ID", "" + cursor.getInt(0));

                PickParcelDetailData pickParcelDetailData = new PickParcelDetailData(cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19), cursor.getString(20));
                PickupAddress pickupAddress = new PickupAddress(cursor.getString(21), cursor.getString(22), cursor.getString(23), cursor.getString(24), cursor.getString(25), cursor.getString(26), cursor.getString(27), cursor.getString(28), cursor.getString(29), cursor.getString(30), cursor.getString(31), cursor.getString(32));
                PickupAddress pickupDeliveryAddress = new PickupAddress(cursor.getString(33), cursor.getString(34), cursor.getString(35), cursor.getString(36), cursor.getString(37), cursor.getString(38), cursor.getString(39), cursor.getString(40), cursor.getString(41), cursor.getString(42), cursor.getString(43), cursor.getString(43));

                PickupParcelData pickupParcelData = new PickupParcelData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), pickupAddress, pickupDeliveryAddress, pickParcelDetailData);

                list.add(pickupParcelData);
            } while (cursor.moveToNext());
        }

        return list;
    }

    // code to update the Pickup Comment
    public int updatePickupComment(int id, int status, String pickup_comment) {

        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
        //Log.i("id", "" + id);
        //Log.i("delivery_comment", "" + delivery_comment);
        ContentValues values = new ContentValues();
        values.put(DataUtils.PARCEL_PICKUP_STATUS, status);
        values.put(DataUtils.PARCEL_PICKUP_COMMENT, pickup_comment);
        values.put(DataUtils.UPDATE_DATE, DIHelper.getDateTime(AppConstant.DATEIME_FORMAT));
        values.put(DataUtils.UPDATE_STATUS, 1);

        // updating row
        int value = mSQLiteDatabase.update(DataUtils.TABLE_NAME_PICKUP_DATA, values, DataUtils.COLUMN_ID + " = " + id, null);
        //Log.i("values", "" + values.toString());

        mSQLiteDatabase.close();

        return value;

    }

    // code to get all Delivery Info for SYNC
    public ArrayList<UpdatedPickupParcelData> getPickupInfoForUpdateAndSYNC(Context context) {
        ArrayList<UpdatedPickupParcelData> list = new ArrayList<>();

        // Select All Query
//        String selectQuery = "SELECT P.*,POD.pod_name_on_server,T.receiver_name, T.national_id FROM PARCEL AS P LEFT JOIN PROOF_OF_DELIVERY AS POD ON P.pod_id=POD.id LEFT JOIN TRANSCTABLE AS T ON T.id==P.trans_row_id WHERE update_status = 1";
        String selectQuery = "SELECT  * FROM " + DataUtils.TABLE_NAME_PICKUP_DATA +" WHERE "+ DataUtils.UPDATE_STATUS +" = 1";
        //Log.i("DeliveryForUpdateSYNC", selectQuery);

        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String barcode = cursor.getString(1);

                ArrayList<ParcelStatus> statusDataList = DIDbHelper.getStatusbyParcelId(context, barcode);

//                UpdatedParcelData updatedParcelData = new UpdatedParcelData(barcode, Integer.parseInt(cursor.getString(5)), cursor.getString(6), Integer.parseInt(cursor.getString(8)), cursor.getString(26), cursor.getString(30), cursor.getString(31),statusDataList);
                UpdatedPickupParcelData updatedParcelData = new UpdatedPickupParcelData(barcode, cursor.getInt(9), cursor.getString(20), cursor.getString(46), statusDataList);

                list.add(updatedParcelData);
            } while (cursor.moveToNext());
        }
        mSQLiteDatabase.close();

        return list;
    }

    public int receivedParcel(int column_id, int status, String updateDate) {

        mSQLiteDatabase = lOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataUtils.PARCEL_PICKUP_STATUS, status);
        values.put(DataUtils.UPDATE_DATE, updateDate);
        values.put(DataUtils.UPDATE_STATUS, 1);

        // updating row
        int value = mSQLiteDatabase.update(DataUtils.TABLE_NAME_PICKUP_DATA, values, DataUtils.COLUMN_ID + " = " + column_id, null);
        //Log.i("values", "" + values.toString());

        mSQLiteDatabase.close();

        return value;

    }


    private String getParcelColumns() {

        String columns = "(" + DataUtils.PARCEL_BARCODE
                + "," + DataUtils.CUSTOMER_ID + ","
                + DataUtils.USER_FNAME + ","
                + DataUtils.USER_LNAME + ","
                + DataUtils.USER_EMAIL + ","
                + DataUtils.USER_PHONE + ","
                + DataUtils.USER_lANDLINE + ","
                + DataUtils.USER_EXTENSION + ","
                + DataUtils.PARCEL_PICKUP_STATUS + ","

                + DataUtils.PARCEL_LENGTH + ","
                + DataUtils.PARCEL_WIDTH + ","
                + DataUtils.PARCEL_HEIGHT + ","
                + DataUtils.PARCEL_VOLUME_WEIGHT + ","
                + DataUtils.PARCEL_ACTUAL_WEIGHT + ","
                + DataUtils.PARCEL_PRICE + ","
                + DataUtils.PARCEL_DESCRIPTION + ","
                + DataUtils.PARCEL_SPECIAL_INS + ","
                + DataUtils.PARCEL_ASSIGN_DATE + ","
                + DataUtils.PARCEL_CREATED_ON + ","
                + DataUtils.PARCEL_PICKUP_COMMENT + ","


                + DataUtils.PICKUP_ADD_FNAME + ","
                + DataUtils.PICKUP_ADD_LNAME + ","
                + DataUtils.PICKUP_ADD_EMAIL + ","
                + DataUtils.PICKUP_ADD_PHONE + ","
                + DataUtils.PICKUP_ADD_lANDLINE + ","
                + DataUtils.PICKUP_ADD_EXTENSION + ","
                + DataUtils.PICKUP_ADD_ADDRESS1 + ","
                + DataUtils.PICKUP_ADD_ADDRESS2 + ","
                + DataUtils.PICKUP_ADD_COUNTRY + ","
                + DataUtils.PICKUP_ADD_STATE + ","
                + DataUtils.PICKUP_ADD_CITY + ","
                + DataUtils.PICKUP_ADD_ZIP_CODE + ","

                + DataUtils.DELIVERY_ADDRESS_FNAME + ","
                + DataUtils.DELIVERY_ADDRESS_LNAME + ","
                + DataUtils.DELIVERY_ADDRESS_EMAIL + ","
                + DataUtils.DELIVERY_ADDRESS_PHONE + ","
                + DataUtils.DELIVERY_ADDRESS_lANDLINE + ","
                + DataUtils.DELIVERY_ADDRESS_EXTENSION + ","
                + DataUtils.DELIVERY_ADDRESS_ADDRESS1 + ","
                + DataUtils.DELIVERY_ADDRESS_ADDRESS2 + ","
                + DataUtils.DELIVERY_ADDRESS_COUNTRY + ","
                + DataUtils.DELIVERY_ADDRESS_STATE + ","
                + DataUtils.DELIVERY_ADDRESS_CITY + ","
                + DataUtils.DELIVERY_ADDRESS_ZIP_CODE


                + ") VALUES ";
        return columns;
    }


    public void closeDatabase() {
        if ((mSQLiteDatabase != null) && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
    }
}
