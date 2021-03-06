package com.inerun.dropinsta.data;

import java.io.Serializable;

/**
 * Created by vineet on 12/1/2016.
 */

public class LoginData implements Serializable {
    public static String USER_TYPE_WAREHOUSE = "11";
    public static String USER_TYPE_DELIVERY = "8";
    public static String USER_TYPE_CUSTOMER_CARE = "6";
    public static String USER_TYPE_CASHIER = "9";
    private boolean status;
    private String message;

    private String userid;
    private String usertype;
    private String name;
    private String email;
    private String phoneno;
    private String location;

    public LoginData(String userid, String usertype, String name, String email, String phoneno, String location) {
        this.userid = userid;
        this.usertype = usertype;
        this.name = name;
        this.email = email;
        this.phoneno = phoneno;
        this.location = location;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public boolean isWarehouseUser() {
        return getUsertype().equalsIgnoreCase(USER_TYPE_WAREHOUSE);
    }

    public boolean isDeliveryUser() {
        return getUsertype().equalsIgnoreCase(USER_TYPE_DELIVERY);
    }

    public boolean isCustomerCareUser() {
        return getUsertype().equalsIgnoreCase(USER_TYPE_CUSTOMER_CARE);
    }

    public boolean isCashierUser() {
        return getUsertype().equalsIgnoreCase(USER_TYPE_CASHIER);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
