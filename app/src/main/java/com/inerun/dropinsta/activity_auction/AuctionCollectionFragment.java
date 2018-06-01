package com.inerun.dropinsta.activity_auction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.AuctionBaseFragment;

/**
 * Created by vineet on 28/05/18.
 */

public class AuctionCollectionFragment extends AuctionBaseFragment {

    private TextView total_collection, cash_val, card_val, cheque_val, bank_transfer_val;

    public static AuctionCollectionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AuctionCollectionFragment fragment = new AuctionCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int initLayout() {
        return R.layout.auction_collection_fragment;
    }

    @Override
    protected void initView(View rootview) throws IonServiceManager.InvalidParametersException {

        total_collection = rootview.findViewById(R.id.collection_value);
        cash_val = rootview.findViewById(R.id.cash_value);
        card_val = rootview.findViewById(R.id.card_value);
        cheque_val = rootview.findViewById(R.id.cheque_value);
        bank_transfer_val = rootview.findViewById(R.id.bank_transfer_value);

        setData();

    }

    private void setData() {
        float cash = 200;
        float card = 300;
        float cheque = 400;
        float bank_transfer = 500;
        float total = cash+card+cheque+bank_transfer;

        total_collection.setText(""+total);
        cash_val.setText(""+cash);
        card_val.setText(""+card);
        cheque_val.setText(""+cheque);
        bank_transfer_val.setText(""+bank_transfer);
    }
}
