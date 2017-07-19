package com.xx.chinetek.model.WMS.Inventory;

/**
 * Created by GHOST on 2017/1/24.
 */

public class CheckDet_Model {
    private String CHECKNO;
    private String AREANO;
    private String MATERIALNO;
    private String MATERIALDESC;
    private String SERIALNO;
    private String BATCHNO;
    private Float QTY ;
    public int AREAID;
    public int MATERIALID;

    public int getAREAID() {
        return AREAID;
    }

    public void setAREAID(int AREAID) {
        this.AREAID = AREAID;
    }

    public int getMATERIALID() {
        return MATERIALID;
    }

    public void setMATERIALID(int MATERIALID) {
        this.MATERIALID = MATERIALID;
    }

    public String getAREANO() {
        return AREANO;
    }

    public void setAREANO(String AREANO) {
        this.AREANO = AREANO;
    }

    public String getBATCHNO() {
        return BATCHNO;
    }

    public void setBATCHNO(String BATCHNO) {
        this.BATCHNO = BATCHNO;
    }

    public String getCHECKNO() {
        return CHECKNO;
    }

    public void setCHECKNO(String CHECKNO) {
        this.CHECKNO = CHECKNO;
    }

    public String getMATERIALDESC() {
        return MATERIALDESC;
    }

    public void setMATERIALDESC(String MATERIALDESC) {
        this.MATERIALDESC = MATERIALDESC;
    }

    public String getMATERIALNO() {
        return MATERIALNO;
    }

    public void setMATERIALNO(String MATERIALNO) {
        this.MATERIALNO = MATERIALNO;
    }

    public Float getQTY() {
        return QTY;
    }

    public void setQTY(Float QTY) {
        this.QTY = QTY;
    }

    public String getSERIALNO() {
        return SERIALNO;
    }

    public void setSERIALNO(String SERIALNO) {
        this.SERIALNO = SERIALNO;
    }
}
