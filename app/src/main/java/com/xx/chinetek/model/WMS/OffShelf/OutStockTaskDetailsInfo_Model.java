package com.xx.chinetek.model.WMS.OffShelf;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GHOST on 2017/1/16.
 */

public class OutStockTaskDetailsInfo_Model extends Base_Model implements Parcelable{
    public OutStockTaskDetailsInfo_Model(){

    }

    public OutStockTaskDetailsInfo_Model(String MaterialNo,String RowNo,String RowNoDel){
        this.MaterialNo=MaterialNo;
        this.RowNo=RowNo;
        this.RowNoDel=RowNoDel;
    }

    public OutStockTaskDetailsInfo_Model(String MaterialNo,String StrongHoldCode){
        this.MaterialNo=MaterialNo;
        this.StrongHoldCode=StrongHoldCode;
    }

    private String MaterialNo;
    private String MaterialDesc;
    private Float TaskQty;
    private Float QualityQty;
    private Float RemainQty; //剩余拣货数量
   // private Float PickQty; //拣货数量
    private Float ShelveQty;
    private String TaskNo;
    private Float IsQualitycomp;
    private String KeeperUserNo;
    private String OperatorUserNo;
    private int TaskID;
    private String TMaterialNo;
    private String TMaterialDesc;
    private Float ReviewQty;
    private Float PackCount;
    private Float ShelvePackCount;
    private String VoucherNo;
    private String RowNo;
    private String RowNoDel;
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
    private Float StockQty; //库存数量
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
    private Float ScanQty;  //扫描数量
    private Float RePickQty;  //扫描数量
    private String AreaNo;
    private String HouseNo;
    private String WareHouseNo;
    private List<StockInfo_Model> lstStockInfo=new ArrayList<StockInfo_Model>();
    private String SupCusCode;
    private String SupCusName;
    private String SaleName;
    private int TaskType ;
    private int IsSerial;
    private String PartNo;
    private String MoveType;
    private String BatchNo;

    /// <summary>
    /// 是否指定批次
    /// </summary>
    private String IsSpcBatch;
    /// <summary>
    /// ERP指定的发货批次
    /// </summary>
    private String FromBatchNo;
    /// <summary>
    /// ERP指定发货储位
    /// </summary>
    public String FromErpAreaNo;
    /// <summary>
    /// ERP指定发货仓库
    /// </summary>
    private String FromErpWarehouse;
    /// <summary>
    /// 给ERP指定的发货批次
    /// </summary>
    private String ToBatchNo;
    /// <summary>
    /// 给ERP指定发货储位
    /// </summary>
    private String ToErpAreaNo;
    /// <summary>
    /// 给ERP指定发货仓库
    /// </summary>
    private String ToErpWareHouse;

    private int FloorType;

    private Boolean isOutOfstock=false; //是否缺货

    private Boolean isPickFinish=false; //是否拣货完毕

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutStockTaskDetailsInfo_Model that = (OutStockTaskDetailsInfo_Model) o;

