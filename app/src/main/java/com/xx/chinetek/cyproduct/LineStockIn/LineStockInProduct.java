package com.xx.chinetek.cyproduct.LineStockIn;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Upshelf.UpShelfScanDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
import com.xx.chinetek.model.WMS.UpShelf.InStockTaskDetailsInfo_Model;
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

@ContentView(R.layout.activity_product_linestockinproduct_scan)
public class LineStockInProduct extends BaseActivity {

    String TAG_GetT_ScanInStockModelADF="UpShelfScanActivity_GetT_ScanInStockModelADF";

    private final int RESULT_Msg_GetT_ScanInStockModelADF=102;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
                     case RESULT_Msg_GetT_ScanInStockModelADF:
                AnalysisetT_PalletDetailByBarCodeJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
                break;
        }
    }

    Context context = LineStockInProduct.this;
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
    @ViewInject(R.id.txt_LineStockInNum)
    TextView txtLineStockInNum;
    @ViewInject(R.id.edt_LineStockInScanBarcode)
    EditText edtLineStockInScanBarcode;

    ArrayList<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels;
    ArrayList<StockInfo_Model> stockInfoModels=null;
    UpShelfScanDetailAdapter upShelfScanDetailAdapter;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.Product_ProductStockin_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        CommonUtil.setEditFocus(edtLineStockInScanBarcode);
    }

    @Event(value =R.id.edt_LineStockInScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtLineStockInScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code = edtLineStockInScanBarcode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
                return true;
            }

            //接口需要定义
            final Map<String, String> params = new HashMap<String, String>();
            params.put("SerialNo", code);
            params.put("WareHouseID", BaseApplication.userInfo.getWarehouseID()+"");
            LogUtil.WriteLog(LineStockInProduct.class, TAG_GetT_ScanInStockModelADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_ScanInStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_ScanInStockModelADF, null,  URLModel.GetURL().GetT_ScanInStockModelADF, params, null);


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

        }
        return super.onOptionsItemSelected(item);
    }


    /*
   扫描条码
    */
    void AnalysisetT_PalletDetailByBarCodeJson(String result){
        LogUtil.WriteLog(LineStockInProduct.class, TAG_GetT_ScanInStockModelADF,result);
        ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            stockInfoModels=returnMsgModel.getModelJson();
            if(stockInfoModels!=null && stockInfoModels.size()!=0) {
                for (StockInfo_Model stockInfoModel:stockInfoModels){
                   // if(!CheckBarcode(stockInfoModel))
                        break;
                }
                InitFrm(stockInfoModels.get(0));
                BindListVIew(inStockTaskDetailsInfoModels);
                CommonUtil.setEditFocus(edtLineStockInScanBarcode);
            }
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
            CommonUtil.setEditFocus(edtLineStockInScanBarcode);
        }
    }




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

    void InitFrm(StockInfo_Model stockInfoModel){
        try {
            if (stockInfoModel != null) {
                txtCompany.setText(stockInfoModel.getStrongHoldName());
                txtBatch.setText(stockInfoModel.getBatchNo());
                txtStatus.setText("");
                txtMaterialName.setText(stockInfoModel.getMaterialDesc());
                txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));

            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtLineStockInScanBarcode);
        }
    }







    private void BindListVIew(ArrayList<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels) {
        upShelfScanDetailAdapter=new UpShelfScanDetailAdapter(context,inStockTaskDetailsInfoModels);
        lsvLineStockInProduct.setAdapter(upShelfScanDetailAdapter);
    }



    void ClearFrm(){
        stockInfoModels = new ArrayList<>();
        edtLineStockInScanBarcode.setText("");
        txtLineStockInNum.setText("");
        txtCompany.setText("");
        txtBatch.setText("");
        txtEDate.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
    }




}
