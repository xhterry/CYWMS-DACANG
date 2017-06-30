package com.xx.chinetek.model.QC;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Stock.StockInfo_Model;

import java.util.List;

/**
 * Created by GHOST on 2017/6/28.
 */

public class QualityDetailInfo_Model extends Base_Model  implements Parcelable{

    private String ERPVoucherNo;
    private String AreaNo;
    private int IsDel;
    private Float SampQty;
    private String MaterialNo;
    private String MaterialDesc;
    private List<StockInfo_Model> lstStock;
    private Float ScanQty;
    private Float RemainQty;

    public List<StockInfo_Model> getLstStock() {
        return lstStock;
    }

    public void setLstStock(List<StockInfo_Model> lstStock) {
        this.lstStock = lstStock;
    }

    public Float getScanQty() {
        return ScanQty;
    }

    public void setScanQty(Float scanQty) {
        ScanQty = scanQty;
    }

    public Float getRemainQty() {
        return RemainQty;
    }

    public void setRemainQty(Float remainQty) {
        RemainQty = remainQty;
    }

    public String getERPVoucherNo() {
        return ERPVoucherNo;
    }

    public void setERPVoucherNo(String ERPVoucherNo) {
        this.ERPVoucherNo = ERPVoucherNo;
    }

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String areaNo) {
        AreaNo = areaNo;
    }

    public int getIsDel() {
        return IsDel;
    }

    public void setIsDel(int isDel) {
        IsDel = isDel;
    }

    public Float getSampQty() {
        return SampQty;
    }

    public void setSampQty(Float sampQty) {
        SampQty = sampQty;
    }

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

    public QualityDetailInfo_Model() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.ERPVoucherNo);
        dest.writeString(this.AreaNo);
        dest.writeInt(this.IsDel);
        dest.writeValue(this.SampQty);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeTypedList(this.lstStock);
        dest.writeValue(this.ScanQty);
        dest.writeValue(this.RemainQty);
    }

    protected QualityDetailInfo_Model(Parcel in) {
        super(in);
        this.ERPVoucherNo = in.readString();
        this.AreaNo = in.readString();
        this.IsDel = in.readInt();
        this.SampQty = (Float) in.readValue(Float.class.getClassLoader());
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.lstStock = in.createTypedArrayList(StockInfo_Model.CREATOR);
        this.ScanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<QualityDetailInfo_Model> CREATOR = new Creator<QualityDetailInfo_Model>() {
        @Override
        public QualityDetailInfo_Model createFromParcel(Parcel source) {
            return new QualityDetailInfo_Model(source);
        }

        @Override
        public QualityDetailInfo_Model[] newArray(int size) {
            return new QualityDetailInfo_Model[size];
        }
    };
}
