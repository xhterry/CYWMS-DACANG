package com.xx.chinetek.model.WMS.OffShelf;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;

import java.util.Date;

/**
 * Created by GHOST on 2017/1/16.
 */

public class OutStockTaskInfo_Model extends Base_Model implements Parcelable {
    public OutStockTaskInfo_Model() {

    }

    public OutStockTaskInfo_Model(String TaskNo){
        this.TaskNo=TaskNo;
    }
    private Float TaskType;
    private String TaskNo;
    private String SupcusName;
    private Float TaskStatus;
    private String AuditUserNo;
    private String ReceiveUserNo;
    private String Remark;
    private String Reason;
    private String SupcusNo;
    private Float IsShelvePost;
    private Float Receive_ID;
    private String ERPVoucherNo;
    private Float IsQuality;
    private Float IsReceivePost;
    private String Plant;
    private String PlanName;
    private Float PostStatus;
    private String MoveType;
    private Float IsOutStockPost;
    private Float IsUnderShelvePost;
    private Float ReviewStatus;
    private String MoveReasonCode;
    private String MoveReasonDesc;
    private Float PrintQty;
    private String CloseUserNo;
    private String CloseReason;
    private Float IsOwe;
    private Float IsUrgent;
    private Date OutStockDate;
    private String TaskIsSueduser;
    public String MaterialNo;
    public String ErpDocNo;

    public String getErpDocNo() {
        return ErpDocNo;
    }

    public void setErpDocNo(String erpDocNo) {
        ErpDocNo = erpDocNo;
    }

    public String getERPVoucherNo() {
        return ERPVoucherNo;
    }

    public void setERPVoucherNo(String ERPVoucherNo) {
        this.ERPVoucherNo = ERPVoucherNo;
    }

    public Float getIsUnderShelvePost() {
        return IsUnderShelvePost;
    }

    public void setIsUnderShelvePost(Float isUnderShelvePost) {
        IsUnderShelvePost = isUnderShelvePost;
    }

    public String getAuditUserNo() {
        return AuditUserNo;
    }

    public void setAuditUserNo(String auditUserNo) {
        AuditUserNo = auditUserNo;
    }


    public String getCloseReason() {
        return CloseReason;
    }

    public void setCloseReason(String closeReason) {
        CloseReason = closeReason;
    }

    public String getCloseUserNo() {
        return CloseUserNo;
    }

    public void setCloseUserNo(String closeUserNo) {
        CloseUserNo = closeUserNo;
    }

    public Float getIsOutStockPost() {
        return IsOutStockPost;
    }

    public void setIsOutStockPost(Float isOutStockPost) {
        IsOutStockPost = isOutStockPost;
    }

    public Float getIsOwe() {
        return IsOwe;
    }

