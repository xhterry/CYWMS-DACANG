package com.xx.chinetek.cyproduct.LineStockIn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.Pallet.CombinPallet;
import com.xx.chinetek.adapter.product.LineStockIn.LineStockInMaterialItemAdapter;
import com.xx.chinetek.adapter.product.LineStockIn.LineStockInMaterialItemymhtuiAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.InnerMove.InnerMoveDetail;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.Receiption.ReceiptionScan;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Production.LineStockIn.LineStockInProductModel;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
import com.xx.chinetek.model.User.WareHouseInfo;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.model.WMS.Stock.AreaInfo_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.DoubleClickCheck;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_product_backmaterialproduct_scan)
public class LineStockInBackProduct extends BaseActivity {

    String TAG_GetPalletDetailByBarCode_Product="LineStockInProduct_GetPalletDetailByBarCode_Product";
    String TAG_SaveModeListForT_StockT="LineStockInProduct_SaveModeListForT_StockT";

    private final int RESULT_Msg_GetPalletDetailByBarCode_Product=102;
    private final int RESULT_SaveModeListForT_StockT=101;

    String TAG_Get_WODetailInfo = "LineStockInBackProduct_Print_Outlabel";
    private final int RESULT_Get_WODetailInfo = 103;

    String TAG_SaveBarcodeListInStockForTuiLiao = "LineStockInBackProduct_SaveBarcodeListInStockForTuiLiao";
    private final int RESULT_SaveBarcodeListInStockForTuiLiao = 104;

    String TAG_GetChengDataByErpVoucherNoBatchno = "LineStockInBackProduct_GetChengDataByErpVoucherNoBatchno";
    private final int RESULT_GetChengDataByErpVoucherNoBatchno = 105;

    String TAG_GetAreaModelADF="LineStockInBackproduct_GetT_SaveInStockModelADF";
    private final int RESULT_Msg_GetAreaModelADF=106;

    String TAG_Get_barcode = "LineStockInBackProduct_Getbarcode";
    private final int RESULT_Get_barcode = 107;

