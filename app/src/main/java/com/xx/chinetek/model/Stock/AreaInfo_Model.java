package com.xx.chinetek.model.Stock;

/**
 * Created by GHOST on 2017/1/20.
 */

public class AreaInfo_Model {

    public String WarehouseNo ;
    public String HouseNo;
    private String AreaNo;
    private int WarehouseID;
    private int HouseID;
    private int ID;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String areaNo) {
        AreaNo = areaNo;
    }

    public String getHouseNo() {
        return HouseNo;
    }

    public void setHouseNo(String houseNo) {
        HouseNo = houseNo;
    }

    public String getWarehouseNo() {
        return WarehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
    }
}
