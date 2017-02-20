package com.inerun.dropinsta.activity_warehouse;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.inerun.dropinsta.DropInsta;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.adapter.InvoiceAdapter;
import com.inerun.dropinsta.base.BaseFragment;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.WhInvoiceParcelData;
import com.inerun.dropinsta.helper.SimpleDividerItemDecoration;
import com.inerun.dropinsta.service.DIRequestCreator;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by vineet on 2/17/2017.
 */

public class WhInvoiceFragment extends BaseFragment {

    private static final String READY_INVOICE = "ready_invoice";
    private Context context;
    private TextView error_txt;
    private RecyclerView detaillistview;
    private InvoiceAdapter adapter;

    public static Fragment newInstance() {
        WhInvoiceFragment fragment = new WhInvoiceFragment();
        return fragment;
    }

    @Override
    public int inflateView() {
        return R.layout.wh_ready_invoice;
    }

    @Override
    public void customOnCreateView(View root, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception {
        setShowBackArrow(true);
        context = getActivity();
        WhInvoiceParcelData data = new WhInvoiceParcelData();
        intView();

//        getData();
        data = getDataNew();
        setData(data);
        setToolBarTitle(R.string.invoices);
    }


    private void intView() {
        error_txt = (TextView) getViewById(R.id.error_textview);
        detaillistview = (RecyclerView) getViewById(R.id.detail_listview);

    }

    private void getData() {
        Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getReadyInvoiceMapParams();

        DropInsta.serviceManager().postRequest(UrlConstants.URL_READY_INVOICE, params, getProgress(), response_listener, response_errorlistener, READY_INVOICE);
    }

    Response.Listener<String> response_listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgress();
            Gson gson = new Gson();

            WhInvoiceParcelData data = gson.fromJson(response, WhInvoiceParcelData.class);

            setData(data);
        }
    };

    Response.ErrorListener response_errorlistener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgress();
            setData(null);
            showSnackbar(error.getMessage());

        }
    };


    private void setData(WhInvoiceParcelData whInvoiceParcelData) {
        if (whInvoiceParcelData != null && whInvoiceParcelData.getInvoices() != null && whInvoiceParcelData.getInvoices().size() > 0) {
            adapter = new InvoiceAdapter(getActivity(), whInvoiceParcelData.getInvoices());
            detaillistview.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            detaillistview.setLayoutManager(mLayoutManager);
            detaillistview.setItemAnimator(new DefaultItemAnimator());
            Drawable mDivider = getDrawable(R.drawable.payment_line_divider);
            detaillistview.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), mDivider));

            detaillistview.setAdapter(adapter);

            adapter.setOnItemClickListener(new InvoiceAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {

                    navigateToFragment(getActivity(),WhInvoiceReadyParcelFragment.newInstance());
                }
            });

        }else {
            error_txt.setVisibility(View.VISIBLE);
            detaillistview.setVisibility(View.GONE);
            showSnackbar(R.string.search_error);

        }
    }


    private WhInvoiceParcelData getDataNew() {
        ArrayList<WhInvoiceParcelData.Invoice> invoiceArrayList = new ArrayList<>();

        WhInvoiceParcelData whInvoiceParcelData = new WhInvoiceParcelData();
        WhInvoiceParcelData.Invoice invoice = whInvoiceParcelData.new Invoice("TI00052",null);

        invoiceArrayList.add(invoice);


        WhInvoiceParcelData.Invoice invoice1 = whInvoiceParcelData.new Invoice("TI00044",null);

        invoiceArrayList.add(invoice1);

        WhInvoiceParcelData.Invoice invoice2 = whInvoiceParcelData.new Invoice("TI00066",null);

        invoiceArrayList.add(invoice2);

        WhInvoiceParcelData whInv = new WhInvoiceParcelData(invoiceArrayList);

        return whInv;

    }



    @Override
    protected String getAnalyticsName() {
        return null;
    }
}