    String TAG_Get_GetKouLiaoRecord = "LineStockInBackProduct_GetKouLiaoRecord";
    private final int RESULT_Get_GetKouLiaoRecord = 108;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Get_GetKouLiaoRecord:
                AnalysisGetT_GetKouLiaoRecordAD((String) msg.obj);
                break;
            case RESULT_Get_barcode:
                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
            case RESULT_GetChengDataByErpVoucherNoBatchno:
                AnalysisetRESULT_GetChengDataByErpVoucherNoBatchnoson((String) msg.obj);
                break;
            case RESULT_SaveBarcodeListInStockForTuiLiao:
                AnalysisetSaveBarcodeListInStockForTuiLiaoJson((String) msg.obj);
                break;
            case RESULT_SaveModeListForT_StockT:
                AnalysisetSaveModeListForT_StockTJson((String) msg.obj);
                break;
//            case RESULT_Msg_GetPalletDetailByBarCode_Product:
//                AnalysisetGetPalletDetailByBarCode_ProductJson((String) msg.obj);
//                break;
            case RESULT_Get_WODetailInfo:
                AnalysisGetT_RESULT_Get_WODetailInfoADFJson((String)msg.obj);
                break;
            case RESULT_Msg_GetAreaModelADF:
                AnalysisetT_GetAreaModelADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
                break;
        }
    }

    Context context = LineStockInBackProduct.this;
    @ViewInject(R.id.lsv_LineStockInProduct)
    ListView lsvLineStockInProduct;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_WareHousName)
    TextView txtWareHousName;
    @ViewInject(R.id.edt_LineStockInScanBarcode)
    EditText edtLineStockInScanBarcode;
    @ViewInject(R.id.editYArea)
    EditText editYArea;

    ArrayList<LineStockInProductModel> lineStockInProductModels;
    // ArrayList<BarCodeInfo> SumbitbarCodeInfos=null;
    LineStockInMaterialItemymhtuiAdapter lineStockInMaterialItemymhtuiAdapter;

    WoModel womodel;
    ArrayList<WoDetailModel> WoDetailModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.LineStockInReturnBillChoice), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        womodel=getIntent().getParcelableExtra("WoModel");
        GetWODetailInfo(String.valueOf(womodel.getID()));

        lineStockInProductModels=new ArrayList<>();
        txtWareHousName.setText(BaseApplication.userInfo.getWarehouseName());        //SumbitbarCodeInfos=new ArrayList<>();
        CommonUtil.setEditFocus(editYArea);
    }


    @Event(value = {R.id.butpost,R.id.butTui},type = View.OnClickListener.class)
    private void onbutClick(View view) {
        try{
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return;
            }
            if(R.id.butpost==view.getId()){
//                if(lineStockInProductModels!=null && lineStockInProductModels.size()!=0){
                    final Map<String, String> params = new HashMap<String, String>();
//                    ArrayList<BarCodeInfo> SumbitbarCodeInfos=new ArrayList<>();
//                    for (LineStockInProductModel lineStockInProduct:lineStockInProductModels) {
//                        if(lineStockInProduct.getBarCodeInfos()!=null && lineStockInProduct.getBarCodeInfos().size()!=0){
//                            SumbitbarCodeInfos.addAll(0,lineStockInProduct.getBarCodeInfos());
//                        }
//                    }

                    if(BarCodeInfosymh.size()!=0) {
                        String ModelJson = GsonUtil.parseModelToJson(BarCodeInfosymh);
                        //设置入库的货位ID
                        UerInfo user = BaseApplication.userInfo;
                        if(!WgFlag)
                        {
                            MessageBox.Show(context, "没有扫描正确库位！");
                            return;
                        }
                        user.setReceiveAreaID(LReceiveAreaID);
                        user.setWarehouseID(LWarehouseID);
                        user.setReceiveHouseID(LReceiveHouseID);
                        user.setReceiveWareHouseNo(LReceiveWareHouseNo);
                        user.setReceiveAreaNo(LReceiveAreaNo);
                        user.setSex(womodel.getID());
                        user.setErpVoucherNo(womodel.getErpVoucherNo());

                        String UserJson = GsonUtil.parseModelToJson(user);
                        params.put("UserJson", UserJson);
                        params.put("ModelJson", ModelJson);
                        LogUtil.WriteLog(LineStockInBackProduct.class, TAG_SaveBarcodeListInStockForTuiLiao, ModelJson);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveBarcodeListInStockForTuiLiao, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_SaveBarcodeListInStockForTuiLiao, null, URLModel.GetURL().SaveBarcodeListInStockForTuiLiao, params, null);
                    }
//                }
            }
            if(R.id.butTui==view.getId()){
                new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否确定对此工单进行扣料操作？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO 自动生成的方法
                                    final Map<String, String> params = new HashMap<String, String>();
                                    params.put("ErpVoucherNo", womodel.getErpVoucherNo());
                                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_GetKouLiaoRecord, "获取状态", context, mHandler, RESULT_Get_GetKouLiaoRecord, null, URLModel.GetURL().GetKouLiaoRecord, params, null);
                                }
                            }).setNegativeButton("取消", null).show();
                    return;
            }

        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
    }



    void AnalysisGetT_GetKouLiaoRecordAD(String result) {
        try {
            LogUtil.WriteLog(LineStockInBackProduct.class, TAG_Get_GetKouLiaoRecord, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("该工单已经做过扣料，是否再次扣料？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自动生成的方法
                                //已经扣料
                                final Map<String, String> params = new HashMap<String, String>();
                                String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                                params.put("UserJson", UserJson);
                                params.put("WoinfoID", String.valueOf(womodel.getID()));
                                if(womodel.getStrVoucherType().toString().equals("散装物料")){
                                    params.put("BatchNo", womodel.getWo_Batch().toString());
                                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetChengDataByErpVoucherNoBatchno, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_GetChengDataByErpVoucherNoBatchno, null, URLModel.GetURL().GetChengDataByErpVoucherNoBatchno, params, null);
                                }else{
                                    params.put("ErpVoucherNo", womodel.getErpVoucherNo().toString());
                                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetChengDataByErpVoucherNoBatchno, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_GetChengDataByErpVoucherNoBatchno, null, URLModel.GetURL().PostDaoKouForChengPinOrSemi, params, null);
                                }
                            }
                        }).setNegativeButton("取消", null).show();
                return;

            }else{
                final Map<String, String> params = new HashMap<String, String>();
                String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                params.put("UserJson", UserJson);
                params.put("WoinfoID", String.valueOf(womodel.getID()));
                if(womodel.getStrVoucherType().toString().equals("散装物料")){
                    params.put("BatchNo", womodel.getWo_Batch().toString());
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetChengDataByErpVoucherNoBatchno, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_GetChengDataByErpVoucherNoBatchno, null, URLModel.GetURL().GetChengDataByErpVoucherNoBatchno, params, null);
                }else{
                    params.put("ErpVoucherNo", womodel.getErpVoucherNo().toString());
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetChengDataByErpVoucherNoBatchno, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_GetChengDataByErpVoucherNoBatchno, null, URLModel.GetURL().PostDaoKouForChengPinOrSemi, params, null);
                }
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.toString());
        }
    }



    ArrayList<BarCodeInfo>  BarCodeInfosymh = new ArrayList<>();
    /*
   解析物料条码扫描
    */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        try {
            LogUtil.WriteLog(LineStockInBackProduct.class, TAG_GetPalletDetailByBarCode_Product,result);
            ReturnMsgModel<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<BarCodeInfo>>() {}.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                BarCodeInfo barCode = returnMsgModel.getModelJson();
                String MaterialNo=barCode.getMaterialNo();
                //判断扫描的物料是不是存在明细中
                boolean flag=false;
                for(WoDetailModel Model:WoDetailModels){
                    if(Model.getMaterialNo().toString().equals(MaterialNo))
                    {
                        flag=true;
                    }
                }
                if (!flag){
                    MessageBox.Show(context, "扫描条码信息和工单明细物料不匹配！");
                    CommonUtil.setEditFocus(edtLineStockInScanBarcode);
                    return;
                }

                if(BarCodeInfosymh.indexOf(barCode)!=-1){
                    BarCodeInfosymh.remove(barCode);
//                    new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // TODO 自动生成的方法
//                                    BarCodeInfosymh.remove(barCode);
//                                }
//                            }).setNegativeButton("取消", null).show();
//                    return;
                }else{
                    BarCodeInfosymh.add(barCode);
                }


                BindListVIew(BarCodeInfosymh);
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
//                Bindbarcode(BarCodeInfosymh);
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }



