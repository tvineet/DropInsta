package com.inerun.dropinsta.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.BaseActivity;
import com.inerun.dropinsta.constant.AppConstant;
import com.inerun.dropinsta.data.POD;
import com.inerun.dropinsta.data.ParcelListingData;
import com.inerun.dropinsta.data.ParcelStatus;
import com.inerun.dropinsta.data.PickupParcelData;
import com.inerun.dropinsta.data.TransactionData;
import com.inerun.dropinsta.data.UpdatedParcelData;
import com.inerun.dropinsta.data.UpdatedPickupParcelData;
import com.inerun.dropinsta.helper.DIHelper;

import java.util.ArrayList;

/**
 * Created by vineet on 7/29/2016.
 */
public class DIDbHelper {


    public static void insertDeliveryInfoListIntoDb(Context context, ArrayList<ParcelListingData.ParcelData> list) {
        for (ParcelListingData.ParcelData parcelData : list) {
            insertDeliveryData(parcelData, context);

        }
    }
//
//    public static void insertDeliveryAndStatusInfoListIntoDb(Context context, ArrayList<ParcelListingData.ParcelData> list) {
//
//        for (ParcelListingData.ParcelData parcelData : list) {
//            insertDeliveryData(parcelData, context);
//            Log.i("insert","start "+System.currentTimeMillis());
////            for (ParcelStatus parcelStatus : parcelData.getStatus()) {
////                StatusDao statusDao = new StatusDao(context);
////                statusDao.insertDeliveryStatus(parcelData, parcelStatus);
////
////
////            }
//
//            StatusDao statusDao = new StatusDao(context);
//            statusDao.insertMultipleDeliveryStatus(parcelData, parcelData.getStatus());
//
//
////            Log.i("insert","end "+System.currentTimeMillis());
//
//        }
//    }

    public static void insertDeliveryAndStatusInfoListIntoDb(Context context, ArrayList<ParcelListingData.ParcelData> list) {
        String parcelmastervalues = "";
        Log.i("insert", "start " + System.currentTimeMillis());
        for (int i = 0; i < list.size(); i++) {
            ParcelListingData.ParcelData parcelData = list.get(i);
            parcelmastervalues += appendParcelValue(parcelData);
            if (i + 1 != list.size()) {
                parcelmastervalues += ",";
            }
//            insertDeliveryData(parcelData, context);

//            for (ParcelStatus parcelStatus : parcelData.getStatus()) {
//                StatusDao statusDao = new StatusDao(context);
//                statusDao.insertDeliveryStatus(parcelData, parcelStatus);
//
//
//            }

            StatusDao statusDao = new StatusDao(context);
            statusDao.insertMultipleDeliveryStatus(parcelData, parcelData.getStatus());


//

        }
        DeliveryDao deliveryDao = new DeliveryDao(context);
        deliveryDao.insertMultipleParcels(parcelmastervalues);
    }


    public static String appendParcelValue(ParcelListingData.ParcelData parcelData) {

        String query = "('" + parcelData.getBarcode() + "','" + parcelData.getName() + "','" + parcelData.getWeight()
                + "','" + parcelData.getSpecialinstructions()
                + "','" + parcelData.getStatus().get(0).getStatus_type()
                + "','" + parcelData.getDeliverycomments()
                + "','" + parcelData.getPayment_type()
                + "','" + parcelData.getPayment_status()
                + "','" + parcelData.getParcel_type()
                + "','" + parcelData.getAmount()
                + "','" + parcelData.getCurrency()
                + "','" + parcelData.getDate()
                + "','" + parcelData.getSource_address1()
                + "','" + parcelData.getSource_address2()
                + "','" + parcelData.getSource_city()
                + "','" + parcelData.getSource_state()
                + "','" + parcelData.getSource_pin()
                + "','" + parcelData.getSource_phone()
                + "','" + parcelData.getDelivery_address1()
                + "','" + parcelData.getDelivery_address2()
                + "','" + parcelData.getDelivery_city()
                + "','" + parcelData.getDelivery_state()
                + "','" + parcelData.getDelivery_pin()
                + "','" + parcelData.getDelivery_phone()
                + "','" + parcelData.getCustid()

                + "')";
        return query;
    }

