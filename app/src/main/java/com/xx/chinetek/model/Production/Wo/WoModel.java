package com.xx.chinetek.model.Production.Wo;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;

import java.util.Date;

/**
 * Created by GHOST on 2017/7/18.
 */

public class WoModel extends Base_Model implements Parcelable{

    private String VoucherNo; //WMS工单号

    private String MaterialNo;

    private String MaterialDesc;

    private String BatchNo;

    private Float ProductQty;

    private Float RemainQty;

    private String Unit;

    private String UnitName;

    private String ERPStaffNo;

    private String ERPStaffName;

    private Date ShipmentDate;

    private Float MaxProductQty;

    private Float Box_Amount;

    private Float Pack_Amount;

    private String DataType;

    private int QualityMonth;// 有效期月

    private int QualityDay;// 有效期天

    public Float getScanReportQty() {
        return ScanReportQty;
    }

    public void setScanReportQty(Float scanReportQty) {
        ScanReportQty = scanReportQty;
    }

    private Float ScanReportQty;// 有效期天

    public String getWo_Batch() {
        return Wo_Batch;
    }

    public void setWo_Batch(String wo_Batch) {
        Wo_Batch = wo_Batch;
    }

    private String Wo_Batch;


    public int getQualityMonth() {
        return QualityMonth;
    }

    public void setQualityMonth(int qualityMonth) {
        QualityMonth = qualityMonth;
    }

    public int getQualityDay() {
        return QualityDay;
    }

    public void setQualityDay(int qualityDay) {
        QualityDay = qualityDay;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public Float getMaxProductQty() {
        return MaxProductQty;
    }

    public void setMaxProductQty(Float maxProductQty) {
        MaxProductQty = maxProductQty;
    }

    public Float getBox_Amount() {
        return Box_Amount;
    }

    public void setBox_Amount(Float box_Amount) {
        Box_Amount = box_Amount;
    }

    public Float getPack_Amount() {
        return Pack_Amount;
    }

    public void setPack_Amount(Float pack_Amount) {
        Pack_Amount = pack_Amount;
    }

    //add by 2017-8-27
    public Float ReportQty;// 报工数量
    public Float getReportQty() {
        return ReportQty;
    }
    public void setReportQty(Float reportQty) {
        ReportQty = reportQty;
    }

    public int JobNumber;// 作业人数
    public int getJobNumber() {
        return JobNumber;
    }
    public void setJobNumber(int jobNumber) {
        JobNumber = jobNumber;
    }

    public Float WorkHour; // 报工工时
    public Float getWorkHour() {
        return WorkHour;
    }
    public void setWorkHour(Float workHour) {
        WorkHour = workHour;
    }

    public String UserNo; // 报工人
    public String getUserNo() {
        return UserNo;
    }
    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public Float InQty;// 完工数量
    public Float getInQty() {
        return InQty;
    }
    public void setInQty(Float inQty) {
        InQty = inQty;
    }

    public String WareHouseNo; // 仓库
    public String getWareHouseNo() {
        return WareHouseNo;
    }
    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    public String AreaNo;// 储位
    public String getAreaNo() {
        return AreaNo;
    }
    public void setAreaNo(String areaNo) {
        AreaNo = areaNo;
    }

    public String StrSupPrdDate; // 制造日期
    public String getStrSupPrdDate() {
        return StrSupPrdDate;
    }
    public void setStrSupPrdDate(String strSupPrdDate) {
        StrSupPrdDate = strSupPrdDate;
    }


    public Float getRemainQty() {
        return RemainQty;
    }

    public void setRemainQty(Float remainQty) {
        RemainQty = remainQty;
    }

    public String getERPStaffNo() {
        return ERPStaffNo;
    }

    public void setERPStaffNo(String ERPStaffNo) {
        this.ERPStaffNo = ERPStaffNo;
    }

    public String getERPStaffName() {
        return ERPStaffName;
    }

    public void setERPStaffName(String ERPStaffName) {
        this.ERPStaffName = ERPStaffName;
    }

    public Date getShipmentDate() {
        return ShipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        ShipmentDate = shipmentDate;
    }


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
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeString(this.BatchNo);
        dest.writeValue(this.ProductQty);
        dest.writeValue(this.RemainQty);
        dest.writeString(this.Unit);
        dest.writeString(this.UnitName);
        dest.writeString(this.ERPStaffNo);
        dest.writeString(this.ERPStaffName);
        dest.writeLong(this.ShipmentDate != null ? this.ShipmentDate.getTime() : -1);
        dest.writeValue(this.MaxProductQty);
        dest.writeValue(this.Box_Amount);
        dest.writeValue(this.Pack_Amount);
        dest.writeString(this.DataType);
        dest.writeInt(this.QualityMonth);
        dest.writeInt(this.QualityDay);
        dest.writeValue(this.ScanReportQty);
        dest.writeString(this.Wo_Batch);
        dest.writeValue(this.ReportQty);
        dest.writeInt(this.JobNumber);
        dest.writeValue(this.WorkHour);
        dest.writeString(this.UserNo);
        dest.writeValue(this.InQty);
        dest.writeString(this.WareHouseNo);
        dest.writeString(this.AreaNo);
        dest.writeString(this.StrSupPrdDate);
    }

    protected WoModel(Parcel in) {
        super(in);
        this.VoucherNo = in.readString();
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.BatchNo = in.readString();
        this.ProductQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
        this.Unit = in.readString();
        this.UnitName = in.readString();
        this.ERPStaffNo = in.readString();
        this.ERPStaffName = in.readString();
        long tmpShipmentDate = in.readLong();
        this.ShipmentDate = tmpShipmentDate == -1 ? null : new Date(tmpShipmentDate);
        this.MaxProductQty = (Float) in.readValue(Float.class.getClassLoader());
        this.Box_Amount = (Float) in.readValue(Float.class.getClassLoader());
        this.Pack_Amount = (Float) in.readValue(Float.class.getClassLoader());
        this.DataType = in.readString();
        this.QualityMonth = in.readInt();
        this.QualityDay = in.readInt();
        this.ScanReportQty = (Float) in.readValue(Float.class.getClassLoader());
        this.Wo_Batch = in.readString();
        this.ReportQty = (Float) in.readValue(Float.class.getClassLoader());
        this.JobNumber = in.readInt();
        this.WorkHour = (Float) in.readValue(Float.class.getClassLoader());
        this.UserNo = in.readString();
        this.InQty = (Float) in.readValue(Float.class.getClassLoader());
        this.WareHouseNo = in.readString();
        this.AreaNo = in.readString();
        this.StrSupPrdDate = in.readString();
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
