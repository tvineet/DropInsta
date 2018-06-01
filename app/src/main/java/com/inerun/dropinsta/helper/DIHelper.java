package com.inerun.dropinsta.helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.inerun.dropinsta.R;
import com.inerun.dropinsta.base.BaseActivity;
import com.inerun.dropinsta.constant.AppConstant;
import com.inerun.dropinsta.constant.UrlConstants;
import com.inerun.dropinsta.data.PickupParcelData;
import com.inerun.dropinsta.data.StatusData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by vinay on 23/11/16.
 */

public class DIHelper {
    public static boolean validateLoginData(Context context, String email, String pass) {


        if (email.length() < 1) {
//            Toast.makeText(context, R.string.error_email_field, Toast.LENGTH_LONG).show();
            ((BaseActivity) context).showSnackbar(R.string.error_email_field);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            Toast.makeText(context, R.string.error_invalid_email_field, Toast.LENGTH_LONG).show();
            ((BaseActivity) context).showSnackbar(R.string.error_invalid_email_field);
            return false;
        }
        if (pass.length() < 1) {
//            Toast.makeText(context, R.string.error_password_field, Toast.LENGTH_LONG).show();
            ((BaseActivity) context).showSnackbar(R.string.error_password_field);
            return false;
        }

        return true;
    }

    public static boolean validateReceiverName(Context context, String receiverName) {
        if (receiverName.length() < 1) {
            Toast.makeText(context, R.string.error_receiver_name, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static boolean validateComment(Context context, String comment) {
        if (comment == null || comment.trim().length() < 1) {
//            Toast.makeText(context, R.string.error_comment, Toast.LENGTH_LONG).show();
            ((BaseActivity) context).showSnackbar(R.string.error_comment);
            return false;
        }

        return true;
    }

    public static boolean validatePickupComment(Context context, String comment) {
        if (comment == null || comment.trim().length() < 1) {
            Toast.makeText(context, R.string.error_comment, Toast.LENGTH_LONG).show();
//            ((BaseActivity) context).showSnackbar(R.string.error_comment);
            return false;
        }

        return true;
    }

    public static boolean getStatus(JSONObject json) throws JSONException {
        if (json.has(UrlConstants.KEY_STATUS)) {
            return json.getBoolean(UrlConstants.KEY_STATUS);
        }
        return false;

    }

    public static String getMessage(JSONObject json) throws JSONException {
        if (json.has(UrlConstants.KEY_MESSAGE)) {
            return json.getString(UrlConstants.KEY_MESSAGE);
        }
        return "";
    }

    public static ArrayList<String> getStringArray(){
        ArrayList<String> status = new ArrayList<>();
        status.add("Delivered");
        status.add("Pending");
        status.add("Door Closed");

        return status;
    }

    public static ArrayList<StatusData> getStatusArrayList(){
        ArrayList<StatusData> status = new ArrayList<>();
        StatusData  statusData = new StatusData("0","Receiver not present.");
        status.add(statusData);
        StatusData  statusData1 = new StatusData("1","Door Closed");
        status.add(statusData1);
        StatusData statusData3 = new StatusData("","Select Option");
        status.add(statusData3);

        return status;
    }

    public static ArrayList<StatusData> getPickupStatusArrayList(){
        ArrayList<StatusData> status = new ArrayList<>();
        StatusData  statusData = new StatusData("0","Not present.");
        status.add(statusData);
        StatusData  statusData1 = new StatusData("1","Door Closed");
        status.add(statusData1);
        StatusData statusData3 = new StatusData("","Select Option");
        status.add(statusData3);

        return status;
    }

    public static String getDateTime(String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return formatter.format(calendar.getTime());

    }



    public static void playBeepSound() {
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

    }


    public static boolean validateLoginPickupAddParcel(Context context, PickupParcelData pickupParcelData) {

        if(pickupParcelData.getFname().length() < 1){
            ((BaseActivity) context).showSnackbar(R.string.error_password_field);
            return false;
        }else if(pickupParcelData.getLname().length() < 1){
            ((BaseActivity) context).showSnackbar(R.string.error_password_field);
            return false;
        }else if(pickupParcelData.getLname().length() < 1){
            ((BaseActivity) context).showSnackbar(R.string.error_password_field);
            return false;
        }else if(pickupParcelData.getLname().length() < 1){
            ((BaseActivity) context).showSnackbar(R.string.error_password_field);
            return false;
        }


//        if (email.length() < 1) {
////            Toast.makeText(context, R.string.error_email_field, Toast.LENGTH_LONG).show();
//            ((BaseActivity) context).showSnackbar(R.string.error_email_field);
//            return false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
////            Toast.makeText(context, R.string.error_invalid_email_field, Toast.LENGTH_LONG).show();
//            ((BaseActivity) context).showSnackbar(R.string.error_invalid_email_field);
//            return false;
//        }
//        if (pass.length() < 1) {
////            Toast.makeText(context, R.string.error_password_field, Toast.LENGTH_LONG).show();
//            ((BaseActivity) context).showSnackbar(R.string.error_password_field);
//            return false;
//        }

        return true;
    }

    public static Gson getGsonInstance() {
        return new GsonBuilder().setDateFormat(AppConstant.DATEFORMAT)


                .registerTypeAdapter(Float.class, new FloatTypeAdapter())
                .registerTypeAdapter(float.class, new FloatTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                .create();
    }


    static class FloatTypeAdapter extends TypeAdapter<Float> {

        @Override
        public Float read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            String stringValue = reader.nextString();
            try {
                Float value = Float.valueOf(stringValue);
                return value;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public void write(JsonWriter writer, Float value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }

    }

    static class IntegerTypeAdapter extends TypeAdapter<Integer> {
        @Override
        public void write(JsonWriter jsonWriter, Integer number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number);
        }

        @Override
        public Integer read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }

            try {
                String value = jsonReader.nextString();
                if ("".equals(value)) {
                    return 0;
                }
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {

                throw new JsonSyntaxException(e);
            }
        }
    }

    public static String[] getPaymentModeArray() {

        String[] array = {"Card","Cash","Cheque","Bank Transfer"};

        return array;
    }

    public static void setSimpleText(TextView txtview, String text)
    {
        txtview.setText(text);

    }
}
