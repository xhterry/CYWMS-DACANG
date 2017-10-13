package com.xx.chinetek.cyproduct.LineStockOut;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.Manage.WoDetailMaterialItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cyproduct.Manage.ProductMaterialConfig;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.CheckNumRefMaterial;
import com.xx.chinetek.model.Production.Wo.WoDetailModel;
import com.xx.chinetek.model.Production.Wo.WoModel;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.User.UerInfo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.zcj)
public class Zcj extends BaseActivity {

    String TAG_PostZhiChengJianADF="zcj_PostZhiChengJianADF";
    private final int RESULT_PostZhiChengJianADF=101;

    String TAG_GetStockModelADF="zcj_GetStockModelADF";
    private final int RESULT_Msg_GetStockModelADF=102;

    String TAG_SaveNewBarcodeToStockForZhiChengJianADF="zcj_SaveNewBarcodeToStockForZhiChengJian";
    private final int RESULT_SaveNewBarcodeToStockForZhiChengJianADF = 104;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_PostZhiChengJianADF:
                AnalysisPostZhiChengJianADFADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockModelADFJson((String) msg.obj);
                break;
            case RESULT_SaveNewBarcodeToStockForZhiChengJianADF:
                AnalysisSaveNewBarcodeToStockForZhiChengJianADFADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    @ViewInject(R.id.lsv_LineStockOutMaterial)
    ListView lsv_LineStockOutMaterial;
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
    @ViewInject(R.id.edt_LineStockOutScanBarcode)
    EditText edtLineStockOutScanBarcode;
    @ViewInject(R.id.edt_Unboxing)
    EditText edtUnboxing;
    @ViewInject(R.id.txt_Unboxing)
    TextView txtUnboxing;

