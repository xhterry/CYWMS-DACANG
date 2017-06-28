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
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.URLModel;
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

import static com.xx.chinetek.cywms.R.id.edt_UnboxCode;

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
    @ViewInject(edt_UnboxCode)
    EditText edtUnboxCode;
    @ViewInject(R.id.edt_BoxNum)
    EditText edtBoxNum;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
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
    @ViewInject(R.id.txt_unMaterialName)
    TextView txtunMaterialName;
    @ViewInject(R.id.txt_unboxQty)
    TextView txtunBoxQty;
    @ViewInject(R.id.txt_box)
    TextView txtbox;
    @ViewInject(R.id.btn_BoxConfig)
    TextView btnBoxConfig;
    @ViewInject(R.id.conLay_unboxInfo)
    ConstraintLayout conLayunboxInfo;
    @ViewInject(R.id.conLay_boxInfo)
    ConstraintLayout conLayboxInfo;

    boolean isUnbox=false;//判断扫描箱子类型 true:拆箱扫描
    BarCodeInfo unbarCodeInfo=new BarCodeInfo();
    BarCodeInfo barCodeInfo=new BarCodeInfo();

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
            params.put("Barcode", barcode);
                LogUtil.WriteLog(Boxing.class, TAG_GetT_OutBarCodeInfoByBoxADF, barcode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutBarCodeInfoByBoxADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_OutBarCodeInfoByBoxADF, null,  URLModel.GetURL().GetT_GetT_OutBarCodeInfoByBoxADF, params, null);
            return false;
        }
        return false;
    }


    @Event(R.id.btn_BoxConfig)
    private void BtnBoxConfigClick(View v){
        String num=edtBoxNum.getText().toString().trim();
        String returnMsg=CheckInputQty(num);
        if(!returnMsg.equals("")){
            MessageBox.Show(context, returnMsg);
            CommonUtil.setEditFocus(edtBoxNum);
            return;
        }
        Float qty=Float.parseFloat(num);
        unbarCodeInfo.setQty(unbarCodeInfo.getQty()-qty);
        barCodeInfo.setQty(barCodeInfo.getQty()+qty);
        String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
        String strOldBarCode = GsonUtil.parseModelToJson(unbarCodeInfo);
        String strNewBarCode = GsonUtil.parseModelToJson(barCodeInfo);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("UserJson", userJson);
        params.put("strOldBarCode", strOldBarCode);
        params.put("strNewBarCode", strNewBarCode);
        LogUtil.WriteLog(Boxing.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode+"||"+strNewBarCode);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_PalletDetailADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null,  URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);
    }


    /*
 解析物料条码扫描
  */
    void AnalysisGetT_SerialNoByPalletAD(String result){
        LogUtil.WriteLog(Boxing.class, TAG_GetT_OutBarCodeInfoByBoxADF,result);
        ReturnMsgModel<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<BarCodeInfo>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            BarCodeInfo  barCodeInfo=returnMsgModel.getModelJson();
            if(isUnbox){
                this.unbarCodeInfo=barCodeInfo;
                txtunBatch.setText(barCodeInfo.getBatchNo());
                txtunBoxQty.setText(barCodeInfo.getQty()+"/"+barCodeInfo.getOutPackQty());
                txtunMaterialName.setText(barCodeInfo.getMaterialDesc());
                txtunCompany.setText(barCodeInfo.getStrongHoldName());
                txtunStatus.setText(barCodeInfo.getStatus());
                if(SWBox.isChecked()){
                    this.barCodeInfo=new BarCodeInfo();
                    this.barCodeInfo.setQty(0f);
                }
            }else{
                this.barCodeInfo=barCodeInfo;
                txtBatch.setText(barCodeInfo.getBatchNo());
                txtBoxQty.setText(barCodeInfo.getQty()+"/"+barCodeInfo.getOutPackQty());
                txtMaterialName.setText(barCodeInfo.getMaterialDesc());
                txtCompany.setText(barCodeInfo.getStrongHoldName());
                txtStatus.setText(barCodeInfo.getStatus());
            }

        }else
        {
            ToastUtil.show(returnMsgModel.getMessage());
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
       } catch (Exception ex) {
           MessageBox.Show(context, ex.getMessage());
       }
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
        }else{
            conLayboxInfo.setVisibility(View.GONE);
            edtBoxCode.setVisibility(View.GONE);
            txtbox.setVisibility(View.GONE);
        }
        CommonUtil.setEditFocus(edtUnboxCode);
    }

    String CheckInputQty(String num){
        if(!CommonUtil.isFloat(num)) {
            return getString(R.string.Error_isnotnum);
        }
        Float qty=Float.parseFloat(num);
        if(qty>unbarCodeInfo.getQty()){
            return getString(R.string.Error_QtyBiger);
        }
        if(qty>Integer.parseInt(txtunBoxQty.getText().toString().split("/")[1])-Integer.parseInt(txtunBoxQty.getText().toString().split("/")[0])){
            return getString(R.string.Error_PackageQtyBiger);
        }
        return "";
    }
}
