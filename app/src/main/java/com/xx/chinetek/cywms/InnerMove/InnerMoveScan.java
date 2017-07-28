package com.xx.chinetek.cywms.InnerMove;

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
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.InnerMove.InnerMoveAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.cywms.UpShelf.UpShelfScanActivity;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Stock.AreaInfo_Model;
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


@ContentView(R.layout.activity_inner_move_scan)
public class InnerMoveScan extends BaseActivity {

    String TAG_GetStockModelADF="InnerMoveScan_GetStockModelADF";
    String TAG_GetAreaModelByMoveStockADF="UpShelfScanActivity_GetAreaModelADF";
    String TAG_SaveT_StockADF="UpShelfScanActivity_SaveT_StockADF";

    private final int RESULT_Msg_GetStockModelADF=101;
    private final int RESULT_GetAreaModelByMoveStockADF=102;
    private final int RESULT_SaveT_StockADF=103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockModelADFJson((String) msg.obj);
                break;
            case RESULT_GetAreaModelByMoveStockADF:
                AnalysisGetAreaModelByMoveStockADFJson((String) msg.obj);
                break;
            case RESULT_SaveT_StockADF:
                AnalysisSaveT_StockADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

  Context context=InnerMoveScan.this;
    @ViewInject(R.id.lsv_InnerMoveDetail)
    ListView lsvInnerMoveDetail;
    @ViewInject(R.id.tb_MoveType)
    ToggleButton TBMoveType;
    @ViewInject(R.id.edt_MoveInStock)
    EditText edtMoveInStock;
    @ViewInject(R.id.edt_MoveScanBarcode)
    EditText edtMoveScanBarcode;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;

