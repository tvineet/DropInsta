package com.inerun.dropinsta.activity_warehouse;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.Map;

/**
 * Created by vineet on 2/17/2017.
 */

public class WhReadyForExecutiveFragment_copy extends BaseFragment implements Paginate.Callbacks, LoadingListItemCreator {

    private static final String READY_REQUEST = "ready_request";
    private Context context;
    private TextView error_txt;
    private RecyclerView recyclerView;
    private WhRequestListAdapter adapter;
    private WhReadyParcelData whReadyParcelData;
    private Drawable mDivider;


    private int start = 0;
    private int limit = 5;
    private static final int GRID_SPAN = 3;
    protected int totalPages = 2;
    protected int itemsPerPage = 10;
    private boolean loading = false;
    private int page = 0;
    private Paginate paginate;
    protected boolean customLoadingListItem = false;

    public static Fragment newInstance() {
        WhReadyForExecutiveFragment_copy fragment = new WhReadyForExecutiveFragment_copy();
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
        getData(start, limit);


    }


    private void intView() {
        error_txt = (TextView) getViewById(R.id.error_textview);
        recyclerView = (RecyclerView) getViewById(R.id.ready_request_listview);
        mDivider = getDrawable(R.drawable.payment_line_divider);

    }

    private void getData(int start, int limit) {
        Log.i("Start_TIME", "" + System.currentTimeMillis());
        Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getReadyForExecutiveMapParams(start, limit);

        DropInsta.serviceManager().postRequest(UrlConstants.URL_READY_FOR_EXECUTIVE_LIST, params, getProgress(), response_listener, response_errorlistener, READY_REQUEST);
    }

    Response.Listener<String> response_listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgress();
            Gson gson = new Gson();

            whReadyParcelData = gson.fromJson(response, WhReadyParcelData.class);
            Log.i("END_TIME", "" + System.currentTimeMillis());
//            setData(whReadyParcelData);

            initAdater(whReadyParcelData);
//            adapter.add(whReadyParcelData.getCustRequestData());
        }
    };

    Response.ErrorListener response_errorlistener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgress();
            setData(null);
            showSnackbar(error.getMessage());
            Log.i("Error_MSG", error.getMessage());

        }
    };


    private void setData(final WhReadyParcelData whReadyParcelData) {
        if (whReadyParcelData != null && whReadyParcelData.getCustRequestData() != null && whReadyParcelData.getCustRequestData().size() > 0) {
            adapter = new WhRequestListAdapter(getActivity(), whReadyParcelData.getCustRequestData(), searchlickListener);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            Drawable mDivider = getDrawable(R.drawable.payment_line_divider);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), mDivider));

            recyclerView.setAdapter(adapter);


//            Paginate.with(recyclerView, this)
//                    .setLoadingTriggerThreshold(2)
//                    .addLoadingListItem(true)
//                    .setLoadingListItemCreator(this)
//                    .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
//                        @Override
//                        public int getSpanSize() {
//                            return GRID_SPAN;
//                        }
//                    })
//                    .build();


        } else {
            error_txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
//            showSnackbar(R.string.search_error);

        }
    }

    View.OnClickListener searchlickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();
            navigateToFragment(getActivity(), WhReadyRequestedParcelFragment.newInstance(whReadyParcelData.getCustRequestData().get(pos), whReadyParcelData.getExecutivedata()));

        }
    };


    @Override
    protected String getAnalyticsName() {
        return null;
    }

    @Override
    public synchronized void onLoadMore() {
        Log.d("Paginate", "onLoadMore");
        loading = true;
        getData1(start, limit);
//        loading = true;

//        adapter.add(whReadyParcelData.getCustRequestData());
//
    }

    @Override
    public synchronized boolean isLoading() {
        Log.d("Paginate", "isLoading");
        return loading; // Return boolean weather data is already loading or not
//        return false; // Return boolean weather data is already loading or not
    }

    @Override
    public boolean hasLoadedAllItems() {
        Log.d("Paginate", "hasLoadedAllItems");
        return page == totalPages; // If all pages are loaded return true
//        return true;// If all pages are loaded return true
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.custom_loading_list_item, viewGroup, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        VH vh = (VH) viewHolder;
        vh.tvLoading.setText(String.format("Total items loaded: %d.\nLoading more...", adapter.getItemCount()));

        // This is how you can make full span if you are using StaggeredGridLayoutManager
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) vh.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }


    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.custom_loading_list_item, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            vh.tvLoading.setText(String.format("Total items loaded: %d.\nLoading more...", adapter.getItemCount()));

            // This is how you can make full span if you are using StaggeredGridLayoutManager
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) vh.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
    }


    static class VH extends RecyclerView.ViewHolder {
        TextView tvLoading;

        public VH(View itemView) {
            super(itemView);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading_text);
        }
    }


    private void initAdater(final WhReadyParcelData whReadyParcelData) {
        if (whReadyParcelData != null && whReadyParcelData.getCustRequestData() != null && whReadyParcelData.getCustRequestData().size() > 0) {
            if (paginate != null) {
                paginate.unbind();
            }

            adapter = new WhRequestListAdapter(getActivity(), whReadyParcelData.getCustRequestData(), searchlickListener);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            Drawable mDivider = getDrawable(R.drawable.payment_line_divider);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), mDivider));

            recyclerView.setAdapter(adapter);

            loading = false;
            page = 0;
            start = page * limit;

            paginate = Paginate.with(recyclerView, this)
                    .setLoadingTriggerThreshold(2)
                    .addLoadingListItem(true)
//                    .setLoadingListItemCreator(this)
                    .setLoadingListItemCreator(customLoadingListItem ? new CustomLoadingListItemCreator() : null)
                    .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                        @Override
                        public int getSpanSize() {
                            return GRID_SPAN;
                        }
                    })
                    .build();


        } else {
            error_txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
//            showSnackbar(R.string.search_error);

        }
    }


    private void getData1(int start, int limit) {
        Log.i("Start_TIME", "" + System.currentTimeMillis());
        Map<String, String> params = DIRequestCreator.getInstance(getActivity()).getReadyForExecutiveMapParams(start, limit);

//        DropInsta.serviceManager().postRequest(UrlConstants.URL_READY_FOR_EXECUTIVE_LIST, params, getProgress(), response_listener1, response_errorlistener1, READY_REQUEST);
        DropInsta.serviceManager().postRequest(UrlConstants.URL_READY_FOR_EXECUTIVE_LIST, params, null, response_listener1, response_errorlistener1, READY_REQUEST);
    }

    Response.Listener<String> response_listener1 = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgress();
            Gson gson = new Gson();

            whReadyParcelData = gson.fromJson(response, WhReadyParcelData.class);
            Log.i("END_TIME", "" + System.currentTimeMillis());
//            setData(whReadyParcelData);
//            if(page == 0){
//                ArrayList<WhReadyParcelData.RequestData> data = new ArrayList<>();
//                adapter.setRequestDataList(data);
//                adapter.add(whReadyParcelData.getCustRequestData());
//            }else {
            adapter.add(whReadyParcelData.getCustRequestData());
            page++;
            start = page * limit;
            loading = false;
//            }
        }
    };

    Response.ErrorListener response_errorlistener1 = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgress();
            setData(null);
            showSnackbar(error.getMessage());
            Log.i("Error_MSG", error.getMessage());

        }
    };

}
