package com.xx.chinetek.cyproduct.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.Pallet.CombinPallet;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cyproduct.LineStockIn.LineStockInProduct;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
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

@ContentView(R.layout.activity_report_output_num)
public class ReportOutputNum extends BaseActivity {

    Context context=ReportOutputNum.this;

    @ViewInject(R.id.txtNo)
    TextView txtNo;

    @ViewInject(R.id.txtNumT)
    TextView txtNumT;

    @ViewInject(R.id.txtNumTC)
    TextView txtNumTP;

    @ViewInject(R.id.txtT)
    TextView txtT;

    @ViewInject(R.id.txtBatch)
    TextView txtBatch;
    @ViewInject(R.id.txtNumber)
    TextView txtNumber;
//    @ViewInject(R.id.txtLast)
//    TextView txtLast;
    @ViewInject(R.id.editTxtNumber)
    TextView editTxtNumber;

    @ViewInject(R.id.editText)
    EditText edtBarcode;

    @ViewInject(R.id.editKu)
    EditText editKu;

    @ViewInject(R.id.textView116)
    TextView textView116;

    @ViewInject(R.id.txt_WareHousName)
    TextView txtWareHousName;
    @ViewInject(R.id.butP)
    Button butP;
    @ViewInject(R.id.butB)
    Button butB;
    @ViewInject(R.id.butO)
    Button butO;

    @ViewInject(R.id.textView105)
    TextView textView105;


    WoModel womodel;

