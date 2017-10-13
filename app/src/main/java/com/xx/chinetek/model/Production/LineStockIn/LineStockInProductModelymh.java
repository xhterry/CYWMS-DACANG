package com.xx.chinetek.model.Production.LineStockIn;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import java.util.ArrayList;

/**
 * Created by GHOST on 2017/8/28.
 */

public class LineStockInProductModelymh implements Parcelable{
    private String MaterialNo;
    private String MaterialDesc;
    private Float Qty;
    private String BatchNo;
    private ArrayList<StockInfo_Model> barCodeInfos;

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

    public ArrayList<StockInfo_Model> getBarCodeInfos() {
        return barCodeInfos;
    }

    public void setBarCodeInfos(ArrayList<StockInfo_Model> barCodeInfos) {
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

    public LineStockInProductModelymh() {
    }
    public LineStockInProductModelymh(String materialNo, String BatchNo) {
        this.MaterialNo=materialNo;
        this.BatchNo=BatchNo;
    }

    protected LineStockInProductModelymh(Parcel in) {
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.Qty = (Float) in.readValue(Float.class.getClassLoader());
        this.BatchNo = in.readString();
        this.barCodeInfos = in.createTypedArrayList(StockInfo_Model.CREATOR);
    }

    public static final Creator<LineStockInProductModelymh> CREATOR = new Creator<LineStockInProductModelymh>() {
        @Override
        public LineStockInProductModelymh createFromParcel(Parcel source) {
            return new LineStockInProductModelymh(source);
        }

        @Override
        public LineStockInProductModelymh[] newArray(int size) {
            return new LineStockInProductModelymh[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineStockInProductModelymh that = (LineStockInProductModelymh) o;

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