        return MaterialNo.equals(that.MaterialNo) && StrongHoldCode.equals(that.StrongHoldCode);
       // return MaterialNo.equals(that.MaterialNo) && RowNo.equals(that.RowNo);

    }

    public Boolean getPickFinish() {
        return isPickFinish;
    }

    public void setPickFinish(Boolean pickFinish) {
        isPickFinish = pickFinish;
    }

    public Float getRePickQty() {
        return RePickQty;
    }

    public void setRePickQty(Float rePickQty) {
        RePickQty = rePickQty;
    }

    public Float getStockQty() {
        return StockQty;
    }

    public void setStockQty(Float stockQty) {
        StockQty = stockQty;
    }

    public String getRowNoDel() {
        return RowNoDel;
    }

    public void setRowNoDel(String rowNoDel) {
        RowNoDel = rowNoDel;
    }

    public Boolean getOutOfstock() {
        return isOutOfstock;
    }

    public void setOutOfstock(Boolean outOfstock) {
        isOutOfstock = outOfstock;
    }

    public String getIsSpcBatch() {
        return IsSpcBatch;
    }

    public void setIsSpcBatch(String isSpcBatch) {
        IsSpcBatch = isSpcBatch;
    }

    public String getFromBatchNo() {
        return FromBatchNo;
    }

    public void setFromBatchNo(String fromBatchno) {
        FromBatchNo = fromBatchno;
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

    public void setFromErpWarehouse(String fromErpWareHouse) {
        FromErpWarehouse = fromErpWareHouse;
    }

    public String getToBatchno() {
        return ToBatchNo;
    }

    public void setToBatchno(String toBatchno) {
        ToBatchNo = toBatchno;
    }

    public String getToErpAreaNo() {
        return ToErpAreaNo;
    }

    public void setToErpAreaNo(String toErpAreaNo) {
        ToErpAreaNo = toErpAreaNo;
    }

    public String getToErpWareHouse() {
        return ToErpWareHouse;
    }

    public void setToErpWareHouse(String toErpWareHouse) {
        ToErpWareHouse = toErpWareHouse;
    }

    public int getFloorType() {
        return FloorType;
    }

    public void setFloorType(int floorType) {
        FloorType = floorType;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getMoveType() {
        return MoveType;
    }

    public void setMoveType(String moveType) {
        MoveType = moveType;
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

    public int getIsSerial() {
        return IsSerial;
    }

    public void setIsSerial(int isSerial) {
        IsSerial = isSerial;
    }

    public List<StockInfo_Model> getLstStockInfo() {
        return lstStockInfo;
    }

    public void setLstStockInfo(List<StockInfo_Model> lstStockInfo) {
        this.lstStockInfo = lstStockInfo;
    }

    public Float getIsLock() {
        return IsLock;
    }

    public void setIsLock(Float isLock) {
        IsLock = isLock;
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

    public String getKeeperUserNo() {
        return KeeperUserNo;
    }

    public void setKeeperUserNo(String keeperUserNo) {
        KeeperUserNo = keeperUserNo;
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
        dest.writeString(this.TaskNo);
        dest.writeValue(this.IsQualitycomp);
        dest.writeString(this.KeeperUserNo);
        dest.writeString(this.OperatorUserNo);
        dest.writeInt(this.TaskID);
        dest.writeString(this.TMaterialNo);
        dest.writeString(this.TMaterialDesc);
        dest.writeValue(this.ReviewQty);
        dest.writeValue(this.PackCount);
        dest.writeValue(this.ShelvePackCount);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.RowNo);
        dest.writeString(this.RowNoDel);
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
        dest.writeValue(this.StockQty);
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
        dest.writeValue(this.RePickQty);
        dest.writeString(this.AreaNo);
        dest.writeString(this.HouseNo);
        dest.writeString(this.WareHouseNo);
        dest.writeTypedList(this.lstStockInfo);
        dest.writeString(this.SupCusCode);
        dest.writeString(this.SupCusName);
        dest.writeString(this.SaleName);
        dest.writeInt(this.TaskType);
        dest.writeInt(this.IsSerial);
        dest.writeString(this.PartNo);
        dest.writeString(this.MoveType);
        dest.writeString(this.BatchNo);
        dest.writeString(this.IsSpcBatch);
        dest.writeString(this.FromBatchNo);
        dest.writeString(this.FromErpAreaNo);
        dest.writeString(this.FromErpWarehouse);
        dest.writeString(this.ToBatchNo);
        dest.writeString(this.ToErpAreaNo);
        dest.writeString(this.ToErpWareHouse);
        dest.writeInt(this.FloorType);
        dest.writeValue(this.isOutOfstock);
        dest.writeValue(this.isPickFinish);
    }

    protected OutStockTaskDetailsInfo_Model(Parcel in) {
        super(in);
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.TaskQty = (Float) in.readValue(Float.class.getClassLoader());
        this.QualityQty = (Float) in.readValue(Float.class.getClassLoader());
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
        this.ShelveQty = (Float) in.readValue(Float.class.getClassLoader());
        this.TaskNo = in.readString();
        this.IsQualitycomp = (Float) in.readValue(Float.class.getClassLoader());
        this.KeeperUserNo = in.readString();
        this.OperatorUserNo = in.readString();
        this.TaskID = in.readInt();
        this.TMaterialNo = in.readString();
        this.TMaterialDesc = in.readString();
        this.ReviewQty = (Float) in.readValue(Float.class.getClassLoader());
        this.PackCount = (Float) in.readValue(Float.class.getClassLoader());
        this.ShelvePackCount = (Float) in.readValue(Float.class.getClassLoader());
        this.VoucherNo = in.readString();
        this.RowNo = in.readString();
        this.RowNoDel = in.readString();
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
        this.StockQty = (Float) in.readValue(Float.class.getClassLoader());
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
        this.RePickQty = (Float) in.readValue(Float.class.getClassLoader());
        this.AreaNo = in.readString();
        this.HouseNo = in.readString();
        this.WareHouseNo = in.readString();
        this.lstStockInfo = in.createTypedArrayList(StockInfo_Model.CREATOR);
        this.SupCusCode = in.readString();
        this.SupCusName = in.readString();
        this.SaleName = in.readString();
        this.TaskType = in.readInt();
        this.IsSerial = in.readInt();
        this.PartNo = in.readString();
        this.MoveType = in.readString();
        this.BatchNo = in.readString();
        this.IsSpcBatch = in.readString();
        this.FromBatchNo = in.readString();
        this.FromErpAreaNo = in.readString();
        this.FromErpWarehouse = in.readString();
        this.ToBatchNo = in.readString();
        this.ToErpAreaNo = in.readString();
        this.ToErpWareHouse = in.readString();
        this.FloorType = in.readInt();
        this.isOutOfstock = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isPickFinish = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<OutStockTaskDetailsInfo_Model> CREATOR = new Creator<OutStockTaskDetailsInfo_Model>() {
        @Override
        public OutStockTaskDetailsInfo_Model createFromParcel(Parcel source) {
            return new OutStockTaskDetailsInfo_Model(source);
        }

        @Override
        public OutStockTaskDetailsInfo_Model[] newArray(int size) {
            return new OutStockTaskDetailsInfo_Model[size];
        }
    };
}