    @Override
    protected void initViews() {
        try{
            super.initViews();
            BaseApplication.context = context;
            BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.ReportOverInstock), true);
            x.view().inject(this);
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }



    }

    @Override
    protected void initData() {
        try{
            palletDetailModels=new ArrayList<>();
            palletDetailModels.add(new PalletDetail_Model());
            palletDetailModels.get(0).setLstBarCode(new ArrayList<BarCodeInfo>());

            super.initData();
            CommonUtil.setEditFocus(edtBarcode);
            womodel=getIntent().getParcelableExtra("WoModel");
            butB.setVisibility(womodel.getStrVoucherType().toString().equals("成品")? View.VISIBLE:View.GONE);
            butP.setVisibility(womodel.getStrVoucherType().toString().equals("成品")? View.VISIBLE:View.GONE);

            //托盘
            textView105.setVisibility(womodel.getStrVoucherType().toString().equals("成品")? View.VISIBLE:View.GONE);

            //货位
            textView116.setVisibility(womodel.getStrVoucherType().toString().equals("成品")? View.GONE:View.VISIBLE);
            editKu.setVisibility(womodel.getStrVoucherType().toString().equals("成品")? View.GONE:View.VISIBLE);

//        textView116.setVisibility(View.GONE);
//        editKu.setVisibility(View.GONE);


            txtWareHousName.setText(BaseApplication.userInfo.getWarehouseName());
            GetWoModel(womodel);
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }


    }

    @Event(value = R.id.editText, type = View.OnKeyListener.class)
    private boolean edtfilterOnKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
                try{
                    keyBoardCancle();
                    String barcode = edtBarcode.getText().toString().trim();
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("Barcode", barcode);
                    params.put("PalletModel", "1");
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByNoADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_SerialNoByPalletADF, null,  URLModel.GetURL().GetT_PalletDetailByNoADF, params, null);
                    return false;
                }catch (Exception ex){
                    MessageBox.Show(context,ex.toString());
                }
        }
        return false;
    }


    List<PalletDetail_Model> palletDetailModels;
    /*
    解析物料条码扫描
     */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        try {
            LogUtil.WriteLog(CombinPallet.class, TAG_GetT_PalletDetailByNoADF,result);
            ReturnMsgModelList<PalletDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<PalletDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                PalletDetail_Model palletDetailModel = returnMsgModel.getModelJson().get(0);
                BarCodeInfo barCodeM = palletDetailModel.getLstBarCode().get(0);
                String Fileter=barCodeM.getErpVoucherNo();
                if (!Fileter.equals(txtNo.getText().toString()))
                {
                    MessageBox.Show(context, "扫描条码信息和工单号不一致！");
                    CommonUtil.setEditFocus(edtBarcode);
                    return;
                }
                //判断组托条件：批次、据点、库位、物料相同才能组托
                if (palletDetailModels.get(0).getLstBarCode() != null) {// &&
                    for (BarCodeInfo barCodeInfo : palletDetailModel.getLstBarCode()) {
                        if (palletDetailModels.get(0).getLstBarCode().size() != 0) {
                            if(palletDetailModels.get(0).getLstBarCode().contains(barCodeInfo)){
                                MessageBox.Show(context, getString(R.string.Error_Contain_Barcode));
                                CommonUtil.setEditFocus(edtBarcode);
                                return;
                            }
                            String checkError = CheckPalletCondition(barCodeInfo);
                            if (!TextUtils.isEmpty(checkError)) {
                                MessageBox.Show(context, checkError);
                                CommonUtil.setEditFocus(edtBarcode);
                                return;
                            }
                            barCodeInfo.setPalletno(palletDetailModels.get(0).getLstBarCode().get(0).getPalletno());
                        }
                        if (!palletDetailModels.get(0).getLstBarCode().contains(barCodeInfo)) {
                            // palletDetailModels.get(0).setPalletNo(barCodeInfo.getPalletno());
                            palletDetailModels.get(0).setPalletType(barCodeInfo.getPalletType());

                            palletDetailModels.get(0).getLstBarCode().add(0, barCodeInfo);
                            // palletDetailModels.get(0).setVoucherType(999);
                            palletDetailModels.get(0).setStrongHoldCode(barCodeInfo.getStrongHoldCode());
                            palletDetailModels.get(0).setStrongHoldName(barCodeInfo.getStrongHoldName());
                            palletDetailModels.get(0).setCompanyCode(barCodeInfo.getCompanyCode());
                            palletDetailModels.get(0).setMaterialNo(barCodeInfo.getMaterialNo());
                            palletDetailModels.get(0).setBatchNo(barCodeInfo.getBatchNo());
                            palletDetailModels.get(0).setSupPrdBatch(barCodeInfo.getSupPrdBatch());
                            palletDetailModels.get(0).setSuppliernNo(barCodeInfo.getSupCode());
                            palletDetailModels.get(0).setSuppliernName(barCodeInfo.getSupName());
                            palletDetailModels.get(0).setErpVoucherNo(barCodeInfo.getErpVoucherNo());
                            palletDetailModels.get(0).setAreaID(barCodeInfo.getAreaID());
                        }
                    }
                }
                BarCodeInfo barCodeInfo = palletDetailModel.getLstBarCode().get(0);
                txtBatch.setText(barCodeInfo.getBatchNo());
                womodel.setBatchNo(barCodeInfo.getBatchNo());

                float sumAll=0;
                for (BarCodeInfo barCodemodel : palletDetailModels.get(0).getLstBarCode()) {
                    sumAll= ArithUtil.add(sumAll,barCodemodel.getQty());
                }
                editTxtNumber.setText(String.valueOf(sumAll));
                txtNumber.setText(String.valueOf(palletDetailModels.get(0).getLstBarCode().size()));

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception e){
            MessageBox.Show(context,e.toString());
        }finally {
            CommonUtil.setEditFocus(edtBarcode);
        }
        CommonUtil.setEditFocus(edtBarcode);
    }

    String CheckPalletCondition(BarCodeInfo  barCodeInfo) {
        if (palletDetailModels.get(0).getPalletType() == 0) {
            if (!palletDetailModels.get(0).getErpVoucherNo().equals(barCodeInfo.getErpVoucherNo()))
                return getString(R.string.Error_VourcherNonotMatch);
            if(!palletDetailModels.get(0).getSuppliernNo().equals(barCodeInfo.getSupCode())){
                return getString(R.string.Error_SuppilerNoMatch);
            }
        } else if (palletDetailModels.get(0).getAreaID() != (barCodeInfo.getAreaID()))
            return getString(R.string.Error_AreaotnotMatch);
        //收货组托判断组托条件：批次、据点、物料、订单相同才能组托
        //在库组托判断库位相同才能组托
        //getPalletType为0：收货组托
        //新增：判断物料是否已组托 插入：判断物料所在托盘属性是否与现有托盘属性一致才能组托
        if (barCodeInfo.getPalletType() != 0)
            return getString(R.string.Error_Contain_Barcode);
//        if (SWPallet.isChecked() && palletDetailModels.get(0).getPalletType() != barCodeInfo.getPalletType())
//            return getString(R.string.Error_PalletypenotMatch);//.getLstBarCode().get(0)
        if (!palletDetailModels.get(0).getMaterialNo().equals(barCodeInfo.getMaterialNo()))
            return getString(R.string.Error_materialnotMatch);
        else if (barCodeInfo.getBatchNo()==null || !palletDetailModels.get(0).getBatchNo().equals(barCodeInfo.getBatchNo()))
            return getString(R.string.Error_BartchnotMatch);
        else if (barCodeInfo.getSupPrdBatch()==null || !palletDetailModels.get(0).getSupPrdBatch().equals(barCodeInfo.getSupPrdBatch()))
            return getString(R.string.Error_ProductBartchnotMatch);
        else if (!palletDetailModels.get(0).getStrongHoldCode().equals(barCodeInfo.getStrongHoldCode()))
            return getString(R.string.Error_CompanynotMatch);
        return "";
    }


    //检查库位是否正确
    protected boolean Check() {
        if(womodel.getStrVoucherType().toString().equals("成品")&&!txtWareHousName.getText().toString().contains("成品")){
            return false;
        }else{
            return true;
        }

//        boolean checkflag=false;
//        if (womodel.getStrVoucherType().toString().equals("成品")&&BaseApplication.userInfo.getPickWareHouseNo().equals("AD03")){
//            checkflag=true;
//        }
//        if((!womodel.getStrVoucherType().toString().equals("成品"))&&(!BaseApplication.userInfo.getPickWareHouseNo().equals("AD03"))){
//                checkflag=true;
//        }
//        return checkflag;
    }

    @Event(value = {R.id.butB,R.id.butO,R.id.butP},type = View.OnClickListener.class)
    private void onClick(View view) {
        try{
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return;
            }
            if (R.id.butP == view.getId()) {
                if (!TaskNo.isEmpty()){
                    List<PalletDetail_Model> model=new ArrayList<>();
                    PalletDetail_Model modeldetail = new PalletDetail_Model();
                    modeldetail.setTaskNo(TaskNo);
                    modeldetail.setPrintIPAdress(URLModel.PrintIP);
                    model.add(modeldetail);
                    String modelJson = GsonUtil.parseModelToJson(model);
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("PalletJson", modelJson);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PrintT, getString(R.string.Msg_Print), context, mHandler, RESULT_PrintT, null, URLModel.GetURL().SaveT_YMHCPPrintADF, params, null);

                }
                return;
            }
            if (editTxtNumber.getText().toString().isEmpty()||txtBatch.getText().toString().isEmpty()){
                MessageBox.Show(context, "填写信息不能为空！");
                return;
            }
            ArrayList<WoModel> models =new ArrayList<>();
            womodel.setBatchNo(txtBatch.getText().toString());
            String Path = "";
            if (R.id.butB == view.getId()) {
                if (palletDetailModels != null && palletDetailModels.size() != 0 && palletDetailModels.get(0).getLstBarCode()!=null
                        && palletDetailModels.get(0).getLstBarCode().size()!=0) {
                    palletDetailModels.get(0).setVoucherType(999);
                    palletDetailModels.get(0).setPrintIPAdress(URLModel.PrintIP);
                    String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                    String modelJson = GsonUtil.parseModelToJson(palletDetailModels);
                    final Map<String, String> params = new HashMap<String, String>();

//                LogUtil.WriteLog(CombinPallet.class, TAG_SaveT_PalletDetailADF, modelJson);
                    params.put("UserJson", userJson);
                    params.put("json", modelJson);
//                params.put("printtype", "1");
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_ProductPalletDetailADF, getString(R.string.Msg_SaveT_PalletDetailADF), context, mHandler, RESULT_GetT_ProductPalletDetailByNoADF, null, URLModel.GetURL().SaveT_YMHCPPalletDetailADF, params, null);
                }
                return;
            }
            if (R.id.butO == view.getId()) {
                if (womodel.getStrVoucherType().toString().equals("成品") && palletDetailModels != null && palletDetailModels.size() != 0 && palletDetailModels.get(0).getLstBarCode()!=null
                        && palletDetailModels.get(0).getLstBarCode().size()!=0){
                    MessageBox.Show(context, "扫描的外箱还没有组托");
                    return;
                }else{
                    if (Check()){
//                    if (womodel.getMaxProductQty()!=null)
//                    {
//                        if (Float.parseFloat(editTxtNumber.getText().toString())>womodel.getMaxProductQty())
//                        {
//                            MessageBox.Show(context, "成品包装完工入库数量不能超过最大限制数量："+ womodel.getMaxProductQty().toString());
//                            return;
//                        }
//                    }
//                        womodel.setDataType(womodel.getStrVoucherType().toString().equals("散装物料")?"Y":"N");
                        womodel.setDataType("N");
                        womodel.setInQty(Float.parseFloat(editTxtNumber.getText().toString()));
                        womodel.setUserNo(BaseApplication.userInfo.getUserNo());
                        womodel.setWareHouseNo(BaseApplication.userInfo.getReceiveWareHouseNo());
                        womodel.setAreaNo(BaseApplication.userInfo.getReceiveAreaNo());
                        womodel.setVoucherType(37);
                        Path = URLModel.GetURL().GetFinishInStockByListWoinfo;
                    }else{
                        MessageBox.Show(context, "成品入库的货位错误！");
                        return;
                    }
                    models.add(womodel);
                    try {
                        //设置入库的货位ID
                        UerInfo user = BaseApplication.userInfo;
                        if(!womodel.getStrVoucherType().toString().equals("成品")){
                            if(!WgFlag)
                            {
                                MessageBox.Show(context, "没有扫描正确库位！");
                                return;
                            }
                            user.setReceiveAreaID(LReceiveAreaID);
                            user.setWarehouseID(LWarehouseID);
                            user.setReceiveHouseID(LReceiveHouseID);
                        }
                        if(womodel.getStrVoucherType().toString().equals("成品")){
                            user.setWarehouseID(3);
                            user.setReceiveHouseID(12);
                            user.setReceiveAreaID(4437);
//                            user.setWarehouseID(3);
//                            user.setReceiveHouseID(1025);
//                            user.setReceiveAreaID(30025);
                    }
//                        if(womodel.getStrVoucherType().toString().equals("半制品")){
//                            user.setWarehouseID(14);
//                            user.setReceiveHouseID(1080);
//                            user.setReceiveAreaID(30270);
//                        }


                        Map<String, String> params = new HashMap<>();
                        if(womodel.getStrVoucherType().toString().equals("成品")){
                            if(barcodemodel==null||barcodemodel.size()==0){
                                MessageBox.Show(context, "扫描条码还没有组托，不能完工入库");
                                return;
                            }
                            else{
                                params.put("BarcodeJson",GsonUtil.parseModelToJson(barcodemodel));
                            }
                        }else{

                            if(palletDetailModels.get(0).getLstBarCode()==null||palletDetailModels.get(0).getLstBarCode().size()==0){
                                MessageBox.Show(context, "没有扫描条码，不能完工入库");
                                return;
                            }
                            else{
                                params.put("BarcodeJson",GsonUtil.parseModelToJson(palletDetailModels.get(0).getLstBarCode()));
                            }
                        }

                        params.put("UserJson", GsonUtil.parseModelToJson(user));
                        params.put("WoInfoJson", GsonUtil.parseModelToJson(models));

//            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetT_OutTaskListADF, ModelJson);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_ReportOutPutNum, getString(R.string.Msg_Post), context, mHandler,
                                RESULT_Get_ReportOutPutNum, null,  Path, params, null);
                    } catch (Exception ex) {
//                mSwipeLayout.setRefreshing(false);
                        MessageBox.Show(context, ex.getMessage());
                    }finally {
                        models.clear();
                        WgFlag=false;
                    }
                }
            }

        }catch (Exception ex){

            MessageBox.Show(context, ex.getMessage());
        }

    }

    @Event(value =R.id.editKu,type = View.OnKeyListener.class)
    private  boolean editKuClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code = editKu.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                CommonUtil.setEditFocus(editKu);
                return true;
            }
            final Map<String, String> params = new HashMap<String, String>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("AreaNo", code);
            LogUtil.WriteLog(ReportOutputNum.class, TAG_GetAreaModelADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetAreaModelADF, null,  URLModel.GetURL().GetAreaModelADF, params, null);
        }
        return false;
    }

    String TAG_Get_ReportOutPutNum = "ReportOutPutNum_Get_ReportOutPutNum";
    private final int RESULT_Get_ReportOutPutNum = 101;

    String TAG_Get_Over = "ReportOutPutNum_Get_Over";
    private final int RESULT_Get_Over = 102;

    String TAG_Get_Barcode = "ReportOutPutNum_Get_Barcode";
    private final int RESULT_Get_Barcode = 103;

    String TAG_Get_Mes = "ReportOutPutNum_Get_Mes";
    private final int RESULT_Get_Mes = 104;

    String TAG_Get_GetBaoGong = "Report_Get_GetBaoGong";
    private final int RESULT_Get_GetBaoGong = 105;

    String TAG_GetT_PalletDetailByNoADF="CombinPallet_GetT_PalletDetailByNoADF";
    private final int RESULT_GetT_SerialNoByPalletADF = 106;

    String TAG_SaveT_ProductPalletDetailADF="CombinPallet_TAG_SaveT_ProductPalletDetailADF";//成品组托
    private final int RESULT_GetT_ProductPalletDetailByNoADF = 107;//成品组托

    String TAG_PrintT="ReportOutput_TAG_PrintT";//组托打印
    private final int RESULT_PrintT = 108;//组托打印

    String TAG_PostBaoJianByListWoinfoADF="LineStockOutProduct_GetT_SaveInStockModelADF";//报检
    private final int RESULT_Msg_PostBaoJianByListWoinfoADF=110;

    String TAG_GetAreaModelADF="LineStockOutProduct_GetT_SaveInStockModelADF";
    private final int RESULT_Msg_GetAreaModelADF=109;

    @Override
    public void onHandleMessage(Message msg) {
//        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_Msg_GetAreaModelADF:
                AnalysisetT_GetAreaModelADFJson((String) msg.obj);
                break;
            case RESULT_GetT_SerialNoByPalletADF:
                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
            case RESULT_Get_ReportOutPutNum:
                Analysis((String)msg.obj,TAG_Get_ReportOutPutNum);
                break;
            case RESULT_Get_Over:
                Analysis((String)msg.obj,TAG_Get_Over);
                break;
            case RESULT_Get_Mes:
                GetMesAnalysis((String)msg.obj,TAG_Get_Mes);
                break;
            case RESULT_Get_Barcode:
                GetBarcodeAnalysis((String)msg.obj,TAG_Get_Barcode);
                break;
            case RESULT_PrintT:
                PrintTAnalysis((String)msg.obj,TAG_PrintT);
                break;
            case RESULT_Get_GetBaoGong:
                GetBaoGongAnalysis((String)msg.obj,TAG_Get_GetBaoGong);
                break;
            case RESULT_GetT_ProductPalletDetailByNoADF:
                AnalysisSaveT_ProductPalletDetailADF((String) msg.obj);
                break;
            case RESULT_Msg_PostBaoJianByListWoinfoADF:
                AnalysisetT_GetPostBaoJianByListWoinfoJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edt_filterContent);
                break;
        }
    }

    private int LReceiveAreaID=0;
    private int LReceiveHouseID=0;
    private int LWarehouseID=0;
    private boolean WgFlag=false;
    void AnalysisetT_GetAreaModelADFJson(String result){
        try {
            LogUtil.WriteLog(LineStockInProduct.class, TAG_GetAreaModelADF,result);
            ReturnMsgModel<AreaInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo_Model>>() {}.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                LReceiveAreaID=returnMsgModel.getModelJson().getID();
                LReceiveHouseID=returnMsgModel.getModelJson().getHouseID();
                LWarehouseID=returnMsgModel.getModelJson().getWarehouseID();
                if(String.valueOf(BaseApplication.userInfo.getWarehouseID()).equals(String.valueOf(LWarehouseID))){
                    WgFlag=true;
                }else{
                    MessageBox.Show(context,"扫描的货位不在登陆人的仓库下！");
                    editKu.setText("");
                    CommonUtil.setEditFocus(editKu);
                }
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtBarcode);
    }

    /*打印托盘标签*/
    void PrintTAnalysis(String result,String Tag){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S"))
            {
                TaskNo="";
                txtT.setText("");
            }
            MessageBox.Show(context, returnMsgModel.getMessage());

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }

