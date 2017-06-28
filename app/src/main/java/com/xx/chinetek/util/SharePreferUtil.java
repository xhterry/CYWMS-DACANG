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
            URLModel.IPAdress=sharedPreferences.getString("IPAdress", "");
            URLModel.Port=sharedPreferences.getInt("Port", 80);
            RequestHandler.SOCKET_TIMEOUT=sharedPreferences.getInt("TimeOut", 20000);
        }
    }

    public static void SetShare(Context context, String IPAdress, Integer Port, Integer TimeOut){
        SharedPreferences sharedPreferences=context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("IPAdress",IPAdress);
        edit.putInt("Port",Port);
        edit.putInt("TimeOut",TimeOut);
        edit.commit();
        URLModel.IPAdress=IPAdress;
        URLModel.Port=Port;
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
