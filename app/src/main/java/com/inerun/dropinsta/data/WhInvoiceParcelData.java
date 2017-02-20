package com.inerun.dropinsta.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vineet on 2/20/2017.
 */

public class WhInvoiceParcelData implements Serializable {
    private boolean status;
    private ArrayList<Invoice> invoices;

    public WhInvoiceParcelData(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    public WhInvoiceParcelData() {
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public class Invoice implements Serializable{
        private String invoiceNo;
        private ArrayList<ParcelListingData.ParcelData> parcelDatas;

        public Invoice(String invoiceNo, ArrayList<ParcelListingData.ParcelData> parcelDatas) {
            this.invoiceNo = invoiceNo;
            this.parcelDatas = parcelDatas;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public ArrayList<ParcelListingData.ParcelData> getParcelDatas() {
            return parcelDatas;
        }
    }


}
