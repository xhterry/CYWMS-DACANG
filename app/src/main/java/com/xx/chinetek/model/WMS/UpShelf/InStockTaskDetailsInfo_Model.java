package com.xx.chinetek.model.WMS.UpShelf;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.WMS.Stock.AreaInfo_Model;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by GHOST on 2017/1/13.
 */

public class InStockTaskDetailsInfo_Model extends Base_Model implements Parcelable {
    public InStockTaskDetailsInfo_Model(){

    }

    public InStockTaskDetailsInfo_Model(String MaterialNo,String BatchNo){
    this.MaterialNo=MaterialNo;
        this.BatchNo=BatchNo;
    }


    private String MaterialNo;
    private String MaterialDesc;
    private Float TaskQty;
    private Float QualityQty;
    private Float RemainQty;
    private Float ShelveQty;
    private Float IsQualitycomp;
    private String KeeperUserNo;
    private String OperatorUserNo;
    private int TaskID;
    private String TaskNo;
    private String TMaterialNo;
    private String TMaterialDesc;
    private Float ReviewQty;
    private Float PackCount;
    private Float ShelvePackCount;
    private String VoucherNo;
    private String RowNo;
    private String TrackNo;
    private String Unit;
    private Float UnQualityQty;
    private Float PostQty;
    private Float PostStatus;
    private Date PostDate;
    private String ReserveNumber;
    private String ReserveRowNo;
    private Float UnShelveQty;
    private String Requstreason;
    private String Remark;
    private String ReviewUser;
    private Date ReviewDate;
    private Float ReviewStatus;
    private String PostUser;
    private String Costcenter;
    private String Wbselem;
    private String ToStorageLoc;
    private String FromStorageLoc;
    private Float OutStockQty;
    private Float LimitStockQtySAP;
    private Float RemainsSockQtySAP;
    private Float PackFlag;
    private Float CurrentRemainStockQtySAP;
    private String MoveReasonCode;
    private String MoveReasonDesc;
    private String PoNo;
    private String PoRowNo;
    private Float IsLock;
    private Float IsSmallBatch;
    private String UnitName;
    private Float ScanQty;
    private String AreaNo;
    private String HouseNo;
    private String WareHouseNo;
    private int WarehouseID;
    private int HouseID;
    private int AreaID;
    private ArrayList<AreaInfo_Model> lstArea=new ArrayList<>();
    private ArrayList<StockInfo_Model> lstStockInfo=new ArrayList<StockInfo_Model>();
    private String SupCusCode;
    private String SupCusName;
    private String SaleName;
    private int TaskType;
    private String PartNo;
    private String FromBatchNo;
    private String BatchNo;
    private String FromErpAreaNo ;
    private String FromErpWarehouse ;
    private String ToBatchNo;
    private String ToErpAreaNo;
    private String ToErpWarehouse;

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public ArrayList<AreaInfo_Model> getLstArea() {
        return lstArea;
    }

    public void setLstArea(ArrayList<AreaInfo_Model> lstArea) {
        this.lstArea = lstArea;
    }

    public String getFromBatchNo() {
        return FromBatchNo;
    }

    public void setFromBatchNo(String fromBatchNo) {
        FromBatchNo = fromBatchNo;
    }

    public String getFromErpAreaNo() {
        return FromErpAreaNo;
    }

    public void setFromErpAreaNo(String fromErpAreaNo) {
        FromErpAreaNo = fromErpAreaNo;
    }

    public String getFromErpWarehouse() {
        return FromErpWarehouse;
    }

    public void setFromErpWarehouse(String fromErpWarehouse) {
        FromErpWarehouse = fromErpWarehouse;
    }

    public String getToBatchNo() {
        return ToBatchNo;
    }

    public void setToBatchNo(String toBatchNo) {
        ToBatchNo = toBatchNo;
    }

    public String getToErpAreaNo() {
        return ToErpAreaNo;
    }

    public void setToErpAreaNo(String toErpAreaNo) {
        ToErpAreaNo = toErpAreaNo;
    }

