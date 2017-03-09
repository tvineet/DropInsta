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
import com.inerun.dropinsta.adapter.WhRequestListAdapter;
import com.inerun.dropinsta.base.BaseFragment;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.WhReadyParcelData;
import com.inerun.dropinsta.helper.SimpleDividerItemDecoration;
import com.inerun.dropinsta.service.DIRequestCreator;

import java.util.Map;

/**
 * Created by vineet on 2/17/2017.
 */

public class WhReadyForExecutiveFragment extends BaseFragment {

    private static final String READY_REQUEST = "ready_request";
    private Context context;
    private TextView error_txt;
    private RecyclerView recyclerView;
    private WhRequestListAdapter adapter;
    private WhReadyParcelData whReadyParcelData;

    public static Fragment newInstance() {
        WhReadyForExecutiveFragment fragment = new WhReadyForExecutiveFragment();
        return fragment;
    }

    @Override
    public int inflateView() {
        return R.layout.wh_ready_for_executive;
    }

    @Override
    public void customOnCreateView(View root, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception {
        setShowBackArrow(true);
        setToolBarTitle(R.string.ready_for_delivery);
        context = getActivity();
        intView();
        getData();

    }


    private void intView() {
        error_txt = (TextView) getViewById(R.id.error_textview);
        recyclerView = (RecyclerView) getViewById(R.id.ready_request_listview);

    }

    private void getData() {
        Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getReadyForExecutiveMapParams();

        DropInsta.serviceManager().postRequest(UrlConstants.URL_READY_FOR_EXECUTIVE_LIST, params, getProgress(), response_listener, response_errorlistener, READY_REQUEST);
    }

    Response.Listener<String> response_listener =  new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgress();
            Gson gson = new Gson();

            whReadyParcelData = gson.fromJson(response, WhReadyParcelData.class);

            setData(whReadyParcelData);
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


    private void setData(final WhReadyParcelData whReadyParcelData) {
        if (whReadyParcelData != null && whReadyParcelData.getCustRequestData() != null && whReadyParcelData.getCustRequestData().size() > 0) {
            adapter = new WhRequestListAdapter(getActivity(), whReadyParcelData.getCustRequestData(), searchlickListener);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            Drawable mDivider = getDrawable(R.drawable.payment_line_divider);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), mDivider));

            recyclerView.setAdapter(adapter);


        }else {
            error_txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
//            showSnackbar(R.string.search_error);

        }
    }

    View.OnClickListener searchlickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();
            navigateToFragment(getActivity(),WhReadyRequestedParcelFragment.newInstance(whReadyParcelData.getCustRequestData().get(pos), whReadyParcelData.getExecutivedata()));

        }
    };


    @Override
    protected String getAnalyticsName() {
        return null;
    }
}
