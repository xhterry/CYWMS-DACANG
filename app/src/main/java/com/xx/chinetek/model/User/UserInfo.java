package com.xx.chinetek.model.User;

import android.text.TextUtils;

/**
 * Created by GHOST on 2017/3/20.
 */

public class UserInfo {

    private String UserNo;
    private String UserName;
    private String PassWord;
    private int WarehouseID;

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public int getWarehouseID() {
        return WarehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        WarehouseID = warehouseID;
    }


    public  Boolean CheckUserAndPass(){
        if(TextUtils.isEmpty(UserNo) || TextUtils.isEmpty(PassWord)){
            return false;
        }
        return true;
    }
}
