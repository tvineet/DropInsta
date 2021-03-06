package com.inerun.dropinsta.activity_warehouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inerun.dropinsta.DropInsta;
import com.inerun.dropinsta.Exception.ExceptionMessages;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.activity.ParcelDetailFragment;
import com.inerun.dropinsta.adapter.CustomerExecutiveAdapter;
import com.inerun.dropinsta.adapter.WhReadyParcelAdapter;
import com.inerun.dropinsta.adapter.WhRequestedParcelListAdapter;
import com.inerun.dropinsta.base.BaseFragment;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.CustomerExecutiveData;
import com.inerun.dropinsta.data.ParcelListingData;
import com.inerun.dropinsta.data.ReadyParcelData;
import com.inerun.dropinsta.data.WhInvoiceParcelData;
import com.inerun.dropinsta.data.WhReadyParcelData;
import com.inerun.dropinsta.helper.DIHelper;
import com.inerun.dropinsta.scanner.CameraTestActivity;
import com.inerun.dropinsta.service.DIRequestCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by vineet on 2/17/2017.
 */

public class WhReadyRequestedParcelFragment extends BaseFragment implements View.OnClickListener {

    private static final int PARCEL_SCAN = 102;
    private static final String DELIVERED = "customer_support_delivered";
    private Context context;
    private LinearLayout submit_layout, parcel_scan_layout;
    private TextView error_txt;
    private RecyclerView recyclerView;
    private WhRequestedParcelListAdapter adapter;
    private Button delivery_button;
    private ArrayList<ParcelListingData.ParcelData> selectedparcelDataArrayList;
    private ArrayList<ReadyParcelData> updatedArrayList;
    private Spinner executiveSpinner;
    private CustomerExecutiveAdapter customerExecutiveAdapter;
    private WhReadyParcelData.RequestData requestData;
    private ArrayList<CustomerExecutiveData> executivedata;

