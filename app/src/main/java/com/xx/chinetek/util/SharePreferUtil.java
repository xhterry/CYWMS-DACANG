package com.xx.chinetek.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.util.Network.RequestHandler;

import java.lang.reflect.Type;


/**
 * Created by GHOST on 2017/2/3.
 */

public class SharePreferUtil {

    public static void ReadShare(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        if(sharedPreferences!=null) {
            URLModel.IPAdress=sharedPreferences.getString("IPAdress", "wms.beukay.com");
            URLModel.Port=sharedPreferences.getInt("Port", 9000);
//            URLModel.IPAdress=sharedPreferences.getString("IPAdress", "wmstest.beukay.com");
//            URLModel.Port=sharedPreferences.getInt("Port", 9010);
            URLModel.PrintIP=sharedPreferences.getString("PrintIP", "");
            URLModel.ElecIP=sharedPreferences.getString("ElecIP", "");
            URLModel.isWMS=sharedPreferences.getBoolean("isWMS", true);
            RequestHandler.SOCKET_TIMEOUT=sharedPreferences.getInt("TimeOut", 20000);
        }
    }

    public static void SetShare(Context context, String IPAdress,String PrintIP,String ElecIP, Integer Port, Integer TimeOut,Boolean isWMS){
        SharedPreferences sharedPreferences=context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("IPAdress",IPAdress);
        edit.putString("PrintIP",PrintIP);
        edit.putString("ElecIP",ElecIP);
        edit.putInt("Port",Port);
        edit.putInt("TimeOut",TimeOut);
        edit.putBoolean("isWMS",isWMS);
        edit.commit();
        URLModel.IPAdress=IPAdress;
        URLModel.PrintIP=PrintIP;
        URLModel.ElecIP=ElecIP;
        URLModel.Port=Port;
        URLModel.isWMS=isWMS;
        RequestHandler.SOCKET_TIMEOUT=TimeOut;
    }

    public static void ReadUserShare(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("User", Context.MODE_PRIVATE);
        if(sharedPreferences!=null) {
            Gson gson = new Gson();
            Type type = new TypeToken<UerInfo>(){}.getType();
            BaseApplication.userInfo= gson.fromJson(sharedPreferences.getString("User", ""), type);
        }
    }

    public static void SetUserShare(Context context, UerInfo user){
        SharedPreferences sharedPreferences=context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        Gson gson=new Gson();
        Type type = new TypeToken<UerInfo>() {}.getType();
        edit.putString("User",gson.toJson(user,type));
        edit.commit();
        BaseApplication.userInfo=user;
    }
}
