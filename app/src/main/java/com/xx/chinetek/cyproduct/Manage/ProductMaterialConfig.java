package com.xx.chinetek.cyproduct.Manage;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.Manage.WoDetailMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Production.Manage.LineManageModel;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_product_material_config)
public class ProductMaterialConfig extends BaseActivity {

    String TAG_GetWoDetailModelByWoNo="ProductMaterialConfig_GetWoDetailModelByWoNo";
    String TAG_GetStockModelADF="ProductMaterialConfig_GetMaterialByBarcode";
//    String TAG_StartWork="ProductMaterialConfig_StartWork";
    private final int RESULT_GetWoDetailModelByWoNo=101;
    private final int RESULT_Msg_GetStockModelADF=102;
//    private final int RESULT_StartWork=103;

    String TAG_SaveBarcodeListForQiTao="ProductMaterialConfig_SaveBarcodeListForQiTao";
    private final int RESULT_SaveBarcodeListForQiTao=103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_SaveBarcodeListForQiTao:
                AnalysisSaveBarcodeListForQiTaoJson((String) msg.obj);
                break;
            case RESULT_GetWoDetailModelByWoNo:
                AnalysisGetWoDetailModelByWoNoJson((String) msg.obj);
                break;
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockModelADFJson((String) msg.obj);
                break;
//            case RESULT_StartWork:
//                AnalysisStartWorkADFJson((String) msg.obj);
//                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


    Context context=ProductMaterialConfig.this;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_BatchNo)
    TextView txtBatchNo;
    @ViewInject(R.id.txt_ProductLineNo)
    TextView txtProductLineNo;
    @ViewInject(R.id.txt_ProductStartTime)
    TextView txtProductStartTime;
    @ViewInject(R.id.txt_MaterialDesc)
    TextView txtMaterialDesc;
