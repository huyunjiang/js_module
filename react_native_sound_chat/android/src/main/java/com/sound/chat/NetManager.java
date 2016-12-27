package com.sound.chat;

import android.content.Context;  
import android.net.ConnectivityManager;  
import android.net.NetworkInfo;  
import android.net.Proxy;
import android.net.Uri;  
import android.os.Bundle;  
import android.telephony.TelephonyManager;  
import android.text.TextUtils;  
import android.util.Log;  

/**
*网络管理类
**/
public class NetManager {
    
    /** 没有网络 */
    public static final String NETWORKTYPE_INVALID = "offline";
    /** wap网络 */
    public static final String NETWORKTYPE_WAP = "wap";
    /** 2G网络 */
    public static final String NETWORKTYPE_2G = "2g";
    /** 3G网络，或统称为快速网络 */
    public static final String NETWORKTYPE_3G = "3g";
    /** 4G网络，或统称为快速网络 */
    public static final String NETWORKTYPE_4G = "4g";
    /** wifi网络 */
    public static final String NETWORKTYPE_WIFI = "wifi";
    /** 未知网络 */
    public static final String NETWORKTYPE_UNKNOW = "unknow";

    public static String getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        String mNetWorkType="";
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost =Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost)?isFastMobileNetwork(context):NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

    private static String isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            return NETWORKTYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            return NETWORKTYPE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
            return NETWORKTYPE_4G;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            return NETWORKTYPE_UNKNOW;
            default:
            return NETWORKTYPE_UNKNOW;
        }
    }
}
