package com.xx.chinetek.model.Production.Manage;

import android.os.Parcel;
import android.os.Parcelable;

import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.User.UserInfo;

import java.util.ArrayList;

/**
 * Created by GHOST on 2017/7/17.
 */

public class LineManageModel extends Base_Model implements Parcelable {

    private String WoVoucherNo; //WMS工单号

    private String WoErpVoucherNo;//ERP工单号

    private String WoBatchNo; //工单号批次

    private String EquipID;//设备号

    private Float PreProductNum;//预生产数

    private String ProductLineNo;//产线编号

    private String ProductTeamNo;//班组编号

    private String StartTime;//工单开始时间

    private WoModel woModel;//工单原料信息

    private ArrayList<UserInfo> userInfos=new ArrayList<>();//人员列表

    public Float getPreProductNum() {
        return PreProductNum;
    }

    public void setPreProductNum(Float preProductNum) {
        PreProductNum = preProductNum;
    }

    public ArrayList<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(ArrayList<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public String getEquipID() {
        return EquipID;
    }

    public void setEquipID(String equipID) {
        EquipID = equipID;
    }

    public String getProductLineNo() {
        return ProductLineNo;
    }

    public void setProductLineNo(String productLineNo) {
        ProductLineNo = productLineNo;
    }

    public String getProductTeamNo() {
        return ProductTeamNo;
    }

    public void setProductTeamNo(String productTeamNo) {
        ProductTeamNo = productTeamNo;
    }

    private String EndTime;//工单结束时间

    public String getWoVoucherNo() {
        return WoVoucherNo;
    }

    public void setWoVoucherNo(String woVoucherNo) {
        WoVoucherNo = woVoucherNo;
    }

    public String getWoErpVoucherNo() {
        return WoErpVoucherNo;
    }

    public void setWoErpVoucherNo(String woErpVoucherNo) {
        WoErpVoucherNo = woErpVoucherNo;
    }

    public String getWoBatchNo() {
        return WoBatchNo;
    }

    public void setWoBatchNo(String woBatchNo) {
        WoBatchNo = woBatchNo;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public WoModel getWoModel() {
        return woModel;
    }

    public void setWoModel(WoModel woModel) {
        this.woModel = woModel;
    }

    public LineManageModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.WoVoucherNo);
        dest.writeString(this.WoErpVoucherNo);
        dest.writeString(this.WoBatchNo);
        dest.writeString(this.EquipID);
        dest.writeValue(this.PreProductNum);
        dest.writeString(this.ProductLineNo);
        dest.writeString(this.ProductTeamNo);
        dest.writeString(this.StartTime);
        dest.writeParcelable(this.woModel, flags);
        dest.writeTypedList(this.userInfos);
        dest.writeString(this.EndTime);
    }

    protected LineManageModel(Parcel in) {
        super(in);
        this.WoVoucherNo = in.readString();
        this.WoErpVoucherNo = in.readString();
        this.WoBatchNo = in.readString();
        this.EquipID = in.readString();
        this.PreProductNum = (Float) in.readValue(Float.class.getClassLoader());
        this.ProductLineNo = in.readString();
        this.ProductTeamNo = in.readString();
        this.StartTime = in.readString();
        this.woModel = in.readParcelable(WoModel.class.getClassLoader());
        this.userInfos = in.createTypedArrayList(UserInfo.CREATOR);
        this.EndTime = in.readString();
    }

    public static final Creator<LineManageModel> CREATOR = new Creator<LineManageModel>() {
        @Override
        public LineManageModel createFromParcel(Parcel source) {
            return new LineManageModel(source);
        }

        @Override
        public LineManageModel[] newArray(int size) {
            return new LineManageModel[size];
        }
    };
}
