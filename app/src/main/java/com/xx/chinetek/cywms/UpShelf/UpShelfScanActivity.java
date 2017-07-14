package com.xx.chinetek.cywms.UpShelf;

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
import com.xx.chinetek.adapter.Upshelf.UpShelfScanDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.Stock.AreaInfo_Model;
import com.xx.chinetek.model.Stock.StockInfo_Model;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.UpShelf.InStockTaskDetailsInfo_Model;
import com.xx.chinetek.model.UpShelf.InStockTaskInfo_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.GsonUtil;
import com.xx.chinetek.util.log.LogUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.xx.chinetek.cywms.R.id.edt_StockScan;
import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_up_shelf_scan)
public class UpShelfScanActivity extends BaseActivity {

    String TAG_GetT_InTaskDetailListByHeaderIDADF="UpShelfScanActivity_GetT_InTaskDetailListByHeaderIDADF";
    String TAG_GetT_ScanInStockModelADF="UpShelfScanActivity_GetT_ScanInStockModelADF";
    String TAG_GetAreaModelADF="UpShelfScanActivity_GetAreaModelADF";
    String TAG_SaveT_InStockTaskDetailADF="UpShelfBillChoice_SaveT_InStockTaskDetailADF";

    private final int RESULT_Msg_GetT_InTaskDetailListByHeaderIDADF=101;
    private final int RESULT_Msg_GetT_ScanInStockModelADF=102;
    private final int RESULT_Msg_SaveT_InStockTaskDetailADF=103;
    private final int RESULT_Msg_GetAreaModelADF=104;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_InTaskDetailListByHeaderIDADF:
                AnalysisGetT_InTaskDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_ScanInStockModelADF:
                AnalysisetT_PalletDetailByBarCodeJson((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_InStockTaskDetailADF:
                AnalysisSaveT_InStockTaskDetailADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetAreaModelADF:
                AnalysisGetAreaModelADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
                break;
        }
    }

    Context context = UpShelfScanActivity.this;
    @ViewInject(R.id.lsv_UpShelfScan)
    ListView lsvUpShelfScan;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
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
    @ViewInject(R.id.txt_UpShelfNum)
    TextView txtUpShelfNum;
    @ViewInject(R.id.txt_UpShelfScanNum)
    TextView txtUpShelfScanNum;
    @ViewInject(R.id.txt_ReferStock)
    TextView txtReferStock;
    @ViewInject(R.id.edt_UpShelfScanBarcode)
    EditText edtUpShelfScanBarcode;
    @ViewInject(edt_StockScan)
    EditText edtStockScan;
    @ViewInject(R.id.btn_ShowStock)
    EditText btnShowStock;

    ArrayList<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels;
    InStockTaskInfo_Model inStockTaskInfoModel=null;
    ArrayList<StockInfo_Model> stockInfoModels=null;
    AreaInfo_Model areaInfoModel=null;//扫描库位

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.UpShelfscan_subtitle), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        inStockTaskInfoModel=getIntent().getParcelableExtra("inStockTaskInfoModel");
        stockInfoModels=getIntent().getParcelableArrayListExtra("palletDetailModel");
        GetInStockTaskDetail(inStockTaskInfoModel);

    }

