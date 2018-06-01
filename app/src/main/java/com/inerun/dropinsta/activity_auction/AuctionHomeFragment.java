package com.inerun.dropinsta.activity_auction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.AuctionBaseFragment;

/**
 * Created by vinay on 24/05/18.
 */

public class AuctionHomeFragment extends AuctionBaseFragment implements View.OnClickListener {

    private RelativeLayout createInvoice_lay, invoiceList_lay;
    private LinearLayout totalCollection_lay;

    private TextView auction_detail, total_collection;


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

        auction_detail = rootview.findViewById(R.id.auction_detail);
        total_collection = rootview.findViewById(R.id.collection_value);

        createInvoice_lay = rootview.findViewById(R.id.create_invoice_layout);
        invoiceList_lay = rootview.findViewById(R.id.all_invoice_layout);
        totalCollection_lay = rootview.findViewById(R.id.collection_layout);

        createInvoice_lay.setOnClickListener(this);
        invoiceList_lay.setOnClickListener(this);
        totalCollection_lay.setOnClickListener(this);
        setData();
    }

    private void setData() {

        auction_detail.setText("ABC/18May2018");
        total_collection.setText(""+2400 + " ZMW");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection_layout:
                pushFragment(AuctionCollectionFragment.newInstance());
                break;
            case R.id.create_invoice_layout:
                pushFragment(AuctionCreateInvoiceFragment.newInstance());
                break;
            case R.id.all_invoice_layout:
                pushFragment(AuctionAllInvoiceFragment.newInstance());
                break;

        }

    }
}
