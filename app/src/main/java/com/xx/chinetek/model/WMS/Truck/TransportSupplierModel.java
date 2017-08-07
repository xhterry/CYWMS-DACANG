package com.xx.chinetek.model.WMS.Truck;

import android.os.Parcel;

import com.xx.chinetek.model.Base_Model;

/**
 * Created by GHOST on 2017/8/3.
 */

public class TransportSupplierModel extends Base_Model{
    public int TDID;

    /// <summary>
    /// wms单号
    /// </summary>
    public String VoucherNo;

    /// <summary>
    /// 车牌
    /// </summary>
    public String PlateNumber;


    public Float IsDel;

    /// <summary>
    /// 体积
    /// </summary>
    public Float Volume;

    /// <summary>
    /// 重量
    /// </summary>
    public Float Weight;

    /// <summary>
    /// 件数
    /// </summary>
    public Float CartonNum;

    /// <summary>
    /// 运费
    /// </summary>
    public Float Feight;

    /// <summary>
    /// 承运商ID
    /// </summary>
    public int TransportSupplierID;

    /// <summary>
    /// 承运商
    /// </summary>
    public String TransportSupplierName;

    /// <summary>
    /// 目的地
    /// </summary>
    public String Destina;

    /// <summary>
    /// 备注
    /// </summary>
    public String Remark;


    public int getTDID() {
        return TDID;
    }

    public void setTDID(int TDID) {
        this.TDID = TDID;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public Float getIsDel() {
        return IsDel;
    }

    public void setIsDel(Float isDel) {
        IsDel = isDel;
    }

    public Float getVolume() {
        return Volume;
    }

    public void setVolume(Float volume) {
        Volume = volume;
    }

    public Float getWeight() {
        return Weight;
    }

    public void setWeight(Float weight) {
        Weight = weight;
    }

    public Float getCartonNum() {
        return CartonNum;
    }

    public void setCartonNum(Float cartonNum) {
        CartonNum = cartonNum;
    }

    public Float getFeight() {
        return Feight;
    }

    public void setFeight(Float feight) {
        Feight = feight;
    }

    public int getTransportSupplierID() {
        return TransportSupplierID;
    }

    public void setTransportSupplierID(int transportSupplierID) {
        TransportSupplierID = transportSupplierID;
    }

    public String getTransportSupplierName() {
        return TransportSupplierName;
    }

    public void setTransportSupplierName(String transportSupplierName) {
        TransportSupplierName = transportSupplierName;
    }

    public String getDestina() {
        return Destina;
    }

    public void setDestina(String destina) {
        Destina = destina;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.TDID);
        dest.writeString(this.VoucherNo);
        dest.writeString(this.PlateNumber);
        dest.writeValue(this.IsDel);
        dest.writeValue(this.Volume);
        dest.writeValue(this.Weight);
        dest.writeValue(this.CartonNum);
        dest.writeValue(this.Feight);
        dest.writeInt(this.TransportSupplierID);
        dest.writeString(this.TransportSupplierName);
        dest.writeString(this.Destina);
        dest.writeString(this.Remark);
    }

    public TransportSupplierModel() {
    }

    protected TransportSupplierModel(Parcel in) {
        super(in);
        this.TDID = in.readInt();
        this.VoucherNo = in.readString();
        this.PlateNumber = in.readString();
        this.IsDel = (Float) in.readValue(Float.class.getClassLoader());
        this.Volume = (Float) in.readValue(Float.class.getClassLoader());
        this.Weight = (Float) in.readValue(Float.class.getClassLoader());
        this.CartonNum = (Float) in.readValue(Float.class.getClassLoader());
        this.Feight = (Float) in.readValue(Float.class.getClassLoader());
        this.TransportSupplierID = in.readInt();
        this.TransportSupplierName = in.readString();
        this.Destina = in.readString();
        this.Remark = in.readString();
    }

    public static final Creator<TransportSupplierModel> CREATOR = new Creator<TransportSupplierModel>() {
        @Override
        public TransportSupplierModel createFromParcel(Parcel source) {
            return new TransportSupplierModel(source);
        }

        @Override
        public TransportSupplierModel[] newArray(int size) {
            return new TransportSupplierModel[size];
        }
    };
}
