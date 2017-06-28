package com.xx.chinetek.model.User;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by GHOST on 2017/2/6.
 */

public class UerInfo extends User implements Parcelable {
    private boolean BIsAdmin;
    private String StrIsAdmin;
    private int IsOnline;
    private boolean BIsOnline;
    private String WarehouseName;
    private String StrUserType;
    private String StrUserStatus;
    private String StrSex;
    private String ReceiveWareHouseNo;
    private String ReceiveAreaNo;
    private List<UserGroupInfo> lstUserGroup;
    private List<MenuInfo> lstMenu;
    private List<WareHouseInfo> lstWarehouse;

    public String getReceiveWareHouseNo() {
        return ReceiveWareHouseNo;
    }

    public void setReceiveWareHouseNo(String receiveWareHouseNo) {
        ReceiveWareHouseNo = receiveWareHouseNo;
    }

    public String getReceiveAreaNo() {
        return ReceiveAreaNo;
    }

    public void setReceiveAreaNo(String receiveAreaNo) {
        ReceiveAreaNo = receiveAreaNo;
    }

    public boolean isBIsAdmin() {
        return BIsAdmin;
    }

    public void setBIsAdmin(boolean BIsAdmin) {
        this.BIsAdmin = BIsAdmin;
    }

    public boolean isBIsOnline() {
        return BIsOnline;
    }

    public void setBIsOnline(boolean BIsOnline) {
        this.BIsOnline = BIsOnline;
    }

    public int getIsOnline() {
        return IsOnline;
    }

    public void setIsOnline(int isOnline) {
        IsOnline = isOnline;
    }

    public List<MenuInfo> getLstMenu() {
        return lstMenu;
    }

    public void setLstMenu(List<MenuInfo> lstMenu) {
        this.lstMenu = lstMenu;
    }

    public List<UserGroupInfo> getLstUserGroup() {
        return lstUserGroup;
    }

    public void setLstUserGroup(List<UserGroupInfo> lstUserGroup) {
        this.lstUserGroup = lstUserGroup;
    }

    public List<WareHouseInfo> getLstWarehouse() {
        return lstWarehouse;
    }

    public void setLstWarehouse(List<WareHouseInfo> lstWarehouse) {
        this.lstWarehouse = lstWarehouse;
    }

    public String getStrIsAdmin() {
        return StrIsAdmin;
    }

    public void setStrIsAdmin(String strIsAdmin) {
        StrIsAdmin = strIsAdmin;
    }

    public String getStrSex() {
        return StrSex;
    }

    public void setStrSex(String strSex) {
        StrSex = strSex;
    }

    public String getStrUserStatus() {
        return StrUserStatus;
    }

    public void setStrUserStatus(String strUserStatus) {
        StrUserStatus = strUserStatus;
    }

    public String getStrUserType() {
        return StrUserType;
    }

    public void setStrUserType(String strUserType) {
        StrUserType = strUserType;
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }


    public UerInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.BIsAdmin ? (byte) 1 : (byte) 0);
        dest.writeString(this.StrIsAdmin);
        dest.writeInt(this.IsOnline);
        dest.writeByte(this.BIsOnline ? (byte) 1 : (byte) 0);
        dest.writeString(this.WarehouseName);
        dest.writeString(this.StrUserType);
        dest.writeString(this.StrUserStatus);
        dest.writeString(this.StrSex);
        dest.writeString(this.ReceiveWareHouseNo);
        dest.writeString(this.ReceiveAreaNo);
        dest.writeTypedList(this.lstUserGroup);
        dest.writeTypedList(this.lstMenu);
        dest.writeTypedList(this.lstWarehouse);
    }

    protected UerInfo(Parcel in) {
        super(in);
        this.BIsAdmin = in.readByte() != 0;
        this.StrIsAdmin = in.readString();
        this.IsOnline = in.readInt();
        this.BIsOnline = in.readByte() != 0;
        this.WarehouseName = in.readString();
        this.StrUserType = in.readString();
        this.StrUserStatus = in.readString();
        this.StrSex = in.readString();
        this.ReceiveWareHouseNo = in.readString();
        this.ReceiveAreaNo = in.readString();
        this.lstUserGroup = in.createTypedArrayList(UserGroupInfo.CREATOR);
        this.lstMenu = in.createTypedArrayList(MenuInfo.CREATOR);
        this.lstWarehouse = in.createTypedArrayList(WareHouseInfo.CREATOR);
    }

    public static final Creator<UerInfo> CREATOR = new Creator<UerInfo>() {
        @Override
        public UerInfo createFromParcel(Parcel source) {
            return new UerInfo(source);
        }

        @Override
        public UerInfo[] newArray(int size) {
            return new UerInfo[size];
        }
    };
}
