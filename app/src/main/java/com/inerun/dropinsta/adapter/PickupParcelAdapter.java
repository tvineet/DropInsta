package com.inerun.dropinsta.adapter;

/**
 * Created by vinay on 18/01/17.
 */


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.data.PickupParcelData;

import java.util.ArrayList;

/**
 * Created by vineet on 11/29/2016.
 */


public class PickupParcelAdapter extends BaseRecyclerViewAdapter implements CompoundButton.OnCheckedChangeListener {
    private Context context;
    private ArrayList<PickupParcelData> parcelDataList;
    private static OnItemClickListener mItemClickListener;
    private boolean onBind;


    public ArrayList<PickupParcelData> getParcelDataList() {
        return parcelDataList;
    }

    public void setParcelDataList(ArrayList<PickupParcelData> parcelDataList) {
        this.parcelDataList = parcelDataList;
    }

    public PickupParcelAdapter(Context context, ArrayList<PickupParcelData> parcelDataList) {
        this.parcelDataList = parcelDataList;
        this.context = context;
    }


    @Override
    protected BaseRecyclerViewAdapter.ViewHolder getViewHolder(View itemView) {

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    protected View oncreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_pickup_list_item, parent, false);
        return itemView;
    }

    @Override
    public void onbindViewHolder(BaseRecyclerViewAdapter.ViewHolder viewholder, int position) {
        ViewHolder holder= (ViewHolder) viewholder.holder;
        PickupParcelData data = parcelDataList.get(position);

//        holder.customerName.setText(context.getString(R.string.parcel_customer_name,data.getName()));
        holder.parcel_id.setText(data.getParcel_barcode());
        holder.name.setText(data.getName());
        holder.phone.setText(data.getPhone());
        holder.weight.setText(data.getPickParcelDetailData().getParcel_volum_weight());
        holder.price.setText(data.getPickParcelDetailData().getParcel_price());

        holder.address.setText(data.getDeliveryAddress());
        Log.i("Adddress",data.getDeliveryAddress());



        holder.item_rootLayout.setTag(position);
//        holder.callbtn.setTag(position);
    }




//    @Override
//    public int getItemViewType(int position) {
//
//        if (parcelDataList.get(position).isDelivered()) {
//            return ParcelListingData.ParcelData.DELIVERED;
//        } else{
//            return ParcelListingData.ParcelData.PENDING;
//        }
//
//    }



    @Override
    public ArrayList initObjectList() {
        return  parcelDataList;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        PickupParcelAdapter.mItemClickListener = mItemClickListener;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//        if (!onBind) {
//            int pos = (int) compoundButton.getTag();
//            if (validateCheck(parcelDataList.get(pos))) {
//                parcelDataList.get(pos).setIsselected(checked);
//            }
//
//
//            // your process when checkBox changed
//            // ...
//
//            notifyDataSetChanged();
//        }


    }

  /*  private boolean validateCheck(ParcelListingData.ParcelData checkparcel) {

        boolean flag = true;

        for (int i = 0; i < parcelDataList.size(); i++) {
            ParcelListingData.ParcelData parcel = parcelDataList.get(i);

            if (parcel.isselected() && (!parcel.getCustid().equalsIgnoreCase(checkparcel.getCustid()))) {

                flag = false;
                ((BaseActivity)context).showSnackbar(R.string.different_parcels_error);
            }
        }

        return flag;
    }*/

   /* public ArrayList<ParcelListingData.ParcelData> getSelectedParcels() {

        ArrayList<ParcelListingData.ParcelData> selectedParcelList= new ArrayList<>();

        for (int i = 0; i < parcelDataList.size(); i++) {
            ParcelListingData.ParcelData parcel = parcelDataList.get(i);
            if (parcel.isselected() ) {
                selectedParcelList.add(parcel);


            }
        }

        return selectedParcelList;
    }*/
   /* public void selectAllParcels(boolean checked) {

        for (int i = 0; i < parcelDataList.size(); i++) {
            if (!parcelDataList.get(i).isDelivered()) {
                parcelDataList.get(i).setIsselected(checked);
            }
        }
        notifyDataSetChanged();


    }*/

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder implements View.OnClickListener {
        public TextView parcel_id, name, phone, address, weight, price;
        public ImageButton callbtn;
        public RelativeLayout item_rootLayout;

        public ViewHolder(View view) {
            super(view);


            parcel_id = (TextView) view.findViewById(R.id.pickup_price_id);
            name = (TextView) view.findViewById(R.id.pickup_name);

            address = (TextView) view.findViewById(R.id.pickup_address);

            phone = (TextView) view.findViewById(R.id.pickup_phone);
            weight = (TextView) view.findViewById(R.id.pickup_weight);
            price = (TextView) view.findViewById(R.id.pickup_price);
            callbtn = (ImageButton) view.findViewById(R.id.pickup_imageview);
            item_rootLayout = (RelativeLayout) view.findViewById(R.id.item_rootLayout);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            int pos= (int) view.getTag();
            Log.i("pos",""+pos);
            if(pos>=0) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, pos);
                }
            }
        }
    }

}
