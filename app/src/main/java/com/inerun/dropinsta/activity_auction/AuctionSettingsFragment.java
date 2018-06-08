package com.inerun.dropinsta.activity_auction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.inerun.dropinsta.Exception.PrinterExceptions;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.AuctionBaseFragment;
import com.inerun.dropinsta.base.SweetAlertUtil;
import com.inerun.dropinsta.printer.AppConstant;
import com.inerun.dropinsta.printer.DiscoveryActivity;
import com.inerun.dropinsta.printer.EPOSHelper;
import com.inerun.dropinsta.printer.PrinterManager;
import com.inerun.dropinsta.printer.SpnModelsItem;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by vinay on 25/05/18.
 */

public class AuctionSettingsFragment extends AuctionBaseFragment implements View.OnClickListener {

    private static final int DISCOVERY_REQUEST = 101;
    private static final int MY_PERMISSIONS_REQUEST_SYS_ALERT = 102;
    private EditText printeraddress;
    private Button printerscan;
    private Button printertest;
    PrinterManager printerManager;
    private MaterialBetterSpinner mSpnSeries, mSpnLang, mSpnPin, mSpnPulse;
    private Button savesettings;

    public static AuctionSettingsFragment newInstance() {

        Bundle args = new Bundle();

        AuctionSettingsFragment fragment = new AuctionSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.auction_settings;
    }

    @Override
    protected void initView(View rootview) throws IonServiceManager.InvalidParametersException {


        printeraddress = rootview.findViewById(R.id.settings_url_edt);
        printerscan = rootview.findViewById(R.id.settings_printer_scan);
        printertest = rootview.findViewById(R.id.settings_printer_test);
        savesettings = rootview.findViewById(R.id.save_settings);
        mSpnSeries = (MaterialBetterSpinner) rootview.findViewById(R.id.spnModel);
        mSpnLang = (MaterialBetterSpinner) rootview.findViewById(R.id.spnLang);
        mSpnPin = (MaterialBetterSpinner) rootview.findViewById(R.id.spnPin);
        mSpnPulse = (MaterialBetterSpinner) rootview.findViewById(R.id.spnPulse);
        printerManager = new PrinterManager(getActivity());
        printerscan.setOnClickListener(this);
        savesettings.setOnClickListener(this);
        printertest.setOnClickListener(this);

        setHasOptionsMenu(true);
        setData();
        setSavedSettings();
    }

    private void setData() {
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(getActivity(), R.layout.spinner_item);
        seriesAdapter.setDropDownViewResource(R.layout.spinner_layout);
        seriesAdapter.addAll(EPOSHelper.getPrinterSeriesList(getActivity()));
        mSpnSeries.setAdapter(seriesAdapter);


        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(getActivity(), R.layout.spinner_item);
        langAdapter.setDropDownViewResource(R.layout.spinner_layout);
        langAdapter.addAll(EPOSHelper.getPrinterLangList(getActivity()));
        mSpnLang.setAdapter(langAdapter);



        //new code

        ArrayAdapter<SpnModelsItem> pinAdapter = new ArrayAdapter<SpnModelsItem>(getActivity(), R.layout.spinner_item);
        pinAdapter.setDropDownViewResource(R.layout.spinner_layout);


        pinAdapter.addAll(EPOSHelper.getPrinterPinList(getActivity()));

        mSpnPin.setAdapter(pinAdapter);



        ArrayAdapter<SpnModelsItem> pulseAdapter = new ArrayAdapter<SpnModelsItem>(getActivity(), R.layout.spinner_item);
        pulseAdapter.setDropDownViewResource(R.layout.spinner_layout);
        pulseAdapter.addAll(EPOSHelper.getPrinterPulseList(getActivity()));


        mSpnPulse.setAdapter(pulseAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_printer_scan:


//                checkAppPermission();
                startActivityForResult(new Intent(getActivity(), DiscoveryActivity.class), DISCOVERY_REQUEST);
                break;
            case R.id.settings_printer_test:

                String data = "{\"date\":\"01.04.2016\",\"order_num\":\"1330\",\"date_time\":\"11:35\",\"cashier_name\":\"admin\",\"customer_name\":\"Amit Rathi\",\"customer_email\":\"rathi_kota@yahoo.co.in\",\"customer_phone\":\"9988998899\",\"register_name\":\"POS\",\"vat1\":\"20%\",\"vat1_amount\":\"19,50\",\"subtotal\":\"100,00\",\"subtotal_net\":\"80,50\",\"total_incl_vat\":\"900\",\"cash_given\":\"1,100\",\"cash_received\":\"100\",\"payment_method\":\"Cheque\",\"sales\":\"100,00\",\"change\":\"0,00\",\"num_items\":2,\"barcode\":\"1330\",\"items\":[{\"qty\":\"123456789011\",\"desc\":\"Mobile Phone Etc...\",\"price\":\"70,00\",\"amount\":\"99999\"},{\"qty\":\"120987654321\",\"desc\":\"DSLR Camera\",\"price\":\"30,00\",\"amount\":\"99900\"}],\"outlet\":\"Friedrichstadt\",\"email\":\"enquiries@tigmoo.com\",\"tel\":\"09712 69390 \",\"fax\":\"\",\"website\":\"www.tigmooshopnship.com\",\"address\":\" Plot 6392 Dundudza Chididza Road \nLongacres,\",\"city\":\"Lusaka\"}";

//old
//                printerService.addPrintJob(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
                try {
                    printerManager.addRequestToPrinter(AppConstant.Keys.TYPE_PRINT_RECEIPT_ONLY, data);
                } catch (PrinterExceptions printerExceptions) {
                    printerExceptions.printStackTrace();
                    SweetAlertUtil.showAlertDialog(getActivity(), printerExceptions.toString());


                }


                break;
            case R.id.save_settings:


                SweetAlertUtil.showAlertDialogwithListener(getActivity(), R.string.title_settings, R.string.title_dialog_save_msg, R.string.yes, R.string.no, positivedialog_listener, canceldialog_listener).show();

                break;
        }
    }