    Context context=Zcj.this;
    ArrayList<WoDetailModel> woDetailModels = new ArrayList<>();
    WoDetailMaterialItemAdapter woDetailMaterialItemAdapter;
    ArrayList<StockInfo_Model> stockInfoModels;//扫描条码
    ArrayList<StockInfo_Model> stockInfoModelsAll = new ArrayList<>();//所有的条码汇总

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }



    @Override
    protected void initData() {
        super.initData();
        txtUnboxing.setVisibility(View.VISIBLE);
        edtUnboxing.setVisibility(View.VISIBLE);
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }



    @Event(value =R.id.edt_LineStockOutScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtLineStockOutScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtLineStockOutScanBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            params.put("ScanType", "3");
            params.put("MoveType", "1"); //1：下架 2:移库
            params.put("IsEdate", "2"); //1：不判断有效期 2:判断有效期
            LogUtil.WriteLog(Zcj.class, TAG_GetStockModelADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF_Product, params, null);


        }
        return false;
    }

    @Event(value =R.id.edt_Unboxing,type = View.OnKeyListener.class)
    private  boolean edtUnboxingClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String num=edtUnboxing.getText().toString().trim();
            if (stockInfoModels != null && stockInfoModels.size() != 0) {
                CheckNumRefMaterial checkNumRefMaterial = CheckMaterialNumFormat(num, stockInfoModels.get(0).getUnitTypeCode(), stockInfoModels.get(0).getDecimalLngth());
                if (!checkNumRefMaterial.ischeck()) {
                    MessageBox.Show(context, checkNumRefMaterial.getErrMsg());
                    CommonUtil.setEditFocus(edtUnboxing);
                    return true;
                }
                Float qty = checkNumRefMaterial.getCheckQty(); //输入数量
                Float scanQty = stockInfoModels.get(0).getQty(); //箱数量
                if (qty > scanQty) {
                    MessageBox.Show(context, getString(R.string.Error_PackageQtyBiger));
                    CommonUtil.setEditFocus(edtUnboxing);
                    return true;
                }
                //拆零
                stockInfoModels.get(0).setPickModel(3);
                stockInfoModels.get(0).setAmountQty(qty);
                String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                String strOldBarCode = GsonUtil.parseModelToJson(stockInfoModels.get(0));
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", userJson);
                params.put("strOldBarCode", strOldBarCode);
                params.put("strNewBarCode", "");
                params.put("PrintFlag", "1"); //0：不打印； 1：打印
                LogUtil.WriteLog(Zcj.class, TAG_SaveNewBarcodeToStockForZhiChengJianADF, strOldBarCode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveNewBarcodeToStockForZhiChengJianADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveNewBarcodeToStockForZhiChengJianADF, null, URLModel.GetURL().SaveNewBarcodeToStockForZhiChengJian, params, null);

            } else {
                MessageBox.Show(context, getString(R.string.Hit_ScanBarcode));
                edtUnboxing.setText("");
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return false;
            }
            try {
                if(stockInfoModelsAll!=null&&stockInfoModelsAll.size()>0){
                    final Map<String, String> params = new HashMap<String, String>();
                    UerInfo user = BaseApplication.userInfo;
                    user.setWarehouseID(14);
                    user.setReceiveHouseID(1080);
                    user.setReceiveAreaID(30329);
                    String userJson = GsonUtil.parseModelToJson(user);
                    params.put("UserJson", userJson);
                    params.put("StockInfoJson", GsonUtil.parseModelToJson(stockInfoModelsAll));
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PostZhiChengJianADF, "提交中...", context, mHandler, RESULT_PostZhiChengJianADF, null, URLModel.GetURL().Post_DBZaRuInStockERPADF, params, null);
                }

            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
        }
        return super.onOptionsItemSelected(item);
    }




    //清空数据
    void ClearFrm(){
        woDetailModels= new ArrayList<>();
        BindListview(woDetailModels);
    }



    void  AnalysisPostZhiChengJianADFADFJson(String result){
        try {
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
            MessageBox.Show(context,returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ClearFrm();
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
    }
    /*扫描条码*/
    void AnalysisGetStockModelADFJson(String result) {
        LogUtil.WriteLog(Zcj.class, TAG_GetStockModelADF, result);
        try {
            ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                stockInfoModels = returnMsgModel.getModelJson();
                InitFrm(stockInfoModels.get(0));
                edtUnboxing.setText(stockInfoModels.get(0).getQty().toString());
                CommonUtil.setEditFocus(edtUnboxing );
                return;
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
        }

    }

    /*拆箱提交*/
    void AnalysisSaveNewBarcodeToStockForZhiChengJianADFADF(String result){
        try {
            LogUtil.WriteLog(Zcj.class, TAG_SaveNewBarcodeToStockForZhiChengJianADF, result);
            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {
            }.getType());
            edtLineStockOutScanBarcode.setText("");
            if(returnMsgModel.getHeaderStatus().equals("S")){
                StockInfo_Model stockInfoModel=returnMsgModel.getModelJson();
//                stockInfoModels=new ArrayList<>();
//                stockInfoModels.add(stockInfoModel);

                //赋值
                WoDetailModel wmodel = new WoDetailModel();
                wmodel.setMaterialNo(stockInfoModel.getMaterialNo());
                wmodel.setMaterialDesc(stockInfoModel.getMaterialDesc());
                wmodel.setFromBatchNo(stockInfoModel.getBatchNo());
                wmodel.setScanQty(stockInfoModel.getQty());
                wmodel.setWoQty(0f);
                woDetailModels.add(wmodel);
                stockInfoModelsAll.add(stockInfoModel);
                BindListview(woDetailModels);

            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        edtUnboxing.setText("");
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);

    }

    void InitFrm(StockInfo_Model stockInfoModel){
        if(stockInfoModel!=null ){
            txtCompany.setText(stockInfoModel.getStrongHoldName());
            txtBatch.setText(stockInfoModel.getBatchNo());
            txtStatus.setText(stockInfoModel.getStrStatus());
            txtMaterialName.setText(stockInfoModel.getMaterialDesc());
            txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
        }
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }

    void BindListview(ArrayList<WoDetailModel> woDetailModels){
        woDetailMaterialItemAdapter=new WoDetailMaterialItemAdapter(context,woDetailModels);
        lsv_LineStockOutMaterial.setAdapter(woDetailMaterialItemAdapter);
    }


}
