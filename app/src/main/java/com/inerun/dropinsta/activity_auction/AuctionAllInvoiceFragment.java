package com.inerun.dropinsta.activity_auction;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.inerun.dropinsta.DropInsta;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.adapter.AuctionInvoiceListAdapter;
import com.inerun.dropinsta.adapter.CustInvoiceListAdapter;
import com.inerun.dropinsta.base.AuctionBaseFragment;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.CustInvoiceParcelData;
import com.inerun.dropinsta.helper.SimpleDividerItemDecoration;
import com.inerun.dropinsta.service.DIRequestCreator;

import java.util.Map;


/**
 * Created by vineet on 28/05/18.
 */

public class AuctionAllInvoiceFragment extends AuctionBaseFragment {

    private RecyclerView detaillistview;
    private AuctionInvoiceListAdapter adapter;
    private TextView error_txt;
    private Drawable mDivider;

    private CustInvoiceParcelData whInvoiceParcelData;

    public static AuctionAllInvoiceFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AuctionAllInvoiceFragment fragment = new AuctionAllInvoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int initLayout() {
        return R.layout.auction_all_invoice_fragment;
    }

    @Override
    protected void initView(View rootview) throws IonServiceManager.InvalidParametersException {
        detaillistview = rootview.findViewById(R.id.auction_invoice_listview);
        error_txt = rootview.findViewById(R.id.error_textview);
        mDivider = getDrawable(R.drawable.payment_line_divider);

       getData();
    }


    private void getData() {
        Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getReadyInvoiceMapParams(0, 20);

        DropInsta.serviceManager().postRequest(UrlConstants.URL_READY_INVOICE_LIST, params, null, response_listener, response_errorlistener, "101");
    }

    Response.Listener<String> response_listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            hideProgress();
            Gson gson = new Gson();

            whInvoiceParcelData = gson.fromJson(response, CustInvoiceParcelData.class);

            setData(whInvoiceParcelData);
        }
    };

    Response.ErrorListener response_errorlistener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
//            hideProgress();
            setData(null);
//            showSnackbar(error.getMessage());
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };


    private void setData(final CustInvoiceParcelData whInvoiceParcelData) {
        if (whInvoiceParcelData != null && whInvoiceParcelData.getInvoiceData() != null && whInvoiceParcelData.getInvoiceData().size() > 0) {
            adapter = new AuctionInvoiceListAdapter(activity(), whInvoiceParcelData.getInvoiceData(), null);
            detaillistview.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//            mLayoutManager = new LinearLayoutManager(getActivity());
            detaillistview.setLayoutManager(mLayoutManager);
            detaillistview.setItemAnimator(new DefaultItemAnimator());
//            Drawable mDivider = getDrawable(R.drawable.payment_line_divider);
            detaillistview.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), mDivider));

            detaillistview.setAdapter(adapter);



        }else {
            error_txt.setVisibility(View.VISIBLE);
            detaillistview.setVisibility(View.GONE);
//            showSnackbar(R.string.search_error);
//            Toast.makeText(getActivity(), R.string.search_error, Toast.LENGTH_SHORT).show();
        }
    }


}
