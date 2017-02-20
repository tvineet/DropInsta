package com.inerun.dropinsta.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.inerun.dropinsta.R;
import com.inerun.dropinsta.activity.SplashActivity;
import com.inerun.dropinsta.constant.UrlConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vinay on 12/11/16.
 */

public class NotiHelper {

    public static final int NOTI_INVOICE_GENERATED = 101;



    public static void processNotification(Context context, String title, String text, String data, String big_picture) {
        Intent resultIntent = new Intent(context, SplashActivity.class);
        try {


            resultIntent.putExtra(UrlConstants.KEY_DATA, data);
            resultIntent.putExtra(UrlConstants.KEY_IS_NOTIFICATION, true);
            showNotification(context, resultIntent, title, text, big_picture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNotification(Context context, Intent intent, String title, String text, String bigpicture) {
//        ImageLoader imgloader = ImageLoader.getInstance();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.notification_icon);
//        if (bigpicture != null && bigpicture .length()>0) {
//            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(imgloader.loadImageSync(bigpicture)).setSummaryText(text));
//        }else
//        {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
//        }
//                        .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(text);


// Creates an explicit intent for an Activity in your app


// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SplashActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(createID(), mBuilder.build());
    }

    public static boolean isNotificationIntent(Intent intent) {

        return intent.getExtras()!=null||intent.hasExtra(UrlConstants.KEY_IS_NOTIFICATION);

    }


    public static int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }
}
