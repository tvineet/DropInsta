package com.inerun.dropinsta.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.activity_auction.IonServiceManager;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.helper.DIHelper;
import com.victor.loading.rotate.RotateLoading;

import cn.pedant.SweetAlert.SweetAlertDialog;


abstract public class BaseFragment extends Fragment {
    View root;


    boolean showBackArrow = false;
    private SweetAlertDialog.OnSweetClickListener dailog_listener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

        }


    };
    private RotateLoading progress;
    private Toolbar toolbar;
    private RelativeLayout progress_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = null;
        inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        int layout_id = inflateView();
        if (layout_id != 0) {
            root = inflater.inflate(layout_id, null);
            try {


                toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                progress_layout = (RelativeLayout) getActivity().findViewById(R.id.progressbar_layout);
                progress = (RotateLoading) getActivity().findViewById(R.id.progressBar);
                customOnCreateView(root, inflater, container, savedInstanceState);
                Log.i("showBackArrow", "" + showBackArrow);
                if (toolbar != null) {
                    if (showBackArrow) {


                        toolbar.setNavigationIcon(R.mipmap.back_arrow_white);
                    } else {
                        toolbar.setNavigationIcon(null);
                        toolbar.setNavigationOnClickListener(backarrowclick);
                    }
                } else {
                    Log.e("BaseFragment", "toolbar is not available in activity ,cannot set back arrow");
                }

            } catch (Exception e) {
                e.printStackTrace();
                SweetAlertUtil.showDialogwithNeutralButton(getActivity(), getString(R.string.exception), e.getMessage(), getString(R.string.ok), dailog_listener).show();
            }
        } else {
            Log.i("layout error", "Layout id cannot be zero");
        }
        return root;
    }


    abstract public int inflateView();

    abstract public void customOnCreateView(View root, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception;

    protected abstract String getAnalyticsName();

    @Override
    public void onStart() {
        super.onStart();
        String name = getAnalyticsName();
        if (name != null && name.length() > 0) {
//            GAnalyticsUtil.sendScreenName(getActivity(), name);
        }

    }

    public void showLongToast(int res_id) {
        Toast.makeText(getActivity(), res_id, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(int res_id) {
        Toast.makeText(getActivity(), res_id, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    public Drawable getDrawable(int id) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            return ContextCompat.getDrawable( getActivity(), id);
//        } else {
//            return getResources().getDrawable(id);
//        }
        return ContextCompat.getDrawable(getActivity(), id);
    }

    protected void gotoActivity(Class classobj, Bundle bundle) {

        Intent intent = new Intent(getActivity(), classobj);
        if (bundle != null) {
            intent.putExtra(UrlConstants.KEY_DATA, bundle);
        }
        startActivity(intent);
    }

    protected void gotoActivityForResult(Class classobj, Bundle bundle, int requestcode) {

        Intent intent = new Intent(getActivity(), classobj);
        if (bundle != null) {
            intent.putExtra(UrlConstants.KEY_DATA, bundle);
        }
        startActivityForResult(intent, requestcode);
    }

    public void setBackground(View view, Drawable d) {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(d);
        } else {
            view.setBackground(d);
        }
    }

    public Bundle getBundleFromIntent(Activity activity) {
        Intent intent = activity.getIntent();
        if (intent.hasExtra(UrlConstants.KEY_DATA)) {
            return intent.getBundleExtra(UrlConstants.KEY_DATA);
        } else {
            return null;
        }
    }

    public View getViewById(int id) {
        return root.findViewById(id);
    }

//    public void navigateToFragment(Context context, Fragment fragment) {
//        String backStateName = fragment.getClass().getName();
//
//        FragmentManager manager = ((BaseActivity) context).getSupportFragmentManager();
//        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
//
//        if (!fragmentPopped) { //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.container, fragment);
//            ft.addToBackStack(backStateName);
//            ft.commit();
//        }
//    }

    //    public void showProgress() {
//        if (progress != null) {
//            progress.setVisibility(View.VISIBLE);
//            progress.start();
//        }
//    }
//
//    public void hideProgress() {
//        if (progress != null) {
//            progress.setVisibility(View.GONE);
//            progress.stop();
//        }
//    }
    public void showProgress() {
        if (progress != null) {
            Log.i("Base", "Progress is visible");
            progress_layout.setVisibility(View.VISIBLE);
            progress.start();
        } else {
            Log.i("Base", "Progress is null");
        }
    }

    public void hideProgress() {
        if (progress != null) {
            Log.i("Base", "Progress is invisible");
            progress_layout.setVisibility(View.GONE);
            progress.stop();
        } else {
            Log.i("Base", "Progress is null");
        }
    }

    public void navigateToFragment(Context context, Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = ((BaseActivity) context).getSupportFragmentManager();

//        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
//            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(backStateName);

            ft.commit();
        }
    }

    public void showSnackbar(int msg) {

        Snackbar snackbar = Snackbar.make((CoordinatorLayout) getActivity().findViewById(R.id.root_appbar), msg, Snackbar.LENGTH_LONG);
//        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this,  R.color.colorPrimary));
//        snackbar.getView().setBackgroundResource(R.color.colorPrimary);
        snackbar.show();
    }

    public void showSnackbar(String msg) {

        Snackbar snackbar = Snackbar.make((CoordinatorLayout) getActivity().findViewById(R.id.root_appbar), msg, Snackbar.LENGTH_LONG);
//        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this,  R.color.colorPrimary));
//        snackbar.getView().setBackgroundResource(R.color.colorPrimary);
        snackbar.show();
    }

    public boolean isStringValid(String string) {
        if (string != null && string.length() > 0) {
            return true;
        }
        return false;
    }

    public CoordinatorLayout getCordinatorLayout() {
        return (CoordinatorLayout) getActivity().findViewById(R.id.root_appbar);
    }


    public boolean isShowBackArrow() {
        return showBackArrow;
    }

    public void setShowBackArrow(boolean showBackArrow) {
        this.showBackArrow = showBackArrow;
    }

    public void setToolBarTitle(int resid) {
        if (toolbar != null) {
            toolbar.setTitle(resid);
        }
    }


    View.OnClickListener backarrowclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((BaseActivity) getActivity()).handleFragmentBackPressed();
        }
    };

    public void syncData() {
        ((BaseActivity) getActivity()).syncData();
    }

    public int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getActivity().getColor(id);
        } else {
            //noinspection deprecation
            return getActivity().getResources().getColor(id);
        }
    }

    public RotateLoading getProgress() {
        return progress;
    }


    public Gson getGsonInstance() {
        return DIHelper.getGsonInstance();
    }
    public void showException(Exception e) {
        SweetAlertUtil.showErrorMessage(getActivity(), IonServiceManager.ResponseCallback.getExceptionMessage(getActivity(), e));
        e.printStackTrace();
    }

}
