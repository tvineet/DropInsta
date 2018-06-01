package com.inerun.dropinsta.model;

import com.inerun.dropinsta.sqldb.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by vineet on 30/05/18.
 */

@Table(database = AppDatabase.class, name = "auction_invoice")
public class AuctionInvoice extends BaseModelClass {

    @Column
    private String invoice_number;
    @Column
    private int auction_id;
    @Column
    private float cash_amount;
    @Column
    private float card_amount;
    @Column
    private String card_transaction_id;
    @Column
    private float cheque_amount;
    @Column
    private String cheque_transaction_id;
    @Column
    private float bank_transfer;
    @Column
    private String bank_transfer_transaction_id;
    @Column
    private float amount_returned;
    @Column
    private String item_ids;
    @Column
    private String customer_name;
    @Column
    private String customer_email;
    @Column
    private String customer_phone;
    @Column
    private float total;
    @Column
    private float grand_total;
    @Column
    private int created_by;


    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public int getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(int auction_id) {
        this.auction_id = auction_id;
    }

    public float getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(float cash_amount) {
        this.cash_amount = cash_amount;
    }

    public float getCard_amount() {
        return card_amount;
    }

    public void setCard_amount(float card_amount) {
        this.card_amount = card_amount;
    }

    public String getCard_transaction_id() {
        return card_transaction_id;
    }

    public void setCard_transaction_id(String card_transaction_id) {
        this.card_transaction_id = card_transaction_id;
    }

    public float getCheque_amount() {
        return cheque_amount;
    }

    public void setCheque_amount(float cheque_amount) {
        this.cheque_amount = cheque_amount;
    }

    public String getCheque_transaction_id() {
        return cheque_transaction_id;
    }

    public void setCheque_transaction_id(String cheque_transaction_id) {
        this.cheque_transaction_id = cheque_transaction_id;
    }

    public float getBank_transfer() {
        return bank_transfer;
    }

    public void setBank_transfer(float bank_transfer) {
        this.bank_transfer = bank_transfer;
    }

    public String getBank_transfer_transaction_id() {
        return bank_transfer_transaction_id;
    }

    public void setBank_transfer_transaction_id(String bank_transfer_transaction_id) {
        this.bank_transfer_transaction_id = bank_transfer_transaction_id;
    }

    public float getAmount_returned() {
        return amount_returned;
    }

    public void setAmount_returned(float amount_returned) {
        this.amount_returned = amount_returned;
    }

    public String getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(String item_ids) {
        this.item_ids = item_ids;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(float grand_total) {
        this.grand_total = grand_total;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }
}