    public String getToErpWarehouse() {
        return ToErpWarehouse;
    }

    public void setToErpWarehouse(String toErpWarehouse) {
        ToErpWarehouse = toErpWarehouse;
    }

    public int getAreaID() {
        return AreaID;
    }

    public void setAreaID(int areaID) {
        AreaID = areaID;
    }

    public int getHouseID() {
        return HouseID;
    }

    public void setHouseID(int houseID) {
        HouseID = houseID;
    }

    public int getWarehouseID() {
        return WarehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        WarehouseID = warehouseID;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
    }

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }

    public String getKeeperUserNo() {
        return KeeperUserNo;
    }

    public void setKeeperUserNo(String keeperUserNo) {
        KeeperUserNo = keeperUserNo;
    }

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String areaNo) {
        AreaNo = areaNo;
    }


    public String getCostcenter() {
        return Costcenter;
    }

    public void setCostcenter(String costcenter) {
        Costcenter = costcenter;
    }

    public Float getCurrentRemainStockQtySAP() {
        return CurrentRemainStockQtySAP;
    }

    public void setCurrentRemainStockQtySAP(Float currentRemainStockQtySAP) {
        CurrentRemainStockQtySAP = currentRemainStockQtySAP;
    }



    public String getFromStorageLoc() {
        return FromStorageLoc;
    }

    public void setFromStorageLoc(String fromStorageLoc) {
        FromStorageLoc = fromStorageLoc;
    }

    public String getHouseNo() {
        return HouseNo;
    }

    public void setHouseNo(String houseNo) {
        HouseNo = houseNo;
    }

    public Float getIsLock() {
        return IsLock;
    }

    public void setIsLock(Float isLock) {
        IsLock = isLock;
    }

    public Float getIsQualitycomp() {
        return IsQualitycomp;
    }

    public void setIsQualitycomp(Float isQualitycomp) {
        IsQualitycomp = isQualitycomp;
    }

    public Float getIsSmallBatch() {
        return IsSmallBatch;
    }

    public void setIsSmallBatch(Float isSmallBatch) {
        IsSmallBatch = isSmallBatch;
    }

    public Float getLimitStockQtySAP() {
        return LimitStockQtySAP;
    }

    public void setLimitStockQtySAP(Float limitStockQtySAP) {
        LimitStockQtySAP = limitStockQtySAP;
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

    public String getOperatorUserNo() {
        return OperatorUserNo;
    }

    public void setOperatorUserNo(String operatorUserNo) {
        OperatorUserNo = operatorUserNo;
    }

    public Float getOutStockQty() {
        return OutStockQty;
    }

    public void setOutStockQty(Float outStockQty) {
        OutStockQty = outStockQty;
    }

    public Float getPackCount() {
        return PackCount;
    }

    public void setPackCount(Float packCount) {
        PackCount = packCount;
    }

    public Float getPackFlag() {
        return PackFlag;
    }

    public void setPackFlag(Float packFlag) {
        PackFlag = packFlag;
    }

    public String getPoNo() {
        return PoNo;
    }

    public void setPoNo(String poNo) {
        PoNo = poNo;
    }

    public String getPoRowNo() {
        return PoRowNo;
    }

    public void setPoRowNo(String poRowNo) {
        PoRowNo = poRowNo;
    }

    public Date getPostDate() {
        return PostDate;
    }

    public void setPostDate(Date postDate) {
        PostDate = postDate;
    }

    public Float getPostQty() {
        return PostQty;
    }

    public void setPostQty(Float postQty) {
        PostQty = postQty;
    }

    public Float getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(Float postStatus) {
        PostStatus = postStatus;
    }

    public String getPostUser() {
        return PostUser;
    }

    public void setPostUser(String postUser) {
        PostUser = postUser;
    }

    public Float getQualityQty() {
        return QualityQty;
    }

    public void setQualityQty(Float qualityQty) {
        QualityQty = qualityQty;
    }

    public Float getRemainQty() {
        return RemainQty;
    }

    public void setRemainQty(Float remainQty) {
        RemainQty = remainQty;
    }

    public Float getRemainsSockQtySAP() {
        return RemainsSockQtySAP;
    }

    public void setRemainsSockQtySAP(Float remainsSockQtySAP) {
        RemainsSockQtySAP = remainsSockQtySAP;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getRequstreason() {
        return Requstreason;
    }

    public void setRequstreason(String requstreason) {
        Requstreason = requstreason;
    }

    public String getReserveNumber() {
        return ReserveNumber;
    }

    public void setReserveNumber(String reserveNumber) {
        ReserveNumber = reserveNumber;
    }

    public String getReserveRowNo() {
        return ReserveRowNo;
    }

    public void setReserveRowNo(String reserveRowNo) {
        ReserveRowNo = reserveRowNo;
    }

    public Date getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        ReviewDate = reviewDate;
    }

    public Float getReviewQty() {
        return ReviewQty;
    }

    public void setReviewQty(Float reviewQty) {
        ReviewQty = reviewQty;
    }

    public Float getReviewStatus() {
        return ReviewStatus;
    }

    public void setReviewStatus(Float reviewStatus) {
        ReviewStatus = reviewStatus;
    }

    public String getReviewUser() {
        return ReviewUser;
    }

    public void setReviewUser(String reviewUser) {
        ReviewUser = reviewUser;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

    public String getSaleName() {
        return SaleName;
    }

    public void setSaleName(String saleName) {
        SaleName = saleName;
    }

    public Float getScanQty() {
        return ScanQty;
    }

    public void setScanQty(Float scanQty) {
        ScanQty = scanQty;
    }

    public Float getShelvePackCount() {
        return ShelvePackCount;
    }

    public void setShelvePackCount(Float shelvePackCount) {
        ShelvePackCount = shelvePackCount;
    }

    public Float getShelveQty() {
        return ShelveQty;
    }

    public void setShelveQty(Float shelveQty) {
        ShelveQty = shelveQty;
    }

    public String getSupCusCode() {
        return SupCusCode;
    }

    public void setSupCusCode(String supCusCode) {
        SupCusCode = supCusCode;
    }

    public String getSupCusName() {
        return SupCusName;
    }

    public void setSupCusName(String supCusName) {
        SupCusName = supCusName;
    }

    public int getTaskID() {
        return TaskID;
    }

    public void setTaskID(int taskID) {
        TaskID = taskID;
    }

    public Float getTaskQty() {
        return TaskQty;
    }

    public void setTaskQty(Float taskQty) {
        TaskQty = taskQty;
    }

    public int getTaskType() {
        return TaskType;
    }

    public void setTaskType(int taskType) {
        TaskType = taskType;
    }

    public String getTMaterialDesc() {
        return TMaterialDesc;
    }

    public void setTMaterialDesc(String TMaterialDesc) {
        this.TMaterialDesc = TMaterialDesc;
    }

    public String getTMaterialNo() {
        return TMaterialNo;
    }

    public void setTMaterialNo(String TMaterialNo) {
        this.TMaterialNo = TMaterialNo;
    }

    public String getToStorageLoc() {
        return ToStorageLoc;
    }

    public void setToStorageLoc(String toStorageLoc) {
        ToStorageLoc = toStorageLoc;
    }

    public String getTrackNo() {
        return TrackNo;
    }

    public void setTrackNo(String trackNo) {
        TrackNo = trackNo;
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

    public Float getUnQualityQty() {
        return UnQualityQty;
    }

    public void setUnQualityQty(Float unQualityQty) {
        UnQualityQty = unQualityQty;
    }

    public Float getUnShelveQty() {
        return UnShelveQty;
    }

    public void setUnShelveQty(Float unShelveQty) {
        UnShelveQty = unShelveQty;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public String getWareHouseNo() {
        return WareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    public String getWbselem() {
        return Wbselem;
    }

    public void setWbselem(String wbselem) {
        Wbselem = wbselem;
    }

    public ArrayList<StockInfo_Model> getLstStockInfo() {
        return lstStockInfo;
    }

    public void setLstStockInfo(ArrayList<StockInfo_Model> lstStockInfo) {
        this.lstStockInfo = lstStockInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InStockTaskDetailsInfo_Model that = (InStockTaskDetailsInfo_Model) o;

        return MaterialNo.equals(that.MaterialNo) && BatchNo.equals(that.BatchNo);

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
        dest.writeValue(this.TaskQty);
        dest.writeValue(this.QualityQty);
        dest.writeValue(this.RemainQty);
        dest.writeValue(this.ShelveQty);
        dest.writeValue(this.IsQualitycomp);
        dest.writeString(this.KeeperUserNo);
        dest.writeString(this.OperatorUserNo);
        dest.writeInt(this.TaskID);
        dest.writeString(this.TaskNo);
        dest.writeString(this.TMaterialNo);
        dest.writeString(this.TMaterialDesc);
        dest.writeValue(this.ReviewQty);
        dest.writeValue(this.PackCount);
        dest.writeValue(this.ShelvePackCount);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.RowNo);
        dest.writeString(this.TrackNo);
        dest.writeString(this.Unit);
        dest.writeValue(this.UnQualityQty);
        dest.writeValue(this.PostQty);
        dest.writeValue(this.PostStatus);
        dest.writeLong(this.PostDate != null ? this.PostDate.getTime() : -1);
        dest.writeString(this.ReserveNumber);
        dest.writeString(this.ReserveRowNo);
        dest.writeValue(this.UnShelveQty);
        dest.writeString(this.Requstreason);
        dest.writeString(this.Remark);
        dest.writeString(this.ReviewUser);
        dest.writeLong(this.ReviewDate != null ? this.ReviewDate.getTime() : -1);
        dest.writeValue(this.ReviewStatus);
        dest.writeString(this.PostUser);
        dest.writeString(this.Costcenter);
        dest.writeString(this.Wbselem);
        dest.writeString(this.ToStorageLoc);
        dest.writeString(this.FromStorageLoc);
        dest.writeValue(this.OutStockQty);
        dest.writeValue(this.LimitStockQtySAP);
        dest.writeValue(this.RemainsSockQtySAP);
        dest.writeValue(this.PackFlag);
        dest.writeValue(this.CurrentRemainStockQtySAP);
        dest.writeString(this.MoveReasonCode);
        dest.writeString(this.MoveReasonDesc);
        dest.writeString(this.PoNo);
        dest.writeString(this.PoRowNo);
        dest.writeValue(this.IsLock);
        dest.writeValue(this.IsSmallBatch);
        dest.writeString(this.UnitName);
        dest.writeValue(this.ScanQty);
        dest.writeString(this.AreaNo);
        dest.writeString(this.HouseNo);
        dest.writeString(this.WareHouseNo);
        dest.writeInt(this.WarehouseID);
        dest.writeInt(this.HouseID);
        dest.writeInt(this.AreaID);
        dest.writeList(this.lstArea);
        dest.writeTypedList(this.lstStockInfo);
        dest.writeString(this.SupCusCode);
        dest.writeString(this.SupCusName);
        dest.writeString(this.SaleName);
        dest.writeInt(this.TaskType);
        dest.writeString(this.PartNo);
        dest.writeString(this.FromBatchNo);
        dest.writeString(this.BatchNo);
        dest.writeString(this.FromErpAreaNo);
        dest.writeString(this.FromErpWarehouse);
        dest.writeString(this.ToBatchNo);
        dest.writeString(this.ToErpAreaNo);
        dest.writeString(this.ToErpWarehouse);
    }

    protected InStockTaskDetailsInfo_Model(Parcel in) {
        super(in);
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.TaskQty = (Float) in.readValue(Float.class.getClassLoader());
        this.QualityQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
        this.ShelveQty = (Float) in.readValue(Float.class.getClassLoader());
        this.IsQualitycomp = (Float) in.readValue(Float.class.getClassLoader());
        this.KeeperUserNo = in.readString();
        this.OperatorUserNo = in.readString();
        this.TaskID = in.readInt();
        this.TaskNo = in.readString();
        this.TMaterialNo = in.readString();
        this.TMaterialDesc = in.readString();
        this.ReviewQty = (Float) in.readValue(Float.class.getClassLoader());
        this.PackCount = (Float) in.readValue(Float.class.getClassLoader());
        this.ShelvePackCount = (Float) in.readValue(Float.class.getClassLoader());
        this.VoucherNo = in.readString();
        this.RowNo = in.readString();
        this.TrackNo = in.readString();
        this.Unit = in.readString();
        this.UnQualityQty = (Float) in.readValue(Float.class.getClassLoader());
        this.PostQty = (Float) in.readValue(Float.class.getClassLoader());
        this.PostStatus = (Float) in.readValue(Float.class.getClassLoader());
        long tmpPostDate = in.readLong();
        this.PostDate = tmpPostDate == -1 ? null : new Date(tmpPostDate);
        this.ReserveNumber = in.readString();
        this.ReserveRowNo = in.readString();
        this.UnShelveQty = (Float) in.readValue(Float.class.getClassLoader());
        this.Requstreason = in.readString();
        this.Remark = in.readString();
        this.ReviewUser = in.readString();
        long tmpReviewDate = in.readLong();
        this.ReviewDate = tmpReviewDate == -1 ? null : new Date(tmpReviewDate);
        this.ReviewStatus = (Float) in.readValue(Float.class.getClassLoader());
        this.PostUser = in.readString();
        this.Costcenter = in.readString();
        this.Wbselem = in.readString();
        this.ToStorageLoc = in.readString();
        this.FromStorageLoc = in.readString();
        this.OutStockQty = (Float) in.readValue(Float.class.getClassLoader());
        this.LimitStockQtySAP = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainsSockQtySAP = (Float) in.readValue(Float.class.getClassLoader());
        this.PackFlag = (Float) in.readValue(Float.class.getClassLoader());
        this.CurrentRemainStockQtySAP = (Float) in.readValue(Float.class.getClassLoader());
        this.MoveReasonCode = in.readString();
        this.MoveReasonDesc = in.readString();
        this.PoNo = in.readString();
        this.PoRowNo = in.readString();
        this.IsLock = (Float) in.readValue(Float.class.getClassLoader());
        this.IsSmallBatch = (Float) in.readValue(Float.class.getClassLoader());
        this.UnitName = in.readString();
        this.ScanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.AreaNo = in.readString();
        this.HouseNo = in.readString();
        this.WareHouseNo = in.readString();
        this.WarehouseID = in.readInt();
        this.HouseID = in.readInt();
        this.AreaID = in.readInt();
        this.lstArea = new ArrayList<AreaInfo_Model>();
        in.readList(this.lstArea, AreaInfo_Model.class.getClassLoader());
        this.lstStockInfo = in.createTypedArrayList(StockInfo_Model.CREATOR);
        this.SupCusCode = in.readString();
        this.SupCusName = in.readString();
        this.SaleName = in.readString();
        this.TaskType = in.readInt();
        this.PartNo = in.readString();
        this.FromBatchNo = in.readString();
        this.BatchNo = in.readString();
        this.FromErpAreaNo = in.readString();
        this.FromErpWarehouse = in.readString();
        this.ToBatchNo = in.readString();
        this.ToErpAreaNo = in.readString();
        this.ToErpWarehouse = in.readString();
    }

    public static final Creator<InStockTaskDetailsInfo_Model> CREATOR = new Creator<InStockTaskDetailsInfo_Model>() {
        @Override
        public InStockTaskDetailsInfo_Model createFromParcel(Parcel source) {
            return new InStockTaskDetailsInfo_Model(source);
        }

        @Override
        public InStockTaskDetailsInfo_Model[] newArray(int size) {
            return new InStockTaskDetailsInfo_Model[size];
        }
    };
}
