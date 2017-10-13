package com.xx.chinetek.cyproduct.LineStockOut;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.xx.chinetek.adapter.product.LineStockOut.LineStockOutReturnScanDetailAdapter;
import com.xx.chinetek.base.BaseActivity;
import com.xx.chinetek.base.BaseApplication;
import com.xx.chinetek.base.ToolBarTitle;
import com.xx.chinetek.cywms.R;
import com.xx.chinetek.model.Material.BarCodeInfo;
import com.xx.chinetek.model.Production.LineStockOut.LineStockOutReturnDetail_Model;
import com.xx.chinetek.model.Receiption.ReceiptDetail_Model;
import com.xx.chinetek.model.Receiption.Receipt_Model;
import com.xx.chinetek.model.ReturnMsgModelList;
import com.xx.chinetek.model.URLModel;
import com.xx.chinetek.util.Network.NetworkError;
import com.xx.chinetek.util.Network.RequestHandler;
import com.xx.chinetek.util.dialog.MessageBox;
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

import static com.xx.chinetek.cywms.R.id.edt_LineStockOutScanBarcode;
import static com.xx.chinetek.util.dialog.ToastUtil.show;
import static com.xx.chinetek.util.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_line_stock_out_return_scan)
public class LineStockOutReturnScan extends BaseActivity {

