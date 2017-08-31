package com.xx.chinetek.model.Production.Wo;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by GHOST on 2017/7/18.
 */

public class WoDetailModel extends Base_Model implements Parcelable {

    private String MaterialNo;

    private String MaterialDesc;

    private Float WoQty;

    private String RowNo;

    private String RowNodel;

    private String Unit;

    private String UnitName;

    private Float ScanQty;

    private Float RemainQty;

    private Date ShipMentDate;

    private String IsspcBatch; //是否指定批次

    private String VoucherNo; //WMS工单号

    private String FromStorageLoc;

    private String FromAreaNo;

    private String FromBatchNo;

    private String FromERPAreaNO;

    private String FromERPWarehouse;

    private ArrayList<StockInfo_Model> stockInfoModels;

    public String getRowNodel() {
        return RowNodel;
    }

    public void setRowNodel(String rowNodel) {
        RowNodel = rowNodel;
    }

    public Date getShipMentDate() {
        return ShipMentDate;
    }

    public void setShipMentDate(Date shipMentDate) {
        ShipMentDate = shipMentDate;
    }

    public String getIsspcBatch() {
        return IsspcBatch;
    }

    public void setIsspcBatch(String isspcBatch) {
        IsspcBatch = isspcBatch;
    }

    public String getFromERPAreaNO() {
        return FromERPAreaNO;
    }

    public void setFromERPAreaNO(String fromERPAreaNO) {
        FromERPAreaNO = fromERPAreaNO;
    }

    public String getFromERPWarehouse() {
        return FromERPWarehouse;
    }

    public void setFromERPWarehouse(String fromERPWarehouse) {
        FromERPWarehouse = fromERPWarehouse;
    }

    public Float getRemainQty() {
        return RemainQty;
    }

    public void setRemainQty(Float remainQty) {
        RemainQty = remainQty;
    }

    public ArrayList<StockInfo_Model> getStockInfoModels() {
        return stockInfoModels;
    }

    public void setStockInfoModels(ArrayList<StockInfo_Model> stockInfoModels) {
        this.stockInfoModels = stockInfoModels;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WoDetailModel that = (WoDetailModel) o;

        return MaterialNo.equals(that.MaterialNo);

    }

    public WoDetailModel() {

    }


    public WoDetailModel(String materialNo) {
        this.MaterialNo=materialNo;
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
        dest.writeString(this.RowNodel);
        dest.writeString(this.Unit);
        dest.writeString(this.UnitName);
        dest.writeValue(this.ScanQty);
        dest.writeValue(this.RemainQty);
        dest.writeLong(this.ShipMentDate != null ? this.ShipMentDate.getTime() : -1);
        dest.writeString(this.IsspcBatch);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.FromStorageLoc);
        dest.writeString(this.FromAreaNo);
        dest.writeString(this.FromBatchNo);
        dest.writeString(this.FromERPAreaNO);
        dest.writeString(this.FromERPWarehouse);
        dest.writeTypedList(this.stockInfoModels);
    }

    protected WoDetailModel(Parcel in) {
        super(in);
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.WoQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RowNo = in.readString();
        this.RowNodel = in.readString();
        this.Unit = in.readString();
        this.UnitName = in.readString();
        this.ScanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
        long tmpShipMentDate = in.readLong();
        this.ShipMentDate = tmpShipMentDate == -1 ? null : new Date(tmpShipMentDate);
        this.IsspcBatch = in.readString();
        this.VoucherNo = in.readString();
        this.FromStorageLoc = in.readString();
        this.FromAreaNo = in.readString();
        this.FromBatchNo = in.readString();
        this.FromERPAreaNO = in.readString();
        this.FromERPWarehouse = in.readString();
        this.stockInfoModels = in.createTypedArrayList(StockInfo_Model.CREATOR);
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