//    @ViewInject(R.id.edt_PrePruductNum)
//    EditText edtPrePruductNum;
    @ViewInject(R.id.edt_Barcode)
    EditText edtBarcode;
    @ViewInject(R.id.lsv_Material)
    ListView lsvMaterial;

    @ViewInject(R.id.textView250)
    TextView textView250;

    @ViewInject(R.id.textView210)
    TextView textView210;

    @ViewInject(R.id.edt_PrePruductNum)
    TextView edtPrePruductNum;



    LineManageModel lineManageModel;
    ArrayList<WoDetailModel> woDetailModels;
    WoDetailMaterialItemAdapter woDetailMaterialItemAdapter;
    BarCodeInfo currentBarCodeInfo;//当前扫描物料
    int currentIndex=-1;//当前扫描物料对应工单物料
    int mHour, mMinute;

    @Override
    protected void initViews() {
        try{
            super.initViews();
            BaseApplication.context = context;
            BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_MaterialConfig_subtitle), true);
            x.view().inject(this);
        }catch(Exception ex){
            MessageBox.Show(context,ex.toString());
        }

    }

    @Override
    protected void initData() {
        try{
            super.initData();


            this.lineManageModel=getIntent().getParcelableExtra("lineManageModel");
            if(lineManageModel!=null && lineManageModel.getWoModel()!=null){

                txtBatchNo.setText(lineManageModel.getWoBatchNo());
                txtMaterialDesc.setText(lineManageModel.getWoModel().getMaterialDesc());
                txtProductLineNo.setText(lineManageModel.getProductLineNo());
                txtVoucherNo.setText(lineManageModel.getWoErpVoucherNo());

//            txtVoucherNo.setText(lineManageModel.getWoModel().getErpVoucherNo());
//            txtBatchNo.setText(lineManageModel.getWoModel().getBatchNo());
//            txtProductLineNo.setText(lineManageModel.getProductLineNo());
//            txtMaterialDesc.setText(lineManageModel.getWoModel().getMaterialDesc());
                GetWoDetailModelByWoNo(lineManageModel.getWoModel());
                textView250.setVisibility(View.GONE);
                txtProductStartTime.setVisibility(View.GONE);
                textView210.setVisibility(View.GONE);
                edtPrePruductNum.setVisibility(View.GONE);

            }
        }catch(Exception ex){
            MessageBox.Show(context,ex.toString());
        }


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_product_materialconfig, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_filter) {
//            if (DoubleClickCheck.isFastDoubleClick(context)) {
//                return false;
//            }
//            //提交
//            //判断是否满足齐套扫描要求
//            //提交数据
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Event(value = R.id.edt_PrePruductNum,type = View.OnKeyListener.class)
//    private  boolean edtPrePruductNumClick(View view, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            keyBoardCancle();
//            String preProductNum=edtPrePruductNum.getText().toString().trim();
//            if(CommonUtil.isFloat(preProductNum)){
//                lineManageModel.setPreProductNum(Float.parseFloat(preProductNum));
//                edtPrePruductNum.setEnabled(false);
//                CommonUtil.setEditFocus(edtBarcode);
//            }else{
//                MessageBox.Show(context,getString(R.string.Error_isnotnum));
//                CommonUtil.setEditFocus(edtPrePruductNum);
//                return true;
//            }
//        }
//        return false;
//    }

    @Event(value = R.id.edt_Barcode,type = View.OnKeyListener.class)
    private  boolean edtBarcodeClick(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode=edtBarcode.getText().toString().trim();
            if(!TextUtils.isEmpty(barcode)){
                try {
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("SerialNo",barcode);
//                    params.put("BarCode", barcode);
//                    params.put("ScanType", "2");
//                    params.put("MoveType", "1"); //1：下架 2:移库
//                    params.put("IsEdate", "2"); //1：不判断有效期 2:判断有效期
//                    LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetStockModelADF, barcode);
//                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetT_SerialNoADF, params, null);
                } catch (Exception ex) {
                    MessageBox.Show(context, ex.getMessage());
                }
            }
            return true;
        }

//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            keyBoardCancle();
//            edtBarcode.setText("");
//            edtPrePruductNum.setEnabled(true);
//            CommonUtil.setEditFocus(edtPrePruductNum);
//            return true;
//
//        }
        return false;
    }



    @Event(value = R.id.txt_ProductStartTime,type = View.OnClickListener.class )
    private void txtProductStartTimeClick(View view){
        final Calendar ca = Calendar.getInstance();
        mHour = ca.get(Calendar.HOUR_OF_DAY);
        mMinute = ca.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour=hourOfDay;
                mMinute=minute;
                txtProductStartTime.setText(display());
                lineManageModel.setStartTime(display());
            }
        },mHour,mMinute,true).show();
    }


    void GetWoDetailModelByWoNo(WoModel woModel){
        try {
           // String ModelJson = GsonUtil.parseModelToJson(woModel);
            Map<String, String> params = new HashMap<>();
           // params.put("UserJson", GsonUtil.parseModelToJson(userInfo));
            params.put("HeadId", woModel.getID()+"");
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetWoDetailModelByWoNo, woModel.getID()+"");
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetWoDetailModelByWoNo, getString(R.string.Mag_GetWoDetailModelByWoNo), context, mHandler, RESULT_GetWoDetailModelByWoNo, null,  URLModel.GetURL().GetWoDetailModelByWoNo, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void  AnalysisGetWoDetailModelByWoNoJson(String result){
        try {
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetWoDetailModelByWoNo, result);
            ReturnMsgModelList<WoDetailModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<WoDetailModel>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                woDetailModels = returnMsgModel.getModelJson();
                if (woDetailModels != null ){
                    BindListview(woDetailModels);
                }

            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){

            MessageBox.Show(context,ex.getMessage());
        }
    }

    void AnalysisStartWorkADFJson(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context,"开始成功！");
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }
    void AnalysisSaveBarcodeListForQiTaoJson(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context,"提交数据成功！");
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }


    public  ArrayList<BarCodeInfo> AllBarcode= new ArrayList<>();
    void AnalysisGetStockModelADFJson(String result){
        try {
            LogUtil.WriteLog(ProductMaterialConfig.class, TAG_GetStockModelADF, result);
            try {
//                ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
//                }.getType());
//                if (returnMsgModel.getHeaderStatus().equals("S")) {
//                   ArrayList<StockInfo_Model> stockInfoModels = returnMsgModel.getModelJson();
                ReturnMsgModel<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<BarCodeInfo>>() {}.getType());
                if (returnMsgModel.getHeaderStatus().equals("S")) {
                    BarCodeInfo barCode = returnMsgModel.getModelJson();
                    StockInfo_Model model = new StockInfo_Model();
                    model.setQty(barCode.getQty());
                    model.setBatchNo(barCode.getBatchNo());
                    model.setMaterialNo(barCode.getMaterialNo());
                    model.setBarcode(barCode.getBarCode());
                    model.setMaterialDesc(barCode.getMaterialDesc());
                    model.setSerialNo(barCode.getSerialNo());
                    ArrayList<StockInfo_Model> stockInfoModels=new ArrayList<>();
                    stockInfoModels.add(model);

                    if (stockInfoModels != null && stockInfoModels.size() != 0) {
                        //判断条码是否已经扫描
                        StockInfo_Model stockInfoModel = stockInfoModels.get(0);
                        WoDetailModel woDetailModel = new WoDetailModel(stockInfoModel.getMaterialNo());
                        int woDetailindex = woDetailModels.indexOf(woDetailModel);
                        if (woDetailindex == -1) {
                            MessageBox.Show(context, getString(R.string.Error_ErpvoucherNoMatch));
                            CommonUtil.setEditFocus(edtBarcode);
                            return;
                        }
                        if (woDetailModels.get(woDetailindex).getStockInfoModels() == null)
                            woDetailModels.get(woDetailindex).setStockInfoModels(new ArrayList<StockInfo_Model>());
                        if ( woDetailModels.get(woDetailindex).getStockInfoModels().indexOf(stockInfoModel)!= -1) {
                            MessageBox.Show(context, getString(R.string.Error_Barcode_hasScan));
                            CommonUtil.setEditFocus(edtBarcode);
                            return;
                        }
                        Float scanQty=woDetailModels.get(woDetailindex).getScanQty();
                        Float StockQty=stockInfoModel.getQty();
                        Float WoQty=woDetailModels.get(woDetailindex).getRemainQty();
                        Float Qty=ArithUtil.add(scanQty,StockQty);
//                        if(WoQty<Qty){
//                            MessageBox.Show(context, getString(R.string.Error_PackageQtyBigerThenWo));
//                            CommonUtil.setEditFocus(edtBarcode);
//                            return;
//                        }
                        AllBarcode.add(barCode);
                        woDetailModels.get(woDetailindex).setScanQty(Qty);
                        woDetailModels.get(woDetailindex).getStockInfoModels().add(0,stockInfoModel);
                        BindListview(woDetailModels);
                    }
                } else {
                    MessageBox.Show(context, returnMsgModel.getMessage());
                    CommonUtil.setEditFocus(edtBarcode);
                }
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
                CommonUtil.setEditFocus(edtBarcode);
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtBarcode);
        }
    }

    void BindListview(ArrayList<WoDetailModel> woDetailModels){
        woDetailMaterialItemAdapter=new WoDetailMaterialItemAdapter(context,woDetailModels);
        lsvMaterial.setAdapter(woDetailMaterialItemAdapter);
    }


    public String  display() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day=format.format(new Date());
        return new StringBuffer().append(day).append(" ").append(mHour<10?"0"+mHour:mHour).append(":").append(mMinute<10?"0"+mMinute:mMinute).toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            try{
                //判断是否已经扫描齐套
                boolean flag=false;
                for (int i=0;i<woDetailModels.size();i++){
                    if(woDetailModels.get(i).getMaterialNo().substring(0,1).equals("4")&&(Float.parseFloat(woDetailModels.get(i).getScanQty().toString())==0)){
                        flag=true;
                    }
                }
                if(flag){
                    //不齐套
                    new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("扫描物料不齐套，是否继续提交？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO 自动生成的方法
                                    PostData();
                                }
                            }).setNegativeButton("取消", null).show();
                }else{
                    PostData();
                }


            }catch(Exception ex){
                MessageBox.Show(context,ex.getMessage());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //数据提交
    void PostData(){
        try {
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", GsonUtil.parseModelToJson(AllBarcode));
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveBarcodeListForQiTao, "数据正在提交", context, mHandler, RESULT_SaveBarcodeListForQiTao, null,  URLModel.GetURL().SaveBarcodeListForQiTao, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }


}
