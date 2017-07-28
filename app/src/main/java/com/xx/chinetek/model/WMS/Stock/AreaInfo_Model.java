package com.xx.chinetek.model.WMS.Stock;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GHOST on 2017/1/20.
 */

public class AreaInfo_Model implements Parcelable {

    public String WarehouseNo ;
    public String HouseNo;
    private String AreaNo;
    private int WarehouseID;
    private int HouseID;
    private int ID;
    private int IsQuality;

    public int getIsQuality() {
        return IsQuality;
    }

    public void setIsQuality(int isQuality) {
        IsQuality = isQuality;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHouseID() {
        return HouseID;
    }

    public void setHouseID(int houseID) {
        HouseID = houseID;
    }

    public int getWarehouseID() {
        return WarehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        WarehouseID = warehouseID;
    }

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String areaNo) {
        AreaNo = areaNo;
    }

    public String getHouseNo() {
        return HouseNo;
    }

    public void setHouseNo(String houseNo) {
        HouseNo = houseNo;
    }

    public String getWarehouseNo() {
        return WarehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.WarehouseNo);
        dest.writeString(this.HouseNo);
        dest.writeString(this.AreaNo);
        dest.writeInt(this.WarehouseID);
        dest.writeInt(this.HouseID);
        dest.writeInt(this.ID);
        dest.writeInt(this.IsQuality);
    }

    public AreaInfo_Model() {
    }

    protected AreaInfo_Model(Parcel in) {
        this.WarehouseNo = in.readString();
        this.HouseNo = in.readString();
        this.AreaNo = in.readString();
        this.WarehouseID = in.readInt();
        this.HouseID = in.readInt();
        this.ID = in.readInt();
        this.IsQuality = in.readInt();
    }

    public static final Parcelable.Creator<AreaInfo_Model> CREATOR = new Parcelable.Creator<AreaInfo_Model>() {
        @Override
        public AreaInfo_Model createFromParcel(Parcel source) {
            return new AreaInfo_Model(source);
        }

        @Override
        public AreaInfo_Model[] newArray(int size) {
            return new AreaInfo_Model[size];
        }
    };
}
