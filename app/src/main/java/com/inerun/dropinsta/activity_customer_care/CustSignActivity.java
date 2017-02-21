package com.inerun.dropinsta.activity_customer_care;

import com.inerun.dropinsta.activity.SignActivity;

/**
 * Created by vinay on 21/02/17.
 */


public class CustSignActivity extends SignActivity {

    @Override
    public boolean isReceiverNameRequired() {
        return true;
    }

    @Override
    public boolean isNationalIdRequired() {
        return true;
    }

    //This is test change1
}


