package com.inerun.dropinsta.activity_customer_care;

import android.os.Bundle;
import android.util.Log;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.BaseActivity;

/**
 * Created by vinay on 15/12/16.
 */

public class CustomerDashboardActivity extends BaseActivity {
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    public int customSetContentView() {

        setWarehouse(true);
        return R.layout.activity_cordinator_container;
    }

    @Override
    public void customOnCreate(Bundle savedInstanceState) {

//        getSupportActionBar().setIcon(R.mipmap.toolbaricon);

        Log.i("CustomerDashboard","customOnCreate");
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                CustomerDashboardFragment.newInstance()).commit();
//        if (NotiHelper.isNotificationIntent(getIntent())) {
//            Log.i("CustomerDashboard","isNotificationIntent");
//           processNotiIntent(CustomerDashboardActivity.this);
//        }

    }


    @Override
    public void onBackPressed() {
            handleFragmentBackPressed();

    }


    @Override
    public void whDeliveryUpdated() {
       showSnackbar(R.string.parcel_updated_msg);

        hideProgress();
    }
}
