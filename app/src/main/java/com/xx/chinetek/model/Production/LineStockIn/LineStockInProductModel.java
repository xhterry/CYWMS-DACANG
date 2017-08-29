package com.xx.chinetek.model.Production.LineStockIn;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Material.BarCodeInfo;

import java.util.ArrayList;

/**
 * Created by GHOST on 2017/8/28.
 */

public class LineStockInProductModel implements Parcelable{
    private String MaterialNo;
    private String MaterialDesc;
    private Float Qty;
    private String BatchNo;
    private ArrayList<BarCodeInfo> barCodeInfos;

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

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public ArrayList<BarCodeInfo> getBarCodeInfos() {
        return barCodeInfos;
    }

    public void setBarCodeInfos(ArrayList<BarCodeInfo> barCodeInfos) {
        this.barCodeInfos = barCodeInfos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeValue(this.Qty);
        dest.writeString(this.BatchNo);
        dest.writeTypedList(this.barCodeInfos);
    }

    public LineStockInProductModel() {
    }
    public LineStockInProductModel(String materialNo,String BatchNo) {
        this.MaterialNo=materialNo;
        this.BatchNo=BatchNo;
    }

    protected LineStockInProductModel(Parcel in) {
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.Qty = (Float) in.readValue(Float.class.getClassLoader());
        this.BatchNo = in.readString();
        this.barCodeInfos = in.createTypedArrayList(BarCodeInfo.CREATOR);
    }

    public static final Creator<LineStockInProductModel> CREATOR = new Creator<LineStockInProductModel>() {
        @Override
        public LineStockInProductModel createFromParcel(Parcel source) {
            return new LineStockInProductModel(source);
        }

        @Override
        public LineStockInProductModel[] newArray(int size) {
            return new LineStockInProductModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineStockInProductModel that = (LineStockInProductModel) o;

        if (!MaterialNo.equals(that.MaterialNo)) return false;
        return BatchNo.equals(that.BatchNo);

    }

    @Override
    public int hashCode() {
        int result = MaterialNo.hashCode();
        result = 31 * result + BatchNo.hashCode();
        return result;
    }
}
