package com.xx.chinetek.cywms.Intentory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.xx.chinetek.adapter.wms.Intentory.InventoryScanItemAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Base_Model;
import com.xx.chinetek.model.ReturnMsgModel;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.model.WMS.Inventory.Barcode_Model;
import com.xx.chinetek.model.WMS.Inventory.Check_Model;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
import com.xx.chinetek.util.dialog.ToastUtil;
import com.xx.chinetek.util.function.ArithUtil;
import com.xx.chinetek.util.function.CommonUtil;
import com.xx.chinetek.util.function.DoubleClickCheck;
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

@ContentView(R.layout.activity_intentory_finc)
public class IntentoryFinc extends BaseActivity {

    String TAG_GetMinDetailbyCheckno="IntentoryFinc_GetMinDetailbyCheckno";
    String TAG_GetMinBarocde="IntentoryFinc_GetMinBarocde";
    String TAG_SummitMin="IntentoryFinc_SummitMin";

    private final int RESULT_GetMinDetailbyCheckno=101;
    private final int RESULT_GetMinBarocde=102;
    private final int RESULT_SummitMin=103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetMinDetailbyCheckno:
                AnalysisGetMinDetailChecknoJson((String) msg.obj);
                break;
            case RESULT_GetMinBarocde:
                AnalysisGetMinBarocdeJson((String) msg.obj);
                break;
            case RESULT_SummitMin:
                AnalysisSummitMinJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtInvScanBarcode);
                break;
        }
    }



    @ViewInject(R.id.lsv_IntentoryScan)
    ListView lsvIntentoryScan;
    @ViewInject(R.id.txt_VourcherNo)
    TextView txtVourcherNo;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_StockNum)
    TextView txtStockNum;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.edt_InvScanBarcode)
    EditText edtInvScanBarcode;

   Context  context=IntentoryFinc.this;
    Check_Model checkModel;
    ArrayList<Barcode_Model> barcodeModels;
    InventoryScanItemAdapter inventoryScanItemAdapter;
    int model=-1;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Intentory_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        checkModel=getIntent().getParcelableExtra("check_model");
        model=getIntent().getIntExtra("model",0);
        GetMinDetailbyCheckno(checkModel);
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

            new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage(getString(R.string.Message_submit_finc))
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            final Map<String, String> params = new HashMap<String, String>();
                            params.put("checkno",checkModel.getCHECKNO() );
                            LogUtil.WriteLog(IntentoryFinc.class, TAG_SummitMin, checkModel.getCHECKNO());
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SummitMin, getString(R.string.Msg_SaveT_InventoryADF), context, mHandler, RESULT_SummitMin, null,  URLModel.GetURL().SummitMin, params, null);
                        }
                    }).setNegativeButton("否",null).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Event(value =R.id.lsv_IntentoryScan,type =AdapterView.OnItemClickListener.class )
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Barcode_Model barcodeModel=(Barcode_Model)inventoryScanItemAdapter.getItem(position);
        Intent intent=new Intent(context,IntentoryDetial.class);
        intent.putExtra("id", barcodeModel.getId());
        intent.putExtra("model", model);
        startActivityLeft(intent);
    }

    @Event(value =R.id.edt_InvScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtInvScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode = edtInvScanBarcode.getText().toString().trim();
            if (!barcode.equals("")) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("barcode", barcode);
                params.put("checkno", checkModel.getCHECKNO());
                String para = (new JSONObject(params)).toString();
                LogUtil.WriteLog(IntentoryFinc.class, TAG_GetMinBarocde, para);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetMinBarocde, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetMinBarocde, null, URLModel.GetURL().GetMinBarocde, params, null);
            }
        }
        return false;
    }

    void  GetMinDetailbyCheckno(Check_Model checkModel){
        if(checkModel!=null) {
            txtVourcherNo.setText(checkModel.getCHECKNO());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("checkno", checkModel.getCHECKNO());
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(IntentoryFinc.class, TAG_GetMinDetailbyCheckno, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetMinDetailbyCheckno, getString(R.string.Msg_Inventory_Load), context, mHandler, RESULT_GetMinDetailbyCheckno, null,  URLModel.GetURL().GetMinDetail, params, null);
        }
    }

    void AnalysisGetMinDetailChecknoJson(String result){
        try {
            LogUtil.WriteLog(IntentoryFinc.class, TAG_GetMinDetailbyCheckno, result);
            ReturnMsgModelList<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Barcode_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                barcodeModels=returnMsgModel.getModelJson();
                inventoryScanItemAdapter=new InventoryScanItemAdapter(context,model,barcodeModels);
                lsvIntentoryScan.setAdapter(inventoryScanItemAdapter);
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }

        CommonUtil.setEditFocus(edtInvScanBarcode);
    }

    void AnalysisSummitMinJson(String result){
        try {
            LogUtil.WriteLog(IntentoryFinc.class, TAG_SummitMin, result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            MessageBox.Show(context, returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                closeActiviry();
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }

        CommonUtil.setEditFocus(edtInvScanBarcode);
    }

    void AnalysisGetMinBarocdeJson(String result){
        try {
            LogUtil.WriteLog(IntentoryFinc.class, TAG_GetMinDetailbyCheckno, result);
            ReturnMsgModel<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Barcode_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                Barcode_Model barcodemodel=returnMsgModel.getModelJson();
                if(barcodemodel!=null){
                    txtCompany.setText(barcodemodel.getStrongHoldName());
                    txtBatch.setText(barcodemodel.getBatchNo());
                    txtStatus.setText("");
                    txtMaterialName.setText(barcodemodel.getMaterialDesc());
                    txtStockNum.setText(barcodemodel.getQty()+"");
                    int index=barcodeModels.indexOf(barcodemodel);
                    if(index==-1){
                        MessageBox.Show(context,getString(R.string.Error_Inventory_Nomaterial));
                        CommonUtil.setEditFocus(edtInvScanBarcode);
                        return;
                    }
                    Float scanQty= ArithUtil.add((barcodeModels.get(index).getSQTY()==null?0f:
                            barcodeModels.get(index).getSQTY()),barcodemodel.getQty());
                    barcodeModels.get(index).setSQTY(scanQty);
                }
                inventoryScanItemAdapter=new InventoryScanItemAdapter(context,model,barcodeModels);
                lsvIntentoryScan.setAdapter(inventoryScanItemAdapter);
            } else {
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context, ex.getMessage());
        }

        CommonUtil.setEditFocus(edtInvScanBarcode);
    }

}
