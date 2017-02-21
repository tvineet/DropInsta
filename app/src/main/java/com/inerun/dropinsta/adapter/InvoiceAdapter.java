package com.inerun.dropinsta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.data.WhInvoiceParcelData;

import java.util.ArrayList;

/**
 * Created by vineet on 11/29/2016.
 */


public class InvoiceAdapter extends BaseRecyclerViewAdapter {
    private Context context;
    private ArrayList<WhInvoiceParcelData.Invoice> invoiceArrayList;
    private static OnItemClickListener mItemClickListener;
    View.OnClickListener onclickListener;

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder implements View.OnClickListener {
        public TextView invoice_no, no_of_parcel;
        public View parentView;


        public ViewHolder(View view) {
            super(view);
            parentView = view;
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
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


    public InvoiceAdapter(Context context, ArrayList<WhInvoiceParcelData.Invoice> invoiceArrayList, View.OnClickListener searchlickListener) {
        this.invoiceArrayList = invoiceArrayList;
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

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_list_item, parent, false);
        return itemView;
    }

    @Override
    public void onbindViewHolder(BaseRecyclerViewAdapter.ViewHolder viewholder, int position) {
        ViewHolder holder= (ViewHolder) viewholder.holder;
        WhInvoiceParcelData.Invoice data = invoiceArrayList.get(position);

        holder.invoice_no.setText(data.getInvoice_number());
        holder.parentView.setTag(position);
        if(data.getParcelData() != null && data.getParcelData().size() >= 0) {
            holder.no_of_parcel.setText("" + data.getParcelData().size());
        }else{
            holder.no_of_parcel.setText("0");
        }
        if(position % 2 == 0){
            holder.invoice_no.setBackgroundColor(context.getResources().getColor(R.color.colorlightyellow));
            holder.no_of_parcel.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        }else{
            holder.invoice_no.setBackgroundColor(context.getResources().getColor(R.color.sideMenuOptionDark));
            holder.no_of_parcel.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

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
        return invoiceArrayList;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        InvoiceAdapter.mItemClickListener = mItemClickListener;
    }

    public ArrayList<WhInvoiceParcelData.Invoice> getInvoiceArrayList() {
        return invoiceArrayList;
    }

    public void setInvoiceArrayList(ArrayList<WhInvoiceParcelData.Invoice> invoiceArrayList) {
        this.invoiceArrayList = invoiceArrayList;
    }
}