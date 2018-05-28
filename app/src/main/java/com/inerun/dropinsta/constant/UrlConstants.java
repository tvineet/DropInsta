package com.inerun.dropinsta.constant;

/**
 * Created by vineet on 5/24/2016.
 */
public class UrlConstants {


//    public static final String BASE_ADDRESS = "http://192.168.1.129/dropinsta/app/";
//    public static final String BASE_ADDRESS = "http://192.168.1.117/dropinsta/app/";
//    public static final String BASE_ADDRESS = "http://148.251.29.69/dropinsta/app/";
//    public static final String BASE_ADDRESS = "http://148.251.29.69/dropinsta1/app/"; // Fist URL for Demo before implementing Pickup
//    public static final String BASE_ADDRESS = "http://148.251.29.69/courier/app/";   //Last Used URL
//    public static final String BASE_ADDRESS = "http://tigmooshopnship.com/app/";        // Live
//    public static final String BASE_ADDRESS = "https://tigmooshopnship.com/old/app/";        // OLD Live
//    public static final String BASE_ADDRESS = "https://tigmooshopnship.com/app/";        // Live
//    public static final String BASE_ADDRESS = "http://192.168.1.118/dropinsta/app/"; // Prabhat
//    public static final String BASE_ADDRESS = "http://192.168.1.113/dropinsta/app/";  // Sumit
//    public static final String BASE_ADDRESS = "http://192.168.1.105/dropinsta/app/";  // Shivani
//    public static final String BASE_ADDRESS = "http://148.251.29.69/dropinsta001/app/";  // 4Apr2018


//    public static final String BASE_ADDRESS = "http://148.251.29.69/dropinsta2/app/";
//    public static final String BASE_ADDRESS = "http://192.168.1.33/dropinsta1/app/";
//    public static final String BASE_ADDRESS = "http://148.251.29.69/sns27feb/app/";  //6Apr2018
//    public static final String BASE_ADDRESS = "http://148.251.29.69/sns13ap/app/";  //6Apr2018
//    public static final String BASE_ADDRESS = "http://192.168.1.33/sns13ap/app/";  //6Apr2018   Shabeena
    public static final String BASE_ADDRESS = "http://192.168.1.32/oldsns/app/";  //6Apr2018  sumit OLD
//    public static final String BASE_ADDRESS = "http://192.168.1.32/newsns/app/";  //6Apr2018  sumit NEW



    /* BaseURL + service_name*/
    public static final String URL_LOGIN = BASE_ADDRESS + "login";

    public static final String URL_LOGOUT = BASE_ADDRESS + "logout";
    public static final String URL_SYNC_AND_UPDATE = BASE_ADDRESS + "index";
    public static final String URL_EXCEPTION = BASE_ADDRESS + "exception";
    public static final String URL_POD_UPLOAD = BASE_ADDRESS + "uploadpod";
    public static final String URL_WH_POD_UPLOAD = BASE_ADDRESS + "uploadpodwarehouse";
    public static final String URL_SEARCH = BASE_ADDRESS + "search";
    public static final String URL_ADD = BASE_ADDRESS + "warehouse" ;
    public static final String URL_INVOICE_SEARCH = BASE_ADDRESS + "invoicesearch";
    public static final String URL_INVOICE_DELIVERY = BASE_ADDRESS + "invoicedelivery";
    public static final String URL_RETURN_PARCEL = BASE_ADDRESS + "returnparcel";

    public static final String URL_READY_INVOICE_LIST = BASE_ADDRESS + "invoicelist";
    public static final String URL_GCM = BASE_ADDRESS + "updategcim";
    public static final String URL_INVOICE_DELIVERED_CUSTOMER = BASE_ADDRESS + "invoicedelivery";

    public static final String URL_READY_FOR_EXECUTIVE_LIST = BASE_ADDRESS + "requestlist";
    public static final String URL_RETURN_PARCEL_LIST = BASE_ADDRESS + "invoicelist";
    public static final String URL_REQUEST_PARCEL_LIST = BASE_ADDRESS + "custrequestparcellist";


    //Pagination Tigmoo Demo
    public static final String BASE_ADDRESS_Demo = "http://148.251.29.69/tigmoo67/api/index/";  //6Apr2018
    public static final String URL_DEMO = BASE_ADDRESS_Demo + "productlisting";



    public static final String KEY_Password = "password";

    public static final String KEY_Usename = "email";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERTYPE = "usertype";
    public static final String KEY_PHONE = "phone";


    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE = "message";

    public static final String KEY_USER_ID = "userid";


    public static final String KEY_ANDROID_VERSION = "androidversion";
    public static final String KEY_BRAND = "brand";
    public static final String KEY_MODEL = "model";
    public static final String KEY_ANDROID_ID = "androidid";
    public static final String KEY_VERSION_CODE = "versioncode";
    public static final String KEY_TRANS_DATA = "transdata";

    public static final String KEY_UPDATE_DATA = "update_data";

    public static final String KEY_DATA = "data";
    public static final String KEY_TYPE = "type";
    public static final String KEY_EXCEPTION = "exception";


    public static final String KEY_BARCODE = "barcode";

    public static final String KEY_PHONE_NO = "phoneno";
    public static final String KEY_RECEIVER_NAME = "receiver_name";
    public static final String KEY_NATIONAL_ID = "nationalid";
    public static final String KEY_COMMENT = "comment";


	public static final String KEY_RACK_NO = "rackno";
    public static final String KEY_BIN_NO = "binno";
    public static final String KEY_PARCELS = "parcels";
    public static final String KEY_PARCEL_NO = "parcelno";

    public static final String KEY_CUSTOMER_ID = "customerid";

    public static final String KEY_androidversion = "androidversion";
    public static final String KEY_brand = "brand";
    public static final String KEY_model = "model";
    public static final String KEY_userid = "userid";
    public static final String KEY_androidid = "androidid";
    public static final String KEY_versioncode = "versioncode";
    public static final String KEY_exception = "exception";

    public static final String KEY_POD = "pod";
    public static final String KEY_INVOICE_NUMBER = "invoice_no";

    public static final String KEY_CUSTOMER_CARE_ID = "customer_care_id";
    public static final String KEY_GCM_REGID = "Gcmid";
    public static final String KEY_Title = "Title";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_Text = "Text";
    public static final String KEY_IS_NOTIFICATION = "APP_NOTIFICATION";


    public static final String KEY_EXECUTIVE_DATA = "executive_data";
    public static final String KEY_REQUEST_ID = "request_id";
    public static final String KEY_DELIVERY_STATUS_FLAG = "Delivery_flag";
    public static final String KEY_USER_TYPE = "user_type";

    public static final String KEY_PICKUP_UPDATE_DATA = "pickup_update_data";
    public static final String KEY_START = "start";
    public static final String KEY_LIMIT = "limit";
    public static final String KEY_PAGETYPE = "pagetype";

    public static final int LOGIN = 0;
    public static final int RREQUEST = 1;
}
