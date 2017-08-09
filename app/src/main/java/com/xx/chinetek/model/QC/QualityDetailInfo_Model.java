package com.xx.chinetek.model.QC;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import java.util.List;

/**
 * Created by GHOST on 2017/6/28.
 */

public class QualityDetailInfo_Model extends Base_Model  implements Parcelable{

    private String AreaNo;
    private String Unit;
    private String UnitName;
    private String WarehouseNo;
    private String BatchNo;
    private String ErpInVoucherNo;
    private String QuanUserNo;
    private int IsDel;
    private Float SampQty;
    private String MaterialNo;
    private String MaterialDesc;
    private List<StockInfo_Model> lstStock;
    private Float ScanQty;
    private Float RemainQty;
    private Float DesQty;
    private Float UnQuanQty;
    private Float QuanQty;
    private Float InSQty;
    private int NoticeStatus;
    private int QualityType;

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getWarehouseNo() {
        return WarehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getErpInVoucherNo() {
        return ErpInVoucherNo;
    }

    public void setErpInVoucherNo(String erpInVoucherNo) {
        ErpInVoucherNo = erpInVoucherNo;
    }

    public String getQuanUserNo() {
        return QuanUserNo;
    }

    public void setQuanUserNo(String quanUserNo) {
        QuanUserNo = quanUserNo;
    }

    public Float getDesQty() {
        return DesQty;
    }

    public void setDesQty(Float desQty) {
        DesQty = desQty;
    }

    public Float getUnQuanQty() {
        return UnQuanQty;
    }

    public void setUnQuanQty(Float unQuanQty) {
        UnQuanQty = unQuanQty;
    }

    public Float getQuanQty() {
        return QuanQty;
    }

    public void setQuanQty(Float quanQty) {
        QuanQty = quanQty;
    }

    public Float getInSQty() {
        return InSQty;
    }

    public void setInSQty(Float inSQty) {
        InSQty = inSQty;
    }

    public int getNoticeStatus() {
        return NoticeStatus;
    }

    public void setNoticeStatus(int noticeStatus) {
        NoticeStatus = noticeStatus;
    }

    public int getQualityType() {
        return QualityType;
    }

    public void setQualityType(int qualityType) {
        QualityType = qualityType;
    }

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
        dest.writeString(this.AreaNo);
        dest.writeString(this.Unit);
        dest.writeString(this.UnitName);
        dest.writeString(this.WarehouseNo);
        dest.writeString(this.BatchNo);
        dest.writeString(this.ErpInVoucherNo);
        dest.writeString(this.QuanUserNo);
        dest.writeInt(this.IsDel);
        dest.writeValue(this.SampQty);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeTypedList(this.lstStock);
        dest.writeValue(this.ScanQty);
        dest.writeValue(this.RemainQty);
        dest.writeValue(this.DesQty);
        dest.writeValue(this.UnQuanQty);
        dest.writeValue(this.QuanQty);
        dest.writeValue(this.InSQty);
        dest.writeInt(this.NoticeStatus);
        dest.writeInt(this.QualityType);
    }

    protected QualityDetailInfo_Model(Parcel in) {
        super(in);
        this.AreaNo = in.readString();
        this.Unit = in.readString();
        this.UnitName = in.readString();
        this.WarehouseNo = in.readString();
        this.BatchNo = in.readString();
        this.ErpInVoucherNo = in.readString();
        this.QuanUserNo = in.readString();
        this.IsDel = in.readInt();
        this.SampQty = (Float) in.readValue(Float.class.getClassLoader());
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.lstStock = in.createTypedArrayList(StockInfo_Model.CREATOR);
        this.ScanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
        this.DesQty = (Float) in.readValue(Float.class.getClassLoader());
        this.UnQuanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.QuanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.InSQty = (Float) in.readValue(Float.class.getClassLoader());
        this.NoticeStatus = in.readInt();
        this.QualityType = in.readInt();
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