private String TaskNo="";
    /*
保存成品组托信息
 */
    void AnalysisSaveT_ProductPalletDetailADF(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S"))
            {
                TaskNo=returnMsgModel.getTaskNo();
                MessageBox.Show(context, "组托成功，托盘号："+TaskNo);
                txtT.setText(String.valueOf(TaskNo));
                InitFrm();
            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }


    void AnalysisetT_GetPostBaoJianByListWoinfoJson(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S"))
            {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }


    void GetBaoGongAnalysis(String result,String Tag){
        try {
            LogUtil.WriteLog(ReportOutputNum.class, Tag, result);
            ReturnMsgModelList<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<String>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                String[] MessageSplit =returnMsgModel.Message.split(",");
                txtNumTP.setText(MessageSplit[0].equals("")?"0":MessageSplit[0]);
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }


    void Analysis(String result,String Tag){
        try {
            LogUtil.WriteLog(ReportOutputNum.class, Tag, result);
            ReturnMsgModelList<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "提交成功！");
                //入库累加数量
//                String[] txtNumTSplit = .getText().toString().split("/");
                txtNumTP.setText(String.valueOf (Float.parseFloat(txtNumTP.getText().toString()) + Float.parseFloat(editTxtNumber.getText().toString())));

                InitFrm();
                txtBatch.setText("");
                txtNumber.setText("");
                editTxtNumber.setText("");

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void GetMesAnalysis(String result,String Tag){
        try {
            LogUtil.WriteLog(ReportOutputNum.class, Tag, result);
            ReturnMsgModelList<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "提交成功！");

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void GetBarcodeAnalysis(String result,String Tag){
        try {
            LogUtil.WriteLog(ReportOutputNum.class, Tag, result);
            ReturnMsgModelList<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "提交成功！");

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }


    private void GetBaoGong(){
        try {
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("GongDan", txtNo.getText().toString());
//            LogUtil.WriteLog(OffShelfBillChoice.class, TAG_GetT_OutTaskListADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_GetBaoGong, getString(R.string.Msg_Post), context, mHandler,
                    RESULT_Get_GetBaoGong, null,   URLModel.GetURL().GetBaoGongSumQtyLastQty, params, null);
        } catch (Exception ex) {
//                mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
        }
    }


    /*
获取工单信息
 */
    void GetWoModel(WoModel womodel){
        if(womodel!=null) {
            txtNo.setText(womodel.getErpVoucherNo());
            txtBatch.setText(womodel.getBatchNo());
            txtNumT.setText( "/" + (womodel.getProductQty()==null?"0":womodel.getProductQty()));
            GetBaoGong();
        }
    }


    private List<BarCodeInfo> barcodemodel= new ArrayList<BarCodeInfo>();
    void InitFrm(){
        //完工入库使用
        barcodemodel= new ArrayList<BarCodeInfo>();
        barcodemodel=palletDetailModels.get(0).getLstBarCode();

        palletDetailModels=new ArrayList<>();
        palletDetailModels.add(new PalletDetail_Model());
        palletDetailModels.get(0).setLstBarCode(new ArrayList<BarCodeInfo>());
        edtBarcode.setText("");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {

            Intent intent=new Intent(context, ReportBaojian.class);
            Bundle bundle=new Bundle();
            bundle.putParcelable("WoModel",womodel);
            bundle.putString("NumT",txtNumTP.getText().toString() + txtNumT.getText().toString());
            intent.putExtras(bundle);
            startActivityLeft(intent);
            closeActiviry();
        }
        return super.onOptionsItemSelected(item);
    }


}
