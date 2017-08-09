package com.xx.chinetek.model.Material;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GHOST on 2016/12/13.
 */

public class SerialNo_Model implements Parcelable,Cloneable {

    public String FacMaterialNo;
    public String SerialNo ;
    public String SerialQty;
    private String MaterialNo;
    private String MaterialDesc;
    private String ABatchNo;

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getABatchNo() {
        return ABatchNo;
    }

    public void setABatchNo(String ABatchNo) {
        this.ABatchNo = ABatchNo;
    }

    public static Creator<SerialNo_Model> getCREATOR() {
        return CREATOR;
    }

    public String getFacMaterialNo() {
        return FacMaterialNo;
    }

    public void setFacMaterialNo(String facMaterialNo) {
        FacMaterialNo = facMaterialNo;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getSerialQty() {
        return SerialQty;
    }

    public void setSerialQty(String serialQty) {
        SerialQty = serialQty;
    }

    public SerialNo_Model() {
    }

    public SerialNo_Model(String SerialNo){
        this.SerialNo=SerialNo;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerialNo_Model that = (SerialNo_Model) o;

        return SerialNo.equals(that.SerialNo);

    }

    @Override
    public int hashCode() {
        return SerialNo.hashCode();
    }

    @Override
    public SerialNo_Model clone() throws CloneNotSupportedException {
        SerialNo_Model serialNo_Model=null;
        serialNo_Model=(SerialNo_Model)super.clone();
        return serialNo_Model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.FacMaterialNo);
        dest.writeString(this.SerialNo);
        dest.writeString(this.SerialQty);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeString(this.ABatchNo);
    }

    protected SerialNo_Model(Parcel in) {
        this.FacMaterialNo = in.readString();
        this.SerialNo = in.readString();
        this.SerialQty = in.readString();
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.ABatchNo = in.readString();
    }

    public static final Creator<SerialNo_Model> CREATOR = new Creator<SerialNo_Model>() {
        @Override
        public SerialNo_Model createFromParcel(Parcel source) {
            return new SerialNo_Model(source);
        }

        @Override
        public SerialNo_Model[] newArray(int size) {
            return new SerialNo_Model[size];
        }
    };
}
