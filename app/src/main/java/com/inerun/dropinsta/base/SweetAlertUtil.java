package com.inerun.dropinsta.base;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.inerun.dropinsta.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Class to define show alert dialog and toast.
 *
 * @author Vinay.
 */
public class SweetAlertUtil  {

    private static final long HIDE_DELAY = 1000;
    /**
     * This method is used to show alert dialog.
     *
     * @param context
     * @param alertMessage
     * @param isGoBack
     */
    // @SuppressWarnings("deprecation")
    // public static void showAlertDialog(String alertMessage) {
    // final Context context = BaseActivity.currentVisibleActivity;
    // AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    // alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    // alertDialog.setMessage(alertMessage);
    // alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    // public void onClick(DialogInterface dialog, int which) {
    // BaseActivity.currentVisibleActivity.finish();
    //
    // }
    // });
    //
    // alertDialog.show();
    // }

//    static ConnectionDialogClickListener dialoglistener;
    private int dialog_id;

    public SweetAlertUtil(int dialog_id) {
        this.dialog_id = dialog_id;
    }

    /**
     * This method is used to show alert dialog.
     *
     * @param context

     */
    // @SuppressWarnings("deprecation")
    // public static void showAlertDialogNotification(String alertMessage) {
    // final Context context = BaseActivity.currentVisibleActivity;
    // AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    // alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    // alertDialog.setMessage(alertMessage);
    // alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    // public void onClick(DialogInterface dialog, int which) {
    // return;
    // }
    // });
    //
    // alertDialog.show();
    // }
    public static void showToastMessage(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    /**
     * This method is used to show alert dialog.
     *
     * @param context
     * @param alertMessage
     */
    @SuppressWarnings("deprecation")
    public static void showAlertDialog(final Context context,
                                       String alertMessage) {
        // final Context context =
        // MobileSaverBaseActivity.currentVisibleActivity;
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.setMessage(alertMessage);
//        alertDialog.setButton("OK", new OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//
//            }
//        });


        new SweetAlertDialog(context)
                .setContentText("Here's a message!")
                .show();
    }

    @SuppressWarnings("deprecation")
    public static void showAlertDialogFinishActivity(final Context context,
                                                     String alertMessage) {
        // final Context context =
        // MobileSaverBaseActivity.currentVisibleActivity;
        SweetAlertDialog alertDialog;

        alertDialog = new SweetAlertDialog(context);


        alertDialog.setContentText(alertMessage);
        alertDialog.setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();


    }

    public static SweetAlertDialog showDialogwithNeutralButton(
            Context context, String title, String errormessage, String button,
            SweetAlertDialog.OnSweetClickListener dialogclicklistener) {
        SweetAlertDialog alert;
//        SweetAlertUtil.dialoglistener = dialogclicklistener;
        alert = new SweetAlertDialog(context);

        alert.setTitle(title);
        alert.setContentText(errormessage);
        alert.setConfirmText(button);
        alert.setConfirmClickListener(dialogclicklistener);

        return alert;

    }

    public static SweetAlertDialog showAlertDialogwithListener(
            Context context, int title, int errormessage, int postivebutton,
            int negative_button, SweetAlertDialog.OnSweetClickListener confirmlistener, SweetAlertDialog.OnSweetClickListener cancellistener) {
        SweetAlertDialog alert = null;
//        dialoglistener = listener;

        alert = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE);


        alert.setTitleText(context.getString(title))
        .setContentText(context.getString(errormessage))
        .setConfirmText(context.getString(postivebutton))
        .setConfirmClickListener(confirmlistener)
        .setCancelText(context.getString(negative_button))
        .setCancelClickListener(cancellistener);

        return alert;
    }

    public static SweetAlertDialog showDialogwithNeutralButton(
            Context context, int title, int errormessage, int button,
            SweetAlertDialog.OnSweetClickListener dialogclicklistener, int dialog_id) {
        SweetAlertDialog alert = null;
//        dialoglistener = listener;

        alert = new SweetAlertDialog(context);


        alert.setTitle(context.getString(title));
        alert.setContentText(context.getString(errormessage));
        alert.setConfirmText(context.getString(button));

        alert.setConfirmClickListener(dialogclicklistener);

        return alert;

    }

//    public static AlertDialog.Builder showInputDialogwithNeutralButton(
//            Context context, int title, int hint, int button,
//            ConnectionDialogClickListener dialogclicklistener, int dialog_id,
//            EditText input) {
//        AlertDialog.Builder alert;
//        SweetAlertUtil.dialoglistener = dialogclicklistener;
//        if (android.os.Build.VERSION.SDK_INT >= 11) {
//            alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
//                    android.R.style.Theme_Holo_Light_Dialog));
//        } else {
//            alert = new AlertDialog.Builder(new ContextThemeWrapper(context,
//                    android.R.style.Theme_Dialog));
//
//        }
//
//        alert.setTitle(context.getString(title));
//        alert.setNeutralButton(context.getString(button), new SweetAlertUtil(
//                dialog_id));
////		input.setTextColor(Color.WHITE);
//        alert.setCancelable(false);
//        // input.setHintTextColor(context.getResources().getColor(R.color.white_opacity35));
//        input.setHint(hint);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER
//                | InputType.TYPE_NUMBER_FLAG_SIGNED);
//        alert.setView(input);
//        return alert;
//
//    }

//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        switch (which) {
//            case DialogInterface.BUTTON_POSITIVE:
//                SweetAlertUtil.dialoglistener.dialogClicklistener(dialog_id,
//                        DialogInterface.BUTTON_POSITIVE);
//                break;
//            case DialogInterface.BUTTON_NEGATIVE:
//                SweetAlertUtil.dialoglistener.dialogClicklistener(dialog_id,
//                        DialogInterface.BUTTON_NEGATIVE);
//                break;
//            case DialogInterface.BUTTON_NEUTRAL:
//                SweetAlertUtil.dialoglistener.dialogClicklistener(dialog_id,
//                        DialogInterface.BUTTON_NEUTRAL);
//                break;
//
//            default:
//                break;
//        }
//
//    }

//    /**
//     * @params this interface is for tracking of dialog button int button will
//     * be result either DialogInterface.BUTTON_POSITIVE or
//     * DialogInterface.BUTTON_NEGATIVE
//     */
//
//    public interface ConnectionDialogClickListener extends SweetAlertDialog.OnSweetClickListener {
//
//        public void dialogClicklistener(int dialog_id, int button);
//    }


    public static void showAlertDialogWithBlackTheme( Context context,
                                                     String alertMessage) {
        // final Context context =
        // MobileSaverBaseActivity.currentVisibleActivity;
        SweetAlertDialog alertDialog;

            alertDialog = new SweetAlertDialog(context);




        alertDialog.setTitle(R.string.exception);
        alertDialog.setContentText(alertMessage);
        alertDialog.setConfirmText("OK")
        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }


        });

        alertDialog.show();
    }


    public static void hideDialogAutomatically(BaseActivity baseActivity, final SweetAlertDialog sweetAlertDialog) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            sweetAlertDialog.dismissWithAnimation();
            }
        },HIDE_DELAY);
    }
}