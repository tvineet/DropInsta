package com.inerun.dropinsta.activity_auction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.adapter.AuctionAddItemAdapter;
import com.inerun.dropinsta.base.AuctionBaseFragment;
import com.inerun.dropinsta.helper.DIHelper;
import com.inerun.dropinsta.helper.ExpandableHeightListView;
import com.inerun.dropinsta.scanner.CameraTestActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by vineet on 28/05/18.
 */

public class AuctionCreateInvoiceFragment extends AuctionBaseFragment implements View.OnClickListener {

    private static final int ITEM_SCAN = 108;
    private ExpandableHeightListView itemlistview;
    private LinearLayout addItem;
    private TextView createInvoice;
    private AuctionAddItemAdapter adapter;

    private MaterialBetterSpinner payment_mode;
    private ArrayAdapter<String> paymentarrayAdapter;

    public static AuctionCreateInvoiceFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AuctionCreateInvoiceFragment fragment = new AuctionCreateInvoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int initLayout() {
        return R.layout.auction_create_invoice_fragment;
    }

    @Override
    protected void initView(View rootview) throws IonServiceManager.InvalidParametersException {

        addItem = rootview.findViewById(R.id.additem_layout);
        itemlistview = rootview.findViewById(R.id.item_listview);
        createInvoice = rootview.findViewById(R.id.create_invoice_btn);
        payment_mode = rootview.findViewById(R.id.add_payment_mode);

        addItem.setOnClickListener(this);
        createInvoice.setOnClickListener(this);
        setData();
    }


    private void setData() {
        adapter = new AuctionAddItemAdapter(getActivity());

        itemlistview.setAdapter(adapter);

        paymentarrayAdapter = new ArrayAdapter<String>(activity(),
                android.R.layout.simple_dropdown_item_1line, DIHelper.getPaymentModeArray());
        payment_mode.setAdapter(paymentarrayAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.additem_layout:
                startActivityForResult(new Intent(getActivity(), CameraTestActivity.class), ITEM_SCAN);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case ITEM_SCAN:

                if (resultCode == Activity.RESULT_OK && data.hasExtra(CameraTestActivity.INTENT_BARCODE_VALUE)) {
                    adapter.addParcelToList(data.getStringExtra(CameraTestActivity.INTENT_BARCODE_VALUE));
                }
                break;
        }

    }
}
