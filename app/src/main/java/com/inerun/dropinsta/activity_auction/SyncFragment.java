package com.inerun.dropinsta.activity_auction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arasthel.asyncjob.AsyncJob;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.AuctionBaseFragment;
import com.inerun.dropinsta.base.DeviceInfoUtil;
import com.inerun.dropinsta.base.SweetAlertUtil;
import com.inerun.dropinsta.helper.DIHelper;
import com.inerun.dropinsta.model.AuctionInvoice;
import com.inerun.dropinsta.model.AuctionItem;
import com.inerun.dropinsta.model.SyncData;
import com.inerun.dropinsta.model.SyncResponseBo;
import com.inerun.dropinsta.network.ServiceManager;
import com.inerun.dropinsta.sqldb.AppDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by vinay on 26/09/17.
 */

public class SyncFragment extends AuctionBaseFragment implements View.OnClickListener {

    TextView device_number, company_name, sync_heading, sync_msg;
    View indicator_view;
    View indicator_circle;
    DonutProgress donut_progress;
    Button start_btn, ok_btn;
    private int numberOfOrdersSynced = 0;
    private boolean gotoLocations = true;
    SweetAlertDialog progressdialog;

    public static SyncFragment newInstance() {

        Bundle args = new Bundle();

        SyncFragment fragment = new SyncFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    public static SyncFragment newInstance(boolean gotoLocations) {
//
//        Bundle args = new Bundle();
//
//        SyncFragment fragment = new SyncFragment();
//        args.getBoolean(ServiceManager.KEYS.flag, gotoLocations);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments().containsKey(ServiceManager.KEYS.flag)) {
//            gotoLocations = getArguments().containsKey(ServiceManager.KEYS.flag);
//        }

    }

    @Override
    protected int initLayout() {
        return R.layout.auction_fragment_sync;
    }

    @Override
    protected void initView(View rootview) {

        device_number = rootview.findViewById(R.id.sync_device_number);
        company_name = rootview.findViewById(R.id.sync_company_name);
        indicator_view = rootview.findViewById(R.id.sync_indicator_view);
        indicator_circle = rootview.findViewById(R.id.sync_indicator_circle);
        donut_progress = rootview.findViewById(R.id.donut_progress);
        sync_heading = rootview.findViewById(R.id.sync_text);
        sync_msg = rootview.findViewById(R.id.sync_msg);
        start_btn = rootview.findViewById(R.id.sync_start_button);
        ok_btn = rootview.findViewById(R.id.sync_ok_button);

//        if (!getApp().fcsPref.hasLastSync()) {
        setData();
//        } else {
//            startActivity(new Intent(activity(), HomeActivity.class));
//            activity().finish();
//        }


    }

    private void setData() {
//        device_number.setText(getString(R.string.navigation_header_device, getApp().appData.getDeviceno()));
//        Helper.setSimpleText(device_number,getApp().fcsPref.deviceName());
        start_btn.setOnClickListener(this);
        ok_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sync_start_button:
//                downloadImages();
                try {
                    startSync();
                } catch (ServiceManager.InvalidParametersException e) {
                    showException(e);

                } catch (Exception e) {
                    showException(e);
                }


                break;


            case R.id.sync_ok_button:

                saveSync();

                break;
        }

    }


    /**
     * perform synchronisation between offline and online databases
     * change text and buttons
     * call syncCompleted in callback to show user that sync has been completed
     */
    private void startSync() throws ServiceManager.InvalidParametersException {


        DIHelper.setSimpleText(sync_heading, getString(R.string.sync_processing_text));
        DIHelper.setSimpleText(sync_msg, getString(R.string.sync_processing_msg));
//        donut_progress.setVisibility(View.VISIBLE);
        start_btn.setVisibility(View.INVISIBLE);
        indicator_circle.setVisibility(View.GONE);
        indicator_view.setVisibility(View.GONE);

//        if (!isTokenPresent()) {
//            Log.i("AuthenticationFragment", "Authentication token not present request it from server");
//            logout();
//        } else {
//
////            getApp().appData.setToken(getPrefs().token());
////            getApp().appData.setDeviceno(getPrefs().deviceNo());
//            Log.i("AuthenticationFragment", "Authentication token is present Goto SyncActivity");
//
//
            syncWithServer();
//
//        }
//        new Handler().postDelayed(
//
//                new Runnable() {
//            @Override
//            public void run() {
//                syncCompleted();
//            }
//        }, 2000);


    }

