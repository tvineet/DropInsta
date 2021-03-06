package com.inerun.dropinsta;

import android.app.Application;
import android.util.Log;

import com.inerun.dropinsta.activity_auction.IonServiceManager;
import com.inerun.dropinsta.base.AuctionBaseActivity;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.constant.Utils;
import com.inerun.dropinsta.data.LoginData;
import com.inerun.dropinsta.fontlib.TypefaceUtil;
import com.inerun.dropinsta.network.ServiceManager;
import com.inerun.dropinsta.service.DIRequestCreator;
import com.inerun.dropinsta.sqldb.AppDatabase;
import com.inerun.dropinsta.sqldb.SQLCipherHelperImpl;
import com.koushikdutta.ion.Ion;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;

import java.util.Map;

/**
 * Created by vinay on 23/11/16.
 */

public class DropInsta extends Application {


    private static final String EXCEPTION_SERVICE = "ExceptionService";
    public static LoginData user;
    static ServiceManager serviceManager;
   public static IonServiceManager ionserviceManager;
    private DropInsta appcontext;
    public AuctionBaseActivity currentActivity;


    @Override
    public void onCreate() {
        super.onCreate();
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
//        Log.i("TMApplication", "onCreate");
        appcontext = DropInsta.this;
//        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        serviceManager = ServiceManager.init(DropInsta.this);

        FlowManager.init(this);
        try {

//            FlowManager.init(new FlowConfig.Builder(this)
//                    .addDatabaseConfig(new
//                            DatabaseConfig.Builder(AppDatabase.class)
//                            .openHelper(new DatabaseConfig.OpenHelperCreator() {
//                                @Override
//                                public OpenHelper createHelper(DatabaseDefinition databaseDefinition, DatabaseHelperListener helperListener) {
//                                    return new SQLCipherHelperImpl(databaseDefinition, helperListener,DropInsta.this);
//                                }
//                            })
//                            .build()).openDatabasesOnInit(true)
//                    .build());

//
            // add for verbose logging
            FlowLog.setMinimumLoggingLevel(FlowLog.Level.D);
            Log.i("PATH", "" + FlowManager.getContext().getDatabasePath(AppDatabase.NAME));

            initServiceManager();
        }catch (Exception e)
        {
            //handle exception with Exception handler
            e.printStackTrace();

        }


        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/robotolight.ttf");
        updateGcmIdToServer();
    }

    private void updateGcmIdToServer() {
        String gcmid = Utils.getGcmId(appcontext);
        Log.d("updateGcmIdToServer", "Gcm id: "+gcmid);
        if (Utils.isUserLoggedIn(appcontext) && gcmid != null && gcmid.length() > 0) {

            Log.i("updateGcmIdToServer", "Updating Gcm id To Server");
            Map<String, String> params = DIRequestCreator.getInstance(DropInsta.this).getGcmRegistrationParams(appcontext, gcmid);

            serviceManager().postRequest(UrlConstants.URL_GCM, params, null, null, null, EXCEPTION_SERVICE);
        } else {
            Log.e("updateGcmIdToServer", "Gcm id Cannot be updated,Either user is not logged in or gcm id is not present");
        }
    }


    public static ServiceManager serviceManager() {
        return serviceManager;
    }


    public static LoginData getUser() {
        return user;
    }

    public static void setUser(LoginData user) {
        DropInsta.user = user;
    }

    private void initServiceManager() {
        IonServiceManager.Builder builder = new IonServiceManager.Builder(this);
        ionserviceManager = builder.build();
        String ip = UrlConstants.BASE_ADDRESS;
        if (ip != null && ip.length() > 0) {
            setBaseUrl(ip);
        }

        Ion.getDefault(this).configure().setLogging("ION", Log.DEBUG);
    }

    public void setCurrentActivity(AuctionBaseActivity currentActivity) {
        this.currentActivity = currentActivity;
        if(ionserviceManager!=null) {
            ionserviceManager.setActivity(currentActivity);
        }
    }

    public void setBaseUrl(String url) {
        ionserviceManager.changeBaseUrlToThis(url);
    }



//    public void setCurrentFragment(Fragment currentFragment) {
//        this.ionserviceManager = currentFragment;
//    }

}
