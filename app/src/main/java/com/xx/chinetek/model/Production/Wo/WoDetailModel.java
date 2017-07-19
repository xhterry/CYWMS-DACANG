package com.xx.chinetek.model.Production.Wo;

import android.os.Parcel;

import com.xx.chinetek.model.Base_Model;

/**
 * Created by GHOST on 2017/7/18.
 */

public class WoDetailModel extends Base_Model{

    private String MaterialNo;

    private String MaterialDesc;

    private Float WoQty;

    private String RowNo;

    private String Unit;

    private String UnitName;

    private Float ScanQty;

    private String IsSpcBatch; //是否指定批次

    private String VoucherNo; //WMS工单号

    private String ErpVoucherNo;//ERP工单号

    private String FromStorageLoc;

    private String FromAreaNo;

    private String FromBatchNo;

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

    public Float getWoQty() {
        return WoQty;
    }

    public void setWoQty(Float woQty) {
        WoQty = woQty;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

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

    public Float getScanQty() {
        return ScanQty;
    }

    public void setScanQty(Float scanQty) {
        ScanQty = scanQty;
    }

    public String getIsSpcBatch() {
        return IsSpcBatch;
    }

    public void setIsSpcBatch(String isSpcBatch) {
        IsSpcBatch = isSpcBatch;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public String getErpVoucherNo() {
        return ErpVoucherNo;
    }

    public void setErpVoucherNo(String erpVoucherNo) {
        ErpVoucherNo = erpVoucherNo;
    }

    public String getFromStorageLoc() {
        return FromStorageLoc;
    }

    public void setFromStorageLoc(String fromStorageLoc) {
        FromStorageLoc = fromStorageLoc;
    }

    public String getFromAreaNo() {
        return FromAreaNo;
    }

    public void setFromAreaNo(String fromAreaNo) {
        FromAreaNo = fromAreaNo;
    }

    public String getFromBatchNo() {
        return FromBatchNo;
    }

    public void setFromBatchNo(String fromBatchNo) {
        FromBatchNo = fromBatchNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeValue(this.WoQty);
        dest.writeString(this.RowNo);
        dest.writeString(this.Unit);
        dest.writeString(this.UnitName);
        dest.writeValue(this.ScanQty);
        dest.writeString(this.IsSpcBatch);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.ErpVoucherNo);
        dest.writeString(this.FromStorageLoc);
        dest.writeString(this.FromAreaNo);
        dest.writeString(this.FromBatchNo);
    }

    public WoDetailModel() {
    }

    protected WoDetailModel(Parcel in) {
        super(in);
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.WoQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RowNo = in.readString();
        this.Unit = in.readString();
        this.UnitName = in.readString();
        this.ScanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.IsSpcBatch = in.readString();
        this.VoucherNo = in.readString();
        this.ErpVoucherNo = in.readString();
        this.FromStorageLoc = in.readString();
        this.FromAreaNo = in.readString();
        this.FromBatchNo = in.readString();
    }

    public static final Creator<WoDetailModel> CREATOR = new Creator<WoDetailModel>() {
        @Override
        public WoDetailModel createFromParcel(Parcel source) {
            return new WoDetailModel(source);
        }

        @Override
        public WoDetailModel[] newArray(int size) {
            return new WoDetailModel[size];
        }
    };
}