//    @Event(R.id.btnUpShelfDetail)
//    private void btnUpShelfDetailOnclick(View view) {
//        Intent intent = new Intent(context, UpshelfBillDetail.class);
//        startActivityLeft(intent);
//    }

    @Event(value =R.id.edt_UpShelfScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtUpShelfScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code=edtUpShelfScanBarcode.getText().toString().trim();
            String StockCode=edtStockScan.getText().toString().trim();
            if(TextUtils.isEmpty(StockCode)){
                CommonUtil.setEditFocus(edtStockScan);
                return true;
            }
            if(TextUtils.isEmpty(code)){
                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
                return true;
            }
            ScanBarcode(code,StockCode);
          }
        return false;
    }

    @Event(value =R.id.edt_StockScan,type = View.OnKeyListener.class)
    private  boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            if(areaInfoModel!=null  && stockInfoModels!=null){
               MessageBox.Show(context,getString(R.string.Error_Upshelf_HasStcokNotSubmit));
                return true;
            }
            String StockCode=edtStockScan.getText().toString().trim();
            if(TextUtils.isEmpty(StockCode)){
                CommonUtil.setEditFocus(edtStockScan);
                return true;
            }
            final Map<String, String> params = new HashMap<String, String>();
            params.put("AreaNo", StockCode);
            LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetAreaModelADF, StockCode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, getString(R.string.Msg_GetAreaModelADF), context, mHandler, RESULT_Msg_GetAreaModelADF, null,  URLModel.GetURL().GetAreaModelADF, params, null);
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
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson = GsonUtil.parseModelToJson(inStockTaskDetailsInfoModels);
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(UpShelfScanActivity.class, TAG_SaveT_InStockTaskDetailADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_InStockTaskDetailADF, getString(R.string.Msg_SaveT_InStockTaskDetailADF), context, mHandler, RESULT_Msg_SaveT_InStockTaskDetailADF, null,  URLModel.GetURL().SaveT_InStockTaskDetailADF, params, null);

        }
        return super.onOptionsItemSelected(item);
    }

    /*
   处理收货明细
    */
    void AnalysisGetT_InTaskDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetT_InTaskDetailListByHeaderIDADF,result);
        ReturnMsgModelList<InStockTaskDetailsInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<InStockTaskDetailsInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            inStockTaskDetailsInfoModels=returnMsgModel.getModelJson();
            //自动确认扫描箱号
            if(stockInfoModels!=null ) {
                for (StockInfo_Model stockInfoModel : stockInfoModels) {
                    CheckBarcode(stockInfoModel);
                    InitFrm(stockInfoModel);
                }
            }
            BindListVIew(inStockTaskDetailsInfoModels);
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }


    void ScanBarcode(String code,String StockCode){
        final Map<String, String> params = new HashMap<String, String>();
        params.put("SerialNo", code);
        params.put("VoucherNo", inStockTaskInfoModel.getErpVoucherNo());
        params.put("TaskNo", inStockTaskInfoModel.getTaskNo());
        params.put("AreaNo", StockCode);
        LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetT_ScanInStockModelADF, code);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_ScanInStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_ScanInStockModelADF, null,  URLModel.GetURL().GetT_ScanInStockModelADF, params, null);

    }

    /*
    获取收货明细
     */
    void GetInStockTaskDetail(InStockTaskInfo_Model inStockTaskInfoModel){
        if(inStockTaskInfoModel!=null) {
            txtVoucherNo.setText(inStockTaskInfoModel.getTaskNo());
            InStockTaskDetailsInfo_Model inStockTaskDetailsInfoModel = new InStockTaskDetailsInfo_Model();
            inStockTaskDetailsInfoModel.setHeaderID(inStockTaskInfoModel.getID());
            inStockTaskDetailsInfoModel.setERPVoucherNo(inStockTaskInfoModel.getErpVoucherNo());
            inStockTaskDetailsInfoModel.setVoucherType(inStockTaskInfoModel.getVoucherType());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(inStockTaskDetailsInfoModel));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetT_InTaskDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InTaskDetailListByHeaderIDADF, getString(R.string.Msg_GetT_InTaskDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_InTaskDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_InTaskDetailListByHeaderIDADF, params, null);
        }
    }

    /*
   扫描条码
    */
    void AnalysisetT_PalletDetailByBarCodeJson(String result){
        LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetT_ScanInStockModelADF,result);
        ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            stockInfoModels=returnMsgModel.getModelJson();
            if(stockInfoModels!=null && stockInfoModels.size()!=0) {
                for (StockInfo_Model stockInfoModel:stockInfoModels){
                    if(!CheckBarcode(stockInfoModel))
                        break;
                }
                InitFrm(stockInfoModels.get(0));
                BindListVIew (inStockTaskDetailsInfoModels);
                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
            }
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
            CommonUtil.setEditFocus(edtUpShelfScanBarcode);
        }
    }


    /*
    扫描库位
     */
    void AnalysisGetAreaModelADFJson(String result){
        LogUtil.WriteLog(UpShelfScanActivity.class, TAG_GetAreaModelADF,result);
        ReturnMsgModel<AreaInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            areaInfoModel=returnMsgModel.getModelJson();
          if(!TextUtils.isEmpty(edtUpShelfScanBarcode.getText().toString().trim())) {
              String code=edtUpShelfScanBarcode.getText().toString().trim();
              String StockCode=edtStockScan.getText().toString().trim();
              ScanBarcode(code,StockCode);
          }
            CommonUtil.setEditFocus(edtUpShelfScanBarcode);
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
            CommonUtil.setEditFocus(edtStockScan);
        }
    }


    /*
   提交收货
    */
    void AnalysisSaveT_InStockTaskDetailADFJson(String result){
        try {
            LogUtil.WriteLog(UpShelfScanActivity.class, TAG_SaveT_InStockTaskDetailADF,result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            MessageBox.Show(context, returnMsgModel.getMessage());
            if(returnMsgModel.getHeaderStatus().equals("S")) {
                stockInfoModels = new ArrayList<>();
                areaInfoModel =null;
                edtStockScan.setText("");
                edtUpShelfScanBarcode.setText("");
            }
            CommonUtil.setEditFocus(edtUpShelfScanBarcode);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void InitFrm(StockInfo_Model stockInfoModel){
        if(stockInfoModel!=null ){
            txtCompany.setText(stockInfoModel.getStrongHoldName());
            txtBatch.setText(stockInfoModel.getBatchNo());
            txtStatus.setText("");
            txtMaterialName.setText(stockInfoModel.getMaterialDesc());
            txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));

        }
    }


    boolean CheckBarcode(StockInfo_Model StockInfo_Model){
        if(StockInfo_Model!=null && inStockTaskDetailsInfoModels!=null){
            InStockTaskDetailsInfo_Model inStockTaskDetailsInfoModel=new InStockTaskDetailsInfo_Model(StockInfo_Model.getMaterialNo());
            int index=inStockTaskDetailsInfoModels.indexOf(inStockTaskDetailsInfoModel);
            if(index!=-1){
                if(inStockTaskDetailsInfoModels.get(index).getLstStockInfo()==null)
                    inStockTaskDetailsInfoModels.get(index).setLstStockInfo(new ArrayList<StockInfo_Model>());
                if(!inStockTaskDetailsInfoModels.get(index).getLstStockInfo().contains(StockInfo_Model))
                {
                    txtReferStock.setText(inStockTaskDetailsInfoModels.get(index).getAreaNo()); //推荐货位
                    inStockTaskDetailsInfoModels.get(index).setVoucherType(9996);//生成调拨单
                    inStockTaskDetailsInfoModels.get(index).setScanQty(inStockTaskDetailsInfoModels.get(index).getScanQty()+StockInfo_Model.getQty());
                    if(areaInfoModel!=null) {
                        inStockTaskDetailsInfoModels.get(index).setAreaID(areaInfoModel.getID());
                        inStockTaskDetailsInfoModels.get(index).setHouseID(areaInfoModel.getHouseID());
                        inStockTaskDetailsInfoModels.get(index).setWarehouseID(areaInfoModel.getWarehouseID());
                        inStockTaskDetailsInfoModels.get(index).setToErpAreaNo(areaInfoModel.getAreaNo());
                        inStockTaskDetailsInfoModels.get(index).setToErpWarehouse(areaInfoModel.getWarehouseNo());
                    }
                    txtUpShelfNum.setText(inStockTaskDetailsInfoModels.get(index).getRemainQty()+"");
                    txtUpShelfScanNum.setText(inStockTaskDetailsInfoModels.get(index).getScanQty()+"");
                    //StockInfo_Model.setAreaNo(edtStockScan.getText().toString().trim());
                    inStockTaskDetailsInfoModels.get(index).getLstStockInfo().add(0,StockInfo_Model);
                }else{
                    MessageBox.Show(context, getString(R.string.Error_BarcodeScaned)+"|"+StockInfo_Model.getSerialNo());
                    return false;
                }
            }else{
                MessageBox.Show(context, R.string.Error_BarcodeNotInList+"|"+StockInfo_Model.getSerialNo());
                return false;
            }
        }
        return true;
    }


    private void BindListVIew(ArrayList<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels) {
        UpShelfScanDetailAdapter receiptScanDetailAdapter=new UpShelfScanDetailAdapter(context,inStockTaskDetailsInfoModels);
        lsvUpShelfScan.setAdapter(receiptScanDetailAdapter);
        CommonUtil.setEditFocus(edtUpShelfScanBarcode);
    }


}
