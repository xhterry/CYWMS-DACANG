package com.xx.chinetek.model.Pallet;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Stock.StockInfo_Model;

import java.util.List;

/**
 * Created by GHOST on 2017/1/19.
 */

public class PalletDetail_Model implements Parcelable {

    public PalletDetail_Model(){

    }

    private String VoucherNo;
    private String ErpVoucherNo;
    private String RowNo;
    private int VoucherType;
    private String PalletNo;
    private String MaterialNo;
    private String MaterialDesc;
    private int IsSerial;
    private String PartNo;
    private int MaterialNoID;
    private List<BarCodeInfo> lstBarCode;
    private List<StockInfo_Model> lstStockInfo;
    private String BarCode;
    private int PalletType;

    public List<StockInfo_Model> getLstStockInfo() {
        return lstStockInfo;
    }

    public void setLstStockInfo(List<StockInfo_Model> lstStockInfo) {
        this.lstStockInfo = lstStockInfo;
    }

    public int getPalletType() {
        return PalletType;
    }

    public void setPalletType(int palletType) {
        PalletType = palletType;
    }

    public List<BarCodeInfo> getLstBarCode() {
        return lstBarCode;
    }

    public void setLstBarCode(List<BarCodeInfo> lstBarCode) {
        this.lstBarCode = lstBarCode;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public int getMaterialNoID() {
        return MaterialNoID;
    }

    public void setMaterialNoID(int materialNoID) {
        MaterialNoID = materialNoID;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
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

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

    public int getVoucherType() {
        return VoucherType;
    }

    public void setVoucherType(int voucherType) {
        VoucherType = voucherType;
    }

    public int getIsSerial() {
        return IsSerial;
    }

    public void setIsSerial(int isSerial) {
        IsSerial = isSerial;
    }


    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getPalletNo() {
        return PalletNo;
    }

    public void setPalletNo(String palletNo) {
        PalletNo = palletNo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.VoucherNo);
        dest.writeString(this.ErpVoucherNo);
        dest.writeString(this.RowNo);
        dest.writeInt(this.VoucherType);
        dest.writeString(this.PalletNo);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeInt(this.IsSerial);
        dest.writeString(this.PartNo);
        dest.writeInt(this.MaterialNoID);
        dest.writeTypedList(this.lstBarCode);
        dest.writeTypedList(this.lstStockInfo);
        dest.writeString(this.BarCode);
        dest.writeInt(this.PalletType);
    }

    protected PalletDetail_Model(Parcel in) {
        this.VoucherNo = in.readString();
        this.ErpVoucherNo = in.readString();
        this.RowNo = in.readString();
        this.VoucherType = in.readInt();
        this.PalletNo = in.readString();
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.IsSerial = in.readInt();
        this.PartNo = in.readString();
        this.MaterialNoID = in.readInt();
        this.lstBarCode = in.createTypedArrayList(BarCodeInfo.CREATOR);
        this.lstStockInfo = in.createTypedArrayList(StockInfo_Model.CREATOR);
        this.BarCode = in.readString();
        this.PalletType = in.readInt();
    }

    public static final Creator<PalletDetail_Model> CREATOR = new Creator<PalletDetail_Model>() {
        @Override
        public PalletDetail_Model createFromParcel(Parcel source) {
            return new PalletDetail_Model(source);
        }

        @Override
        public PalletDetail_Model[] newArray(int size) {
            return new PalletDetail_Model[size];
        }
    };
}