//
//        LogUtil.WriteLog(CombinPallet.class, TAG_Get_barcode,result);
//        try {
//            ReturnMsgModel<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<BarCodeInfo>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                barCodeM = returnMsgModel.getModelJson();
//                //判断扫描的物料是不是存在明细中
//                boolean flag=false;
//                for(WoDetailModel Model:WoDetailModels){
//                    if(Model.getMaterialNo().toString().equals(barCodeM.getMaterialNo().toString()))
//                    {
//                        flag=true;
//                    }
//                }
//                if (!flag){
//                    MessageBox.Show(context, "扫描条码信息和工单明细物料不匹配！");
//                    CommonUtil.setEditFocus(editBarcode);
//                    return;
//                }
//                txtBatch.setText(barCodeM.getBatchNo());
//                txtMaName.setText(barCodeM.getMaterialDesc());
//                if(barCodeM.getLabelMark().toString().equals("OutSanZhuang")||barCodeM.getLabelMark().toString().equals("OutR")){
//                    editNum.setText("");
//                }else{
//                    editNum.setText(barCodeM.getQty().toString());
//                }
////                editNum.setText(barCodeM.getQty().toString());
//            } else {
//                MessageBox.Show(context,returnMsgModel.getMessage());
//            }
//        }catch (Exception e){
//            MessageBox.Show(context,e.toString());
//        }finally {
//            CommonUtil.setEditFocus(editBarcode);
//        }
//        CommonUtil.setEditFocus(editBarcode);
    }


    @Event(value = R.id.editYArea,type = View.OnKeyListener.class)
    private  boolean edtyarea(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code = editYArea.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                CommonUtil.setEditFocus(editYArea);
                return true;
            }
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("AreaNo", code);
            LogUtil.WriteLog(LineStockInBackProduct.class, TAG_GetAreaModelADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetAreaModelADF, null,  URLModel.GetURL().GetAreaModelADF, params, null);

        }
        return false;
    }


    private int LReceiveAreaID=0;
    private int LReceiveHouseID=0;
    private int LWarehouseID=0;
    private String LReceiveWareHouseNo="";
    private String LReceiveAreaNo="";

    private boolean WgFlag=false;
    void AnalysisetT_GetAreaModelADFJson(String result){
        try {
            LogUtil.WriteLog(LineStockInMaterial.class, TAG_GetAreaModelADF,result);
            ReturnMsgModel<AreaInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo_Model>>() {}.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                LReceiveAreaID=returnMsgModel.getModelJson().getID();
                LReceiveHouseID=returnMsgModel.getModelJson().getHouseID();
                LWarehouseID=returnMsgModel.getModelJson().getWarehouseID();
                LReceiveWareHouseNo=returnMsgModel.getModelJson().getWarehouseNo();
                LReceiveAreaNo=returnMsgModel.getModelJson().getAreaNo();
                if(String.valueOf(BaseApplication.userInfo.getWarehouseID()).equals(String.valueOf(LWarehouseID))){
                    WgFlag=true;
                }else{
                    MessageBox.Show(context,"扫描的货位不在登陆人的仓库下！");
                    editYArea.setText("");
                    CommonUtil.setEditFocus(editYArea);
                }
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
                CommonUtil.setEditFocus(editYArea);
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
    }


    //获取工单明细
    private void GetWODetailInfo(String HID){
        try{
            final Map<String, String> params = new HashMap<String, String>();
            params.put("HeadId", HID);
            LogUtil.WriteLog(LineStockInReturn.class, TAG_Get_WODetailInfo, HID);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_WODetailInfo, getString(R.string.Msg_GetT_Wodetailinfo), context, mHandler, RESULT_Get_WODetailInfo, null,  URLModel.GetURL().GetWODetailInfo, params, null);
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
    }


    @Event(value =R.id.edt_LineStockInScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtLineStockInScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            try{
                String Fileter = edtLineStockInScanBarcode.getText().toString().trim();
                final Map<String, String> params = new HashMap<String, String>();
                params.put("SerialNo", Fileter);
                LogUtil.WriteLog(LineStockInReturn.class, TAG_Get_barcode, Fileter);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_barcode, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Get_barcode, null,  URLModel.GetURL().GetT_SerialNoADF, params, null);

            }catch (Exception ex){
                MessageBox.Show(context,ex.toString());
            }
        }

