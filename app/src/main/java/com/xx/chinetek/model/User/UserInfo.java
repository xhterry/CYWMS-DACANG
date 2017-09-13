package com.xx.chinetek.model.User;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by GHOST on 2017/3/20.
 */

public class UserInfo implements Parcelable {

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo that = (UserInfo) o;

        return UserNo.equals(that.getUserNo());

    }

    public  Boolean CheckUserAndPass(){
        if(TextUtils.isEmpty(UserNo) || TextUtils.isEmpty(PassWord)){
            return false;
        }
        return true;
    }

    public UserInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserNo);
        dest.writeString(this.UserName);
        dest.writeString(this.PassWord);
        dest.writeInt(this.WarehouseID);
    }

    protected UserInfo(Parcel in) {
        this.UserNo = in.readString();
        this.UserName = in.readString();
        this.PassWord = in.readString();
        this.WarehouseID = in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
