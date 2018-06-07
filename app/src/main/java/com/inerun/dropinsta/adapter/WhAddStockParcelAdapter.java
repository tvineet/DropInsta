package com.inerun.dropinsta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.SweetAlertUtil;
import com.inerun.dropinsta.constant.Utils;
import com.inerun.dropinsta.helper.RecyclerViewHolderOnClickListener;
import com.inerun.dropinsta.model.AuctionInvoice;
import com.inerun.dropinsta.model.AuctionInvoice_Table;
import com.inerun.dropinsta.model.AuctionItem;
import com.inerun.dropinsta.model.AuctionItem_Table;
import com.inerun.dropinsta.model.WhPhysicalStock;
import com.inerun.dropinsta.model.WhPhysicalStock_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by vinay on 20/12/16.
 */

public class WhAddStockParcelAdapter extends BaseAdapter implements View.OnClickListener {
    List<WhPhysicalStock> parcellist;
    Context context;
    LayoutInflater inflater;


    public WhAddStockParcelAdapter(Context context) {
        this.parcellist = new ArrayList<>();
        this.context=context;
         inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.add_parcel_delete:

                final int pos = (int) view.getTag();
//                String barcode = parcellist.get(pos).getParcel_barcode();


                SweetAlertUtil.showMessageWithCallback(context, context.getString(R.string.delete_confirm_title), context.getString(R.string.delete_confirm_msg), context.getString(R.string.yes), context.getString(R.string.no), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.i("delete", "positive");
                        String barcode = parcellist.get(pos).getParcel_barcode();

                        WhPhysicalStock whPhysicalStock = SQLite.select().from(WhPhysicalStock.class).where(WhPhysicalStock_Table.parcel_barcode.eq(barcode)).querySingle();
                        boolean deleteComplete = whPhysicalStock.delete();
//                SQLite.delete().from(WhPhysicalStock.class).where(WhPhysicalStock_Table.parcel_barcode.eq(barcode)).execute();
                        if(deleteComplete) {
                            parcellist.remove(pos);
                            notifyDataSetChanged();
                        }else{
                            SweetAlertUtil.showSweetMessageToast(context, context.getString(R.string.invoice_error_msg));
                        }
                        sweetAlertDialog.cancel();
//
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();

                    }
                });




                break;

        }

    }

    public List<WhPhysicalStock> getParcellist() {

        return parcellist;
    }


    @Override
    public int getCount() {
        return parcellist.size();
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        ViewHolder holder=null;

        if(convertview==null)
        {

          convertview=  inflater.inflate(R.layout.whaddstockparcelrow, null);
            holder= new ViewHolder(convertview);
           convertview.setTag(holder);
        }else {
            holder= (ViewHolder) convertview.getTag();
        }
        WhPhysicalStock data = parcellist.get(position);
        holder.parcelnumtxt.setText(data.getParcel_barcode());
        holder.edit.setTag(holder);
        holder.delete.setTag(position);
        holder.done.setTag(holder);
        holder.edit.setOnClickListener(new RecyclerViewHolderOnClickListener(position) {
            @Override
            public void onRecyclerClick(int pos, View view) {
                Log.d("WhAddParcelAdapter", "position = " + pos);

                                                                                                                                                                                                    ViewHolder viewHolder= (ViewHolder) view.getTag();
                String parcel = "" + viewHolder.parcelnumtxt.getText();
                viewHolder.parcelnumedt.setVisibility(View.VISIBLE);
                viewHolder.done.setVisibility(View.VISIBLE);
//                parcellist.set(viewHolder.getAdapterPosition(), parcel);

                viewHolder.parcelnumedt.setText(parcel);

                viewHolder.parcelnumtxt.setVisibility(View.GONE);
                viewHolder.edit.setVisibility(View.GONE);
                notifyDataSetChanged();

            }
        });
        holder.done.setOnClickListener(new RecyclerViewHolderOnClickListener(position) {
            @Override
            public void onRecyclerClick(int pos, View view) {
                Log.d("WhAddParcelAdapter", "position = " + pos);
////
////
////
                ViewHolder viewHolder= (ViewHolder) view.getTag();
                String parcel = "" + viewHolder.parcelnumedt.getText();
                WhPhysicalStock whPhysicalStock = new WhPhysicalStock();
                whPhysicalStock.setParcel_barcode(parcel);

                viewHolder.parcelnumedt.setVisibility(View.GONE);
                viewHolder.done.setVisibility(View.GONE);
                parcellist.set(pos, whPhysicalStock);

                viewHolder.parcelnumtxt.setText(parcel);

                viewHolder.parcelnumtxt.setVisibility(View.VISIBLE);
                viewHolder.edit.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        });
        holder.delete.setOnClickListener(this);
        return convertview;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView parcelnumtxt;
        public EditText parcelnumedt;
        public ImageView edit, delete, done;


        public ViewHolder(View view) {
            super(view);
            edit = (ImageView) view.findViewById(R.id.add_parcel_edit);
            delete = (ImageView) view.findViewById(R.id.add_parcel_delete);
            done = (ImageView) view.findViewById(R.id.add_parcel_done);
            parcelnumtxt = (TextView) view.findViewById(R.id.add_parcel_num);
            parcelnumedt = (EditText) view.findViewById(R.id.add_parcel_num_edt);
//            done.setOnClickListener();

        }


    }

    public void addParcelToList(WhPhysicalStock parcel) {
        parcellist.add(parcel);
        Collections.reverse(parcellist);
        notifyDataSetChanged();
    }

    public void addParcelToList(List<WhPhysicalStock> parcellist) {
//        parcellist.add(parcel);
        this.parcellist.clear();
        this.parcellist.addAll(parcellist);
//        Collections.reverse(parcellist);
        notifyDataSetChanged();
    }


}