    SweetAlertDialog.OnSweetClickListener positivedialog_listener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

            sweetAlertDialog.dismiss();
            String target = "" + printeraddress.getText();




            int printerseries = getConstantFromSpinnerListByName(EPOSHelper.getPrinterSeriesList(getActivity()), "" + mSpnSeries.getText());
            int printerlang = getConstantFromSpinnerListByName(EPOSHelper.getPrinterLangList(getActivity()), "" + mSpnLang.getText());
            int printerpulse = getConstantFromSpinnerListByName(EPOSHelper.getPrinterPulseList(getActivity()), "" + mSpnPulse.getText());
            int printerpin = getConstantFromSpinnerListByName(EPOSHelper.getPrinterPinList(getActivity()), "" + mSpnPin.getText());

            if (target!=null&&target.length()>0&&printerseries!=-1&&printerlang!=-1&&printerpulse!=-1&&printerpin!=-1) {
                try {
                    printerManager.restartPrinterService();
//                            readerManager.restartReaderService();
                } catch (Exception printerExceptions) {
                    printerExceptions.printStackTrace();
                }

                EPOSHelper.savePrintSettings(getActivity(), target, printerseries, printerlang, printerpulse, printerpin);

            }else
            {

                SweetAlertUtil.showErrorMessage(getActivity(),getString(R.string.dialog_printer_settings_error_msg));
            }

//                        SettingsActivity.this.finish();


        }
    };

    private int getConstantFromSpinnerListByName(ArrayList<SpnModelsItem> list, String name) {

        if(name!=null&&name.length()>0) {
            int index = list.indexOf(new SpnModelsItem(name, -2) {
                @Override
                public boolean equals(Object obj) {
                    return getmModelName().equals(((SpnModelsItem) obj).getmModelName());
                }
            });
            return list.get(index).getModelConstant();
        }else
        {
            return -1;
        }

    }

    SweetAlertDialog.OnSweetClickListener canceldialog_listener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismiss();

        }
    };

    private void checkAppPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?


                // No explanation needed, we can request the permission.

                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_SYS_ALERT);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            } else {
                startActivityForResult(new Intent(getActivity(), DiscoveryActivity.class), DISCOVERY_REQUEST);
            }
        } else {
            startActivityForResult(new Intent(getActivity(), DiscoveryActivity.class), DISCOVERY_REQUEST);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SYS_ALERT:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(getActivity(), DiscoveryActivity.class), DISCOVERY_REQUEST);
                    // permission was granted, yay! Do the
                    //related task you need to do.

                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

//

                    SweetAlertUtil.showDialogwithNeutralButton(getActivity(), R.string.title_settings, R.string.dialog_permission_denied_error_msg, R.string.ok, new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }, 11).show();

                    // permission denied, boo! Disable the
                    // force it.
                }


                break;


            // other 'case' lines to check for other
            // permissions this app might request
        }
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
    public void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == DISCOVERY_REQUEST && data != null && resultCode == Activity.RESULT_OK)) {
            if (data.hasExtra(getString(R.string.title_target))) {
                String target = data.getStringExtra(getString(R.string.title_target));
                if (target != null) {

                    printeraddress.setText(target);
                }
            }
        }

    }


    private void setSavedSettings() {
        if (EPOSHelper.isSettingsSaved(getActivity())) {
            int pos = 0;
            pos = EPOSHelper.getPrinterLangList(getActivity()).indexOf(new SpnModelsItem("", AppConstant.SharedPref.getPrinterLang(getActivity())));

            if (pos != -1) {
                mSpnLang.setText(EPOSHelper.getPrinterLangList(getActivity()).get(pos).getmModelName());
                pos = 0;
            }

            pos = EPOSHelper.getPrinterPinList(getActivity()).indexOf((new SpnModelsItem("", AppConstant.SharedPref.getPrinterPin(getActivity()))));

            if (pos != -1) {
                mSpnPin.setText(EPOSHelper.getPrinterPinList(getActivity()).get(pos).getmModelName());
                pos = 0;
            }

            pos = EPOSHelper.getPrinterPulseList(getActivity()).indexOf((new SpnModelsItem("", AppConstant.SharedPref.getPrinterPulse(getActivity()))));

            if (pos != -1) {
                mSpnPulse.setText(EPOSHelper.getPrinterPulseList(getActivity()).get(pos).getmModelName());
                pos = 0;
            }

            pos = EPOSHelper.getPrinterSeriesList(getActivity()).indexOf((new SpnModelsItem("", AppConstant.SharedPref.getPrinterSeries(getActivity()))));

            if (pos != -1) {
                mSpnSeries.setText(EPOSHelper.getPrinterSeriesList (getActivity()).get(pos).getmModelName());
                pos = 0;
            }

            String target = "" + AppConstant.SharedPref.getPrinterTarget(getActivity());


            if (target.length() != 0) {
                printeraddress.setText(target);
            }


        }
    }

}