//            String code = edtLineStockInScanBarcode.getText().toString().trim();
//            if (TextUtils.isEmpty(code)) {
//                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
//                return true;
//            }
//            final Map<String, String> params = new HashMap<String, String>();
//            params.put("BarCode", code);
//            LogUtil.WriteLog(ReceiptionScan.class, TAG_GetPalletDetailByBarCode_Product, code);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetPalletDetailByBarCode_Product, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetPalletDetailByBarCode_Product, null,  URLModel.GetURL().GetPalletDetailByBarCode_Product, params, null);
//        }
        return false;
    }

    @Event(R.id.txt_WareHousName)
    private void txtWareHousNameClick(View view){
        SelectWareHouse();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            if (item.getItemId() == R.id.action_filter) {
                if (DoubleClickCheck.isFastDoubleClick(context)) {
                    return false;
                }
                //判断是否是散装物料
                if(womodel.getStrVoucherType().equals("散装物料")){
                    final Map<String, String> params = new HashMap<String, String>();
                    String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                    params.put("UserJson", UserJson);
                    params.put("BatchNo", womodel.getWo_Batch().toString());
                    params.put("WoinfoID", String.valueOf(womodel.getID()));
                    if(womodel.getStrVoucherType().toString().equals("散装物料")){
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetChengDataByErpVoucherNoBatchno, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_GetChengDataByErpVoucherNoBatchno, null, URLModel.GetURL().PostDaoKouForChengPinOrSemi, params, null);
                    }else{
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetChengDataByErpVoucherNoBatchno, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_GetChengDataByErpVoucherNoBatchno, null, URLModel.GetURL().GetChengDataByErpVoucherNoBatchno, params, null);
                    }

                }
                else{
                    //提交
                    if(lineStockInProductModels!=null && lineStockInProductModels.size()!=0){
                        final Map<String, String> params = new HashMap<String, String>();
                        ArrayList<BarCodeInfo> SumbitbarCodeInfos=new ArrayList<>();
                        for (LineStockInProductModel lineStockInProduct:lineStockInProductModels) {
                            if(lineStockInProduct.getBarCodeInfos()!=null && lineStockInProduct.getBarCodeInfos().size()!=0){
                                SumbitbarCodeInfos.addAll(0,lineStockInProduct.getBarCodeInfos());
                            }
                        }
                        if(SumbitbarCodeInfos.size()!=0) {
                            String ModelJson = GsonUtil.parseModelToJson(SumbitbarCodeInfos);
                            String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                            params.put("UserJson", UserJson);
                            params.put("ModelJson", ModelJson);
                            LogUtil.WriteLog(LineStockInBackProduct.class, TAG_SaveBarcodeListInStockForTuiLiao, ModelJson);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveBarcodeListInStockForTuiLiao, getString(R.string.Msg_SaveT_LineInStockProductlADF), context, mHandler, RESULT_SaveBarcodeListInStockForTuiLiao, null, URLModel.GetURL().SaveBarcodeListInStockForTuiLiao, params, null);
                        }



                    }
                }


            }
        }catch(Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }


        return super.onOptionsItemSelected(item);
    }