    public static void insertDeliveryData(ParcelListingData.ParcelData parcelData, Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);
        deliveryDao.insertDeliveryInfo(parcelData);

    }

    public static int getColumnIdFromBarcode(String barcode, Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);
        return deliveryDao.getColumnIdFromBarcode(barcode);
    }

    public static ArrayList<ParcelListingData.ParcelData> getDeliveryInfoForListing(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        return deliveryDao.getDeliveryInfoForListing();
    }

    public static ArrayList<ParcelListingData.ParcelData> getDeliveredParcelInfoForListing(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        return deliveryDao.getDeliveredParcelInfoForListing();
    }

    public static ArrayList<ParcelListingData.ParcelData> getPendingParcelInfoForListing(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        return deliveryDao.getPendingParcelInfoForListing();
    }

    public static ArrayList<ParcelListingData.ParcelData> getPendingParcelsList(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        return deliveryDao.getPendingParcelForListing();
    }

    public static ArrayList<ParcelListingData.ParcelData> getDeliveryInfoForListing2(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        ArrayList<ParcelListingData.ParcelData> dataArrayListDelivered = deliveryDao.getDeliveredParcelForListing();
        ArrayList<ParcelListingData.ParcelData> dataArrayListPending = deliveryDao.getPendingParcelForListing();

        ArrayList<ParcelListingData.ParcelData> dataArrayListAll = new ArrayList<>();
        dataArrayListAll.addAll(dataArrayListPending);
        dataArrayListAll.addAll(dataArrayListDelivered);


        return dataArrayListAll;
    }

    public static ParcelListingData getParcelListData(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);
        int pending_count = deliveryDao.getPendingParcelCount();
        int delivered_count = deliveryDao.getDeliveredParcelCount();

        PickupDao pickupDao = new PickupDao(context);
        int pickup_pending_count = pickupDao.getPickupPendingParcelCount();

