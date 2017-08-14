package com.xx.chinetek.model.Receiption;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;

import java.util.List;

/**
 * Created by GHOST on 2016/12/13.
 */

public class Receipt_Model extends Base_Model implements Parcelable {
    public Receipt_Model(){

    }

   public Receipt_Model(String ErpVoucherNo){
        this.ErpVoucherNo=ErpVoucherNo;
    }

    private String VoucherNo;
    private String SupplierNo;
    private String SupplierName;
    private Float IsQuality;
    private Float IsReceivePost;
    private Float IsShelvePost;
   // private int VoucherType;
    private String Plant;
    private String PlantName;
    private String MoveType;
    private List<ReceiptDetail_Model> lstDetail;
    private String Note;
    private  int  WareHouseID;

    public int getWareHouseID() {
        return WareHouseID;
    }

    public void setWareHouseID(int wareHouseID) {
        WareHouseID = wareHouseID;
    }

    public Float getIsQuality() {
        return IsQuality;
    }

    public void setIsQuality(Float isQuality) {
        IsQuality = isQuality;
    }

    public Float getIsReceivePost() {
        return IsReceivePost;
    }

    public void setIsReceivePost(Float isReceivePost) {
        IsReceivePost = isReceivePost;
    }

    public Float getIsShelvePost() {
        return IsShelvePost;
    }

    public void setIsShelvePost(Float isShelvePost) {
        IsShelvePost = isShelvePost;
    }

    public List<ReceiptDetail_Model> getLstDetail() {
        return lstDetail;
    }

    public void setLstDetail(List<ReceiptDetail_Model> lstDetail) {
        this.lstDetail = lstDetail;
    }

    public String getMoveType() {
        return MoveType;
    }

    public void setMoveType(String moveType) {
        MoveType = moveType;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setPlantName(String plantName) {
        PlantName = plantName;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getSupplierNo() {
        return SupplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        SupplierNo = supplierNo;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt_Model that = (Receipt_Model) o;

        return ErpVoucherNo.equals(that.getErpVoucherNo());

    }

    @Override
    public int hashCode() {
        return VoucherNo.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.SupplierNo);
        dest.writeString(this.SupplierName);
        dest.writeValue(this.IsQuality);
        dest.writeValue(this.IsReceivePost);
        dest.writeValue(this.IsShelvePost);
        dest.writeString(this.Plant);
        dest.writeString(this.PlantName);
        dest.writeString(this.MoveType);
        dest.writeTypedList(this.lstDetail);
        dest.writeString(this.Note);
        dest.writeInt(this.WareHouseID);
    }

    protected Receipt_Model(Parcel in) {
        super(in);
        this.VoucherNo = in.readString();
        this.SupplierNo = in.readString();
        this.SupplierName = in.readString();
        this.IsQuality = (Float) in.readValue(Float.class.getClassLoader());
        this.IsReceivePost = (Float) in.readValue(Float.class.getClassLoader());
        this.IsShelvePost = (Float) in.readValue(Float.class.getClassLoader());
        this.Plant = in.readString();
        this.PlantName = in.readString();
        this.MoveType = in.readString();
        this.lstDetail = in.createTypedArrayList(ReceiptDetail_Model.CREATOR);
        this.Note = in.readString();
        this.WareHouseID = in.readInt();
    }

    public static final Creator<Receipt_Model> CREATOR = new Creator<Receipt_Model>() {
        @Override
        public Receipt_Model createFromParcel(Parcel source) {
            return new Receipt_Model(source);
        }

        @Override
        public Receipt_Model[] newArray(int size) {
            return new Receipt_Model[size];
        }
    };
}
