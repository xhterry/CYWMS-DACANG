package com.xx.chinetek.model.Receiption;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by GHOST on 2016/12/13.
 */

public class ReceiptDetail_Model extends Base_Model implements Parcelable,Cloneable {

    private int InStockID;
    private String RowNo;
    private String RowNoDel;
    private String MaterialNo;
    private String MaterialDesc;
    private Float InStockQty;
    private Float ReceiveQty;
    private String Unit;
    private String StorageLoc;
    private String Plant;
    private String PlantName;
    private Float QualityQty;
    private Float UnQualityQty;
    private String QualityType;
    private String QualityUserNo;
    private String UnitName;
    private Float RemainQty;
    private Float ScanQty;
    private String IsSpcBatch;
    private String QcCode;
    private String QcDesc;
    private ArrayList<BarCodeInfo> lstBarCode=new ArrayList<BarCodeInfo>();
    private String SaleName ;
    private String VoucherNo;
    private int IsSerial;
    private String SaleCode;
    private String SupplierNo;
    private String SupplierName;
    private Date SupPrdDate ;
    private String SupPrdBatch ;
    private String BatchNo;
    private String PartNo;
    private String MoveType;
    private String Company;
    private String Department;
    private Date ArrivalDate;
    private Date ShipmentDate;
    private Date ArrStockDate;
    private String FromBatchNo;
    private String FromErpAreaNo;
    private String FromErpWarehouse;
    private String ToBatchNo;
    private String ToErpAreaNo;
    private String ToErpWarehouse;


    public String getQcCode() {
        return QcCode;
    }

    public void setQcCode(String qcCode) {
        QcCode = qcCode;
    }

    public String getQcDesc() {
        return QcDesc;
    }

    public void setQcDesc(String qcDesc) {
        QcDesc = qcDesc;
    }

    public String getIsSpcBatch() {
        return IsSpcBatch;
    }

    public void setIsSpcBatch(String isSpcBatch) {
        IsSpcBatch = isSpcBatch;
    }

    //private List<SerialNo_Model> lstSerialNo=new ArrayList<SerialNo_Model>();


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

    public String getFromBatchNo() {
        return FromBatchNo;
    }

    public void setFromBatchNo(String fromBatchNo) {
        FromBatchNo = fromBatchNo;
    }

    public Date getSupPrdDate() {
        return SupPrdDate;
    }

    public void setSupPrdDate(Date supPrdDate) {
        SupPrdDate = supPrdDate;
    }

    public String getSupPrdBatch() {
        return SupPrdBatch;
    }

    public void setSupPrdBatch(String supPrdBatch) {
        SupPrdBatch = supPrdBatch;
    }

    public String getRowNoDel() {
        return RowNoDel;
    }

    public void setRowNoDel(String rowNoDel) {
        RowNoDel = rowNoDel;
    }

    public Date getArrivalDate() {
        return ArrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        ArrivalDate = arrivalDate;
    }

    public Date getShipmentDate() {
        return ShipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        ShipmentDate = shipmentDate;
    }

    public Date getArrStockDate() {
        return ArrStockDate;
    }

    public void setArrStockDate(Date arrStockDate) {
        ArrStockDate = arrStockDate;
    }

    public ArrayList<BarCodeInfo> getLstBarCode() {
        return lstBarCode;
    }

