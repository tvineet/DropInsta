package com.inerun.dropinsta.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.ParcelListingData;
import com.inerun.dropinsta.data.PickupParcelData;

/**
 * Created by vineet on 9/28/2017.
 */

public class DeliveryPickupDetailActivity extends FragmentBaseActivity {
    PickupParcelData pickupParcelData;

    @Override
    public Fragment getFragment() {
        Bundle bundle = getBundleFromIntent(DeliveryPickupDetailActivity.this);
        if(bundle != null){
            pickupParcelData = (PickupParcelData) bundle.getSerializable(UrlConstants.KEY_DATA);
        }
        return DeliveryPickupDetailFragment.newInstance(pickupParcelData);
    }


}
