package com.inerun.dropinsta.activity_auction;


import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.AuctionBaseActivity;

/**
 * Created by vinay on 24/05/18.
 */

public class AuctionHomeActivity extends AuctionBaseActivity{
    @Override
    public String getHeaderTitle() {
//        if(isValidString(getApp().appData.getStation().getStation_name())) {
//            return getApp().appData.getStation().getStation_name();
//        }else
//        {
            return getString(R.string.default_auction_title);
//        }
    }


    @Override
    public int initLayout() {

        return R.layout.activity_main_with_auction;
    }

    @Override
    public void initView() {


    }

    @Override
    protected int getMenuLayout() {
        return -1;
    }



}