//        ParcelListingData parcelListingData = new ParcelListingData(delivered_count, pending_count, getDeliveryInfoForListing2(context));
        ParcelListingData parcelListingData = new ParcelListingData(delivered_count, pending_count, getDeliveryInfoForListing2(context), getPickupInfoForListing(context), pickup_pending_count);


        return parcelListingData;
    }



    public static ArrayList<UpdatedParcelData> getDeliveryInfoForUpdateAndSYNC(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        return deliveryDao.getDeliveryInfoForUpdateAndSYNC(context);
    }

    public static ArrayList<TransactionData> getInvoices(Context context) {
        TranscDao transcDao = new TranscDao(context);

        return transcDao.getInvoices();
    }

    public static void updateParcelComment(Context context, int id, String delivery_comment) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        deliveryDao.updateDeliveryComment(id, delivery_comment);
    }

    public static void updateDeliveryInfoDelivered(Context context, int parcel_id, int pod_id) {
        DeliveryDao deliveryDao = new DeliveryDao(context);

        deliveryDao.updateDeliveryInfoDelivered(parcel_id, pod_id);
    }

    public static void deleteDeliveryTable(Context context) {
        DeliveryDao deliveryDao = new DeliveryDao(context);
        deliveryDao.deleteDeliveryTable();
    }

    public static void deleteTables(Context context) {
        //        DeliveryDao deliveryDao = new DeliveryDao(context);
//        deliveryDao.deleteDeliveryTable();
//        TranscDao transcDao = new TranscDao(context);
//        transcDao.deleteTransactionTable();
//        StatusDao statusDao = new StatusDao(context);
//        statusDao.deleteStatusTable();

//       String query= "DROP TABLE "+DataUtils.TABLE_NAME_PARCEL+","+DataUtils.TABLE_NAME_TRANSACTION+","+DataUtils.TABLE_NAME_STATUS+";";


        OpenHelper lOpenHelper = new OpenHelper(context);
        SQLiteDatabase mSQLiteDatabase = lOpenHelper.getWritableDatabase();

        //Log.i("StatusDao","Deleting Table"+  DataUtils.TABLE_NAME_STATUS);
//        Log.i("insertDeliveryStatus", "execSQL " + System.currentTimeMillis());
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataUtils.TABLE_NAME_PARCEL);
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataUtils.TABLE_NAME_STATUS);
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataUtils.TABLE_NAME_TRANSACTION);
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataUtils.TABLE_NAME_PICKUP_DATA);

        //recreate Tables
        mSQLiteDatabase.execSQL(OpenHelper.CREATE_PARCEL_TABLE);
        mSQLiteDatabase.execSQL(OpenHelper.CREATE_STATUS_TABLE);
        mSQLiteDatabase.execSQL(OpenHelper.CREATE_TRANSACTION_TABLE);
        mSQLiteDatabase.execSQL(OpenHelper.CREATE_PICKUP_DATA_TABLE);


        mSQLiteDatabase.close();
        lOpenHelper = new OpenHelper(context);
        mSQLiteDatabase = lOpenHelper.getWritableDatabase();
        mSQLiteDatabase.close();

    }


    /*=================================POD =================================================*/

    // Get Old PODs for delete
    public static ArrayList<POD> getOldPODsListByDate(Context context) {
        PODDao podDao = new PODDao(context);

        return podDao.getOldPODsListByDate();
    }

    // Get Old PODs for delete
    public static void deleteOldPodsData(Context context, ArrayList<POD> podArrayList) {
        PODDao podDao = new PODDao(context);

        podDao.deleteOldPodsData(podArrayList);
    }

    // Insert POD DATA
    public static void insertPODInfo(POD pod, int parcel_id, Context context) {
        PODDao podDao = new PODDao(context);
        podDao.insertPODInfo(pod);

        int pod_id = podDao.getPODIdByName(pod.getName());

        updateDeliveryInfoDelivered(context, parcel_id, pod_id);

    }

    public static int insertNGetPODId(POD pod, Context context) {
        PODDao podDao = new PODDao(context);
        podDao.insertPODInfo(pod);

        int pod_id = podDao.getPODIdByName(pod.getName());
        return pod_id;


    }

    // Get All Pending PODs
    public static ArrayList<POD> getPendingPODs(Context context) {
        PODDao podDao = new PODDao(context);

        return podDao.getAllPendingPODs();
    }

    // Update POD Status And Receiver Name
    public static void updatePODStatus(Context context, int id, String podNameOnServer, int status) {
        PODDao podDao = new PODDao(context);

        podDao.updatePODStatus(id, podNameOnServer, status);
    }

    // Check for Pending PODs
    public static boolean isPendingPods(Context context) {
        PODDao podDao = new PODDao(context);

        return podDao.isPendingPODs();
    }

    // Check for Pending PODs
    public static POD getPODById(Context context, int id) {
        PODDao podDao = new PODDao(context);

        return podDao.getPODById(id);
    }

    public static void updateDeliveryStatus(Context ctx, ArrayList<ParcelListingData.ParcelData> arrayList, ParcelStatus parcelStatus) {
        for (ParcelListingData.ParcelData parcelData : arrayList) {
            StatusDao statusDao = new StatusDao(ctx);
            statusDao.insertDeliveryStatus(parcelData, parcelStatus);
            ParcelDao parcelDao = new ParcelDao(ctx);
            parcelDao.updateDeliveryComment(parcelData.getColumn_id(), ParcelListingData.ParcelData.ATTEMPTED, parcelStatus.getStatus_comment());
        }
    }

    public static void updateReturnParcelStatus(Context ctx, ArrayList<ParcelListingData.ParcelData> arrayList) {
        for (ParcelListingData.ParcelData parcelData : arrayList) {
//            StatusDao statusDao = new StatusDao(ctx);
//            statusDao.insertDeliveryStatus(parcelData, parcelStatus);
            ParcelDao parcelDao = new ParcelDao(ctx);
            parcelDao.updateReturnStatus(parcelData.getColumn_id(), ParcelListingData.ParcelData.RETURN);
        }
    }

    public static void deliverParcelandUpdateTransaction(Context ctx, ArrayList<ParcelListingData.ParcelData> arrayList, ParcelStatus parcelStatus, boolean iscard, String transcid, String receivername, String totalamt, String currency, POD pod, String nationalId) {


        TranscDao transcDao = new TranscDao(ctx);
        long transcrowid = -1;
        if (iscard) {
            transcrowid = transcDao.insertTransaction(TranscDao.TRANSTYPE_CARD, transcid, receivername, totalamt, currency, nationalId);
        } else {
            transcrowid = transcDao.insertTransaction(TranscDao.TRANSTYPE_CASH, "", receivername, totalamt, currency, nationalId);
        }
        PODDao podDao = new PODDao(ctx);

        int podrowid = insertNGetPODId(pod, ctx);


        if (transcrowid != -1 && podrowid != -1) {
            for (ParcelListingData.ParcelData parcelData : arrayList) {
                ParcelDao parcelDao = new ParcelDao(ctx);
                parcelDao.giveDelivery(parcelData.getColumn_id(), ParcelListingData.ParcelData.DELIVERED, (int) transcrowid, DIHelper.getDateTime(AppConstant.DATEIME_FORMAT));
                StatusDao statusDao = new StatusDao(ctx);
                statusDao.insertDeliveryStatus(parcelData, parcelStatus);

                updateDeliveryInfoDelivered(ctx, parcelData.getColumn_id(), podrowid);

//                DIDbHelper.insertPODInfo(pod, parcelData.getColumn_id(), ctx);

            }

        } else {
            ((BaseActivity) ctx).showSnackbar(R.string.error_transcid_error);
        }

//
    }

    public static String getPaymentTotal(Context ctx) {
        TranscDao transcDao = new TranscDao(ctx);
        String payment = "" + transcDao.getTotalPayment();
        return payment;
    }

    public static String getTransactionDataById(Context ctx, int transrowid) {
        TranscDao transcDao = new TranscDao(ctx);
        String payment = "" + transcDao.getTotalPayment();
        return payment;
    }

    public static ArrayList<ParcelStatus> getStatusbyParcelId(Context context, String barcode) {
        StatusDao statusDao = new StatusDao(context);

        return statusDao.getStatusById(context, barcode);
    }

    public static ArrayList<ParcelListingData.ParcelData> getCashPayments(Context ctx) {
        ArrayList<ParcelListingData.ParcelData> list = new ArrayList<>();
        TranscDao transcDao = new TranscDao(ctx);
        list.addAll(transcDao.getPayments(false));
        return list;
    }

    public static ArrayList<ParcelListingData.ParcelData> getCardPayments(Context ctx) {
        ArrayList<ParcelListingData.ParcelData> list = new ArrayList<>();
        TranscDao transcDao = new TranscDao(ctx);
        list.addAll(transcDao.getPayments(true));
        return list;
    }

    public static String getTotalCardPayment(Context ctx) {
        String payment = "";
        TranscDao transcDao = new TranscDao(ctx);
        payment = transcDao.getPaymentsTotal(true);
        return payment;
    }

    public static String getTotalCashPayment(Context ctx) {
        String payment = "";
        TranscDao transcDao = new TranscDao(ctx);
        payment = transcDao.getPaymentsTotal(false);
        return payment;
    }

    public static void insertTransactionInfoToDatabase(Context ctx, ArrayList<TransactionData> transdata) {
        TranscDao transcDao = new TranscDao(ctx);
        for (TransactionData transactionData : transdata) {
            long id = transcDao.insertTransaction(Integer.parseInt(transactionData.getTranstype()), transactionData.getTranscid(), transactionData.getCollectedby(), transactionData.getTotalamount(), transactionData.getCurrency(), transactionData.getNationalId());
//            Log.i("insertTransaction", "id " + id);
//            String barcodearray[] = transactionData.getBarcode().split(",");
//            Log.i("insertTransacInfo2Db", "barcodearray" + barcodearray.toString());
//            Log.i("insertTransacInfo2Db", "start " + System.currentTimeMillis());
//            for (int i = 0; i < barcodearray.length; i++) {
//                String barcode = barcodearray[i];
//                Log.i("barcode", "" + barcode);
//                ParcelDao parcelDao = new ParcelDao(ctx);
//                parcelDao.insertTransactionInfoForBarcode(barcode, (int) id, transactionData.getTransTimeStamp());
//
//
//            }

            ParcelDao parcelDao = new ParcelDao(ctx);
            parcelDao.insertTransactionInfoForMultipleBarcodes(transactionData.getBarcode(), (int) id, transactionData.getTransTimeStamp());

        }
    }


