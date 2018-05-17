package com.inerun.dropinsta.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.adapter.PickupParcelAdapter;
import com.inerun.dropinsta.base.BaseFragment;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.PickParcelDetailData;
import com.inerun.dropinsta.data.PickupAddress;
import com.inerun.dropinsta.data.PickupParcelData;
import com.inerun.dropinsta.sql.DIDbHelper;

import java.util.ArrayList;

/**
 * Created by vineet on 9/27/2017.
 */

public class DeliveryPickupParcelFragment extends BaseFragment {


    private RecyclerView recyclerView;
    private ArrayList<PickupParcelData> pickupParcelDataArrayList;
    private PickupParcelAdapter mAdapter;
    private Context context;

    public static DeliveryPickupParcelFragment newInstance() {

        Bundle args = new Bundle();

        DeliveryPickupParcelFragment fragment = new DeliveryPickupParcelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int inflateView() {
        return R.layout.activity_delivery_pickup_parcel;
    }

    @Override
    public void customOnCreateView(View root, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception {
        context = getActivity();
        pickupParcelDataArrayList = new ArrayList<>();
        setShowBackArrow(true);
        initView();

    }

    private void initView() {
        recyclerView = (RecyclerView) getViewById(R.id.pickup_parcel_listview);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        setdata();
    }

    private void getData() {

        pickupParcelDataArrayList = DIDbHelper.getPickupInfoForListing(context);

    }

    private void setdata() {

        mAdapter = new PickupParcelAdapter(getActivity(), pickupParcelDataArrayList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new DividerItemDecoration(getDrawable(this, R.drawable.parcel_item_thumbnail_divider)));
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PickupParcelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.i("onItemClick", "OnClick");
                Bundle bundle = new Bundle();
                bundle.putSerializable(UrlConstants.KEY_DATA, pickupParcelDataArrayList.get(position));

                gotoActivity(DeliveryPickupDetailActivity.class, bundle);
                //slide from right to left
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });


    }

    @Override
    protected String getAnalyticsName() {
        return null;
    }
}
