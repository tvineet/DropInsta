package com.inerun.dropinsta.model;

import java.util.List;

/**
 * Created by vinay on 06/11/17.
 */

public class SyncResponseBo {


    List<AuctionInvoice> auctioninvoicesdata;
    List<AuctionItem> auctionitemdata;

    public List<AuctionInvoice> getAuctioninvoicesdata() {
        return auctioninvoicesdata;
    }

    public List<AuctionItem> getAuctionitemdata() {
        return auctionitemdata;
    }

}
