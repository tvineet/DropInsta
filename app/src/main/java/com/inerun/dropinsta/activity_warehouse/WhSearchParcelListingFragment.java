package com.inerun.dropinsta.activity_warehouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.inerun.dropinsta.DropInsta;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.activity.ParcelDetailFragment;
import com.inerun.dropinsta.activity.SignActivity;
import com.inerun.dropinsta.adapter.WhSearchAdapter;
import com.inerun.dropinsta.base.BaseFragment;
import com.inerun.dropinsta.constant.AppConstant;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.POD;
import com.inerun.dropinsta.data.ParcelListingData;
import com.inerun.dropinsta.data.ParcelSearchData;
import com.inerun.dropinsta.data.UpdatedParcelData;
import com.inerun.dropinsta.data.WhSearchParcelData;
import com.inerun.dropinsta.helper.DIHelper;
import com.inerun.dropinsta.service.DIRequestCreator;
import com.inerun.dropinsta.service.whParcelUploadService;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by vineet on 2/13/2017.
 */

public class WhSearchParcelListingFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

    private static final String SEARCH = "SEARCH_SERVICE";

    private static final int SIGN_REQUEST = 102;

    private Context context;
    private ParcelSearchData parcelSearchData;
    private TextView error_txt;
    private CheckBox selectall_checkbox;
    private RelativeLayout selectall_layout;
    private Button updatebtn;
    private RecyclerView detaillistview;
    WhSearchAdapter adapter;
    private ArrayList<ParcelListingData.ParcelData> selectedparcelDataArrayList;
    private ArrayList<UpdatedParcelData> updatedArrayList;

    public static Fragment newInstance(ParcelSearchData parcelSearchData) {
        WhSearchParcelListingFragment fragment = new WhSearchParcelListingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UrlConstants.KEY_DATA, parcelSearchData);

        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parcelSearchData = (ParcelSearchData) getArguments().getSerializable(UrlConstants.KEY_DATA);
    }

    @Override
    public int inflateView() {
        return R.layout.wh_parcel_search_listing;
    }

    @Override
    public void customOnCreateView(View root, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception {
        setShowBackArrow(true);
        context = getActivity();

        initView();
        getData();
    }

    private void initView() {
        error_txt = (TextView) getViewById(R.id.error_textview);
        selectall_checkbox = (CheckBox) getViewById(R.id.search_checkbox_selected);
        selectall_layout = (RelativeLayout) getViewById(R.id.checkbox_layout);
        detaillistview = (RecyclerView) getViewById(R.id.detail_listview);
        updatebtn = (Button) getViewById(R.id.search_updatebtn);

//        detaillistview.setOnLongClickListener(this);
        updatebtn.setOnClickListener(this);
        selectall_checkbox.setOnCheckedChangeListener(this);
    }

    private void getData() {

        Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getSearchMapParamsNew(parcelSearchData);

        DropInsta.serviceManager().postRequest(UrlConstants.URL_SEARCH, params, getProgress(), response_listener, response_errorlistener, SEARCH);

    }

    Response.Listener<String> response_listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgress();
            Gson gson = new Gson();

            WhSearchParcelData data = gson.fromJson(response, WhSearchParcelData.class);
            setSearchData(data);
        }
    };

    Response.ErrorListener response_errorlistener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgress();
            setSearchData(null);
            showSnackbar(error.getMessage());

        }
    };

    private void setSearchData(WhSearchParcelData searchParcelData) {
        if (searchParcelData != null && searchParcelData.getDeliverydata() != null && searchParcelData.getDeliverydata().size() > 0) {
            detaillistview.setVisibility(View.VISIBLE);
            selectall_layout.setVisibility(View.VISIBLE);
            adapter = new WhSearchAdapter(getActivity(), searchParcelData.getDeliverydata(), searchlickListener);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            detaillistview.setLayoutManager(mLayoutManager);
            detaillistview.setItemAnimator(new DefaultItemAnimator());
            detaillistview.setAdapter(adapter);


        } else {
            error_txt.setVisibility(View.VISIBLE);
            detaillistview.setVisibility(View.GONE);
            selectall_layout.setVisibility(View.GONE);
            showSnackbar(R.string.search_error);

        }


    }

    @Override
    protected String getAnalyticsName() {
        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.search_updatebtn:

                selectedparcelDataArrayList = new ArrayList<>();
                updatedArrayList = new ArrayList<>();
                for (int i = 0; i < adapter.getParcelDataList().size(); i++) {
                    if (adapter.getParcelDataList().get(i).isselected()) {
                        selectedparcelDataArrayList.add(adapter.getParcelDataList().get(i));
                    }
                }
                if (selectedparcelDataArrayList.size() > 0) {

                    for (int i = 0; i < selectedparcelDataArrayList.size(); i++) {
                        ParcelListingData.ParcelData parceldata = selectedparcelDataArrayList.get(i);
                        UpdatedParcelData updateparcel = new UpdatedParcelData(parceldata.getBarcode(), ParcelListingData.ParcelData.DELIVERED, parceldata.getDeliverycomments(), parceldata.getPayment_status(), DIHelper.getDateTime(AppConstant.DATEIME_FORMAT), "", "");

                        selectedparcelDataArrayList.set(i, parceldata);
                        updatedArrayList.add(updateparcel);
                    }

                    startActivityForResult(new Intent(getActivity(), WhSignActivity.class), SIGN_REQUEST);


                } else {
                    showSnackbar(R.string.no_parcel_error);
                }


                break;
        }
    }


    View.OnClickListener searchlickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();

            ParcelListingData.ParcelData parceldata = adapter.getParcelDataList().get(pos);
            ArrayList<ParcelListingData.ParcelData> parcelDataArrayList = new ArrayList<>();
            parcelDataArrayList.add(parceldata);

            deliverParcel(parceldata);


        }
    };

    private void deliverParcel(ParcelListingData.ParcelData parceldata) {

//        Bundle bundle = new Bundle();
//        bundle.putSerializable(UrlConstants.KEY_DATA, parceldata);
//
//        ((BaseActivity) getActivity()).goToActivity(ParcelDetailActivity.class, bundle);

        navigateToFragment(getActivity(), ParcelDetailFragment.newInstance(parceldata));
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case SIGN_REQUEST:

                if (resultCode == Activity.RESULT_OK && data.hasExtra(SignActivity.INTENT_FILENAME)) {
                    showProgress();
                    String path = data.getStringExtra(SignActivity.INTENT_FILENAME);
                    String receiver_name = data.getStringExtra(SignActivity.INTENT_RECEIVER_NAME);
//                    setImage(path);
                    Log.i("POD_path", path);
                    Log.i("Receiver_Name", ""+receiver_name);
                    String pod_name = path.substring(path.lastIndexOf("/") + 1);
                    Log.i("POD_Name", ""+pod_name);
                    Log.i("Size", ""+selectedparcelDataArrayList.size());
                    POD pod = new POD(pod_name, receiver_name);
                    Intent i = new Intent(getActivity(), whParcelUploadService.class);
                    i.putExtra(UrlConstants.KEY_POD,pod);
                    i.putExtra(UrlConstants.KEY_DATA,updatedArrayList);
                    getActivity().startService(i);
                    navigateToFragment(context, WhSearchParcelFragment.newInstance());

                }
                break;

        }

    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

        adapter.selectAllParcels(compoundButton.isChecked());

    }

    @Override
    public boolean onLongClick(View view) {
//        if(selectall_layout.getVisibility()==View.INVISIBLE) {
        selectall_layout.setVisibility(View.VISIBLE);
//        adapter.setCheckenabled(true);
        adapter.notifyDataSetChanged();
//        }
        return false;
    }



}
