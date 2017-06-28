package com.xx.chinetek.model.Production;

/**
 * Created by GHOST on 2017/5/23.
 */

public class ReportOutputModel {

    private String productReportID;
    private String ReportBatch;
    private Float ReportNum;
    private String LastReportTime;

    public String getProductReportID() {
        return productReportID;
    }

    public void setProductReportID(String productReportID) {
        this.productReportID = productReportID;
    }

    public String getReportBatch() {
        return ReportBatch;
    }

    public void setReportBatch(String reportBatch) {
        ReportBatch = reportBatch;
    }

    public Float getReportNum() {
        return ReportNum;
    }

    public void setReportNum(Float reportNum) {
        ReportNum = reportNum;
    }

    public String getLastReportTime() {
        return LastReportTime;
    }

    public void setLastReportTime(String lastReportTime) {
        LastReportTime = lastReportTime;
    }
}