    public static Fragment newInstance(WhReadyParcelData.RequestData invoice, ArrayList<CustomerExecutiveData> executivedata) {
        WhReadyRequestedParcelFragment fragment = new WhReadyRequestedParcelFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UrlConstants.KEY_DATA, invoice);
        bundle.putSerializable(UrlConstants.KEY_EXECUTIVE_DATA, executivedata);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestData  = (WhReadyParcelData.RequestData) getArguments().getSerializable(UrlConstants.KEY_DATA);
        executivedata  = (ArrayList<CustomerExecutiveData>) getArguments().getSerializable(UrlConstants.KEY_EXECUTIVE_DATA);
        CustomerExecutiveData executiveData_hint = new CustomerExecutiveData("", getString(R.string.customer_executive_hint));
        executivedata.add(executiveData_hint);
    }

    @Override
    public int inflateView() {
        return R.layout.wh_ready_requested_parcel;
    }

    @Override
    public void customOnCreateView(View root, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception {
        setShowBackArrow(true);
        context = getActivity();

        intView();
        setSearchData(requestData, executivedata);
    }

    private void intView() {
        error_txt = (TextView) getViewById(R.id.error_textview);
        parcel_scan_layout = (LinearLayout) getViewById(R.id.parcel_scan_layout);
        recyclerView = (RecyclerView) getViewById(R.id.requested_parcel_listview);

        submit_layout = (LinearLayout) getViewById(R.id.submit_layout);
        delivery_button = (Button) getViewById(R.id.delivery_button);

        delivery_button.setOnClickListener(this);
        parcel_scan_layout.setOnClickListener(this);
        executiveSpinner = (Spinner) getViewById(R.id.customer_care_spinner);

    }

    @Override
    protected String getAnalyticsName() {
        return null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.parcel_scan_layout:
                startActivityForResult(new Intent(getActivity(), CameraTestActivity.class), PARCEL_SCAN);
                break;

            case R.id.delivery_button:
                parcelDelivered();
                break;
        }
    }


    private void setSearchData(WhReadyParcelData.RequestData searchParcelData, ArrayList<CustomerExecutiveData> executivedata) {

        if (searchParcelData != null && searchParcelData.getParcelData() != null && searchParcelData.getParcelData().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            parcel_scan_layout.setVisibility(View.VISIBLE);
            adapter = new WhRequestedParcelListAdapter(getActivity(), searchParcelData.getParcelData(), searchlickListener);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            submit_layout.setVisibility(View.VISIBLE);

            setExecutiveData(executivedata);

        } else {
            error_txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            submit_layout.setVisibility(View.GONE);
            parcel_scan_layout.setVisibility(View.GONE);
            showSnackbar(R.string.search_error);

        }


    }

    private void setExecutiveData(ArrayList<CustomerExecutiveData> executivedata) {

        customerExecutiveAdapter = new CustomerExecutiveAdapter(getActivity(), android.R.layout.simple_spinner_item, executivedata);
        customerExecutiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        executiveSpinner.setAdapter(customerExecutiveAdapter);
        executiveSpinner.setSelection(executivedata.size() - 1);
    }

    View.OnClickListener searchlickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();

            ParcelListingData.ParcelData parceldata = adapter.getParcelDataList().get(pos);
            ArrayList<ParcelListingData.ParcelData> parcelDataArrayList = new ArrayList<>();
            parcelDataArrayList.add(parceldata);

            navigateToFragment(getActivity(), ParcelDetailFragment.newInstance(parceldata));


        }
    };


    private void parcelDelivered() {

        String customerCareId = "";
        customerCareId = ((CustomerExecutiveData) executiveSpinner.getSelectedItem()).getId();

        selectedparcelDataArrayList = new ArrayList<>();
        updatedArrayList = new ArrayList<>();
        for (int i = 0; i < adapter.getParcelDataList().size(); i++) {
            if (adapter.getParcelDataList().get(i).isselected()) {
                selectedparcelDataArrayList.add(adapter.getParcelDataList().get(i));
            }
        }

        if (selectedparcelDataArrayList.size() == adapter.getParcelDataList().size() ) {

            if(isStringValid(customerCareId)) {

                for (int i = 0; i < selectedparcelDataArrayList.size(); i++) {
                    ParcelListingData.ParcelData parceldata = selectedparcelDataArrayList.get(i);
                    ReadyParcelData readyParcelData = new ReadyParcelData(parceldata.getBarcode());

//                selectedparcelDataArrayList.set(i, parceldata);
                    updatedArrayList.add(readyParcelData);
                }

//                Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getReadyParcelDeliveredMapParamsForCustSupport(requestData.getRequest_id(), customerCareId);
                Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getRequestedParcelHandoverMapParamsForCustSupport(requestData.getRequest_id(), customerCareId);

                DropInsta.serviceManager().postRequest(UrlConstants.URL_INVOICE_DELIVERED_CUSTOMER, params, getProgress(), response_listener_delivery, response_errorlistener_delivery, DELIVERED);

            }else{
                showSnackbar(R.string.customer_care_error);
            }
        } else {
            showSnackbar(R.string.select_all_parcel);
        }


    }

    Response.Listener<String> response_listener_delivery = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                JSONObject jsonObject = new JSONObject(response);

                showSnackbar(DIHelper.getMessage(jsonObject));
                if (DIHelper.getStatus(jsonObject)) {

                   navigateToFragment(context, WhReadyForExecutiveFragment.newInstance());

                }
            } catch (JSONException e) {
                e.printStackTrace();

                showSnackbar(ExceptionMessages.getMessageFromException(getActivity(), -1, e));

            } catch (Exception e) {
                e.printStackTrace();

                showSnackbar(ExceptionMessages.getMessageFromException(getActivity(), -1, e));
            }
            hideProgress();
        }
    };

    Response.ErrorListener response_errorlistener_delivery = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgress();

            showSnackbar(error.getMessage());

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PARCEL_SCAN:

                if (resultCode == Activity.RESULT_OK && data.hasExtra(CameraTestActivity.INTENT_BARCODE_VALUE)) {

                    String barcode = data.getStringExtra(CameraTestActivity.INTENT_BARCODE_VALUE);
                    Log.i("BARCODE", "WhReadyRequestParcelFragment: with space "+barcode);
                    barcode = barcode.replaceAll(" ","");
                    barcode = barcode.replaceAll("\\x1d","");
                    Log.i("BARCODE", "WhReadyRequestParcelFragment: "+barcode);
//                    for(ParcelListingData.ParcelData parcelData:invoice.getParcelData()){
//                        if(parcelData.getBarcode().equalsIgnoreCase(barcode)){
//                            parcelData.setIsselected(true);
//                        }
//                    }
                    int selectedPosition = 0;

                    for(int i=0;i<adapter.getParcelDataList().size();i++)
                    {
                        ParcelListingData.ParcelData parcelData= adapter.getParcelDataList().get(i);
                        if(parcelData.getBarcode().equalsIgnoreCase(barcode)){
                            adapter.getParcelDataList().get(i).setIsselected(true);
                            selectedPosition = i;
                        }
                    }
//                    for(ParcelListingData.ParcelData parcelData : adapter.getParcelDataList()){
//                        if(parcelData.getBarcode().equalsIgnoreCase(barcode)){
//                            parcelData.setIsselected(true);
//                        }
//                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                    recyclerView.scrollToPosition(selectedPosition);

                }
                break;
        }

    }

}