    public void setIsOwe(Float isOwe) {
        IsOwe = isOwe;
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

    public Float getIsUrgent() {
        return IsUrgent;
    }

    public void setIsUrgent(Float isUrgent) {
        IsUrgent = isUrgent;
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
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

    public String getMoveType() {
        return MoveType;
    }

    public void setMoveType(String moveType) {
        MoveType = moveType;
    }

    public Date getOutStockDate() {
        return OutStockDate;
    }

    public void setOutStockDate(Date outStockDate) {
        OutStockDate = outStockDate;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public Float getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(Float postStatus) {
        PostStatus = postStatus;
    }

    public Float getPrintQty() {
        return PrintQty;
    }

    public void setPrintQty(Float printQty) {
        PrintQty = printQty;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public Float getReceive_ID() {
        return Receive_ID;
    }

    public void setReceive_ID(Float receive_ID) {
        Receive_ID = receive_ID;
    }

    public String getReceiveUserNo() {
        return ReceiveUserNo;
    }

    public void setReceiveUserNo(String receiveUserNo) {
        ReceiveUserNo = receiveUserNo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Float getReviewStatus() {
        return ReviewStatus;
    }

    public void setReviewStatus(Float reviewStatus) {
        ReviewStatus = reviewStatus;
    }

    public String getSupcusName() {
        return SupcusName;
    }

    public void setSupcusName(String supcusName) {
        SupcusName = supcusName;
    }

    public String getSupcusNo() {
        return SupcusNo;
    }

    public void setSupcusNo(String supcusNo) {
        SupcusNo = supcusNo;
    }

    public String getTaskIsSueduser() {
        return TaskIsSueduser;
    }

    public void setTaskIsSueduser(String taskIsSueduser) {
        TaskIsSueduser = taskIsSueduser;
    }

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }

    public Float getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(Float taskStatus) {
        TaskStatus = taskStatus;
    }

    public Float getTaskType() {
        return TaskType;
    }

    public void setTaskType(Float taskType) {
        TaskType = taskType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.TaskType);
        dest.writeString(this.TaskNo);
        dest.writeString(this.SupcusName);
        dest.writeValue(this.TaskStatus);
        dest.writeString(this.AuditUserNo);
        dest.writeString(this.ReceiveUserNo);
        dest.writeString(this.Remark);
        dest.writeString(this.Reason);
        dest.writeString(this.SupcusNo);
        dest.writeValue(this.IsShelvePost);
        dest.writeValue(this.Receive_ID);
        dest.writeString(this.ERPVoucherNo);
        dest.writeValue(this.IsQuality);
        dest.writeValue(this.IsReceivePost);
        dest.writeString(this.Plant);
        dest.writeString(this.PlanName);
        dest.writeValue(this.PostStatus);
        dest.writeString(this.MoveType);
        dest.writeValue(this.IsOutStockPost);
        dest.writeValue(this.IsUnderShelvePost);
        dest.writeValue(this.ReviewStatus);
        dest.writeString(this.MoveReasonCode);
        dest.writeString(this.MoveReasonDesc);
        dest.writeValue(this.PrintQty);
        dest.writeString(this.CloseUserNo);
        dest.writeString(this.CloseReason);
        dest.writeValue(this.IsOwe);
        dest.writeValue(this.IsUrgent);
        dest.writeLong(this.OutStockDate != null ? this.OutStockDate.getTime() : -1);
        dest.writeString(this.TaskIsSueduser);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.ErpDocNo);
    }

    protected OutStockTaskInfo_Model(Parcel in) {
        this.TaskType = (Float) in.readValue(Float.class.getClassLoader());
        this.TaskNo = in.readString();
        this.SupcusName = in.readString();
        this.TaskStatus = (Float) in.readValue(Float.class.getClassLoader());
        this.AuditUserNo = in.readString();
        this.ReceiveUserNo = in.readString();
        this.Remark = in.readString();
        this.Reason = in.readString();
        this.SupcusNo = in.readString();
        this.IsShelvePost = (Float) in.readValue(Float.class.getClassLoader());
        this.Receive_ID = (Float) in.readValue(Float.class.getClassLoader());
        this.ERPVoucherNo = in.readString();
        this.IsQuality = (Float) in.readValue(Float.class.getClassLoader());
        this.IsReceivePost = (Float) in.readValue(Float.class.getClassLoader());
        this.Plant = in.readString();
        this.PlanName = in.readString();
        this.PostStatus = (Float) in.readValue(Float.class.getClassLoader());
        this.MoveType = in.readString();
        this.IsOutStockPost = (Float) in.readValue(Float.class.getClassLoader());
        this.IsUnderShelvePost = (Float) in.readValue(Float.class.getClassLoader());
        this.ReviewStatus = (Float) in.readValue(Float.class.getClassLoader());
        this.MoveReasonCode = in.readString();
        this.MoveReasonDesc = in.readString();
        this.PrintQty = (Float) in.readValue(Float.class.getClassLoader());
        this.CloseUserNo = in.readString();
        this.CloseReason = in.readString();
        this.IsOwe = (Float) in.readValue(Float.class.getClassLoader());
        this.IsUrgent = (Float) in.readValue(Float.class.getClassLoader());
        long tmpOutStockDate = in.readLong();
        this.OutStockDate = tmpOutStockDate == -1 ? null : new Date(tmpOutStockDate);
        this.TaskIsSueduser = in.readString();
        this.MaterialNo = in.readString();
        this.ErpDocNo = in.readString();
    }

    public static final Creator<OutStockTaskInfo_Model> CREATOR = new Creator<OutStockTaskInfo_Model>() {
        @Override
        public OutStockTaskInfo_Model createFromParcel(Parcel source) {
            return new OutStockTaskInfo_Model(source);
        }

        @Override
        public OutStockTaskInfo_Model[] newArray(int size) {
            return new OutStockTaskInfo_Model[size];
        }
    };
}
