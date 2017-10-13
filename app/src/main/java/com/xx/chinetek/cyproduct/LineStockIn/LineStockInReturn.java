package com.xx.chinetek.cyproduct.LineStockIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.Pallet.CombinPallet;
import com.xx.chinetek.Service.SocketService;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.SocketBaseActivity;
import com.xx.chinetek.cyproduct.Billinstock.BillsIn;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Pallet.PalletDetail_Model;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
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
import java.util.Map;

@ContentView(R.layout.activity_line_stock_in_return)
public class LineStockInReturn extends SocketBaseActivity {

    Context context=LineStockInReturn.this;
    @ViewInject(R.id.txtWeight)
    TextView txtWeight;
    @ViewInject(R.id.editBarcode)
    EditText editBarcode;
    @ViewInject(R.id.editNum)
    EditText editNum;
    @ViewInject(R.id.txtNo)
    TextView txtNo;
    @ViewInject(R.id.txtName)
    TextView txtName;
    @ViewInject(R.id.txtBatch)
    TextView txtBatch;
    @ViewInject(R.id.txtMaName)
    TextView txtMaName;

    WoModel womodel;
    ArrayList<WoDetailModel> WoDetailModels;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        CommonUtil.setEditFocus(editBarcode);
        BaseApplication.isCloseActivity=false;
        initVariables();//设置接收服务

    }

    @Override
    protected void initData() {
        super.initData();
        womodel=getIntent().getParcelableExtra("woModel");
        txtNo.setText(womodel.getErpVoucherNo().toString());
        txtName.setText(womodel.getMaterialDesc().toString());
        editNum.setText("0");
        GetWODetailInfo(String.valueOf(womodel.getID()));

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

    protected void initVariables()
    {
        try{
            //给全局消息接收器赋值，并进行消息处理
            mReciver = new MessageBackReciver(){
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    String action = intent.getAction();
                    if(action.equals(SocketService.MESSAGE_ACTION))
                    {
                        String message = intent.getStringExtra("message");
                        Log.v("WMSLOG_Socket", message);
                        String message1=message.split("\r\n")[0];
                        String[] meg =message1.split(",");
                        if (meg.length>=3 &&isOpenWeight)
//                    {EditW.setText(message1.contains("ST,GS")?meg[2].trim():"");}
                        {
                            txtWeight.setText((message1.contains(",NT")||message1.contains("ST,GS"))?meg[2].trim():"");
                        }
                    }
                }
            };
        }catch(Exception ex){
            MessageBox.Show(context,"电子称异常！");
        }

    }

    private boolean isOpenWeight=true;
    @Event(value = R.id.txtWeight,type = View.OnFocusChangeListener.class)
    private void OnFocusChange(View view, boolean isFouse) {
        if (isFouse)
        {
            txtWeight.setText("");
            isOpenWeight=false;
        }
        else
        {
            if(txtWeight.getText().toString().equals(""))
            {
                isOpenWeight=true;
            }
        }
    }

    String TAG_Get_barcode = "LineStockInReturn_Getbarcode";
    private final int RESULT_Get_barcode = 101;

    String TAG_print_Backlabel = "LineStockInReturn_print_Backlabel";
    private final int RESULT_print_Backlabel = 102;

    String TAG_Print_Outlabel = "LineStockInReturn_Print_Outlabel";
    private final int RESULT_Print_Outlabel = 103;

    String TAG_Get_WODetailInfo = "LineStockInReturn_Print_Outlabel";
    private final int RESULT_Get_WODetailInfo = 104;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Get_barcode:
                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