    String TAG_GetT_InStockDetailListByHeaderIDADF="ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCodeADF="ReceiptionScan_GetT_PalletDetailByBarCodeADF";

    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF=101;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode=102;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
                AnalysisGetT_InStockDetailListJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_PalletDetailByBarCode:
                AnalysisGetT_PalletDetailByNoADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
                break;
        }
    }

    @ViewInject(R.id.lsv_LineStockOutReturn)
    ListView lsvLineStockOutReturn;
    @ViewInject(edt_LineStockOutScanBarcode)
    EditText edtLineStockOutScanBarcode;
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

    Context context=LineStockOutReturnScan.this;
    LineStockOutReturnScanDetailAdapter lineStockOutReturnScanDetailAdapter;
    ArrayList<LineStockOutReturnDetail_Model> lineStockOutReturnDetailModels;
    ArrayList<BarCodeInfo> barCodeInfos=null;
    Receipt_Model receiptModel=null;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context=context;
        BaseApplication.toolBarTitle=new ToolBarTitle(getString(R.string.LineStockOutReturnBillChoice),true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        receiptModel=getIntent().getParcelableExtra("receiptModel");
        this.barCodeInfos=getIntent().getParcelableArrayListExtra("barCodeInfo");
        GetReceiptDetail(receiptModel);

    }

    @Event(value = edt_LineStockOutScanBarcode,type = View.OnKeyListener.class)
    private  boolean edt_LineStockOutScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtLineStockOutScanBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            LogUtil.WriteLog(LineStockOutReturnScan.class, TAG_GetT_PalletDetailByBarCodeADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetPalletDetailByBarCodeForStockOut, params, null);
        }
        return false;
    }

    /*
    获取收货明细
     */
    void GetReceiptDetail(Receipt_Model receiptModel){
        if(receiptModel!=null) {
            txtVoucherNo.setText(receiptModel.getErpVoucherNo());
            final ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model();
            receiptDetailModel.setHeaderID(receiptModel.getID());
            receiptDetailModel.setErpVoucherNo(receiptModel.getErpVoucherNo());
            receiptDetailModel.setVoucherType(receiptModel.getVoucherType());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("ModelDetailJson", parseModelToJson(receiptDetailModel));
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(LineStockOutReturnScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockDetailListByHeaderIDADF, getString(R.string.Msg_GetT_LineInStockDetail), context, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_InStockDetailListByHeaderIDADF, params, null);
        }
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
   处理收货明细
    */
    void AnalysisGetT_InStockDetailListJson(String result){
        LogUtil.WriteLog(LineStockOutReturnScan.class, TAG_GetT_InStockDetailListByHeaderIDADF,result);
        try {
            ReturnMsgModelList<LineStockOutReturnDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<LineStockOutReturnDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                lineStockOutReturnDetailModels = returnMsgModel.getModelJson();
                //自动确认扫描箱号
                BindListVIew(lineStockOutReturnDetailModels);
                if (barCodeInfos != null) {
                    Bindbarcode(barCodeInfos);
                }
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex) {
            MessageBox.Show(context,ex.getMessage());
        }
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }

    /*
   扫描条码
    */
    void AnalysisGetT_PalletDetailByNoADF(String result){
        LogUtil.WriteLog(LineStockOutReturnScan.class, TAG_GetT_PalletDetailByBarCodeADF,result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ArrayList<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
                Bindbarcode(barCodeInfos);
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
        }
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }


    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
        if (barCodeInfos != null && barCodeInfos.size() != 0) {
            try {
                String MaterialNo=barCodeInfos.get(0).getMaterialNo();
                String BatchNo=barCodeInfos.get(0).getBatchNo();
                Float SumQty=0f;
                for (BarCodeInfo barcodinfo:barCodeInfos) {
                    SumQty= ArithUtil.add(SumQty,barcodinfo.getQty());
                }
                final Float sumQty=SumQty;
                LineStockOutReturnDetail_Model templineStockIn=new LineStockOutReturnDetail_Model(MaterialNo,BatchNo);
                final int index=lineStockOutReturnDetailModels.indexOf(templineStockIn);
                if(index!=-1){
                    if(lineStockOutReturnDetailModels.get(index).getLstBarCode()==null)
                        lineStockOutReturnDetailModels.get(index).setLstBarCode(new ArrayList<BarCodeInfo>());
                    if(lineStockOutReturnDetailModels.get(index).getLstBarCode().indexOf(barCodeInfos.get(0))!=-1){
                        //MessageBox.Show(context,getString(R.string.Error_Barcode_hasScan));
                        new AlertDialog.Builder(context) .setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO 自动生成的方法
                                        lineStockOutReturnDetailModels.get(index).getLstBarCode().removeAll(barCodeInfos);
                                        lineStockOutReturnDetailModels.get(index).setScanQty(ArithUtil.sub(lineStockOutReturnDetailModels.get(index).getScanQty(),sumQty));
                                        InitFrm(barCodeInfos.get(0));
                                        BindListVIew(lineStockOutReturnDetailModels);
                                    }
                                }).setNegativeButton("取消", null).show();
                        return;
                    }
                    lineStockOutReturnDetailModels.get(index).setScanQty(ArithUtil.add(lineStockOutReturnDetailModels.get(index).getScanQty(),SumQty));
                    lineStockOutReturnDetailModels.get(index).getLstBarCode().addAll(0,barCodeInfos);
                }else{
                   MessageBox.Show(context,getString(R.string.Error_BarcodeNotInList));
                    return;
                }
                InitFrm(barCodeInfos.get(0));
                BindListVIew(lineStockOutReturnDetailModels);
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
            }

        }
    }


    void InitFrm(BarCodeInfo barCodeInfo){
        if(barCodeInfo!=null ){
            txtCompany.setText(barCodeInfo.getStrongHoldName());
            txtBatch.setText(barCodeInfo.getBatchNo());
            txtStatus.setText(barCodeInfo.getStrStatus());
            txtMaterialName.setText(barCodeInfo.getMaterialDesc());
            txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
        }
        CommonUtil.setEditFocus(edtLineStockOutScanBarcode);
    }

    private void BindListVIew(ArrayList<LineStockOutReturnDetail_Model> lineStockOutReturnDetailModels) {
        lineStockOutReturnScanDetailAdapter=new LineStockOutReturnScanDetailAdapter(context,lineStockOutReturnDetailModels);
        lsvLineStockOutReturn.setAdapter(lineStockOutReturnScanDetailAdapter);
    }
}
