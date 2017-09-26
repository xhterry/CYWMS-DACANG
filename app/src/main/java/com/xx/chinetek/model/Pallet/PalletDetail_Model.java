package com.xx.chinetek.model.Pallet;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GHOST on 2017/1/19.
 */

public class PalletDetail_Model extends Base_Model  implements Parcelable {

    public PalletDetail_Model(){

    }
    private String TaskNo;
    private String VoucherNo;
    private String RowNo;
    private String PalletNo;
    private String MaterialNo;
    private String MaterialDesc;
    private int IsSerial;
    private String PartNo;
    private String BatchNo;
    private String SupPrdBatch ;
    private int AreaID;
    public String SuppliernNo;

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }

    public String SupplierName;
    private ArrayList<BarCodeInfo> lstBarCode;
    private ArrayList<StockInfo_Model> lstStockInfo;
    private String BarCode;
    private int PalletType;

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }


    public String getSuppliernNo() {
        return SuppliernNo;
    }

    public void setSuppliernNo(String suppliernNo) {
        SuppliernNo = suppliernNo;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSuppliernName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getSupPrdBatch() {
        return SupPrdBatch;
    }

    public void setSupPrdBatch(String supPrdBatch) {
        SupPrdBatch = supPrdBatch;
    }

    public int getAreaID() {
        return AreaID;
    }

    public void setAreaID(int areaID) {
        AreaID = areaID;
    }

    public ArrayList<StockInfo_Model> getLstStockInfo() {
        return lstStockInfo;
    }

    public void setLstStockInfo(ArrayList<StockInfo_Model> lstStockInfo) {
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

    public void setLstBarCode(ArrayList<BarCodeInfo> lstBarCode) {
        this.lstBarCode = lstBarCode;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
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

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
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
        super.writeToParcel(dest, flags);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.RowNo);
        dest.writeString(this.PalletNo);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeInt(this.IsSerial);
        dest.writeString(this.PartNo);
        dest.writeString(this.BatchNo);
        dest.writeString(this.SupPrdBatch);
        dest.writeInt(this.AreaID);
        dest.writeString(this.SuppliernNo);
        dest.writeString(this.SupplierName);
        dest.writeTypedList(this.lstBarCode);
        dest.writeTypedList(this.lstStockInfo);
        dest.writeString(this.BarCode);
        dest.writeInt(this.PalletType);
    }

    protected PalletDetail_Model(Parcel in) {
        super(in);
        this.VoucherNo = in.readString();
        this.RowNo = in.readString();
        this.PalletNo = in.readString();
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.IsSerial = in.readInt();
        this.PartNo = in.readString();
        this.BatchNo = in.readString();
        this.SupPrdBatch = in.readString();
        this.AreaID = in.readInt();
        this.SuppliernNo = in.readString();
        this.SupplierName = in.readString();
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
