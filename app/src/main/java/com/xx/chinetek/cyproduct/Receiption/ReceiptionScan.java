package com.xx.chinetek.cyproduct.Receiption;

import android.content.Context;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.product.Receiption.ReceiptionScanItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.cywms.OffShelf.OffshelfScan;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.WMS.OffShelf.OutStockTaskDetailsInfo_Model;
import com.xx.chinetek.model.WMS.OffShelf.OutStockTaskInfo_Model;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.WMS.Stock.StockInfo_Model;
import com.xx.chinetek.model.URLModel;
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
import java.util.List;
import java.util.Map;

import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;


@ContentView(R.layout.activity_product_receiption_scan)
public class ReceiptionScan extends BaseActivity {

    String TAG_GetT_OutTaskDetailListByHeaderIDADF="ReceiptionScan_GetT_OutTaskDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCodeADF="ReceiptionScan_GetT_PalletDetailByBarCodeADF";
    String TAG_SaveT_OutStockTaskDetailADF="ReceiptionScan_SaveT_OutStockTaskDetailADF";

    private final int RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF=101;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode=102;
    private final int RESULT_Msg_SaveT_OutStockTaskDetailADF=103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF:
                AnalysisGetT_OutTaskDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_PalletDetailByBarCode:
                AnalysisGetT_PalletDetailByNoADF((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_OutStockTaskDetailADF:
              //  AnalysisSaveT_OutStockTaskDetailADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLineInstockScanbarcode);
                break;
        }
    }


    Context context = ReceiptionScan.this;
    @ViewInject(R.id.lsvReceiptScan)
    ListView lsvReceiptScan;
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
    @ViewInject(R.id.txt_ReceQTY)
    TextView txtReceQTY;
    @ViewInject(R.id.txt_ReceScanQty)
    TextView txtReceScanQty;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVourcherNo;
    @ViewInject(R.id.edt_LineInstockScanbarcode)
    EditText edtLineInstockScanbarcode;

    OutStockTaskInfo_Model outStockTaskInfoModel;
    ArrayList<OutStockTaskDetailsInfo_Model> outStockTaskDetailsInfoModels;
    List<StockInfo_Model> stockInfoModels;//扫描条码
    ReceiptionScanItemAdapter receiptionScanItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        outStockTaskInfoModel=getIntent().getParcelableExtra("outStockTaskInfoModel");
        GetT_OutTaskDetailListByHeaderIDADF(outStockTaskInfoModel);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value =R.id.edt_LineInstockScanbarcode,type = View.OnKeyListener.class)
    private  boolean edtLineInstockScanbarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {    keyBoardCancle();
            String code=edtLineInstockScanbarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
        }
        return false;
    }

    /*
   线边收货明细获取
    */
    void GetT_OutTaskDetailListByHeaderIDADF(OutStockTaskInfo_Model outStockTaskInfoModel){
        if(outStockTaskInfoModel!=null) {
            txtVourcherNo.setText(outStockTaskInfoModel.getTaskNo());
            final OutStockTaskDetailsInfo_Model outStockTaskDetailsInfoModel = new OutStockTaskDetailsInfo_Model();
            outStockTaskDetailsInfoModel.setHeaderID(outStockTaskInfoModel.getID());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(outStockTaskDetailsInfoModel));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutTaskDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutTaskDetailListByHeaderIDADF, getString(R.string.Msg_QualityDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_OutTaskDetailListByHeaderIDADF, params, null);
        }
    }

    /*
    处理线边收货明细
     */
    void AnalysisGetT_OutTaskDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutTaskDetailListByHeaderIDADF,result);
        ReturnMsgModelList<OutStockTaskDetailsInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockTaskDetailsInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            outStockTaskDetailsInfoModels=returnMsgModel.getModelJson();
            BindListView(outStockTaskDetailsInfoModels);
          }else {
            ToastUtil.show(returnMsgModel.getMessage());
        }
    }

    /*
   扫描条码
    */
    void AnalysisGetT_PalletDetailByNoADF(String result){
        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF,result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                List<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
                if (barCodeInfos != null && barCodeInfos.size() != 0) {
                    for (BarCodeInfo barCodeInfo : barCodeInfos) {
//                        if (!CheckBarcode(barCodeInfo))
//                            break;
                    }
                   // InitFrm(barCodeInfos.get(0));
                }
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineInstockScanbarcode);
    }


    void BindListView(ArrayList<OutStockTaskDetailsInfo_Model> outStockTaskDetailsInfoModels)
    {
        receiptionScanItemAdapter=new ReceiptionScanItemAdapter(context,outStockTaskDetailsInfoModels);
        lsvReceiptScan.setAdapter(receiptionScanItemAdapter);
    }
}