//            case RESULT_print_Backlabel:
//                Analysis_PrintBackLable((String)msg.obj);
//                break;
            case RESULT_Print_Outlabel:
                AnalysisGetT_RESULT_Print_OutlabelADFJson((String)msg.obj);
                break;
            case RESULT_Get_WODetailInfo:
                AnalysisGetT_RESULT_Get_WODetailInfoADFJson((String)msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
//                CommonUtil.setEditFocus(edt_filterContent);
                break;
        }
    }

    @Event(value = R.id.editBarcode, type = View.OnKeyListener.class)
    private boolean edtfilterOnKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            try{
                String Fileter = editBarcode.getText().toString().trim();
                final Map<String, String> params = new HashMap<String, String>();
                params.put("SerialNo", Fileter);
                LogUtil.WriteLog(LineStockInReturn.class, TAG_Get_barcode, Fileter);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Get_barcode, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Get_barcode, null,  URLModel.GetURL().GetT_SerialNoADF, params, null);
                return false;
                }catch (Exception ex){
                    MessageBox.Show(context,ex.toString());
                }
            }

        return false;
    }

    @Event(value = {R.id.button6},type = View.OnClickListener.class)
    private void onClick(View view) {
        try{
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return;
            }
//            editNum.setText("0");
            if (txtBatch.getText().toString().isEmpty()||
                    txtNo.getText().toString().isEmpty()||
//                    txtMaName.getText().toString().isEmpty()||
                    txtName.getText().toString().isEmpty()||
                    editNum.getText().toString().isEmpty()){
                MessageBox.Show(context, "填写信息不能为空！");
                return;
            }

            if (!CommonUtil.isFloat(editNum.getText().toString())){
                MessageBox.Show(context, "数量格式错误！");
                return;
            }
            printlabel();
        }catch(Exception ex){
            MessageBox.Show(context, ex.toString());
        }


    }

    BarCodeInfo barCodeM;
    /*
    解析物料条码扫描
     */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        LogUtil.WriteLog(CombinPallet.class, TAG_Get_barcode,result);
        try {
            ReturnMsgModel<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                barCodeM = returnMsgModel.getModelJson();
                //判断扫描的物料是不是存在明细中
                boolean flag=false;
                for(WoDetailModel Model:WoDetailModels){
                   if(Model.getMaterialNo().toString().equals(barCodeM.getMaterialNo().toString()))
                   {
                       flag=true;
                   }
                }
                if (!flag){
                    MessageBox.Show(context, "扫描条码信息和工单明细物料不匹配！");
                    CommonUtil.setEditFocus(editBarcode);
                    return;
                }
                txtBatch.setText(barCodeM.getBatchNo());
                txtMaName.setText(barCodeM.getMaterialDesc());
//                if(barCodeM.getLabelMark().toString().equals("OutSanZhuang")||barCodeM.getLabelMark().toString().equals("OutR")){
//                    editNum.setText("");
//                }else{
//                    editNum.setText(barCodeM.getQty().toString());
//                }
//                editNum.setText(barCodeM.getQty().toString());
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception e){
            MessageBox.Show(context,e.toString());
        }finally {
            CommonUtil.setEditFocus(editBarcode);
        }
        CommonUtil.setEditFocus(editBarcode);
    }


    public void printlabel() {
        try {
            ArrayList<Barcode_Model> models =new ArrayList<>();//临时的外箱打印标签

            Barcode_Model model =new Barcode_Model();
            model.setRowNoDel(barCodeM.getRowNoDel());;//ymh
//            model.setProtectWay(barCodeM.getprot);;//ymh防护措施
            model.setVoucherNo(barCodeM.getVoucherNo());;//ymh
            model.setVoucherType(barCodeM.getVoucherType());;//ymh
            model.setCreater(barCodeM.getCreater());;
            model.setSTATUS(barCodeM.getStatus());
//            model.setwarehousename();
//            model.setwarehouseno() ;
//            model.setAllIn(barCodeM.get)  ; //用来判断是在库存还是条码表1库存，0条码
            model.setProductClass(barCodeM.getProductClass())  ; //ymh生产班组
//            model.setBoxWeight(barCodeM.getb)  ; //ymh包装方式
//            model.setItemQty()  ; //ymh数量
//            model.setLineno(barCodeM.getlin);  ; //ymh产线
            model.setBarcodeNo(barCodeM.getBarcodeNo());//ymh
            model.setRelaWeight(barCodeM.getRelaWeight());//ymh相对比重
            model.setStoreCondition(barCodeM.getStoreconDition());//
            model.setSupName(barCodeM.getSupName());//ymh 生产日期周六区分
//            model.setid();
            model.setMaterialNoID(barCodeM.getMaterialNoID()) ;
//            model.setBatchNo(barCodeM.getBatchNo());
//            model.setSqty(barCodeM.getsq);
            model.setMaterialDesc(barCodeM.getMaterialDesc());
            model.setBarCode(barCodeM.getBarCode());
//            model.setCHECKNO(barCodeM.getch) ;
            model.setAREAID(barCodeM.getAreaID()) ;
//            model.setAreano(barCodeM.getar);
            model.setSerialNo(barCodeM.getSerialNo());
            model.setLabelMark(barCodeM.getLabelMark());
            model.setSupPrdBatch(barCodeM.getSupPrdBatch());
            model.setProductDate(barCodeM.getProductDate());
//            model.setEds(barCodeM.geted);
            model.setBarcodeType(barCodeM.getBarcodeType());


            model.setIP(URLModel.PrintIP+":9100");
            model.setStrongHoldCode(barCodeM.getStrongHoldCode());
            model.setStrongHoldName(barCodeM.getStrongHoldName());
            model.setErpVoucherNo(barCodeM.getErpVoucherNo());
            model.setMaterialNo(barCodeM.getMaterialNo());
            model.setUnit(barCodeM.getUnit());
            model.setProductClass(barCodeM.getProductClass());
            model.setEDate(barCodeM.getEDate());
            model.setCompanyCode(barCodeM.getCompanyCode());
            model.setVoucherNo(barCodeM.getVoucherNo());
            model.setVoucherType(barCodeM.getVoucherType());
            model.setRowNoDel("1");



            model.setBatchNo(txtBatch.getText().toString());
            model.setQty(Float.parseFloat(editNum.getText().toString()));//数量



            String Weight=txtWeight.getText().toString();
            if (Weight.equals(""))
            {
                MessageBox.Show(context, "电子称不稳定，无法获取数据！");
                return;
            }
            else{
                if (!CommonUtil.isFloat(Weight)){
                    Weight=Weight.substring(0,Weight.length()-2).trim();
                    if (!CommonUtil.isFloat(Weight))
                    {
                        MessageBox.Show(context, "电子称不稳定，无法获取数据！");
                        return;
                    }
                    else{
                        model.setItemQty(Weight);//重量
                    }
                }else{
                    model.setItemQty(Weight);//重量
                }

            }
            if(model.getQty()==0f&&Float.parseFloat(model.getItemQty())!=0f){
                model.setQty(Float.parseFloat(model.getItemQty()));
            }


//            if (flag==1 ){
//                model.setBarcodeType(1);
//                if (barCodeM.getLabelMark().toString().equals("OutBanZhi"))
//                {
//                    //半制外
//                    model.setLabelMark("OutBanZhi");
//                    model.setSupPrdBatch(model.getBatchNo());
//                    model.setBarcodeNo(1);
//                }
//                if (barCodeM.getLabelMark().toString().equals("OutSanZhuang"))
//                {
//                    //散装外
//                    model.setQty(Float.parseFloat(model.getItemQty()));
//                    model.setItemQty("0");
//                    model.setLabelMark("OutSanZhuang");
//                    model.setRelaWeight(barCodeM.getRelaWeight());
//
//                }
//                if (barCodeM.getLabelMark().toString().equals("OutR"))
//                {
//                    //原材料
//                    model.setQty(Float.parseFloat(model.getItemQty()));
//                    model.setItemQty("0");
//                    model.setLabelMark("OutR");
//                    model.setRelaWeight(barCodeM.getRelaWeight());
//
//                }
                models.add(model);
//            }
            String path="";
            int Returnpath=0;
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
//            if (flag==1 ){
                params.put("json", GsonUtil.parseModelToJson(models));
                params.put("printtype", GsonUtil.parseModelToJson(0));
                path=TAG_Print_Outlabel;
                Returnpath=RESULT_Print_Outlabel;
//            }
            RequestHandler.addRequestWithDialog(Request.Method.POST, path, getString(R.string.Msg_Print), context, mHandler,Returnpath, null,  URLModel.GetURL().PrintLabel, params, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }

    }

    void AnalysisGetT_RESULT_Print_OutlabelADFJson(String result){
        try {
            LogUtil.WriteLog(LineStockInReturn.class, TAG_Print_Outlabel, result);
            ReturnMsgModel<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Barcode_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, "退料标签补打成功");
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
    }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_materialbackconfig, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent=new Intent(context, LineStockInBackProduct.class);
            Bundle bundle=new Bundle();
            bundle.putParcelable("WoModel",womodel);
            intent.putExtras(bundle);
            startActivityLeft(intent);
//            closeActiviry();
        }
        return super.onOptionsItemSelected(item);
    }



}
