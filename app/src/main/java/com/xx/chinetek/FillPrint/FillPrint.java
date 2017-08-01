package com.xx.chinetek.FillPrint;

import android.content.Context;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.Pallet.CombinPallet;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_fill_print)
public class FillPrint extends BaseActivity {

    String TAG_GetStockModelADF="FillPrint_GetStockModelADF";
    String TAG_PrintLpkPalletAndroid="FillPrint_TAG_PrintLpkPalletAndroid";

    private final int RESULT_Msg_GetStockModelADF=101;
    private final int RESULT_PrintLpkPalletAndroid = 102;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockADFJson((String) msg.obj);
                break;
            case RESULT_PrintLpkPalletAndroid:
                AnalysisPrintLpkPalletAndroid((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLabelScanbarcode);
                break;
        }
    }


   Context context=FillPrint.this;

    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_BatchNo)
    TextView txtBatchNo;
    @ViewInject(R.id.txt_PalletNo)
    TextView txtPalletNo;
    @ViewInject(R.id.tb_Box)
    ToggleButton tbBox;
    @ViewInject(R.id.tb_Pallet)
    ToggleButton tbPallet;
    @ViewInject(R.id.tb_Sample)
    ToggleButton tbSample;
    @ViewInject(R.id.edt_LabelScanbarcode)
    EditText edtLabelScanbarcode;

    List<StockInfo_Model> stockInfoModels;//扫描条码

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Product_fillPrint_subtitle), true);
        x.view().inject(this);
    }

    @Event(value = R.id.edt_LabelScanbarcode,type = View.OnKeyListener.class)
    private boolean edtLabelScanbarcodeClick(View v, int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code = edtLabelScanbarcode.getText().toString().trim();
            if (!tbSample.isChecked()) {
                int type = tbPallet.isChecked() ? 1 : 2;
                final Map<String, String> params = new HashMap<String, String>();
                params.put("BarCode", code);
                params.put("ScanType", type + "");
                params.put("MoveType", "1"); //1：下架 2:移库
                LogUtil.WriteLog(FillPrint.class, TAG_GetStockModelADF, code);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
            }else{
                CommonUtil.setEditFocus(edtLabelScanbarcode);
            }
        }
        return false;
    }

    @Event(R.id.btn_labelPrint)
    private  void btnlabelPrintClick(View view) {
        String SerialNo = "";
        if (tbSample.isChecked()) {
            String [] Barcode=edtLabelScanbarcode.getText().toString().split("@");
            if(Barcode.length>0)
            SerialNo = Barcode[Barcode.length-1];
        }

        if (stockInfoModels != null && stockInfoModels.size() > 0) {
            SerialNo = tbPallet.isChecked() ? stockInfoModels.get(0).getPalletNo() :
                    stockInfoModels.get(0).getSerialNo();
        }
        ArrayList<Barcode_Model> barcodeModels = new ArrayList<>();
        Barcode_Model barcodeModel = new Barcode_Model();
        barcodeModel.setSerialNo(SerialNo);
        barcodeModel.setIP(URLModel.PrintIP);
        barcodeModels.add(barcodeModel);

        String modelJson = GsonUtil.parseModelToJson(barcodeModels);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("json", modelJson);
        LogUtil.WriteLog(CombinPallet.class, TAG_PrintLpkPalletAndroid, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PrintLpkPalletAndroid, getString(R.string.Msg_PrintLpkPalletAndroid), context, mHandler, RESULT_PrintLpkPalletAndroid, null,
                tbPallet.isChecked() ? URLModel.GetURL().PrintLpkPalletAndroid :
                        (tbBox.isChecked() ? URLModel.GetURL().PrintLpkApartAndroid : URLModel.GetURL().QYReprintAndroid), params, null);


    }

    @Event(value = {R.id.tb_Box,R.id.tb_Pallet,R.id.tb_Sample },type = CompoundButton.OnClickListener.class)
    private void TBonCheckedChanged(View view) {
        tbBox.setChecked(view.getId()== R.id.tb_Box);
        tbPallet.setChecked(view.getId()== R.id.tb_Pallet);
        tbSample.setChecked(view.getId()== R.id.tb_Sample);
        initFrm();
        CommonUtil.setEditFocus(edtLabelScanbarcode);
    }

    void AnalysisGetStockADFJson(String result){
        LogUtil.WriteLog(FillPrint.class, TAG_GetStockModelADF,result);
        ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            stockInfoModels=returnMsgModel.getModelJson();
            if(stockInfoModels!=null && stockInfoModels.size()>0) {
                txtMaterialName.setText(stockInfoModels.get(0).getMaterialDesc());
                txtBatchNo.setText(stockInfoModels.get(0).getBatchNo());
                txtPalletNo.setText(stockInfoModels.get(0).getPalletNo());
            }
        }else{
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
        CommonUtil.setEditFocus(edtLabelScanbarcode);
    }

    void AnalysisPrintLpkPalletAndroid(String result){
        try {
            LogUtil.WriteLog(CombinPallet.class, TAG_PrintLpkPalletAndroid, result);
            ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            MessageBox.Show(context,returnMsgModel.getMessage());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                initFrm();
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtLabelScanbarcode);
    }

    void initFrm(){
        txtPalletNo.setText("");
        txtBatchNo.setText("");
        txtMaterialName.setText("");
    }
}