    List<StockInfo_Model> stockInfoModels;
    AreaInfo_Model OutAreaInfoModel=null;//扫描库位
    InnerMoveAdapter innerMoveAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.InnerMove_subtitle), false);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        stockInfoModels=new ArrayList<>();
    }

    @Event(value = R.id.edt_MoveInStock,type = View.OnKeyListener.class)
    private  boolean edtMoveInStock(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String StockCode=edtMoveInStock.getText().toString().trim();
            if(TextUtils.isEmpty(StockCode)){
                CommonUtil.setEditFocus(edtMoveScanBarcode);
            }

        }
        return false;
    }

    @Event(value = R.id.edt_MoveScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtMoveScanBarcode(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode=edtMoveScanBarcode.getText().toString().trim();
            if(!TextUtils.isEmpty(barcode)) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("BarCode", barcode);
                params.put("ScanType", TBMoveType.isChecked()?"1":"2");
                params.put("MoveType", "2"); //1：下架 2:移库
                LogUtil.WriteLog(InnerMoveScan.class, TAG_GetStockModelADF, barcode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            OutAreaInfoModel=null;
            edtMoveScanBarcode.setText("");
            CommonUtil.setEditFocus(edtMoveInStock);
            return true;

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
            for (int i=0;i<stockInfoModels.size();i++) {
                stockInfoModels.get(i).setVoucherType(9996);
            }
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson = GsonUtil.parseModelToJson(stockInfoModels);
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(InnerMoveScan.class, TAG_SaveT_StockADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_StockADF, getString(R.string.Msg_SaveT_InStockDetailADF), context, mHandler, RESULT_SaveT_StockADF, null,  URLModel.GetURL().SaveT_StockADF, params, null);
        }
        return super.onOptionsItemSelected(item);
    }


    /*
   扫描条码
    */
    void AnalysisGetStockModelADFJson(String result){
        try {
            LogUtil.WriteLog(InnerMoveScan.class, TAG_GetStockModelADF, result);
            ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ArrayList<StockInfo_Model> tempstockInfoModels = returnMsgModel.getModelJson();
                if (tempstockInfoModels != null && tempstockInfoModels.size() > 0) {
                    txtCompany.setText(tempstockInfoModels.get(0).getStrongHoldName());
                    txtStatus.setText(tempstockInfoModels.get(0).getStrStatus());
                    txtEDate.setText(CommonUtil.DateToString(tempstockInfoModels.get(0).getEDate()));
                    txtBatch.setText(tempstockInfoModels.get(0).getBatchNo());
                    txtMaterialName.setText(tempstockInfoModels.get(0).getMaterialDesc());
                    this.stockInfoModels.addAll(0, tempstockInfoModels);
                    // BindArea();
                    if (stockInfoModels != null && stockInfoModels.size() > 0) {
                        String StockCode=edtMoveInStock.getText().toString().trim();
                        final Map<String, String> params = new HashMap<String, String>();
                        String ModelJson = GsonUtil.parseModelToJson(stockInfoModels);
                        params.put("AreaNo", StockCode);
                        params.put("ModelJson", ModelJson);
                        LogUtil.WriteLog(InnerMoveScan.class, TAG_GetAreaModelByMoveStockADF, StockCode+"|"+ModelJson);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelByMoveStockADF, getString(R.string.Msg_GetAreaModelADF), context, mHandler, RESULT_GetAreaModelByMoveStockADF, null, URLModel.GetURL().GetAreaModelByMoveStockADF, params, null);
                    }
                }

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
                CommonUtil.setEditFocus(edtMoveScanBarcode);
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
            CommonUtil.setEditFocus(edtMoveScanBarcode);
        }
    }

    /*
   扫描库位
    */
    void AnalysisGetAreaModelByMoveStockADFJson(String result){
        try {
            LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetAreaModelByMoveStockADF, result);
            ReturnMsgModel<AreaInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                OutAreaInfoModel = returnMsgModel.getModelJson();
                BindArea();
                BindListVIew(stockInfoModels);

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex) {
            MessageBox.Show(context,ex.getMessage());
        }
        CommonUtil.setEditFocus(edtMoveScanBarcode);
    }

    void AnalysisSaveT_StockADFJson(String result){
        try {
            LogUtil.WriteLog(InnerMoveScan.class, TAG_SaveT_StockADF,result);
            ReturnMsgModel<AreaInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                MessageBox.Show(context, returnMsgModel.getMessage());
                intiFrm();
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtMoveScanBarcode);
    }

    private void BindListVIew(List<StockInfo_Model> stockInfo_models) {
        innerMoveAdapter=new InnerMoveAdapter(context,stockInfo_models);
        lsvInnerMoveDetail.setAdapter(innerMoveAdapter);
    }

    void BindArea(){
        if(stockInfoModels!=null && OutAreaInfoModel!=null) {
            for (int i = 0; i < stockInfoModels.size(); i++) {
                stockInfoModels.get(i).setAreaNo(OutAreaInfoModel.getAreaNo());
                stockInfoModels.get(i).setHouseNo(OutAreaInfoModel.getHouseNo());
                stockInfoModels.get(i).setWarehouseNo(OutAreaInfoModel.getWarehouseNo());
                stockInfoModels.get(i).setAreaID(OutAreaInfoModel.getID());
                stockInfoModels.get(i).setHouseID(OutAreaInfoModel.getHouseID());
                stockInfoModels.get(i).setWareHouseID(OutAreaInfoModel.getWarehouseID());
                stockInfoModels.get(i).setStatus(OutAreaInfoModel.getIsQuality());
                stockInfoModels.get(i).setToErpAreaNo(OutAreaInfoModel.getAreaNo());
                stockInfoModels.get(i).setFromErpAreaNo(OutAreaInfoModel.getAreaNo());
                stockInfoModels.get(i).setToErpWarehouse(OutAreaInfoModel.getWarehouseNo());
                stockInfoModels.get(i).setFromErpWarehouse(OutAreaInfoModel.getWarehouseNo());
                stockInfoModels.get(i).setFromBatchNo( stockInfoModels.get(i).getBatchNo());
            }
        }
    }


    void intiFrm(){
        txtCompany.setText("");
        txtBatch.setText("");
        txtEDate.setText("");
        txtStatus.setText("");
        txtMaterialName.setText("");
        edtMoveInStock.setText("");
        edtMoveScanBarcode.setText("");
        stockInfoModels=new ArrayList<>();
        OutAreaInfoModel=null;
        BindListVIew(stockInfoModels);
        CommonUtil.setEditFocus(edtMoveInStock);
    }


}
