package com.xx.chinetek.Box;

import android.content.Context;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_boxing)
public class Boxing extends BaseActivity {

    String TAG_GetT_OutBarCodeInfoByBoxADF="Boxing_GetT_OutBarCodeInfoByBoxADF";
    String TAG_SaveT_BarCodeToStockADF="Boxing_SaveT_BarCodeToStockADF";

    private final int RESULT_GetT_OutBarCodeInfoByBoxADF = 101;
    private final int RESULT_SaveT_BarCodeToStockADF = 102;

    @Override
    public void onHandleMessage(Message msg) {

        switch (msg.what) {
            case RESULT_GetT_OutBarCodeInfoByBoxADF:
                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
            case RESULT_SaveT_BarCodeToStockADF:
                AnalysisSaveT_BarCodeToStockADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
                break;
        }
    }

    Context context=Boxing.this;

    @ViewInject(R.id.SW_Box)
    Switch SWBox;
    @ViewInject(R.id.edt_BoxCode)
    EditText edtBoxCode;
    @ViewInject(R.id.edt_UnboxCode)
    EditText edtUnboxCode;
    @ViewInject(R.id.edt_BoxNum)
    EditText edtBoxNum;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txt_EDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_boxQty)
    TextView txtBoxQty;
    @ViewInject(R.id.txt_unCompany)
    TextView txtunCompany;
    @ViewInject(R.id.txt_unBatch)
    TextView txtunBatch;
    @ViewInject(R.id.txt_unStatus)
    TextView txtunStatus;
    @ViewInject(R.id.txt_UnEDate)
    TextView txt_UnEDate;
    @ViewInject(R.id.txt_unMaterialName)
    TextView txtunMaterialName;
    @ViewInject(R.id.txt_unboxQty)
    TextView txtunBoxQty;
    @ViewInject(R.id.txt_box)
    TextView txtbox;
    @ViewInject(R.id.txt_boxNum)
    TextView txtboxNum;
    @ViewInject(R.id.btn_BoxConfig)
    TextView btnBoxConfig;
    @ViewInject(R.id.conLay_unboxInfo)
    ConstraintLayout conLayunboxInfo;
    @ViewInject(R.id.conLay_boxInfo)
    ConstraintLayout conLayboxInfo;

    boolean isUnbox=false;//判断扫描箱子类型 true:拆箱扫描
    StockInfo_Model unStockInfoModel=new StockInfo_Model();
    StockInfo_Model stockInfoModel=new StockInfo_Model();

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Boxing_title), false);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        ShowBoxScan(SWBox.isChecked());

    }

    @Event(value = R.id.SW_Box,type = CompoundButton.OnCheckedChangeListener.class)
    private void SwPalletonCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ShowBoxScan(isChecked);
    }

    @Event(value ={R.id.edt_UnboxCode,R.id.edt_BoxCode} ,type = View.OnKeyListener.class)
    private  boolean edtboxCodeonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            isUnbox=v.getId()==R.id.edt_UnboxCode;
            String barcode=v.getId()==R.id.edt_UnboxCode?
                    edtUnboxCode.getText().toString().trim():edtBoxCode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", barcode);
                LogUtil.WriteLog(Boxing.class, TAG_GetT_OutBarCodeInfoByBoxADF, barcode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutBarCodeInfoByBoxADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_OutBarCodeInfoByBoxADF, null,  URLModel.GetURL().GetT_GetT_OutBarCodeInfoByBoxADF, params, null);
            return false;
        }
        return false;
    }

    @Event(R.id.btn_BoxConfig)
    private void BtnBoxConfigClick(View v){
        if(SWBox.isChecked()) {
            String num = edtBoxNum.getText().toString().trim();
            String returnMsg = CheckInputQty(num);
            if (!returnMsg.equals("")) {
                MessageBox.Show(context, returnMsg);
                CommonUtil.setEditFocus(edtBoxNum);
                return;
            }
            Float qty = Float.parseFloat(num);
            unStockInfoModel.setAmountQty(qty);
        }
        if(!(unStockInfoModel.getMaterialNo().equals(stockInfoModel.getMaterialNo()) &&
            unStockInfoModel.getStrongHoldCode().equals(stockInfoModel.getStrongHoldCode()))){
            MessageBox.Show(context, getString(R.string.Error_MaterialNotMatch));
            CommonUtil.setEditFocus(edtBoxCode);
            return;
        }

        String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
        String strOldBarCode = GsonUtil.parseModelToJson(unStockInfoModel);
        String strNewBarCode =SWBox.isChecked()?"": GsonUtil.parseModelToJson(stockInfoModel);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("UserJson", userJson);
        params.put(!SWBox.isChecked()?"strNewBarCode":"strOldBarCode", strOldBarCode);
        params.put(!SWBox.isChecked()?"strOldBarCode":"strNewBarCode", strNewBarCode);
        LogUtil.WriteLog(Boxing.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode+"||"+strNewBarCode);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null,  URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);
    }


    /*
 解析物料条码扫描
  */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        LogUtil.WriteLog(Boxing.class, TAG_GetT_OutBarCodeInfoByBoxADF,result);
        ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            try {
                StockInfo_Model stockInfoModel = returnMsgModel.getModelJson();
                if (isUnbox) {
                    this.unStockInfoModel = stockInfoModel;
                    txtunBatch.setText(stockInfoModel.getBatchNo());
                    txtunBoxQty.setText(stockInfoModel.getQty() + "");
                    txtunMaterialName.setText(stockInfoModel.getMaterialDesc());
                    txtunCompany.setText(stockInfoModel.getStrongHoldName());
                    txtunStatus.setText(stockInfoModel.getStrStatus());
                    txt_UnEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
                    if (SWBox.isChecked()) {
                        this.stockInfoModel = new StockInfo_Model();
                        this.stockInfoModel.setQty(0f);
                    }
                } else {
                    this.stockInfoModel = stockInfoModel;
                    txtBatch.setText(stockInfoModel.getBatchNo());
                    txtBoxQty.setText(stockInfoModel.getQty() + "");
                    txtMaterialName.setText(stockInfoModel.getMaterialDesc());
                    txtCompany.setText(stockInfoModel.getStrongHoldName());
                    txtStatus.setText(stockInfoModel.getStrStatus());
                    txt_EDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
                }
            }catch (Exception ex){
                ToastUtil.show(returnMsgModel.getMessage());
                CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
            }
        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
            CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
        }
    }

    /*
    装箱拆箱提交
     */
   void AnalysisSaveT_BarCodeToStockADF(String result){
       try {
           LogUtil.WriteLog(Boxing.class, TAG_SaveT_BarCodeToStockADF, result);
           ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
           }.getType());
           MessageBox.Show(context, returnMsgModel.getMessage());
           if(returnMsgModel.getHeaderStatus().equals("S")){
               clearFrm();
           }
       } catch (Exception ex) {
           MessageBox.Show(context, ex.getMessage());
       }
       CommonUtil.setEditFocus(edtUnboxCode);
   }


    /*
    显示隐藏物料信息
     */
    void ShowBoxScan(boolean check){
        edtBoxCode.setText("");
        edtUnboxCode.setText("");
        edtBoxNum.setText("");
        if(!check){
            conLayboxInfo.setVisibility(View.VISIBLE);
            edtBoxCode.setVisibility(View.VISIBLE);
            txtbox.setVisibility(View.VISIBLE);
            txtboxNum.setVisibility(View.GONE);
            edtBoxNum.setVisibility(View.GONE);
        }else{
            conLayboxInfo.setVisibility(View.GONE);
            edtBoxCode.setVisibility(View.GONE);
            txtbox.setVisibility(View.GONE);
            txtboxNum.setVisibility(View.VISIBLE);
            edtBoxNum.setVisibility(View.VISIBLE);
        }
        CommonUtil.setEditFocus(edtUnboxCode);
    }

    String CheckInputQty(String num){
        if(!CommonUtil.isFloat(num)) {
            return getString(R.string.Error_isnotnum);
        }
        Float qty=Float.parseFloat(num);
        if(qty>unStockInfoModel.getQty()){
            return getString(R.string.Error_QtyBiger);
        }
//        if(qty>Integer.parseInt(txtunBoxQty.getText().toString().split("/")[1])-Integer.parseInt(txtunBoxQty.getText().toString().split("/")[0])){
//            return getString(R.string.Error_PackageQtyBiger);
//        }
        return "";
    }


    void clearFrm(){
        this.unStockInfoModel=new StockInfo_Model();
        this.stockInfoModel=new StockInfo_Model();
        edtBoxCode.setText("");
        edtUnboxCode.setText("");
        edtBoxNum.setText("");
        txtunBatch.setText("");
        txtunBoxQty.setText("");
        txtunMaterialName.setText("");
        txtunCompany.setText("");
        txtunStatus.setText("");
        txt_UnEDate.setText("");
        txtBatch.setText("");
        txtBoxQty.setText("");
        txtMaterialName.setText("");
        txtCompany.setText("");
        txtStatus.setText("");
        txt_EDate.setText("");
        CommonUtil.setEditFocus(edtUnboxCode);
    }
}
