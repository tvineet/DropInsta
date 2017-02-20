package com.inerun.dropinsta.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.inerun.dropinsta.DropInsta;
import com.inerun.dropinsta.Exception.ExceptionMessages;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.adapter.DeliverySearchAdapter;
import com.inerun.dropinsta.base.BaseFragment;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.ParcelListingData;
import com.inerun.dropinsta.helper.DIHelper;
import com.inerun.dropinsta.service.DIRequestCreator;
import com.inerun.dropinsta.sql.DIDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by vineet on 2/20/2017.
 */

public class DeliveryReturnParcelFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ArrayList<ParcelListingData.ParcelData> parcellist;

    private Button submit;
    private RecyclerView recyclerView;
    private DeliverySearchAdapter mAdapter;
    private CheckBox selectall_checkbox;

    public static Fragment newInstance() {
        DeliveryReturnParcelFragment fragment = new DeliveryReturnParcelFragment();
        return fragment;
    }

    @Override
    public int inflateView() {
        return R.layout.activity_delivery_return_parcel;
    }

    @Override
    public void customOnCreateView(View root, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception {
        setShowBackArrow(true);
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) getViewById(R.id.return_parcel_listview);
        submit = (Button) getViewById(R.id.parcel_return_submit);

        submit.setOnClickListener(this);

        selectall_checkbox = (CheckBox) getViewById(R.id.checkbox_allselected);
        selectall_checkbox.setOnCheckedChangeListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
        setdata();
    }

    private void getData() {
        parcellist = DIDbHelper.getPendingParcelInfoForListing(getActivity());

    }

    private void setdata() {
        mAdapter = new DeliverySearchAdapter(getActivity(), parcellist);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new DividerItemDecoration(getDrawable(this, R.drawable.parcel_item_thumbnail_divider)));
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DeliverySearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.i("onItemClick", "OnClick");
                Bundle bundle = new Bundle();
                bundle.putSerializable(UrlConstants.KEY_DATA, parcellist.get(position));

                gotoActivity(ParcelDetailActivity.class, bundle);
                //slide from right to left
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });
    }

    @Override
    protected String getAnalyticsName() {
        return null;
    }


    @Override
    public void onClick(View view) {

        ArrayList<ParcelListingData.ParcelData> selectedparcellist;
        switch (view.getId()) {
            case R.id.parcel_return_submit:
                selectedparcellist = mAdapter.getSelectedParcels();
                if (validateParcels(selectedparcellist)) {
                    returnParcel(selectedparcellist);
                }
                break;
        }
    }


    private boolean validateParcels(ArrayList<ParcelListingData.ParcelData> selectedparcellist) {
        if (selectedparcellist != null && selectedparcellist.size() > 0) {
            return true;
        } else {
            showSnackbar(R.string.selected_parcel_error);
        }
        return false;
    }

    private void returnParcel(ArrayList<ParcelListingData.ParcelData> selectedparcellist) {

        try {
            Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getReturnParcelMapParams(selectedparcellist);
            DropInsta.serviceManager().postRequest(UrlConstants.URL_RETURN_PARCEL, params, response_listener, response_errorlistener, "RETURN_PARCEL");
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar(R.string.exception_alert_message_parsing_exception);
        }

    }


    Response.Listener response_listener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

//            Log.d("Response: ", response.toString());
            try {
                JSONObject jsonObject = new JSONObject(response);

                showSnackbar(DIHelper.getMessage(jsonObject));
                if (DIHelper.getStatus(jsonObject)) {

                    Log.i("DB", "DeliveryReturnParcelFragment: " + System.currentTimeMillis());

                } else {

//                    login_btn.setClickable(true);

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


    Response.ErrorListener response_errorlistener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("onErrorResponse: ", error.toString());
            hideProgress();

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                showSnackbar(R.string.exception_alert_message_timeout_exception);
            } else if (error instanceof AuthFailureError) {
                //TODO
                showSnackbar("AuthFailure Error");
            } else if (error instanceof ServerError) {
                //TODO
                showSnackbar(R.string.exception_alert_message_internal_server_error);
            } else if (error instanceof NetworkError) {
                //TODO
                showSnackbar(R.string.exception_alert_message_network);
            } else if (error instanceof ParseError) {
                //TODO
                showSnackbar(R.string.exception_alert_message_parsing_exception);
            } else {
                showSnackbar(R.string.exception_alert_message_error);
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mAdapter.selectAllParcels(compoundButton.isChecked());
    }
}
