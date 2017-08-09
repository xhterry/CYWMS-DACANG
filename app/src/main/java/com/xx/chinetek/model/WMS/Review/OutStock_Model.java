package com.xx.chinetek.model.WMS.Review;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;

/**
 * Created by GHOST on 2017/6/20.
 */

public class OutStock_Model extends Base_Model implements Parcelable{

    public OutStock_Model(){

    }

    public OutStock_Model(String ErpVoucherNo){
        this.ErpVoucherNo=ErpVoucherNo;
    }

    private String CustomerCode;
    private String CustomerName;
    private Float IsOutStockPost;
    private Float IsUnderShelvePost;
    private String Plant;
    private String PlantName;
    private String MoveType;
    private String SupCode;
    private String SupName;
    private String MoveReasonCode;
    private String MoveReasonDesc;
    private Float ReviewStatus;
    private String VoucherNo;
    private String Note;

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public Float getIsOutStockPost() {
        return IsOutStockPost;
    }

    public void setIsOutStockPost(Float isOutStockPost) {
        IsOutStockPost = isOutStockPost;
    }

    public Float getIsUnderShelvePost() {
        return IsUnderShelvePost;
    }

    public void setIsUnderShelvePost(Float isUnderShelvePost) {
        IsUnderShelvePost = isUnderShelvePost;
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

    public String getMoveType() {
        return MoveType;
    }

    public void setMoveType(String moveType) {
        MoveType = moveType;
    }

    public String getSupCode() {
        return SupCode;
    }

    public void setSupCode(String supCode) {
        SupCode = supCode;
    }

    public String getSupName() {
        return SupName;
    }

    public void setSupName(String supName) {
        SupName = supName;
    }

    public String getMoveReasonCode() {
        return MoveReasonCode;
    }

    public void setMoveReasonCode(String moveReasonCode) {
        MoveReasonCode = moveReasonCode;
    }

    public String getMoveReasonDesc() {
        return MoveReasonDesc;
    }

    public void setMoveReasonDesc(String moveReasonDesc) {
        MoveReasonDesc = moveReasonDesc;
    }

    public Float getReviewStatus() {
        return ReviewStatus;
    }

    public void setReviewStatus(Float reviewStatus) {
        ReviewStatus = reviewStatus;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutStock_Model that = (OutStock_Model) o;

        return ErpVoucherNo.equals(that.ErpVoucherNo) ;

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.CustomerCode);
        dest.writeString(this.CustomerName);
        dest.writeValue(this.IsOutStockPost);
        dest.writeValue(this.IsUnderShelvePost);
        dest.writeString(this.Plant);
        dest.writeString(this.PlantName);
        dest.writeString(this.MoveType);
        dest.writeString(this.SupCode);
        dest.writeString(this.SupName);
        dest.writeString(this.MoveReasonCode);
        dest.writeString(this.MoveReasonDesc);
        dest.writeValue(this.ReviewStatus);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.Note);
    }

    protected OutStock_Model(Parcel in) {
        super(in);
        this.CustomerCode = in.readString();
        this.CustomerName = in.readString();
        this.IsOutStockPost = (Float) in.readValue(Float.class.getClassLoader());
        this.IsUnderShelvePost = (Float) in.readValue(Float.class.getClassLoader());
        this.Plant = in.readString();
        this.PlantName = in.readString();
        this.MoveType = in.readString();
        this.SupCode = in.readString();
        this.SupName = in.readString();
        this.MoveReasonCode = in.readString();
        this.MoveReasonDesc = in.readString();
        this.ReviewStatus = (Float) in.readValue(Float.class.getClassLoader());
        this.VoucherNo = in.readString();
        this.Note = in.readString();
    }

    public static final Creator<OutStock_Model> CREATOR = new Creator<OutStock_Model>() {
        @Override
        public OutStock_Model createFromParcel(Parcel source) {
            return new OutStock_Model(source);
        }

        @Override
        public OutStock_Model[] newArray(int size) {
            return new OutStock_Model[size];
        }
    };
}
