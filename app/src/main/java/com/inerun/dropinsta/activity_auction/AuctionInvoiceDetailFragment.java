package com.inerun.dropinsta.activity_auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inerun.dropinsta.Exception.PrinterExceptions;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.adapter.InvoiceDetailItemsAdapter;
import com.inerun.dropinsta.adapter.InvoiceDetailItemsAdapter_New;
import com.inerun.dropinsta.base.AuctionBaseFragment;
import com.inerun.dropinsta.base.SweetAlertUtil;
import com.inerun.dropinsta.constant.Utils;
import com.inerun.dropinsta.helper.ExpandableHeightListView;
import com.inerun.dropinsta.model.AuctionInvoice;
import com.inerun.dropinsta.model.AuctionInvoice_Table;
import com.inerun.dropinsta.model.AuctionItem;
import com.inerun.dropinsta.model.AuctionItem_Table;
import com.inerun.dropinsta.printer.AppConstant;
import com.inerun.dropinsta.printer.PrinterManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONException;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by vineet on 28/05/18.
 */

public class AuctionInvoiceDetailFragment extends AuctionBaseFragment implements View.OnClickListener {

    private TextView invoice_number, date, cashier, name, email, mobile, grandtotal, paymenttype, cashgiven, cashrecieved;
    private AuctionInvoice invoice;
    String invoice_id;
//    private RecyclerView itemsrecycler;
    private ExpandableHeightListView itemsrecycler;
    private List<AuctionItem> itemList;
    private InvoiceDetailItemsAdapter_New itemsadapter;
    private PrinterManager printerManager;
    private Button printinvoice;
    private View cashrecievedlayout, cashgivenlayout;

