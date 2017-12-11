package com.example.android.iorder.util;



// Lớp chứa các thông tin về domain của webservice
// TODO thông tin webservice
public class MyDomain {
    public static final String URL_SERVICE = "http://f10-preview.atspace.me/webservice.com/";
    //private static final String URL_SERVICE = "http://172.16.3.70:81/iOrder/";
    public static final String URL_DRINK_TYPES = URL_SERVICE + "getData.php?table=drinktypes";
    public static final String URL_DRINKS= URL_SERVICE + "getData.php?table=drinks";
    public static final String URL_TABLES = URL_SERVICE + "getData.php?table=tables";
    public static final String URL_SUBMIT = URL_SERVICE + "submit.php";
}
