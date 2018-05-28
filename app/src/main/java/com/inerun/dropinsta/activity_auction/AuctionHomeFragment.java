package com.inerun.dropinsta.activity_auction;

import android.os.Bundle;
import android.view.View;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.AuctionBaseFragment;

/**
 * Created by vinay on 24/05/18.
 */

public class AuctionHomeFragment extends AuctionBaseFragment {


    public static AuctionHomeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AuctionHomeFragment fragment = new AuctionHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int initLayout() {
        return R.layout.auction_home;
    }

    @Override
    protected void initView(View rootview) throws IonServiceManager.InvalidParametersException {

    }
}