    public static AuctionInvoiceDetailFragment newInstance(String invoice_id) {

        Bundle args = new Bundle();

        AuctionInvoiceDetailFragment fragment = new AuctionInvoiceDetailFragment();
        args.putString(IonServiceManager.KEYS.DATA, invoice_id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int initLayout() {
        return R.layout.auction_invoice_detail_fragment;
    }

    @Override
    protected void initView(View rootview) throws IonServiceManager.InvalidParametersException {

        printerManager = new PrinterManager(getActivity());
        invoice_id = getArguments().getString(IonServiceManager.KEYS.DATA);
        invoice_number = rootview.findViewById(R.id.invoice_value);
        date = rootview.findViewById(R.id.date_value);
        cashier = rootview.findViewById(R.id.cashier_value);
        name = rootview.findViewById(R.id.customer_name_value);
        email = rootview.findViewById(R.id.customer_email_value);
        mobile = rootview.findViewById(R.id.customer_phone_value);
        grandtotal = rootview.findViewById(R.id.grandtotal_value);
        paymenttype = rootview.findViewById(R.id.payment_value);
        cashgiven = rootview.findViewById(R.id.cash_given_value);
        cashrecieved = rootview.findViewById(R.id.cash_recieved_value);
        printinvoice = rootview.findViewById(R.id.print_invoice_detail);
        itemsrecycler = (ExpandableHeightListView) rootview.findViewById(R.id.itemrecyclerView);
        cashrecievedlayout = rootview.findViewById(R.id.cash_recieved_layout);
        cashgivenlayout = rootview.findViewById(R.id.cash_given_layout);
        printinvoice.setOnClickListener(this);


        getData();
    }

    private void getData() {

        invoice = SQLite.select().from(AuctionInvoice.class).where(AuctionInvoice_Table.invoice_number.eq(invoice_id)).querySingle();
        if (invoice != null) {
            setData(invoice);
        } else {
            SweetAlertUtil.showErrorMessage(activity(), getString(R.string.invoice_detail_error));
        }
    }


    private void setData(AuctionInvoice invoice) {

        String stringDate = DateFormat.getDateTimeInstance().format(invoice.getCreated_on());
        invoice_number.setText("" + invoice.getInvoice_number());
        date.setText(stringDate);
        cashier.setText("" + Utils.getName(activity()));
        name.setText("" + invoice.getCustomer_name());
        mobile.setText("" + invoice.getCustomer_phone());
        email.setText("" + invoice.getCustomer_email());
        grandtotal.setText("" + invoice.getGrand_total());
        paymenttype.setText("" + invoice.getPaymentMethods());

        if (invoice.isCashPayment()) {
            cashgiven.setText("" + invoice.getCash_amount());


            cashrecieved.setText("" + invoice.getAmount_returned());
            cashgivenlayout.setVisibility(View.VISIBLE);
            cashrecievedlayout.setVisibility(View.VISIBLE);

        } else {

            cashgivenlayout.setVisibility(View.GONE);
            cashrecievedlayout.setVisibility(View.GONE);
        }


        itemList = SQLite.select().from(AuctionItem.class).where(AuctionItem_Table.invoice_id.eq(invoice.getInvoice_number())).queryList();

        itemsadapter = new InvoiceDetailItemsAdapter_New(activity(), itemList);
//        itemsrecycler.setLayoutManager(new LinearLayoutManager(activity()));
//        itemsrecycler.setItemAnimator(new DefaultItemAnimator());
        itemsrecycler.setAdapter(itemsadapter);


    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Do something that differs the Activity's menu here
//
//        inflater.inflate(R.menu.invoice_menu, menu);
//
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//
//
////
//                String data = "{\"date\":\"01.04.2016\",\"order_num\":\"1330\",\"date_time\":\"11:35\",\"cashier_name\":\"admin\",\"customer_name\":\"Amit Rathi\",\"customer_email\":\"rathi_kota@yahoo.co.in\",\"customer_phone\":\"9988998899\",\"register_name\":\"POS\",\"vat1\":\"20%\",\"vat1_amount\":\"19,50\",\"subtotal\":\"100,00\",\"subtotal_net\":\"80,50\",\"total_incl_vat\":\"900\",\"cash_given\":\"1,100\",\"cash_received\":\"100\",\"payment_method\":\"Cheque\",\"sales\":\"100,00\",\"change\":\"0,00\",\"num_items\":2,\"barcode\":\"1330\",\"items\":[{\"qty\":\"123456789011\",\"desc\":\"Mobile Phone Etc...\",\"price\":\"70,00\",\"amount\":\"99999\"},{\"qty\":\"120987654321\",\"desc\":\"DSLR Camera\",\"price\":\"30,00\",\"amount\":\"99900\"}],\"outlet\":\"Friedrichstadt\",\"email\":\"enquiries@tigmoo.com\",\"tel\":\"09712 69390 \",\"fax\":\"\",\"website\":\"www.tigmooshopnship.com\",\"address\":\" Plot 6392 Dundudza Chididza Road \nLongacres,\",\"city\":\"Lusaka\"}";
//
////old
////                printerService.addPrintJob(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
//                try {
//                    Log.i("Printer","Printer invoice");
//                    printerManager.addRequestToPrinter(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
//                } catch (PrinterExceptions printerExceptions) {
//                    printerExceptions.printStackTrace();
//                    SweetAlertUtil.showAlertDialog(getActivity(), printerExceptions.toString());
//
//
//                }catch (Exception printerExceptions) {
//                    printerExceptions.printStackTrace();
//                    SweetAlertUtil.showAlertDialog(getActivity(), printerExceptions.toString());
//
//
//                }
//
//
//                return false;
////        return super.onOptionsItemSelected(item);
//    }


    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_print:
//
//                String data = "{\"date\":\"01.04.2016\",\"order_num\":\"1330\",\"date_time\":\"11:35\",\"cashier_name\":\"admin\",\"customer_name\":\"Amit Rathi\",\"customer_email\":\"rathi_kota@yahoo.co.in\",\"customer_phone\":\"9988998899\",\"register_name\":\"POS\",\"vat1\":\"20%\",\"vat1_amount\":\"19,50\",\"subtotal\":\"100,00\",\"subtotal_net\":\"80,50\",\"total_incl_vat\":\"900\",\"cash_given\":\"1,100\",\"cash_received\":\"100\",\"payment_method\":\"Cheque\",\"sales\":\"100,00\",\"change\":\"0,00\",\"num_items\":2,\"barcode\":\"1330\",\"items\":[{\"qty\":\"123456789011\",\"desc\":\"Mobile Phone Etc...\",\"price\":\"70,00\",\"amount\":\"99999\"},{\"qty\":\"120987654321\",\"desc\":\"DSLR Camera\",\"price\":\"30,00\",\"amount\":\"99900\"}],\"outlet\":\"Friedrichstadt\",\"email\":\"enquiries@tigmoo.com\",\"tel\":\"09712 69390 \",\"fax\":\"\",\"website\":\"www.tigmooshopnship.com\",\"address\":\" Plot 6392 Dundudza Chididza Road \nLongacres,\",\"city\":\"Lusaka\"}";
//
////old
////                printerService.addPrintJob(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
//                try {
//                    printerManager.addRequestToPrinter(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
//                } catch (PrinterExceptions printerExceptions) {
//                    printerExceptions.printStackTrace();
//                    SweetAlertUtil.showAlertDialog(getActivity(), printerExceptions.toString());
//
//
//                }catch (Exception printerExceptions) {
//                    printerExceptions.printStackTrace();
//                    SweetAlertUtil.showAlertDialog(getActivity(), printerExceptions.toString());
//
//
//                }
//
//
//                return false;
//
//
//            default:
//                break;
//        }
//
//        return false;
//    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
//        registerReceiver(uiUpdated, new IntentFilter("LOG_UPDATED"));

//old
//        startPrinterService();
        try {
            if (printerManager.isACTIVE())
                printerManager.startPrinter();
        } catch (PrinterExceptions printerExceptions) {
            printerExceptions.printStackTrace();

        }
//        readerManager.startReader();
//        registerReceiver(broadcastReceiver, new IntentFilter(AppConstant.ReaderCommands.MA_RECEIVER));
    }


    @Override
    public void onPause() {
        super.onPause();


        try {

//            mReaderBoundService.addRequest(SharedPref.IDS.STOP_POLLING_REQUEST,null);
//            readerManager.stopReader();
//            unregisterReceiver(broadcastReceiver);
//            unregisterReceiver(uiUpdated);

            printerManager.stopPrinter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {


//        Gson gson= new Gson();
        try {
            String data = "{\"date\":\"01.04.2016\",\"order_num\":\"1330\",\"date_time\":\"11:35\",\"cashier_name\":\"admin\",\"customer_name\":\"Amit Rathi\",\"customer_email\":\"rathi_kota@yahoo.co.in\",\"customer_phone\":\"9988998899\",\"total_incl_vat\":\"900\",\"cash_given\":\"1,100\",\"cash_received\":\"100\"," +
                    "\"payment_method\":\"Cheque\",\"items\":[{\"qty\":\"123456789011\",\"desc\":\"Mobile Phone Etc...\",\"amount\":\"99999\"},{\"qty\":\"120987654321\",\"desc\":\"DSLR Camera\",\"amount\":\"99900\"}]}";
            data = preparePrintData(invoice,itemList);
            Log.i("Printer", "Printer invoice" + data);
            //                printerService.addPrintJob(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
            Log.i("Printer", "Printer invoice" + data);

            Log.i("Printer", "Printer invoice" + data);
            printerManager.addRequestToPrinter(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
        } catch (PrinterExceptions printerExceptions) {
            printerExceptions.printStackTrace();
            SweetAlertUtil.showAlertDialog(getActivity(), printerExceptions.toString());


        } catch (JSONException printerExceptions) {
            printerExceptions.printStackTrace();
            SweetAlertUtil.showAlertDialog(getActivity(), getString(R.string.invoice_detail_json_date_error));
//
//
//        }

//old

        } catch (Exception printerExceptions) {
            printerExceptions.printStackTrace();
            SweetAlertUtil.showAlertDialog(getActivity(), printerExceptions.toString());


        }
    }


}
