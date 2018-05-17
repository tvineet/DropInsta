package com.inerun.dropinsta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.data.WhInvoiceParcelData;
import com.inerun.dropinsta.data.WhReadyParcelData;

import java.util.ArrayList;

/**
 * Created by vineet on 11/29/2016.
 */


public class WhRequestListAdapter extends BaseRecyclerViewAdapter {
    private Context context;
    private ArrayList<WhReadyParcelData.RequestData> requestDataList;
    private static OnItemClickListener mItemClickListener;
    View.OnClickListener onclickListener;

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder implements View.OnClickListener {
        public TextView request_id, no_of_parcel;
        public View parentView;


        public ViewHolder(View view) {
            super(view);
            parentView = view;
            request_id = (TextView) view.findViewById(R.id.request_id);
            no_of_parcel = (TextView) view.findViewById(R.id.no_of_parcel);
            view.setOnClickListener(onclickListener);
        }

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }


    public WhRequestListAdapter(Context context, ArrayList<WhReadyParcelData.RequestData> requestDataList, View.OnClickListener searchlickListener) {
        this.requestDataList = requestDataList;
        this.context = context;
        this.onclickListener = searchlickListener;
    }



    @Override
    protected BaseRecyclerViewAdapter.ViewHolder getViewHolder(View itemView) {
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    protected View oncreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wh_request_list_item, parent, false);
        return itemView;
    }

    @Override
    public void onbindViewHolder(BaseRecyclerViewAdapter.ViewHolder viewholder, int position) {
        ViewHolder holder= (ViewHolder) viewholder.holder;
        WhReadyParcelData.RequestData data = requestDataList.get(position);

        holder.request_id.setText(data.getRequest_id());
        holder.parentView.setTag(position);
        if(data.getParcelData() != null && data.getParcelData().size() >= 0) {
            holder.no_of_parcel.setText("" + data.getParcelData().size());
        }else{
            holder.no_of_parcel.setText("0");
        }
//        if(position % 2 == 0){
//            holder.invoice_no.setBackgroundColor(context.getResources().getColor(R.color.colorlightyellow));
//            holder.no_of_parcel.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
//
//        }else{
//            holder.invoice_no.setBackgroundColor(context.getResources().getColor(R.color.sideMenuOptionDark));
//            holder.no_of_parcel.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//        }

    }



//    @Override
//    public int getItemViewType(int position) {
//
//        if (parcelDataList.get(position).isDelivered()) {
//            return ParcelListingData.ParcelData.DELIVERED;
//        } else {
//            return ParcelListingData.ParcelData.PENDING;
//        }
//
//    }



    @Override
    public ArrayList initObjectList() {
        return requestDataList;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        WhRequestListAdapter.mItemClickListener = mItemClickListener;
    }

    public ArrayList<WhReadyParcelData.RequestData> getRequestDataList() {
        return requestDataList;
    }

    public void setRequestDataList(ArrayList<WhReadyParcelData.RequestData> requestDataList) {
        this.requestDataList = requestDataList;
    }


    public void add(ArrayList<WhReadyParcelData.RequestData> items) {
        int previousDataSize = this.requestDataList.size();
        this.requestDataList.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }
}