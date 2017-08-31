package com.xx.chinetek.model;

/**
 * Created by GHOST on 2017/8/13.
 */

public class CheckNumRefMaterial {
    private  boolean ischeck; //转换是否成功
    private  Float CheckQty; //转换后数量
    private  String ErrMsg;  //错误信息

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public Float getCheckQty() {
        return CheckQty;
    }

    public void setCheckQty(Float checkQty) {
        CheckQty = checkQty;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
}
