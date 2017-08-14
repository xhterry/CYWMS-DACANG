package com.xx.chinetek.model.WMS.Inventory;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by GHOST on 2017/7/6.
 */

public class Barcode_Model implements Parcelable{
    public String StrongHoldCode ;
    public String StrongHoldName ;
    public int MaterialNoID ;
    public String Unit;
    private String BatchNo;
    private Float Qty;
    private String MaterialNo;
    private String MaterialDesc;
    private String BarCode;
    private String CHECKNO ;
    private int AREAID ;
    private String areano;
    private String SerialNo;
    private String IP;
    private String LabelMark;
    private String ErpVoucherNo;
    private String SupPrdBatch;
    private Date ProductDate;
    private Date EDate;
    private int BarcodeType;
    private String Creater;
    private int STATUS;
    private String warehousename;
    private String warehouseno ;
    private String AllIn  ; //用来判断是在库存还是条码表1库存，0条码

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }

    public int getBarcodeType() {
        return BarcodeType;
    }

    public void setBarcodeType(int barcodeType) {
        BarcodeType = barcodeType;
    }

    public String getAreano() {
        return areano;
    }

    public void setAreano(String areano) {
        this.areano = areano;
    }

    public Date getProductDate() {
        return ProductDate;
    }

    public void setProductDate(Date productDate) {
        ProductDate = productDate;
    }

    public Date getEDate() {
        return EDate;
    }

    public void setEDate(Date EDate) {
        this.EDate = EDate;
    }

    public String getSupPrdBatch() {
        return SupPrdBatch;
    }

    public void setSupPrdBatch(String supPrdBatch) {
        SupPrdBatch = supPrdBatch;
    }

    public String getErpVoucherNo() {
        return ErpVoucherNo;
    }

    public void setErpVoucherNo(String erpVoucherNo) {
        ErpVoucherNo = erpVoucherNo;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public String getWarehouseno() {
        return warehouseno;
    }

    public void setWarehouseno(String warehouseno) {
        this.warehouseno = warehouseno;
    }

    public String getAllIn() {
        return AllIn;
    }

    public void setAllIn(String allIn) {
        AllIn = allIn;
    }

    public String getLabelMark() {
        return LabelMark;
    }

    public void setLabelMark(String labelMark) {
        LabelMark = labelMark;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getStrongHoldCode() {
        return StrongHoldCode;
    }

    public void setStrongHoldCode(String strongHoldCode) {
        StrongHoldCode = strongHoldCode;
    }

    public String getStrongHoldName() {
        return StrongHoldName;
    }

    public void setStrongHoldName(String strongHoldName) {
        StrongHoldName = strongHoldName;
    }

    public int getMaterialNoID() {
        return MaterialNoID;
    }

    public void setMaterialNoID(int materialNoID) {
        MaterialNoID = materialNoID;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
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

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getCHECKNO() {
        return CHECKNO;
    }

    public void setCHECKNO(String CHECKNO) {
        this.CHECKNO = CHECKNO;
    }

    public int getAREAID() {
        return AREAID;
    }

    public void setAREAID(int AREAID) {
        this.AREAID = AREAID;
    }

    public Barcode_Model() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.StrongHoldCode);
        dest.writeString(this.StrongHoldName);
        dest.writeInt(this.MaterialNoID);
        dest.writeString(this.Unit);
        dest.writeString(this.BatchNo);
        dest.writeValue(this.Qty);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeString(this.BarCode);
        dest.writeString(this.CHECKNO);
        dest.writeInt(this.AREAID);
        dest.writeString(this.areano);
        dest.writeString(this.SerialNo);
        dest.writeString(this.IP);
        dest.writeString(this.LabelMark);
        dest.writeString(this.ErpVoucherNo);
        dest.writeString(this.SupPrdBatch);
        dest.writeLong(this.ProductDate != null ? this.ProductDate.getTime() : -1);
        dest.writeLong(this.EDate != null ? this.EDate.getTime() : -1);
        dest.writeInt(this.BarcodeType);
        dest.writeString(this.Creater);
        dest.writeInt(this.STATUS);
        dest.writeString(this.warehousename);
        dest.writeString(this.warehouseno);
        dest.writeString(this.AllIn);
    }

    protected Barcode_Model(Parcel in) {
        this.StrongHoldCode = in.readString();
        this.StrongHoldName = in.readString();
        this.MaterialNoID = in.readInt();
        this.Unit = in.readString();
        this.BatchNo = in.readString();
        this.Qty = (Float) in.readValue(Float.class.getClassLoader());
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.BarCode = in.readString();
        this.CHECKNO = in.readString();
        this.AREAID = in.readInt();
        this.areano = in.readString();
        this.SerialNo = in.readString();
        this.IP = in.readString();
        this.LabelMark = in.readString();
        this.ErpVoucherNo = in.readString();
        this.SupPrdBatch = in.readString();
        long tmpProductDate = in.readLong();
        this.ProductDate = tmpProductDate == -1 ? null : new Date(tmpProductDate);
        long tmpEDate = in.readLong();
        this.EDate = tmpEDate == -1 ? null : new Date(tmpEDate);
        this.BarcodeType = in.readInt();
        this.Creater = in.readString();
        this.STATUS = in.readInt();
        this.warehousename = in.readString();
        this.warehouseno = in.readString();
        this.AllIn = in.readString();
    }

    public static final Creator<Barcode_Model> CREATOR = new Creator<Barcode_Model>() {
        @Override
        public Barcode_Model createFromParcel(Parcel source) {
            return new Barcode_Model(source);
        }

        @Override
        public Barcode_Model[] newArray(int size) {
            return new Barcode_Model[size];
        }
    };
}