    public void setLstBarCode(ArrayList<BarCodeInfo> lstBarCode) {
        this.lstBarCode = lstBarCode;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getMoveType() {
        return MoveType;
    }

    public void setMoveType(String moveType) {
        MoveType = moveType;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
    }

    public int getIsSerial() {
        return IsSerial;
    }

    public void setIsSerial(int isSerial) {
        IsSerial = isSerial;
    }

    public String getStorageLoc() {
        return StorageLoc;
    }

    public void setStorageLoc(String storageLoc) {
        StorageLoc = storageLoc;
    }

    public int getInStockID() {
        return InStockID;
    }

    public void setInStockID(int inStockID) {
        InStockID = inStockID;
    }

    public Float getInStockQty() {
        return InStockQty;
    }

    public void setInStockQty(Float inStockQty) {
        InStockQty = inStockQty;
    }

  //  public List<SerialNo_Model> getLstSerialNo() {
     //   return lstSerialNo;
  //  }

   // public void setLstSerialNo(List<SerialNo_Model> lstSerialNo) {
   //     this.lstSerialNo = lstSerialNo;
  //  }

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

    public Float getQualityQty() {
        return QualityQty;
    }

    public void setQualityQty(Float qualityQty) {
        QualityQty = qualityQty;
    }

    public String getQualityType() {
        return QualityType;
    }

    public void setQualityType(String qualityType) {
        QualityType = qualityType;
    }

    public String getQualityUserNo() {
        return QualityUserNo;
    }

    public void setQualityUserNo(String qualityUserNo) {
        QualityUserNo = qualityUserNo;
    }

    public Float getReceiveQty() {
        return ReceiveQty;
    }

    public void setReceiveQty(Float receiveQty) {
        ReceiveQty = receiveQty;
    }

    public Float getRemainQty() {
        return RemainQty;
    }

    public void setRemainQty(Float remainQty) {
        RemainQty = remainQty;
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

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

//    public List<PalletDetail_Model> getLstPallet() {
//        return lstPallet;
//    }
//
//    public void setLstPallet(List<PalletDetail_Model> lstPallet) {
//        this.lstPallet = lstPallet;
//    }

    public String getSaleCode() {
        return SaleCode;
    }

    public void setSaleCode(String saleCode) {
        SaleCode = saleCode;
    }

    public String getSupplierNo() {
        return SupplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        SupplierNo = supplierNo;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptDetail_Model that = (ReceiptDetail_Model) o;

        return MaterialNo.equals(that.MaterialNo) && RowNo.equals(that.RowNo) && RowNoDel.equals(that.RowNoDel);

    }

    @Override
    public ReceiptDetail_Model clone() throws CloneNotSupportedException {
        ReceiptDetail_Model inStockDetail_model=null;
        inStockDetail_model=(ReceiptDetail_Model)super.clone();
        return inStockDetail_model;
    }

    @Override
    public int hashCode() {
        return MaterialNo.hashCode();
    }


    public ReceiptDetail_Model() {
    }

    public ReceiptDetail_Model(String MaterialNo,String RowNo,String RowNoDel) {
        this.MaterialNo=MaterialNo;
        this.RowNo=RowNo;
        this.RowNoDel=RowNoDel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.InStockID);
        dest.writeString(this.RowNo);
        dest.writeString(this.RowNoDel);
        dest.writeString(this.MaterialNo);
        dest.writeString(this.MaterialDesc);
        dest.writeValue(this.InStockQty);
        dest.writeValue(this.ReceiveQty);
        dest.writeString(this.Unit);
        dest.writeString(this.StorageLoc);
        dest.writeString(this.Plant);
        dest.writeString(this.PlantName);
        dest.writeValue(this.QualityQty);
        dest.writeValue(this.UnQualityQty);
        dest.writeString(this.QualityType);
        dest.writeString(this.QualityUserNo);
        dest.writeString(this.UnitName);
        dest.writeValue(this.RemainQty);
        dest.writeValue(this.ScanQty);
        dest.writeString(this.IsSpcBatch);
        dest.writeString(this.QcCode);
        dest.writeString(this.QcDesc);
        dest.writeTypedList(this.lstBarCode);
        dest.writeString(this.SaleName);
        dest.writeString(this.VoucherNo);
        dest.writeInt(this.IsSerial);
        dest.writeString(this.SaleCode);
        dest.writeString(this.SupplierNo);
        dest.writeString(this.SupplierName);
        dest.writeLong(this.SupPrdDate != null ? this.SupPrdDate.getTime() : -1);
        dest.writeString(this.SupPrdBatch);
        dest.writeString(this.BatchNo);
        dest.writeString(this.PartNo);
        dest.writeString(this.MoveType);
        dest.writeString(this.Company);
        dest.writeString(this.Department);
        dest.writeLong(this.ArrivalDate != null ? this.ArrivalDate.getTime() : -1);
        dest.writeLong(this.ShipmentDate != null ? this.ShipmentDate.getTime() : -1);
        dest.writeLong(this.ArrStockDate != null ? this.ArrStockDate.getTime() : -1);
        dest.writeString(this.FromBatchNo);
        dest.writeString(this.FromErpAreaNo);
        dest.writeString(this.FromErpWarehouse);
        dest.writeString(this.ToBatchNo);
        dest.writeString(this.ToErpAreaNo);
        dest.writeString(this.ToErpWarehouse);
    }

    protected ReceiptDetail_Model(Parcel in) {
        super(in);
        this.InStockID = in.readInt();
        this.RowNo = in.readString();
        this.RowNoDel = in.readString();
        this.MaterialNo = in.readString();
        this.MaterialDesc = in.readString();
        this.InStockQty = (Float) in.readValue(Float.class.getClassLoader());
        this.ReceiveQty = (Float) in.readValue(Float.class.getClassLoader());
        this.Unit = in.readString();
        this.StorageLoc = in.readString();
        this.Plant = in.readString();
        this.PlantName = in.readString();
        this.QualityQty = (Float) in.readValue(Float.class.getClassLoader());
        this.UnQualityQty = (Float) in.readValue(Float.class.getClassLoader());
        this.QualityType = in.readString();
        this.QualityUserNo = in.readString();
        this.UnitName = in.readString();
        this.RemainQty = (Float) in.readValue(Float.class.getClassLoader());
        this.ScanQty = (Float) in.readValue(Float.class.getClassLoader());
        this.IsSpcBatch = in.readString();
        this.QcCode = in.readString();
        this.QcDesc = in.readString();
        this.lstBarCode = in.createTypedArrayList(BarCodeInfo.CREATOR);
        this.SaleName = in.readString();
        this.VoucherNo = in.readString();
        this.IsSerial = in.readInt();
        this.SaleCode = in.readString();
        this.SupplierNo = in.readString();
        this.SupplierName = in.readString();
        long tmpSupPrdDate = in.readLong();
        this.SupPrdDate = tmpSupPrdDate == -1 ? null : new Date(tmpSupPrdDate);
        this.SupPrdBatch = in.readString();
        this.BatchNo = in.readString();
        this.PartNo = in.readString();
        this.MoveType = in.readString();
        this.Company = in.readString();
        this.Department = in.readString();
        long tmpArrivalDate = in.readLong();
        this.ArrivalDate = tmpArrivalDate == -1 ? null : new Date(tmpArrivalDate);
        long tmpShipmentDate = in.readLong();
        this.ShipmentDate = tmpShipmentDate == -1 ? null : new Date(tmpShipmentDate);
        long tmpArrStockDate = in.readLong();
        this.ArrStockDate = tmpArrStockDate == -1 ? null : new Date(tmpArrStockDate);
        this.FromBatchNo = in.readString();
        this.FromErpAreaNo = in.readString();
        this.FromErpWarehouse = in.readString();
        this.ToBatchNo = in.readString();
        this.ToErpAreaNo = in.readString();
        this.ToErpWarehouse = in.readString();
    }

    public static final Creator<ReceiptDetail_Model> CREATOR = new Creator<ReceiptDetail_Model>() {
        @Override
        public ReceiptDetail_Model createFromParcel(Parcel source) {
            return new ReceiptDetail_Model(source);
        }

        @Override
        public ReceiptDetail_Model[] newArray(int size) {
            return new ReceiptDetail_Model[size];
        }
    };
}


