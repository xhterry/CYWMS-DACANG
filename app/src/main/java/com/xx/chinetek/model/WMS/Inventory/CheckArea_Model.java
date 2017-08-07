package com.xx.chinetek.model.WMS.Inventory;

/**
 * Created by GHOST on 2017/1/24.
 */

public class CheckArea_Model {
    private int ID ;
    private String AREANO	        ;
    private String AREANAME	     ;
    private String CONTACTUSER	     ;
    private String CONTACTPHONE	 ;
    private String ADDRESS	         ;
    private String LOCATIONDESC	 ;
    private String CREATER	         ;
    private String MODIFYER	     ;
    private int AREAID;
    private String warehouseno;

    public String getWarehouseno() {
        return warehouseno;
    }

    public void setWarehouseno(String warehouseno) {
        this.warehouseno = warehouseno;
    }

    public int getAREAID() {
        return AREAID;
    }

    public void setAREAID(int AREAID) {
        this.AREAID = AREAID;
    }

    private Boolean ischeck;

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getAREANAME() {
        return AREANAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
    }

    public String getAREANO() {
        return AREANO;
    }

    public void setAREANO(String AREANO) {
        this.AREANO = AREANO;
    }

    public String getCONTACTPHONE() {
        return CONTACTPHONE;
    }

    public void setCONTACTPHONE(String CONTACTPHONE) {
        this.CONTACTPHONE = CONTACTPHONE;
    }

    public String getCONTACTUSER() {
        return CONTACTUSER;
    }

    public void setCONTACTUSER(String CONTACTUSER) {
        this.CONTACTUSER = CONTACTUSER;
    }

    public String getCREATER() {
        return CREATER;
    }

    public void setCREATER(String CREATER) {
        this.CREATER = CREATER;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(Boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getLOCATIONDESC() {
        return LOCATIONDESC;
    }

    public void setLOCATIONDESC(String LOCATIONDESC) {
        this.LOCATIONDESC = LOCATIONDESC;
    }

    public String getMODIFYER() {
        return MODIFYER;
    }

    public void setMODIFYER(String MODIFYER) {
        this.MODIFYER = MODIFYER;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckArea_Model that = (CheckArea_Model) o;

        return AREANO.equals(that.AREANO) ;

    }
}