//    @Event(value = R.id.lsv_LineStockInProduct,type =  AdapterView.OnItemClickListener.class)
//    private  boolean lsvLineStockInProductClick(AdapterView<?> parent, View view, int position, long id){
//        if(id>=0) {
//            LineStockInProductModel lineStockInProduct=(LineStockInProductModel)lineStockInMaterialItemAdapter.getItem(position);
//            if (lineStockInProduct.getBarCodeInfos().size() != 0) {
//                Intent intent = new Intent(context, InnerMoveDetail.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("barCodeInfos", lineStockInProduct.getBarCodeInfos());
//                intent.putExtras(bundle);
//                startActivityLeft(intent);
//            }
//        }
//        return true;
//    }

    void AnalysisGetT_RESULT_Get_WODetailInfoADFJson(String result){
        try {
            LogUtil.WriteLog(LineStockInReturn.class, TAG_Get_WODetailInfo, result);
            ReturnMsgModelList<WoDetailModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoDetailModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                WoDetailModels=returnMsgModel.getModelJson();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

//    /*
//   扫描条码
//    */
//    void AnalysisetGetPalletDetailByBarCode_ProductJson(String result){
//        try {
//            LogUtil.WriteLog(LineStockInBackProduct.class, TAG_GetPalletDetailByBarCode_Product,result);
//            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {}.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                ArrayList<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
//
//                String MaterialNo=barCodeInfos.get(0).getMaterialNo();
//                //判断扫描的物料是不是存在明细中
//                boolean flag=false;
//                for(WoDetailModel Model:WoDetailModels){
//                    if(Model.getMaterialNo().toString().equals(MaterialNo))
//                    {
//                        flag=true;
//                    }
//                }
//                if (!flag){
//                    MessageBox.Show(context, "扫描条码信息和工单明细物料不匹配！");
//                    CommonUtil.setEditFocus(edtLineStockInScanBarcode);
//                    return;
//                }
//
//                Bindbarcode(barCodeInfos);
//            } else {
//                MessageBox.Show(context,returnMsgModel.getMessage());
//            }
//        }catch (Exception ex){
//            MessageBox.Show(context,ex.toString());
//        }
//        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
//    }




    void  AnalysisetRESULT_GetChengDataByErpVoucherNoBatchnoson(String result){
        try {
            LogUtil.WriteLog(LineStockInBackProduct.class, TAG_GetChengDataByErpVoucherNoBatchno,result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
            MessageBox.Show(context,returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
    }

    void  AnalysisetSaveBarcodeListInStockForTuiLiaoJson(String result){
        LogUtil.WriteLog(LineStockInBackProduct.class, TAG_SaveBarcodeListInStockForTuiLiao,result);
        ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
        try {
            MessageBox.Show(context,returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
    }

    void  AnalysisetSaveModeListForT_StockTJson(String result){
        LogUtil.WriteLog(LineStockInBackProduct.class, TAG_SaveModeListForT_StockT,result);
        ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
        try {
            MessageBox.Show(context,returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
    }

//    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
//        if (barCodeInfos != null && barCodeInfos.size() != 0) {
//            try {
//                String MaterialNo=barCodeInfos.get(0).getMaterialNo();
//                String BatchNo=barCodeInfos.get(0).getBatchNo();
//                String MaterialDesc=barCodeInfos.get(0).getMaterialDesc();
//                Float SumQty=0f;
//                for (BarCodeInfo barcodinfo:barCodeInfos) {
//                    SumQty= ArithUtil.add(SumQty,barcodinfo.getQty());
//                }
//                final Float sumQty=SumQty;
//                LineStockInProductModel templineStockIn=new LineStockInProductModel(MaterialNo,BatchNo);
//                final int index=lineStockInProductModels.indexOf(templineStockIn);
//                if(index!=-1){
//                    if(lineStockInProductModels.get(index).getBarCodeInfos().indexOf(barCodeInfos.get(0))!=-1){
//                        //MessageBox.Show(context,getString(R.string.Error_Barcode_hasScan));
//                        new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // TODO 自动生成的方法
//                                        lineStockInProductModels.get(index).getBarCodeInfos().removeAll(barCodeInfos);
//                                        lineStockInProductModels.get(index).setQty(ArithUtil.sub(lineStockInProductModels.get(index).getQty(),sumQty));
//                                        if( lineStockInProductModels.get(index).getBarCodeInfos().size()==0){
//                                            lineStockInProductModels.remove(index);
//                                        }
//                                        InitFrm(barCodeInfos.get(0));
//                                        BindListVIew(lineStockInProductModels);
//                                    }
//                                }).setNegativeButton("取消", null).show();
//                        return;
//                    }
//                    lineStockInProductModels.get(index).setQty(ArithUtil.add(lineStockInProductModels.get(index).getQty(),SumQty));
//                    lineStockInProductModels.get(index).getBarCodeInfos().addAll(0,barCodeInfos);
//                }else{
//                    templineStockIn.setMaterialDesc(MaterialDesc);
//                    templineStockIn.setQty(SumQty);
//                    if(templineStockIn.getBarCodeInfos()==null)
//                        templineStockIn.setBarCodeInfos(new ArrayList<BarCodeInfo>());
//                    templineStockIn.getBarCodeInfos().addAll(0,barCodeInfos);
//                    lineStockInProductModels.add(0,templineStockIn);
//                }
//                InitFrm(barCodeInfos.get(0));
//                BindListVIew(lineStockInProductModels);
//            }catch (Exception ex){
//                MessageBox.Show(context,ex.getMessage());
//                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
//            }
//
//        }
//    }


    /*
   提交入库
    */
//    void AnalysisSaveT_InStockTaskDetailADFJson(String result){
//        try {
//            LogUtil.WriteLog(LineStockInProduct.class, TAG_SaveT_InStockTaskDetailADF,result);
//            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
//            }.getType());
//            MessageBox.Show(context, returnMsgModel.getMessage());
//            if(returnMsgModel.getHeaderStatus().equals("S")) {
//                ClearFrm();
//                GetInStockTaskDetail(inStockTaskInfoModel);
//            }
//            CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
//        }
//    }

    void InitFrm(BarCodeInfo barCodeInfo){
        try {
            if (barCodeInfo != null) {
                txtCompany.setText(barCodeInfo.getStrongHoldName());
                txtBatch.setText(barCodeInfo.getBatchNo());
                txtStatus.setText("");
                txtMaterialName.setText(barCodeInfo.getMaterialDesc());
                txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtLineStockInScanBarcode);
        }
    }

    private void BindListVIew(ArrayList<BarCodeInfo> lineStockInProductModels) {
        lineStockInMaterialItemymhtuiAdapter=new LineStockInMaterialItemymhtuiAdapter(context,lineStockInProductModels);
        lsvLineStockInProduct.setAdapter(lineStockInMaterialItemymhtuiAdapter);
    }



    void ClearFrm(){
//        lineStockInProductModels = new ArrayList<>();
        BarCodeInfosymh=new ArrayList<>();
        edtLineStockInScanBarcode.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtEDate.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
        BindListVIew(BarCodeInfosymh);
    }


    int  SelectWareHouseID=-1;
    void SelectWareHouse(){
        if (BaseApplication.userInfo==null || BaseApplication.userInfo.getLstWarehouse() == null) return;
        List<String> wareHouses = new ArrayList<String>();
        if(BaseApplication.userInfo.getLstWarehouse().size()>1) {
            for (WareHouseInfo warehouse : BaseApplication.userInfo.getLstWarehouse()) {
                if (warehouse.getWareHouseName() != null && !warehouse.getWareHouseName().equals("")) {
                    wareHouses.add(warehouse.getWareHouseName());
                }
            }
            final String[] items = wareHouses.toArray(new String[0]);
            new AlertDialog.Builder(context) .setCancelable(false).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            SelectWareHouseID = BaseApplication.userInfo.getLstWarehouse().get(which).getID();
                            txtWareHousName.setText(select_item);
                            dialog.dismiss();
                        }
                    }).show();
        }else{
            SelectWareHouseID = BaseApplication.userInfo.getLstWarehouse().get(0).getID();
            txtWareHousName.setText(BaseApplication.userInfo.getLstWarehouse().get(0).getWareHouseName());
        }
    }




}
