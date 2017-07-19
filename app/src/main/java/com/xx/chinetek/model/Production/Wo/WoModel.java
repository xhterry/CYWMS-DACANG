package com.xx.chinetek.model.Production.Wo;

import android.os.Parcel;

import com.xx.chinetek.model.Base_Model;

/**
 * Created by GHOST on 2017/7/18.
 */

public class WoModel extends Base_Model{

    private String VoucherNo; //WMS工单号

    private String ErpVoucherNo;//ERP工单号

    private String MaterialNo;

    private String MaterialDesc;

    private String BatchNo;

    private Float ProductQty;

    private String Unit;

    private String UnitName;

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
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

    public Float getProductQty() {
        return ProductQty;
    }

    public void setProductQty(Float productQty) {
        ProductQty = productQty;
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

    public WoModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.ErpVoucherNo);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeString(this.BatchNo);
        dest.writeValue(this.ProductQty);
        dest.writeString(this.Unit);
        dest.writeString(this.UnitName);
    }

    protected WoModel(Parcel in) {
        super(in);
        this.VoucherNo = in.readString();
        this.ErpVoucherNo = in.readString();
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.BatchNo = in.readString();
        this.ProductQty = (Float) in.readValue(Float.class.getClassLoader());
        this.Unit = in.readString();
        this.UnitName = in.readString();
    }

    public static final Creator<WoModel> CREATOR = new Creator<WoModel>() {
        @Override
        public WoModel createFromParcel(Parcel source) {
            return new WoModel(source);
        }

        @Override
        public WoModel[] newArray(int size) {
            return new WoModel[size];
        }
    };
}
