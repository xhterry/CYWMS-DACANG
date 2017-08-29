package com.xx.chinetek.cywms.Qc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.xx.chinetek.adapter.wms.QC.QCInStockItemAdapter;
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


@ContentView(R.layout.activity_qcin_stock)
public class QCInStock extends BaseActivity {

    String TAG_CreateQualityForStock="QCInStock_TAG_CreateQualityForStock";
    String TAG_ScanQualityStockADF="QCInStock_TAG_ScanQualityStockADF";
    private final int RESULT_CreateQualityForStock=101;
    private final int RESULT_ScanQualityStockADF=102;
    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_CreateQualityForStock:
                AnalysisCreateQualityForStockJson((String) msg.obj);
                break;
            case RESULT_ScanQualityStockADF:
                AnalysisScanQualityStockADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtQCInstockBarcode);
                break;
        }
    }

    Context context=QCInStock.this;
    @ViewInject(R.id.edt_QCInstockBarcode)
    EditText edtQCInstockBarcode;
    @ViewInject(R.id.lsv_QCInstock)
    ListView lsvQCInstock;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Stock)
    TextView txtStock;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;

    ArrayList<StockInfo_Model> stockInfoModels;
    QCInStockItemAdapter qcInStockItemAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context=context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.InStockQC), true);
        x.view().inject(this);
        stockInfoModels=new ArrayList<>();
        BaseApplication.isCloseActivity=false;
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
            if (stockInfoModels != null && stockInfoModels.size() > 0) {
                final Map<String, String> params = new HashMap<String, String>();
                String ModelJson = GsonUtil.parseModelToJson(stockInfoModels);
                String UserJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
                params.put("UserJson", UserJson);
                params.put("ModelJson", ModelJson);
                LogUtil.WriteLog(QCInStock.class, TAG_CreateQualityForStock, ModelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_CreateQualityForStock, getString(R.string.Msg_SaveT_QuanlitySampADF), context, mHandler, RESULT_CreateQualityForStock, null, URLModel.GetURL().CreateQualityForStock, params, null);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Event(value = R.id.edt_QCInstockBarcode,type = View.OnKeyListener.class)
    private  boolean edtQCInstockBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String code=edtQCInstockBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(QCScan.class, TAG_ScanQualityStockADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_ScanQualityStockADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_ScanQualityStockADF, null, URLModel.GetURL().ScanQualityStockADF, params, null);
        }
        return false;
    }

    void  AnalysisCreateQualityForStockJson(String result){
        try {
            LogUtil.WriteLog(QCInStock.class, TAG_CreateQualityForStock,result);
            final ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {}.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")) {
                new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage(returnMsgModel.getMessage())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自动生成的方法
                                Intent intent = new Intent(context, QCMaterialChoice.class);
                                String ErpVourcherNo = returnMsgModel.getMaterialDoc();
                                intent.putExtra("ErpVourcherNo", ErpVourcherNo);
                                startActivityLeft(intent);
                                closeActiviry();
                            }
                        }).show();
            }
            else{
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void AnalysisScanQualityStockADFJson(String result){
        try {
            LogUtil.WriteLog(QCInStock.class, TAG_ScanQualityStockADF, result);
            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                StockInfo_Model stockInfoModel = returnMsgModel.getModelJson();
                if(stockInfoModel!=null){
                    txtCompany.setText(stockInfoModel.getStrongHoldName());
                    txtBatch.setText(stockInfoModel.getBatchNo());
                    txtStatus.setText(stockInfoModel.getStrStatus());
                    txtMaterialName.setText(stockInfoModel.getMaterialDesc());
                    txtStock.setText(stockInfoModel.getQty().toString());
                    txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
                    if(stockInfoModels==null)
                        stockInfoModels=new ArrayList<>();
                    chheckCode(stockInfoModel);
                  }

            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(edtQCInstockBarcode);
    }

    void chheckCode(StockInfo_Model stockInfoModel) {
        boolean hasSameMaterialNo = false;
        for (int i = 0; i < stockInfoModels.size(); i++) {
            String MaterialNo = stockInfoModels.get(i).getMaterialNo();
            String StrongHoldCode = stockInfoModels.get(i).getStrongHoldCode();
            String BatchNo = stockInfoModels.get(i).getBatchNo();
            int AreaID = stockInfoModels.get(i).getAreaID();
            //物料、据点、批次相同,库位不同
            if (MaterialNo.equals(stockInfoModel.getMaterialNo()) &&
                    StrongHoldCode.equals(stockInfoModel.getStrongHoldCode()) &&
                    BatchNo.equals(stockInfoModel.getBatchNo()) && AreaID == stockInfoModel.getAreaID()) {
                hasSameMaterialNo = true;
                break;
            }
        }
        if (hasSameMaterialNo) {
            MessageBox.Show(context, getString(R.string.Error_StockhastMatch));
            return;
        }

        stockInfoModels.add(0, stockInfoModel);
        qcInStockItemAdapter=new QCInStockItemAdapter(context,stockInfoModels);
        lsvQCInstock.setAdapter(qcInStockItemAdapter);
    }

}
