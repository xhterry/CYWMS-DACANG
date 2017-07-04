package com.xx.chinetek.model;

/**
 * Created by GHOST on 2017/6/9.
 */

public class URLModel {

    private static URLModel instance;

    public static URLModel  GetURL() {
        return  new URLModel();
    }

    public static String IPAdress="10.2.32.191";//"";
    public static int Port=9000;//9000;
    public static String  LastContent="AndroidService.svc/";
    String  GetWCFAdress(){
        return  "http://"+IPAdress+":"+Port+"/"+LastContent;
    }

    public String UserLoginADF = GetWCFAdress()+"UserLoginADF"; //用户登录
    public String GetT_InStockListADF = GetWCFAdress()+"GetT_InStockListADF"; //收货表头
    public String GetT_InTaskListADF = GetWCFAdress()+"GetT_InTaskListADF"; //上架表头
    public String GetT_OutStockListADF = GetWCFAdress()+"GetT_OutStockListADF"; //下架复核表头
    public String GetT_InStockDetailListByHeaderIDADF = GetWCFAdress()+"GetT_InStockDetailListByHeaderIDADF"; //收货表体
    public String GetT_InTaskDetailListByHeaderIDADF = GetWCFAdress()+"GetT_InTaskDetailListByHeaderIDADF"; //上架表体
    public String GetT_OutStockDetailListByHeaderIDADF = GetWCFAdress()+"GetT_OutStockDetailListByHeaderIDADF"; //下架复核表体
    public String SaveT_InStockDetailADF = GetWCFAdress()+"SaveT_InStockDetailADF"; //提交收货
    public String UpadteT_QualityUserADF = GetWCFAdress()+"UpadteT_QualityUserADF"; //更新取样人
    public String SaveT_InStockTaskDetailADF = GetWCFAdress()+"SaveT_InStockTaskDetailADF"; //提交上架
    //public static String GetT_SerialNoADF=GetWCFAdress()+"GetT_SerialNoADF"; //获取条码信息
    public String GetT_SerialNoByPalletADF=GetWCFAdress()+"GetT_SerialNoByPalletADF";//获取条码信息
    public String GetT_GetT_OutBarCodeInfoByBoxADF=GetWCFAdress()+"GetT_OutBarCodeInfoByBoxADF";//获取拆托条码信息
    public String GetT_PalletDetailByNoADF=GetWCFAdress()+"GetT_PalletDetailByNoADF";//获取托盘信息
    public String GetT_PalletDetailByBarCodeADF=GetWCFAdress()+"GetT_PalletDetailByBarCodeADF";//库存获取托盘信息
    public String GetT_ScanInStockModelADF=GetWCFAdress()+"GetT_ScanInStockModelADF";//上架扫描条码或者托盘条码
    public String SaveT_PalletDetailADF=GetWCFAdress()+"SaveT_PalletDetailADF";//保存组托信息
    public String Delete_PalletORBarCodeADF=GetWCFAdress()+"Delete_PalletORBarCodeADF";//删除组托信息
    public String SaveT_BarCodeToStockADF=GetWCFAdress()+"SaveT_BarCodeToStockADF";//装箱拆箱提交
    public String Get_PalletDetailByVoucherNo=GetWCFAdress()+"Get_PalletDetailByVoucherNo";//复核获取托盘信息
    public String Del_PalletOrSerialNo=GetWCFAdress()+"Del_PalletOrSerialNo";//复核删除托盘信息
    public String GetT_QualityListADF=GetWCFAdress()+"GetT_QualityListADF";//获取质检表头信息
    public String QualityDetailListByHeaderIDADF=GetWCFAdress()+"QualityDetailListByHeaderIDADF";//获取质检表体信息
    public String GetT_OutBarCodeInfoForQuanADF=GetWCFAdress()+"GetT_OutBarCodeInfoForQuanADF";//获取质检扫描条码信息
    public String SaveT_QuanlitySampADF=GetWCFAdress()+"SaveT_QuanlitySampADF";//提交质检明细
    public String SaveT_OutStockTaskDetailADF=GetWCFAdress()+"SaveT_OutStockTaskDetailADF";//提交下架明细
    public String GetT_OutTaskDetailListByHeaderIDADF=GetWCFAdress()+"GetT_OutTaskDetailListByHeaderIDADF";//获取下架表体信息
    public String GetT_OutTaskListADF=GetWCFAdress()+"GetT_OutTaskListADF";//获取下架表头信息
}
