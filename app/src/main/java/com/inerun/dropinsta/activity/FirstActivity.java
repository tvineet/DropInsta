package com.inerun.dropinsta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.inerun.dropinsta.DropInsta;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.BaseActivity;
import com.inerun.dropinsta.base.CheckConnectionUtil;
import com.inerun.dropinsta.base.SweetAlertUtil;
import com.inerun.dropinsta.constant.AppConstant;
import com.inerun.dropinsta.constant.Utils;
import com.inerun.dropinsta.sql.DIDbHelper;

/**
 * Created by vineet on 25/05/18.
 */

public class FirstActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int customSetContentView() {
        return R.layout.first_layout1;
    }

    @Override
    public void customOnCreate(Bundle savedInstanceState) {

        findViewById(R.id.request_parcel_layout).setOnClickListener(this);
        findViewById(R.id.auction_layout).setOnClickListener(this);
        findViewById(R.id.user_layout).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_parcel_layout:
                Toast.makeText(this, "WIP", Toast.LENGTH_LONG).show();
                break;
            case R.id.auction_layout:
                Toast.makeText(this, "WIP", Toast.LENGTH_LONG).show();
                break;
            case R.id.user_layout:
                gotoLogin();
                break;
        }

    }

    private void gotoLogin() {

        if (CheckConnectionUtil.checkMyConnectivity(this)) {

            if (hasPermissions(this, AppConstant.requiredPermissions())) {
                DIDbHelper.ensureDatabaseIsCorrect(this);
            }
            finish();
            gotoPodDeleteActivity();


        } else {
            if (Utils.isUserLoggedIn(this)) {
                if (DropInsta.getUser().isDeliveryUser()) {
                    finish();
                    gotoHomeActivity();
                } else {
                    SweetAlertUtil.showAlertDialogFinishActivity(this, getString(R.string.activity_base_alert_message_unknown_host_exception));

                }
            } else {
                finish();
                gotoLoginActivity();

            }
        }
    }


    private void gotoPodDeleteActivity() {
        goToActivity(PodDeleteActivity.class);

    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this,
                LoginActivity.class);
        startActivity(intent);


    }

    private void gotoHomeActivity() {

        Intent intent = new Intent(this,
                DeliveryDashBoardActivity.class);
        startActivity(intent);

    }
}
