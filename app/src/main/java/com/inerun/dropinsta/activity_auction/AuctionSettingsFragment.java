package com.inerun.dropinsta.activity_auction;

import android.os.Bundle;
import android.view.View;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.AuctionBaseFragment;

/**
 * Created by vinay on 25/05/18.
 */

public class AuctionSettingsFragment extends AuctionBaseFragment {

    public static AuctionSettingsFragment newInstance() {

        Bundle args = new Bundle();

        AuctionSettingsFragment fragment = new AuctionSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int initLayout() {
        return R.layout.auction_settings;
    }

    @Override
    protected void initView(View rootview) throws IonServiceManager.InvalidParametersException {

    }
}