//    public static void insertCategoryListIntoDb(Context context, List<CategoryData> cat_list) {
//        for (CategoryData categoryData : cat_list) {
//
//            insertCategoryData(categoryData, context);
//        }
//
//    }
//
//    public static void insertCategoryData(CategoryData categoryData, Context context) {
//        CategoryDao categoryDao = new CategoryDao(context);
//        categoryDao.addCategoryInfo(categoryData);
//
//    }
//
//    public static ArrayList<CategoryData> getCategoryData(Context context){
//        CategoryDao categoryDao = new CategoryDao(context);
//
//        return categoryDao.getAllCategory();
//    }
//
//    public static void deleteCategoryAndSubcategoryTable(Context context){
//        CategoryDao categoryDao = new CategoryDao(context);
//        categoryDao.deleteCategoryAndSubcategoryTable();
//
//    }
//
//    public static void insertWishlistItem(MyWishlistData.WishlistData wishlistData, Context context) {
//        WishlistDao wishlistDao = new WishlistDao(context);
//        wishlistDao.addWishlistItem(wishlistData);
//
//    }
//
//    public static ArrayList<MyWishlistData.WishlistData> getWishlistData(Context context){
//        WishlistDao wishlistDao = new WishlistDao(context);
//
//        return wishlistDao.getAllWishlist();
//    }
//
//    public static ArrayList<Integer> getProductIdListFromWishlist(Context context){
//        WishlistDao wishlistDao = new WishlistDao(context);
//
//        return wishlistDao.getProductIdListFromWishlist();
//    }
//
//    public static ArrayList<String> getProductIdListStringFromWishlist(Context context){
//        WishlistDao wishlistDao = new WishlistDao(context);
//
//        return wishlistDao.getProductIdListStringFromWishlist();
//    }
//
//    public static void updateWishlistById(Context context, String productId, MyWishlistData.WishlistData wishlistData ){
//        WishlistDao wishlistDao = new WishlistDao(context);
//
//        wishlistDao.updateWishlistById(productId,wishlistData);
//    }
//
//    public static void updateWishlistQTYById(Context context, List<MyWishlistData.WishlistData> wishlistData ){
//        WishlistDao wishlistDao = new WishlistDao(context);
//
//        wishlistDao.updateWishlistQtyById(wishlistData);
//    }
//
//    public static void deleteWishlistItemById(Context context, String productId){
//        WishlistDao wishlistDao = new WishlistDao(context);
//        wishlistDao.deleteWhishlistItemById(productId);
//
//    }
//
//
//    public static void deleteWishlistTable(Context context){
//        WishlistDao wishlistDao = new WishlistDao(context);
//        wishlistDao.deleteWishlistTable();
//
//    }


    public static void ensureDatabaseIsCorrect(Context context) {
        Log.i("Db", "ensureDatabaseIsCorrect");
        validateParcelTable(context);
        validatePODTable(context);
        validateStatusTable(context);
        validateTransactionTable(context);
        validatePickupParcelTable(context);
        Log.i("Db", "ensureDatabaseIsCorrect Done");

    }


    public static void validateParcelTable(Context context) {

        String tablename = DataUtils.TABLE_NAME_PARCEL;
        String params[] = new String[]{
                DataUtils.COLUMN_ID,
                DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY",
                DataUtils.PARCEL_BARCODE,
                DataUtils.PARCEL_BARCODE + " TEXT ",
                DataUtils.CONSIGNEE_NAME,
                DataUtils.CONSIGNEE_NAME + " TEXT ",
                DataUtils.PARCEL_WEIGHT,
                DataUtils.PARCEL_WEIGHT + " INTEGER DEFAULT -1",
                DataUtils.PARCEL_SPECIAL_INSTRUCTION,
                DataUtils.PARCEL_SPECIAL_INSTRUCTION + " TEXT ",
                DataUtils.PARCEL_DELIVERY_STATUS,
                DataUtils.PARCEL_DELIVERY_STATUS + " INTEGER DEFAULT -1",
                DataUtils.PARCEL_DELIVERY_COMMENT,
                DataUtils.PARCEL_DELIVERY_COMMENT + " TEXT ",
                DataUtils.PARCEL_PAYMENT_TYPE,
                DataUtils.PARCEL_PAYMENT_TYPE + " INTEGER DEFAULT -1",
                DataUtils.PARCEL_PAYMENT_STATUS,
                DataUtils.PARCEL_PAYMENT_STATUS + " INTEGER DEFAULT -1",
                DataUtils.PARCEL_TYPE,
                DataUtils.PARCEL_TYPE + " INTEGER DEFAULT -1",
                DataUtils.PARCEL_AMOUNT,
                DataUtils.PARCEL_AMOUNT + " INTEGER DEFAULT -1",
                DataUtils.PARCEL_CURRENCY,
                DataUtils.PARCEL_CURRENCY + " TEXT ",
                DataUtils.PARCEL_DATE,
                DataUtils.PARCEL_DATE + " TEXT ",


                DataUtils.SOURCE_ADDRESS1,
                DataUtils.SOURCE_ADDRESS1 + " TEXT",
                DataUtils.SOURCE_ADDRESS2,
                DataUtils.SOURCE_ADDRESS2 + " TEXT",
                DataUtils.SOURCE_CITY,
                DataUtils.SOURCE_CITY + " TEXT",
                DataUtils.SOURCE_STATE,
                DataUtils.SOURCE_STATE + " TEXT",
                DataUtils.SOURCE_PIN,
                DataUtils.SOURCE_PIN + " TEXT ",
                DataUtils.SOURCE_PHONE,
                DataUtils.SOURCE_PHONE + " TEXT",

                DataUtils.DELIVERY_ADDRESS1,
                DataUtils.DELIVERY_ADDRESS1 + " TEXT",
                DataUtils.DELIVERY_ADDRESS2,
                DataUtils.DELIVERY_ADDRESS2 + " TEXT",
                DataUtils.DELIVERY_CITY,
                DataUtils.DELIVERY_CITY + " TEXT",
                DataUtils.DELIVERY_STATE,
                DataUtils.DELIVERY_STATE + " TEXT",
                DataUtils.DELIVERY_PIN,
                DataUtils.DELIVERY_PIN + " TEXT",
                DataUtils.DELIVERY_PHONE,
                DataUtils.DELIVERY_PHONE + " TEXT",

                DataUtils.UPDATE_STATUS,
                DataUtils.UPDATE_STATUS + " INTEGER DEFAULT -1",
                DataUtils.UPDATE_DATE,
                DataUtils.UPDATE_DATE + " TEXT ",
                DataUtils.CONSIGNEE_ID,
                DataUtils.CONSIGNEE_ID + " TEXT ",
                DataUtils.TRANSCROWID,
                DataUtils.TRANSCROWID + " INTEGER DEFAULT -1 ",
                DataUtils.POD_ROW_ID,
                DataUtils.POD_ROW_ID + " INTEGER DEFAULT -1 ",

        };


//        DataUtils.KEY_ID
        OpenHelper.validateDatabase(context, tablename, params);
    }

    public static void validatePODTable(Context context) {

        String tablename = DataUtils.TABLE_NAME_POD;
        String params[] = new String[]{DataUtils.COLUMN_ID,
                DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY",
                DataUtils.POD_NAME,
                DataUtils.POD_NAME + " TEXT ",
                DataUtils.POD_CREATED_TIME,
                DataUtils.POD_CREATED_TIME + " TEXT ",
                DataUtils.POD_UPDATED_TIME,
                DataUtils.POD_UPDATED_TIME + " TEXT ",
                DataUtils.POD_NAME_ON_SERVER,
                DataUtils.POD_NAME_ON_SERVER + " TEXT ",
                DataUtils.POD_STATUS,
                DataUtils.POD_STATUS + " INTEGER DEFAULT -1 ",
                DataUtils.PARCEL_BARCODE,
                DataUtils.PARCEL_BARCODE + " TEXT "};
        OpenHelper.validateDatabase(context, tablename, params);

    }

    public static void validateStatusTable(Context context) {

        String tablename = DataUtils.TABLE_NAME_STATUS;
        String params[] = new String[]{DataUtils.COLUMN_ID,
                DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY",
                DataUtils.STATUS_TYPE,
                DataUtils.STATUS_TYPE + " TEXT ",
                DataUtils.STATUS_COMMENT,
                DataUtils.STATUS_COMMENT + " TEXT ",
                DataUtils.STATUS_TIME_STAMP,
                DataUtils.STATUS_TIME_STAMP + " TEXT ",
                DataUtils.PARCEL_BARCODE,
                DataUtils.PARCEL_BARCODE + " TEXT ",
                DataUtils.UPDATE_STATUS,
                DataUtils.UPDATE_STATUS + " INTEGER DEFAULT -1"
        };
        OpenHelper.validateDatabase(context, tablename, params);


    }

    public static void validateTransactionTable(Context context) {

        String tablename = DataUtils.TABLE_NAME_TRANSACTION;
        String params[] = new String[]{DataUtils.COLUMN_ID,
                DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY",
                DataUtils.TRANS_ID,
                DataUtils.TRANS_ID + " TEXT ",
                DataUtils.TRANS_TYPE,
                DataUtils.TRANS_TYPE + " TEXT ",
                DataUtils.TRANS_TIME_STAMP,
                DataUtils.TRANS_TIME_STAMP + " TEXT ",
                DataUtils.TRANS_RECEIVER_NAME,
                DataUtils.TRANS_RECEIVER_NAME + " TEXT ",
                DataUtils.TRANS_TOTAL_AMOUNT,
                DataUtils.TRANS_TOTAL_AMOUNT + " TEXT ",
                DataUtils.TRANS_CURRENCY,
                DataUtils.TRANS_CURRENCY + " TEXT ",
                DataUtils.TRANS_NATIONAL_ID,
                DataUtils.TRANS_NATIONAL_ID + "TEXT "};


        OpenHelper.validateDatabase(context, tablename, params);

    }

    public static void validatePickupParcelTable(Context context) {

        String tablename = DataUtils.TABLE_NAME_PICKUP_DATA;
        String params[] = new String[]{
                DataUtils.COLUMN_ID,
                DataUtils.COLUMN_ID + " INTEGER PRIMARY KEY",
                DataUtils.PARCEL_BARCODE,
                DataUtils.PARCEL_BARCODE + " TEXT ",
                DataUtils.USER_FNAME,
                DataUtils.USER_FNAME + " TEXT ",
                DataUtils.USER_LNAME,
                DataUtils.USER_LNAME + " TEXT",
                DataUtils.USER_EMAIL,
                DataUtils.USER_EMAIL + " TEXT ",
                DataUtils.USER_PHONE,
                DataUtils.USER_PHONE + " TEXT",
                DataUtils.USER_lANDLINE,
                DataUtils.USER_lANDLINE + " TEXT ",
                DataUtils.USER_EXTENSION,
                DataUtils.USER_EXTENSION + " TEXT",
                DataUtils.PARCEL_PICKUP_STATUS,
                DataUtils.PARCEL_PICKUP_STATUS + " INTEGER DEFAULT -1",

                DataUtils.PARCEL_LENGTH,
                DataUtils.PARCEL_LENGTH + " REAL ",
                DataUtils.PARCEL_WIDTH,
                DataUtils.PARCEL_WIDTH + " REAL ",
                DataUtils.PARCEL_HEIGHT,
                DataUtils.PARCEL_HEIGHT + " REAL ",
                DataUtils.PARCEL_VOLUME_WEIGHT,
                DataUtils.PARCEL_VOLUME_WEIGHT + " REAL ",
                DataUtils.PARCEL_ACTUAL_WEIGHT,
                DataUtils.PARCEL_ACTUAL_WEIGHT + " REAL ",
                DataUtils.PARCEL_PRICE,
                DataUtils.PARCEL_PRICE + " REAL ",
                DataUtils.PARCEL_SPECIAL_INS,
                DataUtils.PARCEL_SPECIAL_INS + " TEXT ",
                DataUtils.PARCEL_DESCRIPTION,
                DataUtils.PARCEL_DESCRIPTION + " TEXT ",
                DataUtils.PARCEL_ASSIGN_DATE,
                DataUtils.PARCEL_ASSIGN_DATE + " TEXT ",
                DataUtils.PARCEL_CREATED_ON,
                DataUtils.PARCEL_CREATED_ON + " TEXT ",
                DataUtils.PARCEL_PICKUP_COMMENT,
                DataUtils.PARCEL_PICKUP_COMMENT + " TEXT ",

                DataUtils.PICKUP_ADD_FNAME,
                DataUtils.PICKUP_ADD_FNAME + " TEXT ",
                DataUtils.PICKUP_ADD_LNAME,
                DataUtils.PICKUP_ADD_LNAME + " TEXT ",
                DataUtils.PICKUP_ADD_EMAIL,
                DataUtils.PICKUP_ADD_EMAIL + " TEXT ",
                DataUtils.PICKUP_ADD_PHONE,
                DataUtils.PICKUP_ADD_PHONE + " TEXT ",
                DataUtils.PICKUP_ADD_lANDLINE,
                DataUtils.PICKUP_ADD_lANDLINE + " TEXT ",
                DataUtils.PICKUP_ADD_EXTENSION,
                DataUtils.PICKUP_ADD_EXTENSION + " TEXT ",
                DataUtils.PICKUP_ADD_ADDRESS1,
                DataUtils.PICKUP_ADD_ADDRESS1 + " TEXT ",
                DataUtils.PICKUP_ADD_ADDRESS2,
                DataUtils.PICKUP_ADD_ADDRESS2 + " TEXT ",
                DataUtils.PICKUP_ADD_COUNTRY,
                DataUtils.PICKUP_ADD_COUNTRY + " TEXT ",
                DataUtils.PICKUP_ADD_STATE,
                DataUtils.PICKUP_ADD_STATE + " TEXT ",
                DataUtils.PICKUP_ADD_CITY,
                DataUtils.PICKUP_ADD_CITY + " TEXT ",
                DataUtils.PICKUP_ADD_ZIP_CODE,
                DataUtils.PICKUP_ADD_ZIP_CODE + " TEXT ",

                DataUtils.DELIVERY_ADDRESS_FNAME,
                DataUtils.DELIVERY_ADDRESS_FNAME + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_LNAME,
                DataUtils.DELIVERY_ADDRESS_LNAME + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_EMAIL,
                DataUtils.DELIVERY_ADDRESS_EMAIL + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_PHONE,
                DataUtils.DELIVERY_ADDRESS_PHONE + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_lANDLINE,
                DataUtils.DELIVERY_ADDRESS_lANDLINE + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_EXTENSION,
                DataUtils.DELIVERY_ADDRESS_EXTENSION + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_ADDRESS1,
                DataUtils.DELIVERY_ADDRESS_ADDRESS1 + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_ADDRESS2,
                DataUtils.DELIVERY_ADDRESS_ADDRESS2 + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_COUNTRY,
                DataUtils.DELIVERY_ADDRESS_COUNTRY + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_STATE,
                DataUtils.DELIVERY_ADDRESS_STATE + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_CITY,
                DataUtils.DELIVERY_ADDRESS_CITY + " TEXT ",
                DataUtils.DELIVERY_ADDRESS_ZIP_CODE,
                DataUtils.DELIVERY_ADDRESS_ZIP_CODE + " TEXT ",

                DataUtils.UPDATE_STATUS,
                DataUtils.UPDATE_STATUS + " INTEGER DEFAULT -1",
                DataUtils.UPDATE_DATE,
                DataUtils.UPDATE_DATE + " TEXT "


        };


//        DataUtils.KEY_ID
        OpenHelper.validateDatabase(context, tablename, params);
    }





    /*==================================================Pickup Data=======================================================*/

    public static void insertDataIntoDb(Context context, ParcelListingData parcelListingData) {
        insertDeliveryAndStatusInfoListIntoDb(context, parcelListingData.getDeliveryData());
//        insertPickupAndStatusInfoListIntoDb(context, parcelListingData.getPickupParcelDatas());
    }

    public static void insertPickupAndStatusInfoListIntoDb(Context context, ArrayList<PickupParcelData> list) {
        String parcelmastervalues = "";
        Log.i("insert", "start " + System.currentTimeMillis());
        for (int i = 0; i < list.size(); i++) {
            PickupParcelData parcelData = list.get(i);
            parcelmastervalues += appendParcelValue(parcelData);
            if (i + 1 != list.size()) {
                parcelmastervalues += ",";
            }


            StatusDao statusDao = new StatusDao(context);
            statusDao.insertMultipleDeliveryStatus(parcelData, parcelData.getStatus());


//

        }


        PickupDao pickupDao = new PickupDao(context);
        pickupDao.insertMultipleParcels(parcelmastervalues);
    }

    public static ArrayList<PickupParcelData> getPickupInfoForListing(Context context) {
        PickupDao pickupDao = new PickupDao(context);

        ArrayList<PickupParcelData> pickupParcelDatas = pickupDao.getPickupPendingParcelForListing();

//        ArrayList<ParcelListingData.ParcelData> dataArrayListDelivered = deliveryDao.getDeliveredParcelForListing();
//        ArrayList<ParcelListingData.ParcelData> dataArrayListPending = deliveryDao.getPendingParcelForListing();

//        ArrayList<ParcelListingData.ParcelData> dataArrayListAll = new ArrayList<>();
//        dataArrayListAll.addAll(dataArrayListPending);
//        dataArrayListAll.addAll(dataArrayListDelivered);


        return pickupParcelDatas;
    }

    public static void updatePickupStatus(Context ctx, PickupParcelData pickupParcelData, ParcelStatus parcelStatus) {

        ParcelListingData parcelListingData = new ParcelListingData();
        ParcelListingData.ParcelData parcelData = parcelListingData.new ParcelData(pickupParcelData.getParcel_barcode());

        StatusDao statusDao = new StatusDao(ctx);
        statusDao.insertDeliveryStatus(parcelData, parcelStatus);

        PickupDao pickupDao = new PickupDao(ctx);
        pickupDao.updatePickupComment(pickupParcelData.getColumn_id(), ParcelListingData.ParcelData.PICKUP_ATTEMPTED, parcelStatus.getStatus_comment());


    }

    public static ArrayList<UpdatedPickupParcelData> getPickupInfoForUpdateAndSYNC(Context context) {
        PickupDao deliveryDao = new PickupDao(context);

        return deliveryDao.getPickupInfoForUpdateAndSYNC(context);
    }

    public static void receivedParcel(Context ctx,PickupParcelData pickupParcelData, ParcelStatus parcelStatus) {


        ParcelListingData parcelListingData = new ParcelListingData();
        ParcelListingData.ParcelData parcelData = parcelListingData.new ParcelData(pickupParcelData.getParcel_barcode());

        StatusDao statusDao = new StatusDao(ctx);
        statusDao.insertDeliveryStatus(parcelData, parcelStatus);

        PickupDao pickupDao = new PickupDao(ctx);
        pickupDao.updatePickupComment(pickupParcelData.getColumn_id(), ParcelListingData.ParcelData.PICKUP_RECEIVED, parcelStatus.getStatus_comment());


    }

    public static String appendParcelValue(PickupParcelData parcelData) {

        String query = "('" + parcelData.getParcel_barcode()
                + "','" + parcelData.getCustomer_id()
                + "','" + parcelData.getFname()
                + "','" + parcelData.getLname()
                + "','" + parcelData.getEmail()
                + "','" + parcelData.getPhone()
                + "','" + parcelData.getLandline()
                + "','" + parcelData.getExt()
                + "','" + parcelData.getPickup_status()

                + "','" + parcelData.getPickParcelDetailData().getParcel_length()
                + "','" + parcelData.getPickParcelDetailData().getParcel_width()
                + "','" + parcelData.getPickParcelDetailData().getParcel_height()
                + "','" + parcelData.getPickParcelDetailData().getParcel_volum_weight()
                + "','" + parcelData.getPickParcelDetailData().getParcel_actual_weight()
                + "','" + parcelData.getPickParcelDetailData().getParcel_price()
                + "','" + parcelData.getPickParcelDetailData().getParcel_descrip()
                + "','" + parcelData.getPickParcelDetailData().getParcel_special_ins()
                + "','" + parcelData.getPickParcelDetailData().getParcel_assign_date()
                + "','" + parcelData.getPickParcelDetailData().getParcel_created_on()
                + "','" + parcelData.getPickParcelDetailData().getParcel_pickup_comment()

                + "','" + parcelData.getPickup_address().getfName()
                + "','" + parcelData.getPickup_address().getlName()
                + "','" + parcelData.getPickup_address().getEmail()
                + "','" + parcelData.getPickup_address().getPhone()
                + "','" + parcelData.getPickup_address().getLandline()
                + "','" + parcelData.getPickup_address().getExt()
                + "','" + parcelData.getPickup_address().getAddress1()
                + "','" + parcelData.getPickup_address().getAddress2()
                + "','" + parcelData.getPickup_address().getCountry()
                + "','" + parcelData.getPickup_address().getState()
                + "','" + parcelData.getPickup_address().getCity()
                + "','" + parcelData.getPickup_address().getZipCode()

                + "','" + parcelData.getDelivery_address().getfName()
                + "','" + parcelData.getDelivery_address().getlName()
                + "','" + parcelData.getDelivery_address().getEmail()
                + "','" + parcelData.getDelivery_address().getPhone()
                + "','" + parcelData.getDelivery_address().getLandline()
                + "','" + parcelData.getDelivery_address().getExt()
                + "','" + parcelData.getDelivery_address().getAddress1()
                + "','" + parcelData.getDelivery_address().getAddress2()
                + "','" + parcelData.getDelivery_address().getCountry()
                + "','" + parcelData.getDelivery_address().getState()
                + "','" + parcelData.getDelivery_address().getCity()
                + "','" + parcelData.getDelivery_address().getZipCode()

                + "')";
        return query;
    }


}