    private void syncWithServer() throws ServiceManager.InvalidParametersException {
        progressdialog = SweetAlertUtil.getProgressDialog(activity());
        progressdialog.show();


        new AsyncJob.AsyncJobBuilder<String>()
                .doInBackground(new AsyncJob.AsyncAction<String>() {
                    @Override
                    public String doAsync() {

                        String datatobeSynced = getDataFromLocalDbToSyncWithServer();
//                        String datatobeSynced = null;
                        return datatobeSynced;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<String>() {
                    @Override
                    public void onResult(String dataToSendOnServer) {


                        try {
                            callService(dataToSendOnServer);
                        } catch (ServiceManager.InvalidParametersException e) {
                            showException(e);
                            hideProgress();

                        } catch (Exception e) {
                            showException(e);
                            hideProgress();

                        }

                    }
                }).create().start();


    }


    public void hideProgress() {
        if (progressdialog != null && progressdialog.isShowing())
            progressdialog.dismiss();

    }

    private String getDataFromLocalDbToSyncWithServer() {


//        List<Orders> orders = SQLite.select().from(Orders.class).where(Orders_Table.sync_status.eq(Patient.SYNC_STATUS_PENDING)).queryList();
//        numberOfOrdersSynced = orders.size();
//
//        List<Orders_Details> orders_details = SQLite.select().from(Orders_Details.class).where(Orders_Details_Table.sync_status.eq(Patient.SYNC_STATUS_PENDING)).queryList();
//
//        List<Patient> patients = SQLite.select().from(Patient.class).where(Patient_Table.patient_type.eq(Patient.PATIENT_TYPE_EXTRA_PATIENT)).and(Patient_Table.sync_status.eq(Patient.SYNC_STATUS_PENDING)).or(Patient_Table.sync_status.eq(Patient.SYNC_STATUS_UPDATE_PENDING)).queryList();
//
////        SyncData syncData = new SyncData(orders, orders_details, patients);
//
//        String device_name = "";
//
//        if (getPrefs().hasDeviceName()) {
//            device_name = getPrefs().deviceName();
//        }
//
//        SyncData syncData = new SyncData(orders, orders_details, patients, device_name);
        SyncData syncData = new SyncData();



        return getGsonInstance().toJson(syncData);


    }

    private void callService(String dataToSendOnServer) throws ServiceManager.InvalidParametersException, Exception {


//        String encrypteddataToSendOnServer = MCrypt.bytesToHex(MCrypt.getInstance().encrypt(dataToSendOnServer));
//        String encrypteddataToSendOnServer = Base64.encodeToString( MCrypt.getInstance().encrypt(dataToSendOnServer), Base64.NO_WRAP);
//        Base64.encodeToString(encrypted, Base64.NO_WRAP);

//        String params[] = {ServiceManager.KEYS.ANDROID_ID, DeviceInfoUtil.getAndroidID(activity()), ServiceManager.KEYS.TOKEN, DeviceInfoUtil.getAndroidID(activity()), ServiceManager.KEYS.DATA, encrypteddataToSendOnServer};
        String params[] = {IonServiceManager.KEYS.ANDROID_ID, DeviceInfoUtil.getAndroidID(activity()), IonServiceManager.KEYS.DATA, dataToSendOnServer};
        Log.i("params", params.toString());

        serviceManager.postRequestToServerWOprogress(serviceManager.getAddress().SyncData, new IonServiceManager.ResponseCallback(activity()) {
            @Override
            public void onException(String exception) {
                Log.i("Req Completed", "" + exception);
                showException(exception);
                hideProgress();

            }

            @Override
            public void onResponse(boolean status, String message, Object result) throws JSONException, JsonSyntaxException {


//                String trimmedstring = ((String) result).trim();


//                gson.toJsonTree(trimmedstring);
//            String lenientstring=    gson.toJson(result);
//                check this
//                String lenientstring = gson.toJson(result);
//
//                JsonParser parser = new JsonParser();

//                JsonObject finaljson = parser.parse(trimmedstring).getAsJsonObject();
//                JsonObject finaljson = lenientstring.getAsJsonObject();

                parseJsonAndSaveIntoDatabase((JsonObject) result);


            }

        }, params);
    }


    public void parseJsonAndSaveIntoDatabase(final JsonObject result) throws JsonSyntaxException {


        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {

                        try {


                            deleteAllMenus();
                            Log.i("deleteAllMenus", "deleteAllMenus");
                            JsonObject patientarray = result.getAsJsonObject();
                            Log.i("patientarray", "" + patientarray);
                            Log.i("", "" + System.currentTimeMillis());
                            Gson gson = getGsonInstance();


                            SyncResponseBo responseBo = gson.fromJson(patientarray, SyncResponseBo.class);


                            saveDataintoLocalDb(responseBo);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
//                            showException(e);
                            hideProgress();
                            return false;
                        }

                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {


                        if (result) {

                            syncCompleted();
                        } else {

//                            showException(new RuntimeException(getString(R.string.foodmenu_error_unknown_error)));
                        }
                       hideProgress();

                    }
                }).create().start();


    }


    public void deleteAllMenus() {
        Log.i("deleteAllMenus", "deleteAllMenus");
        Delete.tables(AuctionInvoice.class,
                AuctionItem.class);
        Log.i("deleteAllMenus", "Completed");
    }

    private void saveDataintoLocalDb(SyncResponseBo responseBo) {

        Log.i("saveData", "saveDataintoLocalDb");

        List invoiceList = responseBo.getAuctioninvoicesdata();
        List itemList = responseBo.getAuctionitemdata();

        if (invoiceList != null) {
            FlowManager.getDatabase(AppDatabase.class)
                    .getTransactionManager()
                    .getSaveQueue()
                    .addAll(invoiceList);

        }
        if (itemList != null) {
            FlowManager.getDatabase(AppDatabase.class)
                    .getTransactionManager()
                    .getSaveQueue()
                    .addAll(itemList);
        }


//        getApp().fcsPref.saveLastSync("" + Helper.getFormattedDateAsString(System.currentTimeMillis()));
        Log.i("sync", "finished");


    }

    private void logout() {

    }

    /**
     * show the number of orders synchronised
     * change text and buttons
     * inform user that synchronisation has been completed
     */

    public void syncCompleted() {

//        downloadImages();
//        downloadImages_inside_app();

//        DIHelper.setSimpleText(sync_heading,getString(R.string.sync_completed_text));
        if (numberOfOrdersSynced > 0) {
//            DIHelper.setSimpleText(sync_msg,getString(R.string.sync_completed_msg_with_ordernumber, "" + numberOfOrdersSynced));
        } else {
//            DIHelper.setSimpleText(sync_msg,getString(R.string.sync_completed_msg));
        }
        start_btn.setVisibility(View.GONE);
//        donut_progress.setVisibility(View.GONE);
        indicator_view.setVisibility(View.GONE);
        ok_btn.setVisibility(View.VISIBLE);

    }

    //        String URL1 = "http://images.all-free-download.com/images/graphiclarge/blue_sky_hill_small_lake_hd_picture_166027.jpg";

//    String URL1 = "http://192.168.1.31/hospital.catering/public/images/1522754932Tulips.jpg";

//    private void downloadImages() {
//
//        String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ServiceManager.KEYS.IMAGES_FOLDER_NAME + File.separator;
//        File folder = new File(destinationPath);
//
//        List<Menu> menuList = SQLite.select().from(Menu.class).where(Menu_Table.status.eq(Patient.STATUS_ACTIVE)).queryList();
//
//
//        for (Menu menu : menuList) {
//            final String imageName = menu.getImage();
//            String URL1 = ((FCSApplication) getActivity().getApplication()).serviceManager.getAddress().IMAGE_URL;
//
//            if (imageName != null && imageName.length() > 0) {
//
//                String imageExist = destinationPath + File.separator + imageName;
//
//                File imageFile = new File(imageExist);
//                if (imageFile.exists()) {
//                    Log.i("IsExist", "" + imageFile.exists());
//                    continue;
//
//                } else {
//
//
//                    URL1 = URL1 + imageName;
//
//                    boolean success = true;
//                    if (!folder.exists()) {
//                        success = folder.mkdirs();
////
//                    }
//
//                    if (success) {
//
//                        String extStorageDirectory = folder.toString();
//                        Log.i("Image_url", URL1);
//
//
//                        int downloadIdOne = PRDownloader.download(URL1, extStorageDirectory, imageName)
//                                .build()
//                                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
//                                    @Override
//                                    public void onStartOrResume() {
//
//                                    }
//                                })
//                                .setOnPauseListener(new OnPauseListener() {
//                                    @Override
//                                    public void onPause() {
//
//                                    }
//                                })
//                                .setOnCancelListener(new OnCancelListener() {
//                                    @Override
//                                    public void onCancel() {
//
//                                    }
//                                })
//                                .setOnProgressListener(new OnProgressListener() {
//                                    @Override
//                                    public void onProgress(Progress progress) {
//
//                                    }
//                                })
//                                .start(new OnDownloadListener() {
//                                    @Override
//                                    public void onDownloadComplete() {
////                                    showLongToast("Download Complete"+imageName);
//                                        Log.i("Sync_frag", "Download Complete: " + imageName);
//                                    }
//
//                                    @Override
//                                    public void onError(Error error) {
////                                    showLongToast("Connection_Error: "+error.isConnectionError() + " AND Server_Error: "+ error.isServerError());
//                                        Log.i("Sync_frag", "Connection_Error: " + error.isConnectionError() + " AND Server_Error: " + error.isServerError());
//
//                                    }
//                                });
//
//
//                    } else {
//                        // Do s
//                        showLongToast("download_FALSE " + success);
//                    }
//                }
//            } else {
//                continue;
//            }
//
//        }
//
//
//    }

//    private void downloadImages_inside_app() {
//
//        try {
//
////        String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ServiceManager.KEYS.IMAGES_FOLDER_NAME + File.separator;
////        File image_folder = new File(destinationPath);
//
//            //get path inside app data
//            File image_folder = getActivity().getDir(ServiceManager.KEYS.IMAGES_FOLDER_NAME, Context.MODE_PRIVATE);
//
//
//            List<Menu> menuList = SQLite.select().from(Menu.class).where(Menu_Table.status.eq(Patient.STATUS_ACTIVE)).queryList();
////        List<Menu> menuList = SQLite.select().from(Menu.class).where(Menu_Table.status.eq(Patient.STATUS_ACTIVE)).and(Menu_Table.image.isNotNull()).queryList();
//
//            //get the list of all files from a folder
//            ArrayList<String> allFileList = listFiles(image_folder);
//
//            for (Menu menu : menuList) {
//                final String imageName = menu.getImage();
//                String image_Online_Url = ((FCSApplication) getActivity().getApplication()).serviceManager.getAddress().IMAGE_URL;
//
//                if (imageName != null && imageName.length() > 0) {
//
//                    //List of extra files in download folder
//                    if (allFileList.contains(imageName)) {
//                        allFileList.remove(imageName);
//                    }
//
//
////                String imageExist = destinationPath + File.separator + imageName;
////                File imageFile = new File(imageExist);
//
//                    File imageFile = new File(image_folder, imageName);
//
//                    if (imageFile.exists()) {
//                        Log.i("ImageIsExist", "" + imageFile.exists());
//                        Log.i("Image_path", imageFile.toString());
//                        continue;
//
//                    } else {
//
//
//                        image_Online_Url = image_Online_Url + imageName;
//
//                        boolean success = true;
//                        if (!image_folder.exists()) {
////                            success = image_folder.mkdirs();
//                        success = false;
//
//                        }
//
//                        if (success) {
//
//                            String folder_path = image_folder.toString();
//
//                            Log.i("Image_url", image_Online_Url);
//                            Log.i("Folder_path", folder_path);
//
//
//                            int downloadIdOne = PRDownloader.download(image_Online_Url, folder_path, imageName)
//                                    .build()
//                                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
//                                        @Override
//                                        public void onStartOrResume() {
//
//                                        }
//                                    })
//                                    .setOnPauseListener(new OnPauseListener() {
//                                        @Override
//                                        public void onPause() {
//
//                                        }
//                                    })
//                                    .setOnCancelListener(new OnCancelListener() {
//                                        @Override
//                                        public void onCancel() {
//
//                                        }
//                                    })
//                                    .setOnProgressListener(new OnProgressListener() {
//                                        @Override
//                                        public void onProgress(Progress progress) {
//
//                                        }
//                                    })
//                                    .start(new OnDownloadListener() {
//                                        @Override
//                                        public void onDownloadComplete() {
////                                    showLongToast("Download Complete"+imageName);
//                                            Log.i("Sync_frag", "Download Complete: " + imageName);
//                                        }
//
//                                        @Override
//                                        public void onError(Error error) {
////                                    showLongToast("Connection_Error: "+error.isConnectionError() + " AND Server_Error: "+ error.isServerError());
//                                            Log.i("Sync_frag", "Connection_Error: " + error.isConnectionError() + " AND Server_Error: " + error.isServerError());
//
//                                        }
//                                    });
//
//
//                        } else {
//                            // Do s
//                            showLongToast("No directory found " + success);
//                        }
//                    }
//
//                } else {
//                    continue;
//                }
//
//            }
//
//            //Delete extra files from download folder
//            if (allFileList != null && allFileList.size() > 0) {
//
//                for (String extraFile : allFileList) {
//                    File extraImageFile = new File(image_folder, extraFile);
//                    if (extraImageFile.exists()) {
//                        Log.i("delete_IMG", extraImageFile.toString());
//                        extraImageFile.delete();
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//    }

    private void saveSync() {


//        if (gotoLocations) {
//            startActivity(new Intent(activity(), LocationsActivity.class));
//        }
        activity().finish();
    }

    /**
     * List all the files under a directory
     * @param directory to be listed
     */
    public ArrayList<String> listFiles(File directory){

        ArrayList<String> fileList = new ArrayList<>();
//        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        if(fList != null && fList.length > 0) {
            for (File file : fList) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                    fileList.add(file.getName());
                }
            }
        }

        return fileList;
    }
}
