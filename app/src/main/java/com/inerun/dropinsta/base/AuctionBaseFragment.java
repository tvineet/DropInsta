package com.inerun.dropinsta.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inerun.dropinsta.DropInsta;
import com.inerun.dropinsta.activity_auction.IonServiceManager;
import com.inerun.dropinsta.helper.DIHelper;

/**
 * Created by vinay on 09/09/17.
 */

abstract public class AuctionBaseFragment extends Fragment {


    public View rootview;
    public IonServiceManager serviceManager;
    AuctionBaseActivity currentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        initCurrentActivityContext(context);
    }

    protected  void initCurrentActivityContext(Context context){
        currentActivity=(AuctionBaseActivity) context;
        if(getApp()!=null) {
            getApp().setCurrentActivity((AuctionBaseActivity) context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int layout_id = initLayout();
        serviceManager = getApp().ionserviceManager;
        rootview = inflater.inflate(layout_id, container, false);

        return rootview;
    }

    protected abstract int initLayout();

    protected abstract void initView(View rootview) throws IonServiceManager.InvalidParametersException;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       processActivityCreated();
    }

    public void processActivityCreated() {
        try {
            initView(rootview);
        } catch (IonServiceManager.InvalidParametersException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        StateSaver.saveInstanceState(this, outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("BaseF","onResume");
    }

    public void setActionBarTitle(String title) {
        activity().setToolBarTitle(title);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getApp().setCurrentActivity(null);

    }

    public AuctionBaseActivity activity() {
//        return (BaseActivity) getActivity();
        return currentActivity;
    }

//    public FcsPref getPrefs() {
//
//        return getApp().fcsPref;
//    }




    public void pushFragment(Fragment fragment) {
        activity().pushFragment(fragment);
    }

    public void popFragment() {
        try {
            activity().popFragment();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void popMultipleFragments(int num) {
        activity().popMultipleFragments(num);

    }

    public void replaceCurrentFragment(Fragment fragment) {
        activity().replaceCurrentFragment(fragment);
    }


    public void showLongToast(String msg) {
        Toast.makeText(activity(), msg, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String msg) {
        Toast.makeText(activity(), msg, Toast.LENGTH_SHORT).show();
    }

    public IonServiceManager serviceManager() {

        return getApp().ionserviceManager;
    }

    public View getRootview() {
        return rootview;
    }


    public boolean isValidString(String string) {
       return activity().isValidString(string);

    }

    /**
     * function to get Application Instance in Activities
     *
     * @return FCSApplication Instance
     */

    public DropInsta getApp() {

         return activity().getApp();
    }


//    public void showOkAlert(int title, int message, int button,
//                            Alert.ConnectionDialogClickListener dialogclicklistener, int dialog_id) {
//        AlertUtil.showDialogwithNeutralButton(activity(), title, message, button, dialogclicklistener, dialog_id).show();
//    }
//
//    public boolean isTokenPresent() {
//
//        return getPrefs().hasToken();
//
//
//    }

    public Gson getGsonInstance() {
        return DIHelper.getGsonInstance();
    }

//    public void showEmptyDataFragment(final String errorstring) {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                replaceCurrentFragment(ErrorFragment.newInstance(errorstring));
//            }
//        }, 0);
//
//
//    }


    public void refreshFragment(){

    }


    public void showException(Exception e) {
        SweetAlertUtil.showErrorMessage(activity(), IonServiceManager.ResponseCallback.getExceptionMessage(activity(), e));
        e.printStackTrace();
    }




}
